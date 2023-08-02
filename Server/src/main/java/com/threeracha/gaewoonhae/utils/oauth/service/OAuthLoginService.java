package com.threeracha.gaewoonhae.utils.oauth.service;

import com.threeracha.gaewoonhae.db.domain.Emoji;
import com.threeracha.gaewoonhae.db.repository.EmojiRepository;
import com.threeracha.gaewoonhae.exception.CustomException;
import com.threeracha.gaewoonhae.exception.CustomExceptionList;
import com.threeracha.gaewoonhae.utils.oauth.request.OAuthLoginParams;
import com.threeracha.gaewoonhae.utils.oauth.response.LoginResponse;
import com.threeracha.gaewoonhae.utils.oauth.response.OAuthInfoResponse;
import com.threeracha.gaewoonhae.db.domain.User;
import com.threeracha.gaewoonhae.db.repository.UserRepository;
import com.threeracha.gaewoonhae.utils.jwt.AuthTokens;
import com.threeracha.gaewoonhae.utils.jwt.AuthTokensGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final EmojiRepository emojiRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public LoginResponse login(OAuthLoginParams params) {
        // API 서버로부터 유저 정보를 받아옴
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        
        // 유저 정보를 바탕으로 신규 유저는 회원가입을, 기존 유저는 로그인을 실행
        User user = findOrCreateUser(oAuthInfoResponse);
        
        // 로그인 한 유저 ID를 바탕으로 accessToken과 refreshToken 생성
        AuthTokens token = authTokensGenerator.generate(user.getUserId());

        // user 테이블에 refreshToken 저장
        user.setRefreshToken(token.getRefreshToken());
        userRepository.flush();

        return new LoginResponse(token, user.getUserId());
    }

    public Long logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("유저를 찾을 수 없습니다."));

        user.setRefreshToken(null);
        userRepository.save(user);

        return user.getUserId();
    }

    private User findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByEmail(oAuthInfoResponse
                        .getEmail())
                        .orElseGet(() -> newUser(oAuthInfoResponse));
    }

    private User newUser(OAuthInfoResponse oAuthInfoResponse) {
        User user = User.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                .emoji(emojiRepository.findById(1L).orElseThrow(() -> new CustomException("이모지가 존재하지 않습니다.")))
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();

        return userRepository.save(user);
    }
}