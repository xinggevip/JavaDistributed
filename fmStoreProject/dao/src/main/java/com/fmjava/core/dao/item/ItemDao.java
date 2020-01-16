package com.fmjava.core.dao.item;

import com.fmjava.core.pojo.item.Item;
import com.fmjava.core.pojo.item.ItemQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemDao {
    int countByExample(ItemQuery example);

    int deleteByExample(ItemQuery example);

    int insert(Item record);

    int insertSelective(Item record);

    List<Item> selectByExample(ItemQuery example);

    int updateByExampleSelective(@Param("record") Item record, @Param("example") ItemQuery example);

    int updateByExample(@Param("record") Item record, @Param("example") ItemQuery example);
}