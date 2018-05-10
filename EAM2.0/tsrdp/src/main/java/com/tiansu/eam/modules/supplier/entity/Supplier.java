package com.tiansu.eam.modules.supplier.entity;

import com.tiansu.eam.common.persistence.DataEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by zhangww on 2017/8/21.
 */
public class Supplier extends DataEntity<Supplier> {
    private String id_key;
    private String supplier_code;
    private String supplier_name;
    private String supplier_type;
    private String supplier_level;
    private String supplier_credit;
    private String supplier_president;
    private Double supplier_regfund;
    private String supplier_address;
    private String supplier_linkman;
    private String supplier_phone;
    private String supplier_fax;
    private Date supplier_busdate_start;
    private Date supplier_busdate_end;
    private String supplier_bus_license;
    private String supplier_org_code;
    private String supplier_taxcode;
    private String supplier_sucCode;
    private Double supplier_taxrate;
    private String supplier_deposit_bank;
    private String supplier_account;
    private String supplier_bank_address;
    private String supplier_remark;
    private String supplier_status;

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getSupplier_code() {
        return supplier_code;
    }

    public void setSupplier_code(String supplier_code) {
        this.supplier_code = supplier_code;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSupplier_type() {
        return supplier_type;
    }

    public void setSupplier_type(String supplier_type) {
        this.supplier_type = supplier_type;
    }

    public String getSupplier_level() {
        return supplier_level;
    }

    public void setSupplier_level(String supplier_level) {
        this.supplier_level = supplier_level;
    }

    public String getSupplier_credit() {
        return supplier_credit;
    }

    public void setSupplier_credit(String supplier_credit) {
        this.supplier_credit = supplier_credit;
    }

    public String getSupplier_president() {
        return supplier_president;
    }

    public void setSupplier_president(String supplier_president) {
        this.supplier_president = supplier_president;
    }

    public Double getSupplier_regfund() {
        return supplier_regfund;
    }

    public void setSupplier_regfund(Double supplier_regfund) {
        this.supplier_regfund = supplier_regfund;
    }

    public String getSupplier_address() {
        return supplier_address;
    }

    public void setSupplier_address(String supplier_address) {
        this.supplier_address = supplier_address;
    }

    public String getSupplier_linkman() {
        return supplier_linkman;
    }

    public void setSupplier_linkman(String supplier_linkman) {
        this.supplier_linkman = supplier_linkman;
    }

    public String getSupplier_phone() {
        return supplier_phone;
    }

    public void setSupplier_phone(String supplier_phone) {
        this.supplier_phone = supplier_phone;
    }

    public String getSupplier_fax() {
        return supplier_fax;
    }

    public void setSupplier_fax(String supplier_fax) {
        this.supplier_fax = supplier_fax;
    }

    public Date getSupplier_busdate_start() {
        return supplier_busdate_start;
    }

    public void setSupplier_busdate_start(Date supplier_busdate_start) {
        this.supplier_busdate_start = supplier_busdate_start;
    }

    public Date getSupplier_busdate_end() {
        return supplier_busdate_end;
    }

    public void setSupplier_busdate_end(Date supplier_busdate_end) {
        this.supplier_busdate_end = supplier_busdate_end;
    }

    public String getSupplier_bus_license() {
        return supplier_bus_license;
    }

    public void setSupplier_bus_license(String supplier_bus_license) {
        this.supplier_bus_license = supplier_bus_license;
    }

    public String getSupplier_org_code() {
        return supplier_org_code;
    }

    public void setSupplier_org_code(String supplier_org_code) {
        this.supplier_org_code = supplier_org_code;
    }

    public String getSupplier_taxcode() {
        return supplier_taxcode;
    }

    public void setSupplier_taxcode(String supplier_taxcode) {
        this.supplier_taxcode = supplier_taxcode;
    }

    public String getSupplier_sucCode() {
        return supplier_sucCode;
    }

    public void setSupplier_sucCode(String supplier_sucCode) {
        this.supplier_sucCode = supplier_sucCode;
    }

    public Double getSupplier_taxrate() {
        return supplier_taxrate;
    }

    public void setSupplier_taxrate(Double supplier_taxrate) {
            this.supplier_taxrate = supplier_taxrate;
    }

    public String getSupplier_deposit_bank() {
        return supplier_deposit_bank;
    }

    public void setSupplier_deposit_bank(String supplier_deposit_bank) {
        this.supplier_deposit_bank = supplier_deposit_bank;
    }

    public String getSupplier_account() {
        return supplier_account;
    }

    public void setSupplier_account(String supplier_account) {
        this.supplier_account = supplier_account;
    }

    public String getSupplier_bank_address() {
        return supplier_bank_address;
    }

    public void setSupplier_bank_address(String supplier_bank_address) {
        this.supplier_bank_address = supplier_bank_address;
    }

    public String getSupplier_remark() {
        return supplier_remark;
    }

    public void setSupplier_remark(String supplier_remark) {
        this.supplier_remark = supplier_remark;
    }

    public String getSupplier_status() {
        return supplier_status;
    }

    public void setSupplier_status(String supplier_status) {
        this.supplier_status = supplier_status;
    }
}
