package com.joint.base.service;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.bean.Result;
import com.joint.base.bean.EnumManage;
import com.joint.base.entity.Comment;
import com.joint.base.entity.Users;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service接口 - 批注信息
 * ============================================================================
 * 版权所有 2014 。
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 */

public interface CommentService extends BaseLimitService<Comment, String> {

    public Map<String, Object> getListMap(Comment comment);
	/**
	 * 评论（无视层级，查询所有）
	 * @param pager
	 *				分页对象（不能为空）
	 * @param beginDate
	 *				开始时间（null则无视）
	 * @param endDate
	 *				结束时间（null则无视）
	 * @param creater
	 * 				创建者（null则无视）
	 * @param toUsers
	 * 				接受者（null则无视）
	 * @param targetClass
	 * 				目标对象（null则无视）
	 * @param targetId
	 * 				目标Id（null则无视）
	 * @param ifSystem
	 * 				是否由系统发送（默认0，系统1）（-1则无视）
	 * @param ifRead
	 * 				接受者是否已读（默认0，已读1）（-1则无视）
	 * @param commentStates
	 * 				批注状态[]（CommentState；null则无视）
	 * @param commentTypeEnums
	 * 				批注类别[]（CommentTypeEnum；null则无视）
	 * @param states
	 * 				状态数组（StateEnum；null则无视）
	 * @return Pager
	 */
	public Pager getPager(Pager pager, Date beginDate, Date endDate, Users creater, Users toUsers, String targetClass, String targetId, int ifSystem, int ifRead, EnumManage.CommentState[] commentStates, EnumManage.CommentTypeEnum[] commentTypeEnums, BaseEnum.StateEnum... states);
	public Pager getPager(Pager pager, Users creater, Users toUsers, String targetClass, String targetId, int ifSystem, int ifRead, EnumManage.CommentState[] commentStates, EnumManage.CommentTypeEnum[] commentTypeEnums, BaseEnum.StateEnum... states);
	/**
	 * 评论（by parent 为空则 查所有root，即parent is null）
	 * @param pager
	 *				分页对象（不能为空）
	 * @param beginDate
	 *				开始时间（null则无视）
	 * @param endDate
	 *				结束时间（null则无视）
	 * @param parent
	 *				上级对象（为空则查root，即parent is null）
	 * @param creater
	 * 				创建者（null则无视）
	 * @param toUsers
	 * 				接受者（null则无视）
	 * @param targetClass
	 * 				目标对象（null则无视）
	 * @param targetId
	 * 				目标Id（null则无视）
	 * @param ifSystem
	 * 				是否由系统发送（默认0，系统1）（-1则无视）
	 * @param ifRead
	 * 				接受者是否已读（默认0，已读1）（-1则无视）
	 * @param commentStates
	 * 				批注状态[]（CommentState；null则无视）
	 * @param commentTypeEnums
	 * 				批注类别[]（CommentTypeEnum；null则无视）
	 * @param states
	 * 				状态数组（StateEnum；null则无视）
	 * @return Pager
	 */
	public Pager getPagerByParent(Pager pager, Date beginDate, Date endDate, Comment parent, Users creater, Users toUsers, String targetClass, String targetId, int ifSystem, int ifRead, EnumManage.CommentState[] commentStates, EnumManage.CommentTypeEnum[] commentTypeEnums, BaseEnum.StateEnum... states);
	public Pager getPagerByParent(Pager pager, Comment parent, Users creater, Users toUsers, String targetClass, String targetId, int ifSystem, int ifRead, EnumManage.CommentState[] commentStates, EnumManage.CommentTypeEnum[] commentTypeEnums, BaseEnum.StateEnum... states);
	
	
	public Pager getPagerRoot(Pager pager, String targetClass, String targetId);
	public Pager getPagerByParent(Pager pager, String targetClass, String targetId, Comment parent);
	
	/**
	 * 与我相关的
	 * @param pager
	 * @param toUsers 接收者
	 * @return
	 */
	public Pager getPagerRootByToUsers(Pager pager, Users toUsers);
	



    /**
     * 批注删除
     * @param users
     * @param comment
     * @return
     */
    public Result delete(Users users, Comment comment);
    /**
     * 拷贝批注（目前简单处理，直接替换targetId，targetClass）
     * @param targetClass
     * @param targetId
     * @param toClass
     * @param toId
     */
    public void copyAllComment(Class<?> targetClass, String targetId, Class<?> toClass, String toId);
	/**
	 * 得到该目标Id下所有的直接评论，按日期升序
	 * @param targetId
	 * 
	 * @return
	 */
	public List<Comment> getAllRootComment(String targetId);
	/**
	 * 得到该目标Id下所有的评论，按日期升序，按二级节点排列
	 * @param
	 * 
	 * @return
	 */
	public List<Comment> getAllComment(String targetId, String parentId);
	/**
	 * 得到该目标Id下所有的评论，按日期升序
	 * @param
	 * 
	 * @return
	 */
	public List<Comment> getAllComment(String targetId);
	
	/**
	 * 得到该目标Id下所有的直接评论数量
	 * @param
	 * 
	 * @return
	 */
	public Long getAllRootCommentCount(String targetId);
	/**
	 * 得到该目标Id最新评论记录
	 * @param
	 * 
	 * @return
	 */
	public Comment getLastComment(String targetId);
	/**
	 * 根据Pager对象进行查询(提供分页、查找、排序功能).
	 * 得到该接收者所有的评论，按日期升序，按二级节点排列
	 * @param user
	 * 
	 * @return
	 */
	/**
	 * 根据Pager对象进行查询(提供分页、查找、排序功能).
	 * 得到该接收者所有的评论，按日期升序，按二级节点排列
	 * @param
	 * 
	 * @return
	 */
	
	public List<Comment> getPagerCommentByToUsers(Pager pager, Users users);
	/**
	 * 根据Pager对象进行查询(提供分页、查找、排序功能).
	 * 得到该创建者所有的评论，按日期升序，按二级节点排列
	 * @param
	 * 
	 * @return
	 */
	public List<Comment> getPagerCommentBySender(Pager pager, Users users);

    /**
     * 创建评论
     * @param creater
     * @param toUsers
     * @param targetId
     * @param targetObject
     * @param parent
     * @param text
     * @return
     */
    public List<Comment> createNewComment(Users creater, Set<Users> toUsers, String targetId, String targetObject, Comment parent, String text);


    /**
     * 后台，微信,App用
     * 创建评论（单个评论发送给多人）
     * @param comment
     * @param toUsers
     * @return
     */
    public Comment createNewCommentApp(Comment comment, Set<Users> toUsers);
}
