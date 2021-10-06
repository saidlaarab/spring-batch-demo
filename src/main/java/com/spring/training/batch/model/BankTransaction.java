package com.spring.training.batch.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
public class BankTransaction {
    @Id
    private Long id;
    private long accountID;
    private Date transactionDate;
    @Transient
    private String transactionDateStr;
    private String tranctionType;
    private double amount;
}
