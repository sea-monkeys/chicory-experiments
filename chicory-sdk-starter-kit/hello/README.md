# Build the plugin

**Build**:
```bash
tinygo build -scheduler=none --no-debug \
  -o hello.wasm \
  -target wasi main.go
```

**Run**:
```bash
extism call hello.wasm say_hello \
  --input "Bob Morane" \
  --log-level "info" \
  --wasi
```

## Build with Docker

```bash
docker run --rm -v "$PWD":/hello -w /hello k33g/wasm-builder:0.0.3 \
  tinygo build -scheduler=none --no-debug \
    -o hello.wasm \
    -target wasi main.go
```

## Run with Docker

```bash
docker run --rm -v "$PWD":/hello -w /hello k33g/wasm-builder:0.0.3 \
  extism call hello.wasm say_hello \
  --input "Bob Morane" \
  --log-level "info" \
  --wasi
```

