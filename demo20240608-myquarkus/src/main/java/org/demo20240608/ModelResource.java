package org.demo20240608;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.demo20240608.model.KServeInput;
import org.demo20240608.model.KServeOutput;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/model")
public class ModelResource {
    private static final Logger LOG = Logger.getLogger(ModelResource.class);

    @RestClient 
    KServeInferface kserve;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Object evaluate(Payload payload) {
        LOG.info("received: "+payload);
        KServeInput.Input i = new KServeInput.Input("input-0", List.of(1, 6), "FP32", payload.val());
        KServeInput hardcoded = new KServeInput(List.of(i));
        
        LOG.info("will use: "+hardcoded);
        KServeOutput result = kserve.infer("mm-model", hardcoded);
        var fraud = result.outputs().get(0).data();
        try {
            @SuppressWarnings("unchecked")
            List<Double> rValue = (List<Double>) fraud;
            return Map.of("fraud", YesOrNo.from(rValue.get(0)), "value",rValue);
        } catch (Exception e) {
            LOG.error("kserve output changed for model, weird", e);
            return null;
        }
    }

    public static record Payload(LocalDate when, Where where, Double ratioToMedian, YesOrNo repeatRetailer, YesOrNo usedChip, YesOrNo usedPin, YesOrNo onlineOrder) {
        public Double[][] val() {
            return new Double[][]{{2.00050554e-04, ratioToMedian, repeatRetailer.val(), usedChip.val(), usedPin.val(), onlineOrder.val()}};
        }
    }

    public static enum Where {
        HOME(1),
        ABROAD(100);

        private double val;

        private Where(double val) {
            this.val = val;
        }

        public double val() {
            return this.val;
        }
    }

    public static enum YesOrNo {
        YES(1.0d),
        NO(0.0d);

        private double val;

        private YesOrNo(double val) {
            this.val = val;
        }

        public double val() {
            return this.val;
        }

        public static YesOrNo from(double val) {
            if (val > 0) {
                return YES;
            } else {
                return NO;
            }
        }
    }
}
