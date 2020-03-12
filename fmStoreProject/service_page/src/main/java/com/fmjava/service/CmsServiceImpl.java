package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.good.GoodsDao;
import com.fmjava.core.dao.good.GoodsDescDao;
import com.fmjava.core.dao.item.ItemCatDao;
import com.fmjava.core.dao.item.ItemDao;
import com.fmjava.core.pojo.good.Goods;
import com.fmjava.core.pojo.good.GoodsDesc;
import com.fmjava.core.pojo.item.Item;
import com.fmjava.core.pojo.item.ItemCat;
import com.fmjava.core.pojo.item.ItemQuery;
import com.fmjava.core.service.CmsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class CmsServiceImpl implements CmsService, ServletContextAware {

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsDescDao descDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemCatDao catDao;

    private ServletContext servletContext;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void createStaticPage(Long goodsId) throws Exception {
       //获取商品的数据
        Map<String, Object> goodsData = this.findGoodsData(goodsId);
        //获取配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //获取模板对象
        Template template = configuration.getTemplate("item.ftl");
        //设置路径
        String fileName = goodsId+".html";
        String realPath = this.servletContext.getRealPath(fileName);
        System.out.println(realPath);
        //设置输出内容
        Writer out = new OutputStreamWriter(new FileOutputStream(new File(realPath)),"utf-8");
        template.process(goodsData,out);
        out.close();
    }

    /**
     * 根据goodsid查询出商品的数据
     */
    private Map<String,Object> findGoodsData(Long goodsId ){
        Map<String, Object> resultMap = new HashMap<>();
        //1. 获取商品数据
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        //2. 获取商品详情数据
        GoodsDesc goodsDesc = descDao.selectByPrimaryKey(goodsId);
        //3. 获取库存集合数据
        ItemQuery query = new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<Item> itemList = itemDao.selectByExample(query);

        //4. 获取商品对应的分类数据
        if (goods != null) {
            ItemCat itemCat1 = catDao.selectByPrimaryKey(goods.getCategory1Id());
            ItemCat itemCat2 = catDao.selectByPrimaryKey(goods.getCategory2Id());
            ItemCat itemCat3 = catDao.selectByPrimaryKey(goods.getCategory3Id());
            resultMap.put("itemCat1", itemCat1.getName());
            resultMap.put("itemCat2", itemCat2.getName());
            resultMap.put("itemCat3", itemCat3.getName());
        }
        //5. 将商品所有数据封装成Map返回
        resultMap.put("goods", goods);
        resultMap.put("goodsDesc", goodsDesc);
        resultMap.put("itemList", itemList);
        return resultMap;
    }



}
