package com.happymall.service.imp;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.happymall.common.ServerResponse;
import com.happymall.dao.CategoryMapper;
import com.happymall.pojo.Category;
import com.happymall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by onegx on 17-7-16.
 */
@Service("iCategoryService")
public class CategoryServiceImp implements ICategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse<String> addCategory(String categoryName, int categoryId) {
        if(categoryId == 0 || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByError("商品参数有错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        category.setStatus(true);

        int count = categoryMapper.insert(category);
        if(count > 0){
            return ServerResponse.createBySuccess("添加商品分类成功");
        }

        return ServerResponse.createByError("添加商品失败");

    }

    @Override
    public ServerResponse<String> updataCategoryName(String categoryName, int categoryId) {
        if(categoryId == 0 || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByError("商品参数有错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);

        int count = categoryMapper.updateByPrimaryKeySelective(category);

        if(count > 0 ){
            return ServerResponse.createBySuccessMsg("更新成功");
        }

        return ServerResponse.createByError("更新失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(int categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId1(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            return ServerResponse.createByError("未找到该品类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(int categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

        List<Integer> list = Lists.newArrayList();
        if(categoryId!=0){
            for(Category categoryItem : categorySet){
                list.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(list);
    }

    //递归算法,算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet, int categoryId){
        //在当前层级下,先添加父节点
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        //递归查询子节点
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId1(categoryId);
        for(Category categoryItem : categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
