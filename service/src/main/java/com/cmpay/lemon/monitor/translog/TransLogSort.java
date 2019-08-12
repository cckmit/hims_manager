package com.cmpay.lemon.monitor.translog;

import com.cmpay.lemon.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TransLogSort {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransLogSort.class);

    private static final String ZERO = "0";

    public static TransLog buildTree(List<Map<String, Object>> list) {
        List<TransLog> logList = convert(list);
        if (logList == null || logList.isEmpty()) {
            return null;
        }
        long start = System.currentTimeMillis();
        List<TransLog> sortList = sortAndComplete(logList);
        long end = System.currentTimeMillis();
        LOGGER.info("sortAndComplete, srcList: {}, sortList: {} , cost: {}ms", logList.size(), sortList.size(), (end - start));
        Map<String, TransLog> tree = new HashMap<>(32);
        // put all to flat tree
        for (TransLog log : sortList) {
            tree.put(log.getSn(), log);
        }
        // tree dependency build
        for (TransLog log : logList) {
            String psn = log.getPsn();
            // 主调交易的sn和psn一致
            if (log.getSn().equals(log.getPsn())) {
                continue;
            }
            if (tree.containsKey(psn)) {
                tree.get(psn).getChildren().add(log);
            }
        }
        return tree.get("0");
    }


    public static List<TransLog> inferLost(TransLog before, TransLog after) {
        List<TransLog> list = new ArrayList<>();
        String start = before.getSn();
        String end = after.getPsn();
        String[] a_start = start.split("-");
        String[] a_end = end.split("-");
        String curTmp = start;
        String prefix = "";
        boolean prefixSame = true;
        for (int i = 0; i < a_end.length; i++) {
            int i_end_i = Integer.parseInt(a_end[i]);
            int i_start_i = prefixSame ? Integer.parseInt(a_start[i]) : 0;
            if (i_end_i != i_start_i) {
                prefixSame = false;
            } else {
                prefix += i_end_i + "-";
                continue;
            }
            while (++i_start_i < i_end_i) {
                list.add(new TransLog(prefix + i_start_i, curTmp, 0));
                // curTmp = "" + i_start_i;
                curTmp = prefix + i_start_i;
            }
            //最后一个节点
            if (a_end.length == (i + 1)) {
                list.add(new TransLog(prefix + i_start_i, curTmp, 0));
            }
            prefix += i_end_i + "-";
        }
        return list;
    }

    public static void main(String[] args) {
        List<TransLog> transLogs = inferLost(new TransLog("2", "0", "7", 1), new TransLog("4-2-3", "4-2-2", "7", 1));
        System.out.println(transLogs);
    }


    public static List<TransLog> sortAndComplete(List<TransLog> logList) {
        Collections.sort(logList, new TransLogComaprator());
        // 补全队列 记录补全的序号和补全的虚拟调用节点
        Map<Integer, List<TransLog>> completeList = new LinkedHashMap<>();
        TransLog root = logList.get(0);
        if (!ZERO.equals(root.getSn())) {
            logList.add(0, new TransLog("0", "0", 0));
        }
        TransLog beforeTL = logList.get(0);
        TransLog curTL;
        for (int i = 1; i < logList.size(); i++) {
            curTL = logList.get(i);
            String beforeSn = beforeTL.getSn();
            // 根据上级节点，推出本机节点备选序号
            String[] a_before = StringUtils.split(beforeSn, "-");
            Set<String> options = new HashSet<>();
            // 1  2,2-1   5-1 6,6-1 5-2,5-2-1
            String prefix = "";
            for (int i1 = 0; i1 < a_before.length; i1++) {
                String next = prefix + (Integer.parseInt(a_before[i1]) + 1);
                // 添加同步和异步备选节点
                options.add(next);
                options.add(next + "-1");
                prefix += a_before[i1] + "-";
            }
            // 如果不在备选节点中，说明当前节点上半部分缺失
            if (!options.contains(curTL.getSn()) & !StringUtils.equals(beforeSn, curTL.getSn())) {
                // 推断可能缺失节点infer
                completeList.put(i, inferLost(beforeTL, curTL));
            }
            // 当前位置节点完整
            beforeTL = curTL;
        }

        // 补全缺失元素
        // 1. 主调交易丢失
        int cursor = 0;
        for (Map.Entry<Integer, List<TransLog>> complete : completeList.entrySet()) {
            int pos = complete.getKey();
            List<TransLog> value = complete.getValue();
            logList.addAll(pos + cursor, value);
            cursor += value.size();
        }
        // 设置最小丢失节点数
        logList.get(0).setLostCnt(cursor);
        return logList;

    }

    private static List<TransLog> convert(List<Map<String, Object>> list) {
        List<TransLog> ret = new ArrayList<TransLog>();
        if (list == null || list.isEmpty()) {
            return ret;
        }
        for (Map<String, Object> map : list) {
            if (map == null || map.isEmpty()) {
                continue;
            }
            ret.add(new TransLog((Map<String, Object>) Optional.ofNullable(map.get("txnplte")).orElse(map)));
        }
        return ret;
    }

}
