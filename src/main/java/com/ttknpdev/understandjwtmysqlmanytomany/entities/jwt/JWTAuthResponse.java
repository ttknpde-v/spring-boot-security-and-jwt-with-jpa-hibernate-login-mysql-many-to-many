package com.ttknpdev.understandjwtmysqlmanytomany.entities.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JWTAuthResponse {
    private String token;
    private String type = "Bearer";
}
