package com.joint.web.action.com;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.dao.jdbc.JdbcDao;
import com.fz.us.base.service.memcached.SpyMemcachedClient;
import com.fz.us.dict.bean.DictBean;
import com.fz.us.dict.entity.Dict;
import com.fz.us.dict.service.DictService;
import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.Toolbox;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.Feature;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Pie;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.joint.base.entity.Company;
import com.joint.base.entity.Users;
import com.joint.base.service.CompanyService;
import com.joint.base.service.UsersService;
import com.joint.base.util.DataUtil;
import com.joint.base.util.StringUtils;
import com.joint.base.util.XmlUtil;
import com.joint.base.util.security.NetWork;
import com.joint.core.entity.PieEntity;
import com.joint.core.entity.ProInfo;
import com.joint.core.service.*;
import com.joint.web.action.BaseAdminAction;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.ParentPackage;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dqf on 2015/7/27.
 */
@ParentPackage("com")
public class AjaxIndex extends BaseAdminAction {
    @Resource
    private CompanyService companyService;
    @Resource
    private ClientService clientService;
    @Resource
    private LinkmanService linkmanService;
    @Resource
    private ProInfoService proInfoService;
    @Resource
    private ProBackService proBackService;
    @Resource
    private ProSignInService proSignInService;
    @Resource
    private ProLeaveService proLeaveService;
    @Resource
    private UsersService usersService;
    @Resource
    private SpyMemcachedClient spyMemcachedClient;
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    private DictService dictService;
    @Resource
    private ProAttendService proAttendService;


    //天气预报数据
    private List<Map<String,Object>> weatherData;
    private Map<String,Object> businessData;
    //查询时间类型
    private String type;
    private String type1;
    private String type2;
    //项目类型
    private Set<ProInfo> proInfoSet;
    private Set<Users> userSet;
    //柱状图项目ID
    private String proInfoId;

    private String firstType;
    private List<Long> nData;
    private List<Long> bData;
    private List<Object> labels;

    private List<String> dateStr1;
    private List<String> dateStr2;

    //饼图项目ID
    private String pipeProInfoId;
    //饼图人员ID
    private String pipeUserId;

