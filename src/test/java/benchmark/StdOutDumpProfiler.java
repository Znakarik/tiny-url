package benchmark;

import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.profile.ExternalProfiler;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class StdOutDumpProfiler implements ExternalProfiler {

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
    public Collection<? extends Result> afterTrial(BenchmarkResult benchmarkResult, long l, File file, File file1) {
        System.out.println("STDOUT file: " + file);
        System.out.println("STDERR file: " + file1);
        return List.of();
    }

    @Override
    public boolean allowPrintOut() {
        return true; // разрешаем печать stdout
    }

    @Override
    public boolean allowPrintErr() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Dump stdout/stderr";
    }
}
