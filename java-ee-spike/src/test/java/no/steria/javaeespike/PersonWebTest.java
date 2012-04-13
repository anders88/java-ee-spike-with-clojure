package no.steria.javaeespike;

import static org.fest.assertions.Assertions.assertThat;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class PersonWebTest {
	@Test
	public void shouldCreateAndDisplayPerson() throws Exception {
		Server server = new Server(0);
		server.setHandler(new WebAppContext("src/main/webapp", "/"));
		server.start();
		int localPort = server.getConnectors()[0].getLocalPort();
		String url = "http://localhost:" + localPort + "/";
		
		WebDriver browser = createBrowser();
		
		browser.get(url);
		
		browser.findElement(By.linkText("Create person")).click();
		
		browser.findElement(By.name("first_name")).sendKeys("Luke");
		browser.findElement(By.name("last_name")).sendKeys("Skywalker");
		browser.findElement(By.name("birth_date")).sendKeys("25.05.1977");
		browser.findElement(By.name("createPerson")).click();
		
		browser.findElement(By.linkText("Find people")).click();
		browser.findElement(By.name("name_query")).sendKeys("uke sky");
		browser.findElement(By.name("findPeople")).click();
		
		assertThat(browser.getPageSource()).contains("<li>Luke Skywalker (25.05.1977)</li>");
		
		
		
	}

	private HtmlUnitDriver createBrowser() {
		return new HtmlUnitDriver() {
			@Override
			public WebElement findElement(By by) {
				try {
					return super.findElement(by);
				} catch (NoSuchElementException e) {
					throw new NoSuchElementException("Did not find " + by + " in " + getPageSource());
				}
			}
		};
	}
}
