package com.github.mohamad4444.springtemplate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("mongo")
@EnableMongoRepositories(basePackageClasses = com.github.mohamad4444.springtemplate.repository.MongoNoteRepository.class)
public class MongoConfig { }
