package no.steria.javaeespike;

import static org.fest.assertions.Assertions.assertThat;

import org.joda.time.LocalDate;
import org.junit.Test;

public class PersonTest {
	@Test
	public void factoryShouldReturnPerson() throws Exception {
		Person person = Person.create("Luke", "Skywalker", new LocalDate(2012,1,5));
		
		assertThat(person.getFirstName()).isEqualTo("Luke");
		assertThat(person.getLastName()).isEqualTo("Skywalker");
		assertThat(person.getBirthDate()).isEqualTo(new LocalDate(2012,1,5));
	}
	
	@Test
	public void peopleWithSameNameAreEqual() throws Exception {
		assertThat(Person.create("Luke", "Skywalker", null)) //
			.isEqualTo(Person.create("Luke", "Skywalker", null)) //
			.isNotEqualTo(Person.create("Darth", "Vader", null)) //
			.isNotEqualTo(Person.create("Luke", null, null)) //
			;
	}
}
