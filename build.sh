#!/usr/bin/env bash

./sbt clean test package universal:package-zip-tarball
rm -rf bin lib header-extractor
tar zxf target/universal/header-extractor-0.1-SNAPSHOT.tgz
mv header-extractor-0.1-SNAPSHOT/* .
rm -rf header-extractor-0.1-SNAPSHOT
