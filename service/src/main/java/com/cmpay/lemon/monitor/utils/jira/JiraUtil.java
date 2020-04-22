package com.cmpay.lemon.monitor.utils.jira;

import com.alibaba.fastjson.JSONObject;
import com.cmpay.lemon.monitor.bo.jira.CreateIssueRequestBO;
import com.cmpay.lemon.monitor.bo.jira.JiraTaskBodyBO;
import com.cmpay.lemon.monitor.utils.ReadExcelUtils;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

import java.io.FileNotFoundException;
import java.util.Map;

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

    //创建
    public static JiraTaskBodyBO GetIssue(String jirakey) {
        JiraTaskBodyBO jiraTaskBodyBO = new JiraTaskBodyBO();
        Response response = given()
                .header(AUTHORIZATION, AUTHORIZATIONVALUE)
                .header(CONTENTTYPE, CONTENTTYPEVALUE)
                .get(CREATEISSUEURL + "/" + jirakey);
        ResponseBody body = response.getBody();
        String json = body.print();
        JSONObject object = JSONObject.parseObject(json);
        //获取经办人
        String assignee = object.getJSONObject("fields").getJSONObject("assignee").getString("displayName");
        jiraTaskBodyBO.setAssignee(assignee);
        //获取起始时间
        String planStartTime = object.getJSONObject("fields").getString("customfield_10252");
        jiraTaskBodyBO.setPlanStartTime(planStartTime);
        //获取结束时间
        String planEndTime = object.getJSONObject("fields").getString("customfield_10253");
        jiraTaskBodyBO.setPlanEndTime(planEndTime);
        return jiraTaskBodyBO;
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


    /*
     *删除jira任务脚本
     * */
   /* public static void main(String[] args) {
        for(int i=320;i<370;i++) {
            Response response = given()
                    .header(AUTHORIZATION, AUTHORIZATIONVALUE)
                    .header(CONTENTTYPE, CONTENTTYPEVALUE)
                    .delete(CREATEISSUEURL + "/CMPAY-"+i);
        }
    }*/

    /*
     *修改主任务
     * */
  /*  public static void main(String[] args) {
        EditTheMainTaskRequest editTheMainTaskRequest = new EditTheMainTaskRequest();
        String i="TMT-185";
        Response response = given()
                .header(AUTHORIZATION, AUTHORIZATIONVALUE)
                .header(CONTENTTYPE, CONTENTTYPEVALUE)
                .body(editTheMainTaskRequest.toString())
                .put(CREATEISSUEURL +"/"+i);
    }
*/
    /*
     *获取主任务，并解析相关信息
     */
    public static void main(String[] args) {
        try {
            String filepath = "C:\\Program Files (x86)\\q.xlsx";
            ReadExcelUtils excelReader = new ReadExcelUtils(filepath);

            Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent();
            for (int i = 1; i <= map.size(); i++) {
                 map.get(i).get(0).toString();
                System.err.println(map.get(i).get(0).toString());
                map.get(i).get(1).toString();
                System.err.println(map.get(i).get(1).toString());
                map.get(i).get(2).toString();
                System.err.println(map.get(i).get(2).toString());
                String aa="{\n" +
                        "  \"password\": \""+123456+"\",\n" +
                        "  \"emailAddress\": \""+map.get(i).get(2).toString()+"\",\n" +
                        "  \"displayName\": \""+map.get(i).get(1).toString()+"\",\n" +
                        "  \"name\": \""+map.get(i).get(0).toString()+"\"\n" +
                        "}";
                System.err.println(aa);
               Response response = given()
                        .header(AUTHORIZATION, AUTHORIZATIONVALUE)
                        .header(CONTENTTYPE, CONTENTTYPEVALUE)
                        .body(aa)
                        .post("http://10.9.10.117:18080/rest/api/2/user");
            }



        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件!");
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

