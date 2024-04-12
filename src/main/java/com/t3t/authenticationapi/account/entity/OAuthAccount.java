package com.t3t.authenticationapi.account.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Entity@Table(name = "oauth_accounts")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthAccount extends Account{
    @ManyToOne
    @JoinColumn(name = "oauth_provider_id")
    private OAuthProvider oAuthProvider;

    public OAuthAccount(OAuthProvider oAuthProvider){
        this.oAuthProvider = oAuthProvider;
    }
}
