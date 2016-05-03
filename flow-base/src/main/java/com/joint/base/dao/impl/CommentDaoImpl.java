package com.joint.base.dao.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.joint.base.bean.EnumManage;
import com.joint.base.dao.CommentDao;
import com.joint.base.entity.Comment;
import com.joint.base.entity.Users;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Dao实现类 - 批注信息
 * ============================================================================
 * 版权所有 2014 。
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 * ============================================================================
 */

@Repository
public class CommentDaoImpl extends BaseEntityDaoImpl<Comment, String> implements CommentDao {

	@Override
	public Pager getPager(Pager pager, Date beginDate, Date endDate, Users creater, Users toUsers,
			String targetClass, String targetId, int ifSystem, int ifRead,
			EnumManage.CommentState[] commentStates, EnumManage.CommentTypeEnum[] commentTypeEnums,
			BaseEnum.StateEnum... states) {
		if(pager != null){
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Comment.class);
			if(beginDate != null){
				detachedCriteria.add(Restrictions.ge("createDate", beginDate));
			}
			if(endDate != null){
				detachedCriteria.add(Restrictions.lt("createDate", endDate));
			}
			if(creater != null){
				detachedCriteria.createAlias("creater", "creater");
				detachedCriteria.add(Restrictions.or(
						Restrictions.eq("creater", creater),
						Restrictions.like("atUsers","%"+creater.getId()+"%")
				));
			}
//			if(atUsers != null){
//				detachedCriteria.createAlias("toUsers", "toUsers");
//				detachedCriteria.add(Restrictions.or(Restrictions.eq("toUsers", toUsers), Restrictions.eq("toUsers.id", toUsers.getId())));
//			}
			if(StringUtils.isNotEmpty(targetClass)){
				detachedCriteria.add(Restrictions.eq("targetClass", targetClass));
			}
			if(StringUtils.isNotEmpty(targetId)){
				detachedCriteria.add(Restrictions.eq("targetId", targetId));
			}
			if(ifSystem>-1){
				detachedCriteria.add(Restrictions.eq("ifSystem", ifSystem));
			}
			if(ifRead>-1){
				detachedCriteria.add(Restrictions.eq("ifRead", ifRead));
			}
			if(commentStates!=null&&commentStates.length>0){
				Criterion[] criterions = new Criterion[commentStates.length];
				for(int i=0;i<commentStates.length;i++){
					criterions[i]= Restrictions.eq("commentState", commentStates[i]);
				}
				detachedCriteria.add(Restrictions.or(criterions));
			}
			if(commentTypeEnums!=null&&commentTypeEnums.length>0){
				Criterion[] criterions = new Criterion[commentTypeEnums.length];
				for(int i=0;i<commentTypeEnums.length;i++){
					criterions[i]= Restrictions.eq("type", commentTypeEnums[i]);
				}
				detachedCriteria.add(Restrictions.or(criterions));
			}
			if(states!=null&&states.length>0){
				Criterion[] criterions = new Criterion[states.length];
				for(int i=0;i<states.length;i++){
					criterions[i]= Restrictions.eq("state", states[i]);
				}
				detachedCriteria.add(Restrictions.or(criterions));
			}
			return findByPager(pager, detachedCriteria);
		}
		return null;
	}

	@Override
	public Pager getPagerByParent(Pager pager, Date beginDate, Date endDate, Comment parent, Users creater,
			Users toUsers, String targetClass, String targetId, int ifSystem,
			int ifRead, EnumManage.CommentState[] commentStates,
			EnumManage.CommentTypeEnum[] commentTypeEnums, BaseEnum.StateEnum... states) {
		if(pager != null){
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Comment.class);
			if(beginDate != null){
				detachedCriteria.add(Restrictions.ge("createDate", beginDate));
			}
			if(endDate != null){
				detachedCriteria.add(Restrictions.lt("createDate", endDate));
			}
			if(parent != null){
				detachedCriteria.createAlias("parent", "parent");
				detachedCriteria.add(Restrictions.or(Restrictions.eq("parent", parent), Restrictions.eq("parent.id", parent.getId())));
			}else{
				detachedCriteria.add(Restrictions.isNull("parent"));
			}
			if(creater != null){
				detachedCriteria.createAlias("creater", "creater");
				detachedCriteria.add(Restrictions.or(Restrictions.eq("creater", creater), Restrictions.eq("creater.id", creater.getId())));
			}
			if(toUsers != null){
				detachedCriteria.createAlias("toUsers", "toUsers");
				detachedCriteria.add(Restrictions.or(Restrictions.eq("toUsers", toUsers), Restrictions.eq("toUsers.id", toUsers.getId())));
			}
			if(StringUtils.isNotEmpty(targetClass)){
				detachedCriteria.add(Restrictions.eq("targetClass", targetClass));
			}
			if(StringUtils.isNotEmpty(targetId)){
				detachedCriteria.add(Restrictions.eq("targetId", targetId));
			}
			if(ifSystem>-1){
				detachedCriteria.add(Restrictions.eq("ifSystem", ifSystem));
			}
			if(ifRead>-1){
				detachedCriteria.add(Restrictions.eq("ifRead", ifRead));
			}
			if(commentStates!=null&&commentStates.length>0){
				Criterion[] criterions = new Criterion[commentStates.length];
				for(int i=0;i<commentStates.length;i++){
					criterions[i]= Restrictions.eq("commentState", commentStates[i]);
				}
				detachedCriteria.add(Restrictions.or(criterions));
			}
			if(commentTypeEnums!=null&&commentTypeEnums.length>0){
				Criterion[] criterions = new Criterion[commentTypeEnums.length];
				for(int i=0;i<commentTypeEnums.length;i++){
					criterions[i]= Restrictions.eq("type", commentTypeEnums[i]);
				}
				detachedCriteria.add(Restrictions.or(criterions));
			}
			if(states!=null&&states.length>0){
				Criterion[] criterions = new Criterion[states.length];
				for(int i=0;i<states.length;i++){
					criterions[i]= Restrictions.eq("state", states[i]);
				}
				detachedCriteria.add(Restrictions.or(criterions));
			}
			return findByPager(pager, detachedCriteria);
		}
		return null;
	}
	
	@Override
	public Pager getPagerRootByToUsers(Pager pager, Users toUsers) {
		if(pager != null){
			Integer pageNumber = pager.getPageNumber();
			Integer pageSize = pager.getPageSize();
			
			String hql = "from Comment c where c.parent is null and c.state = :state and (c.toUsers.id = :toUsersId or c.id in (select a.parent.id from Comment a where a.toUsers.id = :toUsersId and a.parent is not null and a.state = :state order by a.createDate desc)) order by c.createDate desc";
			Query query = getSession().createQuery(hql).setFirstResult((pageNumber-1)*pageSize).setMaxResults(pageSize).setParameter("toUsersId", toUsers.getId()).setParameter("state", BaseEnum.StateEnum.Enable);
			pager.setList(query.list());
			return pager;
		}
		return null;
	}
	
	@Override
	public List<Comment> getAllRootComment(String targetId) {
		// TODO Auto-generated method stub
		String hql = "from Comment c where c.targetId = ? and c.parent is null and c.state =? order by c.createDate asc";
		return (List<Comment>) getSession().createQuery(hql).setParameter(0, targetId).setParameter(1, BaseEnum.StateEnum.Enable).list();
	}

	@Override
	public List<Comment> getAllComment(String targetId,String parentId) {
		// TODO Auto-generated method stub
		String hql = "from Comment c where c.targetId = ? and c.parent.id = ? and c.state =? order by c.createDate asc";
		List list = (List<Comment>) getSession().createQuery(hql).setParameter(0, targetId).setParameter(1, parentId).setParameter(2, BaseEnum.StateEnum.Enable).list();
		
		return list;
	}
	
	@Override
	public List<Comment> getAllComment(String targetId) {
		// TODO Auto-generated method stub
		String hql = "from Comment c where c.targetId = ? and c.state =? order by c.createDate asc";
		List list = (List<Comment>) getSession().createQuery(hql).setParameter(0, targetId).setParameter(1, BaseEnum.StateEnum.Enable).list();
		
		return list;
	}
	@Override
	public Long getAllRootCommentCount(String targetId) {
		Session session = sessionFactory.openSession();
		Long count = (long) 0;
		try{
			// TODO Auto-generated method stub
			String hql = "select count(*) from Comment c where c.targetId = ? and c.parent is null and c.state =? order by c.createDate asc";
			count = (Long) session.createQuery(hql).setParameter(0, targetId).setParameter(1, BaseEnum.StateEnum.Enable).uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return count;
	}
	@Override
	public Comment getLastComment(String targetId) {
		Comment comment = new Comment();
		String hql = "from Comment c where c.targetId = ? and c.parent is null and c.state =? order by c.createDate desc";
		List<Comment> commentList= getSession().createQuery(hql).setParameter(0, targetId).setParameter(1, BaseEnum.StateEnum.Enable).list();
		if(commentList.size()>0){
			comment = commentList.get(0);
		}
		return comment;
	}
	@Override
	public List<Comment> getPagerCommentByToUsers(Pager pager, Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Comment> getPagerCommentBySender(Pager pager, Users users) {
		// TODO Auto-generated method stub
		return null;
	}


}
