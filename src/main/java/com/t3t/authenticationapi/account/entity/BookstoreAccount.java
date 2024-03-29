package com.t3t.authenticationapi.account.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

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
