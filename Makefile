# Created Oct-18-2025 
VERSION := $(shell cat VERSION)

all: build doc

build:
	mvn clean install
	mvn clean package
	mvn test

# https://github.com/muquit/markdown-toc-go
doc:
	markdown-toc-go -i docs/README.md -o README.md -f --glossary docs/glossary.txt

push:
	git push -u origin main

.PHONY: all build doc push
