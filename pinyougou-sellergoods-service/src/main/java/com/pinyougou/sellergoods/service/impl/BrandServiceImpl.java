package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * 描述
 *
 * @author 三国的包子
 * @version 1.0
 * @package com.pinyougou.sellergoods.service.impl *
 * @since 1.0
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public PageInfo<TbBrand> findPage(Integer pageNo, Integer pageSize) {
        //1.开始分页 紧跟着的第一个查询才会被分页
        PageHelper.startPage(pageNo, pageSize);
        //2.执行查询
        List<TbBrand> brands = brandMapper.selectAll();
        //3.返回一个pageInfo 对象 构建对象
        PageInfo<TbBrand> pageInfo = new PageInfo<TbBrand>(brands);

        //4.先通过jSON序列化成STRING
        String str = JSON.toJSONString(pageInfo);
        //5.字符串转换成对象
        PageInfo pageInfo1 = JSON.parseObject(str, PageInfo.class);

        return pageInfo1;
    }

    @Override
    public void add(TbBrand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(TbBrand brand) {
        //根据主键来更新  参数 一定要有 id的值  其他的属性 就是更新后的数据
        brandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public void delete(Long[] ids) {
        //  delete from tb_brand where id in (1,2,3)
        /*for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }*/
        Example exmaple = new Example(TbBrand.class);
        //创建条件
        Example.Criteria criteria = exmaple.createCriteria();
        //第一个参数 指定的是pojo的属性名
        //第二个参数 指定的对应的值
        criteria.andIn("id", Arrays.asList(ids));
        brandMapper.deleteByExample(exmaple);// deleteByExample  delete 就相当于delete from tb_brand  exmaple :就是相当于where
    }

    @Override
    public PageInfo<TbBrand> findPage(Integer pageNo, Integer pageSize, TbBrand brand) {
        //1.获取条件对象
        Example example = new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();

        //2.判断组装条件
        if (brand != null) {
            //ctr+ q
            if (StringUtils.isNotBlank(brand.getName())) {
                criteria.andLike("name", "%" + brand.getName() + "%");// name like "%联想%"
            }

            if (StringUtils.isNotBlank(brand.getFirstChar())) {
                criteria.andLike("firstChar", "%" + brand.getFirstChar() + "%");// firstChar="%l%"
            }
        }
        //3.开始分页
        PageHelper.startPage(pageNo, pageSize);
        //4.执行查询
        List<TbBrand> brands = brandMapper.selectByExample(example);
        //5.返回pageinfo

        PageInfo<TbBrand> pageInfo = new PageInfo<TbBrand>(brands);

        //4.先通过jSON序列化成STRING
        String str = JSON.toJSONString(pageInfo);
        //5.字符串转换成对象
        PageInfo pageInfo1 = JSON.parseObject(str, PageInfo.class);

        return pageInfo1;
    }

    /**
     * 删除品牌
     *
     * @param ids
     * @return
     */
    @Override
    public Result delBrandService(Long[] ids) {
        //判断是否有数据
        if (ids.length == 0) {
            return new Result(false, "请选择要删除的数据");
        }
        Example example = new Example(TbBrand.class);
        example.createCriteria().andIn("id", Arrays.asList(ids));
        int i = brandMapper.deleteByExample(example);
        if (i==0) {
            return new Result(false,"删除失败，请稍后再试");
        }

        return new Result(true,"删除成功");
    }
    //保存品牌
    @Override
    public Result saveBrand(String param) {
        JSONObject jsonObject = JSON.parseObject(param);
        Long status = jsonObject.getLong("status");
        if (StringUtils.isEmpty(status.toString())) {
            return new Result(false,"请选择是增加还是修改品牌");
        }
        TbBrand tbBrand = jsonObject.getObject("TbBrand", TbBrand.class);
        if (StringUtils.isEmpty(tbBrand.getFirstChar())) {
            return new Result(false,"品牌首字母不能为空");
        }
        if (StringUtils.isEmpty(tbBrand.getName())) {
            return new Result(false,"品牌不能为空");
        }
        if (0==status) {
            //增加品牌
            int i = brandMapper.insertSelective(tbBrand);
            if (i>0) {
                return new Result(true,"增加成功");
            }else{
                return new Result(false,"增加失败");
            }
        }
        int i = brandMapper.updateByPrimaryKey(tbBrand);
        if (i!=0) {
            return new Result(true,"修改成功");
        }
        return new Result(false,"修改失败");

    }
}
