package com.fmjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.item.ItemCatDao;
import com.fmjava.core.pojo.item.ItemCat;
import com.fmjava.core.pojo.item.ItemCatQuery;
import com.fmjava.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ItemCatDao catDao;
    @Override
    public List<ItemCat> findByParentId(Long parentId) {

        if (!redisTemplate.hasKey(Constants.CATEGORY_LIST_REDIS)){
            System.out.println("redis缓存分类");
            //缓存所有的分类  分类key  分类名称  模板的ID
            List<ItemCat> itemCats1 = catDao.selectByExample(null);
            for (ItemCat itemCat : itemCats1) {
                redisTemplate.boundHashOps(Constants.CATEGORY_LIST_REDIS).put(itemCat.getName(),itemCat.getTypeId());
            }
        }


        ItemCatQuery query = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<ItemCat> itemCats = catDao.selectByExample(query);
        return itemCats;
    }

    @Override
    public ItemCat findOne(Long id) {
        return catDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return catDao.selectByExample(null);
    }


}
