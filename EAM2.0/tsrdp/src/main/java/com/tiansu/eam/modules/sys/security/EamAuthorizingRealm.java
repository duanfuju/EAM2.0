package com.tiansu.eam.modules.sys.security;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.servlet.ValidateCodeServlet;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.security.UsernamePasswordToken;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.sys.controller.EamLoginController;
import com.tiansu.eam.modules.sys.entity.Menu;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.service.EamButtonService;
import com.tiansu.eam.modules.sys.service.EamSystemService;
import com.tiansu.eam.modules.sys.utils.PassWordHelper;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * 系统安全认证实现类
 * @author wangjl
*/
@Service
//@DependsOn({"userDao","roleDao","menuDao"})
public class EamAuthorizingRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private EamSystemService eamSystemService;






    /**
     * 认证回调函数, 登录时调用
     *  执行时机：
        当调用Subject currentUser = SecurityUtils.getSubject();
        currentUser.login(token);
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        //记住用户?
//        token.setRememberMe(true);
        int activeSessionSize = getEamSystemService().getSessionDao().getActiveSessions(false).size();
        if (logger.isDebugEnabled()) {
            logger.debug("login submit, active session size: {}, username: {}", activeSessionSize, token.getUsername());
        }

        // 校验登录验证码
        if (EamLoginController.isValidateCodeLogin(token.getUsername(), false, false)) {
            Session session = UserUtils.getSession();
            String code = (String) session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
            if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)) {
                throw new AuthenticationException("msg:验证码错误, 请重试.");
            }
        }

        // 校验用户名密码
        User user = UserUtils.getByLoginName(token.getUsername());
        if (user != null) {
           if (Global.NO.equals(user.getStatus())) {
                throw new AuthenticationException("msg:该已帐号禁止登录.");
            }

            //密码校验，可以交给AuthenticatingRealm使用CredentialsMattcher进行匹配，也可以在此判断或自定义实现;
//            shiro自身具体密码认证：
//            AuthenticatingRealm.getAuthenticationInfo（AuthenticationToken token）中的assertCredentialsMatch方法
//            调用credentialsMatcher，可在自定义ream中重设改写改bean加密算法和运算次数
            String salt = new String(token.getPassword());//user.getUserpwd();
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo
                (new PrincipalUser(user, token.isMobileLogin()),user.getUserpwd(),ByteSource.Util.bytes(salt),getName());

            return authenticationInfo;
//            byte[] salt = Encodes.decodeHex(user.getUserpwd().substring(0, 16));
//            return new SimpleAuthenticationInfo(new PrincipalUser(user, token.isMobileLogin()),
//                    user.getUserpwd().substring(16), ByteSource.Util.bytes(salt), getName());
        } else {
            return null;
        }
    }
    @Override
    protected void assertCredentialsMatch(AuthenticationToken authcToken,
                                          AuthenticationInfo info) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        // 若单点登录，则使用单点登录授权方法。
        if (token.toString().equals(token.getParams())){
                return;
        }
        super.assertCredentialsMatch(token, info);
    }
    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     * 执行时机有3个：
        1、subject.hasRole(“admin”) 或 subject.isPermitted(“admin”)：自己去调用这个是否有什么角色或者是否有什么权限的时候；
        2、@RequiresRoles("admin") ：在方法上加注解的时候；
        3、[@shiro.hasPermission name = "admin"][/@shiro.hasPermission]：在页面上加shiro标签的时候，即进这个页面的时候扫描到有这个标签的时候。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        PrincipalUser principal = (PrincipalUser) getAvailablePrincipal(principals);
        //同账号多登录处理
         checkAccountLogin(principal);

        User user = UserUtils.getByLoginName(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            List<Menu> list = UserUtils.getMenuList();
            for (Menu menu : list) {
//                if (StringUtils.isNotBlank(menu.getPermission())) {
//                    // 添加基于Permission的权限信息
//                    for (String permission : StringUtils.split(menu.getPermission(), ",")) {
//                        info.addStringPermission(permission);
//                    }
//                }
            }
            // 添加用户权限
            info.addStringPermission("user");
            // 添加用户角色信息
//            for (Role role : user.getRoleList()) {
//                info.addRole(role.getEnname());
//            }
            // 更新登录IP和时间
            getEamSystemService().updateUserLoginInfo();
            // 记录登录日志
//            LogUtils.saveLog(Servlets.getRequest(), "系统登录");
            return info;
        } else {
            return null;
        }
    }

    /**
     * 判断session是否存在，不再存在，返回false
     * @param principal
     * @return
     */
    public boolean checkAccountLogin(PrincipalUser principal) {
        boolean success = true;
        // 获取当前已登录的用户
        if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))) {
            Collection<Session> sessions = getEamSystemService().getSessionDao().getActiveSessions(true, principal, UserUtils.getSession());
            if (sessions.size() > 0) {
                success = false;

                // 如果是登录进来的，则踢出已在线用户
                if (UserUtils.getSubject().isAuthenticated()) {
                    for (Session session : sessions) {
                        getEamSystemService().getSessionDao().delete(session);
                    }
                }
                // 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
                else {
                    UserUtils.getSubject().logout();
                    throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
                }
            }
        }
        return success;
    }


    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        authorizationValidate(permission);
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        authorizationValidate(permission);
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermittedAll(permissions, info);
    }

    /**
     * 授权验证方法
     *
     * @param permission
     */
    private void authorizationValidate(Permission permission) {
        // 模块授权预留接口
    }

    /**
     * 设定密码校验的Hash算法与迭代次数
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(EAMConsts.HASH_ALGORITHM);
        matcher.setHashIterations(EAMConsts.HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }


    /**
     * 清空所有关联认证
     *
     * @Deprecated 不需要清空，授权缓存保存到session中
     */
    @Deprecated
    public void clearAllCachedAuthorizationInfo() {
//		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
//		if (cache != null) {
//			for (Object key : cache.keys()) {
//				cache.remove(key);
//			}
//		}
    }

    /**
     * 获取系统业务对象
     */
    public EamSystemService getEamSystemService() {
        if (eamSystemService == null) {
            eamSystemService = SpringContextHolder.getBean(EamSystemService.class);
        }
        return eamSystemService;
    }


}