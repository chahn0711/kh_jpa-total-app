package com.kh.jpatotalapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity // 테이블 등록 하기 위해 사용
@Table(name = "category") // 지정하고 싶은 테이블명 있어 사용
@Getter @Setter @ToString // 정보 보내고 꺼내기 위해
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;
    private String categoryName;

    // 작성자 하나에 카테고리 여러개?
    @ManyToOne(fetch = FetchType.LAZY) // 지연 전략
    @JoinColumn(name = "member_id") // 외래키, 연결할 때 필요
    private Member member; // 작성자

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Board> boards;

}
