package com.projetointegrador.estoque.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String formatarData(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
    }

    public static boolean isDataFutura(LocalDate data) {
        return data.isAfter(LocalDate.now());
    }
}
