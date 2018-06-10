package com.n26.test.resource;

import com.n26.test.model.TransactionStatistics;
import com.n26.test.service.TransactionStatisticsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/statistics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatisticsResource {

    @Inject
    private TransactionStatisticsService transactionStatisticsService;

    @GET
    public TransactionStatistics getStatistics() {
        return getTransactionStatisticsService().getStatistics();
    }

    private TransactionStatisticsService getTransactionStatisticsService() {
        return this.transactionStatisticsService;
    }

}
