package com.happymall.service;

import com.happymall.common.ServerResponse;
import com.happymall.pojo.Category;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by onegx on 17-7-16.
 */

@Service("iCategoryService")
public interface ICategoryService {
    ServerResponse<String> addCategory (String categoryName, int categoryId);

    ServerResponse<String> updataCategoryName(String categoryName, int categoryId);

    ServerResponse<List<Category>> getChildrenParallelCategory(int categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(int categoryId);
}
