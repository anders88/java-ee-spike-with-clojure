package no.steria.javaeespike;

import java.util.List;


public interface PersonDao {

	void createPerson(Person person);

	void beginTransaction();

	void endTransaction(boolean commit);

	List<Person> findPeople(String query);

}
