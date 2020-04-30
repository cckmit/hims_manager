/*
 * @ClassName DemandJiraSubtaskDO
 * @Description 
 * @version 1.0
 * @Date 2020-04-29 18:00:18
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class DemandJiraSubtaskDO extends BaseDO {
    /**
     * @Fields subtaskkey 子任务key
     */
    private String subtaskkey;
    /**
     * @Fields parenttaskkey 关联父任务key
     */
    private String parenttaskkey;
    /**
     * @Fields subtaskname 子任务名称
     */
    private String subtaskname;
    /**
     * @Fields assignee 经办人
     */
    private String assignee;

    public String getSubtaskkey() {
        return subtaskkey;
    }

    public void setSubtaskkey(String subtaskkey) {
        this.subtaskkey = subtaskkey;
    }

    public String getParenttaskkey() {
        return parenttaskkey;
    }

    public void setParenttaskkey(String parenttaskkey) {
        this.parenttaskkey = parenttaskkey;
    }

    public String getSubtaskname() {
        return subtaskname;
    }

    public void setSubtaskname(String subtaskname) {
        this.subtaskname = subtaskname;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}