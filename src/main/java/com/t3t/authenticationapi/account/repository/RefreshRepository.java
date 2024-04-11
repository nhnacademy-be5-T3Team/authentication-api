package com.t3t.authenticationapi.account.repository;

import com.t3t.authenticationapi.account.entity.Refresh;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshRepository extends CrudRepository<Refresh, String> {
    Optional<Refresh> findByUuid(String uuid);

    boolean existsByUuid(String uuid);
}
