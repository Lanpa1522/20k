package com.heiren.rj.Common;

import lombok.Data;

@Data
public class CustomerException extends  RuntimeException{

    public CustomerException(String message) {
        super(message);
    }

}
