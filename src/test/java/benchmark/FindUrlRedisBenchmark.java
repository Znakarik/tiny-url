package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.znakarik.ApplicationEntryPoint;
import ru.znakarik.db.model.url.UrlPOJO;
import ru.znakarik.repository.UrlRepository;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class FindUrlRedisBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FindUrlRedisBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Thread)
    public static class RedisState {
        private ConfigurableApplicationContext context;

        private UrlRepository urlRepository;

        @Setup(Level.Trial)
        public void setup() {
            System.setProperty("url.repo.type", "redis");

            context = SpringApplication.run(ApplicationEntryPoint.class);

            urlRepository = context.getBean(UrlRepository.class);
            // заполни кэш данными
            for (int i = 0; i < 10; i++) {
                urlRepository.create(
                        UUID.randomUUID().toString(),
                        String.valueOf(new Random().nextLong()),
                        "short-url" + new Random().nextLong(),
                        UrlPOJO.DATE_FORMAT.format(new Date())
                );
            }
        }

        @Benchmark
        @Fork(value = 1)
        public void run() {
            urlRepository.getAllUrls();
        }

        @TearDown(Level.Trial)
        public void tearDown() {
            context.close();
        }
    }

}
