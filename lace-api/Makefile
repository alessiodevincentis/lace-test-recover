export ENV=development

.PHONY: help

help: ## ;-)
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

.DEFAULT_GOAL := help

docker-build-maven: ## build with docker (useful for crystallizing maven dependecies)
	docker build -f Dockerfile.development . -t lacems-api:latest

docker-build: ## build with docker (useful sometimes...)
	docker build -f Dockerfile.${ENV} . -t lacems-api:${ENV}

build: ## build with compose
	docker-compose -f docker-compose-${ENV}.yml up --build
	
up: ## up with compose
	docker-compose -f docker-compose-${ENV}.yml up
	
down: ## down with compose
	docker-compose -f docker-compose-${ENV}.yml down

clean: ## clean with compose
	docker-compose -f docker-compose-${ENV}.yml down --rm all

ps: ## ps with compose
	docker-compose -f docker-compose-${ENV}.yml ps
