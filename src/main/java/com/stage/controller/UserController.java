package com.stage.controller;

import com.stage.plugin.Panic;
import com.stage.plugin.Result;
import com.stage.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/v1/user")
    public Result run(HttpServletRequest request, @RequestBody(required = false) HashMap body) {
        data = function.makeData(request, body);
        String action = request.getHeader("action");
        switch (request.getMethod()) {
            case "GET":
                if (function.checkAction(action, "list")) {
                    return list();
                } else if (function.checkAction(action, "info")) {
                    return info();
                }
                break;
            case "POST":
                if (function.checkAction(action, "add")) {
                    return add();
                }
                break;
            case "PUT":
                if (function.checkAction(action, "update")) {
                    return update();
                }
                break;
            case "DELETE":
                if (function.checkAction(action, "delete")) {
                    return delete();
                }
                break;
        }
        return result.show(function.badRouter());
    }

    private Result list() {
        try {
            function.validate(new HashMap<Integer, String>() {{
                put(80007, "page");
            }}, data);
            return result.show(userService.getList(data));
        } catch (Exception e) {
            return result.show(e.getMessage());
        }
    }

    private Result info() {
        try {
            return result.show(userService.getInfo(data));
        } catch (Exception e) {
            return result.show(e.getMessage());
        }
    }

    private Result add() {
        try {
            function.validate(new HashMap<Integer, String>() {{
                put(20001, "name");
                put(20002, "password");
            }}, data);
            return result.show(new HashMap<String, Integer>() {{
                put("id", userService.add(data));
            }});
        } catch (Panic e) {
            return result.show(e.getMessage());
        }
    }

    private Result update() {
        try {
            function.hasKey(data);
            userService.update(data);
            return result.show();
        } catch (Panic e) {
            return result.show(e.getMessage());
        }
    }

    private Result delete() {
        try {
            function.hasKey(data);
            userService.delete(data);
            return result.show();
        } catch (Panic e) {
            return result.show(e.getMessage());
        }
    }
}
