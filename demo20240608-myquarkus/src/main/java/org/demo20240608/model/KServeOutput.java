package org.demo20240608.model;

import java.util.List;

public record KServeOutput (
    String model_name,
    String model_version,
    String id,
    Object parameters,
    List<Output> outputs
) {
    public static record Output (
        String name,
        List<Integer> shape,
        String datatype,
        Object parameters,
        Object data
    ) {}

}


