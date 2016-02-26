#!/bin/bash

echo "[PreRequirment] Download last version of yuicompressor from https://github.com/yui/yuicompressor/releases and put it here."

java -jar yuicompressor-*.jar -o  '../src/main/webapp/static/css/*.css$:-min.css'  ../src/main/webapp/static/css/*.css
java -jar yuicompressor-*.jar -o  '../src/main/webapp/static/js/*.js:-min.js'  ../src/main/webapp/static/js/*.js