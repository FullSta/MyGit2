package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.SubArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SubAreaService {
    List<SubArea> findAll();

    void saveBatch(List<SubArea> areas);

    Page<SubArea> findPageData(Specification<SubArea> specification, Pageable pageable);

    void save(SubArea model);
}
