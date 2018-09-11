package bootwildfly;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author houguangqiang
 * @date 2018-09-07
 * @since 1.0
 */
@RestController
@RequestMapping("/mimi")
public class MiMiController {

    private static Set<String> ignoreHeader = new HashSet<>();

    static {
        ignoreHeader.add("accept-encoding");
    }

    @RequestMapping("/user")
    public String index() throws UnirestException {
        HttpResponse<String> userResponse = Unirest.get("https://v3.mimi.ooo/user")
                .headers(parseHeader())
                .asString();
        return userResponse.getBody();
    }

    @RequestMapping("/user/checkin")
    public String checkin() throws UnirestException {
        HttpResponse<JsonNode> userResponse = Unirest.post("https://v3.mimi.ooo/user/checkin")
                .headers(parseHeader())
                .asJson();
        JsonNode body = userResponse.getBody();
        return body.getObject().getString("msg");
    }

    @RequestMapping("/user/gacheck")
    public String gacheck() throws UnirestException {
        HttpResponse<JsonNode> userResponse = Unirest.post("https://v3.mimi.ooo/user/gacheck")
                .headers(parseHeader())
                .asJson();
        JsonNode body = userResponse.getBody();
        return body.getObject().getString("msg");
    }

    public Map<String, String> parseHeader() {
        String h = ":authority: v3.mimi.ooo\n" +
                ":method: GET\n" +
                ":path: /user\n" +
                ":scheme: https\n" +
                "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                "accept-encoding: gzip, deflate, br\n" +
                "accept-language: zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,mg;q=0.6\n" +
                "cookie: UM_distinctid=1656ab9ce0443-07d8731196259d-9393265-100200-1656ab9ce0525; _ga=GA1.2.2065632965.1535093887; __cfduid=d10a0541a82550431b3ee64854b5331d01536030510; PHPSESSID=8v545eq7ct9092s4egel6lkgnr; uid=186; email=309259716%40qq.com; key=9bc162ae893c84f19b1a09417819ea68bb05b89fccdc1; ip=ce4ccedf2e722db5d5f4f247f32f2792; expire_in=1536401568\n" +
                "referer: https://v3.mimi.ooo/user\n" +
                "upgrade-insecure-requests: 1\n" +
                "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
        String[] split = h.split("\n");
        Map<String, String> header = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            String[] item = split[i].trim().split(":\\s*");
            if (item.length == 2) {
                if (ignoreHeader.contains(item[0])) {
                    continue;
                }
                header.put(item[0], item[1]);
            }
        }
        return header;
    }

}
