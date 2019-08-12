package com.cmpay.lemon.monitor.translog;

import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;

import java.util.Comparator;

public class TransLogComaprator implements Comparator<TransLog> {
    /**
     * 顺序排序
     */
    @Override
    public int compare(TransLog o1, TransLog o2) {
        return splitCompare(o1.getSn(), o2.getSn(), o1.getCsn(), o2.getCsn());
    }

    private static int splitCompare(String sn1, String sn2, String csn1, String csn2) {
        String[] asn1 = StringUtils.split(sn1, "-");
        String[] asn2 = StringUtils.split(sn2, "-");
        String[] acsn1 = StringUtils.split(csn1, "-");
        String[] acsn2 = StringUtils.split(csn2, "-");
        int minLevel = asn1.length < asn2.length ? asn1.length : asn2.length;
        int i_sn1 = 0, i_sn2 = 0;
        for (int level = 0; level < minLevel; level++) {
            i_sn1 = Integer.parseInt(asn1[level]);
            i_sn2 = Integer.parseInt(asn2[level]);
            //同一级别不相等的时候
            if (i_sn1 != i_sn2) {
                break;
            }
            //最后一个级别都相等 1-1-2  1-1
            if (minLevel == level + 1) {
                if (asn1.length != asn2.length) {
                    return asn1.length - asn2.length;
                } else {
                    // 如果csn不存在,默认相等
                    if (JudgeUtils.isNull(acsn1) || JudgeUtils.isNull(acsn2)) {
                        return 0;
                    }
                    return Integer.parseInt(acsn1[level]) - Integer.parseInt(acsn2[level]);
                }
            }
        }
        return i_sn1 - i_sn2;
    }

}
