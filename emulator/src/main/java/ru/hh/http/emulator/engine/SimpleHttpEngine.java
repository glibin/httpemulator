package ru.hh.http.emulator.engine;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import ru.hh.http.emulator.entity.HttpCriteria;
import ru.hh.http.emulator.entity.HttpEntry;
import ru.hh.http.emulator.exception.AmbiguousRulesException;
import ru.hh.http.emulator.exception.RuleNotFoundException;

@Component
public class SimpleHttpEngine implements HttpEngine{

	private final AtomicLong sequence = new AtomicLong();
	
	private final Map<HttpEntry, Collection<HttpEntry>> rules = new ConcurrentHashMap<HttpEntry, Collection<HttpEntry>>(); 
	
	@Override
	public Collection<HttpEntry> process(final Collection<HttpEntry> request) throws AmbiguousRulesException, RuleNotFoundException{
		
		Collection<HttpEntry> response = null;
		for (HttpEntry httpEntry : request) {
			final Collection<HttpEntry> responseCandidate = rules.get(httpEntry);
			if(responseCandidate != null){
				if(response == null){
					response = responseCandidate;
				}
				else{
					throw new AmbiguousRulesException();
				}
			}
		}
		
		if(response != null){
			return response;
		}
		else{
			throw new RuleNotFoundException();
		}
	}

	@Override
	public Long addRule(final HttpEntry rule, final Collection<HttpEntry> response) throws AmbiguousRulesException {
		if(rules.containsKey(rule)){
			throw new AmbiguousRulesException();
		}
		
		rule.setId(sequence.incrementAndGet());
		rules.put(rule, response);
		
		return rule.getId();
	}

	@Override
	public Long addRule(final HttpCriteria rule, final Collection<HttpEntry> response) throws AmbiguousRulesException {
		throw new UnsupportedOperationException();
	}
}
