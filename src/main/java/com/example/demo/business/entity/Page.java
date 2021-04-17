package com.example.demo.business.entity;

import lombok.Data;

@Data
public class Page {

    private Integer currentPage;
    private Integer pageSize;
    private Integer total;
    private Integer roomId;
    private Integer userId;

}
