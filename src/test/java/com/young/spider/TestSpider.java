package com.young.spider;

import com.young.MyApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.young.utils.IdGenerator;
import com.young.mapper.AnswerMapper;
import com.young.model.Answer;
import com.young.zhihu.service.CollectionService;
import com.young.zhihu.service.SpiderService;

import java.io.IOException;
import java.util.Set;

/**
 * Created by young on 2017-4-21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyApplication.class)
public class TestSpider {

    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private SpiderService spiderService;
    @Autowired
    private CollectionService collectionService;

    @Test
    public void testInsertAnswer() {
        Answer answer = new Answer();
        answer.setId(IdGenerator.getInstance().nextId());
        answer.setCommentCount(1);
        answerMapper.insertSelective(answer);
    }

    @Test
    public void saveAnswersToDb() {
        spiderService.saveAnswers("57443806");
    }

    @Test
    public void testDownloadImages() {
        spiderService.downloadImages("57443806");
        try {
          Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCollection() {
        Set<String> questionIds = collectionService.extractQuestionIds("46627456");
        questionIds.forEach(questionId -> spiderService.downloadImages(questionId));
    }
}
