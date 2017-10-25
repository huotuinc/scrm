package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.service.info.InfoBrowseService;
import com.huotu.scrm.web.CommonTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by luohaibo on 2017/7/12.
 */
public class InfoBrowseControllerTest extends CommonTestBase{


    @Autowired
    InfoBrowseService infoBrowseServer;

//    @Test
//    @Rollback(false)
//    public void insertBrowse(){
//        InfoBrowse infoBrowse = new InfoBrowse();
//        infoBrowse.setInfoId(new Long(123));
//        infoBrowse.setReadUserId(new Long(1234));
//        infoBrowse.setSourceUserId(new Long(123456));
//        infoBrowseServer.infoBroseSave(infoBrowse);
//    }

    @Test
    public void turnRecord(){

        InfoBrowse infoBrowse = new InfoBrowse();
        infoBrowse.setInfoId(1232L);
        Pageable pageable = new PageRequest(0,20);
        InfoBrowseAndTurnSearch infoBrowseAndTurnSearch = new InfoBrowseAndTurnSearch();
        infoBrowseAndTurnSearch.setSourceUserId(2L);
        Page<InfoBrowse> list = infoBrowseServer.infoTurnRecord(infoBrowseAndTurnSearch);


        list.getContent().stream()
                .forEach(System.out::println);


    }

    @Test
    public void downloadBrowseToExcel() throws Exception {
        //模拟商户
        Long customerId = Long.valueOf(random.nextInt(10000));
        //模拟资讯
        Info info1 = mockInfo(customerId);
        Info info2 = mockInfo(customerId);
        //模拟用户等级
        UserLevel userLevel = mockUserLevel(customerId, null, true);
        //模拟用户
        User user1 = mockUser(customerId, userLevel);
        User user2 = mockUser(customerId, userLevel);
        User user3 = mockUser(customerId, userLevel);
        User user4 = mockUser(customerId, userLevel);
        user1.setBelongOne(user3.getId().intValue());
        user2.setBelongOne(user4.getId().intValue());
        user1 = userRepository.saveAndFlush(user1);
        user2 = userRepository.saveAndFlush(user2);
        //模拟浏览记录
        InfoBrowse infoBrowse1 = mockInfoBrowse2(info1.getId(), user1.getId(), customerId);
        InfoBrowse infoBrowse2 = mockInfoBrowse2(info1.getId(), user2.getId(), customerId);
        InfoBrowse infoBrowse3 = mockInfoBrowse2(info2.getId(), user3.getId(), customerId);

        byte[] contentAsByteArray = mockMvc.perform(post("/mall/info/downloadBrowse")
                .param("customerId", customerId.toString())
                .param("infoId", info1.getId().toString())
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();


        File dir = new File("D:\\");
        if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
            dir.mkdirs();
        }
        File file = new File(dir + "资讯浏览记录列表.xls");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(contentAsByteArray);
        bos.flush();
    }

}