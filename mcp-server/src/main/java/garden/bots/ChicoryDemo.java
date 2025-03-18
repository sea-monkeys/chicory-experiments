package garden.bots;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.dylibso.chicory.wasi.WasiOptions;
import org.extism.sdk.chicory.Manifest;
import org.extism.sdk.chicory.ManifestWasm;
import org.extism.sdk.chicory.Plugin;
import org.wildfly.mcp.api.Tool;
import org.wildfly.mcp.api.ToolArg;

public class ChicoryDemo {

    @Tool(description = "Vulcan Salutation from Moonbit")
    public String vulcan_salute(@ToolArg(description = "JSON string with The firstName and lastName") String arguments) {
        try  {
            var wasmFileLocalLocation = Optional.ofNullable(System.getenv("WASM_FILE")).orElse("./plugins/main.wasm");
            var wasmFunctionName = Optional.ofNullable(System.getenv("FUNCTION_NAME")).orElse("vulcan_salute");

            var options = new Manifest.Options().withWasi(WasiOptions.builder().build());
            var wasm = ManifestWasm.fromPath(wasmFileLocalLocation).build();
            var manifest = Manifest.ofWasms(wasm).withOptions(options).build();
            var plugin = Plugin.ofManifest(manifest).build();

            // "{\"firstName\":\"Bob\", \"lastName\":\"Morane\"}"
            var resultStr = new String(plugin.call(wasmFunctionName, arguments.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

            return resultStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}