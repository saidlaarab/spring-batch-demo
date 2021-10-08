package com.spring.training.batch.job;

import com.spring.training.batch.model.BankTransaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BankTransactionDataLoaderProcessor implements ItemProcessor<BankTransaction, BankTransaction> {
    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {
        String dateStr = bankTransaction.getTransactionDateStr();

        Date parsedDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        try{
            parsedDate = dateFormat.parse(dateStr);
        }catch(ParseException exc){
            System.getLogger("Parsing Date Error").log(System.Logger.Level.ERROR, "Cannot parse the string-based date !");
        }


        bankTransaction.setTransactionDate(parsedDate);

        return bankTransaction;
    }
}
