package com.young.spider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utils.IdGenerator;
import zhihu.ZhihuApplication;
import zhihu.com.young.elasticsearch.AnswerSearchRepository;
import zhihu.com.young.model.Answer;

/**
 * Created by young on 2017-4-21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ZhihuApplication.class)
public class TestElastic {

    @Autowired
    private AnswerSearchRepository articleSearchRepository;
    @Test
    public void testSaveAnswerIndex(){
        Answer answer = new Answer();
        answer.setId(IdGenerator.getInstance().nextId());
        answer.setCommentCount(1);

        articleSearchRepository.save(answer);
    }
}
