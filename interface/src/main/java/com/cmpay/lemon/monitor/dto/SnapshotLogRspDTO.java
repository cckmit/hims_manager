package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;

/**
 * Created on 2018/12/19
 *
 * @author: ou_yn
 */
public class SnapshotLogRspDTO extends PageableRspDTO {

    List<SnapshotLogDTO> snapshotLogDTOs;

    public List<SnapshotLogDTO> getSnapshotLogDTOs() {
        return snapshotLogDTOs;
    }

    public void setSnapshotLogDTOs(List<SnapshotLogDTO> snapshotLogDTOs) {
        this.snapshotLogDTOs = snapshotLogDTOs;
    }

    @Override
    public String toString() {
        return "SnapshotLogRspDTO{" +
                "snapshotLogDTOs=" + snapshotLogDTOs +
                '}';
    }
}
