package com.t3t.authenticationapi.account.repository;

import com.t3t.authenticationapi.account.entity.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthProviderRepository extends JpaRepository<OAuthProvider, Integer> {
}
