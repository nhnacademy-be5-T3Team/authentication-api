package com.t3t.authenticationapi.account.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "refresh", timeToLive = 604800)
public class Refresh {
    @Id
    @Indexed
    private String refresh;
    @Indexed
    private String userId;
    private String category;
    private String role;
}
