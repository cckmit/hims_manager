package com.cmpay.lemon.monitor.bo;

import java.util.List;

/**
 * @author  ty
 */
public class TestProgressDetailRspBO {
    private List<TestProgressDetailBO> testProgressDetailBOList ;

    public List<TestProgressDetailBO> getTestProgressDetailBOList() {
        return testProgressDetailBOList;
    }

    public void setTestProgressDetailBOList(List<TestProgressDetailBO> testProgressDetailBOList) {
        this.testProgressDetailBOList = testProgressDetailBOList;
    }

    @Override
    public String toString() {
        return "TestProgressDetailRspBO{" +
                "testProgressDetailBOList=" + testProgressDetailBOList +
                '}';
    }
}
