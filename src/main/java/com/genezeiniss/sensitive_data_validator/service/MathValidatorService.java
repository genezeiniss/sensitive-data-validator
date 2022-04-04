package com.genezeiniss.sensitive_data_validator.service;

import com.genezeiniss.sensitive_data_validator.enums.Parser;
import org.mariuszgromada.math.mxparser.Expression;

public class MathValidatorService {

    public static boolean validate(String detection, Parser parser, Expression expression) {

        String operatee = cleanFormat(detection);

        for (int i = 0; i < operatee.length(); i++) {
            expression.setArgumentValue(
                    String.format("i%d", i), Parser.NUMERIC == parser ?
                            Integer.parseInt(String.valueOf(operatee.charAt(i))) : operatee.charAt(i));
        }
        expression.setArgumentValue("length", operatee.length());

        return expression.calculate() == 1.0;
    }

    private static String cleanFormat(String text) {
        return text.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }
}
