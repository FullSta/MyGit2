package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.FixedArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface FixedAreaService {
    Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable);

    void save(FixedArea fixedArea);

    void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId);

    FixedArea findOne(String fixedAreaId);
}
