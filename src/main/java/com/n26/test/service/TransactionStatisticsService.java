package com.n26.test.service;

import com.n26.test.model.ExpiringKey;
import com.n26.test.model.Transaction;
import com.n26.test.model.TransactionStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.DelayQueue;

/**
 * This stores the transactions in {@link DelayQueue}, which will help us in expiry of the transactions.
 * Actual statistics is pre computed and stored in {@link #transactionStats} when a actual transaction being stored into the {@link #transactionStore}, which will help to achieve the O(1) time.
 */
@Service
public class TransactionStatisticsService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionStatisticsService.class);

    private final DelayQueue<ExpiringKey<Transaction>> transactionStore = new DelayQueue<>();
    private final long EXPIRY_PERIOD = 60000L;
    private final TransactionStats transactionStats = new TransactionStats();
    private final FunctionalReadWriteLock statsLock = new FunctionalReadWriteLock();

    public void record(Transaction transaction) {
        //Don't allow the future transactions
        if (transaction == null || transaction.getTimeStamp() == null || System.currentTimeMillis() < transaction.getTimeStamp()) {
            LOG.info("Invalid Transaction, ignoring");
            throw new IllegalArgumentException("Invalid Transaction");
        }
        if (isExpired(transaction)) {
            throw new IllegalStateException("Transaction Expired");
        }
        transactionStore.offer(new ExpiringKey<>(transaction, EXPIRY_PERIOD));
        addToStatistics(transaction);
    }

    public TransactionStatistics getStatistics() {
        TransactionStatistics statistics = new TransactionStatistics();
        return statsLock.read(() -> {
            statistics.setCount(transactionStats.count);
            statistics.setSum(transactionStats.sum);
            statistics.setMax(transactionStats.max);
            statistics.setMin(transactionStats.min);
            if (transactionStats.count > 0) {
                statistics.setAverage(transactionStats.sum / transactionStats.count);
            }
            return statistics;
        });
    }

    public void removeFromStatistics(Transaction transaction) {
        statsLock.write(() -> {
            transactionStats.count --;
            transactionStats.sum -= transaction.getAmount();
            if (transactionStats.max == transaction.getAmount()) {
                transactionStats.max = getTransactionStore().stream().mapToDouble(value -> value.getKey().getAmount()).max().orElse(0);
            }
            if (transactionStats.min == transaction.getAmount()) {
                transactionStats.min = getTransactionStore().stream().mapToDouble(value -> value.getKey().getAmount()).min().orElse(0);
            }
        });


    }

    public void addToStatistics(Transaction transaction) {
        statsLock.write(() -> {
            transactionStats.count ++;
            transactionStats.sum += transaction.getAmount();
            if (transactionStats.max == 0 || transactionStats.max < transaction.getAmount()) {
                transactionStats.max = transaction.getAmount();
            }
            if (transactionStats.min == 0 || transactionStats.min > transaction.getAmount()) {
                transactionStats.min = transaction.getAmount();
            }
        });
    }

    public DelayQueue<ExpiringKey<Transaction>> getTransactionStore() {
        return transactionStore;
    }

    public boolean isExpired(Transaction transaction) {
        return System.currentTimeMillis() - EXPIRY_PERIOD > transaction.getTimeStamp();
    }

    private static class TransactionStats {
        private volatile double sum;
        private volatile Long count = 0L;
        private volatile double max;
        private volatile double min;
    }
}
