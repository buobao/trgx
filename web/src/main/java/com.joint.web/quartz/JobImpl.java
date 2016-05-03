package com.joint.web.quartz;


import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.joint.base.bean.EnumManage;
import com.joint.base.bean.FlowEnum;
import com.joint.base.bean.SystemConfig;
import com.joint.base.entity.Users;
import com.joint.base.entity.system.Admin;
import com.joint.base.mp.WxMpInCacheConfigStorage;
import com.joint.base.service.AdminService;
import com.joint.base.service.UsersService;
import com.joint.base.service.jms.AdvancedNotifyMessageProducer;
import com.joint.base.util.DataUtil;
import com.joint.core.bean.EstEnum;
import com.joint.core.entity.*;
import com.joint.core.service.*;
import fz.me.chanjar.weixin.common.api.WxConsts;
import fz.me.chanjar.weixin.mp.bean.WxMpCustomMessage;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by hpj on 2015/5/7.
 */
public class JobImpl implements Job {

    @Resource
    private AdminInfoService adminInfoService;
    @Resource
    private ProAttendService proAttendService;
    @Resource
    private ProInfoService proInfoService;
    @Resource
    private WxMpInCacheConfigStorage wxMpConfigStorage;
    @Resource
    private SystemConfig systemConfig;
    @Resource
    private AdvancedNotifyMessageProducer notifyMessageProducer;
    @Resource
    private UsersService usersService;
    @Resource
    private AdminService adminService;
    @Resource
    private AssignmentsService assignmentsService;
    @Resource
    private TaskRemindService taskRemindService;
    /**
     * 计算剩余有效期
     */
    @Override
    public void evalExpire(){
        Pager pager = new Pager(0);
        Map<String,Object> rMap = Maps.newHashMap();
        rMap.put("status", EstEnum.AdminStatusEnum.using);
        pager = adminInfoService.findByPager(pager,rMap);
        List<AdminInfo> adminInfoList = (List<AdminInfo>) (pager.getTotalCount()>0?pager.getList(): Lists.newArrayList());
        adminInfoService.evalExpire(adminInfoList);
    }

    /**
     * @author zhucx
     * @date 2015-6-9
     * 提前1小时发送签到推送
     */
    @Override
    public void doProAttendDeal() {
        System.out.println("doProAttendDeal 执行");
        Date now = new Date();
        String time = DataUtil.DateToString(now, "HH:mm:ss");
        now = DataUtil.StringToDate(time, "HH:mm:ss");
        Pager pager = getPager();
        pager = proInfoService.findByPagerAndStates(pager, new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable} );
        List<ProInfo> listProInfo = (List<ProInfo>) (pager.getTotalCount()>0?pager.getList(): Lists.newArrayList());
      //   System.out.println("项目个数: "+listProInfo.size());
            for(int i=0; i<listProInfo.size();i++){
            //   System.out.println("第一层");
                ProInfo proInfo = listProInfo.get(i);
                if (proInfo.getProState() == 1){
                    continue;
                }
                List<Users> listUsers = (List<Users>) proInfoService.findByProInfoPager(getPager(),null,null,proInfo,null).getList();
                List<ProAttend> listProAttend = proAttendService.getListByProInfo(proInfo,new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable});
                Users usersChief = (Users) proInfoService.findChiefByPager(getPager(),proInfo, null).getList().get(0);

              //  System.out.println("项目总监"+usersChief.getName());
              //  System.out.println("前成员个数:"+listUsers.size());
                if (!listUsers.contains(usersChief)){
               //     System.out.println("加入总监");
                    listUsers.add(usersChief);
                }
               // System.out.println("后成员个数:"+listUsers.size());
                if(listUsers == null){
                    continue;
                }
                Iterator<Users> iUsers = listUsers.iterator();
                while(iUsers.hasNext()){
                //    System.out.println("第二层");
                    Users users =  iUsers.next();
                    String mobile =users.getMobile();
                    Admin admin = adminService.getByMobile(mobile);
                    if(admin == null){
                        continue;
                    }
                    String openId =admin.getOpenId();
                    for(int j=0; j<listProAttend.size();j++){
                    //    System.out.println("第三层");
                        ProAttend proAttend = listProAttend.get(j);
                        Date regularStart  = proAttend.getRegularStart();
                        Date regularEnd  = proAttend.getRegularEnd();
                        String regularStartTime = DataUtil.DateToString(regularStart,"HH:mm");
                        String regularEndTime = DataUtil.DateToString(regularEnd,"HH:mm");
                        long rs = DataUtil.TimeDiff(regularStart, now);
                        if(rs <= 1000*60*60 && rs > 1000*60*30){
                      //      System.out.println("发送消息开始");
                            sendMessage(openId,proInfo.getName(),regularStartTime,regularEndTime);
                     //       System.out.println("发送消息结束");
                        }

                    }
                }

            }

    }

    /**
     * @todo
     * 项目到期定时提醒
     */
    @Override
    public void doTaskRemind() {
        Date now = new Date();
        String time = DataUtil.DateToString(now, "yyyy-MM-dd");
        now = DataUtil.StringToDate(time, "yyyy-MM-dd");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", BaseEnum.StateEnum.Enable);
        params.put("processState",FlowEnum.ProcessState.Finished);
        Pager pager = new Pager(0);
        pager = assignmentsService.findByPagerAndCompany(pager,null,null,params);
        List<Assignments> assignmentsList = (List<Assignments>) (pager.getTotalCount() > 0 ? pager.getList() : Lists.newArrayList());
        for (Assignments assignments : assignmentsList) {
            int rs = DataUtil.DateDiff(now, assignments.getYear());
            if (rs == 10) {
                TaskRemind taskRemind = new TaskRemind();
                taskRemind.setAssignments(assignments);
                taskRemind.setIsread(0);
                String url = "ajax-assignments!read.action?keyId=" + assignments.getId();
                taskRemind.setUrl(url);
                taskRemind.setUsers(assignments.getBoss());
                taskRemindService.save(taskRemind);
            }
        }
    }

    /**
     * 获得分页器
     * @return
     */
    public Pager getPager(){
        Pager pager = new Pager(0);
        pager.setOrderBy("createDate");
        pager.setOrderType(BaseEnum.OrderType.desc);
        return pager;
    }

    /**
     * 发送消息
     * @param openId
     */
    public void sendMessage(String openId, String proInfoName,String regularStartTime,String regularEndTime){
        //发送一个mp的客服消息，利用JMS，这里暂时测试用
        WxMpCustomMessage message = new WxMpCustomMessage();
        message.setMsgType(WxConsts.CUSTOM_MSG_TEXT);
        message.setToUser(openId);
        //String link = "<a href=\""+wxMpConfigStorage.getHttpHost()+"/"+systemConfig.getWebroot()+"/mp/account!resetMp.action?_="+ Identities.randomBase62(8)+"\">设置我的账号</a>";
        message.setContent("[微笑] 您的工程项目'" + proInfoName+ "' 正常签到时间为: "+regularStartTime+ "---- "+ regularEndTime + ",请及时签到" );
        notifyMessageProducer.sendQueue(message, EnumManage.NotifyKeyEnum.wxMpCustomMessage.name());
    }

}
