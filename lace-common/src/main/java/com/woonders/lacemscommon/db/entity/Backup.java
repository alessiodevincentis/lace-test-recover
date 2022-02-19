package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "backup")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Backup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "dateLastBackup")
    private LocalDateTime dateLastBackup;
    @Column(name = "passwordBackup")
    private String passwordBackup;
}
