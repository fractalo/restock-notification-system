# Restock Notification System
재입고 알림 시스템

## ERD
![Restock Notification](https://github.com/user-attachments/assets/d0e44d6c-f99e-492d-9115-94db55ea0f83)


## 비즈니스 요구 사항

- 재입고 알림을 전송하기 전, 상품의 재입고 회차를 1 증가 시킨다.
   - ProductService의 재고 수정 함수에서, 재고가 0보다 큰 상황에서 0으로 될 떄, 그 반대로 될때 이벤트를 발생시킨다.
- 상품이 재입고 되었을 때, 재입고 알림을 설정한 유저들에게 알림 메시지를 전달해야 한다.
   - product_user_notification에서 상품ID와 알림설정 ID에 대해 복합 인덱스를 설정하고, 일정 크기(50개 정도) 만큼 커서 페이지네이션을 통해 가져와서 알림을 발송하고 히스토리를 기록하고 마지막 발송 알림 설정ID를 갱신한다.
- 재입고 알림은 재입고 알림을 설정한 유저 순서대로 메시지를 전송한다.
   - 알림설정ID를 auto incremental id로  설정하고 그것을 기준으로 정렬해서 페이지네이션 한다.
   - 알림을 설정할때는 product_user_notification에 row를 추가하고, 해제할떄는 row를 제거한다.
- 회차별 재입고 알림을 받은 유저 목록을 저장해야 한다.
   - product_user_notification_history에 상품별 회차별 알림 발송 목록을 저장한다.
- 재입고 알림을 보내던 중 재고가 모두 없어진다면 알림 보내는 것을 중단합니다.
   - 상품 재고는 1차적으로 메모리에서 관리하며, 주기적으로 db에 동기화시키는 방식을 사용한다.
- 재입고 알림 전송의 상태를 DB 에 저장해야 한다. IN_PROGRESS (발송 중), CANCELED_BY_SOLD_OUT (품절에 의한 발송 중단), CANCELED_BY_ERROR (예외에 의한 발송 중단), COMPLETED (완료)
   - product_notification_history 테이블을 각 상품별 재입고 회차에 대한 알림 작업 상태 저장용으로 사용한다.

## 기술적 요구 사항

- 알림 메시지는 1초에 최대 500개의 요청을 보낼 수 있다.
   - 재입고 알림 발송에 RateLimiter를 적용한다.
- Mysql 조회 시, 인덱스를 잘 탈 수 있게 설계해야 합니다
- (Optional) 예외에 의해 알림 메시지 발송이 실패한 경우, manual 하게 상품 재입고 알림 메시지를 다시 보내는 API를 호출한다면 마지막으로 전송 성공한 이후 유저부터 다시 알림 메시지를 보낼 수 있어야 한다.
   - 알림 발송 상태가 CANCELED_BY_ERROR인 경우, 마지막 발송 알림 설정 ID부터 다시 알림 메시지를 보낸다.


## 동시성 문제

1. 해당 상품에 대해 재입고 알림을 발송하는 중에 누군가 알림을 등록한다면?
   - 알림 설정 ID가 incremental 하고, 상품별로 알림 설정 ID에 대해 발송 대상 유저들을 오름차순으로 정렬하기 떄문에, 문제 없다.  즉, 알림을 등록한 순서대로 알림이 발송된다.


## 빌드 및 실행

### Docker 이미지 빌드
1. 환경 변수 설정
```
IMAGE_NAME = (빌드할 Docker 이미지 이름)
```
2. Gradle `jibDockerBuild` 실행

### Docker Compose 실행
1. 환경 변수 파일 생성
   1. `.env.db`
    ```
    MYSQL_ROOT_PASSWORD = (MySQL 루트 비밀번호)
    MYSQL_DATABASE = (생성할 데이터베이스 이름)
    ```
   2. `.env.backend`
    ```
    DB_URL = jdbc:mysql://restock-notification-system-db:3306/{데이터베이스 이름}
    DB_USERNAME = root
    DB_PASSWORD = (MySQL 루트 비밀번호)
    ```
2. 실행
```
docker compose up -d
```
