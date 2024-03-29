package com.example.godtudy.domain.post.entity;

import com.example.godtudy.domain.BaseEntity;
import com.example.godtudy.domain.comment.entity.Comment;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.post.dto.request.PostUpdateRequestDto;
import com.example.godtudy.global.file.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPost extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adminPost_Id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Builder.Default
    @OneToMany(mappedBy = "adminPost")
    private List<File> files = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "category")
    private PostEnum noticeOrEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "adminPost", orphanRemoval = true, cascade = ALL) //, orphanRemoval = true, cascade = ALL
    @OrderBy("id asc")
    private List<Comment> commentList = new ArrayList<>();
//    orphanRemoval 는 연관관계가 끊어진 자식 엔티티를 자동으로 삭제해주는 기능이다.

    // == 연관관계 편의 메서드 == //
    public void setAuthor(Member member) {
        if (this.member != null) {
            this.member.getAdminPosts().remove(this);
        }
        this.member = member;
        member.getAdminPosts().add(this);
    }

    public void addFiles(File file) {
        this.files.add(file);
        if (file.getAdminPost()!= this) {
            file.setAdminPost(this);
        }
    }

    //게시글 카테고리 확인
    public void setPostEnum(String post){
        this.noticeOrEvent = PostEnum.valueOf(post.toUpperCase());
    }

    //게시글 업데이트
    public void updateAdminPost(PostUpdateRequestDto postUpdateRequestDto) {
        this.title = postUpdateRequestDto.getTitle();
        this.content = postUpdateRequestDto.getContent();
    }

    //파일 초기화
    public void initFiles() {
        this.files = new ArrayList<>();
    }

}
