package com.t3t.authenticationapi.account.entity;

import com.t3t.authenticationapi.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
/**
 * 회원의 로그인 아이디를 담당하는 엔티티
 * @author joohyun1996 (이주현)
 */
@Getter
@Entity@Table(name = "accounts")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public class Account {
    @Id
    @Column(name = "account_id")
    private String id;
    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;
    @Column(name = "deleted")
    private int deleted;

    public Account(String id, Member member){
        this.id = id;
        this.member = member;
    }
}
