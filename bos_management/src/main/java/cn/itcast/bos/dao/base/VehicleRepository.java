package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {
}
