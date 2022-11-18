package com.grupo6.projetointegrador.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Getter
@Setter
public class PageableResponse {
    private Collection<?> content;
    private int totalPages;
    private Long totalRecords;

    public PageableResponse toResponse(Page<?> page){
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalRecords = page.getTotalElements();
        return this;
    }
}
