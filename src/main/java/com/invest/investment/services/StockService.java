package com.invest.investment.services;

import com.invest.investment.components.AuditComponent;
import com.invest.investment.components.CalculateStocksComponent;
import com.invest.investment.components.StockQuotesComponent;
import com.invest.investment.components.excel.ConsumptionStockByYearExcelReport;
import com.invest.investment.components.excel.ConsumptionStockTotalExcelReport;
import com.invest.investment.entites.postgres.Stock;
import com.invest.investment.enums.Operation;
import com.invest.investment.enums.ReportType;
import com.invest.investment.exceptions.EntityNotFoundException;
import com.invest.investment.models.ServicePage;
import com.invest.investment.models.ServicePageable;
import com.invest.investment.models.StockDto;
import com.invest.investment.models.requests.StockReportRequest;
import com.invest.investment.models.requests.StockRequest;
import com.invest.investment.models.responses.StockPageableResponse;
import com.invest.investment.models.responses.StockReportByYear;
import com.invest.investment.models.responses.StockReportTotal;
import com.invest.investment.repositories.postgres.AccountRepository;
import com.invest.investment.repositories.postgres.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    @Value("${api.brapi.token}")
    private String token;

    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;

    private final AuditComponent auditComponent;
    private final StockQuotesComponent stockQuotesComponent;
    private final CalculateStocksComponent calculateStocksComponent;

    private final ConsumptionStockByYearExcelReport consumptionStockByYearExcelReport = new ConsumptionStockByYearExcelReport();
    private final ConsumptionStockTotalExcelReport consumptionStockTotalExcelReport = new ConsumptionStockTotalExcelReport();

    private final static int FIRST_DATA = 1;
    private final static int LAST_MONTH = 12;
    private final static int LAST_DAY = 31;
    private final static String UNSORTED = "UNSORTED";

    public void insertStockInAccount(StockRequest request) {
        var existsAccount = accountRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Account not found to insert stock %s with CPF %s", request.stock(), request.cpf())
                ));

        // TODO: criar uma regra para somar tds acoes... obs pode ser salvo apenas no relatório/excel e ser adicionado em uma tabela de total e a soma das ações...
        var existsStock = stockRepository.findByStockId(request.stock());

        Stock newStockInserted = Stock.builder()
                .stockId(request.stock())
                .description(request.description())
                .quantity(request.quantity())
                .buyPrice(request.buyPrice())
                .account(existsAccount)
                .build();

        stockRepository.save(newStockInserted);
        auditComponent.saveAudit(request.cpf(), Operation.REGISTER_STOCKS);
        log.info("Stock registered with success {} in quantity {}", request.stock(), request.quantity());
    }

    public File generateReport(StockReportRequest request) throws IOException {
        log.info("Generating stock account report. Request: {}", request);

        auditComponent.saveAudit(request.cpf(), Operation.DOWNLOAD_SPREADSHEET);

        if (request.type().equals(ReportType.BY_YEAR)) {
            return getFileTypeByYear(request);
        }
        return getFileTypeTotal(request);
    }

    public ServicePage<StockPageableResponse> listStocks(String cpf, Pageable pageable) {
        Page<Stock> stockPage = stockRepository.findByAccount_Cpf(cpf, pageable);

        List<StockPageableResponse> stockResponses = stockPage.getContent().stream()
                .map(stock -> new StockPageableResponse(
                        stock.getStockId(),
                        stock.getQuantity(),
                        stock.getBuyPrice(),
                        null,
                        null,
                        calculateStocksComponent.getPageTotalValue(stockPage),
                        stock.getSalePrice(),
                        stock.getGain(),
                        stock.getBuyDate().atZone(ZoneId.systemDefault()).toLocalDate(),
                        stock.getSaleDate() != null ? stock.getSaleDate().atZone(ZoneId.systemDefault()).toLocalDate() : null,
                        null,
                        null
                ))
                .toList();

        ServicePageable servicePageable = new ServicePageable(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().isSorted() ? pageable.getSort().toString() : UNSORTED,
                pageable.getSort().isSorted() ? pageable.getSort().toString() : UNSORTED
        );

        return new ServicePage<>(stockResponses, servicePageable, stockPage.getTotalElements());

    }

    private File getFileTypeTotal(StockReportRequest request) throws IOException {
        var stocks = stockRepository.findByAccount_Cpf(request.cpf());
        List<StockReportTotal> stockReport = calculateStocksComponent.getStockTotals(stocks, token);
        return consumptionStockTotalExcelReport.generate(stockReport);
    }

    private File getFileTypeByYear(StockReportRequest request) throws IOException {
        var firstDateOfYear = LocalDate.of(request.year(), FIRST_DATA, FIRST_DATA);
        var lastDateOfYear = LocalDate.of(request.year(), LAST_MONTH, LAST_DAY);

        var startInstant = firstDateOfYear.atStartOfDay(ZoneId.systemDefault()).toInstant();
        var endInstant = lastDateOfYear.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();
        var stockData = stockRepository.findByCpfAndCreatedAtBetween(
                request.cpf(), startInstant, endInstant);

        List<StockReportByYear> stockReport = getStockByYearReportList(stockData);
        return consumptionStockByYearExcelReport.generate(stockReport);
    }

    private List<StockReportByYear> getStockByYearReportList(List<Stock> stockData) {
        Set<String> stockIds = stockData.stream()
                .map(Stock::getStockId)
                .collect(Collectors.toSet());

        Map<String, StockDto> stockQuotes = stockQuotesComponent.getStockDtoMap(stockIds, token);

        return stockData.stream()
                .map(stock -> {
                    StockDto stockDataFromApi = stockQuotes.get(stock.getStockId());
                    BigDecimal valueOfDay = stockDataFromApi.regularMarketPrice();
                    String range = stockDataFromApi.fiftyTwoWeekRange();

                    return StockReportByYear.builder()
                            .stock(stock.getStockId())
                            .quantity(stock.getQuantity())
                            .buyPrice(stock.getBuyPrice())
                            .valueOfDay(valueOfDay)
                            .fiftyTwoWeekRange(range)
                            .salePrice(stock.getSalePrice() != null ? stock.getSalePrice() : BigDecimal.ZERO)
                            .totalBuy(calculateStocksComponent.calculateQuantityAndBuyPrice(
                                    stock.getBuyPrice(), Math.toIntExact(stock.getQuantity())))
                            .gain(calculateStocksComponent.getGainPerBuyEachStock(
                                    stock.getBuyPrice(), valueOfDay, stock.getQuantity()))
                            .buyDate(stock.getBuyDate().atZone(ZoneId.systemDefault()).toLocalDate())
                            .saleDate(stock.getSaleDate() != null
                                    ? stock.getSaleDate().atZone(ZoneId.systemDefault()).toLocalDate()
                                    : null)
                            .build();
                }).toList();
    }
}
