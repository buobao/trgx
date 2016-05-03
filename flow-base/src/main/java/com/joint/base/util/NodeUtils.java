package com.joint.base.util;

import com.google.common.collect.Lists;
import com.joint.base.entity.Department;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by amin on 2015/4/22.
 */
public class NodeUtils {
    private static List<String> returnList = Lists.newArrayList();

    /**
     * 根据父节点的ID获取所有子节点
     * @param list 分类表
     * @return String
     */
    public static List<String> getChildNodes(List<Department> list,String id) {
        if(list == null) return null;
        for (Iterator<Department> iterator = list.iterator(); iterator.hasNext();) {
            Department department = (Department) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (department.getParent()==null && StringUtils.equals(id, department.getId())) {
                recursionFn(list, department);
            }
        }
        returnList.remove(id);
        return returnList;
    }

    public static void recursionFn(List<Department> list, Department department) {
        List<Department> childList = getChildList(list, department);// 得到子节点列表
        if (hasChild(list, department)) {// 判断是否有子节点
            returnList.add(department.getId());
            Iterator<Department> it = childList.iterator();
            while (it.hasNext()) {
                Department n = (Department) it.next();
                recursionFn(list, n);
            }
        } else {
            returnList.add(department.getId());
        }
    }

    // 得到子节点列表
    public static List<Department> getChildList(List<Department> list, Department department) {
        List<Department> dpartmentList = new ArrayList<Department>();
        Iterator<Department> it = list.iterator();
        while (it.hasNext()) {
            Department n = (Department) it.next();
            if (n!=null && n.getParent()!=null && n.getParent().getId() == department.getId()) {
                dpartmentList.add(n);
            }
        }
        return dpartmentList;
    }

    // 判断是否有子节点
    public static boolean hasChild(List<Department> list, Department Department) {
        return getChildList(list, Department).size() > 0 ? true : false;
    }

}
