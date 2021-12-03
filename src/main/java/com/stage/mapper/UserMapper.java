package com.stage.mapper;

import com.stage.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<User> getAll(HashMap data);

    User getOne(int id);

    int add(User user);

    int update(User user);

    User getOneByName(String name);
}
