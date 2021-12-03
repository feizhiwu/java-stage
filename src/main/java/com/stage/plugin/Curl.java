package com.stage.plugin;

import com.github.kevinsawicki.http.HttpRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Curl {
    private final Function function;

    public Curl(Function function) {
        this.function = function;
    }

    @SneakyThrows
    public String httpGet(String url, HashMap<String, String> data) {
        return HttpRequest.get(url + "?" + new Function().urlParams(data)).body();
    }

    @SneakyThrows
    public String httpGet(String url, HashMap<String, String> data, HashMap<String, String> header) {
        HttpRequest httpRequest = HttpRequest.get(url + "?" + new Function().urlParams(data));
        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpRequest.header(entry.getKey(), entry.getValue());
        }
        return httpRequest.body();
    }
}
