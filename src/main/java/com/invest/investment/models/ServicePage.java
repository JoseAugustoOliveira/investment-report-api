package com.invest.investment.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ServicePage<T> {

    private long total;
    private List<T> content;
    private ServicePageable pageable;

    public ServicePage(List<T> content, ServicePageable pageable, long total) {
        this.total = total;
        this.content = content;
        this.pageable = pageable;
    }
}
