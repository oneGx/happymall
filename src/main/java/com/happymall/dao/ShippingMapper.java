package com.happymall.dao;

import com.happymall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteShippingByUserId(@Param("userId") Integer userId, @Param("shippingId")Integer shippingId );

    int updateByShipping(Shipping shipping);

    Shipping selectByShippingIdUserId(@Param("userId") Integer userId, @Param("shippingId")Integer shippingId);

    List<Shipping> selectByUserId( Integer userId);

    Shipping selectByShippingByPhone(@Param("userId")Integer userId,@Param("receiverMobile")String receiverMobile);
}