package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by luohaibo on 2017/7/11.
 */
public interface InfoBrowseRepository extends JpaRepository<InfoBrowse,Long> {


    /**
     * 根据分页条件查找到某一页的资讯列表
     */
    Page<Info> infoBrowseList( Pageable pageable);
}
