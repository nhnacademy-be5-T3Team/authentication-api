package com.t3t.authenticationapi.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @Column(name = "member_id")
    private Long id;
    @Column(name = "member_grade_id")
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
    private LocalDateTime latestLogin;
    @Column(name = "member_point")
    private Integer point;
    @Column(name = "member_status")
    private String status;
    @Column(name = "member_role")
    private String role;
    @Builder
    public Member(Long id, Integer gradeId, String name, String phone, String email,
                  LocalDate birthdate, LocalDateTime latestLogin, Integer point,
                  String status, String role) {
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

    /**
     * 마지막 로그인 시간을 갱신한다.
     * @author woody35545(구건모)
     */
    public void updateLastLogin() {
        this.latestLogin = LocalDateTime.now();
    }
}
