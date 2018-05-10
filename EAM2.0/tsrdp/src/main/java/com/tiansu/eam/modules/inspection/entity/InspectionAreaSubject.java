
package com.tiansu.eam.modules.inspection.entity;

import com.tiansu.eam.common.persistence.DataEntity;


/**
 * @creator duanfuju
 * @createtime 2017/10/23 11:15
 * @description:
 *  巡检区域和巡检项的关联表entity
 */
public class InspectionAreaSubject extends DataEntity<InspectionAreaSubject> {

        private String area_id;//巡检区域id
        private String subject_id;//巡检项


    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }
}
