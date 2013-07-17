package ru.hh.http.emulator;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class BaseTest {

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	protected static final ObjectMapper objectMapper = new ObjectMapper();
	
	private static JettyServer jetty;
	
	protected static int jettyPort;
	
	protected HttpClient client;
	
	@BeforeClass
	public static void beforeClass() throws Exception{
		objectMapper.registerModule(new Hibernate4Module());
		
		jetty = new JettyServer();
		jettyPort = jetty.start();
	}
	
	@AfterClass
	public static void afterClass() throws Exception{
		jetty.stop();
	}
	
	@Before
	public void beforeTestMethod() throws Exception{
		client = new HttpClient();
		client.setFollowRedirects(false);
		client.start();
	}

	@After
	public void afterTestMethod() throws Exception{
		client.stop();
	}
	
	protected Request newRequest(){
		return client.newRequest("localhost", jettyPort);
	}
}
