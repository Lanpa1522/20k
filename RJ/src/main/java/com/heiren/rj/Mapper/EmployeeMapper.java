package com.heiren.rj.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heiren.rj.Po.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
