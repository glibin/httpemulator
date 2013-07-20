package ru.hh.http.emulator.engine;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.hh.http.emulator.entity.EQHttpRestriction;
import ru.hh.http.emulator.entity.HttpCriteria;
import ru.hh.http.emulator.entity.HttpEntry;
import ru.hh.http.emulator.exception.AmbiguousRulesException;
import ru.hh.http.emulator.exception.RuleNotFoundException;

@Component
public class CriteriaHttpEngine implements HttpEngine {

	private final AtomicLong sequence = new AtomicLong();
	
	private final Map<HttpCriteria, Object> criterias = new ConcurrentHashMap<HttpCriteria, Object>();
	
	@Override
	public Collection<HttpEntry> process(final Collection<HttpEntry> request) throws AmbiguousRulesException, RuleNotFoundException {
		
		HttpCriteria result = null;
		for (HttpCriteria httpCriteria : criterias.keySet()) {
			if(httpCriteria.match(request)){
				if(result == null){
					result = httpCriteria;
				}
				else{
					throw new AmbiguousRulesException("Rules conflict. Request:" + request 
							+ "\nmatch two rules:\n" + result 
							+ "\n and:" + httpCriteria);
				}
			}
		}
		
		if(result == null){
			throw new RuleNotFoundException("Rule not found for request:" + request);
		}
		
		return result.getResponse();
		
		/*try{
			final HttpCriteria criteria = (HttpCriteria) factory.getCurrentSession().createQuery("from HttpCriteria c "
					+ "where (c.restrictions.restrictionType in (:inType, : orType) and c.restrictions.httpEntries.key in (:keys)) "
					+ "and (c.restrictions.restrictionType = :notInType and c.restrictions.httpEntries.key not in (:keys))"
					+ "and (c.restrictions.restrictionType = :andType and (:keys) in c.restrictions.httpEntries.key)").uniqueResult();
			
			if(criteria == null){
				throw new RuleNotFoundException("");
			}
			
			String query = "select * from http_criteria c where exists "
					+ "(select * from http_restriction r connect by r.id root r.id=c.id "
					+ "and ())";
			
			
			return criteria.getResponse();
		}catch(NonUniqueResultException nure){
			throw new AmbiguousRulesException("");
		}*/
	}

	@Override
	public Long addRule(HttpEntry rule, Collection<HttpEntry> response) throws AmbiguousRulesException {
		return addRule(new HttpCriteria(response)
							.addRestriction(new EQHttpRestriction(rule.getKey(), rule.getValue(), rule.getType())));
	}

	@Override
	public Long addRule(final HttpCriteria rule) throws AmbiguousRulesException {
		
		if(criterias.containsKey(rule)){
			throw new AmbiguousRulesException("Rule " + rule + " conflict with another:" + criterias);
		}
		
		rule.setId(sequence.incrementAndGet());
		criterias.put(rule, rule.getResponse());
		
		return rule.getId();
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
