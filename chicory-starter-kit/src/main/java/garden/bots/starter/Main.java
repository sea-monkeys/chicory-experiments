package garden.bots.starter;

import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.Parser;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.wasi.WasiOptions;
import com.dylibso.chicory.wasi.WasiPreview1;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/*
mvn compile exec:java -Dexec.mainClass="garden.bots.starter.Main"
mvn compile exec:java
 */
public class Main {
    public static void main(String[] args) {

        // Check environment variables
        var wasmFileLocalLocation = Optional.ofNullable(System.getenv("WASM_FILE")).orElse("./demo-plugin/demo.wasm");
        var wasmFunctionName = Optional.ofNullable(System.getenv("FUNCTION_NAME")).orElse("hello");


        var wasi = WasiPreview1.builder()
                .withOptions(WasiOptions.builder().inheritSystem().build()) // Enables stdio, env vars, etc.
                .build();

        ImportValues imports = ImportValues.builder()
                .addFunction(wasi.toHostFunctions())
                .build();

        Instance instance = Instance.builder(Parser.parse(new File(wasmFileLocalLocation)))
                .withImportValues(imports) // Pass WASI imports
                .build();


        ExportFunction wasmFunction = instance.export(wasmFunctionName);

         ExportFunction alloc;
         ExportFunction dealloc;
         
         try {
            alloc = instance.export("alloc"); // Rust export (manually added to the Rust code)
            dealloc = instance.export("dealloc"); // Rust export (manually added to the Rust code)
        } catch (com.dylibso.chicory.wasm.InvalidException eRust) {
            System.out.println("alloc/dealloc not found, trying malloc/free...");
            try {
                alloc = instance.export("malloc"); // TinyGo export (automatically expoted by TinyGo compiler)
                dealloc = instance.export("free"); // TinyGo export (automatically expoted by TinyGo compiler)
            } catch (com.dylibso.chicory.wasm.InvalidException eTinygo) {
                throw new RuntimeException("No valid memory management functions found in Wasm module.");
            }
        }
        
        /*
         * To pass it a string, we first need to write the string into the module'smemory.
         */
        Memory memory = instance.memory();
        String message = "Bob";
        int len = message.getBytes().length; // size of the string
        int ptr = (int) alloc.apply(len)[0]; // "position" of the string in the module's memory (reserved space with alloc)
        // We can now write the message to the module's memory:
        memory.writeString(ptr, message);

        /*
         * Now we can call wasmFunction with the position and the size.
         * We will call dealloc to free that memory in the module:
         */

        // Call the wasm function
        var result = wasmFunction.apply(ptr, len)[0];
        dealloc.apply(ptr, len);

        // Extract position and size from the result

        /*
         * Extracting a memory position from a 64-bit value:
         * This line is performing bitwise operations to extract the high 32 bits from a 64-bit value. 
         * 32 means we're shifting 32 bits to the right, effectively moving the high 32 bits to the low position
         * & 0xFFFFFFFFL performs a bitwise AND with a long mask to ensure we only get the last 32 bits
         */
        int positionInMemory = (int) ((result >>> 32) & 0xFFFFFFFFL);

        /*
         * Extracting the size portion from a 64-bit value where:
         * This line extracts the lower 32 bits from a 64-bit value.
         * & 0xFFFFFFFFL is a bitwise AND operation with a long mask that keeps only the lower 32 bits
         * 0xFFFFFFFFL is a 32-bit mask with all bits set to 1
         * The L suffix makes it a long literal
         */
        int sizeImMemory = (int) (result & 0xFFFFFFFFL);

        // Read the string from the module's memory
        byte[] bytes = memory.readBytes(positionInMemory, sizeImMemory);
        String strResult = new String(bytes, StandardCharsets.UTF_8);

        System.out.println(strResult);

    }
}