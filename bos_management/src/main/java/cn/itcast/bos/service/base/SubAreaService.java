package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.SubArea;

import java.util.List;

public interface SubAreaService {
    List<SubArea> findAll();

    void saveBatch(List<SubArea> areas);
}
