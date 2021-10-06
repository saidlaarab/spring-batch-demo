package com.spring.training.batch.job;

import com.spring.training.batch.model.BankTransaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ItemReader<BankTransaction> bankTransactionItemReader;
    @Autowired
    private ItemWriter<BankTransaction> bankTransactionItemWriter;


    @Bean
    public Job bankJob(){
        Step dataLoadingStep = stepBuilderFactory.get("load-data-step")
                .<BankTransaction, BankTransaction>chunk(100)
                .reader(bankTransactionItemReader)
                .processor(compositeItemProcessor())
                .writer(bankTransactionItemWriter)
                .build();

        return jobBuilderFactory.get("data-loader-job")
                .start(dataLoadingStep)
                .build();
    }

    @Bean
    public FlatFileItemReader<BankTransaction> bankTransactionFlatFileItemReader(@Value("${input.file.path}") Resource resource ){
        FlatFileItemReader<BankTransaction> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("Bank_Transaction_FFIR");
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(bankTransactionlineMapper());

        return flatFileItemReader;
    }

    @Bean
    public LineMapper<BankTransaction> bankTransactionlineMapper() {
        DefaultLineMapper<BankTransaction> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(";");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "accountID", "transactionDateStr", "transactionType", "amount");

        lineMapper.setLineTokenizer(lineTokenizer);

        BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BankTransaction.class);

        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public BankTransactionDataLoaderProcessor bankTransactionDataLoaderProcessor(){
        return new BankTransactionDataLoaderProcessor();
    }

    @Bean
    public BankTransactionAnalyticsProcessor bankTransactionAnalyticsProcessor(){
        return new BankTransactionAnalyticsProcessor();
    }

    @Bean
    public CompositeItemProcessor<BankTransaction, BankTransaction> compositeItemProcessor(){
        CompositeItemProcessor<BankTransaction,BankTransaction> compositeProcessor = new CompositeItemProcessor<>();

        List<ItemProcessor<BankTransaction,BankTransaction>> processorList = new ArrayList<>();
        processorList.add(bankTransactionDataLoaderProcessor());
        processorList.add(bankTransactionAnalyticsProcessor());

        compositeProcessor.setDelegates(processorList);

        return compositeProcessor;
    }



}
