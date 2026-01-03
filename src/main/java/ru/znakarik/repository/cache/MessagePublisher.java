package ru.znakarik.repository.cache;

public interface MessagePublisher {
    void publish(final String message);
}
