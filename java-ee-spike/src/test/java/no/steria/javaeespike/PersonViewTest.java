package no.steria.javaeespike;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class PersonViewTest {
	@Test
	public void clojureShouldSayHello() throws Exception {
		assertThat(new clojure_person_view.core().hello("Anders")).isEqualTo("Hello, Anders");
	}
}
