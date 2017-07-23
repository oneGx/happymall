package com.happymall.dao;

import com.happymall.pojo.Category;
import com.happymall.pojo.Product;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Integer> selectCategoryChildrenByParentId(int parentId);

    List<Category> selectCategoryChildrenByParentId1(int parentId);
}