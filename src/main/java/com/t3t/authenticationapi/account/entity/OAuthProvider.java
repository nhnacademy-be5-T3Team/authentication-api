package com.t3t.authenticationapi.account.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity@Table(name = "oauth_providers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthProvider {
    @Id
    @Column(name = "oauth_provider_id")
    private Integer id;
    @Column(name = "oauth_provider_name")
    private String name;
    @Builder
    public OAuthProvider(Integer id, String name){
        this.id = id;
        this.name = name;
    }
}
