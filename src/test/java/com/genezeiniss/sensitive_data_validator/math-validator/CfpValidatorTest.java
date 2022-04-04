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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CfpValidatorTest {

    private static Expression expression;

    @BeforeAll
    static void init() {

        // configuration
        MathValidator mathValidator = new MathValidator();
        mathValidator.setParser(Parser.NUMERIC);
        mathValidator.setMaxOperateeLength(11);
        mathValidator.setArguments(Arrays.asList(
                MathValidatorArgument.create("mod_result_1", "(i0 * 10 + i1 * 9 + i2 * 8 + i3 * 7 + i4 * 6 + i5 * 5 + i6 * 4 + i7 * 3 + i8 * 2) # 11"),
                MathValidatorArgument.create("mod_result_2", "(i0 * 11 + i1 * 10 + i2 * 9 + i3 * 8 + i4 * 7 + i5 * 6 + i6 * 5 + i7 * 4 + i8 * 3 + i9 * 2) # 11")));
        mathValidator.setExpression("if(mod_result_1 < 2, i9 == 0, i9 == (11 - mod_result_1)) && if(mod_result_2 < 2, i10 == 0, i10 == (11 - mod_result_2))");

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
    @ValueSource(strings = {"111.444.777-35", "63130335315", "780.770.484-55", "781106122 88"})
    public void test_cpf(String detection) {
        assertTrue(MathValidatorService.validate(detection, Parser.NUMERIC, expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {"78110612280", "78110612298"})
    public void test_invalid_cpf(String detection) {
        assertFalse(MathValidatorService.validate(detection, Parser.NUMERIC, expression));
    }
}
