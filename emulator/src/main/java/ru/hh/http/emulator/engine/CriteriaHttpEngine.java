package ru.hh.http.emulator.engine;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import ru.hh.http.emulator.client.entity.EQHttpRestriction;
import ru.hh.http.emulator.client.entity.HttpCriteria;
import ru.hh.http.emulator.client.entity.HttpEntry;
import ru.hh.http.emulator.exception.AmbiguousRulesException;
import ru.hh.http.emulator.exception.RuleNotFoundException;

@Component
public class CriteriaHttpEngine implements HttpEngine {

	private final AtomicLong sequence = new AtomicLong();
	
	private final Map<HttpCriteria, Collection<HttpEntry>> criterias = new ConcurrentHashMap<HttpCriteria, Collection<HttpEntry>>();
	
	@Override
	public Collection<HttpEntry> process(final Collection<HttpEntry> request) throws AmbiguousRulesException, RuleNotFoundException {
		
		Map.Entry<HttpCriteria, Collection<HttpEntry>> result = null;
		for (Map.Entry<HttpCriteria, Collection<HttpEntry>> entry : criterias.entrySet()) {
			if(entry.getKey().match(request)){
				if(result == null){
					result = entry;
				}
				else{
					throw new AmbiguousRulesException("Rules conflict. Request:" + request 
							+ "\nmatch two rules:\n" + result 
							+ "\n and:" + entry);
				}
			}
		}
		
		if(result == null){
			throw new RuleNotFoundException("Rule not found for request:" + request);
		}
		
		return result.getValue();
	}

	@Override
	public Long addRule(HttpEntry criteria, Collection<HttpEntry> response) throws AmbiguousRulesException {
		return addRule(new HttpCriteria()
								.addRestriction(new EQHttpRestriction(criteria.getKey(), criteria.getValue(), criteria.getType())), 
						response);
	}

	@Override
	public Long addRule(final HttpCriteria criteria, final Collection<HttpEntry> response) throws AmbiguousRulesException {
		
		if(criterias.containsKey(criteria)){
			throw new AmbiguousRulesException("Rule " + criteria + " conflict with another:" + criterias);
		}
		
		criteria.setId(sequence.incrementAndGet());
		criterias.put(criteria, response);
		
		return criteria.getId();
	}

	@Override
	public void deleteRule(Long id) throws RuleNotFoundException {
		if(id == null){
			throw new RuleNotFoundException("Rule with id='" + id + "' not found");
		}
		
		for(Iterator<HttpCriteria> it = criterias.keySet().iterator(); it.hasNext();){
			if(it.next().getId().equals(id)){
				it.remove();
				return;
			}
		}
		
		throw new RuleNotFoundException("Rule with id='" + id + "' not found");
	}

	@Override
	public void deleteAll() {
		criterias.clear();
	}

}
