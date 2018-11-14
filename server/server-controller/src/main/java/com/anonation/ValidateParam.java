package com.anonation;

import com.model.Validator;
import com.model.Validator;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateParam {


    Validator[] value() default{};
    /*
    * 描述
    * */
    String name() default "";

}
