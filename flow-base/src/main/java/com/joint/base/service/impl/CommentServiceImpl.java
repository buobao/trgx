package com.joint.base.service.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.bean.Result;
import com.fz.us.base.util.DataUtil;
import com.fz.us.base.util.LogUtil;
import com.google.common.collect.Lists;
import com.joint.base.bean.EnumManage;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.CommentDao;
import com.joint.base.entity.Comment;
import com.joint.base.entity.Company;
import com.joint.base.entity.Users;
import com.joint.base.parent.NameEntity;
import com.joint.base.service.CommentService;
import com.joint.base.service.PushLogService;
import com.joint.base.service.UsersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;


/**
 * Service实现类 - 批注信息
 * ============================================================================
 * 版权所有 2014 。
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 */
@Service
public class CommentServiceImpl extends BaseLimitServiceImpl<Comment, String> implements CommentService {

	@Resource
	private CommentDao commentDao;
    @Resource
    private PushLogService pushLogService;
    @Resource
    private UsersService usersService;

    @Override
    public BaseEntityDao<Comment, String> getBaseEntityDao() {
        return commentDao;
    }


	@Override
	public Map<String, Object> getListMap(Comment comment) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", StringUtils.isNotEmpty(comment.getId())?comment.getId():"");
		map.put("no", StringUtils.isNotEmpty(comment.getNo())?comment.getNo():"");
		map.put("text", StringUtils.isNotEmpty(comment.getText())?comment.getText():"");
        map.put("filePath", (comment.getFile()!=null&&StringUtils.isNotEmpty(comment.getFile().getGridId()))?comment.getFile().getGridId():"");
		if(comment.getToUsers()!=null){
			Users toUsers = comment.getToUsers();
			map.put("toUsersId", toUsers.getId());
			map.put("toUsersName", toUsers.getName());
		}else{
			map.put("toUsersId", "");
			map.put("toUsersName", "");
		}
		map.put("targetId", StringUtils.isNotEmpty(comment.getTargetId())?comment.getTargetId():"");
		map.put("targetClass", StringUtils.isNotEmpty(comment.getTargetClass())?comment.getTargetClass():"");
		if(comment.getParent()!=null){
			Comment parent = comment.getParent();
			map.put("parentId", parent.getId());
			map.put("targetId", parent.getTargetId());
			map.put("targetClass", parent.getTargetClass());
            Object object = getObject(parent.getTargetClass(),parent.getTargetId());
            if(object instanceof NameEntity){
                NameEntity nObject = (NameEntity) object;
                map.put("theme", nObject.getName());
            }

		}else{
			map.put("parentId", "");
		}
		map.put("commentState", comment.getCommentState()!=null?comment.getCommentState().name():"");
		map.put("type", comment.getType()!=null?comment.getType().name():"");
        map.put("model", comment.getModel()!=null?comment.getModel().name():"");
		map.put("tempId", StringUtils.isNotEmpty(comment.getTempId())?comment.getTempId():"");
		
