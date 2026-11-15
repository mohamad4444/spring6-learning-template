SHELL := /bin/sh
COMPOSE := docker compose $(if $(wildcard iac/.env),--env-file iac/.env) -f iac/compose.yaml
.PHONY: help run test verify integration package up mysql-up down docker docker-config image
help:
	@echo "run test verify integration package up mysql-up down docker docker-config image"
run:
	./mvnw spring-boot:run
test:
	./mvnw test
verify:
	./mvnw clean verify
integration:
	./mvnw -Pintegration verify
package:
	./mvnw package
up:
	$(COMPOSE) up -d postgres
mysql-up:
	$(COMPOSE) --profile mysql up -d mysql
down:
	$(COMPOSE) --profile '*' down
docker:
	$(COMPOSE) --profile app up --build -d
docker-config:
	$(COMPOSE) config --quiet
image:
	docker build -f iac/Dockerfile -t springtemplate:local .
