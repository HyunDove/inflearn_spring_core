package hello.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

/**
 * HTTP 요청 당 하나씩 생성되며, uuid를 별도로 저장해 다른 HTTP 요청과 구분할 수 있다.
 * */
@Component
/**
 * value만 선언할때는, value를 명시안해줘도 된다.
 * proxyMode : 껍데기만 있는 MyLogger를 생성 [ class면 TARGET_CLASS, interface면 INTERFACES를 선택 ]
 * CGLIB라는 라이브러리로 바이트 코드를 조작하여 가짜 프록시 객체를 생성한다.
 * 싱글톤 처럼 동작하며, request scope와는 관계가 없다.
 */
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
    private String uuid;
    private String requestURL;

    // bean이 생성되는 시점에 requestURL을 알 수 없으므로, 외부에서 setter로 입력 받는다.
    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + " [" + requestURL + "]" + message);
    }

    @PostConstruct
    public void init() {
        // 절대 곂치지 않는 unique한 값 생성
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create : " + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close : " + this);
    }
}
