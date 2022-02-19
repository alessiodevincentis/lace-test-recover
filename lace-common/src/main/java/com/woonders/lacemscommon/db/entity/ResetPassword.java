package com.woonders.lacemscommon.db.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "reset_password")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPassword {

	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Operator operator;

    @Column(name = "modification_data", nullable = false)
    private Date modification_data;
    
    @Column(name = "duration_validity", nullable = false)
    private Integer duration_validity;
    
    @Column(name = "first_access", nullable = false)
    private boolean first_access;

}
