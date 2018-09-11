package bootwildfly.helper;

import bootwildfly.model.HeaderModel;
import bootwildfly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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


    @Scheduled(initialDelay = 10000, fixedRate = 1000*20)
    public void gacheck() {
        Iterable<HeaderModel> headers = userService.getAllHeaders();
        Map<Long, Map<String, String>> result = StreamSupport.stream(headers.spliterator(), false)
                .collect(Collectors.groupingBy(HeaderModel::getUid,
                        Collectors.toMap(HeaderModel::getKey, HeaderModel::getValue)));
        result.forEach((k, v) -> executor.execute(() -> {
            boolean checked = MiMiApi.gacheck(v);
            if (!checked) {
                userService.deleteByUid(k);
            }
        }));
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void checkin() {
        Iterable<HeaderModel> headers = userService.getAllHeaders();
        Map<Long, Map<String, String>> result = StreamSupport.stream(headers.spliterator(), false)
                .collect(Collectors.groupingBy(HeaderModel::getUid,
                        Collectors.toMap(HeaderModel::getKey, HeaderModel::getValue)));
        result.forEach((k, v) -> executor.execute(() -> {
            String msg = MiMiApi.checkin(v);
            userService.updateMsgAndMsgTimeByUid(k, msg, System.currentTimeMillis());
        }));
    }
}
