package com.cmpay.lemon.monitor.utils;

import com.cmpay.lemon.framework.page.PageInfo;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
public class PageConvertUtils {
    public static <T1, T2> void convert(PageInfo<T1> source, PageInfo<T2> target) {
        target.setPageNum(source.getPageNum());
        target.setPageSize(source.getPageSize());
        target.setPages(source.getPages());
        target.setTotal(source.getTotal());
        target.setHasNextPage(source.isHasNextPage());
        target.setHasPreviousPage(source.isHasPreviousPage());
        target.setIsFirstPage(source.isIsFirstPage());
        target.setIsLastPage(source.isIsLastPage());
    }
}
