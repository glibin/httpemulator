package ru.hh.http.emulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Assert;
import org.junit.Test;

import ru.hh.http.emulator.entity.AttributeType;
import ru.hh.http.emulator.entity.HttpEntry;
import ru.hh.http.emulator.exception.AmbiguousRulesException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


public class SimpleRequestControllerTest extends BaseTest{
	
	private static final String SIMPLE_PATH = "/criteria/simple";
	
	@Test
    public void putSimpleRuleTest() throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, AmbiguousRulesException, InterruptedException, TimeoutException, ExecutionException{
		
		final Collection<HttpEntry> responseEntries = new ArrayList<HttpEntry>(1);
		responseEntries.add(new HttpEntry(AttributeType.STATUS, null, "123"));
		
		final ContentResponse criteriaResponse = newRequest()
			.path(SIMPLE_PATH)
			.method(HttpMethod.PUT)
			.param("rule", objectMapper.writeValueAsString(new HttpEntry(AttributeType.PARAMETER, "param1", "value1")))
			.param("response", objectMapper.writeValueAsString(responseEntries))
			.send();
		
		Assert.assertEquals(HttpServletResponse.SC_OK, criteriaResponse.getStatus());
		
		final long id = Long.parseLong(new String(criteriaResponse.getContent()));
		Assert.assertTrue(id > 0);
		
		final ContentResponse response = newRequest()
				.path("/abc/def")
				.method(HttpMethod.GET)
				.param("param1", "value1")
				.send();
		
		Assert.assertEquals(123, response.getStatus());
    }
	
	
}
