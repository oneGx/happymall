package com.happymall.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.happymall.common.ResponseCode;
import com.happymall.common.ServerResponse;
import com.happymall.dao.ShippingMapper;
import com.happymall.pojo.Shipping;
import com.happymall.service.iShippingService;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;

import java.util.List;
import java.util.Map;

/**
 * Created by onegx on 17-7-25.
 */
@Service("iShippingService")
public class ShippingService implements iShippingService {

    @Autowired
    private ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int count = shippingMapper.insert(shipping);
        Shipping shipping1 = shippingMapper.selectByShippingByPhone(userId,shipping.getReceiverMobile());
        if(count > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping1.getId());
            return ServerResponse.createBySuccess("新建地址成功",result);
        }
        return ServerResponse.createByError("新建地址失败");
    }

    @Override
    public ServerResponse<String> del(Integer userId,Integer shippingId) {
        if(shippingId < 0){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误,请重新输入");
        }
        int count = shippingMapper.deleteShippingByUserId(userId,shippingId);
        if(count > 0){
            return ServerResponse.createBySuccessMsg("删除地址成功");
        }
        return ServerResponse.createByError("删除地址失败");
    }

    @Override
    public ServerResponse<String> update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int count = shippingMapper.updateByShipping(shipping);
        if(count > 0){
            return ServerResponse.createBySuccessMsg("更新地址成功");
        }
        return ServerResponse.createByError("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        if(shippingId < 0){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误,请重新输入");
        }
        Shipping  shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping == null){
            return ServerResponse.createByError("无法查询相关地址");
        }
        return ServerResponse.createBySuccess(shipping);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> list = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(list);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
