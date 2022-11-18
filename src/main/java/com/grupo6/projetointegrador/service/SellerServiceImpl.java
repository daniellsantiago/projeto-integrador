package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateSellerDto;
import com.grupo6.projetointegrador.dto.ZipCodeDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.entity.Seller;
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

    @Override
    @Transactional
    public Seller createSeller(CreateSellerDto createSellerDto) {
        checkZipCode(createSellerDto.getZipCode(), createSellerDto.getAddress());
        checkEmail(createSellerDto.getEmail(), null);

        Seller newSeller = new Seller();
        List<Product> products = new ArrayList<>();

        newSeller.setProducts(products);
        newSeller.setAddress(createSellerDto.getAddress());
        newSeller.setZipCode(createSellerDto.getZipCode());
        newSeller.setEmail(createSellerDto.getEmail());
        newSeller.setHouseNumber(createSellerDto.getHouseNumber());
        newSeller.setFirstName(createSellerDto.getFirstName());
        newSeller.setLastName(createSellerDto.getLastName());

        return sellerRepo.save(newSeller);
    }

    @Override
    public Seller findSeller(Long id) {
        return sellerRepo.findById(id).orElseThrow(() -> new NotFoundException("Vendedor não encontrado."));
    }

    @Override
    @Transactional
    public Seller updateSeller(Long id, CreateSellerDto createSellerDto) {
        Seller seller = sellerRepo.findById(id).orElseThrow(() -> new NotFoundException("Vendedor não encontrado."));

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

    @Override
    public void deleteSeller(Long id) {
        Seller seller = sellerRepo.findById(id).orElseThrow(() -> new NotFoundException("Vendedor não encontrado."));
        sellerRepo.delete(seller);
    }

    private void checkZipCode(String zipCode, String address) {
        ZipCodeDto zipCodeDtoDto = new RestTemplate()
                .getForEntity("https://viacep.com.br/ws/"+zipCode+"/json/", ZipCodeDto.class)
                .getBody();
        if (!zipCodeDtoDto.getLogradouro().equals(address)) {
            throw new BusinessRuleException("CEP não corresponde ao endereço passado.");
        }
    }

    private void checkEmail(String email, Long id) {
        List<Seller> sellers = sellerRepo.findAll();
        sellers.forEach((seller) -> {
            if (seller.getEmail().matches(email) && !seller.getId().equals(id)) {
                throw new BusinessRuleException("Email já cadastrado.");
            }
        });
    }

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
