package com.t3t.authenticationapi.account.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
/**
 * logout 된 access token을 redis에 저장하는 엔티티
 * @author joohyun1996 (이주현)
 */
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "BlackList")
public class BlackList {
    @Id
    private String blackList;
}
