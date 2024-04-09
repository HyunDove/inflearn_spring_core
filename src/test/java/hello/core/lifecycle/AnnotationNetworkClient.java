package hello.core.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 3. annotation 방법으로 bean life-cycle 초기화, 소멸에 대한 부분을 담당한다.
 * spring에서 가장 권장하는 방법이다.
 * 외부 라이브러리에는 적용이 불가능하다.
 */
public class AnnotationNetworkClient {
    private String url;

    public AnnotationNetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }
    public void setUrl(String url) {
        this.url = url;
    }
    //서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }
    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }
    //서비스 종료시 호출
    public void disConnect() {
        System.out.println("close + " + url);
    }
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }
    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disConnect();
    }
}
