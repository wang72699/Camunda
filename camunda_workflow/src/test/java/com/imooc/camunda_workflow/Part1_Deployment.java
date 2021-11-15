package com.imooc.camunda_workflow;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 *  Deployment : 添加资源文件、获取部署信息、部署时间
 *  ProcessDefinition :获取版本号、key、资源名称、部署时间
 */


@SpringBootTest
public class Part1_Deployment {


      @Autowired
      private RepositoryService repositoryService;

      //流程部署：
      @Test
      public void initDeploymentBPMN(){
          //String filename = "BPMN/Part1_deployment.bpmn";
          //String filename = "BPMN/Part4_Task.bpmn";
          //String filename = "BPMN/Part4_Task_claim.bpmn";
          String filename = "BPMN/Part6_UEL_V1.bpmn";
          Deployment deployment = repositoryService.createDeployment()
                  .name("流程部署测试UEL_V1")
                  .addClasspathResource(filename)
                  .deploy();
          System.out.println("部署ID： " + deployment.getId());
          System.out.println("部署名称： " + deployment.getName());

    }

    //查询流程部署
    @Test
    public void getDeployments() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for(Deployment dep : list){
            System.out.println("——————————————————————start——————————————————————————————");
            System.out.println("Id："+dep.getId());
            System.out.println("Name："+dep.getName());
            System.out.println("DeploymentTime："+dep.getDeploymentTime());
            System.out.println("———————————————————————end—————————————————————————————");
        }

    }


}
