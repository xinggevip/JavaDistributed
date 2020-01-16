package com.fmjava.core.dao.item;

import com.fmjava.core.pojo.item.ItemCat;
import com.fmjava.core.pojo.item.ItemCatQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemCatDao {
    int countByExample(ItemCatQuery example);

    int deleteByExample(ItemCatQuery example);

    int insert(ItemCat record);

    int insertSelective(ItemCat record);

    List<ItemCat> selectByExample(ItemCatQuery example);

    int updateByExampleSelective(@Param("record") ItemCat record, @Param("example") ItemCatQuery example);

    int updateByExample(@Param("record") ItemCat record, @Param("example") ItemCatQuery example);
}