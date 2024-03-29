package com.t3t.authenticationapi.account.repository;

import com.t3t.authenticationapi.account.entity.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, String> {
}
