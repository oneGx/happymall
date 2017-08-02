package com.happymall.controller.portal;

import com.happymall.common.Const;
import com.happymall.common.ResponseCode;
import com.happymall.common.ServerResponse;
import com.happymall.dao.ShippingMapper;
import com.happymall.pojo.Shipping;
import com.happymall.pojo.User;
import com.happymall.service.IUserService;
import com.happymall.service.iShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by onegx on 17-7-25.
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private iShippingService iShippingService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iShippingService.add(user.getId(),shipping);
    }


    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse del(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iShippingService.del(user.getId(),shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iShippingService.update(user.getId(),shipping);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse select(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        if(shippingId==null){
            return ServerResponse.createByError("地址ID为空,请重新输入地址ID");
        }
        return iShippingService.select(user.getId(),shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                               HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"需要先登录");
        }
        return iShippingService.list(user.getId(),pageSize,pageNum);
    }
}
