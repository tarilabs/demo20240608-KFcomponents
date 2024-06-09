package org.demo20240608;

import org.demo20240608.model.KServeInput;
import org.demo20240608.model.KServeOutput;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@RegisterRestClient(configKey="kserve-api")
public interface KServeInferface {
    // v2/models/mm-model/infer
    @POST
    @Path("/v2/models/{modelName}/infer")
    public KServeOutput infer(@PathParam("modelName") String modelName, KServeInput input);
}
