package bootwildfly.service;

import bootwildfly.UserRepository;
import bootwildfly.consts.Constants;
import bootwildfly.model.HeaderModel;
import bootwildfly.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author houguangqiang
 * @date 2018-09-11
 * @since 1.0
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void updateMsgAndMsgTimeByUid(Long uid, String msg, Long msgTime) {
        userRepository.updateMsgAndMsgTimeByUid(uid, msg, msgTime);
    }

    public void deleteByUid(Long uid) {
        userRepository.delete(uid);
    }

    public Map<Long, List<HeaderModel>> getAllUserHeaders() {
        return userRepository.getAllUserHeaders();
    }

    public Iterable<UserModel> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void saveUser(String headers) {
        Long uid = parseUid(headers);
        String email = parseEmail(headers);
        UserModel user = new UserModel(uid, email, headers);
        List<HeaderModel> headerModels = parseHeaders(uid, headers);
        userRepository.saveUser(user, headerModels);
    }

    private static Set<String> ignoreHeader = new HashSet<>();

    static {
        ignoreHeader.add("accept-encoding");
    }

    private List<HeaderModel> parseHeaders(Long uid, String headers) {
        return Arrays.stream(headers.split("\n"))
                .map(v -> v.trim().split(":\\s*"))
                .filter(v -> v.length == 2 && !ignoreHeader.contains(v[0]))
                .map(v -> new HeaderModel(v[0], v[1], uid))
                .collect(Collectors.toList());

    }

    private Long parseUid(String headers) {
        try {
            int start = headers.indexOf(Constants.COOKIE_UID_KEY_INDEX) + Constants.COOKIE_UID_KEY_INDEX.length();
            int end = headers.indexOf(';', start);
            return Long.valueOf(headers.substring(start, end));
        } catch (Exception e) {
            throw new NullPointerException("Not support headers!!!");
        }
    }

    private String parseEmail(String headers) {
        try {
            int start = headers.indexOf(Constants.COOKIE_EMAIL_KEY_INDEX) + Constants.COOKIE_EMAIL_KEY_INDEX.length();
            int end = headers.indexOf(';', start);
            return headers.substring(start, end).replace("%40","@");
        } catch (Exception e) {
            throw new NullPointerException("Not support headers!!!");
        }

    }
}
