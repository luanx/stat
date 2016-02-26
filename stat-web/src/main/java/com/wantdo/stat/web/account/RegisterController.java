package com.wantdo.stat.web.account;

import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.service.account.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * 用户注册的Controller
 *
 * @Date : 2015-8-18
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String registerForm(){
        return "account/register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String register(@Valid User user){
        userService.saveUser(user);
        return "redirect:/login";
    }

    /**
     * Ajax请求校验loginName是否唯一。
     */
    @RequestMapping(value = "checkLoginName")
    @ResponseBody
    public String checkLoginName(@RequestParam("loginName") String loginName) {
        if (userService.findByLoginName(loginName) == null) {
            return "true";
        } else {
            return "false";
        }
    }

}
