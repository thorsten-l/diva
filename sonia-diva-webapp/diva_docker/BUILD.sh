#!/bin/bash
cd ..
mvn -P development clean install
cp target/diva.jar diva_docker/diva
