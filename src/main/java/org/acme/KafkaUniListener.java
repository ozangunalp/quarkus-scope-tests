package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class KafkaUniListener {

	private static final Logger log = LoggerFactory.getLogger(KafkaUniListener.class);

	private static volatile String lastMessage;

	@Inject
	public GloballyScopedThing globalThing;

	@Incoming("uni")
//	@Blocking
	@ActivateRequestContext
	public Uni<Void> receive(Message<String> event) {
		log.info("Kafka event received, {}", event.getPayload());
		globalThing.actOnGlobalThing(event.getPayload());
		log.info("Kafka event processed, {}", event);
		this.lastMessage = event.getPayload();
		return Uni.createFrom().completionStage(event.ack());
	}

	public static String lastReceivedMessage() {
		return lastMessage;
	}

}
