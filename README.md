# 프로젝트 계획 설정 및 회고

## TO-DO

### 개발

- [x] Global Exception 설정
- [x] user 도메인 설계 및 생성, 조회, 수정 API 구현
- [x] URL 관련 필터 구현
- [x] 로깅 AOP 구현
- [x] 로또 번호 발급 API 구현
- [x] 로또 당첨자 관련 Batch 구현

### TEST

- [ ] exception 관련 통합 테스트 작성
- [ ] user 생성, 조회, 수정 API 관련 통합 테스트 작성
- [ ] URL 필터 관련 통합 테스트 작
- [ ] 로또 번호 발급, Batch 관련 통합 테스트 작성

## 라이브러리 추가

- runtimeOnly 'com.h2database:h2' : H2 데이터베이스를 사용하기 위해 추가
- implementation 'org.springframework.boot:spring-boot-starter-data-jpa' : Spring Data JPA 라이브러리 추가
- implementation 'org.springframework.boot:spring-boot-starter-validation' : 요청 시 유효성 검증을 하기 위해 추가
- implementation 'org.springframework.boot:spring-boot-starter-batch' : Spring Batch 라이브러리 추가

## 프로젝트 설명

### Global 예외 처리

- @Valid로 요청 데이터를 검증하여 MethodArgumentNotValidException 예외가 발생하면 HTTP 400 상태와 이유를 응답하도록 설정했습니다.
- 정의되지 않은 API 요청이 들어오면 발생하는 NoHandlerFoundException 예외를 핸들링해 HTTP 404 상태와 이유를 응답하도록 설정했습니다.

### User

- Spring Data Jpa를 사용해서 생성, 조회, 수정 기능을 구현했습니다.
- Repository, Service, Controller 계층으로 구성해서 API를 구현했습니다.

### Filter

- '? & = : /'를 제외한 특수문자가 URL에 포함된 경우 접속을 차단합니다.
- 영소문자, 영대문자, 숫자, '?', '&', '=', ':', '/' 문자들로만 구성된 URL을 허용하도록 설정했습니다.
- 접속을 차단한다는 것은 HTTP 400 상태와 이유를 응답하는 형태로 구현했습니다.

### Spring AOP

- 컨트롤러의 생성, 조회, 수정 메서드에 진입하기 전에 설정해놓은 로직이 실행되도록 구현했습니다.
- 컨트롤러 메서드 명과 요청 헤더에 들어있는 User-Agent 값을 console에 출력합니다.

### Lotto

- 중복되지 않는 1 ~ 45 범위의 6개의 숫자를 랜덤으로 생성하는 Util 클래스를 작성했습니다.
- /lottos POST 요청이 들어온다면 랜덤으로 로또 번호를 생성해서 저장 후 응답하도록 구현했습니다.

### Batch

- 스케줄러를 활용하여 일요일 자정마다 Batch가 실행되도록 설정했습니다.
- 당첨 번호와 전부 같으면 1등, 다른 숫자가 1개씩 생길때마다 그 다음 등수로 rank를 측정했습니다.
- 당첨자를 검수하는 Batch Job은 1개의 Step으로 구성했습니다.
- Chunk 기반으로 데이터를 처리하도록 설정했습니다. 그에 따라 DB에서 데이터를 가져오는 LottoItemReader, 로또 번호의 당첨 여부를 검증하여 당첨자를 반환하는 LottoItemProcessor, 당첨자를 DB에 저장하는 LottoItemWriter를 구현했습니다.
- Batch 실행 시 필요한 Metadata 테이블을 생성하기 위한 Initializer를 구현했습니다.

## 회고

### Lotto (데이터 설계)

- 로또 번호 6개를 저장할 때, 초기엔 Converter를 사용해서 Spring에서는 List 형태로 데이터를 관리하고 DB에서는 ","를 활용해서 관리하려고 했습니다. 하지만, 데이터가 원자성을 지켜야한다는 제1정규화에 위배된다고 생각해서 각 번호를 따로 저장하는 방식을 선택했습니다.

