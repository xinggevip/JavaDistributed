package com.fmjava.core.dao.seller;

import com.fmjava.core.pojo.seller.Seller;
import com.fmjava.core.pojo.seller.SellerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SellerDao {
    int countByExample(SellerQuery example);

    int deleteByExample(SellerQuery example);

    int insert(Seller record);

    int insertSelective(Seller record);

    List<Seller> selectByExample(SellerQuery example);

    int updateByExampleSelective(@Param("record") Seller record, @Param("example") SellerQuery example);

    int updateByExample(@Param("record") Seller record, @Param("example") SellerQuery example);
}