package httplibrary;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpCallerTest {
    Logger log = LoggerFactory.getLogger(HttpCallerTest.class);

    @Test
    public void restTemplateTest() throws InterruptedException {
        String url = new String("https://api.github.com/search/code?q=code+language:java"+"&per_page=100&page=1");

        // Auth
        String auth = "Pudding124" + ":" + "4c8e0e642";
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );

        try {
            // Request
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            headers.add("Retry-After","3");
            headers.set("Accept","application/vnd.github.v3.text-match + json");
            headers.set("Authorization", authHeader);
            HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", headers);
            log.info("等待 5 秒...");
            Thread.sleep(5000);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        }catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
            log.info("停止 20 秒");
            Thread.sleep(20000);
        }
    }
}
