package my.jelly.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@DynamicInsert
public class Member {
    @Id
    @Column(nullable = false, unique = true)
    private String mEmail; //유저 이메일 : 카카오에서 받아온 정보

    @Column(length = 10, nullable = false)
    private String mNick; //유저 닉네임 : 카카오에서 받아온 정보

    @Column(nullable = true)
    private int mJelly; //나의 젤리

    public void changeJelly(int mJelly) {
        this.mJelly = mJelly;
    } //젤리 변경시 사용

}
