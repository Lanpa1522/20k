package com.heiren.rj.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heiren.rj.Mapper.DishFlavorMapper;
import com.heiren.rj.Po.DishFlavor;
import com.heiren.rj.Service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
