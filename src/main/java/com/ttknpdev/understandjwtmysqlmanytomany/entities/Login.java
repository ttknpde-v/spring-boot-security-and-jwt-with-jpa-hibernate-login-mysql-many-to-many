package com.ttknpdev.understandjwtmysqlmanytomany.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

// for login then get token
@Data
@AllArgsConstructor
public class Login {
    private String nameOrEmail;
    private String password;
}
