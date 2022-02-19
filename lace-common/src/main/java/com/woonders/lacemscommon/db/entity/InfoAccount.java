package com.woonders.lacemscommon.db.entity;

import com.woonders.lacemscommon.db.entityenum.AccountVersion;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "info_account")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "version")
    private AccountVersion version;
    @Column(name = "expirationDate")
    private LocalDate expirationDate;
    @Column(name = "simulator")
    private boolean simulator;
    @Column(name = "multiagency")
    private boolean multiagency;
    @Column(name = "fileStorage")
    private boolean fileStorage;
    @Column(name = "additionalUsers")
    private int additionalUsers;
}
