package com.t3t.authenticationapi.member.service;

import com.t3t.authenticationapi.member.exception.MemberNotFoundException;
import com.t3t.authenticationapi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 인증 성공시 로그인 시간을 업데이트한다.
     * @param memberId 로그인 시간을 업데이트 할 회원 식별자
     * @author woody35545(구건모)
     */
    public void updateMemberLoginAt(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException())
                .updateLastLogin();
    }
}
