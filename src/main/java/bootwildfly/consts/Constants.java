package bootwildfly.consts;

/**
 * @author houguangqiang
 * @date 2018-09-10
 * @since 1.0
 */
public interface Constants {

    String COOKIE_UID_KEY = "uid";
    String COOKIE_UID_KEY_INDEX = String.format(" %s=", COOKIE_UID_KEY);
    String COOKIE_EMAIL_KEY = "email";
    String COOKIE_EMAIL_KEY_INDEX = String.format(" %s=", COOKIE_EMAIL_KEY);


    String HEADER_USER_AGENT_KEY = "user-agent";
    String HEADER_USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";

    String HEADER_REFERER_KEY = "referer";
    String HEADER_REFERER_VALUE = "https://v3.mimi.ooo/user";
}
