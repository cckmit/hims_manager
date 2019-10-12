package com.cmpay.lemon.monitor.utils.jira;

import com.cmpay.lemon.monitor.bo.jira.CreateIssueRequestBO;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class JiraUtil {
    // header Authorization
    private final String AUTHORIZATION = "Authorization";
    //Authorization value
    private final String AUTHORIZATIONVALUE = "Basic YWRtaW46SGlzdW5wYXlAMjAxOQ==";
    // header ContentType
    private final String CONTENTTYPE = "Content-Type";
    // header ContentTypeValue
    private final String CONTENTTYPEVALUE = "application/json";
    // post 请求
    private final String CREATEISSUEURL= "http://10.9.10.117:18080/rest/api/2/issue";

    public Response CreateIssue(CreateIssueRequestBO createIssueRequest) {
        System.out.println(createIssueRequest.toString());
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
}
