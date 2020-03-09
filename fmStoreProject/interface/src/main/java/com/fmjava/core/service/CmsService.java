package com.fmjava.core.service;

public interface CmsService {

    /**
     * 根据商品ID生成对应的静态页面
     * @param goodsId  商品ID
     * @throws Exception
     */
    public void createStaticPage(Long goodsId) throws Exception;

}
