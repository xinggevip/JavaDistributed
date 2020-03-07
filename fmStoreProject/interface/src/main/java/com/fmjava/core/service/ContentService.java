package com.fmjava.core.service;

import com.fmjava.core.pojo.ad.Content;
import com.fmjava.core.pojo.entity.PageResult;

import java.util.List;

public interface ContentService {
    public PageResult findPage(Content content, Integer page, Integer rows);

    void add(Content content);

    Content findOne(Long id);

    void update(Content content);

    void delete(Long[] ids);

    List<Content> findByCategoryId(long categoryId);

    public List<Content> findByCategoryIdFromRedis(Long categoryId);
}
