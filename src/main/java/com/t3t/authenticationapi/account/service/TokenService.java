package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.entity.Access;
import com.t3t.authenticationapi.account.entity.BlackList;
import com.t3t.authenticationapi.account.entity.Refresh;
import com.t3t.authenticationapi.account.repository.AccessRepository;
import com.t3t.authenticationapi.account.repository.BlackListRepository;
import com.t3t.authenticationapi.account.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final AccessRepository accessRepository;
    private final RefreshRepository refreshRepository;
    private final BlackListRepository blackListRepository;

    public Access saveAccessToken(Access access){
        return accessRepository.save(access);
    }
    public Refresh saveRefreshToken(Refresh refresh){
        return refreshRepository.save(refresh);
    }
    public void saveBlackListToken(BlackList blackList){
        blackListRepository.save(blackList);
    }

    public void removeAccessToken(String access){
        Access newAccess = accessRepository.findAccessByAccess(access);
        accessRepository.delete(newAccess);
    }

    public void removeRefreshToken(String refresh){
        Refresh newRefresh = refreshRepository.findRefreshByRefresh(refresh);
        refreshRepository.delete(newRefresh);
    }

    public String findRefreshByAccessToken(String access){
        Access newAccess = accessRepository.findAccessByAccess(access);
        if(Objects.isNull(newAccess)){
            throw new NullPointerException();
        }
        return newAccess.getRefresh();
    }

    public String findRefreshByBlackListToken(String access){
        BlackList blackList = blackListRepository.findBlackListByAccess(access);
        return blackList.getRefresh();
    }

    public boolean accessTokenExists(String access){
        return accessRepository.existsById(access);
    }

    public boolean refreshTokenExists(String refresh){
        return refreshRepository.existsById(refresh);
    }

    public boolean blackListTokenExists(String access){
        return blackListRepository.existsById(access);
    }
}
