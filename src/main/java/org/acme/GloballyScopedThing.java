package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GloballyScopedThing {

	@Inject
	CriticalResource cr;

	public String currentBeanInstanceCount() {
		return cr.useIt();
	}

	public String actOnGlobalThing(String event) {
		return cr.useIt();
	}
}
