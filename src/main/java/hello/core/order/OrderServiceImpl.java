package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

// @RequiredArgsConstructor
@Component
public class OrderServiceImpl implements OrderService {

    @Getter
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // 생성자 주입 방식이며 생성자가 딱 1개 있을때는 @Autowired를 생략해도 자동 주입이 된다. 단 스피링 빈에만 해당된다.
    // 타입 매칭의 결과가 2개 이상일 때 필드명, 파라미터 명으로 bean 이름을 매칭한다.
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    // 필드 주입 방식 즉 필드에 바로 주입하는 방법이다. DI 컨테이너가 없으면 아무것도 할 수 없다. 사용을 권장하지 않음.
    /*@Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DiscountPolicy discountPolicy;*/

    // setter 주입 방식 즉 수정자 주입이다. 단 직접 @Autowired를 적용해줘야한다.
    /*@Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }*/

    // 일반 메서드 주입, 한번에 여러 필드를 주입 받을 수 있다. 일반적으로는 잘 사용하지 않는다.
    /*@Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }*/

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
