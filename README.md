# stock-concurrency

## 개요
재고 처리 시 동시성 문제를 해결하는 미니 프로젝트

## 문제 인식
팀 프로젝트 진행 도중 경매 시스템, 인기글의 경우 많은 조회수 update 등 동시성 문제가 발생한다는 부분을 알게되었다. 이러한 문제를 어떻게 개선하고 해결해가는지 알아보고자 했다.

## 목표
- 비즈니스 로직 중 Race Condition이 발생하는 상황에서 해결 방법을 찾아보고 구현한다.
- 여러 방법을 시도해보고 어떤 트레이드 오프가 있는지 확인한다.
- mySQL의 Lock과 Redis를 사용한 분산 Lock을 공부한다.


