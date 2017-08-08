package com.huotu.scrm.web.controller.info;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserFormalIntegral;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.web.controller.WebTestOfMock;
import com.huotu.scrm.web.controller.utils.WebElementUtils;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.bcel.generic.L2D;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;


/**
 * Created by cosy on 2017/8/1.
 *
 *
 *1.今日uv排名验证
 *    A，B,C用户生成浏览数据（2,1,0）后，用三个用户进行登入，验证排名是否正确。 A用户登入显示1，B用户登入显示2，C用户登入显示0
 *
 *2.创造UV
 *    用户A，未产生UV的情况下，显示为0
 *    用户A，同条咨询2个人人浏览，产生一条新的UV的情况下，显示2
 *    用户A，不同条咨询，不同个人浏览，产生2条新的UV的情况下，显示2
 *

 *
 *
 *
 */


public class InFoCenterOfTest extends WebTestOfMock {
    Customer customer;

     /* ********************************************验证页面元素*************************************** */

    /**
     * 小伙伴非销售员登入，不能查看关注按钮
     * @throws Exception
     */
    @Test
    public void assertEqualsPageElementOfNotSaleMan()throws Exception{

        //创建商户，用户为小伙伴但不是销售员，不显示 ”关注标签“
        customer= mockCustomer();
        UserLevel userLevelNoSaleMan= mockUserLevel(customer.getId(), UserType.buddy,false);
        User userNoSaleMan= mockUser(customer.getId(),userLevelNoSaleMan);

        //用户登入
        mockUserLogin(userNoSaleMan.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userNoSaleMan.getId());

        // 判断页面元素显示是否正确
        Assert.assertTrue(webDriver.findElement(By.xpath("html/body/div[3]/ul/li[1]/a/p[2]")).isEnabled());
        Assert.assertTrue(webDriver.findElement(By.xpath("html/body/div[3]/ul/li[2]/a/p[2]")).isEnabled());
        Assert.assertTrue(webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[2]")).isEnabled());
        Assert.assertTrue(webDriver.findElement(By.xpath("html/body/div[3]/ul/li[4]/a/p[2]")).isEnabled());
        Assert.assertFalse(WebElementUtils.doesWebElementExist(webDriver,By.xpath("html/body/div[3]/ul/li[5]/a/p[2]")));
    }


    /**
     * 小伙伴销售员，可以查看关注按钮
     * @throws Exception
     */
    @Test
    public void assertEqualsPageElementOfIsSaleMan() throws Exception{
        customer= mockCustomer();
        //创建用户为小伙伴且是销售员,显示 “关注标签”
        UserLevel userLevelIsSaleMan= mockUserLevel(customer.getId(), UserType.buddy,true);
        User userIsSaleMan= mockUser(customer.getId(),userLevelIsSaleMan);

        //用户登入
        mockUserLogin(userIsSaleMan.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userIsSaleMan.getId());
        Assert.assertTrue(webDriver.findElement(By.xpath("html/body/div[3]/ul/li[1]/a/p[2]")).isEnabled());
        Assert.assertTrue(webDriver.findElement(By.xpath("html/body/div[3]/ul/li[2]/a/p[2]")).isEnabled());
        Assert.assertTrue(webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[2]")).isEnabled());
        Assert.assertTrue(webDriver.findElement(By.xpath("html/body/div[3]/ul/li[4]/a/p[2]")).isEnabled());
        Assert.assertTrue(webDriver.findElement(By.xpath("html/body/div[3]/ul/li[5]/a/p[2]")).isEnabled());
    }

    /**
     * 普通会员登入，不能查看关注按钮
     * @throws Exception
     */
    @Test
    public void assertEqualsPageElementOfNormal()throws Exception{
        customer= mockCustomer();
        //创建用户为普通会员
        UserLevel userLevelIsSaleMan= mockUserLevel(customer.getId(), UserType.normal,true);
        User userIsSaleMan= mockUser(customer.getId(),userLevelIsSaleMan);

        //用户登入
        mockUserLogin(userIsSaleMan.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userIsSaleMan.getId());
        //校验页面是否显示 进入UV排名等
        Assert.assertFalse(WebElementUtils.doesWebElementExist(webDriver,By.xpath("html/body/div[3]/ul")));
    }


    /**
     *
     * 今日uv排名,创造UV验证
     *    A，B,C，D用户生成浏览数据（2,1,0,2）后，分别用4个用户进行登入，验证排名是否正确。 A用户登入显示1，B用户登入显示2，C用户登入显示-- D用户显示3
     * @throws Exception
     */
    @Test
    public void uvOrder()throws Exception{
        customer= mockCustomer();
        UserLevel userLevel= mockUserLevel(customer.getId(), UserType.buddy,true);

        //创建三个转发用户（A,B,C，D）
        User userTurnOne= mockUser(customer.getId(),userLevel);
        User userTurnTwo= mockUser(customer.getId(),userLevel);
        User userTurnThree= mockUser(customer.getId(),userLevel);
        User userTurnFour= mockUser(customer.getId(),userLevel);

        //创建一个浏览用户
        User userReadOne= mockUser(customer.getId(),userLevel);
        User userReadTwo= mockUser(customer.getId(),userLevel);

        //创建二个咨询
        Info infoOne=createInFoMock(customer,true,true,"test1");
        Info infoTwo=createInFoMock(customer,true,true,"test2");

        //创建用户usertrunOne转发同条咨询，用户ReadOne，ReadTwo浏览，生成2条浏览量
        infoBrowse(infoOne.getId(),userTurnOne.getId(),userReadOne.getId(),customer.getId());
        infoBrowse(infoOne.getId(),userTurnOne.getId(),userReadTwo.getId(),customer.getId());

        //创建用户userTurnTwo转发，一个用户ReadOne浏览，生成1条浏览量
        infoBrowse(infoOne.getId(),userTurnTwo.getId(),userReadOne.getId(),customer.getId());

        //创建userTurnFour转发两条咨询，两个用户浏览
        infoBrowse(infoOne.getId(),userTurnFour.getId(),userReadOne.getId(),customer.getId());
        infoBrowse(infoTwo.getId(),userTurnFour.getId(),userReadOne.getId(),customer.getId());

        //用户userTurnThree转发，无浏览量

        //用户userTurnOne登入，并验证排行值是1,创造UV是2
        mockUserLogin(userTurnOne.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnOne.getId());
        Assert.assertEquals("1",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[1]/a/p[1]")).getText().toString());

        Assert.assertEquals("2",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[2]/a/p[1]")).getText().toString());



        //用户userTurnTwo登入，并验证排行值是3，创造UV是1
        mockUserLogin(userTurnTwo.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnTwo.getId());
        Assert.assertEquals("3",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[1]/a/p[1]")).getText().toString());
        Assert.assertEquals("1",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[2]/a/p[1]")).getText().toString());

        //用户userTurnThree登入，并验证排行值是2，创造UV是0
        mockUserLogin(userTurnThree.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnThree.getId());
        Assert.assertEquals("--",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[1]/a/p[1]")).getText().toString());
        Assert.assertEquals("0",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[2]/a/p[1]")).getText().toString());

        //用户userTurnFour登入，并验证排行值是2，创造UV是2（转发2条咨询，2个人浏览）
        mockUserLogin(userTurnFour.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnFour.getId());
        Assert.assertEquals("2",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[1]/a/p[1]")).getText().toString());
        Assert.assertEquals("2",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[2]/a/p[1]")).getText().toString());
    }



    /***********************************************************今日预计积分**************************************************/


    /**
     * 模拟用户，咨询等数据，用于测试转发积分显示
     */

     User userTurnOne;
     User userReadOne;
     Info infoOne;
     Info infoTwo;
     UserLevel userLevel;

    public void dataMockOfReward(UserType userType)throws Exception {
        customer= mockCustomer();
        userLevel= mockUserLevel(customer.getId(), userType,true);

        //转发浏览用户
        userTurnOne= mockUser(customer.getId(),userLevel);
        userReadOne=mockUser(customer.getId(),userLevel);

        //创建咨询
        infoOne=createInFoMock(customer,true,true,"test1");
        infoTwo=createInFoMock(customer,true,true,"test2");


    }



    /**
     * 关闭转发奖励，用户A转发咨询 验证是否产生今日积分
     * @throws Exception
     */
    @Test
    public void closeReward()throws Exception{
        dataMockOfReward(UserType.buddy);

        createInfoConfigureMock(false,5,false,5,1,false,1,1,customer);
        mockUserLogin(userTurnOne.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnOne.getId());
        Assert.assertEquals("0",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[1]")).getText().toString());
    }

    /**
     * 开启转发奖励，但是奖励设置为0，验证是否产生今日积分
     * @throws Exception
     */
    @Test
    public void rewardScoreOfZero()throws Exception{
        dataMockOfReward(UserType.buddy);

        createInfoConfigureMock(true,0,false,5,1,false,1,1,customer);
        mockUserLogin(userTurnOne.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnOne.getId());
        Assert.assertEquals("0",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[1]")).getText().toString());
    }

    /**
     * 开启转发奖励，奖励设置为1，验证是否产生今日积分
     * @throws Exception
     */
    @Test
    public void havaRewardScore()throws Exception{
        dataMockOfReward(UserType.buddy);

        createInfoConfigureMock(true,1,false,5,1,false,1,1,customer);
        mockUserLogin(userTurnOne.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnOne.getId());
        Assert.assertEquals("1",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[1]")).getText().toString());
    }

    /**
     * 转发接口没有调通，等调通后在测试
     * 开启转发奖励，奖励先设置为1，用户A转发后，验证是否产生今日积分为1，修改转发配置为5，用户A转发B咨询，积分为6
     * 5.开启转发奖励，但是奖励先设置为1，并设置转发奖励次数为2，用户A转发第二次。验证能否获取对应的积分
     * 6.开启转发奖励，但是奖励先设置为1，并设置转发奖励次数为2，用户A转发第三次。验证能否获取对应的积分
     * 7.开启转发奖励，但是奖励先设置为1，并设置转发奖励次数为1，用户A转发第一次。验证能否获取对应的积分，然后设置转发奖励积分为5，再次转发，验证能否获得相应的值
     * 8.设置转发奖励获取的对象是小伙伴，用户A为小伙伴，转发。验证能否获得值
     * 9.设置转发奖励获取的对象是小伙伴，用户A普通会员，转发。验证能否获得值
     * @throws Exception
     */
    @Test
    public void addRewardScore()throws Exception{
        dataMockOfReward(UserType.buddy);
        createInfoConfigureMock(true,1,false,5,1,false,1,1,customer);

        infoBrowse(infoTwo.getId(),userTurnOne.getId(),userReadOne.getId(),customer.getId());
        createInfoConfigureMock(true,5,false,5,1,false,1,1,customer);

        mockUserLogin(userTurnOne.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnOne.getId());
        Assert.assertEquals("6",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[1]")).getText().toString());
    }



    /**
     *  10.关闭UV转积分按钮，验证uv能否转成积分
     * @throws Exception
     */
    @Test
    public void cloceExchangeSwitch()throws Exception{
        dataMockOfReward(UserType.buddy);
        createInfoConfigureMock(false,1,false,5,1,false,1,1,customer);
        infoBrowse(infoOne.getId(),userTurnOne.getId(),userReadOne.getId(),customer.getId());


        mockUserLogin(userTurnOne.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnOne.getId());
        Assert.assertEquals("0",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[1]")).getText().toString());
    }


    /**
     *    11.开启UV转积分按钮，设置转换的比例为1，且账户下有2个uv，验证该用户有多少个积分

     * @throws Exception
     */
    @Test
    public void oneAndTwoUV()throws Exception{

        dataMockOfReward(UserType.buddy);
        createInfoConfigureMock(false,1,false,5,1,true,1,1,customer);
        infoBrowse(infoOne.getId(),userTurnOne.getId(),userReadOne.getId(),customer.getId());
        infoBrowse(infoTwo.getId(),userTurnOne.getId(),userReadOne.getId(),customer.getId());

        mockUserLogin(userTurnOne.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnOne.getId());
        Assert.assertEquals("2",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[1]")).getText().toString());
    }

    /**
     * *  12.开启UV转积分按钮，设置转换比例为1，且账户下无uv，验证该用户下有0个积分
     *
     * @throws Exception
     */
    @Test
    public void oneAndNullUV()throws Exception{
        dataMockOfReward(UserType.buddy);
        createInfoConfigureMock(false,1,false,5,1,true,1,1,customer);

        mockUserLogin(userTurnOne.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnOne.getId());
        Assert.assertEquals("0",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[1]")).getText().toString());
    }

    /**
     *  13.开启UV转积分按钮，设置转换比例为5，且账户下有1个uv，有0个积分
     *
     *
     * @throws Exception
     */
    @Test
    public void fiveAndOneUV()throws Exception{
        dataMockOfReward(UserType.buddy);
        createInfoConfigureMock(false,1,false,5,1,true,5,1,customer);
        infoBrowse(infoOne.getId(),userTurnOne.getId(),userReadOne.getId(),customer.getId());

        mockUserLogin(userTurnOne.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnOne.getId());
        Assert.assertEquals("0",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[1]")).getText().toString());
    }


    /**
     * 14.开启UV转积分按钮，设置转换比例为3，且账户下有2个uv
     * @throws Exception
     */
    @Test
    public void twoAndOneUV()throws Exception{
        dataMockOfReward(UserType.buddy);
        createInfoConfigureMock(false,1,false,5,1,true,3,1,customer);
        infoBrowse(infoOne.getId(),userTurnOne.getId(),userReadOne.getId(),customer.getId());
        infoBrowse(infoTwo.getId(),userTurnOne.getId(),userReadOne.getId(),customer.getId());

        mockUserLogin(userTurnOne.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnOne.getId());
        Assert.assertEquals("0",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[1]")).getText().toString());
    }


    /**
     * 15.开启UV转积分按钮，设置转换比例为1，uv转积分获取对象是小伙伴，且账户下有uv，用户A为小伙伴，验证能否正常获得
     * @throws Exception
     */
    @Test
    public void uvToBuddy()throws Exception{
        dataMockOfReward(UserType.buddy);
        createInfoConfigureMock(false,1,false,5,1,true,1,1,customer);
        infoBrowse(infoOne.getId(),userTurnOne.getId(),userReadOne.getId(),customer.getId());

        mockUserLogin(userTurnOne.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userTurnOne.getId());
        Assert.assertEquals("1",webDriver.findElement(By.xpath("html/body/div[3]/ul/li[3]/a/p[1]")).getText().toString());
    }



    /**************************************************************4.累计积分***********************************************/
    //TODO: 2017/8/8
    /**************************************************************5.关注人数***********************************************/
     //TODO: 2017/8/8






    /***************************************************************6.咨询列表显示******************************************/


    /**
     * 模拟出两个用户（小伙伴，普通会员）用于查看咨询显示
     */
    UserLevel userLevelOfBuddy;
    UserLevel userLevelOfNormal;
    User userBuddy;//小伙伴
    User userNormal;//普通会员

    public void dataMockOfShowInFo()throws Exception{
        customer= mockCustomer();

        userLevelOfBuddy= mockUserLevel(customer.getId(), UserType.buddy,true);
        userLevelOfNormal= mockUserLevel(customer.getId(), UserType.normal,true);


        userBuddy= mockUser(customer.getId(),userLevelOfBuddy);
        userNormal=mockUser(customer.getId(),userLevelOfNormal);
    }

    /**
     * 1.创建咨询，设置 咨询状态为1，设置资讯推广状态0,验证咨询的显示。普通会员可以看,小伙伴不能看
     * @throws Exception
     */
    @Test
    public void normalSee()throws Exception{
        dataMockOfShowInFo();

        //创建咨询
        Info info=createInFoMock(customer,true,false,"test");

       //模拟普通会员登入，能查看该咨询
        mockUserLogin(userNormal.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userNormal.getId());

        //验证页面能否显示该咨询
        Assert.assertEquals(info.getTitle(),webDriver.findElement(By.className("weui_media_title")).getText());


        //小伙伴登入，不显示该咨询
        mockUserLogin(userBuddy.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userBuddy.getId());
        Assert.assertFalse(WebElementUtils.doesWebElementExist(webDriver,By.className("weui_media_title")));
    }

    /**
     * 2.创建咨询，设置 咨询状态为0，设置资讯推广状态1,验证咨询的显示。普通会员不可以看,小伙伴能看
     * @throws Exception
     */
    @Test
    public  void buddySee()throws Exception{

        dataMockOfShowInFo();

        //创建咨询
        Info info=createInFoMock(customer,false,true,"test");

        //模拟普通会员登入，不能查看该咨询
        mockUserLogin(userNormal.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userNormal.getId());

        //验证页面能否显示该咨询

        Assert.assertFalse(WebElementUtils.doesWebElementExist(webDriver,By.className("weui_media_title")));


        //小伙伴登入，显示该咨询
        mockUserLogin(userBuddy.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userBuddy.getId());
        Assert.assertEquals(info.getTitle(),webDriver.findElement(By.className("weui_media_title")).getText());
    }


    /**
     * 3.创建咨询，设置 咨询状态为0，设置资讯推广状态0,验证咨询的显示。普通会员不可以看,小伙伴不能看
     * @throws Exception
     */
    @Test
    public void buddyAndNormalNoSee()throws Exception{
        dataMockOfShowInFo();

        //创建咨询
        Info info=createInFoMock(customer,false,false,"test");

        //模拟普通会员登入，不能查看该咨询
        mockUserLogin(userNormal.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userNormal.getId());

        //验证页面能否显示该咨询

        Assert.assertFalse(WebElementUtils.doesWebElementExist(webDriver,By.className("weui_media_title")));


        //小伙伴登入，不显示该咨询
        mockUserLogin(userBuddy.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userBuddy.getId());
        Assert.assertFalse(WebElementUtils.doesWebElementExist(webDriver,By.className("weui_media_title")));
    }


    /**
     * 4.创建咨询，设置 咨询状态为1，设置资讯推广状态1,验证咨询的显示。普通会员可以看,小伙伴能看
     * @throws Exception
     */
    @Test
    public  void  buddyAndNormalSee()throws Exception{
        dataMockOfShowInFo();

        //创建咨询
        Info info=createInFoMock(customer,true,true,"test");

        //模拟普通会员登入，能查看该咨询
        mockUserLogin(userNormal.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userNormal.getId());

        //验证页面能否显示该咨询

        Assert.assertEquals(info.getTitle(),webDriver.findElement(By.className("weui_media_title")).getText());


        //小伙伴登入，显示该咨询
        mockUserLogin(userBuddy.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userBuddy.getId());
        Assert.assertEquals(info.getTitle(),webDriver.findElement(By.className("weui_media_title")).getText());
    }


    /**
     * 5.验证咨询显示的各项值  没有通过测试
     * @throws Exception
     */
    @Test
    public void checkValue()throws Exception{
        dataMockOfShowInFo();

        //创建咨询
        Info infoOne=createInFoMock(customer,true,true,"test1");
        Info infoTwo=createInFoMock(customer,true,true,"test2");

        //模拟普通会员登入，能查看该咨询
        mockUserLogin(userNormal.getId(),customer.getId());
        webDriver.get("http://localhost/site/extension/getInfoExtension?customerId="+customer.getId()+"&userId="+userNormal.getId());

        //验证咨询列表上各项值显示是否正确
        List<WebElement> webElements= webDriver.findElements(By.className("weui_panel"));
        Assert.assertEquals(2,webElements.size());
        Assert.assertEquals(infoOne.getTitle(),webElements.get(0).findElement(By.className("weui_media_title")).getText());
        Assert.assertEquals(infoTwo.getTitle(),webElements.get(1).findElement(By.className("weui_media_title")).getText());
    }
}
