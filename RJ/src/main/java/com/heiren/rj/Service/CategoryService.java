package com.heiren.rj.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heiren.rj.Po.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
