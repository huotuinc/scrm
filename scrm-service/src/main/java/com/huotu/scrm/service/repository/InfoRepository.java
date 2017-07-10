package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.Information.Info;
import org.springframework.data.jpa.repository.JpaRepository;



/**
 * Created by luohaibo on 2017/7/5.
 */
public interface InfoRepository extends JpaRepository<Info,Long>{


    /**
     * 根据id号查找对应的资讯
     * @param id
     * @return
     */
    Info findById(Long id);




}

