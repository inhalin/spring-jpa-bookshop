package book.shop.service;

import book.shop.domain.Member;
import book.shop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 기본값을 readOnly 로 설정
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional // 수정이 필요한 곳에는 트랜젝셔널 애노테이션을 붙여준다.
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        System.out.println(member.getId());
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> membersFound = memberRepository.findByName(member.getName());

        if (!membersFound.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }

}
