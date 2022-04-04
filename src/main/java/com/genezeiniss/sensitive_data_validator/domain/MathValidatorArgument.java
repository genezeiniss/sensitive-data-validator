package com.genezeiniss.sensitive_data_validator.domain;

import lombok.Value;

@Value(staticConstructor = "create")
public class MathValidatorArgument {

    String name;
    String expression;
}
