package bootwildfly.controller;

import bootwildfly.consts.Constants;
import bootwildfly.dao.HeaderDao;
import bootwildfly.dao.UserDao;
import bootwildfly.model.HeaderModel;
import bootwildfly.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author houguangqiang
 * @date 2018-09-10
 * @since 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private HeaderDao headerDao;

    @GetMapping("/add")
    public String add(Model model) {
        Iterable<UserModel> users = userDao.findAll();
        model.addAttribute("users", users);
        return "user/index";
    }


    @GetMapping("/index")
    public String index(Model model) {
        Iterable<UserModel> users = userDao.findAll();
        model.addAttribute("users", users);
        return "user/index";
    }


    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/add")
    public String add(@RequestParam String headers, Model model) {
        Long uid = parseUid(headers);
        String email = parseEmail(headers);
        UserModel user = new UserModel(uid, email, headers);
        userDao.save(user);
        List<HeaderModel> headerModels = parseHeaders(uid, headers);
        headerDao.save(headerModels);
        Iterable<UserModel> users = userDao.findAll();
        model.addAttribute("users", users);
        return "redirect:add";
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
