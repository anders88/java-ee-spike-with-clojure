package no.steria.javaeespike;

import java.util.List;

import no.steria.javaeespike.common.Person;


public interface PersonDao {

	void createPerson(Person person);

	void beginTransaction();

	void endTransaction(boolean commit);

	List<Person> findPeople(String query);

}
