package org.acme;

import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.logging.Logger;

public class CriticalResource {

	private static final Logger LOG = Logger.getLogger( CriticalResource.class);

	private static final AtomicInteger instanceCounter = new AtomicInteger();
	private final String id;

	public CriticalResource(String id) {
		this.id = id;
		LOG.info("Created CriticalResource with id " + id);
		instanceInit();
	}

	private static void instanceInit() {
		instanceCounter.incrementAndGet();
	}

	private static void instanceDestroyed(){
		instanceCounter.decrementAndGet();
	}

	private static int getInstanceCount() {
		return instanceCounter.get();
	}

	public void dispose() {
		instanceDestroyed();
		LOG.info("Destroyed CriticalResource with id " + id);
	}

	public String useIt() {
		return "count: " + getInstanceCount();
	}

}
