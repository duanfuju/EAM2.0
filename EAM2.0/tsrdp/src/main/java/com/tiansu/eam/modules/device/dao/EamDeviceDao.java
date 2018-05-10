package com.tiansu.eam.modules.device.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.device.entity.DevSpareParts;
import com.tiansu.eam.modules.device.entity.DevTools;
import com.tiansu.eam.modules.device.entity.Device;

import java.util.List;
import java.util.Map;

/**
 * Created by tiansu on 2017/8/8.
 */
@MyBatisDao
public interface EamDeviceDao extends CrudDao<Device> {

/**
*@Create
*@Description :根据id获得相应的设备名称
*@Param :  * @param null
*@author : suven
*@Date : 8:52 2017/11/13
*/

public String getDev_name(String id);
    /**
     * 根据实体对象删除符合条件记录
     * @param device
     * @return
     */
    public int delete(Device device);

    /**
     * 根据设备id获取故障现象
     * @param devId
     * @return
     */
    public List<Map> getFaultAppearance(String devId);

    /**
     * 根据主键Id获取设备信息
     * @param map
     * @return
     */
    public Map<String,Object> getEdit(Map map);
    public Device get(Map map);

    public List<Map> findDevCategoryList(Map map);
    public List<Map> findDeviceTreeList(Map map);

    /**
     * 查询当前编码在数据库中的数量
     * @param code
     * @return
     */
    public Map<String,Object> getByCode(String code);

    /**
     * 批量导入
     * @param list
     * @return
     */
    public int insertBatch(List<Device> list);//用于批量导入

    // 备品备件插入
    public int insertDevSpareparts(List<DevSpareParts> devSparePartsList);

    // 工器具插入
    public int insertDevTools(List<DevTools> devToolsList);

    /**
     * 根据设备信息主键删除其关联子表数据
     * @param device_id
     * @return
     */
    public int deleteSpareparts(String device_id);
    public int deleteTools(String device_id);

    public List<Map<String,Object>> getMaterials(Map param);
    /**
     * 根据位置信息获取其级联位置
     * @param param
     * @return
     */
    Map getLoc(Map param);

    public void  updateQrcode(Map param);
    /**
    *@Create
    *@Description :通过工单故障报修id查询设备专业
    *@Param :  * @param null
    *@author : suven
    *@Date : 18:49 2017/11/28
    */
    public String getDevMajorByFaultOrderId(String business_key);
}
