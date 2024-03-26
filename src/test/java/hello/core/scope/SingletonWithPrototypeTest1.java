package hello.core.scope;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        Assertions.assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        Assertions.assertThat(prototypeBean2.getCount()).isEqualTo(1);

    }

    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class , PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        Assertions.assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        Assertions.assertThat(count2).isEqualTo(1);
    }

    @Scope("singleton") // 기본이 singleton이라 명시 안해줘도됨.
    static class ClientBean {
        /** Case 1
         * 생성 시점에 주입이 되었기때문에, ClientBean에 종속되어 호출되도 새로운 인스턴스가 생성되지 않는다.
         *
         * 참고 : 여러 빈에서 같은 프로토타입 빈을 주입 받으면, 주입 받는 시점에 각각 새로운 프로토타입 빈이 생성된다.
         * 예를 들어서 clientA, clientB가 각각 의존관계 주입을 받으면 각각 다른 인스턴스의 프로토타입 빈을 주입 받는
         * 다.
         * clientA prototypeBean@x01
         * clientB prototypeBean@x02
         * 물론 사용할 때 마다 새로 생성되는 것은 아니다.
            private final PrototypeBean prototypeBean;

            public ClientBean(PrototypeBean prototypeBean) {
                this.prototypeBean = prototypeBean;
            }

            public int logic() {
                prototypeBean.addCount();
                return prototypeBean.getCount();
            }
         */

        /** Case 2
         * 과거에는 ObjectFactory를 사용했으나, 여러 편의 기능을 추가해서 ObjectProvider가 만들어 졌다.
            // private ObjectFactory<PrototypeBean> prototypeBeanProvider;
            @Autowired
            private ObjectProvider<PrototypeBean> prototypeBeanProvider;

            public int logic() {
                // IOC 컨테이너에서 bean을 찾아서 반환한다. 찾는 기능만 제공하는 역할이다 [DL]. 즉, 새로운 프로토타입 bean이 생성된다.
                PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
                prototypeBean.addCount();
                return prototypeBean.getCount();
            }
         */

        /** Case 3
         * JSR-330 자바 표준을 사용하는 방법이다.
         * get() 메서드 하나로 기능이 매우 단순하다. 다만 라이브러리를 추가해야한다.
         * 자바 표준이므로, 스프링이 아닌 다른 컨테이너에서도 사용할 수 있다.
         */
        // private ObjectFactory<PrototypeBean> prototypeBeanProvider;
        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            // IOC 컨테이너에서 bean을 찾아서 반환한다. 찾는 기능만 제공하는 역할이다 [DL]. 즉, 새로운 프로토타입 bean이 생성된다.
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }

        /** Case 4
         * 위 방법이 아닌, 호출 될 때마다 새로운 인스턴스가 생성되야 한다면 아래 같은 방법으로 수정이 가능하다.
         * 하지만 IOC 컨테이너에 종속적인 코드가 되고 단위 테스트도 어려워진다.
            @Autowired
            ApplicationContext applicationContext;

            public int logic() {
                PrototypeBean prototypeBean = applicationContext.getBean(PrototypeBean.class);
                prototypeBean.addCount();
                return prototypeBean.getCount();
            }
         */

    }

    @Scope("prototype") // 호출 될 때마다 새로운 인스턴스가 생성된다.
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init = " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
