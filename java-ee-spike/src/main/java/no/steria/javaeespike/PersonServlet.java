package no.steria.javaeespike;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.steria.javaeespike.common.Person;
import no.steria.javaeespike.common.PersonView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PersonServlet extends HttpServlet {

	private static final long serialVersionUID = -2123411892356492338L;
	private PersonView personView;
	private PersonDao personDao;
	private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd.MM.yyyy");

	public void setPersonView(PersonView personView) {
		this.personView = personView;
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		if ("/createPerson.html".equals(req.getPathInfo())) {
			personView.displayCreatePage(resp.getWriter(),"","","",null);
		} else {
			List<Person> people = personDao.findPeople(req.getParameter("name_query"));
			personView.displaySearchPage(resp.getWriter(), people);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String firstName = req.getParameter("first_name");
		String lastName = req.getParameter("last_name");
		String birthDateStr = req.getParameter("birth_date");
		
		StringBuilder errormessage = new StringBuilder(); 
		validateName("First name",firstName,errormessage);
		validateName("Last name", lastName,errormessage);
		LocalDate birthDate = convertToDate(birthDateStr,errormessage);
		
		if (errormessage.toString().isEmpty()) {
			personDao.createPerson(Person.create(firstName, lastName, birthDate));
			resp.sendRedirect("/");
		} else {
			personView.displayCreatePage(resp.getWriter(), firstName, lastName, birthDateStr, errormessage.toString());
		}
	}

	private void validateName(String fieldname, String value, StringBuilder errormessage) {
		if (!errormessage.toString().isEmpty()) return;
		if (value == null || value.isEmpty()) {
			errormessage.append(fieldname + " can not be empty");
			return;
		}
		for (char c : value.toCharArray()) {
			if (Character.isLetter(c) || c == ' ' || c == '-') {
				continue;
			}
			errormessage.append(fieldname + " contains illegal character");
			return;
		}
	}

	private LocalDate convertToDate(String dateStr, StringBuilder errormessage) {
		if ((!errormessage.toString().isEmpty()) || dateStr.isEmpty()) return null;
		try {
			return dateFormat.parseLocalDate(dateStr);
		} catch (IllegalArgumentException e) {
			errormessage.append("Birth date must have format dd.mm.yyyy");
			return null;
		}
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		personDao = new InMemoryPersonDao();
		personView = new PersonViewClojureAdapter();
	}
	
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		personDao.beginTransaction();
		boolean commit = false;
		try {
			super.service(req, resp);
			commit = true;
		} finally {
			personDao.endTransaction(commit);
		}
	}
	
	

}
