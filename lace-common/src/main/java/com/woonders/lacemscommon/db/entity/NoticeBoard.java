package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "notice_board")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "body")
    private String body;
    @Column(name = "dateTimeCreation")
    private LocalDateTime dateTimeCreation;
    @OneToOne
    private Operator creatorOperator;
    @Column(name = "fileNameAttached")
    private String fileNameAttached;
    @OneToOne
    private Azienda aziendaAssigned;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "notice_board_operator", joinColumns = @JoinColumn(name = "notice_board_id"), inverseJoinColumns = @JoinColumn(name = "operator_id"))
    private Set<Operator> operatorSet;
}
