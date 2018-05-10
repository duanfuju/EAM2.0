package com.tiansu.eam.modules.maintain.service;

import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.maintain.controller.MaintainProjectSubController;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectSub;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectSubContent;
import com.tiansu.eam.modules.maintain.entity.MaintainTask;
import com.tiansu.eam.modules.opestandard.service.StandardOrderService;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangww
 * @description  流程结束完相关信息入业务表
 * @create 2017-09-22 9:36
 **/
public class ActMaintainProjectSubAfter implements TaskListener {
    private Expression approveReason;
    private Expression deptLeaderApproved;
    private Expression pIid;

    private MaintainProjectSubService maintainProjectSubService;
    private MaintainTaskService maintainTaskService;
    private EamProcessService eamProcessService;//流程service

    @Transactional
    public void notify(DelegateTask delegateTask) {
        ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        maintainProjectSubService = (MaintainProjectSubService) context .getBean("maintainProjectSubService");
        maintainTaskService=(MaintainTaskService) context.getBean("maintainTaskService");
        eamProcessService=(EamProcessService)context.getBean("eamProcessService");
        String approve = (String) deptLeaderApproved.getValue(delegateTask);
        String piid=(String)pIid.getValue(delegateTask);
        String appReason=(String)approveReason.getValue(delegateTask);
        String createBy= UserUtils.getUser().getLoginname();
        Map<String,Object> map=new HashMap<String,Object>();
        if(approve !=null&& !"".equals(approve)){
            if(approve.equals("true")){
                map.put("project_status",2);

                //审批通过，生成保养任务
                MaintainProjectSub maintainProjectSub = maintainProjectSubService.getMaintByPIid(piid);
                List<MaintainTask> maintainTasks = new ArrayList<MaintainTask>();
                HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String stime=maintainProjectSub.getProject_stime();
                String endtime=maintainProjectSub.getProject_dtime();
                String cycle=maintainProjectSub.getProject_cycle();
                String period=maintainProjectSub.getProject_period();
                String code=maintainProjectSub.getProject_code();
                String name=maintainProjectSub.getProject_name();
                String project_id=maintainProjectSub.getId_key();
                Map param=(Map)new HashMap();
                param.put("project_id",project_id);
                List<Map> devices=maintainProjectSubService.findDeviceListByMap(param);
                List<MaintainProjectSubContent> mpcontent=maintainProjectSubService.findContentByMap(param);
                StringBuffer contentcode=new StringBuffer();
                StringBuffer conten=new StringBuffer();

                if (!"".equals(stime) && !"".equals(endtime)&& !"".equals(cycle) && !"".equals(period)&& devices.size()>0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    int codeindex=1;//给名称编码加下标
                    try {
                       Date start= sdf.parse(stime);
                        Date end =sdf.parse(endtime);
                       List<String> times= MaintainProjectSubController.getCycleDays(start,end,cycle,period,"day");
                       for(Map devmap:devices){
                           for(String time:times){
                               MaintainTask maintainTask=new MaintainTask();
                               maintainTask.preInsert();
                               for(MaintainProjectSubContent mpc:mpcontent){
                                   contentcode.append(mpc.getMaintain_code()+",");
                                   conten.append(mpc.getMaintain_content()+",");
                                   mpc.setProject_id(maintainTask.getId());
                               }
                               maintainTask.setProject_id(project_id);
                               maintainTask.setTask_code(code+"_"+codeindex);
                               maintainTask.setTask_name(name+(String)devmap.get("dev_name")+"_"+codeindex);
                               maintainTask.setTask_type(maintainProjectSub.getProject_type());
                               maintainTask.setTask_mode(maintainProjectSub.getProject_mode());
                               maintainTask.setTask_period(maintainProjectSub.getProject_period());
                               maintainTask.setProject_cycle(maintainProjectSub.getProject_cycle());
                               maintainTask.setTask_time_plan(time);
                               maintainTask.setIsoverdue("0");
                               maintainTask.setTask_status("0");//任务默认是0
                               maintainTask.setTask_act_processor_plan(maintainProjectSub.getProject_empid());
                               maintainTask.setTask_device((String)devmap.get("dev_id"));
                               maintainTask.setTask_maintain_code(contentcode.toString());//保养编码
                               maintainTask.setTask_maintain_content(conten.toString());//保养内容
                               String pstid = eamProcessService.startProcessByPdid("maintainFlow","eam_maintain_task",maintainTask.getId(),request);
                               maintainTask.setPstid(pstid);
                               codeindex++;
                               maintainTasks.add(maintainTask);
                           }
                       }
                       if(maintainTasks.size() !=0) {
                           maintainTaskService.insertTask(maintainTasks);
                           if(mpcontent.size() !=0){
                               maintainProjectSubService.insertTotaskContent(mpcontent);
                           }
                       }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }else{
                map.put("project_status",3);
            }
        }
        map.put("project_sp_empid",createBy);
        map.put("project_reason",appReason);
        map.put("pstid",piid);
        String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        map.put("project_sp_time",time);

        maintainProjectSubService.approveByPstid(map);

    }

}
