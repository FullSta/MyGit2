package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Area;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AreaService {
    public void saveBatch(List<Area> areas);
}
