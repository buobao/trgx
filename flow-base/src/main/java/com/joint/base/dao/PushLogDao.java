package com.joint.base.dao;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.joint.base.bean.EnumManage;
import com.joint.base.entity.PushLog;
import com.joint.base.entity.Users;

import java.util.Date;


/**
 * Dao接口 - 推送记录
 * ============================================================================
 * 版权所有 2014 。
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 * ============================================================================
 */

public interface PushLogDao extends BaseEntityDao<PushLog, String> {

	/**
	 * 用户下推送记录
	 * 
	 * @param pager
	 *            分页对象（不能为空）
	 * @param users
	 *            用户（null则无视）
	 * @param pushWay
	 *            推送类别（PushWayEnum；-1则无视）
	 * @param pushType
	 *            推送方式（PushTypeEnum；-1则无视）
	 * @param pushErrcode
	 *            推送错误code（ErrorCodeEnum；-1则无视）
	 * @param states
	 *            状态数组（DocumentStateEnum；null则无视）
	 *                      
	 * @return Pager
	 */
	public Pager getPager(Pager pager, Users users, EnumManage.PushWayEnum pushWay, EnumManage.PushTypeEnum pushType, int pushErrcode, BaseEnum.StateEnum[] states);
	
	/**
	 * 用户下推送记录
	 * 
	 * @param pager
	 *            分页对象（不能为空）
	 * @param users
	 *            用户（null则无视）
	 * @param pushWay
	 *            推送类别（null则无视）
	 * @param pushType
	 *            推送方式（null则无视）
	 * @param pushErrcode
	 *            推送错误code（ErrorCodeEnum；-1则无视）
	 * @param isSend
	 *            是否已发送
	 * @param states
	 *            状态数组（DocumentStateEnum；null则无视）
	 *                      
	 * @return Pager
	 */
	public Pager getPager(Pager pager, Users users, EnumManage.PushWayEnum pushWay, EnumManage.PushTypeEnum pushType, int pushErrcode, boolean isSend, BaseEnum.StateEnum[] states);
	
	
	public Pager getPager(Pager pager, Users users, EnumManage.PushWayEnum pushWay, EnumManage.PushTypeEnum pushType,
						  int pushErrcode, Date date, BaseEnum.StateEnum[] states);
}