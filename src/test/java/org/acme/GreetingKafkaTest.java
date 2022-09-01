package org.acme;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;

@QuarkusTest
public class GreetingKafkaTest {

    @Inject
    @Channel("main-channel")
    Emitter<String> greetingEmitter;

    @RepeatedTest(20)
    public void testHelloKafka(RepetitionInfo repetitionInfo) {
        final int testN = repetitionInfo.getCurrentRepetition();
        final String message = "Hello " + testN;
        greetingEmitter.send(message);

        await()
                .atMost(10000, MILLISECONDS)
                .until(() -> eventReceived( message ) );

        Assertions.assertEquals( testN, CriticalResourceManager.producerCallsCount(), "Mismatch in invocation number of producers (??)" );
        Assertions.assertEquals( testN, CriticalResourceManager.disposerCallsCount(), "Mismatch in invocation number of disposers (leak?)" );
    }

    private static boolean eventReceived(String message) {
        return message != null && message.equals( KafkaListener.lastReceivedMessage() );
    }

}