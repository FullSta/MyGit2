package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.VehicleRepository;
import cn.itcast.bos.domain.base.Vehicle;
import cn.itcast.bos.service.base.VehicleService;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;


    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }
}
