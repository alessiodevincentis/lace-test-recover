package com.woonders.lacemscommon.db.entity;

import com.woonders.lacemscommon.laceenum.PermissionProfile;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Emanuele on 14/11/2016.
 */
@Entity
@Table(name = "operator")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Column(name = "smsBalance", nullable = false)
    private int smsBalance;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "operator_role", joinColumns = @JoinColumn(name = "operator_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> role;
    private boolean receiveLeadEnabled;
    // salva i lead ricevuti dall ultimo cambiamenti di preferenza ricezione
    // lead
    private long leadRicevuti;
    @ManyToOne(fetch = FetchType.LAZY)
    private Azienda azienda;
    @Enumerated(EnumType.STRING)
    @Column(name = "permission_profile")
    private PermissionProfile permissionProfile;
}
