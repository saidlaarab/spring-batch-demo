package com.spring.training.batch.job;

import com.spring.training.batch.dao.BankTransactionRepository;
import com.spring.training.batch.model.BankTransaction;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class BankTransactionWriter implements ItemWriter<BankTransaction> {
    @Autowired
    private BankTransactionRepository repository;

    @Override
    public void write(List<? extends BankTransaction> list) throws Exception {
          repository.saveAll(list);
    }
}
