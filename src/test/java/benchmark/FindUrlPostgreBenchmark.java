package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.znakarik.ApplicationEntryPoint;
import ru.znakarik.service.UrlService;

import java.util.Random;

public class FindUrlPostgreBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FindUrlPostgreBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Thread)
    public static class StateClass {
        private ConfigurableApplicationContext context;
        private UrlService urlService;

        @Setup(Level.Trial)
        public void setup() {
            System.setProperty("url.repo.type", "postgres");

            context = SpringApplication.run(ApplicationEntryPoint.class);
            urlService = context.getBean(UrlService.class);

            // заполни кэш данными
            for (int i = 0; i < 10; i++) {
                urlService.create("short-url" + new Random().nextLong());
            }
        }

        @Benchmark
        @Fork(value = 1)
        public void run() {
            urlService.getAll();
        }

        @TearDown(Level.Trial)
        public void tearDown() {
            context.close();
        }
    }

}