		map.put("createDate", DataUtil.DateTimeToString(comment.getCreateDate()));
		map.put("modifyDate", DataUtil.DateTimeToString(comment.getModifyDate()));
		if(comment.getCreater()!=null){
			Users creater = comment.getCreater();
			map.put("createrId", creater.getId());
			map.put("createrName", creater.getName());
//			map.put("createrFace", (creater.getHeadImage()!=null&&StringUtils.isNotEmpty(creater.getHeadImage().getGridId()))?creater.getHeadImage().getGridId():"");
		}else{
			map.put("createrId", "");
			map.put("createrName", "");
			map.put("createrFace", "");
		}
		return map;
	}

    @Override
    public Result delete(Users users, Comment comment) {
        if(comment!=null){
            if(comment.getParent()==null){//主批注
                //删除子批注
                List<Comment> childrenList = getAllComment(comment.getTargetId(), comment.getId());
                for(Comment child:childrenList){
                    child.setState(BaseEnum.StateEnum.Delete);
                    update(child);
                }
                //删除主批注
                comment.setState(BaseEnum.StateEnum.Delete);
                update(comment);
                return new Result(1, "操作成功！");
            }else{//子批注
                comment.setState(BaseEnum.StateEnum.Delete);
                update(comment);
                return new Result(1, "操作成功！");
            }
        }
        return new Result(0, "操作失败！");
    }

    @Override
    public void copyAllComment(Class<?> targetClass, String targetId, Class<?> toClass, String toId) {
        List<Comment> comments = getAllComment(targetId);
        for(Comment temp:comments){
            temp.setTargetId(toId);
            temp.setTargetClass(toClass.getName());
            update(temp);
        }
    }

	
	public List<Comment> getAllRootComment(String targetId){
		return commentDao.getAllRootComment(targetId);
	}
	
	public Long getAllRootCommentCount(String targetId){
		return commentDao.getAllRootCommentCount(targetId);
	}
	@Override
	public List<Comment> getAllComment(String targetId, String parentId) {
		return commentDao.getAllComment(targetId, parentId);
	}
	@Override
	public Comment getLastComment(String targetId) {
		return commentDao.getLastComment(targetId);
	}
	@Override
	public List<Comment> getPagerCommentByToUsers(Pager pager, Users users) {
		return commentDao.getPagerCommentByToUsers(pager, users);
	}

	@Override
	public List<Comment> getPagerCommentBySender(Pager pager, Users users) {
		return commentDao.getPagerCommentBySender(pager, users);
	}

    @Override
    public List<Comment> createNewComment(Users creater, Set<Users> toUsers, String targetId, String targetObject, Comment parent, String text) {
        List<Comment> commentList = Lists.newArrayList();
        for(Users users : toUsers){
            Comment comment = createNewComment(creater,targetId,targetObject,parent, EnumManage.CommentModelEnum.text,null,null,null,text,null);
            commentList.add(comment);
        }
        return commentList;

    }

    @Override
    public Comment createNewCommentApp(Comment comment, Set<Users> toUsers) {
        LogUtil.info("createNewCommentApp start");
        comment = updateAndEnable(comment);
        pushLogService.sendCommentNotificationApp(comment, toUsers);
        LogUtil.info("createNewCommentApp end");
        return comment;
    }

    @Override
	public List<Comment> getAllComment(String targetId) {
		return commentDao.getAllComment(targetId);
	}

	public Pager queryByPager(Pager pager,Company company,Users creater,Users toUsers,String targetId,BaseEnum.StateEnum[] states){
		
		return pager;
	}

	@Override
	public Pager getPager(Pager pager, Users creater, Users toUsers,
			String targetClass, String targetId, int ifSystem, int ifRead,
			EnumManage.CommentState[] commentStates, EnumManage.CommentTypeEnum[] commentTypeEnums,
			BaseEnum.StateEnum... states) {
		return commentDao.getPager(pager, null, null, creater, toUsers, targetClass, targetId, ifSystem, ifRead, commentStates, commentTypeEnums, states);
	}

	@Override
	public Pager getPagerByParent(Pager pager, Comment parent, Users creater,
			Users toUsers, String targetClass, String targetId, int ifSystem,
			int ifRead, EnumManage.CommentState[] commentStates,
			EnumManage.CommentTypeEnum[] commentTypeEnums, BaseEnum.StateEnum... states) {
		return commentDao.getPagerByParent(pager, null, null, parent, creater, toUsers, targetClass, targetId, ifSystem, ifRead, commentStates, commentTypeEnums, states);
	}

	@Override
	public Pager getPagerRoot(Pager pager, String targetClass, String targetId) {
		return commentDao.getPagerByParent(pager, null, null, null, null, null, targetClass, targetId, -1, -1, null, null, BaseEnum.StateEnum.Enable);
	}

	@Override
	public Pager getPagerByParent(Pager pager, String targetClass,
			String targetId, Comment parent) {
		return commentDao.getPagerByParent(pager, null, null, parent, null, null, targetClass, targetId, -1, -1, null, null, BaseEnum.StateEnum.Enable);
	}

	@Override
	public Pager getPagerRootByToUsers(Pager pager, Users toUsers) {
		return commentDao.getPagerRootByToUsers(pager, toUsers);
	}

	@Override
	public Pager getPager(Pager pager, Date beginDate, Date endDate,
			Users creater, Users toUsers, String targetClass, String targetId,
			int ifSystem, int ifRead, EnumManage.CommentState[] commentStates,
			EnumManage.CommentTypeEnum[] commentTypeEnums, BaseEnum.StateEnum... states) {
		return commentDao.getPager(pager, beginDate, endDate, creater, toUsers, targetClass, targetId, ifSystem, ifRead, commentStates, commentTypeEnums, states);
	}

	@Override
	public Pager getPagerByParent(Pager pager, Date beginDate, Date endDate,
			Comment parent, Users creater, Users toUsers, String targetClass,
			String targetId, int ifSystem, int ifRead,
			EnumManage.CommentState[] commentStates, EnumManage.CommentTypeEnum[] commentTypeEnums,
			BaseEnum.StateEnum... states) {
		return commentDao.getPagerByParent(pager, beginDate, endDate, parent, creater, toUsers, targetClass, targetId, ifSystem, ifRead, commentStates, commentTypeEnums, states);
	}

    /**
     *
     * @param creater
     * @param targetId
     * @param target
     * @param parent
     * @param modelEnum
     * @param file
     * @param fileContentType
     * @param fileName
     * @param text
     * @param atUsers
     * @return
     */
    public Comment createNewComment(Users creater, String targetId, String target, Comment parent, EnumManage.CommentModelEnum modelEnum,File file,String fileContentType,String fileName,String text,String atUsers) {
        Comment comment = new Comment(creater,targetId,target,parent,modelEnum,text,atUsers);
        comment.setCompany(usersService.getCompanyByUser(creater));
        save(comment);
        pushLogService.sendCommentNotification(comment, target);
        return comment;
    }


}
