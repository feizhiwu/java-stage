package com.stage.service;

import com.stage.mapper.UserMapper;
import com.stage.model.User;
import com.stage.plugin.Curl;
import com.stage.plugin.Function;
import com.stage.plugin.Panic;
import com.github.pagehelper.PageHelper;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final Function function;
    private final UserMapper userMapper;

    public UserService(Curl curl, Function function, UserMapper userMapper) {
        this.function = function;
        this.userMapper = userMapper;
    }

    /**
     * 添加用户
     * @param data
     * @return id
     */
    @SneakyThrows
    public int add(HashMap data) {
        if (null != userMapper.getOneByName((String) data.get("name"))) {
            throw new Panic(20003);
        }
        HashMap<String, Object> params = function.copyParams(new String[]{
                "name"
        }, data);
        params.put("password", function.encryptPass((String) data.get("password")));
        params.put("status", User.STATUS_OK);
        User user = new User();
        BeanUtils.populate(user, params);
        userMapper.add(user);
        return user.getId();
    }

    /**
     * 用户列表
     * @param data
     * @return list
     */
    public HashMap<String, Object> getList(HashMap data) {
        HashMap<String, Integer> ps = function.makePS(data);
        PageHelper.startPage(ps.get("page"), ps.get("size"));
        List<User> users = userMapper.getAll(data);
        return function.makeList(users);
    }

    /**
     * 用户详情
     * 传id时获取他人信息，不传时获取自己信息
     * @param data
     * @return info
     */
    @SneakyThrows
    public User getInfo(HashMap data) {
        int id;
        if (null != data.get("id")) {
            id = function.makeInt(data.get("id"));
        } else {
            id = function.makeInt(data.get("login_uid"));
        }
        User user = userMapper.getOne(id);
        if (null == user) {
            throw new Panic(20004);
        }
        user.setPassword(null);
        return user;
    }

    /**
     * 修改用户
     * @param data
     */
    @SneakyThrows
    public void update(HashMap data) {
        HashMap<String, Object> params = function.copyParams(new String[]{
                "id",
                "name",
        }, data);
        if (null != data.get("password")) {
            params.put("password", function.encryptPass((String) data.get("password")));
        }
        User user = userMapper.getOne(function.makeInt(data.get("id")));
        BeanUtils.populate(user, params);
        userMapper.update(user);
    }

    /**
     * 删除用户
     * @param data
     */
    @SneakyThrows
    public void delete(HashMap data) {
        User user = userMapper.getOne(function.makeInt(data.get("id")));
        System.out.println(user);
        user.setStatus(User.STATUS_FAIL);
        userMapper.update(user);
    }
}
