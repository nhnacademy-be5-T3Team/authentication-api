package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.entity.BlackList;
import com.t3t.authenticationapi.account.entity.Refresh;
import com.t3t.authenticationapi.account.exception.TokenAlreadyExistsException;
import com.t3t.authenticationapi.account.exception.TokenNotExistsException;
import com.t3t.authenticationapi.account.repository.BlackListRepository;
import com.t3t.authenticationapi.account.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshRepository refreshRepository;
    private final BlackListRepository blackListRepository;

    public void saveRefreshToken(Refresh refresh){
        if(refreshRepository.existsById(refresh.toString())){
            throw new TokenAlreadyExistsException("Token Already Exists");
        }
        refreshRepository.save(refresh);
    }
    public void saveBlackListToken(String blackList){
        if(blackListRepository.existsById(blackList)){
            throw new TokenAlreadyExistsException("Token Already Exists");
        }
        blackListRepository.save(BlackList.builder().blackList(blackList).build());
    }

    public void removeRefreshToken(String refresh){
        if(!refreshRepository.existsById(refresh)){
            throw new TokenNotExistsException("Token Not Exists");
        }
        Refresh newRefresh = refreshRepository.findById(refresh).get();
        refreshRepository.delete(newRefresh);
    }

    public void removeRefreshTokenByUUID(String uuid){
        Optional<Refresh> optionalRefresh = refreshRepository.findByUuid(uuid);
        if(!optionalRefresh.isPresent()){
            throw new TokenNotExistsException("Token Not Exists");
        }
        refreshRepository.delete(optionalRefresh.get());
    }

    public boolean refreshTokenExists(String refresh){
        return refreshRepository.existsById(refresh);
    }
}
