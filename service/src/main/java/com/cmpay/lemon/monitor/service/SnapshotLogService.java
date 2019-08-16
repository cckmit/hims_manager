package com.cmpay.lemon.monitor.service;

import com.cmpay.lemon.monitor.bo.SnapshotLogBO;
import com.cmpay.lemon.monitor.bo.SnapshotLogQueryBO;
import com.github.pagehelper.PageInfo;

/**
 * Created on 2018/12/17
 *
 * @author: ou_yn
 */
public interface SnapshotLogService {

    /***
     * 保存快照数据
     * @param snapshotLogBO
     * @return
     */
    int insert(SnapshotLogBO snapshotLogBO);

    /***
     * 查询保存的快照数据
     * @param snapshotLogQueryBO
     * @return
     */
    public PageInfo<SnapshotLogBO> getSnapshotHtmls(SnapshotLogQueryBO snapshotLogQueryBO);
}
