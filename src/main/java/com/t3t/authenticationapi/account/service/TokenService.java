package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.entity.BlackList;
import com.t3t.authenticationapi.account.entity.Refresh;
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
            throw new RuntimeException("token Already Exists"); // 추후 규격에 맞춘 수정 필요
        }
        refreshRepository.save(refresh);
    }
    public void saveBlackListToken(String blackList){
        blackListRepository.save(BlackList.builder().blackList(blackList).build());
    }

    public void removeRefreshToken(String refresh){
        Refresh newRefresh = refreshRepository.findRefreshByRefresh(refresh);
        refreshRepository.delete(newRefresh);
    }

    public void removeRefreshTokenByUUID(String uuid){
        Optional<Refresh> optionalRefresh = refreshRepository.findByUuid(uuid);
        if(!optionalRefresh.isPresent()){
            throw new RuntimeException("token Already Exists"); // 추후 수정 필요
        }
        refreshRepository.delete(optionalRefresh.get());
    }

    public boolean refreshTokenExists(String refresh){
        return refreshRepository.existsById(refresh);
    }
}
