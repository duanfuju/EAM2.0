package com.tiansu.eam.modules.inspection.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;


/**
 * @creator duanfuju
 * @createtime 2017/10/19 10:15
 * @description:
 * 		巡检项实体
 */
public class InspectionSubject extends DataEntity<InspectionSubject> {

	private static final long serialVersionUID = 1L;

	private String id_key;//主键
	private String dev_id;//巡检设备id(一对一)
	private String subject_code;//巡检项编码
	private String subject_name;//巡检项名称
	private String subject_content;//巡检项内容和方法
	private String subject_standard;//巡检项判定标准
	private String subject_valuetype;//巡检项值类型
	private String subject_way;//测量通道
	private String subject_value1;//结果1
	private String subject_value2;//结果2
	private String subject_value3;//结果3
	private String subject_sx_value;//上限值
	private String subject_xx_value;//下限值
	private String subject_ck_value;//参考值
	private String subject_unit;//单位
	private int subject_decimal;//小数位
	private String subject_default;//默认结果集
	private String subject_status;//状态


	public String getId_key() {
		return id_key;
	}

	public void setId_key(String id_key) {
		this.id_key = id_key;
	}

	public String getDev_id() {
		return dev_id;
	}

	public void setDev_id(String dev_id) {
		this.dev_id = dev_id;
	}

	public String getSubject_code() {
		return subject_code;
	}

	public void setSubject_code(String subject_code) {
		this.subject_code = subject_code;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public String getSubject_content() {
		return subject_content;
	}

	public void setSubject_content(String subject_content) {
		this.subject_content = subject_content;
	}

	public String getSubject_standard() {
		return subject_standard;
	}

	public void setSubject_standard(String subject_standard) {
		this.subject_standard = subject_standard;
	}

	public String getSubject_valuetype() {
		return subject_valuetype;
	}

	public void setSubject_valuetype(String subject_valuetype) {
		this.subject_valuetype = subject_valuetype;
	}

	public String getSubject_way() {
		return subject_way;
	}

	public void setSubject_way(String subject_way) {
		this.subject_way = subject_way;
	}

	public String getSubject_value1() {
		return subject_value1;
	}

	public void setSubject_value1(String subject_value1) {
		this.subject_value1 = subject_value1;
	}

	public String getSubject_value2() {
		return subject_value2;
	}

	public void setSubject_value2(String subject_value2) {
		this.subject_value2 = subject_value2;
	}

	public String getSubject_value3() {
		return subject_value3;
	}

	public void setSubject_value3(String subject_value3) {
		this.subject_value3 = subject_value3;
	}

	public String getSubject_sx_value() {
		return subject_sx_value;
	}

	public void setSubject_sx_value(String subject_sx_value) {
		this.subject_sx_value = subject_sx_value;
	}

	public String getSubject_xx_value() {
		return subject_xx_value;
	}

	public void setSubject_xx_value(String subject_xx_value) {
		this.subject_xx_value = subject_xx_value;
	}

	public String getSubject_ck_value() {
		return subject_ck_value;
	}

	public void setSubject_ck_value(String subject_ck_value) {
		this.subject_ck_value = subject_ck_value;
	}

	public String getSubject_unit() {
		return subject_unit;
	}

	public void setSubject_unit(String subject_unit) {
		this.subject_unit = subject_unit;
	}

	public int getSubject_decimal() {
		return subject_decimal;
	}

	public void setSubject_decimal(int subject_decimal) {
		this.subject_decimal = subject_decimal;
	}

	public String getSubject_default() {
		return subject_default;
	}

	public void setSubject_default(String subject_default) {
		this.subject_default = subject_default;
	}

	public String getSubject_status() {
		return subject_status;
	}

	public void setSubject_status(String subject_status) {
		this.subject_status = subject_status;
	}
}