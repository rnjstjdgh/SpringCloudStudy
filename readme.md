# 도전할 사항

### 1.  Gateway, Config, Service Discovery 서버까지 포함한 모든 마이크로 서비스가 임의의 엔드포인트를 가지더라도 유기적으로 구동될 수 있는 구조를 가질  수 있을까?
* 비즈니스와 관련된 서비스들은 config server의 엔드포인트를 알면 그곳을 호출해 설정을 가져올 수 있다.
* 또, gateway 엔드포인트를 알면 gateway를 통해 서로가 서로를 호출 할 수 있다.
* gateway는 service discovery 엔드포인트를 알면 나머지 서버의 엔드포인트도 알 수 있다.

* 이러한 이유로 본 강의에서는 Gateway, Config, Service Discovery 서버의 포트는 고정해 놓았다.
* 목표는, 이런 고정된 엔드포인트를 가진 서버 마저도 임의의 엔드포인트를 가지가 되었을 때, 마이크로 서비스가 잘 돌아갈 수 있도록 하는 것이다.
    * 이게 가능하다면, fargate 구동 시 elb의 의존도를 많이 줄일 수 있다.
    * 하나의 아이디어는, 모든 fargate task가 공통된 NFS를 마운트 하고 특정 파일에 엔드포인트 정보를 기입하기로 약속하는 방식이다.
    
### 하나의 가설
1. 가장 먼저 Service discovery, gateway를 올린다.
    * 구동 시 엔드포인트를 약속된 파일에 기입
2. config server를 올린다
    * 이때 Service discovery, gateway의 엔드포인트도 약속된 파일에서 읽어서 config server 설정에 추가한다.
    * 그 엔드포인트에 헬스체크를 날려보고 죽어있다면 본 테스크도 죽인다.
        * fargate 서비스가 알아서 다시 구동시켜줄 것이다.
        * 그럼 Service discovery, gateway서버가 정상 준비 될 때 까지 이 과정을 반복하게 된다.
    * 또, config server 자기 자신의 엔드포인트도 약속된 파일에 기입한다.
3. 다른 나머지 서비스를 기동시킨다.
    * 이때 약속된 파일로 부터 config server의 엔드포인트를 얻는다.
    * 그 엔드포인트에 헬스체크를 날려보고 죽어있다면 본 테스크도 죽인다.  
    * 그리고 모든 설정을 config server로 부터 업데이트 한다.

### 중요한 것은 config임
* config에 결국 각 마이크로 서비스의 엔드포인트가 있을 것임
* 이걸 어떻게 관리하느냐가 마이크로서비스간의 연관 관계를 묶는 핵심이 될 것이다.

* 모든 서비스는 다른 서비스를 호출할 때 아래 순서를 따른다.
   1.  기존 프로퍼티에 있는 엔드포인트로 요청한다.
   2.  1번에서 성공하면 끝이다.
   3.  1번에서 not found로 실패하면 약속된 파일로 부터 실패한 서비스의 엔드포인트를 업데이트 후 다시 요청한다.
   4.  3번도 not found로 실패하면 그냥 실패한 것이다.

### 약속된 엔드포인트 파일을 json으로 설계할까?


### 서버 위치는 항상 변한다는 점을 해결하고 싶은데 항상 변하는 서버 위치를 결코 변하지 않는 파일 위치를 지정해 기록해 두는 것으로 해결 할 수 있지 않을까? 하는 생각 

### 일반 마이크로 서비스는 기동 시 자동으로 서비스 디스커버리에 엔드포인트가 등록되기 때문에, spring cloud 말고 내가 별도로 관리해야 하는 엔드포인트는 gateway, service-discovery, config server 3가지 일 것이다.

### 찾는게 목적이니 discovery-service를 기준으로 움직이는게 맞는것 아닐까?
* 최소에 discovery-service를 가장 먼저 실행시키고 그 다음에 config-service를 실행시키면서 discovery-service에게 등록하고 나머지 서비스들도 실행하면서 config-service에서 설정을 읽고 discovery-service에 등록하는 방식으로 하면 되는것 아닌가?
* 이렇게 되면, discovery-service의 엔드포인트만 맨 처음에 공유 되면 되는 것 아닌가?


### 아니면 결국 모든 서비스는 설정 정보에 의존해서 연관관계가 묶이기 때문에 config server를 중심으로 생각하는게 맞는건가?
* 지금으로서는 이게 더 맞는 방법으로 보인다.
* ![image](https://user-images.githubusercontent.com/41561652/144712769-16689fd1-f55f-4962-9dce-26ccd7a6b06d.png)
    * 매 요청마다 discovery-service를 조회할 수 밖에 없기 때문에 discovery service와 다른 서비스는 같은 network에 묶여있는게 맞는것 같다.
    * 유사시에만, config server는 적게 호출 되기 때문에 외부 고정 ip를 부여받을 수 있는 network에 배치해도 큰 문제는 없을 것 같다.
    * 이렇게 되면 discovery server의 엔드포인트를 찾는 것이 문제가 된다.

### yml 파일 편집하는 방법
* https://stackabuse.com/reading-and-writing-yaml-files-in-java-with-jackson/
* 서비스 구동 시 해당 서비스의 엔드포인트를 yml파일 편집하여 기존 엔드포인트에 추가해 주는 방식을 생각해 볼 수 있을 것 같다.
* spring boot 구동 직후 특정 코드 수행하기
   * https://osc131.tistory.com/139 
