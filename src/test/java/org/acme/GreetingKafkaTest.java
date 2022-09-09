package org.acme;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;

import java.util.function.Supplier;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
public class GreetingKafkaTest {

    @InjectKafkaCompanion
    KafkaCompanion companion;

    @BeforeEach
    void setUp() {
        CriticalResourceManager.resetCounts();
    }

    private static boolean eventReceived(String message, Supplier<String> lastMsgSupplier) {
        return message != null && message.equals(lastMsgSupplier.get());
    }

    @Test
    public void testKafkaCompletionStageListener() {
        for (int testN = 0; testN < 20; testN++) {
            final String message = "Hello " + testN;
            companion.produceStrings().fromRecords(new ProducerRecord<>("completion-stage", message));

            await()
                    .atMost(10000, MILLISECONDS)
                    .until(() -> eventReceived(message, KafkaCompletionStageListener::lastReceivedMessage));

            Assertions.assertEquals(testN + 1, CriticalResourceManager.producerCallsCount(), "Mismatch in invocation number of producers (??)");
            Assertions.assertEquals(testN + 1, CriticalResourceManager.disposerCallsCount(), "Mismatch in invocation number of disposers (leak?)");
        }
    }

    @Test
    public void testKafkaUniListener() {
        for (int testN = 0; testN < 20; testN++) {
            final String message = "Hello " + testN;
            companion.produceStrings().fromRecords(new ProducerRecord<>("uni", message));

            await()
                    .atMost(10000, MILLISECONDS)
                    .until(() -> eventReceived(message, KafkaUniListener::lastReceivedMessage));

            Assertions.assertEquals(testN + 1, CriticalResourceManager.producerCallsCount(), "Mismatch in invocation number of producers (??)");
            Assertions.assertEquals(testN + 1, CriticalResourceManager.disposerCallsCount(), "Mismatch in invocation number of disposers (leak?)");
        }
    }

    @Test
    public void testKafkaPayloadListener() {
        for (int testN = 0; testN < 20; testN++) {
            final String message = "Hello " + testN;
            companion.produceStrings().fromRecords(new ProducerRecord<>("payload", message));

            await()
                    .atMost(10000, MILLISECONDS)
                    .until(() -> eventReceived(message, KafkaPayloadListener::lastReceivedMessage));

            Assertions.assertEquals(testN + 1, CriticalResourceManager.producerCallsCount(), "Mismatch in invocation number of producers (??)");
            Assertions.assertEquals(testN + 1, CriticalResourceManager.disposerCallsCount(), "Mismatch in invocation number of disposers (leak?)");
        }
    }

    @Test
    public void testKafkaBlockingPayloadListener() {
        for (int testN = 0; testN < 20; testN++) {
            final String message = "Hello " + testN;
            companion.produceStrings().fromRecords(new ProducerRecord<>("blocking-payload", message));

            await()
                    .atMost(10000, MILLISECONDS)
                    .until(() -> eventReceived(message, KafkaBlockingPayloadListener::lastReceivedMessage));

            Assertions.assertEquals(testN + 1, CriticalResourceManager.producerCallsCount(), "Mismatch in invocation number of producers (??)");
            Assertions.assertEquals(testN + 1, CriticalResourceManager.disposerCallsCount(), "Mismatch in invocation number of disposers (leak?)");
        }
    }

}
