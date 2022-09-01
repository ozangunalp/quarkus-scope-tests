package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class KafkaListener {

	private static final Logger log = LoggerFactory.getLogger(KafkaListener.class);

	private static volatile String lastMessage;

	@Inject
	public GloballyScopedThing globalThing;

	@Incoming("main-channel")
//	@Blocking
	@Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
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
