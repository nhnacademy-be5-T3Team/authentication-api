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
/**
 * Redis에 저장된 Refresh, BlackList 토큰의 CRUD를 담당하는 service 클래스
 * @author joohyun1996 (이주현)
 */
@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshRepository refreshRepository;
    private final BlackListRepository blackListRepository;
    /**
     * Refresh Token을 Redis에 저장한다.
     * @param refresh
     * @author joohyun1996 (이주현)
     */
    public void saveRefreshToken(Refresh refresh){
        if(refreshRepository.existsById(refresh.toString())){
            throw new TokenAlreadyExistsException("Token Already Exists");
        }
        refreshRepository.save(refresh);
    }
    /**
     * Logout 요청시 헤더에 있던 access token을 레디스에 저장한다
     * @param blackList
     * @author joohyun1996 (이주현)
     */
    public void saveBlackListToken(String blackList){
        if(blackListRepository.existsById(blackList)){
            throw new TokenAlreadyExistsException("Token Already Exists");
        }
        blackListRepository.save(BlackList.builder().blackList(blackList).build());
    }
    /**
     * refresh 토큰을 제거한다.
     * @param refresh
     */
    public void removeRefreshToken(String refresh){
        if(!refreshRepository.existsById(refresh)){
            throw new TokenNotExistsException("Token Not Exists");
        }
        Refresh newRefresh = refreshRepository.findById(refresh).get();
        refreshRepository.delete(newRefresh);
    }
    /**
     * Logout 요청시 access token과 UUID가 같은 refresh token을 찾아, 제거한다.
     * @param uuid
     * @author joohyun1996 (이주현)
     */
    public void removeRefreshTokenByUUID(String uuid){
        Optional<Refresh> optionalRefresh = refreshRepository.findByUuid(uuid);
        if(!optionalRefresh.isPresent()){
            throw new TokenNotExistsException("Token Not Exists");
        }
        refreshRepository.delete(optionalRefresh.get());
    }
    /**
     * Refresh Token이 redis에 있는지 확인하는 메소드
     * @param refresh
     * @return true/false
     * @author joohyun1996 (이주현)
     */
    public boolean refreshTokenExists(String refresh){
        return refreshRepository.existsById(refresh);
    }
    /**
     * Access token과 매핑되는 refresh 토큰을 찾아 반환한다
     * @param uuid
     * @return String refresh
     * @author joohyun1996 (이주현)
     */
    public String findRefreshByUUID(String uuid){
        if(refreshRepository.findByUuid(uuid).isEmpty()){
            throw new TokenNotExistsException("Expired");
        }
        return refreshRepository.findByUuid(uuid).get().getToken();
    }
    /**
     * access token과 일치하는 Refresh token이 있는지 찾는다
     * @param uuid
     * @return true/fales
     * @author joohyun1996 (이주현)
     */
    public Boolean refreshTokenExistsByUUID(String uuid){
        return refreshRepository.existsByUuid(uuid);
    }
}
