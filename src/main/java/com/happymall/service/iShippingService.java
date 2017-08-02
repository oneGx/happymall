package com.happymall.service;

import com.github.pagehelper.PageInfo;
import com.happymall.common.ServerResponse;
import com.happymall.pojo.Shipping;

/**
 * Created by onegx on 17-7-25.
 */
public interface iShippingService {
    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse<String> del(Integer userId,Integer shippingId);

    ServerResponse<String> update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId,Integer pageSize,Integer PageNum);
}
