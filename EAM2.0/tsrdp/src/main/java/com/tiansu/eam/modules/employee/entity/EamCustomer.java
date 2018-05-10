package com.tiansu.eam.modules.employee.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;


/**
 * @creator duanfuju
 * @createtime 2017/8/28 9:08
 * @description: 
 * 客户信息 entity
 */
public class EamCustomer extends DataEntity<EamCustomer> {


    private String id_key;//主键id    和插件ligerui属性冲突
    private String customer_code;//客户编码
    private String customer_name;//客户名称
    private String customer_level;//客户类别
    private String customer_credit;//客户信用等级
    private String customer_bus_license;//客户营业执照编码
    private String customer_org_code;//组织机构代码
    private Double customer_taxcode;//增值税率
    private String customer_sucCode;//社会统一信用代码
    private String customer_taxrate;//税务登记证代码
    private String customer_deposit_bank;//客户开户行
    private String customer_account;//客户账号
    private String customer_bank_address;//开户行地址
    private String customer_president;//法人代表
    private Integer customer_regfund;//注册资金
    private String customer_address;//客户地址
    private String customer_linkman;//联系人
    private String customer_phone;//联系电话
    private String customer_fax;//传真
    private String customer_email;//邮箱
    private Date customer_busdate_start;//业务开始日期
    private Date customer_busdate_end;//业务结束日期
    private String customer_remark;//备注
    private String customer_status;//客户状态


    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_level() {
        return customer_level;
    }

    public void setCustomer_level(String customer_level) {
        this.customer_level = customer_level;
    }

    public String getCustomer_credit() {
        return customer_credit;
    }

    public void setCustomer_credit(String customer_credit) {
        this.customer_credit = customer_credit;
    }

    public String getCustomer_bus_license() {
        return customer_bus_license;
    }

    public void setCustomer_bus_license(String customer_bus_license) {
        this.customer_bus_license = customer_bus_license;
    }

    public String getCustomer_org_code() {
        return customer_org_code;
    }

    public void setCustomer_org_code(String customer_org_code) {
        this.customer_org_code = customer_org_code;
    }

    public Double getCustomer_taxcode() {
        return customer_taxcode;
    }

    public void setCustomer_taxcode(Double customer_taxcode) {
        this.customer_taxcode = customer_taxcode;
    }

    public String getCustomer_sucCode() {
        return customer_sucCode;
    }

    public void setCustomer_sucCode(String customer_sucCode) {
        this.customer_sucCode = customer_sucCode;
    }

    public String getCustomer_taxrate() {
        return customer_taxrate;
    }

    public void setCustomer_taxrate(String customer_taxrate) {
        this.customer_taxrate = customer_taxrate;
    }

    public String getCustomer_deposit_bank() {
        return customer_deposit_bank;
    }

    public void setCustomer_deposit_bank(String customer_deposit_bank) {
        this.customer_deposit_bank = customer_deposit_bank;
    }

    public String getCustomer_account() {
        return customer_account;
    }

    public void setCustomer_account(String customer_account) {
        this.customer_account = customer_account;
    }

    public String getCustomer_bank_address() {
        return customer_bank_address;
    }

    public void setCustomer_bank_address(String customer_bank_address) {
        this.customer_bank_address = customer_bank_address;
    }

    public String getCustomer_president() {
        return customer_president;
    }

    public void setCustomer_president(String customer_president) {
        this.customer_president = customer_president;
    }

    public Integer getCustomer_regfund() {
        return customer_regfund;
    }

    public void setCustomer_regfund(Integer customer_regfund) {
        this.customer_regfund = customer_regfund;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getCustomer_linkman() {
        return customer_linkman;
    }

    public void setCustomer_linkman(String customer_linkman) {
        this.customer_linkman = customer_linkman;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_fax() {
        return customer_fax;
    }

    public void setCustomer_fax(String customer_fax) {
        this.customer_fax = customer_fax;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public Date getCustomer_busdate_start() {
        return customer_busdate_start;
    }

    public void setCustomer_busdate_start(Date customer_busdate_start) {
        this.customer_busdate_start = customer_busdate_start;
    }

    public Date getCustomer_busdate_end() {
        return customer_busdate_end;
    }

    public void setCustomer_busdate_end(Date customer_busdate_end) {
        this.customer_busdate_end = customer_busdate_end;
    }

    public String getCustomer_remark() {
        return customer_remark;
    }

    public void setCustomer_remark(String customer_remark) {
        this.customer_remark = customer_remark;
    }

    public String getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(String customer_status) {
        this.customer_status = customer_status;
    }
}
