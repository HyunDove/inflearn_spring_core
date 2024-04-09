package hello.core.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 1. interface 방법으로 bean life-cycle 초기화, 소멸에 대한 부분을 담당한다.
 * 요즘은 거의 사용하지 않는 방법이다.
 * InitializingBean => bean 의존관계 설정 후 실행되는 함수이다.
 * DisposableBean => 종료 전 콜백함수로 실행된다.
 * */
public class InterfaceNetworkClient implements InitializingBean, DisposableBean {

     private String url;
     public InterfaceNetworkClient() {
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

     @Override
     public void afterPropertiesSet() throws Exception {
         connect();
         call("초기화 연결 메시지");
     }

     @Override
     public void destroy() throws Exception {
         disConnect();
     }
}