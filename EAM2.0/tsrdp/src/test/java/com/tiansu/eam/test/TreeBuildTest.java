package com.tiansu.eam.test;

import com.tiansu.eam.modules.sys.entity.Dept;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjl on 2017/7/26.
 */
public class TreeBuildTest {

    public static void main(String args[]){
        String curdeptid = "1";
        List<Dept> result = new ArrayList<>();

        List<Dept> list = initData();

        List<Dept> childMenu=new ArrayList<Dept>();

        result = treeMenuList(list,curdeptid,childMenu);
        System.out.println(result);

    }

    public static List<Dept> treeMenuList( List<Dept> menuList, String pid,List<Dept> childMenu) {
        for (Dept mu : menuList) {
            //遍历出父id等于参数的id，add进子节点集合
            if (mu.getParentid().equals(pid)) {
                //递归遍历下一级
                treeMenuList(menuList, mu.getDeptno(), childMenu);
                childMenu.add(mu);
            }
        }
        return childMenu;
    }
    private static List<Dept> initData(){
        List<Dept> list = new ArrayList<>();
        Dept d1 = new Dept();
        d1.setDeptno("1");
        d1.setDeptname("1");
        d1.setParentid("0");
        list.add(d1);


        Dept d3 = new Dept();
        d3.setDeptno("1.2");
        d3.setDeptname("1.2");
        d3.setParentid("1");
        list.add(d3);

        Dept d4 = new Dept();
        d4.setDeptno("1.1.1");
        d4.setDeptname("1.1.1");
        d4.setParentid("1.1");
        list.add(d4);

        Dept d2 = new Dept();
        d2.setDeptno("1.1");
        d2.setDeptname("1.1");
        d2.setParentid("1");
        list.add(d2);

        return list;
    }
}
