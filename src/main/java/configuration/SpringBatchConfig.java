package configuration;

import model.BankTransaction;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

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
    @Autowired
    private ItemProcessor<BankTransaction, BankTransaction> bankTransactionItemProcessor;

    public Job bankJob(){
        Step dataLoadingStep = stepBuilderFactory.get("load-data-step")
                .<BankTransaction, BankTransaction>chunk(100)
                .reader(bankTransactionItemReader)
                .processor(bankTransactionItemProcessor)
                .writer(bankTransactionItemWriter)
                .build();

        return jobBuilderFactory.get("data-loader-job")
                .start(dataLoadingStep)
                .build();
    }

    @Bean
    public FlatFileItemReader<BankTransaction> bankTransactionFlatFileItemReader(@Value("${input.fileP.path}") Resource resource ){
        FlatFileItemReader<BankTransaction> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("Bank_Transaction_FFIR");
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(bankTransactinolineMapper());

        return flatFileItemReader;
    }

    @Bean
    public LineMapper<BankTransaction> bankTransactinolineMapper() {
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

}
