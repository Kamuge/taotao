package com.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * 描述
 *
 * @author 三国的包子
 * @version 1.0
 * @package com.pinyougou.sellergoods.service *
 * @since 1.0
 */
public interface BrandService {
    List<TbBrand> findAll();

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<TbBrand> findPage(Integer pageNo, Integer pageSize);

    void add(TbBrand brand);


    TbBrand findOne(Long id);

    //更新品牌
    void update(TbBrand brand);

    void delete(Long[] ids);

    //条件查询 分页
    PageInfo<TbBrand> findPage(Integer pageNo, Integer pageSize, TbBrand brand);
}
