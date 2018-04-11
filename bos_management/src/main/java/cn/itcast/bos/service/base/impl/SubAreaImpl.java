package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.SubAreaRepository;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubAreaImpl implements SubAreaService {
    @Autowired
    private SubAreaRepository subAreaRepository;

    @Override
    public List<SubArea> findAll() {
        return subAreaRepository.findAll();
    }

    @Override
    public void saveBatch(List<SubArea> areas) {
        subAreaRepository.save(areas);
    }
}
