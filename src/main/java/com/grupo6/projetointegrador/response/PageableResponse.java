package com.grupo6.projetointegrador.response;

import org.springframework.data.domain.Page;

import java.util.Collection;

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

    public Collection<?> getContent() {
        return content;
    }

    public void setContent(Collection<?> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }
}
