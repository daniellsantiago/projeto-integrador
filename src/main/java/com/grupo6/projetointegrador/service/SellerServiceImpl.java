package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateSellerDto;
import com.grupo6.projetointegrador.dto.ZipCodeDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.repository.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepo sellerRepo;

    /**
     * This method returns a new Seller.
     * Or throws a {@link BusinessRuleException} if the zip code doesn't match the address given.
     * Or throws a {@link BusinessRuleException} if the email provided already exists in the database.
     *
     * @param createSellerDto This is the entry object of seller information,
     *                        containing first name, last name, email, address, house number and zip code.
     * @return A Seller object containing the information received.
     */
    @Override
    @Transactional
    public Seller createSeller(CreateSellerDto createSellerDto) {
        checkZipCode(createSellerDto.getZipCode(), createSellerDto.getAddress());
        checkEmail(createSellerDto.getEmail(), null);

        Seller newSeller = new Seller();
        List<Product> products = new ArrayList<>();

        newSeller.setProducts(products);
        newSeller.setActive(Active.ATIVO);
        newSeller.setAddress(createSellerDto.getAddress());
        newSeller.setZipCode(createSellerDto.getZipCode());
        newSeller.setEmail(createSellerDto.getEmail());
        newSeller.setHouseNumber(createSellerDto.getHouseNumber());
        newSeller.setFirstName(createSellerDto.getFirstName());
        newSeller.setLastName(createSellerDto.getLastName());

        return sellerRepo.save(newSeller);
    }

    /**
     * This method returns a Seller.
     * Or throws a {@link NotFoundException} if the seller is not found.
     *
     * @param id This is the id of the searched seller.
     * @return A Seller object of the searched seller.
     */
    @Override
    public Seller findSeller(Long id) {
        return sellerRepo.findById(id).orElseThrow(() -> new NotFoundException("Vendedor não encontrado."));
    }

    /**
     * This method returns an updated Seller.
     * Or throws a {@link NotFoundException} if the seller is not found.
     * Or throws a {@link BusinessRuleException} if the seller is inactive.
     *
     * @param id This is the id of the seller set to be updated.
     * @param createSellerDto This is the entry object of seller information,
     *                        it may contain first name, last name, email, address, house number and zip code.
     * @return A Seller object containing the updated information.
     */
    @Override
    @Transactional
    public Seller updateSeller(Long id, CreateSellerDto createSellerDto) {
        Seller seller = sellerRepo.findById(id).orElseThrow(() -> new NotFoundException("Vendedor não encontrado."));
        if (seller.getActive().equals(Active.INATIVO)) {
            throw new BusinessRuleException("Usuário inativo.");
        }

        updateCheckZipAndAddress(createSellerDto, seller);
        if (createSellerDto.getEmail() != null) {
            checkEmail(createSellerDto.getEmail(), seller.getId());
            seller.setEmail(createSellerDto.getEmail());
        }

        if (createSellerDto.getAddress() != null) {
            seller.setAddress(createSellerDto.getAddress());
        }
        if (createSellerDto.getZipCode() != null) {
            seller.setZipCode(createSellerDto.getZipCode());
        }
        if (createSellerDto.getHouseNumber() != null) {
            seller.setHouseNumber(createSellerDto.getHouseNumber());
        }
        if (createSellerDto.getFirstName() != null) {
            seller.setFirstName(createSellerDto.getFirstName());
        }
        if (createSellerDto.getLastName() != null) {
            seller.setLastName(createSellerDto.getLastName());
        }

        return sellerRepo.save(seller);
    }

    /**
     * This method makes a Seller inactive.
     * Or throws a {@link NotFoundException} if the seller is not found.
     * Or throws a {@link BusinessRuleException} if the seller is already inactive.
     *
     * @param id This is the id of the seller set to be deleted.
     */
    @Override
    public void deleteSeller(Long id) {
        Seller seller = sellerRepo.findById(id).orElseThrow(() -> new NotFoundException("Vendedor não encontrado."));
        if (seller.getActive().equals(Active.INATIVO)) {
            throw new BusinessRuleException("Usuário já está inativo.");
        }
        seller.setActive(Active.INATIVO);
        sellerRepo.save(seller);
    }

    /**
     * This method checks if the zip code matches the address received.
     * Or throws a {@link BusinessRuleException} if the zip code doesn't match the address provided.
     *
     * @param zipCode This is the seller's zip code.
     * @param address This is the seller's address.
     */
    private void checkZipCode(String zipCode, String address) {
        ZipCodeDto zipCodeDtoDto = new RestTemplate()
                .getForEntity("https://viacep.com.br/ws/"+zipCode+"/json/", ZipCodeDto.class)
                .getBody();
        if (!zipCodeDtoDto.getLogradouro().equals(address)) {
            throw new BusinessRuleException("CEP não corresponde ao endereço passado.");
        }
    }

    /**
     * This method checks if the email received already exists in the database, excluding the own seller entry.
     * Or throws a {@link BusinessRuleException} if the email provided already exists in the database
     *                                           and is not associated with the current seller.
     *
     * @param email This is the seller's email.
     * @param id This is the seller's id.
     */
    private void checkEmail(String email, Long id) {
        List<Seller> sellers = sellerRepo.findAll();
        sellers.forEach((seller) -> {
            if (seller.getEmail().matches(email) && !seller.getId().equals(id)) {
                throw new BusinessRuleException("Email já cadastrado.");
            }
        });
    }

    /**
     * This method checks if the zip code matches the address received, depending on the information provided.
     * * Or throws a {@link BusinessRuleException} if the zip code or address provided doesn't match each other
     *                                             or the information from the database.
     *
     * @param createSellerDto This is the entry object of seller information,
     *                        containing first name, last name, email, address, house number and zip code.
     * @param seller Seller object containing all the seller's information.
     */
    private void updateCheckZipAndAddress(CreateSellerDto createSellerDto, Seller seller) {
        if(createSellerDto.getZipCode() != null && createSellerDto.getAddress() != null) {
            checkZipCode(createSellerDto.getZipCode(), createSellerDto.getAddress());
        } else if (createSellerDto.getZipCode() != null) {
            ZipCodeDto zipCodeDtoDto = new RestTemplate()
                    .getForEntity("https://viacep.com.br/ws/"+createSellerDto.getZipCode()+"/json/", ZipCodeDto.class)
                    .getBody();
            if (!zipCodeDtoDto.getLogradouro().equals(seller.getAddress())) {
                throw new BusinessRuleException("CEP não corresponde ao endereço passado.");
            }
        } else if (createSellerDto.getAddress() != null) {
            ZipCodeDto zipCodeDtoDto = new RestTemplate()
                    .getForEntity("https://viacep.com.br/ws/"+seller.getZipCode()+"/json/", ZipCodeDto.class)
                    .getBody();
            if (!zipCodeDtoDto.getLogradouro().equals(createSellerDto.getAddress())) {
                throw new BusinessRuleException("CEP não corresponde ao endereço passado.");
            }
        }
    }
}
