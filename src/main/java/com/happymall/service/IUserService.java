package com.happymall.service;

import com.happymall.common.ServerResponse;
import com.happymall.pojo.User;

/**
 * Created by onegx on 17-7-14.
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> check_valid(String str, String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkQuestion(String username,String question,String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew,User user);

    ServerResponse<User> updateInfomation(User user);

    ServerResponse<User> getInformation(User user);
}
