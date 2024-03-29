package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.auth.CustomUserDetails;
import com.t3t.authenticationapi.account.dto.UserEntity;
import com.t3t.authenticationapi.account.dto.UserEntityDto;
import com.t3t.authenticationapi.account.entity.Account;
import com.t3t.authenticationapi.account.entity.BookstoreAccount;
import com.t3t.authenticationapi.account.repository.AccountRepository;
import com.t3t.authenticationapi.account.repository.BookStoreAccountRepository;
import com.t3t.authenticationapi.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DefaultUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final BookStoreAccountRepository bookStoreAccountRepository;
    private final MemberRepository memberRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public DefaultUserDetailsService(AccountRepository accountRepository, BookStoreAccountRepository bookStoreAccountRepository, MemberRepository memberRepository) {
        this.accountRepository = accountRepository;
        this.bookStoreAccountRepository = bookStoreAccountRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntityDto userEntityDto = accountRepository.loadUserEntity(username);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userEntityDto.getUsername());
//        userEntity.setPassword(userEntityDto.getPassword());
//        bCryptPasswordEncoder.encode(password)
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntityDto.getPassword()));
        userEntity.setRole(userEntityDto.getRole());


        if(Objects.nonNull(userEntity)){
            return new CustomUserDetails(userEntity);
        }

        return null;
    }
}
