package com.t3t.authenticationapi.account.repository;

import com.t3t.authenticationapi.account.dto.UserEntityDto;
import com.t3t.authenticationapi.account.entity.BookstoreAccount;
import com.t3t.authenticationapi.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;

@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testLoadUserEntity() {
        Member member = Member.builder()
                .id(3l)
                .name("foo")
                .phone("010-1234-5678")
                .point(100)
                .role("USER")
                .email("2@Mail.com")
                .birthdate(LocalDate.now())
                .status("ACTIVE")
                .latestLogin(LocalDateTime.of(2024, 1, 1, 0, 0))
                .gradeId(1)
                .build();

        entityManager.persist(member);

        BookstoreAccount bookstoreAccount = BookstoreAccount.builder()
                .id("user")
                .password("password")
                .member(member)
                .build();

        entityManager.persist(bookstoreAccount);


        UserEntityDto entityDto = accountRepository.loadUserEntity(bookstoreAccount.getId());

        Assertions.assertThat(entityDto.getUserId()).isEqualTo("3");
        Assertions.assertThat(entityDto.getUsername()).isEqualTo("user");
        Assertions.assertThat(entityDto.getPassword()).isEqualTo("password");
        Assertions.assertThat(entityDto.getRole()).isEqualTo("USER");

    }
}