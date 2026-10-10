package com.test.drools.constraint;

import java.util.Set;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

/**
 * @author laizhihui on 2021/6/23
 */
public class CustomAgendaFilter implements AgendaFilter {
    //传入的rule { public CustomAgendaFilter(Set<String> ruleNamesThatAreAllowedToFire)
    private final Set<String> ruleNamesThatAreAllowedToFire;

    public CustomAgendaFilter(Set<String> ruleNamesThatAreAllowedToFire) {
        this.ruleNamesThatAreAllowedToFire = ruleNamesThatAreAllowedToFire;
    }

    @Override
    public boolean accept(Match match) {
        return ruleNamesThatAreAllowedToFire.contains(match.getRule().getName());
    }
}
