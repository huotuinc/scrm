package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.Information.Info;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * Created by luohaibo on 2017/7/5.
 */
public interface InfoRepository extends JpaRepository<Info,Long>{

//    //带条件的分页查询
//    @Query("select i from Info i where i.disable = ?1 ")
//    public Page<Info> findByDisable(Integer disable, Pageable pageable);
//
//
//
//    Info findByTitleLike(String title);

}

