package com.t3t.authenticationapi.account.repository;

import com.t3t.authenticationapi.account.entity.Access;
import org.springframework.data.repository.CrudRepository;

public interface AccessRepository extends CrudRepository<Access, String> {
    Access findAccessByAccess(String access);
}
