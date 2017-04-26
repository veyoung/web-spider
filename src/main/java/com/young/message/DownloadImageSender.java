package com.young.message;

import com.young.model.ZhihuAnswer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by young on 2017-4-25.
 */
@Component
public class DownloadImageSender {
    private static final String QUEUE_NAME = "zhihuAnswer";

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(List<ZhihuAnswer> zhihuAnswers) {
        this.rabbitTemplate.convertAndSend(QUEUE_NAME, zhihuAnswers);
    }
}
