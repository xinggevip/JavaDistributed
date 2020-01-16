package com.fmjava.core.dao.good;

import com.fmjava.core.pojo.good.Goods;
import com.fmjava.core.pojo.good.GoodsQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsDao {
    int countByExample(GoodsQuery example);

    int deleteByExample(GoodsQuery example);

    int insert(Goods record);

    int insertSelective(Goods record);

    List<Goods> selectByExample(GoodsQuery example);

    int updateByExampleSelective(@Param("record") Goods record, @Param("example") GoodsQuery example);

    int updateByExample(@Param("record") Goods record, @Param("example") GoodsQuery example);
}