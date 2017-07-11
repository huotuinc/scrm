package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.Information.Info;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


/**
 * Created by luohaibo on 2017/7/5.
 */
public interface InfoRepository extends JpaRepository<Info,Long>,JpaSpecificationExecutor<Info> {


    long countByDisable(Boolean state);


    List<Info> findByTitleLike(String title);

}

