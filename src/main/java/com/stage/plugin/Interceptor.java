package com.stage.plugin;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;

@Component
public class Interceptor implements HandlerInterceptor {
    private final Result result;

    public Interceptor(Result result) {
        this.result = result;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
            return false;
        }
        if (!exclude(request, handler)) {
            String token = request.getHeader("token");
            if (null == token) {
                //此处直接免密访问
                return true;
                //TODO token校验
                //coding....
            }
            //TODO 塞入登陆用户id
            //request.setAttribute("login_uid", uid);
        }
        return true;
    }

    /**
     * 检查接口是否免密访问
     * @param request
     * @return bool
     */
    private Boolean exclude(HttpServletRequest request, Object handler) {
        String action = request.getHeader("action");
        if (handler.toString().contains("Login")) {
            return Arrays.asList(new String[]{"login"}).contains(action);
        }
        return false;
    }

    @SneakyThrows
    private void expire(HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.append(JSONObject.toJSON(result.show(80003)).toString());
        out.flush();
    }
}
