package com.invest.investment.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ServiceSort {

    private String direction;
    private String property;

    public ServiceSort(String direction, String property) {
        this.direction = direction;
        this.property = property;
    }
}
