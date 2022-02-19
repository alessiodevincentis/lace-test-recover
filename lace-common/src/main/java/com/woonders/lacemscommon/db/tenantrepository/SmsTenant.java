package com.woonders.lacemscommon.db.tenantrepository;


import javax.persistence.*;
import lombok.*;


/**
 * Created by Giulio on 31/07/2019.
 */
@Entity
@Table(name = "smstenant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsTenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "tenant_name", nullable = false)
    private String tenantName;
    @Column(name = "idSms",unique = true, nullable = false)
    private String idSms;
}
