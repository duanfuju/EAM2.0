package com.tiansu.eam.modules.device.service;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.device.dao.EamDeviceDao;
import com.tiansu.eam.modules.device.entity.DevSpareParts;
import com.tiansu.eam.modules.device.entity.DevTools;
import com.tiansu.eam.modules.device.entity.Device;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 设备信息Service
 * Created by tiansu on 2017/8/8.
 */
@Service
public class EamDeviceService extends CrudService<EamDeviceDao, Device> {

    @Autowired
    private EamDeviceDao eamDeviceDao;
    @Autowired
    private EamDevLocService eamDevLocService;

    /**
     * 根据主键id获取设备信息
     *
     * @param id
     * @return
     */
    public Device get(String id) {
        Map map = new HashMap();
        map.put("id_key", id);
        map.put("dbName", Global.getConfig("jdbc.type"));
        return eamDeviceDao.get(map);
    }

    public Map dataTablePage(Device device) {
        return super.dataTablePage(device);
    }

    public Map dataTablePageMap(Map map) {
        return super.dataTablePageMap(map);
    }

    /**
     * 根据设备信息某些信息删除设备信息
     *
     * @param device
     */
    @Transactional(readOnly = false)
    public void delete(Device device) {
        super.delete(device);
    }

    @Transactional(readOnly = false)
    public void insert(Device device) {
        device.preInsert();
        super.insert(device);
    }

    /**
     * 查询当前编码在数据库中的数量
     *
     * @param code
     * @return
     */
    public int getBycode(String code) {
        Map<String, Object> map = eamDeviceDao.getByCode(code);
        return (int) map.get("cou");
    }

    /**
     * 更新设备信息
     *
     * @param device
     */
    public void update(Device device) {
        device.preUpdate();
        super.update(device);
    }

    /**
     * 根据设备id获取故障现象
     *
     * @return
     */
    public List<Map> getFaultAppearance(String devId) {
        return eamDeviceDao.getFaultAppearance(devId);
    }

    /**
     * 根据主键id获取要编辑的对象信息
     *
     * @return
     */
    public Map getEdit(String id) {
        Map map = new HashMap();
        map.put("id_key", id);
        map.put("dbName", Global.getConfig("jdbc.type"));
        return eamDeviceDao.getEdit(map);
    }

    /**
     * 查询设备列表
     *
     * @param device
     * @return
     */
    @Transactional(readOnly = false)
    public List<Device> findList(Device device) {
        return eamDeviceDao.findList(device);
    }

    public List<Map> findDevCategoryList(Map map) {
        return eamDeviceDao.findDevCategoryList(map);
    }

    public List<Map> findDeviceTreeList(Map map) {
        return eamDeviceDao.findDeviceTreeList(map);
    }

    @Transactional(readOnly = false)
    public Map<String, Object> delete(String[] ids) {
        Map<String, Object> returnMap = new HashMap();
        if (ids != null && ids.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                Device device = get(ids[i]);
                if ("1".equals(device.getDev_status())) {
                    returnMap.put("flag", false);
                    returnMap.put("msg", "此数据为有效数据不允许删除");
                    return returnMap;
                } else {
                    device.preUpdate();
                    device.setId_key(ids[i]);
                    eamDeviceDao.delete(device);
                    returnMap.put("flag", true);
                    returnMap.put("msg", "操作成功");

                }
            }
        }
        return returnMap;
    }

    /**
     * @param map 入参
     * @return
     * @creator tiansu
     * @createtime 2017/9/2 11:41
     * @description: 获取设备信息全部信息
     */
    public List<Map> findListByMap(Map map) {
        return eamDeviceDao.findListByMap(map);
    }

    /**
     * @param devLocation
     * @return
     * @creator wujh
     * @createtime 2017/9/14 17:21
     * @description: 根据空间信息id获取空间的基本信息，并取该空间信息下的设备信息
     */
    public Map getDevices(String devLocation) {
        List<Map> devices = new LinkedList<Map>();

        // 根据空间信息id获取空间信息
        Map map = eamDevLocService.findByIdForApp(devLocation);
        // 获取设备信息列表
        if (devLocation != null) {
            Device device1 = new Device();
            device1.setDev_location(devLocation);
            List<Device> deviceList = findList(device1);

            for (int i = 0; i < deviceList.size(); i++) {
                Device device = deviceList.get(i);
                Map param = new HashMap();
                param.put("dev_code", device.getId_key());
                param.put("dev_name", device.getDev_name());
                devices.add(param);
            }
            map.put("dev", devices);
        }
        return map;
    }

    /**
     * @param param 设备id、位置id
     * @return
     * @creator shufq
     * @createtime 2017/10/19 17:21
     * @description: 获取故障编码
     */
    public Map<String, Object> getAllLoc(Map param) {
        List<Map> Loc = new LinkedList<Map>();
        Map<String, Object> maps = new HashMap();
        while (true) {
            Map map = eamDeviceDao.getLoc(param);
            if (map == null) {
                break;
            }
            Loc.add(map);
            param.put("dev_Loc_id", map.get("loc_pid"));
        }
        maps.put("Loc", Loc);
        return maps;
    }

    /**
     * @param param 设备id、位置id
     * @return
     * @creator shufq
     * @createtime 2017/10/19 17:21
     * @description: 获取故障编码
     */
    public void updateQrcode(Map param) {
        eamDeviceDao.updateQrcode(param);
    }

    /**
     * @param devSparePartsList 备品备件列表
     * @param devToolsList      工器具列表
     * @creator wujh
     * @createtime 2017/9/20 14:43
     * @description: 分别给备品备件/工器具插入新增数据
     */
    public void insertDetail(List<DevSpareParts> devSparePartsList, List<DevTools> devToolsList) {
        if (devSparePartsList != null && devSparePartsList.size() != 0) {
            eamDeviceDao.insertDevSpareparts(devSparePartsList);
        }
        if (devToolsList != null && devToolsList.size() != 0) {
            eamDeviceDao.insertDevTools(devToolsList);
        }
    }

    /**
     * @param device_id
     * @creator wujh
     * @createtime 2017/9/21 14:07
     * @description: 根据设备信息主键删除其关联子表数据
     */
    public void deleDetail(String device_id) {
        eamDeviceDao.deleteSpareparts(device_id);  //删除该设备下的备品备件
        eamDeviceDao.deleteTools(device_id);  //删除工器具
    }

    /**
     * 根据设备Id获取该设备下的备品备件和工器具信息
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getMaterials(Map param) {
        return eamDeviceDao.getMaterials(param);
    }


    /**
     * @creator wujh
     * @createtime 2017/12/07
     * @description:
     *  重写列表排序功能
     * @param page
     * @param request
     * @return
     */
    @Override
    public Page setOrderBy(Page page, HttpServletRequest request){
        Enumeration<String> en = request.getParameterNames();
        while (en.hasMoreElements()) {
            String nms = en.nextElement().toString();
            if(nms.startsWith("order") && nms.endsWith("[column]")){
                String column = request.getParameterValues(nms)[0];
                String columnname = request.getParameterValues("columns["+column+"][data]")[0];
                if("0".equals(columnname)||"1".equals(columnname)){
                    return setDefaultOrderBy(page,request);
                }else if(columnname.equals("cat_name")){
                    columnname = "dev_category";
                }
                String orderby = request.getParameterValues("order[0][dir]")[0];
                page.setOrderBy(columnname+" "+orderby);
                break;
            }
        }
        return page;
    }
}
