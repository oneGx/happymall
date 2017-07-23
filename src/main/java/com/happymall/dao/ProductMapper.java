package com.happymall.dao;

import com.happymall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    int checkId(int categoryId);

    List<Product> selectList();

    List<Product> selectByNameAndProductId(@Param("productName") String productName, @Param("productId") int productId);

    List<Product> selectByNameAndCategoryIds (@Param(value = "productName")String productName,@Param(value = "categoryIdList") List<Integer> categoryIdList);
}