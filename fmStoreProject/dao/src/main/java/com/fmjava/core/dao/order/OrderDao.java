package com.fmjava.core.dao.order;

import com.fmjava.core.pojo.order.Order;
import com.fmjava.core.pojo.order.OrderQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDao {
    int countByExample(OrderQuery example);

    int deleteByExample(OrderQuery example);

    int insert(Order record);

    int insertSelective(Order record);

    List<Order> selectByExample(OrderQuery example);

    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderQuery example);

    int updateByExample(@Param("record") Order record, @Param("example") OrderQuery example);
}