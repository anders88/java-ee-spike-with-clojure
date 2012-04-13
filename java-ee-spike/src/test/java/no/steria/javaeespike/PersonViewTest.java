package no.steria.javaeespike;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class PersonViewTest {
	@Test
	public void clojureShouldSayHello() throws Exception {
		assertThat(new clojure_person_view.core().hello("Anders")).isEqualTo("Hello, Anders");
	}
	
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
	
}
