
package com.tiansu.eam.modules.inspection.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * @creator duanfuju
 * @createtime 2017/10/23 11:15
 * @description:
 *  巡检区域entity
 */
public class InspectionArea extends DataEntity<InspectionArea> {

        private String id_key;//主键
        private String area_code;//巡检区域编码
        private String area_name;//巡检区域名称
        private String area_remark;//备注
        private String area_qrcode_status;//二维码状态
        private String area_status;//状态
        private String area_device_ids;//设备ids
        private String area_device_names;//设备名称
        private String area_device_location_ids;//位置ids
        private String area_device_location_names;//位置名称
        private String area_subject_ids;//巡检项ids
        private String area_subject_names;//巡检项称
        private List<InspectionAreaSubject> inspectionAreaSubjects=new ArrayList<>();

        public String getId_key() {
            return id_key;
        }

        public void setId_key(String id_key) {
            this.id_key = id_key;
        }

        public String getArea_code() {
            return area_code;
        }

        public void setArea_code(String area_code) {
            this.area_code = area_code;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getArea_remark() {
            return area_remark;
        }

        public void setArea_remark(String area_remark) {
            this.area_remark = area_remark;
        }

        public String getArea_qrcode_status() {
            return area_qrcode_status;
        }

        public void setArea_qrcode_status(String area_qrcode_status) {
            this.area_qrcode_status = area_qrcode_status;
        }

        public String getArea_status() {
            return area_status;
        }

        public void setArea_status(String area_status) {
            this.area_status = area_status;
        }

        public String getArea_device_ids() {
            return area_device_ids;
        }

        public void setArea_device_ids(String area_device_ids) {
            this.area_device_ids = area_device_ids;
        }

        public String getArea_device_names() {
            return area_device_names;
        }

        public void setArea_device_names(String area_device_names) {
            this.area_device_names = area_device_names;
        }

        public String getArea_device_location_ids() {
            return area_device_location_ids;
        }

        public void setArea_device_location_ids(String area_device_location_ids) {
            this.area_device_location_ids = area_device_location_ids;
        }

        public String getArea_device_location_names() {
            return area_device_location_names;
        }

        public void setArea_device_location_names(String area_device_location_names) {
            this.area_device_location_names = area_device_location_names;
        }

    public String getArea_subject_ids() {
            return area_subject_ids;
        }

        public void setArea_subject_ids(String area_subject_ids) {
            this.area_subject_ids = area_subject_ids;
        }

        public String getArea_subject_names() {
            return area_subject_names;
        }

        public void setArea_subject_names(String area_subject_names) {
            this.area_subject_names = area_subject_names;
        }

        public List<InspectionAreaSubject> getInspectionAreaSubjects() {
            return inspectionAreaSubjects;
        }

        public void setInspectionAreaSubjects(List<InspectionAreaSubject> inspectionAreaSubjects) {
            this.inspectionAreaSubjects = inspectionAreaSubjects;
        }
        public void getInspectionAreaSubjectsList(){
            if (area_subject_ids.length()>0&&area_subject_ids!=null){
                if(id.length()>0&&id!=null){
                    String[] area_subject_ids_arr=area_subject_ids.split(",");
                    for (int i = 0; i <area_subject_ids_arr.length; i++) {
                        String sid=area_subject_ids_arr[i];
                        if(sid.length()>0){
                            InspectionAreaSubject inspectionAreaSubject=new InspectionAreaSubject();
                            inspectionAreaSubject.setArea_id(id);
                            inspectionAreaSubject.setSubject_id(sid);
                            inspectionAreaSubject.preInsert();
                            inspectionAreaSubjects.add(inspectionAreaSubject);
                        }
                    }
                }
            }
        }
}
