## 온라인 쇼핑몰 플랫폼 회원/인증 서비스

### 0. 소개
* 온라인 쇼핑몰 플랫폼 내 회원정보 관리 및 인증을 담당하는 서비스
* Access Token 과 Refresh Token 을 활용한 JWT 토큰 방식으로 인증 수행

### 1. 개발 환경
* Java 17
* SpringBoot 3.1
* Spring Data JPA / QueryDSL
* JWT
* H2
* JUnit 5

### 2. 주요 구현 기능
* 회원가입 시, 이메일 발송을 통한 본인인증 절차
  * 이메일 발송은 별도의 Mail Worker 프로젝트에서 처리
  * 인증번호 생성 후, kafka 를 통해 이메일 발송을 비동기 처리
* 로그인 시, JWT 방식을 통한 인증방식 처리
  * 최초 로그인 시, Access Token 발급 및 Refresh Token 저장
  * Access Token 만료 시, Refresh Token 을 조회하여 새 Access Token 재발급
  * Refresh Token 만료 시, 로그아웃 처리