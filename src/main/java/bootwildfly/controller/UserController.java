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

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

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
        Map<Long, List<HeaderModel>> allUserHeaders = userService.getAllUserHeaders();
        allUserHeaders.forEach((k, v) -> executor.execute(() -> {
            String msg = MiMiApi.checkin(v);
            userService.updateMsgAndMsgTimeByUid(k, msg, System.currentTimeMillis());
        }));
        Iterable<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "redirect:index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@RequestParam String headers, Model model) {
        userService.saveUser(headers);
        Iterable<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "redirect:index";
    }

}
