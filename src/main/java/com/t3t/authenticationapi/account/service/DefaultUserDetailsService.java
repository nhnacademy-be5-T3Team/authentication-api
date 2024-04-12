package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.auth.CustomUserDetails;
import com.t3t.authenticationapi.account.dto.UserEntity;
import com.t3t.authenticationapi.account.dto.UserEntityDto;
import com.t3t.authenticationapi.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntityDto userEntityDto = accountRepository.loadUserEntity(username);

        if(Objects.isNull(userEntityDto)){
            throw new UsernameNotFoundException("User Not Found");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userEntityDto.getUsername());
        userEntity.setUserId(userEntityDto.getUserId());
        userEntity.setPassword(userEntityDto.getPassword()); // 이게 맞는데 현재 회원에 password가 암호화 되어있지 않음
//        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntityDto.getPassword()));
        userEntity.setRole(userEntityDto.getRole());

        return new CustomUserDetails(userEntity);
    }
}
