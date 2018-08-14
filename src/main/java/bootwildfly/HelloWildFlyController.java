package bootwildfly;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class HelloWildFlyController {

    private static final Logger logger = LoggerFactory.getLogger(HelloWildFlyController.class);

    private static final String FREE_SSR_DATA_URL = "https://free-ss.tk/ss.json?_=1534225740278";
    private static final String FREE_SSR_SCHEME = "https";
    private static final String FREE_SSR_AUTHORITY = "free-ss.tk";
    private static final String FREE_SSR_PATH = "/ss.json";

    @RequestMapping("hello")
    public String sayHello(){
        return ("Hello, SpringBoot on Wildfly");
    }

    @RequestMapping("getSite")
    public String getSite() throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.get("https://free-ss.site").asString();
        return httpResponse.getBody();
    }

    @RequestMapping("getTk")
    public String getTk() throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.get("https://free-ss.tk").asString();
        return httpResponse.getBody();
    }

    @RequestMapping("get")
    public String get(String url) throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.get(url).asString();
        return httpResponse.getBody();
    }

    @RequestMapping("getSSR")
    public String getSSR(@RequestParam(required = false, defaultValue = "10") Integer count) throws UnirestException {
        String path = buildPath();
        String url = buildURL(FREE_SSR_SCHEME, FREE_SSR_AUTHORITY, path);
        HttpResponse<JsonNode> httpResponse = doRequest(url, path);
        JSONObject root = httpResponse.getBody().getObject();
        JSONArray data = root.getJSONArray("data");
        int length = data.length();
        int index = 0;
        StringBuilder result = new StringBuilder();
        String groupName = buildGroupName();
        for (int i = 0; i < length; i++) {
            if (index >= count) {
                break;
            }
            JSONArray array = data.getJSONArray(i);
            if (support(array.getString(0))) {
                result.append("ssr://");
                StringBuilder item = new StringBuilder();
                item.append(array.getString(1))
                        .append(":")
                        .append(array.getString(2))
                        .append(":origin:")
                        .append(array.getString(4))
                        .append(":plain:")
                        .append(base64(array.getString(3)))
                        .append("/?obfsparam=&remarks=")
                        .append(base64(array.getString(6) + "_" + array.getString(5)))
                        .append("&group=")
                        .append(groupName);
                result.append(base64(item.toString()).replace("/", "_"));
                result.append(System.lineSeparator());
                index++;
            }
        }
        System.out.println(result);
        return root.toString();
    }

    private String base64(String str){
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8)).replace("=", "");
    }

    private boolean support(String value){
        return "10/10/10/10".equals(value);
    }

    private String buildGroupName(){
        String groupName = "动态节点";
        return Base64.getEncoder().encodeToString(groupName.getBytes(StandardCharsets.UTF_8));
    }

    private HttpResponse<JsonNode> doRequest(String url, String path) throws UnirestException {
        logger.debug("请求URL:{}", url);
        return Unirest.get(url)
                .header(":authority", FREE_SSR_AUTHORITY)
                .header(":method", "GET")
                .header(":path", path)
                .header(":scheme", FREE_SSR_SCHEME)
                .header("accept", "application/json, text/javascript, */*; q=0.01")
                .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,mg;q=0.6")
                .header("referer", "https://free-ss.tk/")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                .asJson();
    }

    private String buildURL(String scheme, String authority, String path){
        StringBuilder sb = new StringBuilder();
        sb
                .append(scheme)
                .append("://")
                .append(authority)
                .append(path);
        return sb.toString();
    }

    private String buildPath() {
        return FREE_SSR_PATH + "?_=" + System.currentTimeMillis();
    }
}