package com.heiren.rj.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heiren.rj.Common.R;
import com.heiren.rj.DTO.SetMealDto;
import com.heiren.rj.Po.Category;
import com.heiren.rj.Po.Setmeal;
import com.heiren.rj.Service.CategoryService;
import com.heiren.rj.Service.SetMealDishService;
import com.heiren.rj.Service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetMealDto setMealDto) {
        log.info("套餐信息: {}",setMealDto);
        setMealService.saveWithDish(setMealDto);
        return R.success("保存信息成功");
    }

    /**
     * 套餐分页查询
     */

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name) {
        //构造分页器对象
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetMealDto> setmealPage1 = new Page<>();

        //添加条件查询，根据name进行模糊查询
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序调价，根据更行时间进行排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setMealService.page(setmealPage,queryWrapper);

        //对象copy
        BeanUtils.copyProperties(setmealPage,setmealPage1,"records");
        //进行records的copy
        List<Setmeal> records = setmealPage.getRecords();
        List<SetMealDto> collect = records.stream().map((item) -> {
            SetMealDto setMealDto = new SetMealDto();
            //对象copy
            BeanUtils.copyProperties(item, setMealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category byId = categoryService.getById(categoryId);
            if (byId != null) {
                String name1 = byId.getName();
                setMealDto.setCategoryName(name1);
            }
            return setMealDto;
        }).collect(Collectors.toList());
        //将records保存至page1
        setmealPage1.setRecords(collect);
        return R.success(setmealPage1);
    }

    /**
     * delete
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("{}" ,ids);
        setMealService.removeWithIds(ids);
        return R.success("删除菜品成功");
    }
}
