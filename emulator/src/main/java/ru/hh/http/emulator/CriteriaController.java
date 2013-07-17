package ru.hh.http.emulator;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.hh.http.emulator.engine.SimpleHttpEngine;
import ru.hh.http.emulator.entity.HttpEntry;
import ru.hh.http.emulator.exception.AmbiguousRulesException;
import ru.hh.http.emulator.exception.RuleNotFoundException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

@Controller
@Order(Ordered.LOWEST_PRECEDENCE)
@RequestMapping("criteria")
public class CriteriaController {

	private final Logger LOGGER = LoggerFactory.getLogger(CriteriaController.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private SimpleHttpEngine engine;
	
	@PostConstruct
	public void postConstruct(){
		objectMapper.registerModule(new Hibernate4Module());
	}
	
	@RequestMapping(value = "simple", method = RequestMethod.PUT, produces = {"text/plain"})
	@ResponseBody
	public String createCriteria(@RequestParam("rule") final String httpEntry, @RequestParam("response") final String response) throws JsonParseException, JsonMappingException, IOException, AmbiguousRulesException{
		
		return ""+engine.addRule(objectMapper.readValue(httpEntry, HttpEntry.class), (Collection<HttpEntry>)objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(Collection.class, HttpEntry.class)));
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = {"text/plain"})
	public void deleteCriteria(@PathVariable("id") final Long id) throws JsonParseException, JsonMappingException, IOException, AmbiguousRulesException, RuleNotFoundException{
		engine.deleteRule(id);
	}
	
	@RequestMapping(value = "all", method = RequestMethod.DELETE, produces = {"text/plain"})
	public void deleteAllCriteria() throws JsonParseException, JsonMappingException, IOException, AmbiguousRulesException{
		engine.deleteAll();
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public void internalFail(Exception e){
		e.printStackTrace();
		LOGGER.warn(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e);
	}
}
