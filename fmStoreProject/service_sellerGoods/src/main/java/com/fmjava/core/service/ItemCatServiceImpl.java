package com.fmjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.item.ItemCatDao;
import com.fmjava.core.pojo.item.ItemCat;
import com.fmjava.core.pojo.item.ItemCatQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao itemCatDao;

    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = itemCatQuery.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<ItemCat> itemCats = itemCatDao.selectByExample(itemCatQuery);
        return itemCats;
    }
}
