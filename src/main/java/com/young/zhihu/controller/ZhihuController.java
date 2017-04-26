package com.young.zhihu.controller;

import com.young.zhihu.service.AnswerService;
import com.young.zhihu.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by young on 2017-4-24.
 */
@RestController
@RequestMapping("/zhihu")
public class ZhihuController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private CollectionService collectionService;

    @RequestMapping("index")
    public String index() {
        return "hello world...";
    }

    @RequestMapping("/{questionId}/download")
    public String downloadImages(
            @PathVariable("questionId") String qustionId) {

        answerService.listAnswers(qustionId, true);
        return "success";
    }

}
