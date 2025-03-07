package com.invest.investment.controllers.v1;

import com.invest.investment.models.ServicePage;
import com.invest.investment.models.requests.StockReportRequest;
import com.invest.investment.models.requests.StockRequest;
import com.invest.investment.models.responses.StockPageableResponse;
import com.invest.investment.services.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Stock")
@RequestMapping("/v1/stock")
public class StockController {

    private final StockService stockService;

    @PostMapping("/buy")
    @Operation(summary = "Register stocks to account")
    public ResponseEntity<String> insertStock(@Valid @RequestBody StockRequest request) {
        stockService.insertStockInAccount(request);
        return ResponseEntity.ok().body("Stock inserted successfully " + request.stock());
    }

    @GetMapping("/download-report")
    @Operation(summary = "Download spreadsheet report with buy year")
    public ResponseEntity<Resource> getReport(@Valid StockReportRequest request) {
        File file = null;
        try {
            file = stockService.generateReport(request);
            var resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
            var header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (file != null) {
                    FileUtils.delete(file);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @GetMapping("/{cpf}")
    @Operation(summary = "Get Stocks info pageable")
    public ServicePage<StockPageableResponse> listStocks(@PathVariable String cpf,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "buyDate") String sortProperty,
                                                         @RequestParam(defaultValue = "DESC") String sortDirection) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
        return stockService.listStocks(cpf, pageable);
    }


        // TODO: Ajustar a lógica para remover a ação, fazendo uma soma da quantidade
    @PostMapping("/sale")
    public ResponseEntity<String> removeStock() {
        return ResponseEntity.ok().body("Stock removed successfully");
    }

}
