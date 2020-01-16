package com.fmjava.core.dao.specification;

import com.fmjava.core.pojo.specification.Specification;
import com.fmjava.core.pojo.specification.SpecificationQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpecificationDao {
    int countByExample(SpecificationQuery example);

    int deleteByExample(SpecificationQuery example);

    int insert(Specification record);

    int insertSelective(Specification record);

    List<Specification> selectByExample(SpecificationQuery example);

    int updateByExampleSelective(@Param("record") Specification record, @Param("example") SpecificationQuery example);

    int updateByExample(@Param("record") Specification record, @Param("example") SpecificationQuery example);
}