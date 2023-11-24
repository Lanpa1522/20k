package com.heiren.rj.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heiren.rj.DTO.DishDto;
import com.heiren.rj.Po.Dish;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
    public DishDto getByIdWithFlavor(Long id);
    public void updateWithFlavor(DishDto dishDto);
    public void removeList(String ids);
    public void startSell(String ids);
    public void stopSell(String ids);
}
