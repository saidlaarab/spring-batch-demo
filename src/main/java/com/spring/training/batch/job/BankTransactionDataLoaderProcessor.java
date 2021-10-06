package com.spring.training.batch.job;

import com.spring.training.batch.model.BankTransaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


public class BankTransactionDataLoaderProcessor implements ItemProcessor<BankTransaction, BankTransaction> {
    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {
        String dateStr = bankTransaction.getTransactionDateStr();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        Date parsedDate = dateFormat.parse(dateStr);

        bankTransaction.setTransactionDate(parsedDate);

        return bankTransaction;
    }
}
