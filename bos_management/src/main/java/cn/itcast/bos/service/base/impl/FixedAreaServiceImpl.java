package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
    @Autowired
    private FixedAreaRepository fixedAreaRepository;

    @Override
    public Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable) {
        return fixedAreaRepository.findAll(specification,pageable);
    }

    @Override
    public void save(FixedArea fixedArea) {
        fixedAreaRepository.save(fixedArea);
    }

    // 注入快递员和时间表的dao层
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private TakeTimeRepository takeTimeRepository;
    @Override
    public void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId) {
        FixedArea persistFixedArea = fixedAreaRepository.findOne(model.getId());
        Courier courier = courierRepository.findOne(courierId);
        TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
        // 快递员 关联到定区
        persistFixedArea.getCouriers().add(courier);
        // 将收派标准关联到快递员上
        courier.setTakeTime(takeTime);
    }

    @Override
    public FixedArea findOne(String fixedAreaId) {
        return fixedAreaRepository.findOne(fixedAreaId);
    }
}
