package no.steria.javaeespike;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.steria.javaeespike.common.Person;
import no.steria.javaeespike.common.PersonView;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

public class PersonServletTest {
	private PersonServlet servlet = new PersonServlet();
	private HttpServletRequest req = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);
	private PersonDao personDao = mock(PersonDao.class);
	private PersonView personView = mock(PersonView.class);
	private PrintWriter writer = new PrintWriter(new StringWriter());

	@Test
	public void shouldDisplayCreateForm() throws Exception {
		createGetRequest("/createPerson.html");
		servlet.service(req, resp);
		
		verify(resp).setContentType("text/html");
		verify(personView).displayCreatePage(writer,"","","",null);
	}

	private void createGetRequest(String path) {
		when(req.getMethod()).thenReturn("GET");
		when(req.getPathInfo()).thenReturn(path);
	}
	
	@Test
	public void shouldCreatePerson() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("first_name")).thenReturn("Obi-Wan");
		when(req.getParameter("last_name")).thenReturn("Kenobi");
		when(req.getParameter("birth_date")).thenReturn("25.05.1977");
		
		servlet.service(req, resp);
		
		InOrder order = inOrder(personDao);
		
		order.verify(personDao).beginTransaction();
		order.verify(personDao).createPerson(Person.create("Obi-Wan","Kenobi",new LocalDate(1977,5,25)));
		order.verify(personDao).endTransaction(true);
		verify(resp).sendRedirect("/");
		
	}

	
	@Test
	public void shouldNotAllowSpecialCharactersInFirstname() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("first_name")).thenReturn("Dar<&>th");
		when(req.getParameter("last_name")).thenReturn("Vader");
		when(req.getParameter("birth_date")).thenReturn("25.05.1977");
		
		servlet.service(req, resp);
		
		verify(personDao,never()).createPerson(any(Person.class));
		verify(personView).displayCreatePage(writer,"Dar<&>th","Vader","25.05.1977","First name contains illegal character");
	}
	
	@Test
	public void shouldNotAllowEmptyFirstname() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("first_name")).thenReturn("");
		when(req.getParameter("last_name")).thenReturn("Vader");
		when(req.getParameter("birth_date")).thenReturn("25.05.1977");
		
		servlet.service(req, resp);
		
		verify(personDao,never()).createPerson(any(Person.class));
		verify(personView).displayCreatePage(writer,"","Vader","25.05.1977","First name can not be empty");
	}
	
	@Test
	public void shouldNotAllowSpecialCharactersInLastname() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("first_name")).thenReturn("Darth");
		when(req.getParameter("last_name")).thenReturn("Vad<&>er");
		when(req.getParameter("birth_date")).thenReturn("25.05.1977");
		
		servlet.service(req, resp);
		
		verify(personDao,never()).createPerson(any(Person.class));
		verify(personView).displayCreatePage(writer,"Darth","Vad<&>er","25.05.1977","Last name contains illegal character");
	}
	
	@Test
	public void shouldNotAllowEmptyLastname() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("first_name")).thenReturn("Darth");
		when(req.getParameter("last_name")).thenReturn("");
		when(req.getParameter("birth_date")).thenReturn("25.05.1977");
		
		servlet.service(req, resp);
		
		verify(personDao,never()).createPerson(any(Person.class));
		verify(personView).displayCreatePage(writer,"Darth","","25.05.1977","Last name can not be empty");
	}
	
	@Test
	public void shouldCreateCorrectBirthDate() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("first_name")).thenReturn("Obi-Wan");
		when(req.getParameter("last_name")).thenReturn("Kenobi");
		when(req.getParameter("birth_date")).thenReturn("25.05.1977");
		
		servlet.service(req, resp);
		
		ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
		verify(personDao).createPerson(captor.capture());
		assertThat(captor.getValue().getBirthDate()).isEqualTo(new LocalDate(1977,05,25));
	}
	
	@Test
	public void shouldHandleEmptyBirthDate() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("first_name")).thenReturn("Obi-Wan");
		when(req.getParameter("last_name")).thenReturn("Kenobi");
		when(req.getParameter("birth_date")).thenReturn("");
		
		servlet.service(req, resp);
		
		ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
		verify(personDao).createPerson(captor.capture());
		assertThat(captor.getValue().getBirthDate()).isNull();
	}
	
	@Test
	public void shouldNotAllowIllegalDate() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("first_name")).thenReturn("Darth");
		when(req.getParameter("last_name")).thenReturn("Vader");
		when(req.getParameter("birth_date")).thenReturn("32.05.1977");
		
		servlet.service(req, resp);
		
		verify(personDao,never()).createPerson(any(Person.class));
		verify(personView).displayCreatePage(writer,"Darth","Vader","32.05.1977","Birth date must have format dd.mm.yyyy");
	}
	
	@Test
	public void shouldRollbackOnError() throws Exception {
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("first_name")).thenReturn("Darth");
		when(req.getParameter("last_name")).thenReturn("Vader");
		when(req.getParameter("birth_date")).thenReturn("25.05.1977");
		doThrow(new RuntimeException()).when(personDao).createPerson(any(Person.class));
		
		try {
			servlet.service(req, resp);
		} catch (Exception e) {
		}
		
		verify(personDao).endTransaction(false);
		
		
	}

	@Test
	public void shouldDisplaySearchForm() throws Exception {
		createGetRequest("/findPeople.html");
		servlet.service(req, resp);
		
		verify(resp).setContentType("text/html");
		verify(personView).displaySearchPage(writer,new ArrayList<Person>());		
	}
	
	@Test
	public void shouldSearch() throws Exception {
		createGetRequest("/findPeople.html");
		when(req.getParameter("name_query")).thenReturn("Darth");
		
		servlet.service(req, resp);
		
		verify(personDao).findPeople("Darth");
		
	}
	
	@Test
	public void shouldDisplaySearchResult() throws Exception {
		createGetRequest("/findPeople.html");
		when(req.getParameter("name_query")).thenReturn("Darth");
		Person vader = Person.create("Darth", "Vader", new LocalDate(1977,5,25));
		Person maul = Person.create("Darth", "Maul", new LocalDate(1999,5,19));
		when(personDao.findPeople(anyString())).thenReturn(Arrays.asList( //
				vader ,
				maul //
				));
		
		servlet.service(req, resp);
		
		verify(personView).displaySearchPage(writer, Arrays.asList(vader,maul));
		
	}
	
	@Before
	public void setup() throws IOException {
		servlet.setPersonDao(personDao);
		servlet.setPersonView(personView);
		when(resp.getWriter()).thenReturn(writer);
	}
	
}
