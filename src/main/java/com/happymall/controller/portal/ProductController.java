package com.happymall.controller.portal;

import com.happymall.common.ResponseCode;
import com.happymall.common.ServerResponse;
import com.happymall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by onegx on 17-7-22.
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "keyword",required = false) String keyword,
                               @RequestParam(value = "categoryId",defaultValue = "-1",required = false) int categoryId,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                               @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                               @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return productService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(int productId){
        return productService.getProductDetail(productId);
    }
}
