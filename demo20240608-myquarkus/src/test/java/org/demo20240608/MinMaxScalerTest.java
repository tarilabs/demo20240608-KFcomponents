package org.demo20240608;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MinMaxScalerTest {
    
    @Test
    public void dryRun() throws IOException {
        MinMaxScaler scaler = new MinMaxScaler("scale_values.txt", "min_values.txt");
        
        double[] liveData = { 2.131956, 6.358667, 1, 0, 0, 1};
        double[] transformedData = scaler.transform(liveData);

        for (double val : transformedData) {
            System.out.println(val);
        }
    }
}
