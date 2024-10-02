
# 🚀 레디스에서 읽기 및 MySQL에서 읽기의 성능 비교

### ☄️ 'vegeta' 설치
```
brew install vegeta
```

### ☄️ 도커로 Redis, MySQL 의 CPU, MEMORY 모니터링 하기
```
docker stats {CONTAINER_ID}
```

### ☄️ 테스트를 위한 txt 파일 작성
```
touch request1.txt
```

```
vi request1.txt
```

```
GET http://127.0.0.1:8080/users-db/1
GET http://127.0.0.1:8080/users-db/2
GET http://127.0.0.1:8080/users-db/3
:wq
```

### ☄️ vegeta 를 이용한 테스트를 위한 vegeta 설정
```
vegeta attack -timeout=30s -duration=15s -rate=5000/1s -targets=request1.txt -workers=100 | tee v_results.bin | vegeta report
```
![iScreen Shoter - 20241002133032690](https://github.com/user-attachments/assets/5a5e981d-c6c2-49af-9f04-2b375fc7468b)


### ✅ MySQL을 적용한 테스트
![iScreen Shoter - 20241002132954497](https://github.com/user-attachments/assets/4464bd2d-8dae-4ee8-bb99-c2896eed8a72)
![iScreen Shoter - 20241002133317420](https://github.com/user-attachments/assets/98812dc0-f237-41ec-849a-c5eabe346e6f)


### ✅ 레디스를 적용한 테스트
![iScreen Shoter - 20241002133011561](https://github.com/user-attachments/assets/94c7fda5-2602-403a-aaf0-02fb98605219)
![iScreen Shoter - 20241002133333294](https://github.com/user-attachments/assets/9b1bf90f-0bce-4c1d-b279-3cc5471e273c)



### 테스트 하드웨어 환경
```
CPU : Mac M1 PRO 8코어
RAM : 16GB
```
