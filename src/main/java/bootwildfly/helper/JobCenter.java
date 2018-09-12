package bootwildfly.helper;

import bootwildfly.model.HeaderModel;
import bootwildfly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author houguangqiang
 * @date 2018-09-11
 * @since 1.0
 */
@Component
public class JobCenter {

    @Autowired
    private Executor executor;
    @Autowired
    private UserService userService;


    @Scheduled(cron = "0 0/20 * * * ?")
    public void gacheck() {
        Map<Long, List<HeaderModel>> allUserHeaders = userService.getAllUserHeaders();
        allUserHeaders.forEach((k, v) -> executor.execute(() -> {
            boolean checked = MiMiApi.gacheck(v);
            if (!checked) {
                userService.deleteByUid(k);
            }
        }));
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void checkin() {
        Map<Long, List<HeaderModel>> allUserHeaders = userService.getAllUserHeaders();
        allUserHeaders.forEach((k, v) -> executor.execute(() -> {
            String msg = MiMiApi.checkin(v);
            userService.updateMsgAndMsgTimeByUid(k, msg, System.currentTimeMillis());
        }));
    }
}
