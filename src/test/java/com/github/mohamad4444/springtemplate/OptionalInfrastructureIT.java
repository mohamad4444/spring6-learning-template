package com.github.mohamad4444.springtemplate;

import com.github.mohamad4444.springtemplate.config.RabbitMQConfig;
import com.github.mohamad4444.springtemplate.service.MongoNoteService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.rabbitmq.listener.simple.auto-startup=false")
@ActiveProfiles({"test", "mongo", "messaging"})
@Testcontainers
class OptionalInfrastructureIT {
    @Container @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:8.0");
    @Container @ServiceConnection
    static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:4-management");
    @Autowired MongoNoteService notes;
    @Autowired RabbitTemplate messages;
    @Test void persistsMongoDocuments() {
        var created = notes.create("Mongo integration", "test");
        assertThat(notes.findById(created.getId())).isPresent();
        notes.deleteById(created.getId());
    }
    @Test void routesRabbitMessages() {
        messages.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, "integration-test");
        assertThat(messages.receiveAndConvert(RabbitMQConfig.QUEUE_NAME, 5000)).isEqualTo("integration-test");
    }
}
