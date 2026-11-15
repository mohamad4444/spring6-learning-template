package com.github.mohamad4444.springtemplate.messaging;

import com.github.mohamad4444.springtemplate.config.RabbitMQConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@Profile("messaging")
@RequestMapping("/api/v1/messages")
public class MessageController {
    private static final Logger LOG = LoggerFactory.getLogger(MessageController.class);
    private final RabbitTemplate rabbit;
    public MessageController(RabbitTemplate rabbit) { this.rabbit = rabbit; }
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> publish(@Valid @RequestBody Message message) {
        rabbit.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message.text());
        return ResponseEntity.accepted().build();
    }
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(String text) { LOG.info("Received sample message ({} characters)", text.length()); }
    public record Message(@NotBlank @Size(max = 500) String text) { }
}
