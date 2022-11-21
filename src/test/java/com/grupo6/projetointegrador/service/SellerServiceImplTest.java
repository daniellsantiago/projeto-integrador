package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateSellerDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.factory.SellerFactory;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.repository.SellerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class SellerServiceImplTest {

    @Mock
    private SellerRepo sellerRepo;

    @InjectMocks
    private SellerServiceImpl sellerService;

    @Test
    void createSeller_returnSeller_whenAllDataValid() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();

        // When
        Seller newSeller = SellerFactory.build(createSellerDto);
        Mockito.when(sellerRepo.findAll()).thenReturn(new ArrayList<>());
        Mockito.when(sellerRepo.save(ArgumentMatchers.any())).thenReturn(newSeller);
        Seller result = sellerService.createSeller(createSellerDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getZipCode()).isEqualTo(createSellerDto.getZipCode());
        assertThat(result.getAddress()).isEqualTo(createSellerDto.getAddress());
        assertThat(result.getEmail()).isEqualTo(createSellerDto.getEmail());
        assertThat(result.getFirstName()).isEqualTo(createSellerDto.getFirstName());
        assertThat(result.getLastName()).isEqualTo(createSellerDto.getLastName());
        assertThat(result.getHouseNumber()).isEqualTo(createSellerDto.getHouseNumber());
    }

    @Test
    void createSeller_returnBusinessRuleException_whenZipCodeAddressNotMatch() {
        // Given
        CreateSellerDto createSellerDto = new CreateSellerDto(
                "Fulano",
                "de Tal",
                "fulano.dtal@teste.com",
                "Rua José Miguel Titonel",
                123,
                "22793420"
        );

        // When / Then
        assertThatThrownBy(() -> sellerService.createSeller(createSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void createSeller_returnBusinessRuleException_whenZipCodeInvalid() {
        // Given
        CreateSellerDto createSellerDto = new CreateSellerDto(
                "Fulano",
                "de Tal",
                "fulano.dtal@teste.com",
                "Rua José Miguel Titonel",
                123,
                "00000000"
        );

        // When / Then
        assertThatThrownBy(() -> sellerService.createSeller(createSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void createSeller_returnBusinessRuleException_whenEmailAlreadyExists() {
        // Given
        CreateSellerDto createSellerDto = new CreateSellerDto(
                "Fulano",
                "de Tal",
                "fulano.dtal@teste.com",
                "Rua José Miguel Titonel",
                123,
                "26379030"
        );
        List<Seller> sellers = new ArrayList<>();
        sellers.add(SellerFactory.build(setupCreateSellerDto()));

        // When
        Mockito.when(sellerRepo.findAll()).thenReturn(sellers);

        // Then
        assertThatThrownBy(() -> sellerService.createSeller(createSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void findSeller_returnSeller_whenSellerExists() {
        // Given
        Seller seller = SellerFactory.build(setupCreateSellerDto());

        // When
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));
        Seller result = sellerService.findSeller(ArgumentMatchers.anyLong());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(seller.getId());
    }

    @Test
    void findSeller_returnNotFoundException_whenSellerDoesntExists() {
        // Given

        // When
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> sellerService.findSeller(ArgumentMatchers.anyLong())).isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateSeller_returnSeller_whenAllDataValid() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();
        CreateSellerDto updateSellerDto = new CreateSellerDto(
                "Ciclano",
                null,
                "ciclano.dtal@teste.com",
                null,
                null,
                null
        );

        // When
        Seller seller = SellerFactory.build(createSellerDto);
        Seller updatedSeller = new Seller(
                seller.getId(),
                updateSellerDto.getFirstName(),
                seller.getLastName(),
                updateSellerDto.getEmail(),
                seller.getAddress(),
                seller.getHouseNumber(),
                seller.getZipCode(),
                seller.getActive(),
                seller.getProducts()
        );
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));
        Mockito.when(sellerRepo.findAll()).thenReturn(new ArrayList<>());
        Mockito.when(sellerRepo.save(ArgumentMatchers.any())).thenReturn(updatedSeller);
        Seller result = sellerService.updateSeller(seller.getId(), updateSellerDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(seller.getId());
        assertThat(result.getZipCode()).isEqualTo(createSellerDto.getZipCode());
        assertThat(result.getAddress()).isEqualTo(createSellerDto.getAddress());
        assertThat(result.getEmail()).isEqualTo(updatedSeller.getEmail());
        assertThat(result.getFirstName()).isEqualTo(updatedSeller.getFirstName());
        assertThat(result.getLastName()).isEqualTo(createSellerDto.getLastName());
        assertThat(result.getHouseNumber()).isEqualTo(createSellerDto.getHouseNumber());
    }

    @Test
    void updateSeller_returnBusinessRuleException_whenSellerIsInactive() {
        // Given

        // When
        Seller seller = SellerFactory.build(setupCreateSellerDto());
        seller.setActive(Active.INATIVO);
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(seller.getId(), setupCreateSellerDto()))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void updateSeller_returnNotFoundException_whenSellerDoesntExists() {
        // Given

        // When
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(1L, setupCreateSellerDto()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateSeller_returnBusinessRuleException_whenSellerZipCodeAddressDoesntMatch() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();
        CreateSellerDto updateSellerDto = new CreateSellerDto(
                null,
                null,
                null,
                "Rua Teste",
                null,
                "86030190"
        );

        // When
        Seller seller = SellerFactory.build(createSellerDto);
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(seller.getId(), updateSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void updateSeller_returnBusinessRuleException_whenSellerZipCodeDoesntMatchDBAddress() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();
        CreateSellerDto updateSellerDto = new CreateSellerDto(
                null,
                null,
                null,
                null,
                null,
                "86030190"
        );

        // When
        Seller seller = SellerFactory.build(createSellerDto);
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(seller.getId(), updateSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void updateSeller_returnBusinessRuleException_whenSellerAddressDoesntMatchDBZipCode() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();
        CreateSellerDto updateSellerDto = new CreateSellerDto(
                null,
                null,
                null,
                "Rua Teste",
                null,
                null
        );

        // When
        Seller seller = SellerFactory.build(createSellerDto);
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(seller.getId(), updateSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void updateSeller_returnBusinessRuleException_whenSellerEmailAlreadyExists() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();
        CreateSellerDto updateSellerDto = new CreateSellerDto(
                null,
                null,
                "teste@teste.com",
                null,
                null,
                null
        );
        Seller seller = SellerFactory.build(createSellerDto);
        Seller seller2 = new Seller(
                2L,
                "Beltrana",
                "de Tal",
                "teste@teste.com",
                "Rua Jose Penha Tavares",
                321,
                "68909791",
                Active.ATIVO,
                new ArrayList<>()
        );
        List<Seller> sellers = new ArrayList<>();
        sellers.add(seller);
        sellers.add(seller2);

        // When
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));
        Mockito.when(sellerRepo.findAll()).thenReturn(sellers);

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(seller.getId(), updateSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void deleteSeller_inactivatesSeller_whenSellerExistsAndActive() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();

        // When
        Seller seller = SellerFactory.build(createSellerDto);
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));
        sellerService.deleteSeller(seller.getId());
        Seller inactiveSeller = sellerService.findSeller(seller.getId());

        // Then
        assertThat(inactiveSeller).isNotNull();
        assertThat(inactiveSeller.getActive()).isEqualTo(Active.INATIVO);
    }

    @Test
    void deleteSeller_throwsNotFoundException_whenSellerDoesntExists() {
        // Given

        // When
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> sellerService.deleteSeller(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void deleteSeller_throwsBusinessRuleException_whenSellerAlreadyInactive() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();

        // When
        Seller seller = new Seller(
                1L,
                createSellerDto.getFirstName(),
                createSellerDto.getLastName(),
                createSellerDto.getEmail(),
                createSellerDto.getAddress(),
                createSellerDto.getHouseNumber(),
                createSellerDto.getZipCode(),
                Active.INATIVO,
                new ArrayList<>()
        );
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));

        // Then
        assertThatThrownBy(() -> sellerService.deleteSeller(seller.getId()))
                .isInstanceOf(BusinessRuleException.class);
    }

    private CreateSellerDto setupCreateSellerDto() {
        return new CreateSellerDto(
                "Fulano",
                "de Tal",
                "fulano.dtal@teste.com",
                "Rua José Miguel Titonel",
                123,
                "26379030"
        );
    }
}
