package org.demo20240608;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class MinMaxScaler {
    private double[] scale_;
    private double[] min_;

    public MinMaxScaler(String scaleFilePath, String minFilePath) {
        try {
            this.scale_ = loadParameters(scaleFilePath);
            this.min_ = loadParameters(minFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double[] loadParameters(String filePath) throws IOException {
        List<String> lines = readLinesFromResource(filePath);
        return lines.stream().mapToDouble(Double::parseDouble).toArray();
    }

    private List<String> readLinesFromResource(String resourcePath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    public double[] transform(double[] data) {
        double[] transformedData = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            transformedData[i] = data[i] * scale_[i] + min_[i];
        }
        return transformedData;
    }
}
