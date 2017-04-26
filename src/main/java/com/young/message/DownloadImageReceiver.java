package com.young.message;

import com.young.model.ZhihuAnswer;
import com.young.zhihu.service.AnswerService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by young on 2017-4-25.
 */
@Component
@RabbitListener(queues = "zhihuAnswer")
public class DownloadImageReceiver {
    private ExecutorService executorService;

    @Autowired
    private AnswerService answerService;

    @PostConstruct
    public void init() {
        executorService = Executors.newCachedThreadPool();
    }


    @RabbitHandler
    public void process(List<ZhihuAnswer> zhihuAnswer) {
        answerService.downloadImages(zhihuAnswer);
    }
}
