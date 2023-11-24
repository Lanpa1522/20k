package com.heiren.rj.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heiren.rj.Mapper.SetMealDishMapper;
import com.heiren.rj.Po.SetmealDish;
import com.heiren.rj.Service.SetMealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetMealDishServiceImpl extends ServiceImpl<SetMealDishMapper, SetmealDish> implements SetMealDishService {
}
