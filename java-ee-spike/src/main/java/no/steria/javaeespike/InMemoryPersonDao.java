package no.steria.javaeespike;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.steria.javaeespike.common.Person;

public class InMemoryPersonDao implements PersonDao {
	
	private static Set<Person> people = new HashSet<>();
	
	private Set<Person> transaction = null;
	
	private static void add(Person person) {
		synchronized (people) {
			people.add(person);
		}
	}
	
	private static List<Person> all() {
		synchronized (people) {
			List<Person> result = new ArrayList<>();
			for (Person person : people) {
				result.add(Person.create(person.getFirstName(), person.getLastName(), person.getBirthDate()));
			}
			return result;
		}
	}
	
	private void validateTransaction() {
		if (transaction == null) {
			throw new IllegalStateException("No transaction active");
		}
	}
	
	@Override
	public void createPerson(Person person) {
		validateTransaction();
		transaction.add(Person.create(person.getFirstName(), person.getLastName(), person.getBirthDate()));
	}

	@Override
	public void beginTransaction() {
		transaction = new HashSet<>();
	}

	@Override
	public void endTransaction(boolean commit) {
		if (commit) {
			for (Person person : transaction) {
				add(person);
			}
		}
		transaction = null;
	}

	@Override
	public List<Person> findPeople(String query) {
		validateTransaction();
		Set<Person> allPersons = new HashSet<>(all());
		allPersons.addAll(transaction);
		return search(allPersons,query);
	}

	private List<Person> search(Set<Person> allPersons, String queryMixedCase) {
		if (queryMixedCase == null) {
			return new ArrayList<>(allPersons);
		}
		List<Person> result = new ArrayList<>();
		String query = queryMixedCase.toLowerCase();
		for (Person person : allPersons) {
			String fullName = new String(person.getFirstName() + " " + person.getLastName()).toLowerCase();
			if (fullName.contains(query)) {
				result.add(person);
			}
		}
		return result;
	}

	public static void deleteAll() {
		synchronized (people) {
			people.clear();
		}
	}

}
