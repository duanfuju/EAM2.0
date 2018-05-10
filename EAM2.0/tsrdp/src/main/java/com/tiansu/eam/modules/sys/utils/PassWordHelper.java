package com.tiansu.eam.modules.sys.utils;

import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.sys.entity.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Created by wangjl on 2017/7/21.
 */
public class PassWordHelper
{
    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private static String algorihtmName = EAMConsts.HASH_ALGORITHM;
    private static final int hashIterations = EAMConsts.HASH_INTERATIONS;
    public static String encryptPassword(User user){
        String salt = randomNumberGenerator.nextBytes().toHex();
        String password = "123456";
//        String credentialsSalt = user.getLoginname()+salt;
        String credentialsSalt = password;
        String newPassword = new SimpleHash(algorihtmName,password, ByteSource.Util.bytes(credentialsSalt),hashIterations).toHex();
       return newPassword;
    }

    public static void main(String args[]){
        User user = new User();
        user.setLoginname("tiger"); //fd0ba678c152aff0b0f013d1e2f71182
        user.setLoginname("zhangww"); //d56f5734724163e97c54c3f55e7a5237
        user.setLoginname("emagine"); //91298a6a8887ca41cf28e87650a3ab42
        user.setLoginname("eamdevelop");//93cb7d0aec87b8276f815ace793c527e
        user.setLoginname("eamtest");//e1f7acd8179dff4e1b2037de28009c31 -- 3980e4044675f6339248ee0c735c7d72
        String ps = encryptPassword(user);
        System.out.println(ps);
    }
}
