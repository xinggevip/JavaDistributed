package com.fmjava.core.dao.template;

import com.fmjava.core.pojo.template.TypeTemplate;
import com.fmjava.core.pojo.template.TypeTemplateQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TypeTemplateDao {
    int countByExample(TypeTemplateQuery example);

    int deleteByExample(TypeTemplateQuery example);

    int insert(TypeTemplate record);

    int insertSelective(TypeTemplate record);

    List<TypeTemplate> selectByExample(TypeTemplateQuery example);

    int updateByExampleSelective(@Param("record") TypeTemplate record, @Param("example") TypeTemplateQuery example);

    int updateByExample(@Param("record") TypeTemplate record, @Param("example") TypeTemplateQuery example);
}