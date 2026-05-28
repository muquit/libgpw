# Created Oct-18-2025 
VERSION     := $(shell cat VERSION)
MVN_VERSION := $(patsubst v%,%,$(VERSION))

.PHONY: all build doc push release check-central check-sha

all: build doc

build:
	mkdir -p bin
	rm -f bin/*
	mvn clean install
	mvn clean package
	mvn test
	cd bin && ln -s ../target/gpw-$(MVN_VERSION).jar

deploy-to-central:
	# Check the VERSION in pom.xml matches $(VERSION) in the Makefile
	# check ~/.m2/settings.xml has the correct Sonatype credentials and GPG passphrase
	mvn clean deploy -Prelease

# https://github.com/muquit/markdown-toc-go
doc:
	markdown-toc-go -i docs/README.md -o README.md -f --glossary docs/glossary.txt

release:
	go-xbuild-go -release

check-sha:
	@echo "Central:"; curl -s https://repo1.maven.org/maven2/com/muquit/libgpw/libgpw/$(MVN_VERSION)/libgpw-$(MVN_VERSION).jar.sha1
	@echo "\nLocal:  "; shasum bin/libgpw-$(MVN_VERSION).jar | awk '{print $$1}'

check-central:
	curl -s -o /dev/null -w "%{http_code}\n" \
	  https://repo1.maven.org/maven2/com/muquit/libgpw/libgpw/$(MVN_VERSION)/libgpw-$(MVN_VERSION).jar

push:
	git push -u origin main

# check if GITHUB_TOKEN is valid
check_github_token:
	@if [ -z "$(GITHUB_TOKEN)" ]; then \
	    echo "ERROR: GITHUB_TOKEN is not set"; exit 1; \
	fi
	@status=$$(curl -s -o /dev/null -w "%{http_code}" \
	    -H "Authorization: token $(GITHUB_TOKEN)" \
	    https://api.github.com/user); \
	if [ "$$status" != "200" ]; then \
	    echo "ERROR: GITHUB_TOKEN is not working (HTTP $$status)"; exit 1; \
	fi
	@curl -s -H "Authorization: token $(GITHUB_TOKEN)" \
        https://api.github.com/user | jq '{login, name, type}'
	@curl -sI -H "Authorization: token $(GITHUB_TOKEN)" \
        https://api.github.com/user | grep -i x-oauth-scopes

clean:
	mvn clean
