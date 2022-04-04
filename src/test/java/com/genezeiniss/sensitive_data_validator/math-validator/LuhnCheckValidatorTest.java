package com.genezeiniss.sensitive_data_validator.math;

import com.genezeiniss.sensitive_data_validator.domain.MathValidator;
import com.genezeiniss.sensitive_data_validator.domain.MathValidatorArgument;
import com.genezeiniss.sensitive_data_validator.enums.Parser;
import com.genezeiniss.sensitive_data_validator.service.MathValidatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LuhnCheckValidatorTest {

    private static Expression expression;

    @BeforeAll
    static void init() {

        // configuration
        MathValidator mathValidator = new MathValidator();
        mathValidator.setParser(Parser.NUMERIC);
        mathValidator.setMaxOperateeLength(16);
        mathValidator.setArguments(Arrays.asList(
                MathValidatorArgument.create("base_9", "10 - (i0 +" +
                        "if (i1 * 2 < 10, i1 * 2, floor(i1 * 2 / 10) + (i1 * 2) # 10) + i2 + " +
                        "if (i3 * 2 < 10, i3 * 2, floor(i3 * 2 / 10) + (i3 * 2) # 10) + i4 + " +
                        "if (i5 * 2 < 10, i5 * 2, floor(i5 * 2 / 10) + (i5 * 2) # 10) + i6 + " +
                        "if (i7 * 2 < 10, i7 * 2, floor(i7 * 2 / 10) + (i7 * 2) # 10)) # 10 == i8"),
                MathValidatorArgument.create("base_14", "10 - (" +
                        "if (i0 * 2 < 10, i0 * 2, floor(i0 * 2 / 10) + (i0 * 2) # 10) + i1 + " +
                        "if (i2 * 2 < 10, i2 * 2, floor(i2 * 2 / 10) + (i2 * 2) # 10) + i3 + " +
                        "if (i4 * 2 < 10, i4 * 2, floor(i4 * 2 / 10) + (i4 * 2) # 10) + i5 + " +
                        "if (i6 * 2 < 10, i6 * 2, floor(i6 * 2 / 10) + (i6 * 2) # 10) + i7 + " +
                        "if (i8 * 2 < 10, i8 * 2, floor(i8 * 2 / 10) + (i8 * 2) # 10) + i9 + " +
                        "if (i10 * 2 < 10, i10 * 2, floor(i10 * 2 / 10) + (i10 * 2) # 10) + i11 + " +
                        "if (i12 * 2 < 10, i12 * 2, floor(i12 * 2 / 10) + (i12 * 2) # 10)) # 10 == i13"),
                MathValidatorArgument.create("base_15", "10 - (i0 +" +
                        "if (i1 * 2 < 10, i1 * 2, floor(i1 * 2 / 10) + (i1 * 2) # 10) + i2 + " +
                        "if (i3 * 2 < 10, i3 * 2, floor(i3 * 2 / 10) + (i3 * 2) # 10) + i4 + " +
                        "if (i5 * 2 < 10, i5 * 2, floor(i5 * 2 / 10) + (i5 * 2) # 10) + i6 + " +
                        "if (i7 * 2 < 10, i7 * 2, floor(i7 * 2 / 10) + (i7 * 2) # 10) + i8 + " +
                        "if (i9 * 2 < 10, i9 * 2, floor(i9 * 2 / 10) + (i9 * 2) # 10) + i10 + " +
                        "if (i11 * 2 < 10, i11 * 2, floor(i11 * 2 / 10) + (i11 * 2) # 10) + i12 + " +
                        "if (i13 * 2 < 10, i13 * 2, floor(i13 * 2 / 10) + (i13 * 2) # 10)) # 10 == i14"),
                MathValidatorArgument.create("base_16", "10 - (" +
                        "if (i0 * 2 < 10, i0 * 2, floor(i0 * 2 / 10) + (i0 * 2) # 10) + i1 + " +
                        "if (i2 * 2 < 10, i2 * 2, floor(i2 * 2 / 10) + (i2 * 2) # 10) + i3 + " +
                        "if (i4 * 2 < 10, i4 * 2, floor(i4 * 2 / 10) + (i4 * 2) # 10) + i5 + " +
                        "if (i6 * 2 < 10, i6 * 2, floor(i6 * 2 / 10) + (i6 * 2) # 10) + i7 + " +
                        "if (i8 * 2 < 10, i8 * 2, floor(i8 * 2 / 10) + (i8 * 2) # 10) + i9 + " +
                        "if (i10 * 2 < 10, i10 * 2, floor(i10 * 2 / 10) + (i10 * 2) # 10) + i11 + " +
                        "if (i12 * 2 < 10, i12 * 2, floor(i12 * 2 / 10) + (i12 * 2) # 10) + i13 + " +
                        "if (i14 * 2 < 10, i14 * 2, floor(i14 * 2 / 10) + (i14 * 2) # 10)) # 10 == i15")
        ));
        mathValidator.setExpression("if(length = 16, base_16, if(length = 15, base_15, if(length = 14, base_14, base_9)))");

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
    @ValueSource(strings = {"4711 4437 1144 3766", "371144371144376", "3411-443711-44373"})
    public void test_valid_credit_card(String detection) {
        assertTrue(MathValidatorService.validate(detection, Parser.NUMERIC, expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {"371144371144374", "3411-443511-44373", "4711 4487 1144 3766"})
    public void test_invalid_credit_card(String detection) {
        assertFalse(MathValidatorService.validate(detection, Parser.NUMERIC, expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {"474-824-224", "926 542 226", "474824224"})
    @DisplayName("australian sin: check digit (luhn) validator")
    public void test_valid_sin(String detection) {
        assertTrue(MathValidatorService.validate(detection, Parser.NUMERIC, expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {"474-824-228", "926 546 226", "474-824-228", "926 546 226"})
    public void test_invalid_sin(String detection) {
        assertFalse(MathValidatorService.validate(detection, Parser.NUMERIC, expression));
    }
}
