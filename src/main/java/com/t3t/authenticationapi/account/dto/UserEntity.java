package com.t3t.authenticationapi.account.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserEntity {
    private String username;
    private String userId;
    private String password;
    private String role;
}
