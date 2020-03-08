package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.pojo.item.Item;
import com.fmjava.core.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map paramMap) {
       //1.获取查询条件
        String keywords = String.valueOf(paramMap.get("keywords"));
        Integer pageNo = Integer.parseInt(String.valueOf(paramMap.get("pageNo")));
        Integer pageSize = Integer.parseInt(String.valueOf(paramMap.get("pageSize")));

        //2.创建查询对象
        //SimpleQuery query = new SimpleQuery();
        SimpleHighlightQuery query = new SimpleHighlightQuery();
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
