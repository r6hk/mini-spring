package dev.rennen.webmvc.test.controller;

import dev.rennen.beans.factory.annotation.Autowired;
import dev.rennen.webmvc.test.dto.User;
import dev.rennen.webmvc.test.service.UserService;
import dev.rennen.webmvc.util.annotation.RequestMapping;
import dev.rennen.webmvc.web.annotations.ResponseBody;
import dev.rennen.webmvc.web.view.ModelAndView;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author rennen.dev
 * @since 2025/1/5 16:03
 */
@Slf4j
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @RequestMapping("/test1")
    public String test1() {
        return "hello world from test1!";
    }

    @RequestMapping("/test2")
    @ResponseBody
    public String test2() {
        return "hello world from test2!";
    }

    @RequestMapping("/test3")
    public ModelAndView doTest3(User user) {
        log.debug("user: {}", user);
        return new ModelAndView("test","msg", user.getName());
    }

    @RequestMapping("/test4")
    public String doTest4(User user) {
        return "error";
    }

    @RequestMapping("/test5")
    @ResponseBody
    public User doTest5(User user) {
        user.setName(user.getName() + "---");
        user.setBirthday(new Date());
        return user;
    }
}