package com.example.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	@Override
	public Person process(final Person person) throws Exception {
		final int id = person.getId();
		final String firstname = person.getFirstName().toUpperCase();
		final String lastName = person.getLastName().toUpperCase();
		var transformedPerson = new Person(id, firstname, lastName);
		log.info("Converting (" + person + ") into (" + transformedPerson + ")");
		return transformedPerson;
	}

}
