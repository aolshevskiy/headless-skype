package com.skype.headless;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

class WebService extends AbstractIdleService {
	private Server server;
	private final Provider<IndexServlet> servlet;
	
	@Inject
	WebService(Provider<IndexServlet> servlet) {
		this.servlet = servlet;		
	}

	@Override
	protected void startUp() throws Exception {
		server = new Server(8080);
		Context context = new Context(server,"/",Context.SESSIONS);
		context.addServlet(new ServletHolder(servlet.get()), "/*");
		server.start();
	}

	@Override
	protected void shutDown() throws Exception {
		server.stop();
	}
}

class IndexServlet extends HttpServlet {
	private final DBusClient client;
	@Inject
	IndexServlet(DBusClient client) {
		this.client = client;
		String result = client.invoke("NAME headlessskype");
		if(!result.equals("OK"))
			throw new IllegalStateException("NAME headlessskype -> "+result+" should be OK");
		result = client.invoke("PROTOCOL 5");
		if(!result.equals("PROTOCOL 5"))
			throw new IllegalStateException("PROTOCOL 5 -> "+result+" should be PROTOCOL 5");
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String command = CharStreams.toString(req.getReader());
		resp.getWriter().write(client.invoke(command));
	}	
}
