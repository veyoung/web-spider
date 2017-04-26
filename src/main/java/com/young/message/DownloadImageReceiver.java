package com.young.message;

import com.young.model.ZhihuAnswer;
import com.young.zhihu.service.SpiderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by young on 2017-4-25.
 */
@Component
@RabbitListener(queues = "zhihuAnswer")
public class DownloadImageReceiver {

    @Autowired
    private SpiderService spiderService;

    @RabbitHandler
    public void process(List<ZhihuAnswer> zhihuAnswer) {
        spiderService.downloadImages(zhihuAnswer);
    }
}
