package no.steria.javaeespike;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PersonViewClojureAdapter implements PersonView {
	private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd.MM.yyyy");

	@Override
	public void displayCreatePage(PrintWriter writer, String firstName, String lastName, String birthDate,
			String errormessage) {
		String context = new clojure_person_view.core().generateCreatePage(firstName,lastName,birthDate,errormessage);
		writer.append(context);
	}

	public void displaySearchPage(PrintWriter writer, List<Person> searchResults) {
		String context = new clojure_person_view.core().generateSearchPage(generateResponse(searchResults));
		writer.append(context);

	}
	
	private List<String> generateResponse(List<Person> people) {
		List<String> result = new ArrayList<>();
		for (Person person : people) {
			result.add(prettyPrint(person));
		}
		return result;
	}

	private String prettyPrint(Person person) {
		return person.getFirstName() + " " + person.getLastName() + formatDate(person.getBirthDate());
	}
	
	private String formatDate(LocalDate date) {
		if (date == null) return "";
		return " (" + dateFormat.print(date) + ")";
	}


}
