package no.steria.javaeespike;

import static org.fest.assertions.Assertions.assertThat;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

public class InMemoryPersonDaoTest {
	@Test
	public void shouldSave() throws Exception {
		PersonDao personDao = createPersonDao();
		personDao.beginTransaction();
		Person darth = Person.create("Darth","Vader",new LocalDate(1977,5,25));
		personDao.createPerson(darth);
		assertThat(personDao.findPeople(null)).containsOnly(darth);
		personDao.endTransaction(false);
	}
	
	@Test
	public void shouldRollback() throws Exception {
		Person luke = Person.create("Luke","Skywalker",null);
		Person ani = Person.create("Anakin","Skywalker",null);
		Person jarjar = Person.create("JarJar","Binks",null);
		PersonDao personDao = createPersonDao();

		personDao.beginTransaction();
		personDao.createPerson(luke);
		personDao.createPerson(ani);
		personDao.endTransaction(true);
		
		personDao.beginTransaction();
		personDao.createPerson(jarjar);
		personDao.endTransaction(false);
		
		personDao.beginTransaction();
		assertThat(personDao.findPeople(null)).containsOnly(luke,ani);
		personDao.endTransaction(false);
		
	}
	
	@Test
	public void shouldUseQuery() throws Exception {
		Person luke = Person.create("Luke","Skywalker",null);
		Person ani = Person.create("Anakin","Skywalker",null);
		Person jarjar = Person.create("JarJar","Binks",null);
		PersonDao personDao = createPersonDao();

		personDao.beginTransaction();
		personDao.createPerson(luke);
		personDao.createPerson(ani);
		personDao.createPerson(jarjar);
		
		assertThat(personDao.findPeople("sky")).containsOnly(luke,ani);
		personDao.endTransaction(false);
		
	}

	
	@After
	public void deleteAll() {
		InMemoryPersonDao.deleteAll();
	}
	
	

	private PersonDao createPersonDao() {
		return new InMemoryPersonDao();
	}
}
