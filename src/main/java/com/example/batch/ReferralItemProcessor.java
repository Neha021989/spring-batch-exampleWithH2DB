package com.example.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class ReferralItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger log = LoggerFactory.getLogger(ReferralItemProcessor.class);

	@Override
	public Person process(final Person person) throws Exception {
		final int id = person.getId();
		final String lastName = person.getLastName() + "hello you";
		var transformedPerson = new Person(id, person.getFirstName(), lastName);
		log.info("Converting (" + person.getLastName() + ") into (" + lastName + ")");
		return transformedPerson;
	}

}
