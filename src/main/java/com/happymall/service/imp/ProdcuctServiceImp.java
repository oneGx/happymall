package com.happymall.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.happymall.common.Const;
import com.happymall.common.ResponseCode;
import com.happymall.common.ServerResponse;
import com.happymall.dao.CategoryMapper;
import com.happymall.dao.ProductMapper;
import com.happymall.pojo.Category;
import com.happymall.pojo.Product;
import com.happymall.service.ProductService;
import com.happymall.util.DateTimeUtil;
import com.happymall.util.PropertiesUtil;
import com.happymall.vo.ProductDetailVo;
import com.happymall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by onegx on 17-7-19.
 */
@Service("productService")
public class ProdcuctServiceImp implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if(product!=null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subImages = product.getSubImages().split(",");
                if(subImages.length > 0){
                    product.setMainImage(subImages[0]);
                }
            }
            int checkId = productMapper.checkId(product.getCategoryId());
            if(checkId > 0){
                int result = productMapper.updateByPrimaryKeySelective(product);
                if(result > 0){
                    return ServerResponse.createBySuccessMsg("更新产品成功");
                }else{
                    return ServerResponse.createByError("更新产品失败");
                }
            }else{
                int result = productMapper.insert(product);
                if(result > 0){
                    return ServerResponse.createBySuccessMsg("添加产品成功");
                }else{
                    return ServerResponse.createByError("添加产品失败");
                }
            }
        }
        return ServerResponse.createByError("商品参数不正确,请重新输入");
    }

    @Override
    public ServerResponse<String> updateProductStatus(int productId, int status) {
        if(productId<0||status<0){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int result = productMapper.updateByPrimaryKeySelective(product);
        if(result > 0 ){
            return  ServerResponse.createBySuccessMsg("修改产品状态成功");
        }
        return ServerResponse.createByError("修改产品销售状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(int productId) {
        if(productId < 0){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByError("商品不存在或者已经下架");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setName(product.getName());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setSubtitle(productDetailVo.getSubtitle());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum,int pageSize){
        //startPage--start
        //填充自己的sql查询逻辑
        //pageHelper-收尾
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVos = Lists.newArrayList();
        for(Product productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVos.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVos);
        return ServerResponse.createBySuccess(pageResult);

    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, int productId, int pageNum, int pageSize) {
        /*PageHelper.startPage(pageNum,pageSize);*/
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVos = Lists.newArrayList();
        for(Product productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVos.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVos);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, int categoryId, int pageNum, int pageSize, String orderBy) {
        if(StringUtils.isBlank(keyword)&&categoryId < 0){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误,请重新输入");
        }
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId >=0){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if((StringUtils.isBlank(keyword)&& category ==null)){
                //表示该分类并且关键字为空,即返回一个空的结果集
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = categoryMapper.selectCategoryChildrenByParentId((category==null)?-1:category.getId());
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum,pageSize);
        //排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.productListOrderBy.PRICE_DESC_ASC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+orderByArray[1]);
            }
        }
        List<Product> productList =productMapper.selectByNameAndCategoryIds((StringUtils.isBlank(keyword))?null:keyword,(categoryIdList.size() ==0)?null:categoryIdList);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product product : productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }


}
