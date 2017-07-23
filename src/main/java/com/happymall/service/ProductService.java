package com.happymall.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.happymall.common.ServerResponse;
import com.happymall.pojo.Product;
import com.happymall.vo.ProductDetailVo;

/**
 * Created by onegx on 17-7-19.
 */
public interface ProductService {

    ServerResponse<String> saveOrUpdateProduct(Product product);

    ServerResponse<String> updateProductStatus(int productId, int status);

    ServerResponse<ProductDetailVo> getProductDetail(int productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName,int productId,int pageNum,int pageSize);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, int categoryId, int pageNum, int pageSize, String orderBy);
}
