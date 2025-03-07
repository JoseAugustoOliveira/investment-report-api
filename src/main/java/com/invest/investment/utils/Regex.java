package com.invest.investment.utils;

public interface Regex {

    String CPF = "(\\d{11})?";
    String CNPJ = "(\\d{14})?";
    String YEAR = "(\\d{4})?";
    String EMAIL_REGEX = "^[\\w-_.+]*@([\\w-+]+\\.)+[\\w]+[\\w]$";
    String ONLY_NUMBER = "\\d+";
}
