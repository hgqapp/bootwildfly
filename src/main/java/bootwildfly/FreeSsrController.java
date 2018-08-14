package bootwildfly;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;

/**
 * @author houguangqiang
 * @date 2018-08-14
 * @since 1.0
 */
@Controller
public class FreeSsrController {

    private static final String FREE_SSR_SCHEME = "https";
    private static final String FREE_SSR_AUTHORITY = "free-ss.tk";
    private static final String FREE_SSR_URL = FREE_SSR_SCHEME + "://" + FREE_SSR_AUTHORITY;

    @RequestMapping(value = "ssr", method = RequestMethod.GET)
    public String ssr() throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.get(FREE_SSR_URL).asString();
        return httpResponse.getBody();
    }

    @RequestMapping(value = "ss.json", method = RequestMethod.GET)
    public String ss(String _) throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.get(url(FREE_SSR_URL, "ss.json"))
                .header(":authority", FREE_SSR_AUTHORITY)
                .header(":method", "GET")
                .header(":path", "/ss.json?_=" + _)
                .header(":scheme", FREE_SSR_SCHEME)
                .header("accept", "application/json, text/javascript, */*; q=0.01")
                .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,mg;q=0.6")
                .header("origin", FREE_SSR_URL)
                .header("referer", FREE_SSR_URL)
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                .asString();
        return httpResponse.getBody();
    }

    @RequestMapping(value = "data.php", method = RequestMethod.POST)
    public String data(String a, String b, String c) throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.post(url(FREE_SSR_URL, "data.php"))
                .header(":authority", FREE_SSR_AUTHORITY)
                .header(":method", "POST")
                .header(":path", "/data.php")
                .header(":scheme", FREE_SSR_SCHEME)
                .header("accept", "*/*")
                .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,mg;q=0.6")
                .header("origin", FREE_SSR_URL)
                .header("referer", FREE_SSR_URL)
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                .asString();
        return httpResponse.getBody();
    }

    private String url(String... url) {
        return String.join("/", url);
    }
}
