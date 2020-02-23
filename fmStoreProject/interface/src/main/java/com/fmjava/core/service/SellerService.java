package com.fmjava.core.service;

import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.seller.Seller;

public interface SellerService {
    public void add(Seller seller);

    public PageResult findPage(Seller seller, Integer page, Integer rows);

    Seller findOne(String id);

    void updateStatus(String sellerId, String status);
}
