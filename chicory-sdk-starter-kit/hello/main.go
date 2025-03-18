package main

import (
"github.com/extism/go-pdk"
)

//go:wasmexport say_hello
func say_hello() int32 {

	// read input
	// read the function argument from the memory
	input := pdk.Input()
		
	// create output
	output := "🎉 Extism is 💜, 🌍, by " + string(input)
	
    pdk.OutputString(output)
    return 0
	
}


/*
func main() {
	//say_hello()
}
*/
