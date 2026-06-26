package com.belezaagil.utils;

import com.belezaagil.exception.BusinessException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoUtils {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm");

    public static List<String> gerarHorariosOcupados(
            String horarioInicio, int duracaoMin, int intervaloMin) {

        LocalTime inicio;
        try {
            inicio = LocalTime.parse(horarioInicio, FMT);
        } catch (DateTimeParseException e) {
            throw new BusinessException("Formato de horário inválido: " + horarioInicio);
        }

        int slots = (int) Math.ceil((double) duracaoMin / intervaloMin);
        List<String> horarios = new ArrayList<>();
        for (int i = 0; i < slots; i++) {
            horarios.add(inicio.plusMinutes((long) i * intervaloMin).format(FMT));
        }
        return horarios;
    }
}