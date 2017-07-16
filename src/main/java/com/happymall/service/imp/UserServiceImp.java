package com.happymall.service.imp;

import com.github.pagehelper.StringUtil;
import com.happymall.common.TokenCache;
import com.happymall.common.Const;
import com.happymall.common.ServerResponse;
import com.happymall.dao.UserMapper;
import com.happymall.pojo.User;
import com.happymall.service.IUserService;
import com.happymall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by onegx on 17-7-14.
 */
@Service("iUerService")
public class UserServiceImp implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse<User> login(String username, String password) {
        int result = userMapper.checkUsername(username);
        if(result == 0){
            return ServerResponse.createByError("该用户不存在");
        }
        //MD5加密
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.createByError("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }



    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse check_valid_Response = this.check_valid(user.getUsername(),Const.USERNAME);
        if(!check_valid_Response.isSuccess()){
            return check_valid_Response;
        }
        check_valid_Response = this.check_valid(user.getEmail(),Const.EMAIL);
        if(!check_valid_Response.isSuccess()){
            return check_valid_Response;
        }

        user.setRole(Const.Role.Role_Customer);

        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int result = userMapper.insert(user);
        if(result == 0){
            return ServerResponse.createByError("注册失败");
        }
        return ServerResponse.createBySuccessMsg("注册成功");
    }

    @Override
    public ServerResponse<String> check_valid(String str, String type) {
        if(StringUtils.isNotBlank(type)){
            if(Const.USERNAME.equals(type)){
                int result = userMapper.checkUsername(str);
                if(result > 0){
                    return ServerResponse.createByError("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int result = userMapper.checkEmail(str);
                if(result > 0){
                    return ServerResponse.createByError("该邮箱已存在");
                }
            }
        }else{
            return ServerResponse.createByError("参数错误");
        }
        return ServerResponse.createBySuccess("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse check_valid = this.check_valid(username,Const.USERNAME);
        if(check_valid.isSuccess()){
            return ServerResponse.createByError("该用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByError("该用户未设置找回密码问题");
    }


    @Override
    public ServerResponse<String> checkQuestion(String username, String question, String answer) {
        int result = userMapper.checkQuestion(username,question,answer);
        if(result > 0){
            //说明该用户已经正确回答问题
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByError("问题答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token) {
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByError("toek参数错误,请重新传递");
        }
        ServerResponse vaildResponse = this.check_valid(username,Const.USERNAME);
        if(vaildResponse.isSuccess()){
            return ServerResponse.createByError("用户不存在");
        }
        String token1 = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token1)){
            return  ServerResponse.createByError("token过期");
        }
        if(StringUtils.equals(token,token1)){
            String MD5password = MD5Util.MD5EncodeUtf8(passwordNew);
            int count = userMapper.updatePasswordByUsername(username,MD5password);
            if(count > 0){
                return ServerResponse.createBySuccessMsg("密码重置成功");
            }else{
                return ServerResponse.createByError("token错误,请重新获取重置密码的token");
            }
        }
        return ServerResponse.createByError("密码重置失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew,User user) {
        int count = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(count == 0){
            return ServerResponse.createByError("原密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMsg("密码修改成功");
        }
        return ServerResponse.createByError("密码修改失败");
    }

    @Override
    public ServerResponse<User> updateInfomation(User user) {
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的
        int count = userMapper.checkEmailByUserName(user.getId(),user.getEmail());
        if(count > 0){
            return ServerResponse.createByError("Email已经存在,请使用其他邮箱");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByError("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(User user) {
        User currentUser = userMapper.selectByPrimaryKey(user.getId());
        if(currentUser == null){
            return ServerResponse.createByError("该用户不存在");
        }
        currentUser.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(currentUser);
    }

}
