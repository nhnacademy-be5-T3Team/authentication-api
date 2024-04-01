package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.auth.CustomUserDetails;
import com.t3t.authenticationapi.account.dto.UserEntity;
import com.t3t.authenticationapi.account.dto.UserEntityDto;
import com.t3t.authenticationapi.account.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DefaultUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DefaultUserDetailsService(AccountRepository accountRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntityDto userEntityDto = accountRepository.loadUserEntity(username);

        if(Objects.isNull(userEntityDto)){
            throw new UsernameNotFoundException("username not found");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userEntityDto.getUsername());
        userEntity.setUserId(userEntityDto.getUserId());
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
