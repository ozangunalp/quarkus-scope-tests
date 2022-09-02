package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
public class KafkaBlockingPayloadListener {

	private static final Logger log = LoggerFactory.getLogger(KafkaBlockingPayloadListener.class);

	private static volatile String lastMessage;

	@Inject
	public GloballyScopedThing globalThing;

	@Incoming("blocking-payload")
	@Blocking
	@ActivateRequestContext
	public void receive(String event) {
		log.info("Kafka event received, {}", event);
		globalThing.actOnGlobalThing(event);
		log.info("Kafka event processed, {}", event);
		this.lastMessage = event;
	}

	public static String lastReceivedMessage() {
		return lastMessage;
	}

}
