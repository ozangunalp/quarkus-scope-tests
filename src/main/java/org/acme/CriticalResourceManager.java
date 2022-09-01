package org.acme;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import org.jboss.logging.Logger;

@ApplicationScoped
public class CriticalResourceManager {

	private static final AtomicInteger producerCalls = new AtomicInteger();
	private static final AtomicInteger disposerCalls = new AtomicInteger();

	private static final Logger LOG = Logger.getLogger(CriticalResourceManager.class);

	@Produces @RequestScoped
	public CriticalResource produceInstance() {
		producerCalls.incrementAndGet();
		final String id = UUID.randomUUID().toString();
		LOG.info("Invoked producer for CriticalResource, going to create with id " + id);
		return new CriticalResource(id);
	}

	public void disposer(@Disposes CriticalResource cr) {
		cr.dispose();
		disposerCalls.incrementAndGet();
	}

	public static int producerDisposerDelta() {
		return disposerCalls.get() - producerCalls.get();
	}

	public static int producerCallsCount() {
		return producerCalls.get();
	}

	public static int disposerCallsCount() {
		return disposerCalls.get();
	}

}
