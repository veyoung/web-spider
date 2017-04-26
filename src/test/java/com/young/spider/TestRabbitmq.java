package com.young.spider;

import com.young.MyApplication;
import com.young.message.DownloadImageSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by young on 2017-4-26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyApplication.class)
public class TestRabbitmq {

    @Autowired
    private DownloadImageSender sender;

    @Test
    public void send() {
    }
}
