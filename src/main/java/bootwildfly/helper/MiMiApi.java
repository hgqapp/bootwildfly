package bootwildfly.helper;

import bootwildfly.model.HeaderModel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author houguangqiang
 * @date 2018-09-11
 * @since 1.0
 */
public class MiMiApi {

    private static final Logger logger = LoggerFactory.getLogger(MiMiApi.class);

    public static String checkin(List<HeaderModel> headers) {
        return checkin(parseHeader(headers));
    }

    public static String checkin(Map<String, String> headers) {
        HttpResponse<JsonNode> userResponse = null;
        try {
            userResponse = Unirest.post("https://v3.mimi.ooo/user/checkin")
                    .headers(headers)
                    .asJson();
            boolean checked = checkResponse(userResponse);
            if (checked) {
                JsonNode body = userResponse.getBody();
                String msg = body.getObject().getString("msg");
                logger.info(msg);
                return msg;
            }
        } catch (UnirestException e) {
            logger.error("Error request: https://v3.mimi.ooo/user/checkin", e);
        }
        return null;
    }

    public static boolean gacheck(List<HeaderModel> headers) {
        return gacheck(parseHeader(headers));
    }

    public static boolean gacheck(Map<String, String> headers) {
        HttpResponse<JsonNode> userResponse = null;
        try {
            userResponse = Unirest.post("https://v3.mimi.ooo/user/gacheck")
                    .headers(headers)
                    .asJson();
            boolean checked = checkResponse(userResponse);
            if (checked) {
                JsonNode body = userResponse.getBody();
                String msg = body.getObject().getString("msg");
                logger.info(msg);
                return true;
            }
        } catch (UnirestException e) {
            logger.error("Error request: https://v3.mimi.ooo/user/gacheck", e);
        }
        return false;
    }

    private static <T>  boolean checkResponse(HttpResponse<T> response) {
        int status = response.getStatus();
        switch (status) {
            case HttpStatus.SC_MOVED_TEMPORARILY : {
                String location = response.getHeaders().getFirst("Location");
                if ("/auth/login".equals(location)) {
                    logger.debug("登陆超时，请重新上传token");
                } else {
                    logger.warn("未知错误, Response: {}", response);
                }
                return false;
            }
            case HttpStatus.SC_NOT_FOUND : {
                logger.warn("请求地址不存在", response);
                return false;
            }
            default:
        }
        return true;
    }

    private static Map<String,String> parseHeader(List<HeaderModel> headers) {
        return headers.stream().collect(Collectors.toMap(HeaderModel::getKey, HeaderModel::getValue));
    }

    private static Set<String> ignoreHeader = new HashSet<>();
    static {
        ignoreHeader.add("accept-encoding");
    }
    private static Map<String, String> parseHeaders(String headers) {
        return Arrays.stream(headers.split("\n"))
                .map(v -> v.trim().split(":\\s*"))
                .filter(v -> v.length == 2 && !ignoreHeader.contains(v[0]))
                .collect(Collectors.toMap(v->v[0], v->v[1]));

    }

    public static void main(String[] args) {
        String headers = ":authority: v3.mimi.ooo\n" +
                ":method: GET\n" +
                ":path: /user\n" +
                ":scheme: https\n" +
                "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                "accept-encoding: gzip, deflate, br\n" +
                "accept-language: zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,mg;q=0.6\n" +
                "cookie: UM_distinctid=1656ab9ce0443-07d8731196259d-9393265-100200-1656ab9ce0525; _ga=GA1.2.2065632965.1535093887; __cfduid=d10a0541a82550431b3ee64854b5331d01536030510; PHPSESSID=8v545eq7ct9092s4egel6lkgnr; uid=137; email=1641438574%40qq.com; key=4c148456ff08fd00eb8610ed70dfff955844e3bcf2c9f; ip=207699b04f3b203ea26ffa531f0f5dc6; expire_in=1536739770\n" +
                "referer: https://v3.mimi.ooo/auth/login\n" +
                "upgrade-insecure-requests: 1\n" +
                "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
        String gacheck = checkin(parseHeaders(headers));
        System.out.println(gacheck);
    }
}
