package com.example.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Person>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("Update person set first_name=:firstName, last_name=:lastName where id =:id")
				.dataSource(dataSource).build();
	}

	@Bean
	public ItemReader<Person> reader(DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<Person>().name("cursorItemReader").dataSource(dataSource)
				.sql("select id, first_name, last_name from person").dataSource(dataSource)
				.rowMapper(new BeanPropertyRowMapper<>(Person.class)).build();
	}

	@Bean
	public ItemReader<Person> referralReader(DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<Person>().name("cursorItemReader").dataSource(dataSource)
				.sql("select id, first_name, last_name from person where last_name='CHAUDHARY'").dataSource(dataSource)
				.rowMapper(new BeanPropertyRowMapper<>(Person.class)).build();
	}

	@Bean
	public JdbcBatchItemWriter<Person> referralWriter(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Person>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("Update person set last_name=:lastName where id =:id").dataSource(dataSource).build();
	}

	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	public ReferralItemProcessor referralProcessor() {
		return new ReferralItemProcessor();
	}

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1, Step step2) {
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).start(step1).next(step2)
				.listener(listener).build();
	}

	@Bean
	public Step step1(JdbcBatchItemWriter<Person> writer, ItemReader<Person> reader) {
		return stepBuilderFactory.get("step1").<Person, Person>chunk(1).reader(reader).processor(processor())
				.writer(writer).build();
	}

	@Bean
	public Step step2(JdbcBatchItemWriter<Person> referralWriter, ItemReader<Person> referralReader) {
		return stepBuilderFactory.get("step2").<Person, Person>chunk(1).reader(referralReader)
				.processor(referralProcessor()).writer(referralWriter).build();
	}
}
