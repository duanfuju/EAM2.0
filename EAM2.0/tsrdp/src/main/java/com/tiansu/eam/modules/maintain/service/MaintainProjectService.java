package com.tiansu.eam.modules.maintain.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.maintain.controller.MaintainProjectSubController;
import com.tiansu.eam.modules.maintain.dao.MaintainProjectDao;
import com.tiansu.eam.modules.maintain.entity.MaintainProject;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectContent;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectDevice;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @creator duanfuju
 * @createtime 2017/11/2 15:51
 * @description:
 * 		保养年计划 service
 */
@Service
@Transactional(readOnly = true)
public class MaintainProjectService extends CrudService<MaintainProjectDao, MaintainProject> {

	@Autowired
	private MaintainProjectDao maintainProjectDao;

	@Autowired
	private EamProcessService eamProcessService;//流程


	/**
	 * @creator duanfuju
	 * @createtime 2017/11/14 17:11
	 * @description:
	 * 审批（流程启动中）
	 * @param map
	 * @return
	 */
	public int approvalsp(Map map){
		return maintainProjectDao.approvalsp(map);
	}
	/**
	 * @creator duanfuju
	 * @createtime 2017/11/14 11:13
	 * @description:
	 * 获取导出的数据
	 * @param map
	 * @return
	 */
	public List<Map> getExportData(Map map){
		List<Map> result =maintainProjectDao.getExportData(map);
		for (int i = 0; i < result.size(); i++) {
			try{
				Map m=result.get(i);
				Map param = new HashMap();
				param.put("project_id",m.get("id_key"));
				List<Map> devices= findDeviceListByMap(param);//设备编码和设备名称
				if(devices.size()>0){
					String str_dev_code="";
					String str_dev_name="";
					for (int j = 0; j < devices.size(); j++) {
						Map device=devices.get(j);
						str_dev_code+=device.get("dev_code").toString()+",";
						str_dev_name+=device.get("dev_name").toString()+",";
					}
					str_dev_code=str_dev_code.substring(0,str_dev_code.length()-1);
					str_dev_name=str_dev_name.substring(0,str_dev_name.length()-1);
					m.put("dev_code",str_dev_code);
					m.put("dev_name",str_dev_name);
				}
				//保养周期
				String[] strs=m.get("project_cycle").toString().split("_");
				if(m.get("project_period").toString().equals("天")){
					m.put("project_period","每"+strs[0]+"天");
				}else if(m.get("project_period").toString().equals("周")){
					m.put("project_period","每"+strs[0]+"周的周"+strs[1]);
				}else if(m.get("project_period").toString().equals("月")){
					m.put("project_period","每"+strs[0]+"月的第"+strs[1]+"周的周"+strs[2]);
				}else if(m.get("project_period").toString().equals("季")){
					m.put("project_period","每"+strs[0]+"季的第"+strs[1]+"月的第"+strs[2]+"周的周"+strs[3]);
				}else if(m.get("project_period").toString().equals("年")){
					m.put("project_period","每"+strs[0]+"年的第"+strs[1]+"月的第"+strs[2]+"周的周"+strs[3]);
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return  result;
	}
	/**
	 * @creator duanfuju
	 * @createtime 2017/11/13 15:17
	 * @description:
	 * 		修改
	 * @param maintainProject
	 */
	public void save(MaintainProject maintainProject){
		//更新保养年计划
		maintainProject.preUpdate();
		maintainProjectDao.update(maintainProject);
		//删除所有和保养年计划关联的设备和内容
		Map map = new HashMap();
		map.put("project_id",maintainProject.getId_key());
		deleteContent(map);
		deleteDevice(map);
		//新建关联
		List<MaintainProjectDevice> devices=new ArrayList<>();//关联设备
		if (maintainProject.getDev_id()!=null) {
			String[] arr_devices = maintainProject.getDev_id().split(",");
			int devicesmSize = arr_devices.length;
			for (int i = 0; i < devicesmSize; i++) {
				MaintainProjectDevice device = new MaintainProjectDevice();
				device.setDev_id(arr_devices[i]);
				device.setProject_id(maintainProject.getId_key());
				device.preInsert();
				devices.add(device);
				if ((i > 0 && i % 500 == 0) || i == devicesmSize - 1) {//分段提交；一次的参数量为2000，
					insertBatchDevice(devices);
					devices = new ArrayList<>();
				}
			}
		}
		if(maintainProject.getMaintainProjectContents()!=null){
			List<MaintainProjectContent> arrcontents=maintainProject.getMaintainProjectContents();//关联内容
			List<MaintainProjectContent> contents=new ArrayList<>();
			int contentsmSize = arrcontents.size();
			for (int i = 0; i < contentsmSize; i++) {
				MaintainProjectContent content = arrcontents.get(i);
				content.setProject_id(maintainProject.getId_key());
				content.preInsert();
				contents.add(content);
				if((i > 0 && i % 500 == 0) || i == contentsmSize - 1){//分段提交；一次的参数量为2000，
					insertBatchContent(contents);
					contents=new ArrayList<>();
				}
			}
		}
	}
    /**
     * @creator duanfuju
	 * @createtime 2017/11/8 11:32
	 * @description: 
	 *      保养设置生成保养年计划
     * @param year
     */
	public String createProjectData(String year){
		String code ="norecord";//#17925 liwenlong
		List<Map> devicesm=new ArrayList<>();//关联设备
		List<Map> contentsm=new ArrayList<>();//关联内容
        List<Map> getNeedInsertProject=getNeedInsertProject(year);
        //保养年计划数据新增
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i <getNeedInsertProject.size(); i++) {
            Map map = getNeedInsertProject.get(i);
			String stime=map.get("project_stime").toString();//设备启用时间
			String dtime=year+"-12-31";//获取当年最后一天
			String cycle=(String)map.get("project_cycle");
			String period=(String)map.get("project_period");
			try {
				List<String> yeardate= MaintainProjectSubController.getCycleDays(sdff.parse(stime),sdff.parse(dtime),cycle,period,"year");//获取所有时间到年
				if(yeardate.contains(year)) {//是否存在当年的保养计划
					MaintainProject maintainProject = mapToBean(map, new MaintainProject());
					maintainProject.setProject_code("N-" + maintainProject.getProject_code());
					maintainProject.setProject_name(maintainProject.getProject_code() + "年计划");
					maintainProject.setStatus("1");
					maintainProject.setProject_status("0");
					maintainProject.setProject_cycleyear(year);
					maintainProject.setProject_produceyear(year);
					maintainProject.setProjectset_id((String)map.get("id"));
					maintainProject.preInsert();//赋值id,创建时间,创建人
					//projectId获取关联设备和内容数据
					map.put("project_id", map.get("id"));
					devicesm.addAll(getNeedInsertDevicesByProjectIds(map,maintainProject.getId().toString()));
					contentsm.addAll(getNeedInsertContentsByProjectIds(map,maintainProject.getId().toString()));
					maintainProjectDao.insert(maintainProject);
					code ="success";
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }
		List<MaintainProjectDevice> devices=new ArrayList<>();//关联设备
		int devicesmSize = devicesm.size();
		for (int i = 0; i < devicesmSize; i++) {
			Map map = devicesm.get(i);
			MaintainProjectDevice device =mapToBean(map,new MaintainProjectDevice());
			device.preInsert();
			devices.add(device);
			if((i > 0 && i % 500 == 0) || i == devicesmSize - 1){//分段提交；一次的参数量为2000，
				insertBatchDevice(devices);
				devices=new ArrayList<>();
			}
		}
		List<MaintainProjectContent> contents=new ArrayList<>();//关联内容
		int contentsmSize = contentsm.size();
		for (int i = 0; i < contentsmSize; i++) {
			Map map = contentsm.get(i);
			MaintainProjectContent content =mapToBean(map,new MaintainProjectContent());
			content.preInsert();
			contents.add(content);
			if((i > 0 && i % 500 == 0) || i == contentsmSize - 1){//分段提交；一次的参数量为2000，
				insertBatchContent(contents);
				contents=new ArrayList<>();
			}
		}
		return code;
	}
    /**
     * 将map装换为javabean对象
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map,T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * @creator duanfuju
     * @createtime 2017/11/7 17:08
     * @description: 
     *  根据年份获取保养设置的数据
     * @param year
     * @return
     */
    public List<Map> getNeedInsertProject(String year){
        Map map = new HashMap();
        map.put("year",year);
        return  maintainProjectDao.getNeedInsertProject(map);
    }

	/**
	 * @creator duanfuju
	 * @createtime 2017/11/7 17:31
	 * @description:
	 *      根据保养设置id获取关联设备
	 * @return
	 */
	public List<Map> getNeedInsertDevicesByProjectIds(Map map,String project_id){
		List<Map> result =	maintainProjectDao.getNeedInsertDevicesByProjectIds(map);
		for (int i = 0; i < result.size(); i++) {
			Map m = result.get(i);
			m.put("project_id",project_id);
		}
		return result;
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/11/7 17:31
	 * @description:
	 *      根据保养设置id获取关联内容
	 * @return
	 */
	public List<Map> getNeedInsertContentsByProjectIds(Map map,String project_id){
		List<Map> result =	maintainProjectDao.getNeedInsertContentsByProjectIds(map);
		for (int i = 0; i < result.size(); i++) {
			Map m = result.get(i);
			m.put("project_id",project_id);
		}
		return result;
	}
    /**
     * @creator duanfuju
     * @createtime 2017/11/7 16:48
     * @description:
     *      批量新增
     * @param maintainProjects
     * @return
     */
    public void  insertBatchProject(List<MaintainProject> maintainProjects){
        maintainProjectDao.insertBatchProject(maintainProjects);
    }

	/**
	 * @creator duanfuju
	 * @createtime 2017/11/7 11:11
	 * @description:
	 * 审批功能
	 * @param map
	 * @return
	 */
	public int approval(Map map,HttpServletRequest request){
		User user = UserUtils.getUser();
		map.put("loginname",user.getLoginname());
		map.put("updateDate",new Date());
		if(map.containsKey("project_status")&&(!map.get("project_status").toString().equals("1"))){
			map.put("project_sp_empid",user.getLoginname());
			map.put("project_sp_time",user.getUpdateDate());
		}
		if(map.containsKey("ids")){//选中多个
			String[] id_arr=map.get("ids").toString().split(",");
			for (int i = 0; i <id_arr.length ; i++) {
				map.put("id",id_arr[i]);
				String pstid = eamProcessService.startProcessByPdid("maintAnnual_approve","test_material",id_arr[i],request);
				map.put("pstid",pstid);
				maintainProjectDao.approval(map);
			}
		}else{//全部
			Map param = new HashMap();
			param.put("project_status","0");
			param.put("status","1");
			List<Map> result=maintainProjectDao.findListByMap(param);
			for (int i = 0; i <result.size() ; i++) {
				Map m =result.get(i);
				map.put("id",m.get("id_key").toString());
				String pstid = eamProcessService.startProcessByPdid("maintAnnual_approve","test_material",m.get("id_key").toString(),request);
				map.put("pstid",pstid);
				maintainProjectDao.approval(map);
			}
		}
		return 1;
	}
	/**
	 * @creator duanfuju
	 * @createtime 2017/11/2 15:29
	 * @description:
	 * 列表查询
	 * @param map
	 * @return
	 */
	public List<Map> findListByMap(Map map){
		List<Map> results = maintainProjectDao.findListByMap(map);
		for (int i = 0; i < results.size(); i++) {
			String id_key=map.get("id_key").toString();
			Map param = new HashedMap();
			param.put("project_id",id_key);
			List<Map> contents = findContentListByMap(param);
			List<Map> devices = findContentListByMap(param);
			map.put("contentList",contents);
			map.put("devicesList",devices);
			String c="";
			String d="";
			for (int j = 0; j <contents.size(); j++) {
				Map m=contents.get(j);
				c+=m.get("maintain_content").toString()+",";
			}
			for (int j = 0; j <devices.size(); j++) {
				Map m=devices.get(j);
				d+=m.get("device")+",";
			}
			map.put("contentStr",c);
			map.put("devicesStr",d);
		}
		return  results;
	}

	/**
	 *@creator duanfuju
	 * @createtime 2017/11/2 15:29
	 * @description:
	 * 根据id获取
	 * @param id
	 * @return
	 */
	public Map findById(String id){
		Map map = maintainProjectDao.findById(id);
		String id_key=map.get("id_key").toString();
		Map param = new HashedMap();
		param.put("project_id",id_key);
		map.put("contentList",findContentListByMap(param));
		List<Map> devices= findDeviceListByMap(param);
		map.put("devicesList",devices);
		if(devices.size()>0){
			String str_dev_id="";
			String str_dev_name="";
			for (int i = 0; i < devices.size(); i++) {
				Map device=devices.get(i);
				str_dev_id+=device.get("dev_id").toString()+",";
				str_dev_name+=device.get("dev_name").toString()+",";
			}
			str_dev_id=str_dev_id.substring(0,str_dev_id.length()-1);
			str_dev_name=str_dev_name.substring(0,str_dev_name.length()-1);
			map.put("dev_id",str_dev_id);
			map.put("dev_name",str_dev_name);
		}
		return  map;
	}


	/**
	 * @creator duanfuju
	 * @createtime 2017/11/15 8:58
	 * @description:
	 * 根据流程实例id获取数据
	 * @param pstid
	 * @return
	 */
	public Map findByPstid(String pstid){
		Map map = maintainProjectDao.findByPstid(pstid);
		String id_key=map.get("id_key").toString();
		Map param = new HashedMap();
		param.put("project_id",id_key);
		map.put("contentList",findContentListByMap(param));
		List<Map> devices= findDeviceListByMap(param);
		map.put("devicesList",devices);
		if(devices.size()>0){
			String str_dev_id="";
			String str_dev_name="";
			for (int i = 0; i < devices.size(); i++) {
				Map device=devices.get(i);
				str_dev_id+=device.get("dev_id").toString()+",";
				str_dev_name+=device.get("dev_name").toString()+",";
			}
			str_dev_id=str_dev_id.substring(0,str_dev_id.length()-1);
			str_dev_name=str_dev_name.substring(0,str_dev_name.length()-1);
			map.put("dev_id",str_dev_id);
			map.put("dev_name",str_dev_name);
		}
		return  map;
	}
	/**
	 * @creator duanfuju
	 * @createtime 2017/11/2 15:48
	 * @description:
	 *  根据年计划的id获取保养内容
	 * @param map
	 * @return
	 */
	public List<Map> findContentListByMap(Map map){
		return  maintainProjectDao.findContentListByMap(map);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/11/2 15:49
	 * @description:
	 *  根据年计划的id获取保养设备
	 * @param map
	 * @return
	 */
	public List<Map> findDeviceListByMap(Map map){
		return  maintainProjectDao.findDeviceListByMap(map);
	}

	/**
	 *@creator duanfuju
	 * @createtime 2017/11/2 15:29
	 * @description:
	 * 根据id删除（逻辑）
	 * @param id
	 * @return
	 */
	public int delete(String id){
		Map map = new HashMap();
		String ids[] = id.split(",");
		map.put("ids",ids);
		map.put("updateBy", UserUtils.getUser().getLoginname());
		map.put("updateDate",new Date());

		//先删除关系表的数据(外建表)
		for (int i = 0; i <ids.length ; i++) {
			map.put("project_id",ids[i]);
			deleteContent(map);
			deleteDevice(map);
		}

		return  maintainProjectDao.delete(map);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/11/2 15:42
	 * @description:
	 *      删除保养内容（物理）
	 * @param map
	 * @return
	 */
	public int deleteContent(Map map){
		return  maintainProjectDao.deleteContent(map);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/11/2 15:43
	 * @description:
	 *      删除关联设备（物理）
	 * @param map
	 * @return
	 */
	public int deleteDevice(Map map){
		return  maintainProjectDao.deleteDevice(map);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/11/2 15:45
	 * @description:
	 *  批量新增保养内容
	 * @param maintainProjectContents
	 * @return
	 */
	public int insertBatchContent(List<MaintainProjectContent> maintainProjectContents){
		return maintainProjectDao.insertBatchContent(maintainProjectContents);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/11/2 15:46
	 * @description:
	 *  批量新增保养设备
	 * @param maintainProjectDevices
	 * @return
	 */
	public int insertBatchDevice(List<MaintainProjectDevice> maintainProjectDevices){
		return  maintainProjectDao.insertBatchDevice(maintainProjectDevices);
	}

}