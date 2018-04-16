package cn.itcast.bos.service.take_delivery.impl;

import cn.itcast.bos.dao.take_delivery.PromotionRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public void save(Promotion model) {
        promotionRepository.save(model);
    }

    @Override
    public Page<Promotion> findPageData(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }

    @Override
    public PageBean<Promotion> findPageDate(int page, int rows) {
        Pageable pageable = new PageRequest(page-1,rows);
        Page<Promotion> pageData = promotionRepository.findAll(pageable);
        // 封装到page对象
        PageBean<Promotion> pageBean =new PageBean<Promotion>();
        pageBean.setTotalCount(pageData.getTotalElements());
        pageBean.setPageData(pageData.getContent());

        return pageBean;
    }

    @Override
    public Promotion findById(Integer id) {
        System.out.println("后台收到的要查询的id:"+id);
        return promotionRepository.findOne(id);
    }

    @Override
    public void updateStatus(Date date) {
        promotionRepository.updateStatus(date);
    }
}
