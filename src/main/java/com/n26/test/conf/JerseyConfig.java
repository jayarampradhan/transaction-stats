package com.n26.test.conf;

import com.n26.test.resource.StatisticsResource;
import com.n26.test.resource.TransactionResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(StatisticsResource.class);
        register(TransactionResource.class);
    }

}
