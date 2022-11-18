package com.grupo6.projetointegrador.model.entity;

import com.grupo6.projetointegrador.model.enumeration.Category;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Warehouse warehouse;

    private Long volume;

    @Enumerated(EnumType.STRING)
    private Category category;
}
