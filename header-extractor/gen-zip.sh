#!/usr/bin/env bash

echo "Generating Azkaban job file"

./sbt clean test package universal:package-zip-tarball
rm -rf bin lib header-extractor
tar zxf target/universal/header-extractor.tgz
mv header-extractor/* .
rm -rf header-extractor
