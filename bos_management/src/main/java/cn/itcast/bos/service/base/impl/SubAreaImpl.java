package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.SubAreaRepository;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public Page<SubArea> findPageData(Specification<SubArea> specification, Pageable pageable) {
        return subAreaRepository.findAll(specification,pageable);
    }

    @Override
    public void save(SubArea model) {
        subAreaRepository.save(model);
    }
}
