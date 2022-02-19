package com.woonders.lacemscommon.db.entity;

import com.woonders.lacemscommon.service.FileService;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by ema98 on 9/15/2017.
 */
@Entity
@Table(name = "cliente_pratica_file")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientePraticaFile {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pratica")
    private Pratica pratica;
    @Column(name = "fileCategory")
    @Enumerated(EnumType.STRING)
    private FileService.FileCategory fileCategory;
    @Column(name = "fileName")
    private String fileName;
    @Column(name = "length")
    private int length;

}
