package com.console.mall.respository;

import com.console.mall.dto.MemberDTO;
import com.console.mall.entitiy.Cart;
import com.console.mall.entitiy.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);

    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public Member findId(String id) {
        return em.createQuery("select m from Member m where m.login_id=:login_id", Member.class)
                .setParameter("login_id", id)
                .getSingleResult();
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select  m from Member m where m.name = :name", Member.class).setParameter("name", name).getResultList();
    }

    public List<Member> isId(String loginid) {
        return em.createQuery("select  m from Member m where m.login_id = :loginId", Member.class).setParameter("loginId", loginid).getResultList();
    }
    public List<Member> isEmail(String email){
        return em.createQuery("select m from Member m where m.email=:email",Member.class).setParameter("email",email).getResultList();
    }
    public void deleteById(Long id) {
    }

    public Long findById(String loginId) {
        try {
            return em.createQuery("SELECT m.id FROM Member m WHERE m.login_id = :loginId", Long.class)
                    .setParameter("loginId", loginId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // 회원이 존재하지 않을 때는 null을 반환하도록 처리
        }
    }


    //    public Member findByLoginid(String login_id){
//        return em.find(Member.class,login_id);
//
//    }

    public List<Member> findByloginid(Member member) {
        return em.createQuery("select  m from Member m where m.login_id = :login_id and m.pw = :pw", Member.class)
                .setParameter("login_id", member.getLogin_id())
                .setParameter("pw", member.getPw())
                .getResultList();
    }


    public Cart findByIdCart(String id) {
        return (Cart) em.createQuery("select m.cart from Member m where m.login_id =:id")
                .setParameter("id", id)
                .getSingleResult();

    }
    public void delete(Member m) {

        // Member 엔티티 삭제
        em.remove(m);

    }
}
