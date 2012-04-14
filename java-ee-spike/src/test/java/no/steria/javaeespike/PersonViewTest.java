package no.steria.javaeespike;

import static org.fest.assertions.Assertions.assertThat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import no.steria.javaeespike.common.Person;
import no.steria.javaeespike.common.PersonView;

import org.junit.Test;

public class PersonViewTest {
	@Test
	public void clojureShouldDisplayCreatePage() throws Exception {
		assertThat(new clojure_person_view.core().generateCreatePage("", "", "", null))
			.contains("<form")
			.doesNotContain("<div");
	}
	
	@Test
	public void shoulDisplaySearchForm() throws Exception {
		assertThat(new clojure_person_view.core().generateSearchPage(Arrays.asList("Luke","Anakin"))).contains("<li>Luke");
	}
	
	@Test
	public void shouldGetCreatePersonViewFromClojure() throws Exception {
		PersonView personView = new clojure_person_view.core().personViewClojure();
		assertThat(personView).isNotNull();
	
		StringWriter result = new StringWriter();
		personView.displayCreatePage(new PrintWriter(result), "", "", "", null);
		
		assertThat(result.toString()).contains("<form action=\"createPerson.html\"");
	}
	
	
	@Test
	public void shouldGetDisplaySearchResult() throws Exception {
		PersonView personView = new clojure_person_view.core().personViewClojure();
		assertThat(personView).isNotNull();
		
		StringWriter result = new StringWriter();
		
		personView.displaySearchPage(new PrintWriter(result), Arrays.asList(
				Person.create("Luke", "Skywalker", null),
				Person.create("Anakin", "Skywalker", null)));
		
		assertThat(result.toString()).contains("<li>Luke Skywalker").contains("<li>Anakin Skywalker");
	}
	
	
}
