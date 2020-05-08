package com.cmpay.lemon.monitor.utils.jira;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.monitor.bo.jira.CreateIssueRequestBO;
import com.cmpay.lemon.monitor.bo.jira.JiraSubtasksBO;
import com.cmpay.lemon.monitor.bo.jira.JiraTaskBodyBO;
import com.cmpay.lemon.monitor.bo.jira.JiraWorklogBO;
import com.cmpay.lemon.monitor.utils.DateUtil;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class JiraUtil {
    // header Authorization
    private final static String AUTHORIZATION = "Authorization";
    //Authorization value
    private final static String AUTHORIZATIONVALUE = "Basic YWRtaW46SGlzdW5wYXlAMjAxOQ==";
    // header ContentType
    private final static String CONTENTTYPE = "Content-Type";
    // header ContentTypeValue
    private final static String CONTENTTYPEVALUE = "application/json";
    // post 请求
    private final static String CREATEISSUEURL = "http://10.9.10.117:18080/rest/api/2/issue";

    // post 请求
    private final static String GETSEARCH = "http://10.9.10.117:18080/rest/api/2/search";
    //创建
    public static Response CreateIssue(CreateIssueRequestBO createIssueRequest) {
        Response response = given()
                .header(AUTHORIZATION, AUTHORIZATIONVALUE)
                .header(CONTENTTYPE, CONTENTTYPEVALUE)
                .body(createIssueRequest.toString())
                .post(CREATEISSUEURL);
        //返回状态码
        //  int statusCode = response.getStatusCode();
        //返回jira项目信息
        /*if(response.getStatusCode()==201) {
            CreateIssueResponseBO as = response.getBody().as(CreateIssueResponseBO.class);
        }*/
        return response;
    }

    //获取
    public static JiraTaskBodyBO GetIssue(String jirakey) {

        Response response = given()
                .header(AUTHORIZATION, AUTHORIZATIONVALUE)
                .header(CONTENTTYPE, CONTENTTYPEVALUE)
                .get(CREATEISSUEURL + "/" + jirakey);
        ResponseBody body = response.getBody();
        String json = body.print();
        JSONObject object = JSONObject.parseObject(json);
        JiraTaskBodyBO jiraTaskBodyBO = getJiraTaskBodyBO(object);
        return jiraTaskBodyBO;
    }

    private static JiraTaskBodyBO getJiraTaskBodyBO(JSONObject object) {
        JiraTaskBodyBO jiraTaskBodyBO = new JiraTaskBodyBO();
        //获取jirakey
        jiraTaskBodyBO.setJiraKey(object.getString("key"));
        //获取获取jira任务类型
        String jiraType = object.getJSONObject("fields").getJSONObject("issuetype").getString("name");

        //获取获取jira任务名
        String issueName = object.getJSONObject("fields").getString("summary");
        jiraTaskBodyBO.setIssueName(issueName);
        jiraTaskBodyBO.setJiraType(jiraType);
        //获取获取总共已用时
        String timespent = object.getJSONObject("fields").getString("timespent");
        jiraTaskBodyBO.setTimespent(timespent);

        String aggregatetimespent = object.getJSONObject("fields").getString("aggregatetimespent");
        if(object.getJSONObject("fields").getString("customfield_10225")!=null) {
            String department = object.getJSONObject("fields").getJSONObject("customfield_10225").getString("value");
            jiraTaskBodyBO.setDepartment(department);
        }
        //获取经办人
        String assignee = object.getJSONObject("fields").getJSONObject("assignee").getString("displayName");
        jiraTaskBodyBO.setAssignee(assignee);

        //获取起始时间
        String planStartTime = object.getJSONObject("fields").getString("customfield_10252");
        jiraTaskBodyBO.setPlanStartTime(planStartTime);
        //获取结束时间
        String planEndTime = object.getJSONObject("fields").getString("customfield_10253");
        jiraTaskBodyBO.setPlanEndTime(planEndTime);

        //日志流水
        String worklogs = object.getJSONObject("fields").getJSONObject("worklog").getString("worklogs");
        jiraTaskBodyBO.setWorklogs(worklogs);
        //附属子任务
        String subtasks = object.getJSONObject("fields").getString("subtasks");
        jiraTaskBodyBO.setSubtasks(subtasks);
        return jiraTaskBodyBO;
    }

    //获得子任务
    public static List<JiraSubtasksBO> getSubtasks(JiraTaskBodyBO jiraTaskBodyBO) {
        if(StringUtils.isBlank(jiraTaskBodyBO.getSubtasks())){
            return null;
        }
        List<JiraSubtasksBO> jiraSubtasksBOLinkedList = new LinkedList<>();
        //附属子任务
        JSONArray subtasksJsonArray = JSONArray.parseArray(jiraTaskBodyBO.getSubtasks());
        if (subtasksJsonArray != null) {
            for (int i = 0; i < subtasksJsonArray.size(); i++) {
                JSONObject jsonObject1 =JSONObject.parseObject(subtasksJsonArray.get(i).toString());
                String JiraKey = jsonObject1.getString("key");
                String summary = jsonObject1.getJSONObject("fields").getString("summary");

                JiraSubtasksBO jiraSubtasksBO = new JiraSubtasksBO();
                jiraSubtasksBO.setSubtaskkey(JiraKey);
                jiraSubtasksBO.setSubtaskname(summary);
                jiraSubtasksBO.setParenttaskkey(jiraTaskBodyBO.getJiraKey());
                jiraSubtasksBOLinkedList.add(jiraSubtasksBO);
            }
            return jiraSubtasksBOLinkedList;
        }
        return null;

    }

    //获得工作流水
    public static  List<JiraWorklogBO> getWorklogs(JiraTaskBodyBO jiraTaskBodyBO) {
        JSONArray worklogsJsonArray = JSONArray.parseArray(jiraTaskBodyBO.getWorklogs());
        List<JiraWorklogBO> jiraWorklogBOLinkedList = new LinkedList<>();
        if (worklogsJsonArray != null) {
            for (int i = 0; i < worklogsJsonArray.size(); i++) {
                JiraWorklogBO jiraWorklogBO = new JiraWorklogBO();
                JSONObject jsonObject1 =JSONObject.parseObject(worklogsJsonArray.get(i).toString());
                String jiraWorklogKey = jsonObject1.getString("self");
                jiraWorklogBO.setJiraWorklogKey(jiraWorklogKey);

                String timeSpent = jsonObject1.getString("timeSpentSeconds");
                jiraWorklogBO.setTimespnet(timeSpent);
                String displayName = jsonObject1.getJSONObject("author").getString("displayName");
                jiraWorklogBO.setDisplayname(displayName);
                String name = jsonObject1.getJSONObject("author").getString("name");
                jiraWorklogBO.setName(name);
                //备注
                String comment = jsonObject1.getString("comment");
                jiraWorklogBO.setComment(comment);
                String created = DateUtil.addDateMinut(DateUtil.dealDateFormat(jsonObject1.getString("created")), 8);
                jiraWorklogBO.setCreatedtime(created);

                String started = DateUtil.addDateMinut(DateUtil.dealDateFormat(jsonObject1.getString("started")), 8);
                jiraWorklogBO.setStartedtime(started);

                String updated = DateUtil.addDateMinut(DateUtil.dealDateFormat(jsonObject1.getString("updated")), 8);
                jiraWorklogBO.setUpdatedtime(updated);

                jiraWorklogBOLinkedList.add(jiraWorklogBO);
            }
            return jiraWorklogBOLinkedList;
        }
        return null;
    }

    //修改测试主任务
    public static void EditTheTestMainTask(JiraTaskBodyBO jiraTaskBodyBO) {
        System.err.println(jiraTaskBodyBO.getEditTestMainTaskBody());
        System.err.println(CREATEISSUEURL + "/" + jiraTaskBodyBO.getJiraKey());
        Response response = given()
                .header(AUTHORIZATION, AUTHORIZATIONVALUE)
                .header(CONTENTTYPE, CONTENTTYPEVALUE)
                .body(jiraTaskBodyBO.getEditTestMainTaskBody())
                .put(CREATEISSUEURL + "/" + jiraTaskBodyBO.getJiraKey());
    }


    //依据jql批量获取jira数据
    public static List<JiraTaskBodyBO>  batchQueryIssuesModifiedWithinOneDay(int page) {
        Response response = given()
                .header(AUTHORIZATION, AUTHORIZATIONVALUE)
                .header(CONTENTTYPE, CONTENTTYPEVALUE)
                .get(GETSEARCH + "?" + "jql= updated >= -1d order by created&startAt="+page+"&maxResults=50");
        ResponseBody body = response.getBody();
        String json = body.print();
        JSONObject object = JSONObject.parseObject(json);
        String maxResults = object.getString("maxResults");
        String total = object.getString("total");
        JSONArray issueJsonArray = JSONArray.parseArray( object.getString("issues"));
        List<JiraTaskBodyBO> jiraTaskBodyBOlist = new LinkedList<>();
        if (issueJsonArray != null) {
            for (int i = 0; i < issueJsonArray.size(); i++) {
                JSONObject jsonObject1 =JSONObject.parseObject(issueJsonArray.get(i).toString());
                JiraTaskBodyBO jiraTaskBodyBO = new JiraTaskBodyBO();
                jiraTaskBodyBO.setJiraKey(jsonObject1.getString("key"));
                jiraTaskBodyBOlist.add(jiraTaskBodyBO);
            }
        }
        return jiraTaskBodyBOlist;
    }


    /*
     *获取主任务，并解析相关信息
     */
    public static void main(String[] args) {
        JiraUtil.batchQueryIssuesModifiedWithinOneDay(1000);

    }


}

