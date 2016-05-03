package com.joint.web.shiro.realm;


import com.fz.us.base.util.LogUtil;
import com.google.common.collect.Sets;
import com.joint.base.entity.Users;
import com.joint.base.entity.system.Admin;
import com.joint.base.service.*;
import com.joint.base.util.StringUtils;
import com.joint.core.service.AdminInfoService;
import com.joint.web.shiro.codec.HmacSHA256Utils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Created by hpj on 2014/9/18.
 */
public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private UsersService usersService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;
    @Resource
    private AdminService adminService;
    @Resource
    private AdminInfoService adminInfoService;
    @Resource
    private CompanyService companyService;

    private static final Logger log = LoggerFactory.getLogger(MyShiroRealm.class);

    /**
     * 对Token的支持类型
     * */
    @Override
    public boolean supports(AuthenticationToken token) {
        return (token instanceof  StatelessFormToken) || (token instanceof StatelessToken) || (token instanceof AuthenticationToken);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        Users users = usersService.getUsersByMobile(username);
        Set<String> roleSet = Sets.newHashSet();
        roleSet= roleService.findPnameByUser(users);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if(StringUtils.contains(users.getAdminType(),"3")){
            roleSet.add("admin");
        }
        if(StringUtils.contains(users.getAdminType(),"2")){
            roleSet.add("wechat");
        }


        if(companyService.get("adminId",users.getAdminId())!=null)roleSet.add("company");


        authorizationInfo.setRoles(roleSet);

        return authorizationInfo;
    }


    /**
     * 用户登录
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //无状态的审核验证过程
        System.out.println("进入no statelessToken");
        if(authenticationToken instanceof StatelessToken){
            return doGetStatelessTokenInfo(authenticationToken);
        }

//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//        String username = token.getUsername();
        //判断是否是企业帐号，企业帐号都可以登录 -->判断帐号是否可用-->帐号是否是后台监控人员
        String username = (String)authenticationToken.getPrincipal();
        Admin admin = adminService.getByMobile(username);
        Users users = usersService.getUsersByMobile(username);

        if(admin == null) {
            throw new UnknownAccountException();//没找到帐号
        }
        LogUtil.info("admin : "+admin.getUsermobile());
//        String codeStatus = adminInfoService.getStatusByUsers(users);
//        if(!StringUtils.equals(codeStatus, EstEnum.AdminStatusEnum.using.value())){
//            //如果不是企业帐号&&不是监控帐号抛异常
//            if(!StringUtils.contains(users.getAdminType(),"3") && companyService.get("adminId",admin.getId())==null){
//                throw new UserExpiredException();
//            }
//        }
        //我注释的
//        if(!StringUtils.contains(users.getAdminType(),"2")){
//            throw new UnknownAccountException();
//        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                username,
                admin.getPassword(),
                ByteSource.Util.bytes(username+admin.getSalt()),
                getName());
        return info;

    }

    protected AuthenticationInfo doGetStatelessTokenInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("进入statelessToken");
        StatelessToken statelessToken = (StatelessToken) token;
        String username = statelessToken.getUsername();
        Users users = usersService.getUsersByMobile(username);
        LogUtil.info("username : "+username);
        String key = getKey(username);              //根据用户名获取盐值
        if(key == null){
            throw new UnknownAccountException();//没找到帐号
        }
//        String codeStatus = adminInfoService.getStatusByUsers(users);
//        if(!StringUtils.equals(codeStatus, EstEnum.AdminStatusEnum.using.value())){
//            //如果不是企业帐号&&不是监控帐号抛异常
//            if(!StringUtils.contains(users.getAdminType(),"3") && companyService.get("adminId",users.getAdminId())==null){
//                throw new UserExpiredException();
//            }
//        }

        if(!StringUtils.contains(users.getAdminType(),"1")){
            throw new UnknownAccountException();
        }

        //在服务器端生成客户端参数消息摘要
        String serverDigest = HmacSHA256Utils.digest(key, statelessToken.getParams());
        //System.out.println(statelessToken.getClientDigest());
        //System.out.println(serverDigest);
        LogUtil.info("{clientDigest}"+statelessToken.getClientDigest()+"{serverDigest}"+serverDigest);
        //然后进行客户端消息摘要和服务器端消息摘要的匹配
        return new SimpleAuthenticationInfo(
                username,
                serverDigest,
                getName());
    }

    //得到密钥-salt
    private String getKey(String username) {
        Admin admin = adminService.getByMobile(username);
        LogUtil.info("{admin}" + " " + admin.getUsermobile());
        if(admin == null) {
            return null;
        }
        return admin.getSalt();
    }
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }







}
