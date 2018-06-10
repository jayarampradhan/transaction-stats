package com.n26.test.service;


import com.n26.test.model.Transaction;
import com.n26.test.model.TransactionStatistics;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

public class TransactionStatisticsServiceTest {

    private TransactionStatisticsService transactionStatisticsService = new TransactionStatisticsService();

    @Test
    public void testTransactionExpiry() {
        Transaction t = new Transaction();
        t.setAmount(new Double("12.20"));
        System.out.println(Instant.now().minusSeconds(5).toEpochMilli());
        t.setTimeStamp(Instant.now().minusSeconds(5).toEpochMilli());
        Assert.assertFalse(transactionStatisticsService.isExpired(t));
        t.setTimeStamp(Instant.now().minusSeconds(64).toEpochMilli());
        Assert.assertTrue(transactionStatisticsService.isExpired(t));
        t.setTimeStamp(Instant.now().plusSeconds(5).toEpochMilli());
        Assert.assertFalse(transactionStatisticsService.isExpired(t));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRecordInvalid() {
        Transaction t = new Transaction();
        t.setAmount(new Double("12.20"));
        t.setTimeStamp(Instant.now().plusSeconds(5).toEpochMilli());
        transactionStatisticsService.record(t);

    }

    @Test
    public void testRecord() {
        Transaction t = new Transaction();
        t.setAmount(new Double("12.20"));
        t.setTimeStamp(Instant.now().minusSeconds(5).toEpochMilli());
        transactionStatisticsService.record(t);
        TransactionStatistics statistics = transactionStatisticsService.getStatistics();
        Assert.assertEquals(Long.valueOf(1), statistics.getCount());

        //Invalid shouldn't update
        t.setAmount(new Double("12.20"));
        t.setTimeStamp(Instant.now().minusSeconds(65).toEpochMilli());
        try {
            transactionStatisticsService.record(t);
        } catch (Exception e) {
            //Sallow
        }
        statistics = transactionStatisticsService.getStatistics();
        Assert.assertEquals(Long.valueOf(1), statistics.getCount());

        //Add one more valid
        t.setAmount(new Double("12.20"));
        t.setTimeStamp(Instant.now().minusSeconds(10).toEpochMilli());
        transactionStatisticsService.record(t);
        statistics = transactionStatisticsService.getStatistics();
        Assert.assertEquals(Long.valueOf(2), statistics.getCount());

        //Remove From Stats
        transactionStatisticsService.removeFromStatistics(t);
        statistics = transactionStatisticsService.getStatistics();
        Assert.assertEquals(Long.valueOf(1), statistics.getCount());
    }
}
