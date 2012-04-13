package no.steria.javaeespike;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyStarter {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8081);
		server.setHandler(new WebAppContext("src/main/webapp", "/"));
		server.start();
		System.out.println("http://localhost:8081/");
	}
}
