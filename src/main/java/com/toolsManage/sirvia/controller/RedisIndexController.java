package com.toolsManage.sirvia.controller;

import com.toolsManage.sirvia.service.RedisIndexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/admin/redis")
public class RedisIndexController {
    public static Jedis jedis;

    @Resource(name = "redisIndexServiceImpl")
    private RedisIndexService redisIndexService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String userName, @RequestParam("database_number") Integer databaseNumber,
                        HttpSession session, Model model) {
        if (databaseNumber < 0 || databaseNumber > 15) {
            model.addAttribute("error_msg", "数据库号码异常");
            return "login";
        }
        // 没有填写用户名就使用默认的
        userName = userName.equals("") ? "游客" : userName;
        session.setAttribute("userName", userName);
        session.setAttribute("databaseNumber", databaseNumber);
        // 选择jedis数据库号
        jedis.select(databaseNumber);
        return "redirect:/admin/redis/index";
    }

    // 退出登录
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 移除session中的值
        session.setAttribute("userName", null);
        session.setAttribute("databaseNumber", null);
        return "redirect:/admin/redis/login";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/view_data")
    public String viewData(Model model) {
        // 获取所有的key
        Set<String> redisKeys = redisIndexService.getRedisKeys();
        model.addAttribute("keys", redisKeys);
        return "view_data";
    }

    @GetMapping("/opt_data")
    public String optData() {
        return "opt_data";
    }

    @ResponseBody
    @PostMapping("/get_data")
    public Map<String, Object> getData(String key) {
        Map<String, Object> res = new LinkedHashMap<>();
        String data = redisIndexService.getData(key);
        res.put("flag", true);
        res.put("content", data);
        return res;
    }

    @Resource(name = "jedis")
    public void setJedis(Jedis jedis) {
        RedisIndexController.jedis = jedis;
    }
}
