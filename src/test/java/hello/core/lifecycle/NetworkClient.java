package hello.core.lifecycle;

import hello.core.annotation.MainDiscountPolicy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

// public class NetworkClient implements InitializingBean, DisposableBean {
public class NetworkClient{
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect() {
        System.out.println("connect = " + url);
    }

    public void call(String message) {
        System.out.println("call = " + url + " message = " + message);
    }

    // 서비스 종료시 호출
    public void disconnect() {
        System.out.println("close = " + url);
    }

    /**
     * 1. interface 방법으로 bean life-cycle 초기화, 소멸에 대한 부분을 담당한다.
     * 요즘은 거의 사용하지 않는 방법이다.
     * */
    // InitializingBean => bean 의존관계 설정 후 실행되는 함수이다.
    /*@Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메시지");
    }

    // DisposableBean => 종료 전 콜백함수로 실행된다.
    @Override
    public void destroy() throws Exception {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }*/

    /**
     * 2. 설정 정보 방법으로 bean life-cycle 초기화, 소멸에 대한 부분을 담당한다.
     * 외부 라이브러리에도 적용 가능하며, 메서드 이름을 자유롭게 줄 수 있다.
     * @bean에 등록할때만 설정 가능하다.
     */

    /**
     * 3. annotation 방법으로 bean life-cycle 초기화, 소멸에 대한 부분을 담당한다.
     * spring에서 가장 권장하는 방법이다.
     * 외부 라이브러리에는 적용이 불가능하다.
     */
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }
}
