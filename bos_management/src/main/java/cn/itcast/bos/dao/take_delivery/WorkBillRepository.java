package cn.itcast.bos.dao.take_delivery;

import cn.itcast.bos.domain.take_delivery.WorkBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkBillRepository extends JpaRepository<WorkBill, Integer> {
}
