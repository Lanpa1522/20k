package com.heiren.rj.DTO;

import com.heiren.rj.Po.Dish;
import com.heiren.rj.Po.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
