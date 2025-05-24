package com.github.mohamad4444.springtemplate.examples.rest.controller;

import java.util.List;

public record ExampleRequestBody(String name, int age, List<String> favoriteThings) {}
