#!/bin/bash
extism call addition.wasm add \
  --input '{"left":30, "right":12}' \
  --log-level "info" \
  --wasi
echo ""
