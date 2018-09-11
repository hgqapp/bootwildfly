package bootwildfly;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.cookie.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

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

    @ResponseBody
    @RequestMapping(value = "/ssr", method = RequestMethod.GET)
    public String ssr() throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.get(FREE_SSR_URL)
                .header(":authority", FREE_SSR_AUTHORITY)
                .header(":method", "GET")
                .header(":path", "/")
                .header(":scheme", FREE_SSR_SCHEME)
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,mg;q=0.6")
                .header("cache-control", "max-age=0")
                .header("referer", FREE_SSR_URL + "/")
                .header("upgrade-insecure-requests", "1")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                .asString();
        return httpResponse.getBody();
    }


    private void setCookie(HttpServletResponse response, List<Cookie> cookies){
        if (cookies == null || cookies.isEmpty()) {
            return;
        }
        CookieGenerator cookieGenerator = new CookieGenerator();
        for (Cookie cookie : cookies) {
            cookieGenerator.setCookieName(cookie.getName());
            //cookieGenerator.setCookieDomain(cookie.getDomain());
            cookieGenerator.setCookiePath(cookie.getPath());
            cookieGenerator.setCookieSecure(cookie.isSecure());
            Date expiryDate = cookie.getExpiryDate();
            if (expiryDate != null) {
                cookieGenerator.setCookieMaxAge((int)(expiryDate.getTime() / 1000));
            }
            cookieGenerator.addCookie(response, cookie.getValue());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/cdn-cgi/l/chk_jschl", method = RequestMethod.GET)
    public String check(@RequestParam("jschl_vc") String vc, String pass, @RequestParam("jschl_answer") String answer) throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.get(url(FREE_SSR_URL, "cdn-cgi/l/chk_jschl"))
                .queryString("jschl_vc", vc)
                .queryString("pass", pass)
                .queryString("jschl_answer", answer)
                .header(":authority", FREE_SSR_AUTHORITY)
                .header(":method", "GET")
                .header(":path", String.format("/cdn-cgi/l/chk_jschl?jschl_vc=%s&pass=%s&jschl_answer=%s", vc, pass, answer))
                .header(":scheme", FREE_SSR_SCHEME)
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,mg;q=0.6")
                .header("referer", FREE_SSR_URL + "/")
                .header("upgrade-insecure-requests", "1")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                .asString();
        if (HttpStatus.valueOf(httpResponse.getStatus()) == HttpStatus.FOUND) {
            return "redirect:/ssr";
        }
        return httpResponse.getBody();
    }

    @ResponseBody
    @RequestMapping(value = "ss.json", method = RequestMethod.GET)
    public String ss(@RequestParam("_") String time) throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.get(url(FREE_SSR_URL, "ss.json"))
                .header(":authority", FREE_SSR_AUTHORITY)
                .header(":method", "GET")
                .header(":path", "/ss.json?_=" + time)
                .header(":scheme", FREE_SSR_SCHEME)
                .header("accept", "application/json, text/javascript, */*; q=0.01")
                .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,mg;q=0.6")
                .header("origin", FREE_SSR_URL)
                .header("referer", FREE_SSR_URL + "/")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                .asString();

        return httpResponse.getBody();
    }

    @ResponseBody
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
                .header("referer", FREE_SSR_URL + "/")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                .asString();
        return httpResponse.getBody();
    }

    private String url(String... url) {
        return String.join("/", url);
    }
}
