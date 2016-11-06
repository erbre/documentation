package com.erbre.appstatus.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"schema_version\"")
public class SchemaVersionEntity {

    @Column(name = "\"checksum\"")
    private Integer checksum;
    @Column(name = "\"description\"")
    private String description;
    @Column(name = "\"execution_time\"")
    private Integer executionTime;
    @Column(name = "\"installed_by\"")
    private String installedBy;
    @Column(name = "\"installed_on\"")
    private Date installedOn;
    @Column(name = "\"installed_rank\"")
    private Integer installedRank;
    @Column(name = "\"script\"")
    private String script;

    @Column(name = "\"success\"")
    private boolean success;

    @Column(name = "\"type\"")
    private String type;

    @Id
    @Column(name = "\"version\"")
    private String version;

    public Integer getChecksum() {
        return checksum;
    }

    public String getDescription() {
        return description;
    }

    public Integer getExecutionTime() {
        return executionTime;
    }

    public String getInstalledBy() {
        return installedBy;
    }

    public Date getInstalledOn() {
        return installedOn;
    }

    public Integer getInstalledRank() {
        return installedRank;
    }

    public String getScript() {
        return script;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public void setInstalledBy(String installedBy) {
        this.installedBy = installedBy;
    }

    public void setInstalledOn(Date installedOn) {
        this.installedOn = installedOn;
    }

    public void setInstalledRank(Integer installedRank) {
        this.installedRank = installedRank;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
