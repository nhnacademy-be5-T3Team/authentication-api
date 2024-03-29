package com.t3t.authenticationapi.member.entity;

import lombok.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @Column(name = "member_id")
    private Long id;
    @Column(name = "grade_id")
    private Integer gradeId;
    @Column(name = "member_name")
    private String name;
    @Column(name = "member_phone")
    private String phone;
    @Column(name = "member_email")
    private String email;
    @Column(name = "member_birthdate")
    private LocalDate birthdate;
    @Column(name = "member_latest_login")
    private LocalDate latestLogin;
    @Column(name = "member_point")
    private Integer point;
    @Column(name = "member_status")
    private String status;
    @Column(name = "member_role")
    private Integer role;
    @Builder
    public Member(Long id, Integer gradeId, String name, String phone, String email,
                  LocalDate birthdate, LocalDate latestLogin, Integer point,
                  String status, Integer role) {
        this.id = id;
        this.gradeId = gradeId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthdate = birthdate;
        this.latestLogin = latestLogin;
        this.point = point;
        this.status = status;
        this.role = role;
    }
}
