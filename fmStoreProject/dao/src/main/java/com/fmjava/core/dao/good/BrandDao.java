package com.fmjava.core.dao.good;

import com.fmjava.core.pojo.good.Brand;
import com.fmjava.core.pojo.good.BrandQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BrandDao {
    int countByExample(BrandQuery example);

    int deleteByExample(BrandQuery example);

    int insert(Brand record);

    int insertSelective(Brand record);

    List<Brand> selectByExample(BrandQuery example);

    int updateByExampleSelective(@Param("record") Brand record, @Param("example") BrandQuery example);

    int updateByExample(@Param("record") Brand record, @Param("example") BrandQuery example);
}