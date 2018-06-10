package com.n26.test.resource;

import com.n26.test.model.Transaction;
import com.n26.test.service.TransactionStatisticsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    @Inject
    private TransactionStatisticsService transactionStatisticsService;
    @POST
    public Response recordTransaction(Transaction transaction) {
        Response.Status status = Response.Status.CREATED;
        try {
            getTransactionStatisticsService().record(transaction);
        } catch (IllegalStateException e) {
            status = Response.Status.NO_CONTENT;
        } catch (Exception e) {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return Response.noContent().status(status).build();
    }

    private TransactionStatisticsService getTransactionStatisticsService() {
        return this.transactionStatisticsService;
    }
}
