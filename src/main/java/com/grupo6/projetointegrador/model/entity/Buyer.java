package com.grupo6.projetointegrador.model.entity;

import br.com.caelum.stella.bean.validation.CPF;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.grupo6.projetointegrador.model.enumeration.Active;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "buyer")
    @JsonManagedReference
    private List<OrderPurchase> orders;

    private String name;

    @CPF
    @Column(unique = true)
    private String cpf;

    private String address;

    private String neighborhood;

    private String city;

    private String state;

    private String zipCode;

    @Enumerated(EnumType.STRING)
    private Active active;
}
