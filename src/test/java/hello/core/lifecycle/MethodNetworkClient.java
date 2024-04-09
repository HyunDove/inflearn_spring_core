package hello.core.lifecycle;

/**
 * 2. 설정 정보 방법으로 bean life-cycle 초기화, 소멸에 대한 부분을 담당한다.
 * 외부 라이브러리에도 적용 가능하며, 메서드 이름을 자유롭게 줄 수 있다.
 * @bean에 등록할때만 설정 가능하다.
 */
public class MethodNetworkClient {

    private String url;

    public MethodNetworkClient() {
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
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }
    public void close() {
        System.out.println("NetworkClient.close");
        disConnect();
    }
}