### Lotto (개선할 점)

- 최대한 문제에 기술되어 있는대로 설계했지만, 한 가지 문제가 발생할 수 있을 가능성이 있다고 생각했습니다. Lotto와 Winner는 1:1 관계로 설정했는데, 데이터가 누적된 상태로 배치 프로그램이 실행될 때 이미 당첨에 저장된 로또가 중복 당첨될 수 있다고 생각했습니다. 해결할 수 있는 방법에는 여러가지가 있겠지만, 로또 엔티티에 발급 일시를 넣고, 배치 프로그램이 실행될 때 일주일 전의 데이터만을 가져오는 방법도 좋을 것 같다고 생각했습니다.

### Batch (Chunk vs Tasklet)

- Chunk 기반으로 데이터를 처리할지, Tasklet 기반으로 데이터를 처리할지 고민했습니다. 주로 대량의 데이터를 처리하기 위해선 한번에 처리하는 Tasklet 형식보단 데이터를 나눠서 처리하는 Chunk 형식을 사용하는 것으로 알고 있습니다. 로또 당첨 검증 시스템의 경우, 대량의 사용자가 로또 번호를 발급했을 것이라고 생각했고, 그에 따라 데이터의 개수를 일정하게 나누어 반복적으로 처리해주는 Chunk 형식을 채택해 적용했습니다.

### Batch (ItemReader, ItemProcessor, ItemWriter)

- Chunk 기반으로 데이터를 처리하려면 ItemReader, ItemProcessor, ItemWriter를 구현해야 합니다.
- LottoItemReader에서는 Job이 실행될 때, DB에 저장된 로또 번호 데이터를 가져와야 합니다. 그리고 해당 데이터는 배치가 실행될 때(매주 일요일 자정)마다 변경될 것입니다. Spring Bean으로 설정하면 Spring Context가 초기화될 때, 데이터가 조회될 것입니다. 하지만 앞서 말했듯이 Job이 실행될때마다 DB에서 조회해야 하므로 @JobScope를 활용하여 Job이 실행될때마다 데이터가 조회되도록 설정했습니다.
- LottoItemProcessor에서도 비슷한 이유로 @JobScope를 사용했습니다. 당첨 로또 번호는 배치 Job이 실행될때마다 초기화되어야 하므로 @JobScope를 사용했고, 검증하는 로또 번호들과는 동일한 당첨 로또 번호를 비교할 수 있도록 구현했습니다.
- ItemWriter의 경우는 단순히 입력받은 당첨자(Winner)를 DB에 저장하는 역할만 수행하므로 다른 설정을 해주지 않았습니다.

# 폴리큐브 백엔드 개발자 코딩 테스트

## 1. 시작하기

### 1.1. 개발 환경

- OpenJDK 17
- Spring Boot 3.2.1

### 1.2. 라이브러리

- Spring Web
- Lombok
- H2 Database ( ID : pc, PW : 2024 )
- 그 외 필요한 라이브러리는 `build.gradle`에 추가하시면 됩니다.

**라이브러리 추가 시, 어떠한 이유로 추가했는지 프로젝트 설명에 간단히 적어주시면 됩니다.**

### 1.3. 실행 방법

```shell
./gradlew bootRun
```

## 2. 개발 요구사항

공통, 기본, 구현 문제로 구성되어 있으며, 각 문제에 대한 요구사항을 모두 만족해야 합니다.

### 2.1. 공통 (20점)

- [ ] `@ControllerAdvice`, `@ExceptionHandler`를 이용하여, 잘못된 요청에 대한 응답을 처리한다. (4점)
  - [ ] API를 호출할 때, 잘못된 요청이 들어오면 HTTP 400 상태의 `{"reason": 실제사유}`을 응답한다.
  - [ ] API에 대한 실패 상황 통합 테스트 코드 작성
  - [ ] 존재하지 않는 API 호출 시, HTTP 404 상태의 `{"reason": 실제사유}`을 응답한다.
