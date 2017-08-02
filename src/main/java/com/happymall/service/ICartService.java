package com.happymall.service;

import com.happymall.common.Const;
import com.happymall.common.ServerResponse;
import com.happymall.vo.CartVo;

/**
 * Created by onegx on 17-7-26.
 */
public interface ICartService {
    ServerResponse<CartVo> getCartlist(Integer userId);

    ServerResponse<CartVo> addCart(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> deleteProduct(Integer userId,String productIds);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer check);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}
