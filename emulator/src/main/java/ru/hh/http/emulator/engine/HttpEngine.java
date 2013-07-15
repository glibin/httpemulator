package ru.hh.http.emulator.engine;

import java.util.Collection;

import ru.hh.http.emulator.entity.HttpCriteria;
import ru.hh.http.emulator.entity.HttpEntry;
import ru.hh.http.emulator.exception.AmbiguousRulesException;
import ru.hh.http.emulator.exception.RuleNotFoundException;

public interface HttpEngine {

	public Collection<HttpEntry> process(Collection<HttpEntry> request) throws AmbiguousRulesException, RuleNotFoundException;
	
	public Long addRule(HttpEntry rule, Collection<HttpEntry> response) throws AmbiguousRulesException;
	
	public Long addRule(HttpCriteria rule, Collection<HttpEntry> response) throws AmbiguousRulesException;
	
}
