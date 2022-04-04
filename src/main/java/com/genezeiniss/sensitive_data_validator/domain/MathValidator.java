package com.genezeiniss.sensitive_data_validator.domain;

import com.genezeiniss.sensitive_data_validator.enums.Parser;
import lombok.Data;

import java.util.List;

@Data
public class MathValidator {

    Parser parser;
    int maxOperateeLength;
    List<MathValidatorArgument> arguments;
    String expression;
}
