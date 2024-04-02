package com.t3t.authenticationapi.account.repository;

import com.t3t.authenticationapi.account.entity.Refresh;
import org.springframework.data.repository.CrudRepository;

public interface RefreshRepository extends CrudRepository<Refresh, String> {
    Refresh findRefreshByRefresh(String refresh);
}
