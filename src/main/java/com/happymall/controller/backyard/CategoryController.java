package com.happymall.controller.backyard;

import com.happymall.common.Const;
import com.happymall.common.ResponseCode;
import com.happymall.common.ServerResponse;
import com.happymall.pojo.User;
import com.happymall.service.ICategoryService;
import com.happymall.service.IUserService;
import com.happymall.service.imp.CategoryServiceImp;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by onegx on 17-7-16.
 */

@Controller
@RequestMapping("/manage/category/")
public class CategoryController {
    @Autowired
    ICategoryService iCategoryService;

    @Autowired
    IUserService iUserService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session, String categoryName,@RequestParam(value = "categoryId",defaultValue = "0") int categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }

        //校验是不是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //确定为管理员
            //进行添加分类操作
            return iCategoryService.addCategory(categoryName,categoryId);
        }

        return ServerResponse.createByError("当前用户无权限进行此操作");
    }

    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse<String>  setCategory(HttpSession session,String categoryName,int categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        //校验是不是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //进行添加商品类别操作
            return iCategoryService.updataCategoryName(categoryName,categoryId);
        }
        return ServerResponse.createByError("当前用户无权限进行此操作");
    }

    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0")int categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        //校验是不是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //查询平级的子节点
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }
        return ServerResponse.createByError("当前用户无权限进行此操作");
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getDeepCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0")int categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        //校验是不是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //查询父节点及其对应的递归子节点的ID
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }
        return ServerResponse.createByError("当前用户无权限进行此操作");
    }
}
