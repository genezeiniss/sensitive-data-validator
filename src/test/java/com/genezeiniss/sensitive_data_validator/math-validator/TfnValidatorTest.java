package com.genezeiniss.sensitive_data_validator.math;

import com.genezeiniss.sensitive_data_validator.domain.MathValidator;
import com.genezeiniss.sensitive_data_validator.domain.MathValidatorArgument;
import com.genezeiniss.sensitive_data_validator.enums.Parser;
import com.genezeiniss.sensitive_data_validator.service.MathValidatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TfnValidatorTest {

    private static Expression expression;

    @BeforeAll
    static void init() {

        // configuration
        MathValidator mathValidator = new MathValidator();
        mathValidator.setParser(Parser.NUMERIC);
        mathValidator.setMaxOperateeLength(9);
        mathValidator.setArguments(Collections.singletonList(
                MathValidatorArgument.create("wighted_sum", "(i0 * 1 + i1 * 4 + i2 * 3 + i3 * 7 + i4 * 5 + i5 * 8 + i6 * 6 + i7 * 9 + i8 * 10)")));
        mathValidator.setExpression("wighted_sum # 11 == 0.0");

        // initiation
        expression = new Expression(mathValidator.getExpression());

        List<Argument> arguments = new ArrayList<>();
        for (int i = 0; i < mathValidator.getMaxOperateeLength(); i++) {
            arguments.add(new Argument(String.format("i%d", i)));
        }
        arguments.add(new Argument("length"));
        expression.addArguments(arguments.toArray(new Argument[0]));

        mathValidator.getArguments().forEach(
                argument -> expression.addArguments(new Argument(argument.getName(), argument.getExpression(), arguments.toArray(new Argument[0]))));
    }

    @ParameterizedTest
    @ValueSource(strings = {"876543210", "123-456-782", "876543210", "123-456-782", "876543210", "123-456-782"})
    public void test(String detection) {
        assertTrue(MathValidatorService.validate(detection, Parser.NUMERIC, expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {"867 543 210", "123-456-783"})
    public void test_invalid_tfn(String detection) {
        assertFalse(MathValidatorService.validate(detection, Parser.NUMERIC, expression));
    }
}
