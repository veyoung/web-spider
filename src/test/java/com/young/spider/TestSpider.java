package com.young.spider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utils.IdGenerator;
import zhihu.ZhihuApplication;
import zhihu.com.young.mapper.AnswerMapper;
import zhihu.com.young.model.Answer;
import zhihu.com.young.service.CollectionService;
import zhihu.com.young.service.SpiderService;

import java.util.Set;

/**
 * Created by young on 2017-4-21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ZhihuApplication.class)
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
        spiderService.downloadImages("26037846");
    }

    @Test
    public void testCollection() {
        Set<String> questionIds = collectionService.extractQuestionIds("46627456");
        questionIds.forEach(questionId -> spiderService.downloadImages(questionId));
    }
}
