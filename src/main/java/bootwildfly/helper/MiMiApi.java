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

}
