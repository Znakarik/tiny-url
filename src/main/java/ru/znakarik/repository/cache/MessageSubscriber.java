package ru.znakarik.repository.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<>();
    @Override
    public void onMessage(Message message, byte[] pattern) {
        messageList.add(message.toString());
        log.info("Message received: {}", new String(message.getBody()));
    }
}
