package org.demo20240608.model;

import java.util.List;

public record KServeInput (
    List<Input> inputs
) {
    public static record Input (
        String name,
        List<Integer> shape,
        String datatype,
        Object data
    ) {}
}
