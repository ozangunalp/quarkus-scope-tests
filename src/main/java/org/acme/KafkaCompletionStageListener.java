package org.acme;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KafkaCompletionStageListener {

	private static final Logger log = LoggerFactory.getLogger(KafkaCompletionStageListener.class);

	private static volatile String lastMessage;

	@Inject
	public GloballyScopedThing globalThing;

	@Incoming("completion-stage")
//	@Blocking
	@ActivateRequestContext
	public CompletionStage<Void> receive(Message<String> event) {
		log.info("Kafka event received, {}", event.getPayload());
		globalThing.actOnGlobalThing(event.getPayload());
		log.info("Kafka event processed, {}", event);
		this.lastMessage = event.getPayload();
		return event.ack();
	}

	public static String lastReceivedMessage() {
		return lastMessage;
	}

}
