package com.stage.controller;

import com.stage.plugin.Function;
import com.stage.plugin.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;


public class BaseController {
    protected HashMap data;
    Function function;
    Result result;

    @Autowired
    private void setFunction(Function function) {
        this.function = function;
    }

    @Autowired
    private void setResult(Result result) {
        this.result = result;
    }
}
