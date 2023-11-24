package com.heiren.rj.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heiren.rj.Common.R;
import com.heiren.rj.DTO.DishDto;
import com.heiren.rj.Po.Category;
import com.heiren.rj.Po.Dish;
import com.heiren.rj.Service.CategoryService;
import com.heiren.rj.Service.DishFlavorService;
import com.heiren.rj.Service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Transactional
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品的分页
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        //拷贝
        Page<DishDto> dishInfo = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name != null, Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);


        //除了records不拷贝
        BeanUtils.copyProperties(pageInfo,dishInfo,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();
            //根据Id查询对象
            Category byId = categoryService.getById(categoryId);
            if (byId!=null) {
                String categoryName = byId.getName();
                dishDto.setCategoryName(categoryName);
                }

            return dishDto;
        }).collect(Collectors.toList());

        dishInfo.setRecords(list);
        return R.success(dishInfo);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
        return R.success(byIdWithFlavor);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 更改销售状态
     * @param
     * @return
     */
    @PostMapping("/status/1")
    public R<String> updateByDishId0(String ids) {
        dishService.startSell(ids);
        return R.success("Success0");
    }

    @PostMapping("/status/0")
    public R<String> updateByDishId1(String ids) {
        dishService.stopSell(ids);
        return R.success("Success1");
    }



    /**
     * 批量删除
     */
    @DeleteMapping
    public R<String> deleteList(String ids) {
        dishService.removeList(ids);
        return R.success("批量删除成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        //sort
        queryWrapper.orderByAsc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }
}
