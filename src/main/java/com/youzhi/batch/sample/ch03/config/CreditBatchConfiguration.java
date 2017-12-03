package com.youzhi.batch.sample.ch03.config;

import com.youzhi.batch.sample.ch03.model.CreditBill;
import com.youzhi.batch.sample.ch03.processor.CreditBillItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * @description 信用卡账单批处理作业配置
 * @create by youzhi.li on 2017-12-02 10:44
 **/
@Configuration
@EnableBatchProcessing
public class CreditBatchConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Bean
    public FlatFileItemReader<CreditBill> csvItemReader() {
        FlatFileItemReader<CreditBill> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("data/ch03/credit-card-bill-201303.csv"));
        reader.setLineMapper(new DefaultLineMapper<CreditBill>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"accountID", "name", "amount", "date", "address"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<CreditBill>() {{
                setTargetType(CreditBill.class);
            }});
        }});
        return reader;
    }

    @Bean
    public FlatFileItemWriter<CreditBill> csvItemWriter() {
        FlatFileItemWriter<CreditBill> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("data/ch03/outputFile.csv"));
        writer.setLineAggregator(new DelimitedLineAggregator<CreditBill>() {{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<CreditBill>() {{
                setNames(new String[]{"accountID", "name", "amount", "date", "address"});
            }});
        }});
        return writer;
    }

    @Bean
    public CreditBillItemProcessor processor() {
        return new CreditBillItemProcessor();
    }

    @Bean
    public Job billJob() {
        return jobBuilderFactory.get("billJob")
                .incrementer(new RunIdIncrementer())
                .flow(billStep())
                .end()
                .build();
    }

    @Bean
    public Step billStep() {
        return stepBuilderFactory.get("billStep")
                .<CreditBill, CreditBill>chunk(5)
                .reader(csvItemReader())
                .processor(processor())
                .writer(csvItemWriter())
                .build();
    }

}
