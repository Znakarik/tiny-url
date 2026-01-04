package benchmark;

import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.profile.ExternalProfiler;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;

public class StdOutToFileProfiler implements ExternalProfiler {

    private final Path outputDir;

    public StdOutToFileProfiler() {
        // можно переопределять через -Djmh.stdout.dir=...
        String dir = System.getProperty(
                "jmh.stdout.dir",
                "jmh-stdout"
        );
        this.outputDir = Path.of(dir);
    }

    @Override
    public Collection<String> addJVMInvokeOptions(BenchmarkParams benchmarkParams) {
        return List.of();
    }

    @Override
    public Collection<String> addJVMOptions(BenchmarkParams benchmarkParams) {
        return List.of();
    }

    @Override
    public void beforeTrial(BenchmarkParams benchmarkParams) {

    }

    @Override
    public Collection<? extends Result> afterTrial(BenchmarkResult br, long l, File file, File file1) {

        try {
            Files.createDirectories(outputDir);

            String baseName =
                    br.getParams().getBenchmark()
                            .replace('.', '_');

            copyIfExists(file,
                    outputDir.resolve(baseName + "-stdout.log"));
            copyIfExists(file1,
                    outputDir.resolve(baseName + "-stderr.log"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }

    @Override
    public boolean allowPrintOut() {
        return false; // ❗ не печатать stdout в консоль
    }

    @Override
    public boolean allowPrintErr() {
        return false;
    }

    private void copyIfExists(File src, Path target) throws IOException {
        if (src != null && src.exists()) {
            Files.copy(
                    src.toPath(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    @Override
    public String getDescription() {
        return "Save fork stdout/stderr into separate files";
    }
}