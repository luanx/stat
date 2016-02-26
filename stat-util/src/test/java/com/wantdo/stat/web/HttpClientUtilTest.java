package com.wantdo.stat.web;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ Date : 15/10/7
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class HttpClientUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtilTest.class);

    @Test
    public void testGet(){
        String url = "http://www.17track.net/r/handlertrack.ashx?callback=express" +
                "&num=LS084967881CN&pt=0&cm=0&cc=0&_=1444265424156";
        String responseText = HttpClientUtil.get(url);
        responseText = responseText.substring(8, responseText.length()-1);
        JsonObject jsonObject = new JsonParser().parse(responseText).getAsJsonObject();
        JsonObject jsonElement =(JsonObject) jsonObject.get("dat");
        JsonObject z0 = (JsonObject) jsonElement.get("z0");
        System.out.println(z0.get("a"));
    }
}
