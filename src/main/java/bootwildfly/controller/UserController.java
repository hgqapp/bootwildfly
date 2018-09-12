package bootwildfly.controller;

import bootwildfly.helper.MiMiApi;
import bootwildfly.model.HeaderModel;
import bootwildfly.model.UserModel;
import bootwildfly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author houguangqiang
 * @date 2018-09-10
 * @since 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private Executor executor;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        Iterable<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/index";
    }

    @RequestMapping(value = "/checkin", method = RequestMethod.GET)
    public String checkin(Model model) {
        Iterable<HeaderModel> headers = userService.getAllHeaders();
        Map<Long, Map<String, String>> result = StreamSupport.stream(headers.spliterator(), false)
                .collect(Collectors.groupingBy(HeaderModel::getUid,
                        Collectors.toMap(HeaderModel::getKey, HeaderModel::getValue)));
        result.forEach((k, v) -> executor.execute(() -> {
            String msg = MiMiApi.checkin(v);
            userService.updateMsgAndMsgTimeByUid(k, msg, System.currentTimeMillis());
        }));
        Iterable<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "redirect:index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@RequestParam String headers, Model model) {
        userService.addUser(headers);
        Iterable<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "redirect:index";
    }

}
