package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);
    }

    @Override
    public void delBatch(String[] idArray) {
        // 调用dao实现,update修改操作,讲deltag修改为1
        for (String idStr:idArray){
            Integer id=Integer.parseInt(idStr);
            courierRepository.updateDelTag(id);
        }
    }

    @Override
    public Page<Courier> findPageDate(Specification<Courier> specification, Pageable pageable) {
        return courierRepository.findAll(specification,pageable);
    }
}
