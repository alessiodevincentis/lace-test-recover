package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "time_parameters")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeParameters {

	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    
    @Column(name = "data_type", nullable = false)
    private String dataType;
    
    @Column(name = "value_parameter", nullable = false)
    private String valueParameter;
    
    @Column(name = "code_identify", nullable = false, length = 2)
    private String codeIdentify;
    
    @Column(name = "description", nullable = true)
    private String description;
}
