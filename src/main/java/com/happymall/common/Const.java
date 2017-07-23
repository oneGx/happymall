package com.happymall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by onegx on 17-7-14.
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role{
        int Role_Customer = 0; //表示普通用户
        int Role_Admin = 1;//表示管理员
    }

    public interface productListOrderBy{
        Set<String> PRICE_DESC_ASC = Sets.newHashSet("price_desc","price_asc");
    }
}
