package ru.hh.http.emulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.hh.http.emulator.engine.CriteriaHttpEngine;
import ru.hh.http.emulator.engine.SimpleHttpEngine;
import ru.hh.http.emulator.entity.AttributeType;
import ru.hh.http.emulator.entity.HttpEntry;
import ru.hh.http.emulator.exception.AmbiguousRulesException;
import ru.hh.http.emulator.exception.RuleNotFoundException;
import ru.hh.http.emulator.utils.CharsetUtils;


@Controller
@RequestMapping("/**")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(RequestController.class);
	
	@Autowired
	private CriteriaHttpEngine engine;
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
	@ResponseBody
	public void process(final HttpServletRequest request, final HttpServletResponse response) throws AmbiguousRulesException, RuleNotFoundException, IOException{
		
		convertToHttpResponse(response, engine.process(convertToHttpEntries(request)));
	}
	
	private Collection<HttpEntry> convertToHttpEntries(final HttpServletRequest request){
		final Collection<HttpEntry> entries = new ArrayList<HttpEntry>();
		
		entries.add(new HttpEntry(AttributeType.PATH, null, request.getPathInfo()));
		
		for(Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements();){
			final String headerName = headerNames.nextElement();
			
			for(Enumeration<String> headerValues = request.getHeaders(headerName); headerValues.hasMoreElements();){
				entries.add(new HttpEntry(AttributeType.HEADER, headerName, headerValues.nextElement()));
			}
		}
		
		entries.add(new HttpEntry(AttributeType.METHOD, null, request.getMethod()));
		
		for (Map.Entry<String, String[]> parameters : request.getParameterMap().entrySet()) {
			for (String paramValue : parameters.getValue()) {
				entries.add(new HttpEntry(AttributeType.PARAMETER, parameters.getKey(), paramValue));
			}
		}
		
		entries.add(new HttpEntry(AttributeType.PROTOCOL, null, request.getProtocol()));
		
		return entries;
	}
	
	private void convertToHttpResponse(final HttpServletResponse response, final Collection<HttpEntry> entries) throws IOException{
		
		for (HttpEntry httpEntry : entries) {
			switch(httpEntry.getType()){
			case BODY:
				response.getOutputStream().write(httpEntry.getValue().getBytes(CharsetUtils.UTF_8));
				break;
			case HEADER:
				response.addHeader(httpEntry.getKey(), httpEntry.getValue());
				break;
			case STATUS:
				response.setStatus(Integer.parseInt(httpEntry.getValue()));
				break;
			default:
				break;
			}
		}
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler({AmbiguousRulesException.class})
	@ResponseBody
	public String badRequest(AmbiguousRulesException e){
		LOGGER.warn(HttpStatus.CONFLICT.toString(), e);
		return e.getMessage();
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(RuleNotFoundException.class)
	@ResponseBody
	public void notFound(RuleNotFoundException e){	
		LOGGER.warn(HttpStatus.NOT_FOUND.toString(), e);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String internalFail(Exception e){
		LOGGER.warn(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e);
		return e.getMessage();
	}
}
