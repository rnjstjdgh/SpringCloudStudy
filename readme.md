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
    * 이때 Service discovery, gateway의 엔드포인트도 약속된 파일에서 읽어서 설정에 추가한다.
    * 그 엔드포인트에 헬스체크를 날려보고 죽어있다면 본 테스크도 죽인다.
        * fargate 서비스가 알아서 다시 구동시켜줄 것이다.
        * 그럼 Service discovery, gateway서버가 정상 준비 될 때 까지 이 과정을 반복하게 된다.
    * 또, config server 자기 자신의 엔드포인트도 약속된 파일에 기입한다.
3. 다른 나머지 서비스를 기동시킨다.
    * 이때 약속된 파일로 부터 config server의 엔드포인트를 얻는다.
    * 그리고 모든 설정을 config server로 부터 업데이트 한다.
