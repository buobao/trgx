package com.joint.base.dao.impl;


import com.fz.us.base.bean.BaseEnum;
import com.joint.base.dao.PostDao;
import com.joint.base.entity.Post;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Dao实现类 - 岗位
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

@Repository
public class PostDaoImpl extends BaseEntityDaoImpl<Post, String> implements PostDao {
    public Post getPostByName(String name){
        Assert.notNull(name, "name is required");
        String hql = "from Post p where p.name=? and p.state = ?";
        List<Post> postList= (List<Post>) getSession().createQuery(hql).setParameter(0,name).setParameter(1, BaseEnum.StateEnum.Enable);
        if(postList.size()>0){
            return postList.get(0);
        }else {
            return null;
        }
    }
}