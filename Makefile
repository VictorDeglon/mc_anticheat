.PHONY: test lint compile install

## Run tests

test:
	mvn -B test

## Lint the code using Maven Checkstyle plugin

lint:
	mvn -B checkstyle:check

## Compile and package the plugin

compile:
	mvn -B package

## Install the plugin to the local Maven repository

install:
	mvn -B install
