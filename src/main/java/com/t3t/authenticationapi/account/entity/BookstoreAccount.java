package com.t3t.authenticationapi.account.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
/**
 * 회원의 password를 저장하는 엔티티
 * @author joohyun1996 (이주현)
 */
@Getter
@Entity@Table(name = "bookstore_accounts")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookstoreAccount extends Account{
    @Column(name = "account_password")
    private String password;

    public BookstoreAccount(String password) {
        this.password = password;
    }
}
