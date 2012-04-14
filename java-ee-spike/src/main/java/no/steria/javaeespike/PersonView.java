package no.steria.javaeespike;

import java.io.PrintWriter;
import java.util.List;

public interface PersonView {

	void displayCreatePage(PrintWriter writer, String firstName, String lastName, String birthDate, String errormessage);

	void displaySearchPage(PrintWriter writer, List<Person> searchResults);


}
