package com.spring.training.batch.job;

import com.spring.training.batch.model.BankTransaction;
import lombok.Getter;
import org.springframework.batch.item.ItemProcessor;


@Getter
public class BankTransactionAnalyticsProcessor implements ItemProcessor<BankTransaction, BankTransaction> {
    private double totalCredit;
    private double totalDebit;

    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {
        String tranctionType = bankTransaction.getTranctionType();
        if(tranctionType.equals("D")){
            this.totalDebit += bankTransaction.getAmount();
        }else if(tranctionType.equals("C")){
            this.totalCredit += bankTransaction.getAmount();
        }

        return bankTransaction;
    }
}
