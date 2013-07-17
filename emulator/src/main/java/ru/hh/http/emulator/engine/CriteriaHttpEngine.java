package ru.hh.http.emulator.engine;

import java.util.Collection;

import org.hibernate.NonUniqueResultException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.hh.http.emulator.entity.HttpCriteria;
import ru.hh.http.emulator.entity.HttpEntry;
import ru.hh.http.emulator.exception.AmbiguousRulesException;
import ru.hh.http.emulator.exception.RuleNotFoundException;

@Component
public class CriteriaHttpEngine implements HttpEngine {

	@Autowired
	private SessionFactory factory;
	
	@Override
	@Transactional(readOnly = true)
	public Collection<HttpEntry> process(final Collection<HttpEntry> request)	throws AmbiguousRulesException, RuleNotFoundException {
		
		try{
			final HttpCriteria criteria = (HttpCriteria) factory.getCurrentSession().createQuery("from HttpCriteria c "
					+ "where (c.restrictions.restrictionType in (:inType, : orType) and c.restrictions.httpEntries.key in (:keys)) "
					+ "and (c.restrictions.restrictionType = :notInType and c.restrictions.httpEntries.key not in (:keys))"
					+ "and (c.restrictions.restrictionType = :andType and (:keys) in c.restrictions.httpEntries.key)").uniqueResult();
			
			if(criteria == null){
				throw new RuleNotFoundException("");
			}
			
			return criteria.getResponse();
		}catch(NonUniqueResultException nure){
			throw new AmbiguousRulesException("");
		}
	}

	@Override
	public Long addRule(HttpEntry rule, Collection<HttpEntry> response) throws AmbiguousRulesException {
		return null;
	}

	@Override
	public Long addRule(HttpCriteria rule, Collection<HttpEntry> response) throws AmbiguousRulesException {
		return null;
	}

	@Override
	public void deleteRule(Long id) throws RuleNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

}
