package com.example.demo;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.business.util.StringUtils;
import com.example.demo.system.baidu.auth.AuthService;
import com.example.demo.system.baidu.ocr.GeneralBasic;
import com.example.demo.system.baidu.tts.AudioTts;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;

/**
 * 自动化测试
 */
public class WebDriverTest {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String getOcrAuth(boolean exist){
        String auth = "";
//        if (exist){
//            auth = AuthService.getAuth();
//            redisTemplate.opsForValue().set("ocrAuth",auth, 60000);
//        }else{
//            auth = (String) redisTemplate.opsForValue().get("ocrAuth");
//            if (StringUtils.isBlank(auth)){
//                auth = AuthService.getAuth();
//                redisTemplate.opsForValue().set("ocrAuth", auth, 60000);
//            }
//        }
//        auth = AuthService.getAuth();
        auth = "24.fd34a3606546ef04b04a7b269898efdf.2592000.1622276566.282335-16434676";
        System.err.println("auth:" + auth);
        return auth;
    }

    private void tts(){
        String token = "24.c793556a5d57a4903a95e594ea44211e.2592000.1622812933.282335-24111520";
        AudioTts.transform(token,"你好，我是志琳","get");
    }

    public static void main(String[] args) throws IOException {

        WebDriverTest webDriverTest = new WebDriverTest();
        webDriverTest.tts();
        if (true){
            return;
        }

        String ocrAuth = webDriverTest.getOcrAuth(false);
//        AuthService.getTtsAuth();


        System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");


        WebDriver driver = new ChromeDriver();

        driver.get("http://localhost/");
        driver.manage().window().maximize();

        WebElement element = driver.findElement(By.className("username")).findElement(By.tagName("input"));
        WebElement element1 = driver.findElement(By.className("password")).findElement(By.tagName("input"));
        WebElement element2 = driver.findElement(By.className("randCode")).findElement(By.tagName("input"));
        WebElement submitButtonElement = driver.findElement(By.className("submitButton"));
        WebElement randCodeImage = driver.findElement(By.id("randCodeImage"));

        if (element != null){
            CharSequence[] csUser = new CharSequence[1];
            csUser[0] = "easeok";
            element.sendKeys(csUser);
        }
        if (element1 != null){
            CharSequence[] csPW = new CharSequence[1];
            csPW[0] = "easeok.2018";
            element1.sendKeys(csPW);
        }
        String randCode = "";
        if (randCodeImage != null){
            File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            // Get entire page screenshot
            BufferedImage fullImg= ImageIO.read(screenshot);
            // Get the location of element on the page
            org.openqa.selenium.Point point= randCodeImage.getLocation();
            // Get width and height of the element
            int eleWidth= randCodeImage.getSize().getWidth();
            int eleHeight= randCodeImage.getSize().getHeight();
            // Crop the entire page screenshot to get only element screenshot
            BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
            ImageIO.write(eleScreenshot, "png", screenshot);
            // Copy the element screenshot to disk
            String filePath = "d:/test1.png";
            File screenshotLocation= new File(filePath);
            FileUtil.copyFile(screenshot, screenshotLocation, StandardCopyOption.REPLACE_EXISTING);

            String ocrResult = GeneralBasic.generalBasic(filePath, ocrAuth);
            JSONObject jsonObject = JSONObject.parseObject(ocrResult);
            JSONArray jsonArray = (JSONArray) jsonObject.get("words_result");
            jsonObject = jsonArray.getJSONObject(0);
            String words = jsonObject.getString("words");
            randCode = words.replaceAll(" ","");

            System.err.println("ocr:" + randCode);
        }
        if (element2 != null){
            element2.sendKeys(randCode);
        }

        System.out.println("The testing page title is: " + driver.getTitle());
        //driver.quit();
    }


}
