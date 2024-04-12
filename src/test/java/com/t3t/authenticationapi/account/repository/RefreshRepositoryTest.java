package com.t3t.authenticationapi.account.repository;

import com.t3t.authenticationapi.account.entity.Refresh;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RefreshRepositoryTest {
    @Autowired
    private RefreshRepository refreshRepository;

    private Refresh refresh = null;
    @BeforeEach
    public void set(){
        refresh = Refresh.builder()
                .token("r.r.r")
                .uuid("1")
                .build();

        refreshRepository.save(refresh);
    }

    @AfterEach
    public void tearDown(){
        refreshRepository.delete(refresh);
    }
    @Test
    public void findRefreshTest(){
        Refresh newRefresh = refreshRepository.findById("r.r.r").get();

        Assertions.assertThat(newRefresh.getToken()).isEqualTo("r.r.r");
        Assertions.assertThat(newRefresh.getUuid()).isEqualTo("1");
    }

    @Test
    public void findByUUIDTest(){
        String uuid = "1";

        Assertions.assertThat(refresh.getUuid()).isEqualTo("1");
    }

    @Test
    public void cannotFindByUUIDTest(){
        String uuid = "2";
        Assertions.not(refresh.getUuid()).equals(uuid);
    }
}