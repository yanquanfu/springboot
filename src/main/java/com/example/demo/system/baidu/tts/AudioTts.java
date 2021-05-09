package com.example.demo.system.baidu.tts;

import com.example.demo.business.util.StringUtils;
import com.example.demo.system.baidu.util.HttpUtil;

import java.net.URLEncoder;

/**
 * 语音合成
 */
public class AudioTts {

    /**
     * 合成语音
     * @param accessToken
     * @param tex
     * @return
     */
    public static String transform(String accessToken, String tex, String method){
        String url = "http://tsn.baidu.com/text2audio?tok=" + accessToken;
        try{
            String params = "&lan=zh&cuid=mine&ctp=1&aue=3&tex=" +
                URLEncoder.encode(URLEncoder.encode(tex, "UTF-8"), "UTF-8");
            String result = "";
            if (StringUtils.isBlank(method) || "post".equalsIgnoreCase(method)){
                result = HttpUtil.post(url, accessToken, params);
            }
            System.out.println(result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
