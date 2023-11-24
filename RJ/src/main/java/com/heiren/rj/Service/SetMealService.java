package com.heiren.rj.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heiren.rj.DTO.SetMealDto;
import com.heiren.rj.Po.Setmeal;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {
    public void saveWithDish(SetMealDto setMealDto);
    public void removeWithIds(List<Long> ids);
}
