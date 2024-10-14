# Restock Notification System
재입고 알림 시스템

## ERD
![Restock Notification](https://github.com/user-attachments/assets/d0e44d6c-f99e-492d-9115-94db55ea0f83)


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
