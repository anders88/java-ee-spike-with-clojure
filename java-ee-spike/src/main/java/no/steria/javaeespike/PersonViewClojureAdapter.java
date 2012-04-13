package no.steria.javaeespike;

import java.io.PrintWriter;
import java.util.List;

public class PersonViewClojureAdapter implements PersonView {

	@Override
	public void displayCreatePage(PrintWriter writer, String firstName, String lastName, String birthDate,
			String errormessage) {
		String context = new clojure_person_view.core().generateCreatePage(firstName,lastName,birthDate,errormessage);
		writer.append(context);
	}

	@Override
	public void displaySearchPage(PrintWriter writer, List<String> searchResults) {
		String context = new clojure_person_view.core().generateSearchPage(searchResults);
		writer.append(context);

	}

}
