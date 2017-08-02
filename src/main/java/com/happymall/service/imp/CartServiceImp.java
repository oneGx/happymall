package com.happymall.service.imp;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.happymall.common.Const;
import com.happymall.common.ResponseCode;
import com.happymall.common.ServerResponse;
import com.happymall.dao.CartMapper;
import com.happymall.dao.ProductMapper;
import com.happymall.pojo.Cart;
import com.happymall.pojo.Product;
import com.happymall.service.ICartService;
import com.happymall.util.BigDecimalUtil;
import com.happymall.util.PropertiesUtil;
import com.happymall.vo.CartProductVo;
import com.happymall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by onegx on 17-7-26.
 */
@Service("iCartService")
public class CartServiceImp implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse<CartVo> getCartlist(Integer userId) {
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误,请重新输入");
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart == null){
            //说明购物车中并没有该商品,现在进行添加
            Cart cartItem = new Cart();
            cartItem.setProductId(productId);
            cartItem.setQuantity(count);
            cartItem.setUserId(userId);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartMapper.insert(cartItem);
        }else{
            //说明购物车中有该商品,现在进行添加操作
            int countNew = count + cart.getQuantity();
            cart.setQuantity(countNew);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.getCartlist(userId);

    }

    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误,请重新输入");
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart!=null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.getCartlist(userId);
    }

    @Override
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productIdList)){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId,productIdList);
        return this.getCartlist(userId);
    }

    @Override
    public ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer check) {
        cartMapper.checkedOrUncheckedProduct(userId,productId,check);
        return this.getCartlist(userId);
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }

    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart>  cartList = cartMapper.selectByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if(CollectionUtils.isNotEmpty(cartList)){
            for(Cart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setUserId(userId);
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product!=null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    //判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                        //库存充足
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if(cartItem.getChecked() == Const.Cart.CHECKED){
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
            cartVo.setCartTotalPrice(cartTotalPrice);
            cartVo.setAllChecked(this.getAllstatusByuserId(userId));
            cartVo.setCartProductVoList(cartProductVoList);
            cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        }
        return cartVo;
    }

    private boolean getAllstatusByuserId(Integer userId){
        if(userId == null){
            return false;
        }
        //表示全选中的情况下 返回true
        return cartMapper.selectCartProductCheckedStatusByUserId(userId)==0;
    }


}
