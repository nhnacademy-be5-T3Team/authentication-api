package com.t3t.authenticationapi.account.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "access", timeToLive = 600)
public class Access {
    @Id
    @Indexed
    private String access;
    private String userId;
    @Indexed
    private String refresh;
    private String category;
    private String role;
}
