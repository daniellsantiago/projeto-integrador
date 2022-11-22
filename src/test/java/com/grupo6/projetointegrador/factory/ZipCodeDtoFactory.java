package com.grupo6.projetointegrador.factory;

import com.grupo6.projetointegrador.dto.CreateSellerDto;
import com.grupo6.projetointegrador.dto.ZipCodeDto;

public class ZipCodeDtoFactory {
    private static String complemento;
    private static String bairro;
    private static String localidade;
    private static String uf;

    public static ZipCodeDto build(CreateSellerDto createSellerDto) {
        ZipCodeDto zipCodeDto = new ZipCodeDto();

        if (complemento == null) complemento = "Casa";
        if (bairro == null) bairro = "Centro";
        if (localidade == null) localidade = "Teste";
        if (uf == null) uf = "AM";

        zipCodeDto.setCep(createSellerDto.getZipCode());
        zipCodeDto.setLogradouro(createSellerDto.getAddress());
        zipCodeDto.setComplemento(complemento);
        zipCodeDto.setBairro(bairro);
        zipCodeDto.setLocalidade(localidade);
        zipCodeDto.setUf(uf);

        return zipCodeDto;
    }
}
