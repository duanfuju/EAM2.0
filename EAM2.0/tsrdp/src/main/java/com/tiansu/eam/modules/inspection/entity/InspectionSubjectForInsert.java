package com.tiansu.eam.modules.inspection.entity;
import java.util.ArrayList;
import java.util.List;


/**
 * @creator duanfuju
 * @createtime 2017/10/19 14:58
 * @description:
 * 		巡检项实体
 */
public class InspectionSubjectForInsert{
	private String id_key;
	private String dev_id;
	private List<InspectionSubject> charSubjects = new ArrayList<>();
	private List<InspectionSubject> numberSubjects = new ArrayList<>();
	private String subject_status;

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

	public List<InspectionSubject> getCharSubjects() {
		return charSubjects;
	}

	public void setCharSubjects(List<InspectionSubject> charSubjects) {
		this.charSubjects = charSubjects;
	}

	public List<InspectionSubject> getNumberSubjects() {
		return numberSubjects;
	}

	public void setNumberSubjects(List<InspectionSubject> numberSubjects) {
		this.numberSubjects = numberSubjects;
	}

	public String getSubject_status() {
		return subject_status;
	}

	public void setSubject_status(String subject_status) {
		this.subject_status = subject_status;
	}
}