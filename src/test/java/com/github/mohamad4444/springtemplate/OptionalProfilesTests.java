package com.github.mohamad4444.springtemplate;

import com.github.mohamad4444.springtemplate.service.MongoNoteService;
import com.github.mohamad4444.springtemplate.messaging.MessageController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "spring.rabbitmq.listener.simple.auto-startup=false",
    "spring.data.mongodb.uri=mongodb://localhost:27099/test?connectTimeoutMS=100&serverSelectionTimeoutMS=100"
})
@ActiveProfiles({"test", "mongo", "messaging"})
class OptionalProfilesTests {
    @Autowired ApplicationContext context;
    @Test void optionalComponentsCanBeWiredWithoutConnectingAtStartup() {
        assertThat(context.getBean(MongoNoteService.class)).isNotNull();
        assertThat(context.getBean(MessageController.class)).isNotNull();
    }
}
