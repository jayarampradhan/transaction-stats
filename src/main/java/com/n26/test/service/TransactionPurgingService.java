package com.n26.test.service;

import com.n26.test.model.ExpiringKey;
import com.n26.test.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * A separate thread will be responsible for deleting the expiry transactions and updating the statistics.
 */
@Service
public class TransactionPurgingService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionPurgingService.class);
    @Inject
    private TransactionStatisticsService transactionStatisticsService;


    @PostConstruct
    public void init() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    final ExpiringKey<Transaction> expiredTransaction = getTransactionStatisticsService().getTransactionStore().take();
                    getTransactionStatisticsService().removeFromStatistics(expiredTransaction.getKey());
                } catch (InterruptedException e) {
                    LOG.error("oops", e);
                }
            }
        });
        t.start();
    }

    public TransactionStatisticsService getTransactionStatisticsService() {
        return this.transactionStatisticsService;
    }
}
