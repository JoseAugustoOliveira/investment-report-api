package com.invest.investment.components;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ReportGenerator<T> {
    File generate(List<T> items) throws IOException;
}
