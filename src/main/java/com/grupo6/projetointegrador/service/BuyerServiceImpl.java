package com.grupo6.projetointegrador.service;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import com.grupo6.projetointegrador.dto.CreateBuyerDto;
import com.grupo6.projetointegrador.dto.ZipCodeDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.model.entity.Buyer;
import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.repository.BuyerRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BuyerServiceImpl implements BuyerService {
    private BuyerRepo buyerRepo;

    public BuyerServiceImpl(BuyerRepo buyerRepo) {
        this.buyerRepo = buyerRepo;
    }

    /**
     * This method is responsible for returning a seller by id
     *
     * @param id - id of the seller
     * @return - seller of searched id
     */
    @Override
    public Buyer findById(Long id) {
        Optional<Buyer> buyer = buyerRepo.findById(id);
        return buyer.get();
    }

    /**
     * This method is responsible for creating a new buyer.
     * Or throw a {@Link BusinessRuleException} if the buyer already exists.
     * Or throw a {@Link BusinessRuleException} if the zip doesn't match the given address.
     *
     * @param createBuyerDto This is the entry object of seller information, containing the following fields:
     *                       name, cpf, address, neighborhood, city, state, zipCode.
     * @return Buyer This returns the created buyer.
     */
    @Override
    @Transactional
    public Buyer save(CreateBuyerDto createBuyerDto) {
        checkZipCode(createBuyerDto.getZipCode(), createBuyerDto.getAddress());
        checkCpf(createBuyerDto.getCpf());
        checkCpfUnique(createBuyerDto);
        Buyer newBuyer = new Buyer();
        List<OrderPurchase> orders = new ArrayList<>();

        newBuyer.setName(createBuyerDto.getName());
        newBuyer.setCpf(createBuyerDto.getCpf());
        newBuyer.setAddress(createBuyerDto.getAddress());
        newBuyer.setNeighborhood(createBuyerDto.getNeighborhood());
        newBuyer.setCity(createBuyerDto.getCity());
        newBuyer.setState(createBuyerDto.getState());
        newBuyer.setZipCode(createBuyerDto.getZipCode());
        newBuyer.setActive(Active.ATIVO);
        newBuyer.setOrders(orders);

        return buyerRepo.save(newBuyer);
    }

    /**
     * This method is responsible for update a buyer.
     * Or throw a {@Link BusinessRuleException} if the buyer doesn't exists.
     * Or throw a {@Link BusinessRuleException} if the buyer is inactive.
     * Or throw a {@Link BusinessRuleException} if the zip doesn't match the given address.
     *
     * @param id             This is the id of the buyer to be updated.
     * @param createBuyerDto This is the entry object of buyer information, containing the following fields:
     *                       name, cpf, address, neighborhood, city, state, zipCode.
     * @return Buyer This returns the updated buyer.
     */
    @Override
    @Transactional
    public Buyer update(Long id, CreateBuyerDto createBuyerDto) {
        Buyer buyer = buyerRepo.findById(id).orElseThrow(() -> new BusinessRuleException("Cliente não encontrado."));
        if (buyer.getActive().equals(Active.INATIVO)) {
            throw new BusinessRuleException("Cliente inativo.");
        }
        updateCheckZipAndAddress(createBuyerDto, buyer);

        if (createBuyerDto.getName() != null) {
            buyer.setName(createBuyerDto.getName());
        }
        if (createBuyerDto.getCpf() != null) {
            buyer.setCpf(createBuyerDto.getCpf());
        }
        if (createBuyerDto.getAddress() != null) {
            buyer.setAddress(createBuyerDto.getAddress());
        }
        if (createBuyerDto.getNeighborhood() != null) {
            buyer.setNeighborhood(createBuyerDto.getNeighborhood());
        }
        if (createBuyerDto.getCity() != null) {
            buyer.setCity(createBuyerDto.getCity());
        }
        if (createBuyerDto.getState() != null) {
            buyer.setState(createBuyerDto.getState());
        }
        if (createBuyerDto.getZipCode() != null) {
            buyer.setZipCode(createBuyerDto.getZipCode());
        }

        return buyerRepo.save(buyer);
    }

    /**
     * This method is responsible for inactive a Buyer.
     * Or throw a {@Link BusinessRuleException} if the buyer doesn't exists.
     * Or throw a {@Link BusinessRuleException} if the buyer is already inactive.
     *
     * @param id This is the id of the buyer to be inactive.
     * @param id
     * @return Buyer This returns the deleted buyer.
     */
    public void deleteById(Long id) {
        Buyer buyer = buyerRepo.findById(id).orElseThrow(() -> new BusinessRuleException("Cliente não encontrado"));
        if (buyer.getActive().equals(Active.INATIVO)) {
            throw new BusinessRuleException("Cliente já está inativo");
        }
        buyer.setActive(Active.INATIVO);
        buyerRepo.save(buyer);
    }

    /**
     * This method is responsible for checking if the zip code matches the given address.
     * Or throw a {@link BusinessRuleException} if the zip doesn't match the given address.
     *
     * @param zipCode This is the zip code of the buyer.
     * @param address This is the address of the buyer.
     */
    private void checkZipCode(String zipCode, String address) {
        ZipCodeDto zipCodeDtoDto = new RestTemplate()
                .getForEntity("https://viacep.com.br/ws/" + zipCode + "/json/", ZipCodeDto.class)
                .getBody();
        if (zipCodeDtoDto.getErro() != null) {
            throw new BusinessRuleException("CEP inválido.");
        }
        if (!zipCodeDtoDto.getLogradouro().equals(address)) {
            throw new BusinessRuleException("CEP não corresponde ao endereço passado.");
        }
    }

    /**
     * This method checks if the zip code matches the given address.
     * Or throw a {@link BusinessRuleException} if the zip doesn't match the given address.
     *
     * @param createBuyerDto This is the entry object of buyer information, containing the following fields:
     *                       name, cpf, address, neighborhood, city, state, zipCode.
     * @param buyer          This is the buyer information.
     */
    private void updateCheckZipAndAddress(CreateBuyerDto createBuyerDto, Buyer buyer) {
        if (createBuyerDto.getZipCode() != null && createBuyerDto.getAddress() != null) {
            checkZipCode(createBuyerDto.getZipCode(), createBuyerDto.getAddress());
        } else if (createBuyerDto.getZipCode() != null) {
            ZipCodeDto zipCodeDtoDto = new RestTemplate()
                    .getForEntity("https://viacep.com.br/ws/" + createBuyerDto.getZipCode() + "/json/", ZipCodeDto.class)
                    .getBody();
            if (!zipCodeDtoDto.getLogradouro().equals(buyer.getAddress())) {
                throw new BusinessRuleException("CEP não corresponde ao endereço passado.");
            }
        } else if (createBuyerDto.getAddress() != null) {
            ZipCodeDto zipCodeDtoDto = new RestTemplate()
                    .getForEntity("https://viacep.com.br/ws/" + buyer.getZipCode() + "/json/", ZipCodeDto.class)
                    .getBody();
            if (!zipCodeDtoDto.getLogradouro().equals(createBuyerDto.getAddress())) {
                throw new BusinessRuleException("CEP não corresponde ao endereço passado.");
            }
        }
    }

    private boolean checkCpf(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        try {
            cpfValidator.assertValid(cpf);
        } catch (InvalidStateException e) {
            throw new BusinessRuleException("CPF inválido.");
        }
        return true;
    }

    private boolean checkCpfUnique(CreateBuyerDto buyer) {
        for (Buyer buyer1 : buyerRepo.findAll()) {
            if (buyer1.getCpf().equals(buyer.getCpf())) {
                throw new BusinessRuleException("CPF já cadastrado.");
            }
        }
        return true;
    }
}
