package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "access_log")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessLog {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Operator operator;

    @Column(name = "loginDateTime", nullable = false)
    private LocalDateTime loginDateTime;
    
    @Column(name = "ip_code", nullable = true)
    private String ipCode;
    
    @Column(name = "note", nullable = true)
    private String note;

}

