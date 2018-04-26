package cn.itcast.bos.dao.take_delivery;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WayBillRepository extends JpaRepository<WayBill,Integer>{

    WayBill findByWayBillNum(String wayBillNum);
}
