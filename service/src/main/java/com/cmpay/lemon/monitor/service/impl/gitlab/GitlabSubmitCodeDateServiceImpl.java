package com.cmpay.lemon.monitor.service.impl.gitlab;

import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.dao.IGitlabDataDao;
import com.cmpay.lemon.monitor.dao.IGitlabDataExtDao;
import com.cmpay.lemon.monitor.dao.IUserExtDao;
import com.cmpay.lemon.monitor.entity.GitlabDataDO;
import com.cmpay.lemon.monitor.entity.UserDO;
import com.cmpay.lemon.monitor.service.gitlab.GitlabSubmitCodeDateService;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.utils.ISO8601;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
@Service
public class GitlabSubmitCodeDateServiceImpl implements GitlabSubmitCodeDateService {
    //jira项目类型 和包项目 jira编号10106(测试环境)
    private final static String HOSTURL = "http://172.16.50.222:82/";
    private final static String TOKEN = "4YkRcSKMJKyWtRUGxxU3";

    @Autowired
    private IGitlabDataExtDao gitlabDataDao;
    @Autowired
    private IUserExtDao iUserDao;


    @Async
    @Override
    public void getGitlabDate(){
        // 获取连接
        // hostUrl：gitLab的域名（或者IP:port）
        // personalAccessToken：步骤2中的AccessToken
        try{
            String hostUrl= HOSTURL;
            String privateToken= TOKEN;
            //获取当前日期的前一天
            String date = DateUtil.getBeforeDay();
            String start = date+"T00:00:00Z";
            String end = date+"T23:59:59Z";
            Date since = ISO8601.toDate(start);
            Date until = ISO8601.toDate(end);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            GitLabApi gitLabApi = new GitLabApi(hostUrl, privateToken);
//            RepositoryApi repositoryApi = gitLabApi.getRepositoryApi();
//            CommitsApi commitsApi = gitLabApi.getCommitsApi();
            //获取所有的项目
            List<Project> projects = gitLabApi.getProjectApi().getProjects();
            System.err.println("projects===================="+projects.size());
            for (int i=0;i<projects.size();i++){
                System.err.println("projects循环次数===================="+i);
                //根据项目id获取该项目下的所有项目分支
                // http://172.16.50.222:82/middleground/clearingcenter/clear.git 下没有分支，报403
                if (projects.get(i).getId() == 302){
                    continue;
                }
                List <Branch> branch ;
                try{
                     branch = gitLabApi.getRepositoryApi().getBranches(projects.get(i).getId());
                }catch (Exception e){
                    continue;
                }
                if(JudgeUtils.isNotEmpty(branch)){
                    for(int j=0;j<branch.size();j++){
                        // 查询指定时间内，该分支是否有提交记录
                        List<Commit> commits ;
                        try{
                            commits = gitLabApi.getCommitsApi().getCommits(projects.get(i).getId(), branch.get(j).getName(), since, until);
                        }catch (Exception e){
                            continue;
                        }
                        if(JudgeUtils.isNotEmpty(commits)){
                            //如果分支存在提交记录
                            for(int k =0;k<commits.size();k++){
                                GitlabDataDO gitlabDataDO = new GitlabDataDO();
                                // 获取代码提交记录详情
                                // 判断邮箱后缀，@hisuntech.com为高阳员工
                                if(commits.get(k).getCommitterEmail().contains("@hisuntech.com")){
                                    Commit commit ;
                                    try{
                                         commit = gitLabApi.getCommitsApi().getCommit(projects.get(i).getId(),commits.get(k).getShortId());
                                    }catch (Exception e){
                                        continue;
                                    }

                                    gitlabDataDO.setGitlabId(commit.getId());
                                    gitlabDataDO.setCommitterName(commit.getCommitterName());
                                    gitlabDataDO.setCommittedDate(sdf.format(commit.getCommittedDate()));
                                    gitlabDataDO.setCommitterEmail(commit.getCommitterEmail());

                                    if(commit.getTitle().length()>2048){
                                        gitlabDataDO.setTitle(commit.getTitle().substring(0,2048));
                                    }else {
                                        gitlabDataDO.setTitle(commit.getTitle());
                                    }
                                    if(commit.getMessage().length()>2048){
                                        gitlabDataDO.setMessage(commit.getMessage().substring(0,2048));
                                    }else {
                                        gitlabDataDO.setMessage(commit.getMessage());
                                    }


                                    gitlabDataDO.setStatsTotal(commit.getStats().getTotal());
                                    gitlabDataDO.setStatsAdditions(commit.getStats().getAdditions());
                                    gitlabDataDO.setStatsDeletions(commit.getStats().getDeletions());
                                    gitlabDataDO.setBranchName(branch.get(j).getName());
                                    gitlabDataDO.setHttpUrlToRepo(projects.get(i).getHttpUrlToRepo());
                                    gitlabDataDO.setProjectId(projects.get(i).getId()+"");
                                    gitlabDataDO.setNameWithNamespace(projects.get(i).getNameWithNamespace());
                                    gitlabDataDO.setDescription(projects.get(i).getDescription());
                                    // 根据邮箱查询用户信息
                                    // 根据姓名获得邮箱
                                    UserDO userDO =iUserDao.getUserByEmail(gitlabDataDO.getCommitterEmail());
                                    if(JudgeUtils.isNotNull(userDO)){
                                        gitlabDataDO.setDevpLeadDept(userDO.getDepartment());
                                        gitlabDataDO.setDisplayName(userDO.getFullname());
                                    }
                                    // 邮箱后缀为hisun的才保存
                                    // 先判断是否已经存在
                                    GitlabDataDO gitlabDataDO1 = gitlabDataDao.get(commit.getId());
                                    if(JudgeUtils.isNull(gitlabDataDO1)){
                                        // 保存代码提交记录
                                        gitlabDataDao.insert(gitlabDataDO);
                                    }else {
                                        // 更新代码提交记录
                                        gitlabDataDao.update(gitlabDataDO);
                                    }

                                }

                            }

                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
