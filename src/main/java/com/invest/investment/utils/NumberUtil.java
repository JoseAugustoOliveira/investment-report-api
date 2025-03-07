package com.invest.investment.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtil {

    public static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
}
