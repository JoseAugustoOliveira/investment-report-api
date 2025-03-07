package com.invest.investment.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ServicePageable {

    private int page;
    private int size;
    private ServiceSort sort;

    public ServicePageable(int page, int size, String direction, String property) {
        this.page = page;
        this.size = size;
        this.sort = new ServiceSort(direction, property);
    }
}
