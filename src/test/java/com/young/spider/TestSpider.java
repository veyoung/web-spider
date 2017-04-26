package com.young.spider;

import com.young.MyApplication;
import com.young.zhihu.service.AnswerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.young.utils.IdGenerator;
import com.young.mapper.AnswerMapper;
import com.young.model.Answer;
import com.young.zhihu.service.CollectionService;

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
    private AnswerService answerService;
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
        answerService.saveAndIndexAnswers("57443806");
    }

    @Test
    public void testDownloadImages() {
        String questionId = "28641536";
        System.out.println("start test download images of question["+ questionId+"].....");
        answerService.listAnswers(questionId, true);
    }

    @Test
    public void testCollection() {
        Set<String> questionIds = collectionService.extractQuestionIds("46627456");
        questionIds.forEach(questionId -> answerService.listAnswers(questionId, true));
    }
}
