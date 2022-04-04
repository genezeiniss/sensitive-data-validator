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

public class NinoValidatorTest {

    private static Expression expression;

    @BeforeAll
    static void init() {

        // configuration
        MathValidator mathValidator = new MathValidator();
        mathValidator.setParser(Parser.ASCII);
        mathValidator.setMaxOperateeLength(9);

        // prefix - forbidden first and second letters: d = 100, f = 102, i = 105, q = 113, u = 117, v = 118
        // prefix - forbidden second letter: o = 111
        // prefix - forbidden sequences:
        // bg = 98 - 103,
        // gb = 103 - 98,
        // nk = 110 - 107,
        // nt = 110 - 116
        // kn = 107 - 110,
        // tn = 116 - 110,
        // zz = 122 - 122
        // suffix - allowed: a = 97, b = 98, c = 99, d = 100.
        mathValidator.setArguments(Arrays.asList(
                MathValidatorArgument.create("prefix", "" +
                        "(i0 != 100 & i0 != 102 & i0 != 105 & i0 != 113 & i0 != 117 & i0 != 118) & " +
                        "(i1 != 100 & i1 != 102 & i1 != 105 & i1 != 113 & i1 != 117 & i1 != 118 & i1 != 111)"),
                MathValidatorArgument.create("prefix_sequences", "" +
                        "if(i0 = 98, i1 != 103, " +
                        "if(i0 = 103, i1 != 98, " +
                        "if(i0 = 110, (i1 != 107 && i1 != 116), " +
                        "if(i0 = 107, i1 != 110, " +
                        "if(i0 = 116, i1 != 110, " +
                        "if(i0 = 122, i1 != 122, 1.0))))))"),
                MathValidatorArgument.create("suffix", "i8 > 96 && i8 < 101")
        ));
        mathValidator.setExpression("prefix && prefix_sequences && suffix");

        // initiation
        expression = new Expression(mathValidator.getExpression());

        List<Argument> arguments = new ArrayList<>();
        for (int i = 0; i < mathValidator.getMaxOperateeLength(); i++) {
            arguments.add(new Argument(String.format("i%d", i)));
        }
        expression.addArguments(arguments.toArray(new Argument[0]));

        mathValidator.getArguments().forEach(
                argument -> expression.addArguments(new Argument(argument.getName(), argument.getExpression(), arguments.toArray(new Argument[0]))));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ac112233a", "ab 123123 c", "eg-443322-a",
            "ba112233a", "gc112233b", "ng112233c", "km112233d", "tg112233a", "np112233a", "za112233a",
    })
    public void test_valid_nino(String detection) {
        assertTrue(MathValidatorService.validate(detection, Parser.ASCII, expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "dc112233a", "fc112233a", "ic112233a", "qc112233a", "uc112233a", "vc112233a",
            "ad112233a", "af112233a", "ai112233a", "aq112233a", "au112233a", "av112233a",
            "bg112233a", "gb112233a", "nk112233a", "kn112233a", "tn112233a", "nt112233a", "zz112233a",
            "ac112233e", "ac112233f", "ac112233g"
    })
    public void test_invalid_nino(String detection) {
        assertFalse(MathValidatorService.validate(detection, Parser.ASCII, expression));
    }
}
