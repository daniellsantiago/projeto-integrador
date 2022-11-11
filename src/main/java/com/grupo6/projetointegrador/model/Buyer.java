package com.grupo6.projetointegrador.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Buyer {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

  @OneToMany(mappedBy = "buyer")
  private List<OrderPurchase> orders;
}
