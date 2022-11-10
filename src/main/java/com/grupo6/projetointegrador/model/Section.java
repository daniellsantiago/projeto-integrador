package com.grupo6.projetointegrador.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Warehouse warehouse;

    private Long volume;

    @Enumerated(EnumType.STRING)
    private StorageType storageType;
}
