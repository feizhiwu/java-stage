package com.stage.plugin;

import com.github.pagehelper.Page;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class Function {
    public void validate(Map<Integer, String> val, HashMap data) throws Panic {
        for (Integer key : val.keySet()) {
            if (null == data.get(val.get(key))) {
                throw new Panic(key);
            }
        }
    }

    //组装参数
    public HashMap makeData(HttpServletRequest request, HashMap body) {
        if (null == body) {
            body = new HashMap();
        }
        if (null != request.getAttribute("login_uid")) {
            body.put("login_uid", request.getAttribute("login_uid"));
        }
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String paramName = enums.nextElement();
            String paramValue = request.getParameter(paramName);
            //形成键值对应的map
            body.put(paramName, paramValue);
        }
        return body;
    }

    //获取提醒信息
    public String msg(Integer status) {
        HashMap msg = (HashMap) loadYaml("/message.yaml").get("msg");
        return (String) msg.get(status);
    }

    //获取配置信息
    public String config(String key) {
        String value = null;
        key = Context.active() + "." + key;
        String[] arr = key.split("[.]");
        Map map = (Map) loadYaml("/config.yaml").get(arr[0]);
        for (int i = 1; i < arr.length; i++) {
            Object object = map.get(arr[i]);
            if (arr.length - 1 == i) {
                value = String.valueOf(object);
            } else {
                map = (Map) object;
            }
        }
        return value;
    }

    //加载yaml文件
    @SneakyThrows
    private Map loadYaml(String name) {
        InputStream resource = Function.class.getResourceAsStream(name);
        Yaml yaml = new Yaml();
        assert resource != null;
        return yaml.load(new BufferedReader(new InputStreamReader(resource)));
    }

    //密码加密
    public String encryptPass(String pass) {
        String salt = config("salt");
        return encrypt(pass + salt);
    }

    //字符串加密，默认MD5
    public String encrypt(String value) {
        return DigestUtils.md5DigestAsHex(value.getBytes());
    }

    //不存在路由
    public Integer badRouter() {
        return 80006;
    }

    public Boolean checkAction(String action, String value) {
        if (null == action) {
            return false;
        } else {
            return action.equals(value);
        }
    }

    public int makeInt(Object value) {
        return Integer.parseInt(value.toString());
    }

    public void hasKey(HashMap data) throws Panic {
        if (null == data.get("id")) {
            throw new Panic(80001);
        }
    }

    //返回数据列表
    public HashMap<String, Object> makeList(Object data) {
        Page page = (Page) data;
        HashMap<String, Number> pages = new HashMap<>();
        pages.put("count", page.getTotal());
        pages.put("page", page.getPageNum());
        pages.put("limit", page.getPageSize());
        HashMap<String, Object> list = new HashMap<>();
        list.put("list", data);
        list.put("pages", pages);
        return list;
    }

    //创建返回page&size
    public HashMap<String, Integer> makePS(HashMap data) {
        HashMap<String, Integer> ps = new HashMap<>();
        if (null == data.get("page")) {
            ps.put("page", 1);
        } else {
            ps.put("page", this.makeInt(data.get("page")));
        }
        if (null == data.get("size")) {
            ps.put("size", 50);
        } else {
            ps.put("size", this.makeInt(data.get("size")));
        }
        return ps;
    }

    //创建token
    public String makeToken() {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            token.append(encrypt(UUID.randomUUID().toString() + new Date().getTime()));
        }
        return token.toString();
    }

    //拷贝赋值
    public HashMap<String, Object> copyParams(String[] val, Map data) {
        HashMap<String, Object> params = new HashMap<>();
        for (String s : val) {
            String regex = ":";
            if (s.contains(regex)) {
                String[] arr = s.split(regex);
                if (null != data.get(arr[1])) {
                    params.put(arr[0], data.get(arr[1]));
                }
            } else {
                if (null != data.get(s)) {
                    params.put(s, data.get(s));
                }
            }
        }
        return params;
    }

    //参数ascii码排序
    public String formatParams(HashMap<String, String> data) {
        SortedMap<String, String> map = new TreeMap(data);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            stringBuilder.append(k).append("=").append(v).append("&");
        }
        if (stringBuilder.length() > 0) {
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }
        return null;
    }

    //map转url参数
    @SneakyThrows
    public String urlParams(HashMap<String, String> data) {
        if (null != data) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                String k = entry.getKey();
                String v = URLEncoder.encode(entry.getValue(), "utf-8");
                stringBuilder.append(k).append("=").append(v).append("&");
            }
            if (stringBuilder.length() > 0) {
                return stringBuilder.substring(0, stringBuilder.length() - 1);
            }
        }
        return null;
    }

    //返回格式化日期
    public String date(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    //返回格式化日期
    public String date(String pattern, long time) {
        return new SimpleDateFormat(pattern).format(new Date(time * 1000));
    }
}
