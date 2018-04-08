package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CourierService {
    public void save(Courier courier);

    public void delBatch(String[] idArray);

    public Page<Courier> findPageDate(Specification<Courier> specification, Pageable pageable);
}