- [ ] Spring MVC 아키텍처와 Restful API를 준수하여 개발한다. (8점)
  - [ ] `@RestController`, `@Service`, `@Repository`를 이용하여 개발한다.
  - [ ] HTTP Method와 URI를 적절하게 사용하여 개발한다.
- [ ] Clean Code를 준수하여 개발한다. (8점)
  - [ ] 코드 스타일을 일관되고 명확하게 작성한다.
  - [ ] 메소드와 클래스의 역할을 명확하게 작성한다.

### 2.2. 기본 문제 (50점)

- [ ] user 등록 API 구현 (8점)
  - [ ] `/users` API를 호출하면, `{"id": ?}`을 응답한다.
  - [ ] `/users` API에 대한 통합 테스트 코드 작성
- [ ] user 조회 API 구현 (8점)
  - [ ] `/users/{id}` API를 호출하면, `{"id": ?, "name": "?"}`을 응답한다.
  - [ ] `/users/{id}` API에 대한 통합 테스트 코드 작성
- [ ] user 수정 API 구현 (8점)
  - [ ] `/users/{id}` API를 호출하면, `{"id": ?, "name": "?"}`을 응답한다.
  - [ ] `/users/{id}` API에 대한 통합 테스트 코드 작성
- [ ] 필터 구현 (12점)
  - [ ] URL에 `? & = : //`를 제외한 특수문자가 포함되어 있을경우 접속을 차단하는 Filter 구현한다.
  - [ ] `/users/{id}?name=test!!` API 호출에 대한 통합 테스트 코드 작성
- [ ] Spring AOP를 활용한 로깅 구현 (14점)
  - [ ] user 등록, 조회, 수정 API에 대해 Request시 Console에 Client Agent를 출력한다.

`user` 테이블

```csv
id,name
```

### 2.3. 구현 문제 (30점)

#### 로또 번호 발급 API 구현 (10점)
- [ ] `POST /lottos` API를 호출하면, `{"numbers": [?, ?, ?, ?, ?, ?]}`을 응답한다.
- [ ] `POST /lottos` API에 대한 통합 테스트 코드 작성

##### Request

```shell
curl -X POST -H "Content-Type: application/json" http://localhost:8080/lottos
```

##### Response

```json
{
  "numbers": [?, ?, ?, ?, ?, ?]
}
```

#### 로또 번호 당첨자 검수 Batch 구현 (20점)

- [ ] 랜덤하게 로또 번호를 발급하여, 당첨 번호와 비교하여 당첨자를 검수하는 Batch를 구현한다.
  - [ ] 당첨자의 등수는 1등, 2등, 3등, 4등, 5등이 있다.
  - [ ] 당첨자의 등수는 당첨 번호와 일치하는 번호의 개수로 판단한다.
  - [ ] 당첨자 정보는 `winner` 테이블에 저장한다.
- [ ] Batch는 매주 일요일 0시에 실행되도록 구현한다.
- [ ] Batch에 대한 통합 테스트 코드 작성

##### Input Data

`lotto` 테이블

```csv
id,number_1,number_2,number_3,number_4,number_5,number_6
1,7,28,33,2,45,19
2,26,14,41,3,22,35
3,15,29,38,6,44,21
4,31,16,42,9,23,36
5,17,30,39,10,45,24
```

##### Output Data

`winner` 테이블

```csv
id,lotto_id,rank
```

- `id`: generated value
- `lottos_id`: 당첨 번호의 `id`
- `rank`: 당첨 등수 (1등, 2등, 3등, 4등, 5등)

#### 추가 설명

- `?`는 임의의 값으로, 실제로 응답할 때는 해당 값이 들어가야 합니다.
- `id`는 `Long` 타입입니다.
- `@ExceptionHandler`는 `@RestControllerAdvice`를 이용하여 구현합니다.

## 제출 방법

- 개발이 완료되면, 본인의 github 리포지토리에 올리고 해당 주소를 보내주시면 됩니다.
- 응시자가 개발하면서 고민했던 점, 혹은 어려웠던 점을 프로젝트 설명에 간단히 적어주시면 됩니다. (선택사항)
