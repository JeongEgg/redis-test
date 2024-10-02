
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

