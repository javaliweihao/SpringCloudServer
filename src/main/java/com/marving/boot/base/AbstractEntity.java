package com.marving.boot.base;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreatedDate
    @Column
    private Date createTime;

    @LastModifiedBy
    @Column
    private Date updateTime;

    @Column
    private long createdBy;//for public schema, adminId; for tenant schema, staffId or tenantAdmin Id

    @Column
    private int creatorMode;//AuthContext.Mode

    @Column
    private long updatedBy;//for public schema, adminId; for tenant schema, staffId or tenantAdmin Id

    @Column
    private int updaterMode;//AuthContext.Mode

    @Column
    private String notes;

    @Column
    private int status = EntityStatus.ACTIVE;

    public int getCreatorMode() {
        return creatorMode;
    }

    public void setCreatorMode(int creatorMode) {
        this.creatorMode = creatorMode;
    }

    public int getUpdaterMode() {
        return updaterMode;
    }

    public void setUpdaterMode(int updaterMode) {
        this.updaterMode = updaterMode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }

}
