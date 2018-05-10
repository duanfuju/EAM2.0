package com.tiansu.eam.modules.act.utils;

import com.tiansu.eam.modules.sys.entity.Role;
import com.tiansu.eam.modules.sys.entity.User;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @creator caoh
 * @createtime 2017-9-8 16:46
 * @description: 汉化流程图渲染信息
 */
public class ActivitiUtil {

    public static Map<String, String> ACTIVITY_TYPE = new HashMap<String, String>();

    static {
        ACTIVITY_TYPE.put("userTask", "用户任务");
        ACTIVITY_TYPE.put("serviceTask", "系统任务");
        ACTIVITY_TYPE.put("startEvent", "开始节点");
        ACTIVITY_TYPE.put("endEvent", "结束节点");
        ACTIVITY_TYPE.put("exclusiveGateway", "条件判断节点(系统自动根据条件处理)");
        ACTIVITY_TYPE.put("inclusiveGateway", "并行处理任务");
        ACTIVITY_TYPE.put("callActivity", "调用活动");
        ACTIVITY_TYPE.put("subProcess", "子流程");
    }

    /**
     * 根据英文获取中文类型
     *
     * @param type
     * @return
     */
    public static String getZhActivityType(String type) {
        return ACTIVITY_TYPE.get(type) == null ? type : ACTIVITY_TYPE.get(type);
    }


    public static UserEntity toActivitiUser(User user){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getLoginname().toString());
        userEntity.setFirstName(user.getRealname());
        //userEntity.setLastName(bUser.getLoginName());
        userEntity.setPassword(user.getUserpwd());
        userEntity.setEmail(user.getLoginemail());
        userEntity.setRevision(1);
        return userEntity;
    }

    public static GroupEntity toActivitiGroup(Role role){
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setRevision(1);
        groupEntity.setType("assignment");

        groupEntity.setId(role.getId());
        groupEntity.setName(role.getRolename());
        return groupEntity;
    }

    public static List<Group> toActivitiGroups(List<Role> roles){

        List<Group> groupEntitys = new ArrayList<Group>();

        for (Role role : roles) {
            GroupEntity groupEntity = toActivitiGroup(role);
            groupEntitys.add(groupEntity);
        }
        return groupEntitys;
    }

}
