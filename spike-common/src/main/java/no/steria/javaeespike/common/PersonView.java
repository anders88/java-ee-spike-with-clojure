package no.steria.javaeespike.common;

import java.io.PrintWriter;
import java.util.List;

import no.steria.javaeespike.common.Person;

public interface PersonView {

	void displayCreatePage(PrintWriter writer, String firstName, String lastName, String birthDate, String errormessage);

	void displaySearchPage(PrintWriter writer, List<Person> searchResults);


}
