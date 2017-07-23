package com.happymall.controller.backyard;

import com.google.common.collect.Maps;
import com.google.zxing.common.StringUtils;
import com.happymall.common.Const;
import com.happymall.common.ResponseCode;
import com.happymall.common.ServerResponse;
import com.happymall.dao.ProductMapper;
import com.happymall.pojo.Product;
import com.happymall.pojo.User;
import com.happymall.service.IUserService;
import com.happymall.service.*;
import com.happymall.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by onegx on 17-7-19.
 */

@Controller
@RequestMapping("/manger/product/")
public class ProductMangerController {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ProductService productService;

    @Autowired
    private  iFileServer  fileServer;


    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse save(HttpSession session , Product product){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //进行添加或者更新操作
            return productService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByError("当前用户无权限进行此操作");
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session,int productId,int status){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //进行产品状态的更新
            return productService.updateProductStatus(productId,status);
        }
        return ServerResponse.createByError("当前用户无权限进行此操作");
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session,int productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //进行产品状态的更新
            return productService.getProductDetail(productId);
        }
        return ServerResponse.createByError("当前用户无权限进行此操作");
    }


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getProductList(HttpSession session, @RequestParam(value = "pageNum",defaultValue="1") int pageNum,  @RequestParam(value = "pageSize",defaultValue="10")int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        if(true){
            //返回产品列表
            return productService.getProductList(pageNum,pageSize);
        }
        return ServerResponse.createByError("当前用户无权限进行此操作");
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse serachProduct(HttpSession session, @RequestParam(value = "pageNum",defaultValue="1") int pageNum,  @RequestParam(value = "pageSize",defaultValue="10")int pageSize,String productName,int productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        if(true){
            //返回产品列表
            return productService.searchProduct(productName,productId,pageNum,pageSize);
        }
        return ServerResponse.createByError("当前用户无权限进行此操作");
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        if(true){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileServer.upLoad(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccess(fileMap);
        }
        return ServerResponse.createByError("当前用户无权限进行此操作");
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        if(true){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileServer.upLoad(file,path);
            if(org.apache.commons.lang3.StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }
}
