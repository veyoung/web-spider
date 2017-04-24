package zhihu.com.young.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by young on 2017-4-24.
 */
@RestController
public class SpiderController {

    @RequestMapping("index")
    public String index() {
        return "hello world...";
    }
}
