package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.SubArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubAreaRepository extends JpaRepository<SubArea,String>,JpaSpecificationExecutor<SubArea> {
}
