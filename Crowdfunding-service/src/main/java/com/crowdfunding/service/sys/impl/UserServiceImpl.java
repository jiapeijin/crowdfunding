package com.crowdfunding.service.sys.impl;

import com.crowdfunding.dao.sys.UserDao;
import com.crowdfunding.entity.sys.UserInfo;
import com.crowdfunding.framework.Base.BaseService;
import com.crowdfunding.framework.Page.PageBean;
import com.crowdfunding.framework.Page.PaginationContext;
import com.crowdfunding.service.sys.UserService;
import com.github.pagehelper.PageHelper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lucy on 2017/11/14.
 */
@Service
@Transactional(readOnly = false)
public class UserServiceImpl extends BaseService implements UserService{

    @Autowired
    private UserDao userDao;
    /**
     * @description 用户列表
     * @methodName findUserList
     * @param
     * @return java.util.List<com.crowdfunding.entity.sys.UserInfo>
     * @author lucy [18616735761@163.com]
     * @time 2017/11/14 13:57
     */
    @Override
    public PageBean<UserInfo> findUserList() {
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(PaginationContext.getPageNum(),PaginationContext.getPageSize());
        List<UserInfo> list=userDao.findUserList();
        return new PageBean<UserInfo>(list);

    }
    /**
     * @description 添加用户
     * @methodName addUser
     * @param userInfo userInfo
     * @return int
     * @author lucy [18616735761@163.com]
     * @time 2017/11/14 13:58
     */
    @Override
    public Map<String,Object> addUser(UserInfo userInfo) {
        Map<String,Object> map = new HashMap<String,Object>();
        //判断是否存在相同的用户名
        UserInfo user=userDao.findUserByName(userInfo.getLoginName());
        if(user != null ){
            //保存失败 ，存在相同用户
            map.put("false",false);
        }else{
            preInsert(userInfo);
            //未登录前注册  将创建用户和修改用户设置为注册名
            userInfo.setCreateUser(userInfo.getLoginName());
            userInfo.setUpdateUser(userInfo.getLoginName());
            int count=userDao.addUser(userInfo);
            if(count >0){
                //保存成功
                map.put("success",true);
            }else{
                //保存失败
                map.put("fail",false);
            }
        }

        return  map;
    }
    /**
     * @description 修改用户
     *
     * @methodName editUser
     * @param userInfo userInfo
     * @return int
     * @author lucy [18616735761@163.com]
     * @time 2017/11/14 13:58
     */
    @Override
    public int editUser(UserInfo userInfo) {
        preUpdate(userInfo);
        return userDao.editUser(userInfo);
    }
    /**
     * @description 删除用户
     * @methodName delUser
     * @param id id
     * @return int
     * @author lucy [18616735761@163.com]
     * @time 2017/11/14 13:58
     */
    @Override
    public int delUser(String id) {
        return userDao.delUser(id);
    }
    /**
     * @description 通过名字查找用户信息
     * @methodName findUserByName
     * @param loginName loginName
     * @return java.util.List<com.crowdfunding.entity.sys.UserInfo>
     * @author lucy [18616735761@163.com]
     * @time 2017/11/15 11:26
     */
    @Override
    public UserInfo findUserByName(String loginName) {
       return userDao.findUserByName(loginName);

    }
    /**
     * @description
     * @methodName findUserForLogin
     * @param loginName loginName
     * @param password password
     * @return com.crowdfunding.entity.sys.UserInfo
     * @author lucy [18616735761@163.com]
     * @time 2017/12/11 16:21
     */
    @Override
    public UserInfo findUserForLogin(String loginName, String password) {
        UserInfo userInfo=userDao.findUserForLogin(loginName,password);
        return userInfo;
    }
}
