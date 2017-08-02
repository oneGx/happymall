package com.happymall.controller.portal;

import com.happymall.common.Const;
import com.happymall.common.ResponseCode;
import com.happymall.common.ServerResponse;
import com.happymall.pojo.User;
import com.happymall.service.ICartService;
import com.happymall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by onegx on 17-7-26.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getCartlist(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iCartService.getCartlist(user.getId());
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse addCart(HttpSession session,Integer productId,Integer count){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iCartService.addCart(user.getId(),productId,count);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session,Integer productId,Integer count){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iCartService.update(user.getId(),productId,count);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ServerResponse delete(HttpSession session,String productIds){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iCartService.deleteProduct(user.getId(),productIds);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse select(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse unSelect(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }

    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse getCartProductCount(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iCartService.getCartProductCount(user.getId());
    }

    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse selectAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse unSelectAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }
}
