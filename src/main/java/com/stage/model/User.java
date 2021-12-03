package com.stage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    public static final int STATUS_OK = 1;
    public static final int STATUS_FAIL = 0;
    private int id;
    private String name;
    private String password;
    private int status;
}
