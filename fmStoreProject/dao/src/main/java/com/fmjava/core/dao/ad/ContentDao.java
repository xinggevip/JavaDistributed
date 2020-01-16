package com.fmjava.core.dao.ad;

import com.fmjava.core.pojo.ad.Content;
import com.fmjava.core.pojo.ad.ContentQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContentDao {
    int countByExample(ContentQuery example);

    int deleteByExample(ContentQuery example);

    int insert(Content record);

    int insertSelective(Content record);

    List<Content> selectByExample(ContentQuery example);

    int updateByExampleSelective(@Param("record") Content record, @Param("example") ContentQuery example);

    int updateByExample(@Param("record") Content record, @Param("example") ContentQuery example);
}