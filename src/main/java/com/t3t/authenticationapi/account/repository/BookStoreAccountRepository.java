package com.t3t.authenticationapi.account.repository;

import com.t3t.authenticationapi.account.entity.BookstoreAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStoreAccountRepository extends JpaRepository<BookstoreAccount, String> {
}
