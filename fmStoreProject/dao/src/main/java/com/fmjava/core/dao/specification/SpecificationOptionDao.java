package com.fmjava.core.dao.specification;

import com.fmjava.core.pojo.specification.SpecificationOption;
import com.fmjava.core.pojo.specification.SpecificationOptionQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpecificationOptionDao {
    int countByExample(SpecificationOptionQuery example);

    int deleteByExample(SpecificationOptionQuery example);

    int insert(SpecificationOption record);

    int insertSelective(SpecificationOption record);

    List<SpecificationOption> selectByExample(SpecificationOptionQuery example);

    int updateByExampleSelective(@Param("record") SpecificationOption record, @Param("example") SpecificationOptionQuery example);

    int updateByExample(@Param("record") SpecificationOption record, @Param("example") SpecificationOptionQuery example);
}