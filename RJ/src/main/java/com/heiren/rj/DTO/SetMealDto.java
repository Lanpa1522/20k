package com.heiren.rj.DTO;

import com.heiren.rj.Po.Setmeal;
import com.heiren.rj.Po.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetMealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
