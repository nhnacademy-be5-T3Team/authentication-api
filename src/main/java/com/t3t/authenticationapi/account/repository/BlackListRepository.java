package com.t3t.authenticationapi.account.repository;

import com.t3t.authenticationapi.account.entity.BlackList;
import org.springframework.data.repository.CrudRepository;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
}
