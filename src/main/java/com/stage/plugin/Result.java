package com.stage.plugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Result {
    Function function;
    private Integer status;
    private String msg;
    private Object body;

    @Autowired
    private void setFunction(Function function) {
        this.function = function;
    }

    public Result show() {
        return this.show(10000);
    }

    public Result show(Integer status) {
        this.status = status;
        this.msg = function.msg(this.status);
        this.body = null;
        return this;
    }

    public Result show(String msg) {
        Pattern pattern = Pattern.compile("[0-9]{5}");
        if (pattern.matcher(msg).matches()) {
            return this.show(Integer.parseInt(msg));
        } else {
            this.status = 0;
            this.msg = msg;
            this.body = null;
            return this;
        }
    }

    public Result show(Object data) {
        this.status = 10000;
        this.msg = function.msg(this.status);
        this.body = data;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Object getBody() {
        return body;
    }
}
