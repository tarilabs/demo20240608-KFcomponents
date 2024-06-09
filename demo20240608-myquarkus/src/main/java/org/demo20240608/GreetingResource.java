package org.demo20240608;

import java.util.List;

import org.demo20240608.model.KServeInput;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @RestClient 
    KServeInferface kserve;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        KServeInput.Input i = new KServeInput.Input("input-0", List.of(1, 6), "FP32", new Double[][]{{2.00050554e-04, 2.37277918e-02, 1.00000000e+00, 0.00000000e+00, 0.00000000e+00, 1.00000000e+00}});
        KServeInput hardcoded = new KServeInput(List.of(i));
        var result = kserve.infer("mm-model", hardcoded);
        var fraud = result.outputs().get(0).data();
        return "Hello from Quarkus REST"+fraud;
    }
}
