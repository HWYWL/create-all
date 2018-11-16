package com.generate.core;

import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Service 层基础接口,其他Service 接口 需要继承该接口
 * @author YI
 * @date 2018-6-27 16:11:01
 */
public interface Service<T> {
    /**
     * 保存
     * @param model
     */
    void save(T model);

    /**
     * 批量保存
     * @param models
     */
    void save(List<T> models);

    /**
     * 通过主键删除
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 通过质检批量刪除 eg：ids -> “1,2,3,4”
     * @param ids
     */
    void deleteByIds(String ids);

    /**
     * 根据条件删除
     * @param criteria Example
     */
    void deleteByExample(Example.Criteria criteria);

    /**
     * 更新
     * @param model
     */
    void update(T model);

    /**
     * 根据条件更新
     * @param model JavaBean对象
     * @param criteria Example
     */
    void updateByExampleSelective(T model, Example.Criteria criteria);

    /**
     * 通过ID查找
     * @param id
     * @return
     */
    T findById(Integer id);

    /**
     * 通过某个成员属性查找,value需符合unique约束
     * @param property
     * @param value
     * @return
     * @throws TooManyResultsException
     */
    T findBy(String property, Object value) throws TooManyResultsException;

    /**
     * 通过多个ID查找//eg：ids -> “1,2,3,4”
     * @param ids
     * @return
     */
    List<T> findByIds(String ids);

    /**
     * 根据条件查找
     * @param criteria
     * @return
     */
    List<T> findByCondition(Example.Criteria criteria);

    /**
     * 自定义查询
     * @param criteria
     * @return
     */
    List<T> findByExample(Example.Criteria criteria);

    /**
     * 获取所有数据
     * @return
     */
    List<T> findAll();
}
