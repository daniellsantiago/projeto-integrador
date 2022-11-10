package com.grupo6.projetointegrador.model.entity;

<<<<<<< HEAD:src/main/java/com/grupo6/projetointegrador/model/entity/Section.java
import com.grupo6.projetointegrador.model.enumeration.StorageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import lombok.*;
>>>>>>> refs/remotes/origin/feature/freshproductslist:src/main/java/com/grupo6/projetointegrador/model/Section.java

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
