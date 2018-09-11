package bootwildfly;

import bootwildfly.consts.Constants;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @author houguangqiang
 * @date 2018-09-07
 * @since 1.0
 */
@RestController
public class MiMiResourceController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String loginHtml() throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.get("https://v3.mimi.ooo/auth/login")
                .header("accept", "*/*")
                .header(Constants.HEADER_USER_AGENT_KEY, Constants.HEADER_USER_AGENT_VALUE)
                .asString();
        return httpResponse.getBody();
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public String login(@RequestParam("email") String email,
                        @RequestParam("passwd") String passwd,
                        @RequestParam(value = "remember_me", required = false) String rememberMe,
                        @RequestParam("geetest_challenge") String geetestChallenge,
                        @RequestParam("geetest_validate") String geetestValidate,
                        @RequestParam("geetest_seccode") String geetestSeccode) throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.post("https://v3.mimi.ooo/auth/login")
                .header("accept", "application/json, text/javascript, */*; q=0.01")
                .header(Constants.HEADER_USER_AGENT_KEY, Constants.HEADER_USER_AGENT_VALUE)
                .asString();
        System.out.println(httpResponse.getBody());
        HttpResponse<String> userResponse = Unirest.get("https://v3.mimi.ooo/user")
                .header("accept", "*/*")
                .header(Constants.HEADER_USER_AGENT_KEY, Constants.HEADER_USER_AGENT_VALUE)
                .asString();
        return userResponse.getBody();
    }

    @RequestMapping("/theme/**")
    public String theme(HttpServletRequest request) throws UnirestException {
        String path = request.getServletPath();
        HttpResponse<String> httpResponse = Unirest.get(String.format("https://v3.mimi.ooo%s", path))
                .header("accept", "*/*")
                .header(Constants.HEADER_USER_AGENT_KEY, Constants.HEADER_USER_AGENT_VALUE)
                .asString();
        return httpResponse.getBody();
    }

    @RequestMapping("/assets/**")
    public String assets(HttpServletRequest request) throws UnirestException {
        String path = request.getServletPath();
        HttpResponse<String> httpResponse = Unirest.get(String.format("https://v3.mimi.ooo%s", path))
                .header("accept", "*/*")
                .header(Constants.HEADER_USER_AGENT_KEY, Constants.HEADER_USER_AGENT_VALUE)
                .asString();
        return httpResponse.getBody();
    }

    @RequestMapping("/cdn-cgi/**")
    public String cdnCgi(HttpServletRequest request) throws UnirestException {
        String path = request.getServletPath();
        HttpResponse<String> httpResponse = Unirest.get(String.format("https://v3.mimi.ooo%s", path))
                .header("accept", "*/*")
                .header(Constants.HEADER_USER_AGENT_KEY, Constants.HEADER_USER_AGENT_VALUE)
                .asString();
        return httpResponse.getBody();
    }

    @RequestMapping("/images/**")
    public StreamingResponseBody images(HttpServletRequest request, HttpServletResponse response) throws UnirestException {
        String path = request.getServletPath();
        HttpResponse<InputStream> httpResponse = Unirest.get(String.format("https://v3.mimi.ooo%s", path))
                .header("accept", "*/*")
                .header(Constants.HEADER_USER_AGENT_KEY, Constants.HEADER_USER_AGENT_VALUE)
                .asBinary();
        InputStream body = httpResponse.getBody();
        Headers headers = httpResponse.getHeaders();
        headers.forEach((k, v) -> {
            if (v != null && !v.isEmpty()) {
                response.addHeader(k, v.get(0));
            }
        });
        return outputStream -> {
            byte[] buff = new byte[1024];
            int count = 0;
            while ((count = body.read(buff)) != -1) {
                outputStream.write(buff, 0, count);
            }
            outputStream.flush();
        };
    }

}
