package com.kh.jpatotalapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Board {
    @Id // 해당 필드가 Entity클래스(Board)의 primary key임을 지정
    @GeneratedValue(strategy = GenerationType.AUTO) // primary key가 자동 생성하도록 만듬. strategy속성을 통해 생성 전략을 지정
    private Long boardId;
    private String title;
    private String content;
    private String imgPath;
    private LocalDateTime regDate;
    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }
    @ManyToOne(fetch = FetchType.LAZY) // 지연 전략
    @JoinColumn(name = "member_id") // 외래키
    private Member member; // 작성자

    // 카테고리 추가
    @ManyToOne(fetch = FetchType.LAZY) // 지연 전략
    @JoinColumn(name = "category_id") // 외래키
    private Category category; // 카테고리
    // Board와 Comment는 1:N 관계, mappedBy는 연관관계의 주인이 아니다(난 FK가 아니에요)라는 의미
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments; // 댓글 목록
}