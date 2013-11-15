#!/usr/bin/env bash

./sbt clean test package universal:package-zip-tarball
rm -rf bin lib gen-random-file
tar zxf target/universal/gen-random-file-0.1-SNAPSHOT.tgz
mv gen-random-file-0.1-SNAPSHOT/* .
rm -rf gen-random-file-0.1-SNAPSHOT
