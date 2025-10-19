# Created Oct-18-2025 
VERSION := $(shell cat VERSION)

all: build doc

build:
	mvn clean install
	mvn clean package
	mvn test

# https://github.com/muquit/markdown-toc-go
doc:
	markdown-toc-go -i docs/README.md -o README.md -f

.PHONY: all build doc
