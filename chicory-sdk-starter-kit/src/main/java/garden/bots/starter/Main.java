package garden.bots.starter;

import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.Parser;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.wasi.WasiOptions;
import com.dylibso.chicory.wasi.WasiPreview1;
import org.extism.sdk.chicory.Manifest;
import org.extism.sdk.chicory.ManifestWasm;
import org.extism.sdk.chicory.Plugin;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/*
mvn compile exec:java -Dexec.mainClass="garden.bots.starter.Main"
mvn compile exec:java
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Check environment variables
            var wasmFileLocalLocation = Optional.ofNullable(System.getenv("WASM_FILE")).orElse("./greetings-prj/target/wasm/release/build/main/main.wasm");
            var wasmFunctionName = Optional.ofNullable(System.getenv("FUNCTION_NAME")).orElse("vulcan_salute");

            //var wasmFileLocalLocation = Optional.ofNullable(System.getenv("WASM_FILE")).orElse("./hello/hello.wasm");
            //var wasmFunctionName = Optional.ofNullable(System.getenv("FUNCTION_NAME")).orElse("say_hello");

            //var wasmFileLocalLocation = Optional.ofNullable(System.getenv("WASM_FILE")).orElse("./addition/addition.wasm");
            //var wasmFunctionName = Optional.ofNullable(System.getenv("FUNCTION_NAME")).orElse("add");

            System.out.println("wasmFileLocalLocation: "+wasmFileLocalLocation);
            System.out.println("wasmFunctionName: "+wasmFunctionName);

            var options = new Manifest.Options().withWasi(WasiOptions.builder().build());

            var wasm = ManifestWasm.fromPath(wasmFileLocalLocation).build();

            var manifest = Manifest.ofWasms(wasm).withOptions(options).build();

            var plugin = Plugin.ofManifest(manifest).build();

            //var message = "Bob";
            //var result = plugin.call(wasmFunctionName, message.getBytes(StandardCharsets.UTF_8));

            var resultStr = new String(plugin.call(wasmFunctionName, "{\"firstName\":\"Bob\", \"lastName\":\"Morane\"}".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);


            //var resultStr = new String(result, StandardCharsets.UTF_8);
            System.out.println(resultStr);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}