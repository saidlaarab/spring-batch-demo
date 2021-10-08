package com.spring.training.batch.job;

import com.spring.training.batch.model.BankTransaction;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;


public class ExcelFileRowMapper implements RowMapper<BankTransaction> {
    @Override
    public BankTransaction mapRow(RowSet rs) throws Exception {
        String[] currentRow = rs.getCurrentRow();
        BankTransaction bankTransaction = new BankTransaction();
        bankTransaction.setId(Long.parseLong(currentRow[0]));
        bankTransaction.setAccountID(Long.parseLong(currentRow[1]));
        bankTransaction.setTransactionDateStr(currentRow[2]);
        bankTransaction.setTranctionType(currentRow[3]);
        bankTransaction.setAmount(Double.parseDouble(currentRow[4]));

        return bankTransaction;
    }
}
