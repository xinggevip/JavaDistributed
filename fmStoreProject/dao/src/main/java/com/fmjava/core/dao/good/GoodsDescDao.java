package com.fmjava.core.dao.good;

import com.fmjava.core.pojo.good.GoodsDesc;
import com.fmjava.core.pojo.good.GoodsDescQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsDescDao {
    int countByExample(GoodsDescQuery example);

    int deleteByExample(GoodsDescQuery example);

    int insert(GoodsDesc record);

    int insertSelective(GoodsDesc record);

    List<GoodsDesc> selectByExample(GoodsDescQuery example);

    int updateByExampleSelective(@Param("record") GoodsDesc record, @Param("example") GoodsDescQuery example);

    int updateByExample(@Param("record") GoodsDesc record, @Param("example") GoodsDescQuery example);
}