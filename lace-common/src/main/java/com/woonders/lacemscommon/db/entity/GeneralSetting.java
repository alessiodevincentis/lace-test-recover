package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Created by ema98 on 9/21/2017.
 */
@Entity
@Table(name = "general_setting")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    @Column(name = "storageEnabled", nullable = false)
    private boolean storageEnabled;
    @Column(name = "usedStorageSpace", nullable = false)
    private long usedStorageSpace;

}
