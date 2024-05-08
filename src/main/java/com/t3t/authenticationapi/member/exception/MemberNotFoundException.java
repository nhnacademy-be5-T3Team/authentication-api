package com.t3t.authenticationapi.member.exception;

/**
 * 존재하지 않는 회원에 대한 조회를 시도할 때 발생하는 예외
 *
 * @author woody35545
 */
public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("존재하지 않는 회원 입니다.");
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
