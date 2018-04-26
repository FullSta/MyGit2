package cn.itcast.bos.service.take_delivery;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WayBillService {
    void save(WayBill model);

    Page<WayBill> findPageData(WayBill model, Pageable pageable);

    WayBill findByWayBillNum(String wayBillNum);
}
