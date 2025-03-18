#!/bin/bash
tinygo build \
  -o hello.wasm \
  -target wasip1 -buildmode=c-shared main.go

ls -lh *.wasm
