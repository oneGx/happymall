package com.happymall.service;

import com.github.pagehelper.PageInfo;
import com.happymall.common.ServerResponse;
import com.happymall.vo.OrderVo;

import java.util.Map;

/**
 * Created by onegx on 17-7-30.
 */
public interface IOrderService {

    ServerResponse pay(Long orderNo,String path,Integer userId);

    ServerResponse aliCallback(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);

    ServerResponse<OrderVo> createOrder(Integer userId, Integer shippingId);

    ServerResponse cancel(Integer userId,Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse<PageInfo> getOrderList(Integer userId,Integer pageNum,Integer pageSize);

    ServerResponse getOrderDetail(Integer userId,Long orderNo);

    ServerResponse manageList(Integer pageNum,Integer pageSize);

    ServerResponse manageDetail(Long orderNo);

    ServerResponse manageSearch(Long orderNo,Integer pageNum,Integer pageSize);

    ServerResponse manageSendGoods(Long orderNo);
}

