package benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;

public class BenchmarkTest {

    public static void main(String[] args) throws IOException, RunnerException {
        Options opt = new OptionsBuilder()
                .addProfiler(StdOutToFileProfiler.class)
                .include(BenchmarkTest.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Thread)
     public static class UnderTestClass {
        private Integer a;
        private Integer b;

        @Setup
        public void setup() {
            a = Integer.valueOf(7);
            b = Integer.valueOf(3);
        }

        @Benchmark
        public int plus() {
            return a + b;
        }
    }
}
