.PHONY: compile test lint

compile:
	mvn -B package

test:
	mvn -B test

lint:
	mvn -B checkstyle:check
