package com.heiren.rj.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heiren.rj.Mapper.EmployeeMapper;
import com.heiren.rj.Po.Employee;
import com.heiren.rj.Service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
