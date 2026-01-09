# 저기어때
선착순 특가, 날짜별 재고 관리, 이벤트 쿠폰 발급을 중심으로 한 숙박 상품 판매 플랫폼
## 프로젝트 소개
숙박 도메인에서 자주 발생하는 선착순 예약, 날짜 기반 재고 관리, 이벤트 쿠폰 발급과 같은 문제를 서버 단에서 안정적으로 해결하려는 것을 목표로 한 숙박 상품 판매 플랫폼입니다.

단순 CRUD 구현을 넘어 동시성 제어, 성능 최적화, 실서비스 관점의 설계 경험에 집중한 프로젝트 입니다.
## 프로젝트 목적
### 1. 숙박 도메인 문제 해결
- 선착순 숙소 예약 처리
- 날짜 기반 객실 재고 관리
- 이벤트 쿠폰 발급 및 사용 관리
### 2. 백엔드 실전 역량 강화
- 동시성 제어(락, 트랜잭션)
- 쿼리 최적화 및 성능 개선
- 성능 측정 및 비교 테스트
- CI/CD 기반 배포 경험

## 핵심 기능
### 숙소 예약
- 선착순 초특가 숙소 예약
- 날짜별 객실 재고 관리
- 동시 요청 환경에서도 초과 예약 방지
### 이벤트 쿠폰
- 선착순 이벤트 할인 쿠폰 발급
- 1인 1회 발급 제한
- 수량 기반 쿠폰 소진 관리
### 숙소 검색
- 조건 기반 숙소 검색
- 검색 성능 최적화
## 기술 스택
Backend
- Java
- Spring Boot
- Spring Security
- JWT
- OAuth2
- QueryDSL
- Spring WebFlux
  
Database
- MySQL
- Redis 
- Elasticsearch

Infrastructure / DevOps
- AWS EC2
- AWS ALB (Application Load Balancer)
- Docker
- Docker Hub
- GitHub Actions

External API
- Kakao OAuth2 API
- Toss Payments API

Collaboration & Tools
- GitHub
- GitHub Actions
- Jira
- Slack
- Notion
- Figma
- IntelliJ IDEA

Modeling & Design
- DrawSQ
## 시스템 아키텍쳐
<img width="1881" height="1041" alt="저기어때_시스템_아키텍처 drawio (1)" src="https://github.com/user-attachments/assets/b70d2f0e-4b4a-46a7-8008-53967b7b38d2" />

## ERD
<img width="701" height="669" alt="스크린샷 2026-01-08 오후 9 50 22" src="https://github.com/user-attachments/assets/8021141e-96ff-4e5a-a03e-366c11ec3166" />

## API 명세
[API 명세](https://www.notion.so/teamsparta/API-2da2dc3ef5148045aae8ef24815f048e?source=copy_link)
