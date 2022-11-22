package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateBuyerDto;
import com.grupo6.projetointegrador.dto.CreateSellerDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.factory.BuyerFactory;
import com.grupo6.projetointegrador.factory.SellerFactory;
import com.grupo6.projetointegrador.model.entity.Buyer;
import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.repository.BuyerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
public class BuyerServiceImplTest {

    @Mock
    private BuyerRepo buyerRepo;

    @Mock
    private OrderPurchase orderPurchase;

    @InjectMocks
    private BuyerServiceImpl buyerService;

    @Test
    void createBuyer() {
        CreateBuyerDto createBuyerDto = setupCreateBuyerDto();

        Buyer newBuyer = BuyerFactory.build(createBuyerDto);
        Mockito.when(buyerRepo.save(ArgumentMatchers.any())).thenReturn(newBuyer);
        Buyer result = buyerService.save(createBuyerDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(createBuyerDto.getName());
        assertThat(result.getCpf()).isEqualTo(createBuyerDto.getCpf());
        assertThat(result.getAddress()).isEqualTo(createBuyerDto.getAddress());
        assertThat(result.getNeighborhood()).isEqualTo(createBuyerDto.getNeighborhood());
        assertThat(result.getCity()).isEqualTo(createBuyerDto.getCity());
        assertThat(result.getState()).isEqualTo(createBuyerDto.getState());
        assertThat(result.getZipCode()).isEqualTo(createBuyerDto.getZipCode());
    }

    @Test
    void updateBuyer() {
        CreateBuyerDto createBuyerDto = setupCreateBuyerDto();
        CreateBuyerDto updateBuyerDto = new CreateBuyerDto(
                "Fernando",
                null,
                null,
                null,
                null,
                null,
                null
        );

        Buyer buyer = BuyerFactory.build(createBuyerDto);
        Buyer updatedBuyer = new Buyer(
                buyer.getId(),
                buyer.getOrders(),
                updateBuyerDto.getName(),
                buyer.getCpf(),
                buyer.getAddress(),
                buyer.getNeighborhood(),
                buyer.getCity(),
                buyer.getState(),
                buyer.getZipCode(),
                buyer.getActive()
        );

        Mockito.when(buyerRepo.findById(ArgumentMatchers.any())).thenReturn(java.util.Optional.of(buyer));
        Mockito.when(buyerRepo.save(ArgumentMatchers.any())).thenReturn(updatedBuyer);
        Buyer result = buyerService.update(buyer.getId(), updateBuyerDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(buyer.getId());
        assertThat(result.getName()).isEqualTo(updatedBuyer.getName());
        assertThat(result.getCpf()).isEqualTo(updatedBuyer.getCpf());
        assertThat(result.getAddress()).isEqualTo(updatedBuyer.getAddress());
        assertThat(result.getNeighborhood()).isEqualTo(updatedBuyer.getNeighborhood());
        assertThat(result.getCity()).isEqualTo(updatedBuyer.getCity());
        assertThat(result.getState()).isEqualTo(updatedBuyer.getState());
        assertThat(result.getZipCode()).isEqualTo(updatedBuyer.getZipCode());
    }

    @Test
    void updateBuyer_returnBusinessRuleException() {
        Buyer buyer = BuyerFactory.build(setupCreateBuyerDto());
        buyer.setActive(Active.INATIVO);
        Mockito.when(buyerRepo.findById(ArgumentMatchers.any())).thenReturn(java.util.Optional.of(buyer));

        assertThatThrownBy(() -> buyerService.update(buyer.getId(), setupCreateBuyerDto()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cliente inativo.");
    }

    @Test
    void updateSeller_returnBusinessRuleException_whenSellerZipCodeDoesntMatchDBAddress() {
        CreateBuyerDto createBuyerDto = setupCreateBuyerDto();
        CreateBuyerDto updateSellerDto = new CreateBuyerDto(
                null,
                null,
                null,
                null,
                null,
                null,
                "86030190"
        );

        Buyer buyer = BuyerFactory.build(createBuyerDto);
        Mockito.when(buyerRepo.findById(ArgumentMatchers.any())).thenReturn(Optional.of(buyer));

        assertThatThrownBy(() -> buyerService.update(buyer.getId(), updateSellerDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("CEP não corresponde ao endereço passado.");
    }

    @Test
    void deleteBuyer() {
        CreateBuyerDto createBuyerDto = setupCreateBuyerDto();

        Buyer buyer = BuyerFactory.build(createBuyerDto);
        Mockito.when(buyerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(buyer));
        buyerService.deleteById(buyer.getId());
        Buyer result = buyerService.findById(buyer.getId());

        assertThat(result).isNotNull();
        assertThat(result.getActive()).isEqualTo(Active.INATIVO);
    }

    private CreateBuyerDto setupCreateBuyerDto() {
        return new CreateBuyerDto("João", "46540955006", "Rua Murajuba", "Parque Verde", "Belém", "Pará", "66635120");
    }
}
