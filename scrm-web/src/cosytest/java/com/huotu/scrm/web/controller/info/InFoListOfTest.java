package com.huotu.scrm.web.controller.info;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.web.controller.WebTestOfMock;
import org.junit.Test;

/**
 * Created by cosy on 2017/8/7.
 * 1. 列表验证
 *    页面元素验证
 *    输入不存在的资讯标题，验证系统能否给出响应的数据。且验证翻页记录
 *    输入完全匹配的资讯标题aaa，验证系统能否检索出数据（abc，bca，aaa，）检索出1条，且验证翻页记录
 *    输入咨询标题a，验证能否检索出数据（abc，bca，aaa，ccc），检索出3条，且验证翻页记录
 *    验证页面是否显示 “显示全部”按钮是否存在，且功能是否有效
 *    验证页面是否显示 ”添加资讯“按钮是否存在，且验证链接是否有效
 *    验证咨询列表中显示的字段是否正确，资讯标题，简介，创建时间，发布状态，推广状态，操作（编辑，删除，转发记录，浏览记录）
 * 2.编辑页面
 *    点击编辑页面，修改各项值后，保存，回到列表页，验证该页面的值是否修改
 *
 * 3.转发记录
 *    咨询A，B，分别有2，3条转发记录，
 *    分别进入A，B咨询，验证数据显示是否正确
 *
 * 4.浏览记录
 *    咨询A，B，分别有2，3条浏览记录，
 *    分别进入A，B咨询，验证数据显示是否正确
 *
 *  5.删除
 *     删除资讯
 *
 */
public class InFoListOfTest extends WebTestOfMock {
    Customer customer;
    UserLevel userLevel;

    User userTurnOfOne,userTurnOfTwo,userTurnOfThree;//用于转发

    User userRead;//用于读取

    Info inFoOne,inFoTwo,inFoThree;

   public void dataMockOfInFo()throws Exception{
       customer= mockCustomer();
       userLevel= mockUserLevel(customer.getId(), UserType.buddy,true);


       //转发用户
       userTurnOfOne= mockUser(customer.getId(),userLevel);
       userTurnOfTwo= mockUser(customer.getId(),userLevel);
       userTurnOfThree= mockUser(customer.getId(),userLevel);

       //读取用户
       userRead= mockUser(customer.getId(),userLevel);

       //咨询
       inFoOne=createInFoMock(customer,true,true,"aaa");
       inFoTwo=createInFoMock(customer,true,true,"abc");
       inFoThree=createInFoMock(customer,true,true,"cde");

   }
    /*********************************************************************************页面验证*******************************************************/


    /**
     * 页面元素检查
     * @throws Exception
     */
    @Test
    public void checkElenemt()throws Exception{
        dataMockOfInFo();
        /*mockUserLogin(userNormal.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userNormal.getId());*/

    }

}
