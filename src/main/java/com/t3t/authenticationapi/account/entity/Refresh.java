package com.t3t.authenticationapi.account.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * refresh 토큰을 redis에 저장하는 엔티티
 * @author joohyun1996 (이주현)
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "refresh", timeToLive = 1800)
public class Refresh {
    @Id
    private String token;
    @Indexed
    private String uuid;
}
