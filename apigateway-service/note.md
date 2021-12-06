# spring netflix zuul vs spring cloud gateway

### 왜 zuul을 안쓰고 별도 gateway를 쓰기로 spring 진영에서 결정한 것일까?

* zuul v1은 비동기 처리에 취약
* 비동기 처리를 위해 spring에서 자체적으로 gateway를 구현하기로 결정
* zuul v2부터 비동기를 지원했으나 여전히 spring의 다른 모듈과 호환성에 문제가 있었음

### 참고
* https://spring.io/blog/2018/12/12/spring-cloud-greenwich-rc1-available-now#spring-cloud-netflix-projects-entering-maintenance-mode