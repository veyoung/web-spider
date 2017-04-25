package com.young.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import com.young.model.Answer;

/**
 * Created by young on 2017-4-24.
 */
@Component("answerSearchRepository")
public interface AnswerSearchRepository extends ElasticsearchRepository<Answer, Long> {
}
