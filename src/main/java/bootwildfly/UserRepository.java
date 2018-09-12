package bootwildfly;

import bootwildfly.model.HeaderModel;
import bootwildfly.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author houguangqiang
 * @date 2018-09-12
 * @since 1.0
 */
@Component
public class UserRepository {

    private Map<Long, UserModel> userCache = new LinkedHashMap<>();
    private Map<Long, List<HeaderModel>> userHeaderCache = new LinkedHashMap<>();

    public void updateMsgAndMsgTimeByUid(Long uid, String msg, Long msgTime) {
        userCache.computeIfPresent(uid, (k, v) -> {
            v.setMsg(msg);
            v.setMsgTime(msgTime);
            return v;
        });
    }

    public void delete(Long uid) {
        userCache.remove(uid);
        userHeaderCache.remove(uid);
    }

    public Iterable<UserModel> getAllUsers() {
        return userCache.values();
    }

    public Map<Long, List<HeaderModel>> getAllUserHeaders() {
        return userHeaderCache;
    }

    public void saveUser(UserModel user, List<HeaderModel> headerModels) {
        userCache.put(user.getUid(), user);
        userHeaderCache.put(user.getUid(), headerModels);
    }
}
