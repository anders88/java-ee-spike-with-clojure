package no.steria.javaeespike;

import org.joda.time.LocalDate;

public class Person {

	private String firstName;
	private String lastName;
	private LocalDate birthDate;

	public static Person create(String firstName, String lastName, LocalDate birthDate) {
		Person person = new Person();
		
		person.firstName = firstName;
		person.lastName = lastName;
		person.birthDate = birthDate;
		
		return person;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}
	
	@Override
	public int hashCode() {
		return 31 * nullSafeHashCode(firstName) * nullSafeHashCode(lastName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Person)) return false;
		Person other = (Person) obj;
		return nullSafeEquals(firstName, other.firstName) && nullSafeEquals(lastName, other.lastName);
	}
	
	private static <T> boolean nullSafeEquals(T a, T b) {
		return (a != null) ? a.equals(b) : b == null;
	}
	
	private static int nullSafeHashCode(Object obj) {
		return obj != null ? obj.hashCode() : -1;
	}

	@Override
	public String toString() {
		return "Person<" + getFirstName() + " " + getLastName() +">";
	}
}
