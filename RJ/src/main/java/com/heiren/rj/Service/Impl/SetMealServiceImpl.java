package com.heiren.rj.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heiren.rj.Common.CustomerException;
import com.heiren.rj.DTO.SetMealDto;
import com.heiren.rj.Mapper.SetMealMapper;
import com.heiren.rj.Po.Setmeal;
import com.heiren.rj.Po.SetmealDish;
import com.heiren.rj.Service.SetMealDishService;
import com.heiren.rj.Service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {
    @Autowired
    private SetMealDishService setMealDishService;
    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setMealDto
     */
    @Override
    public void saveWithDish(SetMealDto setMealDto) {
        //保存套餐的基本信息
        this.save(setMealDto);
        //保存套餐和菜品的关联信息
        List<SetmealDish> setmealDishList = setMealDto.getSetmealDishes();
        List<SetmealDish> collect = setmealDishList.stream().map((a) -> {
            a.setSetmealId(setMealDto.getId());
            return a;
        }).collect(Collectors.toList());
        setMealDishService.saveBatch(collect);
    }

    /**
     * 根据ids来删除对应的套餐信息
     * @param ids
     */
    @Override
    public void removeWithIds(List<Long> ids) {
        //if status == 1 ?
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomerException("套餐正在售卖中,无法删除");
        }
        //delete  setMeal
        this.removeByIds(ids);
        //delete setMealDish
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setMealDishService.remove(setmealDishLambdaQueryWrapper);
    }

}
