package com.grupo6.projetointegrador.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToMany(mappedBy = "buyer")
  @JsonManagedReference
  private List<OrderPurchase> orders;

  @NotNull
  private String name;

  @NotNull
  private String cpf;

  @NotNull
  private String address;

  @NotNull
  private String neighborhood;

  @NotNull
  private String city;

  @NotNull
  private String state;

  @NotNull
  @Pattern(regexp = "^[0-9]*$")
  @Size(min = 8, max = 8)
  private String zipCode;
}
