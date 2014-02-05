#!/usr/bin/env bash

rm -rf bin lib header-replacer && sync
./sbt clean test package universal:package-zip-tarball && sync
tar zxf target/universal/header-replacer-0.1-SNAPSHOT.tgz && sync
mv header-replacer-0.1-SNAPSHOT/* . && sync
rm -rf header-replacer-0.1-SNAPSHOT && sync
