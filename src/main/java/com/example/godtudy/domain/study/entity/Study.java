package com.example.godtudy.domain.study.entity;

import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.post.entity.StudyPost;
import com.example.godtudy.domain.study.dto.request.UpdateStudyRequestDto;
import com.example.godtudy.domain.todo.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Study {

    @GeneratedValue @Id
    @Column(name = "study_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Member teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Member student;

    @Builder.Default
    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
    private List<Todo> todoList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
    private List<StudyPost> studyPosts = new ArrayList<>();

    private String name;

    private String url;

    private String subject;

    private String shortDescription;

    public void updateStudy(UpdateStudyRequestDto request) {
        this.name = request.getName();
        this.shortDescription = request.getShortDescription();
    }

    @Builder
    public Study(Member teacher, Member student, String name, String url, String subject, String shortDescription) {
        this.teacher = teacher;
        this.student = student;
        this.name = name;
        this.url = url;
        this.subject = subject;
        this.shortDescription = shortDescription;
    }
}
