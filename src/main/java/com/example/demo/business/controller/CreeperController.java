package com.example.demo.business.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/creeper")
public class CreeperController {

    @RequestMapping(value = "")
    public void data(){
        String url = "https://www.baidu.com/#iact=wiseindex%2Ftabs%2Fnews%2Factivity%2Fnewsdetail%3D%257B%2522linkData%2522%253A%257B%2522name%2522%253A%2522iframe%252Fmib-iframe%2522%252C%2522id%2522%253A%2522feed%2522%252C%2522index%2522%253A0%252C%2522url%2522%253A%2522https%253A%252F%252Fmbd.baidu.com%252Fnewspage%252Fdata%252Flandingpage%253Fs_type%253Dnews%2526dsp%253Dwise%2526context%253D%25257B%252522nid%252522%25253A%252522news_10128509980442960602%252522%25257D%2526pageType%253D1%2526n_type%253D1%2526p_from%253D-1%2526innerIframe%253D1%2522%252C%2522title%2522%253Anull%257D%257D";
//        HttpRequest get = HttpUtil.createGet(url);
        String html = HttpUtil.get(url);

        System.err.println("");
    }
}
