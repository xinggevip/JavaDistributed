package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.fmjava.core.pojo.item.Item;
import com.fmjava.core.service.SearchService;
import com.fmjava.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map paramMap) {
       //1.获取查询条件
        String keywords = String.valueOf(paramMap.get("keywords"));
       //2.设置高亮
        Map resMap = this.setHighlights(paramMap);
        //3.根据关键字查询所在的分类
        List<String> categoryList = this.findCagegory(paramMap);
        resMap.put("categoryList",categoryList);

        //4.取出分类条件,判断一下有没有 值
        //获取页面点击的分类过滤条件
        String category = String.valueOf(paramMap.get("category"));
        //获取页面点击的品牌过滤条件
        String brand = String.valueOf(paramMap.get("brand"));
        //获取页面点击的规格过滤条件
        String spec = String.valueOf(paramMap.get("spec"));
        //取规格与品牌
        if (category != null && !"".equals(category)){
            //有分类
            this.findSpecAndBrandWithCateName(category);
        }else {
            //如果说没有分类  从分类结果当中取出第一个 查询出品牌和规格
            Map map = this.findSpecAndBrandWithCateName(categoryList.get(0));
            resMap.putAll(map);
        }
        return resMap;
    }

    //根据分类名称查出对应的品牌与规格
    private Map findSpecAndBrandWithCateName(String categoryName){
        //1.从redids当中取出分类对应的模板
        Long typeID =  (Long)redisTemplate.boundHashOps(Constants.CATEGORY_LIST_REDIS).get(categoryName);
        //2.根据模板的id 从redis当中取出对应的规格与品牌
        List<Map> brandList = (List<Map>)redisTemplate.boundHashOps(Constants.BRAND_LIST_REDIS).get(typeID);
        List<Map> specList = (List<Map>)redisTemplate.boundHashOps(Constants.SPEC_LIST_REDIS).get(typeID);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("brandList",brandList);
        map.put("specList",specList);
        return map;
    }


    //查询分类
    public List<String> findCagegory(Map paramMap){
        //1.获取查询条件
        String keywords = String.valueOf(paramMap.get("keywords"));
        //2.创建查询对象
        SimpleQuery query = new SimpleQuery();
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        query.addCriteria(criteria);

        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");
        //把分组放入到查询对象当中
        query.setGroupOptions(groupOptions);

        GroupPage<Item> items = solrTemplate.queryForGroupPage(query, Item.class);
        GroupResult<Item> item_category = items.getGroupResult("item_category");
        Page<GroupEntry<Item>> groupEntries = item_category.getGroupEntries();
        ArrayList<String> resList = new ArrayList<>();
        for (GroupEntry<Item> groupEntry : groupEntries) {
            String groupValue = groupEntry.getGroupValue();
            resList.add(groupValue);
        }
        return resList;
    }

    //高亮查询
    public Map setHighlights(Map paramMap){
        //1.获取查询条件
        String keywords = String.valueOf(paramMap.get("keywords"));
        Integer pageNo = Integer.parseInt(String.valueOf(paramMap.get("pageNo")));
        Integer pageSize = Integer.parseInt(String.valueOf(paramMap.get("pageSize")));

        //获取页面点击的分类过滤条件
        String category = String.valueOf(paramMap.get("category"));
        //获取页面点击的品牌过滤条件
        String brand = String.valueOf(paramMap.get("brand"));
        //获取页面点击的规格过滤条件
        String spec = String.valueOf(paramMap.get("spec"));
        //获取页面点击的价格区间过滤条件
        String price = String.valueOf(paramMap.get("price"));



        //2.创建查询对象
        //SimpleQuery query = new SimpleQuery();
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //根据分类过滤查询
        if (category != null && !"".equals(category)) {
            //创建过滤查询对象
            FilterQuery filterQuery = new SimpleFilterQuery();
            //创建条件对象
            Criteria filterCriteria = new Criteria("item_category").is(category);
            //将条件对象放入过滤对象中
            filterQuery.addCriteria(filterCriteria);
            //过滤对象放入查询对象中
            query.addFilterQuery(filterQuery);
        }

        //根据品牌过滤查询
        if (brand != null && !"".equals(brand)) {
            //创建过滤查询对象
            FilterQuery filterQuery = new SimpleFilterQuery();
            //创建条件对象
            Criteria filterCriteria = new Criteria("item_brand").is(brand);
            //将条件对象放入过滤对象中
            filterQuery.addCriteria(filterCriteria);
            //过滤对象放入查询对象中
            query.addFilterQuery(filterQuery);
        }

        //根据规格过滤查询 spec中的数据格式{网络:移动4G, 内存小大: 16G}
        if (spec != null && !"".equals(spec)) {
            Map<String, String> speMap = JSON.parseObject(spec, Map.class);
            if (speMap != null && speMap.size() > 0) {
                Set<Map.Entry<String, String>> entries = speMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    //创建过滤查询对象
                    FilterQuery filterQuery = new SimpleFilterQuery();
                    //创建条件对象
                    Criteria filterCriteria = new Criteria("item_spec_" + entry.getKey())
                            .is(entry.getValue());
                    //将条件对象放入过滤对象中
                    filterQuery.addCriteria(filterCriteria);
                    //过滤对象放入查询对象中
                    query.addFilterQuery(filterQuery);
                }
            }
        }


        //根据价格过滤    0-500   500-1000    1000-1500   .....    3000 - *
        if (price != null && !"".equals(price)) {
            //切分价格, 这个素组中有最小值和最大值
            String[] split = price.split("-");
            if (split != null && split.length == 2) {
                //说明大于等于最小值, 如果第一个最小值为0, 进入不到这里
                if (!"0".equals(split[0])) {
                    //创建过滤查询对象
                    FilterQuery filterQuery = new SimpleFilterQuery();
                    //创建条件对象
                    Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(split[0]);
                    //将条件对象放入过滤对象中
                    filterQuery.addCriteria(filterCriteria);
                    //过滤对象放入查询对象中
                    query.addFilterQuery(filterQuery);
                }
                //说明小于等于最大值, 如果最后的元素也就是最大值为*, 进入不到这里
                if (!"*".equals(split[1])) {
                    //创建过滤查询对象
                    FilterQuery filterQuery = new SimpleFilterQuery();
                    //创建条件对象
                    Criteria filterCriteria = new Criteria("item_price").lessThanEqual(split[1]);
                    //将条件对象放入过滤对象中
                    filterQuery.addCriteria(filterCriteria);
                    //过滤对象放入查询对象中
                    query.addFilterQuery(filterQuery);
                }
            }
        }

        //设置高亮选项
        HighlightOptions highlightOptions = new HighlightOptions();
        //设置哪个域要高亮显示
        highlightOptions.addField("item_title");
        //设置样式 设置高亮前缀   <em style=小米sa
        highlightOptions.setSimplePrefix("<em style=\"color: red\">");
        highlightOptions.setSimplePostfix("</em>");
        query.setHighlightOptions(highlightOptions);


        Criteria criteria = new Criteria("item_keywords").is(keywords);
        query.addCriteria(criteria);

        //3.添加分页
        if (pageNo ==null || pageNo<=0){
            pageNo = 1;
        }
        Integer start = (pageNo-1) *pageSize;
        query.setOffset(start);
        query.setRows(pageSize);

        HighlightPage<Item> items = solrTemplate.queryForHighlightPage(query, Item.class);
        //获取高亮的集合
        List<HighlightEntry<Item>> highlighted = items.getHighlighted();
        List<Item> itemList = new ArrayList<>();
        for (HighlightEntry<Item> itemHighlightEntry : highlighted) {
            //获取每一条商品(是没有高亮)
            Item item = itemHighlightEntry.getEntity();
            List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();
            if (highlights !=null && highlights.size() >0){
                List<String> highlightsTitle = highlights.get(0).getSnipplets();
                if (highlightsTitle != null && highlightsTitle.size() >0){
                    String title = highlightsTitle.get(0);
                    item.setTitle(title);
                }
            }
            itemList.add(item);
        }
        HashMap resMap = new HashMap();
        resMap.put("rows",itemList);
        resMap.put("totalPages",items.getTotalPages());
        resMap.put("total",items.getTotalElements());
        return resMap;
    }
}
