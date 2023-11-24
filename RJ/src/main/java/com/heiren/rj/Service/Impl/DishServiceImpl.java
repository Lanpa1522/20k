package com.heiren.rj.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.heiren.rj.Mapper.DishMapper;
import com.heiren.rj.Po.Dish;
import com.heiren.rj.Service.DishFlavorService;
import com.heiren.rj.Service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heiren.rj.DTO.DishDto;
import com.heiren.rj.Po.DishFlavor;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;
     @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().peek((item) -> item.setDishId(dishId)).collect(Collectors.toList());

         dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据ID查询对应的口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //查询当前菜品的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 更新菜品的口味和信息
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish的基本信息
        this.updateById(dishDto);
        //清理当前的菜品信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 实现批量的删除
     * @param ids
     */
    @Override
    public void removeList(String ids) {
        List<Long> idList = Arrays.asList(ids.split(",")).
                stream().map(item ->
                        Long.parseLong(item)
                ).collect(Collectors.toList());
        this.removeByIds(idList);
    }

    @Override
    public void startSell(String ids) {
//        String[] idList = ids.split(",");
        List<Long> idLong = Arrays.asList(ids.split(",")).
                stream().map(a ->
                        Long.parseLong(a)
                ).collect(Collectors.toList());
        idLong.forEach(id -> {
            Dish dish = this.getBaseMapper().selectById(id);
            Integer status = dish.getStatus();
            if (status == 0) {
                dish.setStatus(1);
                this.updateById(dish);
            }
        });
    }

    @Override
    public void stopSell(String ids) {
        List<Long> idLong = Arrays.asList(ids.split(",")).
                stream().map(a ->
                        Long.parseLong(a)
                ).collect(Collectors.toList());
        idLong.forEach(id -> {
            Dish dish = this.getBaseMapper().selectById(id);
            Integer status = dish.getStatus();
            if (status == 1) {
                dish.setStatus(0);
                this.updateById(dish);
            }

        });
    }

}
