package ru.hh.http.emulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.hh.http.emulator.entity.AttributeType;
import ru.hh.http.emulator.entity.HttpEntry;
import ru.hh.http.emulator.exception.AmbiguousRulesException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:test-context.xml")
public class SimpleRequestControllerTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRequestControllerTest.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private JettyServer jetty;
	
	private HttpClient client;
	
	{
		objectMapper.registerModule(new Hibernate4Module());
	}
	
	@Before
	public void startServer() throws Exception{
		jetty = new JettyServer();
		jetty.start();
		
		client = new HttpClient();
		client.start();
	}
	
	@After
	public void stopServer() throws Exception{
		client.stop();
		jetty.stop();
	}
	
	@Test
    public void putSimpleRuleTest() throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, AmbiguousRulesException, InterruptedException, TimeoutException, ExecutionException{
		
		final Collection<HttpEntry> responseEntries = new ArrayList<HttpEntry>(1);
		responseEntries.add(new HttpEntry(AttributeType.STATUS, null, "201"));
		
		final ContentResponse response = client.newRequest("localhost", jetty.getServerPort()).path("/criteria/rule/simple").method(HttpMethod.PUT)
			.param("rule", objectMapper.writeValueAsString(new HttpEntry(AttributeType.PARAMETER, "param1", "value1")))
			.param("response", objectMapper.writeValueAsString(responseEntries))
			.send();
		
		System.out.println(new String(response.getContent()));
    }
	
	
}
