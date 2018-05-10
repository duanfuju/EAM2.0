package com.tiansu.eam.modules.inspection.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.inspection.dao.InspectionAreaDao;
import com.tiansu.eam.modules.inspection.dao.InspectionSubjectDao;
import com.tiansu.eam.modules.inspection.entity.InspectionArea;
import com.tiansu.eam.modules.inspection.entity.InspectionAreaSubject;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @creator duanfuju
 * @createtime 2017/10/23 14:08
 * @description:
 * 		巡检区域services
 */
@Service
@Transactional(readOnly = true)
public class InspectionAreaService extends CrudService<InspectionAreaDao, InspectionArea> {

	@Autowired
	private InspectionAreaDao inspectionAreaDao;

    @Autowired
    private InspectionSubjectDao inspectionSubjectDao;

	/**
	 * @creator duanfuju
	 * @createtime 2017/10/23 11:42
	 * @description:
	 * 列表查询
	 * @param map
	 * @return
	 */
	public List<Map> findListByMap(Map map) {
		return inspectionAreaDao.findListByMap(map);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/10/23 11:42
	 * @description:
	 * 根据id获取
	 * @param id
	 * @return
	 */
	public Map findById(String id) {
	    Map map = new HashedMap();
	    Map obj = inspectionAreaDao.findById(id);
	    map.put("obj",obj);
	    if (obj.containsKey("area_subject_ids")){
            String area_subject_ids=obj.get("area_subject_ids").toString();
            if (area_subject_ids.length()>0) {
                String[] area_subject_ids_arr=area_subject_ids.split(",");
                Map param = new HashedMap();
                param.put("ids",area_subject_ids_arr);
                List<Map> subjects=inspectionSubjectDao.findListByMap(param);
                map.put("subjects",subjects);
            }
        }
		return map;
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/8/28 9:03
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
		deteleBatchAreaAndSubjectByAreaId(map);
		return  inspectionAreaDao.delete(map);
	}


	/**
	 *@creator duanfuju
	 * @createtime 2017/10/23 11:20
	 * @description:
	 *批量插入(巡检区域)
	 * @return
	 */
	public int insertBatch(List<InspectionArea> inspectionAreas){
		return  inspectionAreaDao.insertBatch(inspectionAreas);
	}

	/**
	 *@creator duanfuju
	 * @createtime 2017/10/23 11:20
	 * @description:
	 *批量插入(巡检区域和巡检项的关联表)
	 * @return
	 */
	public int insertBatchAreaAndSubject(List<InspectionAreaSubject> inspectionAreaSubjects){
		return  inspectionAreaDao.insertBatchAreaAndSubject(inspectionAreaSubjects);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/10/23 15:16
	 * @description:
	 * 		 根据id删除（物理删除，检区域和巡检项的关联表)）
	 * @param map
	 * @return
	 */
	public  int deteleBatchAreaAndSubjectByAreaId(Map map){
		return  inspectionAreaDao.deteleBatchAreaAndSubjectByAreaId(map);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/10/24 17:13
	 * @description:
	 * 		保存
	 * @param inspectionAreas
	 * @return
	 */
	public int insertAndUpdate(InspectionArea inspectionAreas) {
		int res = -1;
		if(inspectionAreas.getId_key().length()>0){//存在主键的，更新数据
			//先删除关联表的关系
			Map map = new HashedMap();
			String ids[] = inspectionAreas.getId_key().split(",");
			map.put("ids",ids);
			deteleBatchAreaAndSubjectByAreaId(map);
			inspectionAreas.preUpdate();
			inspectionAreaDao.update(inspectionAreas);
			inspectionAreas.setId(inspectionAreas.getId_key());
		}else{//新增数据
			inspectionAreas.preInsert();
			inspectionAreaDao.insert(inspectionAreas);
		}
		//批量新增关系表
		inspectionAreas.getInspectionAreaSubjectsList();
		List inspectionAreaSubjects=inspectionAreas.getInspectionAreaSubjects();
		if(inspectionAreaSubjects.size()>0){
			inspectionAreaDao.insertBatchAreaAndSubject(inspectionAreaSubjects);
		}
		return res;
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/11/10 16:20
	 * @description:
	 * 		列表参数查询
	 * @param param
	 * @return
	 */
	public Map dataTablePageMap(Map param) {
		Map<String,Object> result=super.dataTablePageMap(param);
		List<Map> data=(List<Map>)result.get("data");
		for (int i = 0; i <data.size() ; i++) {
			Map m = data.get(i);
			if(m.containsKey("area_device_location_names")) {
				String[] area_device_location_names_arr = m.get("area_device_location_names").toString().split(",");
				Set<String> set = new HashSet<>();
				for (int j = 0; j < area_device_location_names_arr.length; j++) {
					set.add(area_device_location_names_arr[j]);
				}
				String[] arrayResult = set.toArray(new String[set.size()]);
				m.put("area_device_location_names",arrayToString(arrayResult));
			}
			if(m.containsKey("area_device_names")){
				String[] area_device_names_arr = m.get("area_device_names").toString().split(",");
				Set<String> set1 = new HashSet<>();
				for (int j = 0; j < area_device_names_arr.length; j++) {
					set1.add(area_device_names_arr[j]);
				}
				String[] arrayResult1 = set1.toArray(new String[set1.size()]);
				m.put("area_device_names",arrayToString(arrayResult1));
			}


		}
		return result;
	}
	public String arrayToString(String[] array){
		String result="";
		if(array.length>0){
			for (int i = 0; i < array.length; i++) {
				result+= array[i]+",";
			}
			result=result.substring(0,result.length()-1);
		}

		return  result;
	}
}