# Created Oct-18-2025 
VERSION := $(shell cat VERSION)

all: build doc

build:
	mkdir -p bin
	rm -f bin/*
	mvn clean install
	mvn clean package
	mvn test
	cd bin && ln -s ../target/gwtp-$(VERSION).jar

# https://github.com/muquit/markdown-toc-go
doc:
	markdown-toc-go -i docs/README.md -o README.md -f --glossary docs/glossary.txt

release:
	go-xbuild-go -release

push:
	git push -u origin main

.PHONY: all build doc push release
