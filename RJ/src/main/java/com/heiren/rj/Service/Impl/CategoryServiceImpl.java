package com.heiren.rj.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heiren.rj.Common.CustomerException;
import com.heiren.rj.Mapper.CategoryMapper;
import com.heiren.rj.Po.Category;
import com.heiren.rj.Po.Dish;
import com.heiren.rj.Po.Setmeal;
import com.heiren.rj.Service.CategoryService;
import com.heiren.rj.Service.DishFlavorService;
import com.heiren.rj.Service.DishService;
import com.heiren.rj.Service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetMealService setMealService;
    /**
     * 根据Id查询是否有关联
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(dishLambdaQueryWrapper);
        if (count > 0) {
            //关联了菜品，抛出异常
            throw  new CustomerException("当前分类下关联了菜品 ，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setMealService.count(setMealLambdaQueryWrapper);
        if (count2 > 0) {
            //关联了菜品，抛出异常
            throw  new CustomerException("当前分类下关联的套餐 ，不能删除");

        }

        super.removeById(id);
    }
}
