package com.imooc.camunda_workflow.controller;

import com.imooc.camunda_workflow.SecurityUtil;
import com.imooc.camunda_workflow.pojo.UserInfoBean;
import com.imooc.camunda_workflow.util.AjaxResponse;
import com.imooc.camunda_workflow.util.GlobalConfig;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/processInstance")
public class ProcessInstanceController {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @GetMapping(value = "/getInstances")
    public AjaxResponse getInstances(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            //测试用写死的用户POSTMAN测试用；生产场景已经登录，在processDefinitions中可以获取到当前登录用户的信息
            //if (GlobalConfig.Test) {
             //   securityUtil.logInAs("bajie");
            //}
            List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            for(ProcessInstance pi : list){
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("ProcessInstanceId", pi.getProcessInstanceId());
                hashMap.put("businessKey",  pi.getBusinessKey());
                hashMap.put("processDefinitionId", pi.getProcessDefinitionId());
                hashMap.put("isEnded", pi.isEnded());
                hashMap.put("isSuspended", pi.isSuspended());
                //hashMap.put("processDefinitionVersion", pi.getProcessDefinitionVersion());
                //因为processRuntime.processDefinition("流程部署ID")查询的结果没有部署流程与部署ID，所以用repositoryService查询
                //因为processRuntime.processDefinition("流程部署ID")查询的结果没有部署流程与部署ID，所以用repositoryService查询
                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(pi.getProcessDefinitionId())
                        .singleResult();
                hashMap.put("resourceName", pd.getResourceName());
                hashMap.put("deploymentId", pd.getDeploymentId());
                listMap.add(hashMap);
            }
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(), listMap);
        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),"获取流程实例失败", e.toString());
        }
    }

    //启动流程实例
    @GetMapping(value = "/startProcess")
    public AjaxResponse startProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                     @RequestParam("instanceName") String instanceName,
                                     @RequestParam("instanceVariable") String instanceVariable) {
        try {
            //if (GlobalConfig.Test) {
            //   securityUtil.logInAs("bajie");
            //}else{
            //    securityUtil.logInAs(SecurityContextHolder.getContext().getAuthentication().getName());
            //}
            //instanceName 实例名称 返回业务表主键ID==businessKey
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_claim","bKey003");
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(), processInstance.getId());
        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),"创建流程实例失败", e.toString());
        }
    }

    //删除
    @GetMapping(value = "/deleteInstance")
    public AjaxResponse deleteInstance(@RequestParam("instanceID") String instanceID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("wukong");
            }
            runtimeService.deleteProcessInstance("549d9399-42cb-11ec-bbfd-005056c00008","删着玩");

            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"");
        }
        catch(Exception e){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),"删除流程实例失败", e.toString());
        }

    }

    //挂起
    @GetMapping(value = "/suspendInstance")
    public AjaxResponse suspendInstance(@RequestParam("instanceID") String instanceID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("wukong");
            }
            runtimeService.suspendProcessInstanceById("549d9399-42cb-11ec-bbfd-005056c00008");
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(), "");
        }
        catch(Exception e){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),"挂起流程实例失败", e.toString());
        }
    }

    //激活
    @GetMapping(value = "/resumeInstance")
    public AjaxResponse resumeInstance(@RequestParam("instanceID") String instanceID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("wukong");
            }
            runtimeService.activateProcessInstanceById("549d9399-42cb-11ec-bbfd-005056c00008");
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(), "");
        }
        catch(Exception e){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),"激活流程实例失败", e.toString());
        }
    }

}