    public String page(){
        pager=new Pager(0);
        Map<String,Object> params= Maps.newHashMap();
        params.put("state", BaseEnum.StateEnum.Enable);
        Users loginUser=usersService.getLoginInfo();
        Company com=usersService.getCompanyByUser();
        pager=proInfoService.findByPagerAndLimit(pager,loginUser,com,params);
        List<ProInfo> proInfos= (List<ProInfo>) (pager.getTotalCount()>0?pager.getList(): Lists.newArrayList());
        proInfoSet= Sets.newHashSet();
        proInfoSet.addAll(proInfos);

        pager=new Pager(0);
        pager = usersService.findByPagerAndCompany(pager,null,com,params);
        List<Users> usersInfo = (List<Users>) (pager.getTotalCount()>0?pager.getList(): Lists.newArrayList());
        userSet = Sets.newHashSet();
        userSet.addAll(usersInfo);

        businessData = Maps.newHashMap();
        Pager pager = new Pager(0);
        Users user=usersService.getLoginInfo();
        ProInfo proInfo=null;
        //项目
        Map<String,Object> rmap= Maps.newHashMap();
        rmap.put("state", BaseEnum.StateEnum.Enable);
        //反馈
        int clientNum = clientService.findByPagerAndCompany(pager,null,com,new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable}).getList().size();
        int linkNum = linkmanService.findByPagerAndCompany(pager,null,com,new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable}).getList().size();
        int proNum=0;
        int backNum=0;
        Pager infopager=proInfoService.findByPagerAndLimit(new Pager(0),user,com,rmap);
        Pager backpager=proBackService.findByPagerAndLimit(new Pager(0),user,com,rmap);
        if(infopager!=null && infopager.getTotalCount() > 0){
            proNum=infopager.getList().size();
        }
        if(backpager!=null && backpager.getTotalCount() > 0){
            backNum=backpager.getList().size();
        }

        businessData.put("client", clientNum);
        businessData.put("link",linkNum);
        businessData.put("proInfo",proNum);
        businessData.put("proBack",backNum);

        weatherData  = new ArrayList<Map<String, Object>>();

        for(int i=0;i<6;i++){
            Map<String,Object> map = this.getWeather(i);
            weatherData.add(map);
        }
        /*
        int j=1;
        for(Map<String,Object> map: weatherData){
            System.out.println("第"+j+"次循环");
            for(Map.Entry<String,Object> entry:map.entrySet()) {
                System.out.println("key:" + entry.getKey());
                System.out.println("value:" + entry.getValue());
            }
            j++;
        }*/
        List<String> timeList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        for(int i=0; i<5; i++){
            cal.add(Calendar.DATE, 1);
            timeList.add(sdf.format(cal.getTime())) ;
        }
        dateStr1 = timeList;

        return "page";
    }

    //组装成option
    private GsonOption getPipeOption(List<String> dateList, List<GsonOption> pieList){
        GsonOption option = new GsonOption();
        option.timeline().autoPlay(false);
        option.timeline().playInterval(2000);

        for (GsonOption p:pieList){
            option.options().add(p);
        }

        for (String s:dateList){
            option.timeline().data(s);
        }

        return option;
    }

    private GsonOption initFirstOption(){
        GsonOption gp = new GsonOption();
        Title title = new Title();
        title.setText("");
        title.setSubtext("");
        gp.setTitle(title);
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger(Trigger.item);
        tooltip.setFormatter("{a} <br/>{b} : {c} ({d}%)");
        gp.setTooltip(tooltip);
        Toolbox toolbox = new Toolbox();
        toolbox.setShow(true);
        Map<String, Feature> features = new HashMap<String,Feature>();
        Feature f = new Feature();
        f.setShow(true);
        features.put("mark", f);
        features.put("restore", f);
        features.put("saveAsImage", f);
        f = new Feature();
        f.setShow(true);
        f.setReadOnly(false);
        features.put("dataView", f);
        f = new Feature();
        f.setShow(true);
        f.setType(new String[]{"pie", "funnel"});
        features.put("magicType",f);
        toolbox.setFeature(features);
        //gp.setToolbox(toolbox);                           //显示工具栏
        return gp;
    }

    //获取时间轴列表
    private List<String> getPipeDateList(String type){
        List<String> dateList = new ArrayList<String>();
        Date curDate = new Date();
        Calendar cd = Calendar.getInstance();
        if (StringUtils.equals(type, "week")){
            int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
            curDate = DataUtil.getAfterDay(curDate, -dayOfWeek);
            for (int i=0;i<7;i++){
                dateList.add(DataUtil.DateToString(DataUtil.getAfterDay(curDate, i), "yyyy-MM-dd"));
            }
        } else if (StringUtils.equals(type, "month")){
            dateList = new ArrayList<String>(Arrays.asList(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"}));
        } else {
            int year = cd.get(Calendar.YEAR);
            for (int i=0;i<=4;i++){
                dateList.add(year-i+"");
            }
        }

        return dateList;
    }

    //按照月份和年份查询时需要计算查询范围，这里返回查询每个时间节点的时间范围
    private Map<String, String> getDateRange(String type){
        Map<String, String> mp = new HashMap<String,String>();
        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        if (StringUtils.equals(type, "month")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            cd.set(Calendar.YEAR, year);
            for (int i=0;i<12;i++){
                cd.set(Calendar.MONTH, i);
                cd.set(Calendar.DATE, 1);
                mp.put((i + 1) + "_start", format.format(cd.getTime()));
                cd.roll(Calendar.DATE, -1);
                mp.put((i + 1) + "_end", format.format(cd.getTime()));
            }

        } else {
            for (int i=4;i>=0;i--){
                mp.put((year-i)+"_start",(year-i)+"-01-01");
                mp.put((year-i)+"_end",(year-i)+"-12-31");
            }
        }
        return mp;
    }

    //查询指定时间范围内的记录数
    private Long getPipeCountForRange(String start, String end, Map<String,String> mp){
        Long get_num = new Long(0);
        String sql_all = "SELECT count(*) as perCount FROM ec_proback WHERE createDate >= :date1 and createDate <= :date2 and company_id= :companyId and state='Enable' ";
        if (mp != null && mp.size() > 0){
            for (String key :mp.keySet()){
                sql_all += " and "+key+"='"+mp.get(key)+"' ";
            }
        }
        Map<String,String> all_filter = new HashMap<String,String>();
        all_filter.put("date1", start+" 00:00:00");
        all_filter.put("date2", end +" 23:59:59");
        all_filter.put("companyId", usersService.getLoginInfo().getCompany().getId());
        List<Map<String, Object>> all_ListMap = jdbcDao.findForListMap(sql_all, all_filter);
        if(null != all_ListMap && all_ListMap.size()>0) {
            Map lMap = all_ListMap.get(0);
            get_num = (Long)lMap.get("perCount");
        }
        return get_num;
    }

    private List<GsonOption> queryPipeaByYear(List<Dict> protypes){
        List<GsonOption> pieList = new ArrayList<GsonOption>();
        Map<String,String> mp = getDateRange("year");
        Calendar cd = Calendar.getInstance();
        int thisYear = cd.get(Calendar.YEAR);
        boolean isfirst = true;
        for (int y=thisYear;y>=thisYear-4;y--){
            Long all_num = getPipeCountForRange(mp.get(y+"_start"), mp.get(y+"_end"),null);  //当年总计
            String otherStr = "";
            String[] names = new String[]{"","","","",""};
            Long[] nums = new Long[]{-1L,-1L,-1L,-1L};
            GsonOption gp = new GsonOption();
            Pie pie = new Pie();
            if (isfirst){
                gp=initFirstOption();
                pie.setCenter(new String[]{"50%","45%"});
                pie.setRadius("60%");
            }
            pie.setName(y+"");
            //pie.setName(i+"");
            pie.setType(SeriesType.pie);

            for (Dict d:protypes){
                Map<String,String> filter = new HashMap<String,String>();
                filter.put("category_id", d.getId());
                Long get_num = getPipeCountForRange(mp.get(y+"_start"), mp.get(y+"_end"), filter);

                int mark = 0;
                for (int j=1;j<nums.length;j++){
                    if (nums[j]<nums[mark]){
                        mark = j;
                    }
                }
                if (get_num > nums[mark]){
                    nums[mark] = get_num;
                    names[mark] = d.getName();
                }else{
                    otherStr +=d.getName()+":"+(get_num)+"\n";
                }
            }

            for (int j=0;j<nums.length;j++){
                if (nums[j] < 0){
                    break;
                }
                PieEntity entity = new PieEntity();
                entity.setName(names[j]);
                entity.setValue(nums[j]);
                pie.data(entity);
                all_num -= nums[j];
            }
            if (protypes.size() > 4){
                PieEntity last_entity = new PieEntity();
                last_entity.setName("其他");
                last_entity.setValue(all_num);
                pie.data(last_entity);
            }
            gp.series(pie);

            if (isfirst){
                Legend legend = new Legend();
                List<String> titNames = new ArrayList<String>();
                for (int j=0;j<names.length;j++){
                    if (StringUtils.isEmpty(names[j])){
                        break;
                    }
                    titNames.add(names[j]);
                }
                if (protypes.size() > 4){
                    titNames.add("其他");
                }
                for (String t:titNames){
                    legend.data(t);
                }
                gp.setLegend(legend);
                isfirst = false;
            }
            pieList.add(gp);
        }

        return pieList;
    }

    private List<GsonOption> queryPipeaByMonth(List<Dict> protypes){
        List<GsonOption> pieList = new ArrayList<GsonOption>();
        Map<String, String> mp = getDateRange("month");

        boolean isfirst = true;
        List<String> cont_month = new ArrayList<String>(Arrays.asList(new String[]{"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"}));
        for (int i=1;i<=12;i++){
            Long all_num = getPipeCountForRange(mp.get(i+"_start"), mp.get(i+"_end"),null);  //当月总数
            String otherStr = "";
            String[] names = new String[]{"","","","",""};
            Long[] nums = new Long[]{-1L,-1L,-1L,-1L};
            GsonOption gp = new GsonOption();
            Pie pie = new Pie();
            if (isfirst){
                gp=initFirstOption();
                pie.setCenter(new String[]{"50%","45%"});
                pie.setRadius("60%");
            }
            pie.setName(cont_month.get(i-1));
            //pie.setName(i+"");
            pie.setType(SeriesType.pie);

            for (Dict d:protypes){
                Map<String,String> filter = new HashMap<String,String>();
                filter.put("category_id", d.getId());
                Long get_num = getPipeCountForRange(mp.get(i+"_start"), mp.get(i+"_end"), filter);

                int mark = 0;
                for (int j=1;j<nums.length;j++){
                    if (nums[j]<nums[mark]){
                        mark = j;
                    }
                }
                if (get_num > nums[mark]){
                    nums[mark] = get_num;
                    names[mark] = d.getName();
                }else{
                    otherStr +=d.getName()+":"+(get_num)+"\n";
                }
            }

            for (int j=0;j<nums.length;j++){
                if (nums[j] < 0){
                    break;
                }
                PieEntity entity = new PieEntity();
                entity.setName(names[j]);
                entity.setValue(nums[j]);
                pie.data(entity);
                all_num -= nums[j];
            }
            if (protypes.size() > 4){
                PieEntity last_entity = new PieEntity();
                last_entity.setName("其他");
                last_entity.setValue(all_num);
                pie.data(last_entity);
            }
            gp.series(pie);

            if (isfirst){
                Legend legend = new Legend();
                List<String> titNames = new ArrayList<String>();
                for (int j=0;j<names.length;j++){
                    if (StringUtils.isEmpty(names[j])){
                        break;
                    }
                    titNames.add(names[j]);
                }
                if (protypes.size() > 4){
                    titNames.add("其他");
                }
                for (String t:titNames){
                    legend.data(t);
                }
                gp.setLegend(legend);
                isfirst = false;
            }
            pieList.add(gp);

        }

        return pieList;
    }

    private List<GsonOption> queryPipeaByWeek(List<String> dateList, List<Dict> protypes){
        List<GsonOption> pieList = new ArrayList<GsonOption>();
        boolean isfirst = true;
        for (String s:dateList){
            Long all_num = getPipeCountForRange(dateList.get(0), dateList.get(dateList.size()-1),null);  //当周总数

            String otherStr = "";
            String[] names = new String[]{"","","","",""};
            Long[] nums = new Long[]{-1L,-1L,-1L,-1L};
            GsonOption gp = new GsonOption();
            Pie pie = new Pie();
            if (isfirst){
                gp=initFirstOption();
                pie.setCenter(new String[]{"50%","45%"});
                pie.setRadius("60%");
            }
            pie.setName(s);
            pie.setType(SeriesType.pie);

            for (Dict d:protypes){
                Map<String,String> filter = new HashMap<String,String>();
                filter.put("category_id", d.getId());
                Long get_num = getPipeCountForRange(dateList.get(0), dateList.get(dateList.size()-1),filter);

                int mark = 0;
                for (int i=1;i<nums.length;i++){
                    if (nums[i]<nums[mark]){
                        mark = i;
                    }
                }
                if (get_num > nums[mark]){
                    nums[mark] = get_num;
                    names[mark] = d.getName();
                }else{
                    otherStr +=d.getName()+":"+(get_num)+"\n";
                }
            }

            for (int i=0;i<nums.length;i++){
                if (nums[i] < 0){
                    break;
                }
                PieEntity entity = new PieEntity();
                entity.setName(names[i]);
                entity.setValue(nums[i]);
                pie.data(entity);
                all_num -= nums[i];
            }
            if (protypes.size() > 4){
                PieEntity last_entity = new PieEntity();
                last_entity.setName("其他");        //其他字段设置
                last_entity.setValue(all_num);
                pie.data(last_entity);
            }
            gp.series(pie);

            if (isfirst){
                Legend legend = new Legend();
                List<String> titNames = new ArrayList<String>();
                for (int i=0;i<names.length;i++){
                    if (StringUtils.isEmpty(names[i])){
                        break;
                    }
                    titNames.add(names[i]);
                }
                if (protypes.size() > 4){
                    titNames.add("其他");
                }
                for (String t:titNames){
                    legend.data(t);
                }
                gp.setLegend(legend);
                isfirst = false;
            }
            pieList.add(gp);
        }
        return pieList;
    }

    public String pipea(){
        List<Dict> protypes = dictService.listFormDefinedEnable(DictBean.DictEnum.ProBackCategory, usersService.getLoginInfo().getCompany().getId());
        Date curDate = new Date();
        Calendar cd = Calendar.getInstance();
        if (StringUtils.isEmpty(type1)){
            type1 = "year";
        }

        //时间轴条目
        List<String> dateList = getPipeDateList(type1);
        //各时间节点option
        List<GsonOption> pieList = new ArrayList<GsonOption>();
        //返回的option
        GsonOption option = new GsonOption();

        if (StringUtils.equals(type1, "week")){
            pieList = queryPipeaByWeek(dateList, protypes);
        }else if (StringUtils.equals(type1, "month")){
            pieList = queryPipeaByMonth(protypes);
        }else{
            pieList = queryPipeaByYear(protypes);
        }

        option = getPipeOption(dateList,pieList);
      //  System.out.println(option.toString());
        return ajaxJson(option.toString());
    }

    //查询指定时间内的所有应考勤次数
    private Long getSignInCountForRange(String start, String end ,String proInfoId, String userId) throws ParseException {
        //返回的总次数
        Long count = new Long(0);

        //Calendar   calendar = Calendar.getInstance();
        //calendar.setTime(startDate);

        String sql = "SELECT id,chief_id,start,end FROM ec_proinfo WHERE start <= :date2 and (end is NULL or end >= :date1)  and company_id= :companyId";
        //循环查询每一天的项目
        //while (calendar.getTime().getTime() <= endDate.getTime()){
            //String thisString = sdf.format(calendar.getTime());
            Map<String,String> all_filter = new HashMap<String,String>();
            all_filter.put("date1", start);
            all_filter.put("date2", end);
            all_filter.put("companyId", usersService.getLoginInfo().getCompany().getId());
            if(StringUtils.isNotEmpty(proInfoId)){
                sql += " and id= :id";
                all_filter.put("id",proInfoId);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);

            List<Map<String, Object>> all_ListMap = jdbcDao.findForListMap(sql,all_filter);
            for (Map<String, Object> mp:all_ListMap){
                //查当前项目中有多少人
                Long thisPeople = 0l;
                //每项目的考勤点
                Long thisSign = 0l;
                //当前项目的有效天数
                Long thisdays = 0l;

                String sql_1 = "SELECT count(*) as count FROM ec_proinfo_group WHERE id= :id ";
                Map<String,String> filter_1 = new HashMap<String,String>();
                filter_1.put("id", mp.get("id").toString());
                if (StringUtils.isNotEmpty(userId)){
                    sql_1 += "and userId= :userId ";
                    filter_1.put("userId", userId);
                    List<Map<String, Object>> listMap_1 = jdbcDao.findForListMap(sql_1,filter_1);
                    if (StringUtils.equals(mp.get("chief_id").toString(), userId) || (Long)listMap_1.get(0).get("count") > 0){
                        thisPeople = new Long(1);
                    }
                } else {
                    List<Map<String, Object>> listMap_1 = jdbcDao.findForListMap(sql_1,filter_1);
                    if(null != listMap_1 && listMap_1.size()>0) {
                        Map lMap = listMap_1.get(0);
                        thisPeople = (Long)lMap.get("count")+1;
                    }
                }

                String sql_2 = "SELECT count(*) as count FROM ec_proattend WHERE proInfo_id= :proInfoId and state='Enable'";
                Map<String,String> filter_2 = new HashMap<String,String>();
                filter_2.put("proInfoId",mp.get("id").toString());
                List<Map<String, Object>> listMap_2 = jdbcDao.findForListMap(sql_2,filter_2);
                if (listMap_2 != null && listMap_2.size()>0){
                    Map lMap = listMap_2.get(0);
                    thisSign = (Long)lMap.get("count");
                }

                Date thisStart = sdf.parse(mp.get("start").toString());
                Date thisEnd = mp.get("end")!=null?sdf.parse(mp.get("end").toString()):endDate;
                if (thisStart.before(startDate)){
                    thisStart = startDate;
                }
                if (thisEnd.after(endDate)){
                    thisEnd = endDate;
                }
                thisdays = (thisEnd.getTime() - thisStart.getTime())/(1000 * 60 * 60 * 24);
                //当前项目人数乘以当前项目的考勤点数再乘以该项目在当前时间段力的有效天数，得到当前项目的应签到次数
                count += thisPeople * thisSign * thisdays;
            }


            //calendar.add(calendar.DATE,1);
        //}

        return count;
    }
    //查询指定时间内的考勤次数，依据参数类型查询正常和非正常次数
    private Long getSignInByType(String start, String end, String type,Map<String,String> mp){
        String sql = "SELECT count(*) as perCount FROM ec_prosignin WHERE createDate >= :date1 and createDate <= :date2 and company_id= :companyId and state='Enable' ";
        Long count = new Long(0);

        //正常或者异常
        if (StringUtils.equals(type, "normal")){
            sql += " and status= 1 ";
        } else if (StringUtils.equals(type, "error")){
            sql += " and status= 0 ";
        }

        //其他的查询条件，按人员或者按项目
        if (mp != null && mp.size() > 0){
            for (String key :mp.keySet()){
                sql += " and "+key+"='"+mp.get(key)+"' ";
            }
        }

        Map<String,String> filter = new HashMap<String,String>();
        filter.put("date1", start);
        filter.put("date2", end);
        filter.put("companyId", usersService.getLoginInfo().getCompany().getId());
        List<Map<String, Object>> tmpListMap = jdbcDao.findForListMap(sql,filter);
        if(null != tmpListMap && tmpListMap.size()>0) {
            Map lMap = tmpListMap.get(0);
            count = (Long)lMap.get("perCount");
        }

        return count;
    }


    private List<GsonOption> queryPipeb(List<String> dateList , String proInfoId, String userId, String type) throws ParseException {
        List<GsonOption> pieList = new ArrayList<GsonOption>();
        boolean isfirst = true;
        String[] names = new String[]{"正常签到","异常签到","未签"};
        Long[] nums = new Long[]{0l,0l,0l};
        for (String s:dateList){
            GsonOption gp = new GsonOption();
            Pie pie = new Pie();
            if (isfirst){
                gp=initFirstOption();
                pie.setCenter(new String[]{"50%","45%"});
                pie.setRadius("60%");
            }
            pie.setName(s);
            pie.setType(SeriesType.pie);

            Map<String,String> filter = new HashMap<String,String>();
            if (StringUtils.isNotEmpty(proInfoId)){
                filter.put("proInfo_id",proInfoId);
            }
            if(StringUtils.isNotEmpty(userId)){
                filter.put("creater_id",userId);
            }
            if (StringUtils.equals(type, "week")){
                nums[0] = getSignInByType(s,s,"normal",filter);
                nums[1] = getSignInByType(s,s,"error",filter);
                nums[2] = getSignInCountForRange(s,s,proInfoId, userId) - nums[0] - nums[1];
            }else if (StringUtils.equals(type, "month")){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cd = Calendar.getInstance();
                cd.set(Calendar.MONTH, Integer.parseInt(s)-1);
                cd.set(Calendar.DATE, 1);
                String start = format.format(cd.getTime());
                cd.roll(Calendar.DATE, -1);
                String end = format.format(cd.getTime());
                nums[0] = getSignInByType(start,end,"normal",filter);
                nums[1] = getSignInByType(start,end,"error",filter);
                nums[2] = getSignInCountForRange(start,end,proInfoId, userId) - nums[0] - nums[1];
            }else if (StringUtils.equals(type, "year")){
                String start = s + "-01-01";
                String end = s + "-12-31";
                nums[0] = getSignInByType(start,end,"normal",filter);
                nums[1] = getSignInByType(start,end,"error",filter);
                nums[2] = getSignInCountForRange(start,end,proInfoId, userId) - nums[0] - nums[1];
            }

            for (int i=0;i<nums.length;i++){
                PieEntity entity = new PieEntity();
                entity.setName(names[i]);
                entity.setValue(nums[i]);
                pie.data(entity);
            }
            gp.series(pie);

            if (isfirst){
                Legend legend = new Legend();
                for (int i=0;i<names.length;i++){
                    legend.data(names[i]);
                }
                gp.setLegend(legend);
                isfirst = false;
            }
            pieList.add(gp);
        }
        return pieList;
    }

    public String pipeb() throws ParseException {
        //System.out.println("type:"+type2+";proinfoId:"+pipeProInfoId+";userId:"+pipeUserId);

        if (StringUtils.isEmpty(type2)){
            type2 = "year";
        }

        //各时间节点option
        List<GsonOption> pieList = new ArrayList<GsonOption>();
        //返回的option
        GsonOption option = new GsonOption();

        List<String> dateList = getPipeDateList(type2);

        pieList = queryPipeb(dateList, pipeProInfoId, pipeUserId, type2);

        option = getPipeOption(dateList,pieList);
      //  System.out.println(option.toString());
        return ajaxJson(option.toString());
    }

    public List<ProInfo> getInfoByArea(String province){
        Map<String,Object> rmap= Maps.newHashMap();
        rmap.put("state", BaseEnum.StateEnum.Enable);
        rmap.put("province", province);
        List<ProInfo> proInfoList= (List<ProInfo>) proInfoService.findByPagerAndCompany(new Pager(0), null, usersService.getCompanyByUser(), rmap).getList();
        return proInfoList;
    }

    public String mapData(){
        Company company=usersService.getCompanyByUser();
        Map<String,Object> proInfomap;
        List<JSONObject> pdataRows= Lists.newArrayList();
        Map<String,Object> data= Maps.newHashMap();

        String[] province={"北京","天津","上海","重庆","河北","河南","云南","辽宁","黑龙江","湖南","安徽","山东","新疆",
        "江苏","浙江","江西","湖北","广西","甘肃","山西","内蒙古","陕西","吉林","福建","贵州","广东","青海","西藏","四川",
        "宁夏","海南","台湾","香港","澳门"};
        for(String area:province){
            List<ProInfo> areaList= getInfoByArea(area);
            proInfomap= Maps.newHashMap();
            proInfomap.put("name",area);
            if(areaList!=null && areaList.size()>0){
                proInfomap.put("value",areaList.size());
            }else{
                proInfomap.put("value",0);
            }
            pdataRows.add(JSONObject.fromObject(proInfomap));
        }
        data.put("dataRows",pdataRows);
        return  ajaxJson(JSONObject.fromObject(data).toString());
    }

    /**
     * 获得天气预报
     * @return
     */
    public Map<String,Object> getWeather(int day){
        String url = "http://php.weather.sina.com.cn/xml.php?city=%C9%CF%BA%A3&password=DJOYnieT8234jlsK&day="+day;
        String xml = NetWork.getUrlData(url) ;
        String arr[] = new String[]{"status1","temperature1"};
        Map<String,Object> map = XmlUtil.xmlElements(xml, arr);
        return map;
    }

    public List<Map<String, Object>> getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(List<Map<String, Object>> weatherData) {
        this.weatherData = weatherData;
    }


    public Map<String, Object> getBusinessData() {
        return businessData;
    }

    public void setBusinessData(Map<String, Object> businessData) {
        this.businessData = businessData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<ProInfo> getProInfoSet() {
        return proInfoSet;
    }

    public void setProInfoSet(Set<ProInfo> proInfoSet) {
        this.proInfoSet = proInfoSet;
    }

    public String getProInfoId() {
        return proInfoId;
    }

    public void setProInfoId(String proInfoId) {
        this.proInfoId = proInfoId;
    }

    public String getFirstType() {
        return firstType;
    }

    public void setFirstType(String firstType) {
        this.firstType = firstType;
    }

    public List<Long> getnData() {
        return nData;
    }

    public void setnData(List<Long> nData) {
        this.nData = nData;
    }

    public List<Long> getbData() {
        return bData;
    }

    public void setbData(List<Long> bData) {
        this.bData = bData;
    }

    public List<String> getDateStr1() {
        return dateStr1;
    }

    public void setDateStr1(List<String> dateStr1) {
        this.dateStr1 = dateStr1;
    }

    public List<String> getDateStr2() {
        return dateStr2;
    }

    public void setDateStr2(List<String> dateStr2) {
        this.dateStr2 = dateStr2;
    }

    public List<Object> getLabels() {
        return labels;
    }

    public void setLabels(List<Object> labels) {
        this.labels = labels;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getPipeProInfoId() {
        return pipeProInfoId;
    }

    public void setPipeProInfoId(String pipeProInfoId) {
        this.pipeProInfoId = pipeProInfoId;
    }

    public String getPipeUserId() {
        return pipeUserId;
    }

    public void setPipeUserId(String pipeUserId) {
        this.pipeUserId = pipeUserId;
    }

    public Set<Users> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<Users> userSet) {
        this.userSet = userSet;
    }
}
