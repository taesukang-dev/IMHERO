# IMHERO

## 🖥 개요

- 단시간 내 트래픽 처리를 고려한 예매 사이트 백엔드 API repository 입니다.

<br>

## 📍 프로젝트 중점사항

- 특정 시간 동안 트래픽이 급증하는 경우를 대비하여, 부하를 효과적으로 처리하기 위한 성능 향상에 초점을 맞추었습니다.
- 서비스 계층까지의 테스트 커버리지를 100% 달성하는 것을 목표로 하였습니다.
- 동시성 이슈를 해결하면서 단계적으로 성능도 향상시키는 방향을 고민하였습니다.

<br>

## 🛠 사용 기술

- Java, Spring Boot, JPA, MySQL, Groovy, Spock
- Naver Cloud Platform, Docker, Docker-Compose, Github Actions

<br>

## 🪜 시스템 아키텍쳐

<img width="1047" alt="v2" src="https://github.com/f-lab-edu/IMHERO/assets/69712211/af3db2e7-31d0-4144-b914-63d916dcfb10">

<br>

## 🔗 ERD

<img width="663" alt="erd" src="https://github.com/f-lab-edu/IMHERO/assets/69712211/4fa0e318-bb6e-4641-9fa7-e84332996a83">

<br>

## 💡 Issues

- 좌석 예매 시 동시성 문제 해결

  - [비관적 락 -> 낙관적 락 -> 분산 락을 활용한 동시성 문제 해결](https://liltdevs.tistory.com/198)

- Redis Session Clustering

  - [JWT 대신 Session 을 쓰는 이유](https://liltdevs.tistory.com/197)

- 검색 기능 개발

  - [MySQL full text search 활용한 검색 기능 개발](https://liltdevs.tistory.com/199)

- github actions, docker, docker-compose를 활용한 CI/CD 구성

  - [Jekins 대신 Github Actions 를 쓰게 된 이유](https://liltdevs.tistory.com/195)

  - [CI 구성 및 로컬 MySQL 연동](https://sundotcom.tistory.com/25)

  - [CD 구성](https://sundotcom.tistory.com/30)

- NCP를 활용한 클라우드 서버 구성

  - [application server 생성](https://sundotcom.tistory.com/28)

  - [cloud DB for MySQL 생성](https://sundotcom.tistory.com/29)

- APM(Pinpoint), JMeter를 활용한 시스템 모니터링 및 로드 테스트

  - [Pinpoint를 활용한 시스템 모니터링](https://sundotcom.tistory.com/31)

  - [JMeter, Pinpoint를 활용한 로드 테스트](https://sundotcom.tistory.com/32)

<br>
