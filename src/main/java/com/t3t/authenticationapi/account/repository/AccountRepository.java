package com.t3t.authenticationapi.account.repository;

import com.t3t.authenticationapi.account.dto.UserEntityDto;
import com.t3t.authenticationapi.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("SELECT a.id as username, m.id as userId , b.password as password, m.role as role FROM Account a " +
            "INNER JOIN a.member m " +
            "INNER JOIN BookstoreAccount b ON a.id = b.id " +
            "WHERE a.id = :id AND a.deleted = 0")
    UserEntityDto loadUserEntity(String id);
}
