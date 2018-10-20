package cn.dblearn.blog.manage.sys.service.impl;

import cn.dblearn.blog.common.pojo.RedisBaseKeyConstants;
import cn.dblearn.blog.common.pojo.Result;
import cn.dblearn.blog.common.util.RedisUtils;
import cn.dblearn.blog.manage.sys.oauth2.TokenGenerator;
import cn.dblearn.blog.manage.sys.pojo.entity.SysUserToken;
import cn.dblearn.blog.manage.sys.service.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * SysUserTokenServiceImpl
 *
 * @author bobbi
 * @date 2018/10/20 15:18
 * @email 571002217@qq.com
 * @description
 */
@Service
public class SysUserTokenServiceImpl implements SysUserTokenService {

    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 生成Token
     *
     * @param userId
     * @return
     */
    @Override
    public Result createToken(Integer userId) {
        // 生成一个token
        String token = TokenGenerator.generateValue();

        String tokenKey=RedisBaseKeyConstants.MANAGE_SYS_USER_TOKEN+token;
        String userIdKey=RedisBaseKeyConstants.MANAGE_SYS_USER_TOKEN+userId;

        //判断是否生成过token
        String tokenInRedis=redisUtils.get(userIdKey);
        if(!StringUtils.isEmpty(tokenInRedis)){
            // 将原来的token删除
            redisUtils.delete(RedisBaseKeyConstants.MANAGE_SYS_USER_TOKEN+tokenInRedis);
        }
        // 将token存进redis
        redisUtils.set(tokenKey,userId,EXPIRE);
        redisUtils.set(userIdKey,token,EXPIRE);

        return new Result().put("token",token).put("expire",EXPIRE);
    }

    /**
     * 查询token
     *
     * @param token
     * @return
     */
    @Override
    public SysUserToken queryByToken(String token) {
        String userId=redisUtils.get(token);
        if(StringUtils.isEmpty(userId)){
            return null;
        }
        SysUserToken sysUserToken=new SysUserToken();
        sysUserToken.setToken(token);
        sysUserToken.setUserId(Integer.parseInt(userId));
        return sysUserToken;
    }


}