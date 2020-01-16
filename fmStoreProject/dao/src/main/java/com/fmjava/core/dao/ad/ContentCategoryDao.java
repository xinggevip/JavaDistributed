package com.fmjava.core.dao.ad;

import com.fmjava.core.pojo.ad.ContentCategory;
import com.fmjava.core.pojo.ad.ContentCategoryQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContentCategoryDao {
    int countByExample(ContentCategoryQuery example);

    int deleteByExample(ContentCategoryQuery example);

    int insert(ContentCategory record);

    int insertSelective(ContentCategory record);

    List<ContentCategory> selectByExample(ContentCategoryQuery example);

    int updateByExampleSelective(@Param("record") ContentCategory record, @Param("example") ContentCategoryQuery example);

    int updateByExample(@Param("record") ContentCategory record, @Param("example") ContentCategoryQuery example);
}