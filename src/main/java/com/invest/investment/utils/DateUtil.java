package com.invest.investment.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    public static final DateTimeFormatter isoDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter isoDateFormatReport = DateTimeFormatter.ofPattern("dd_MM_yyyy");
}
