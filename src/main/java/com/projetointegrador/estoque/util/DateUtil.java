package com.projetointegrador.estoque.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    // Método para formatar LocalDate no formato brasileiro (dd/MM/yyyy)
    public static String formatarData(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
    }

    // Método para converter uma string no formato dd/MM/yyyy para LocalDate
    public static LocalDate converterParaLocalDate(String dataString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dataString, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de data inválido. Use o formato dd/MM/yyyy.", e);
        }
    }


    // Método para verificar se a data é futura
    public static boolean isDataFutura(LocalDate data) {
        return data.isAfter(LocalDate.now());
    }
}
