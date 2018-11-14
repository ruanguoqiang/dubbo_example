package com.config;

import com.anonation.ValidateParam;
import com.model.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodArgumentResovler implements HandlerMethodArgumentResolver {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Map<MethodParameter,ParamInfo> paramInfoCache=new ConcurrentHashMap<MethodParameter, ParamInfo>();
    public MethodArgumentResovler() {
        super();
    }

    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(ValidateParam.class)){
            return true;
        }
        else {
            return  false;
        }
    }

    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
         ParamInfo paramInfo=getParamInfo(methodParameter);
         Object value=null;
         String[] parameValue=nativeWebRequest.getParameterValues(paramInfo.paramName);
         if(parameValue!=null){
             value=parameValue.length==1?parameValue[0]:parameValue;
         }
         if (value==null){
             value=nativeWebRequest.getAttribute(paramInfo.paramName, RequestAttributes.SCOPE_REQUEST);
         }
         if (paramInfo.validators!=null){
             validateValue(value, paramInfo.name, paramInfo.validators, methodParameter);
         }
        if (webDataBinderFactory != null) {
            WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, null, paramInfo.name);
            value = binder.convertIfNecessary(value, methodParameter.getParameterType(), methodParameter);
        }
        return value;
    }

    private ParamInfo getParamInfo(MethodParameter parameter) {
        ParamInfo paramInfo=paramInfoCache.get(parameter);
        if (paramInfo==null){
            paramInfo=createParamInfo(parameter);
            paramInfoCache.put(parameter,paramInfo);
        }
        return paramInfo;
    }
    protected ParamInfo createParamInfo(MethodParameter parameter) {
            ValidateParam validateParam=parameter.getParameterAnnotation(ValidateParam.class);
            ParamInfo info=new ParamInfo(parameter.getParameterName());
            if (validateParam!=null){
                if (StringUtils.isBlank(parameter.getParameterName())){
                    info.setName("");
                }
                else {
                    info.setName(parameter.getParameterName());
                }
                info.setValidators(validateParam.value());
            }
            return info;
    }
    private void validateValue(Object value, String cName, Validator[] validators, MethodParameter parameter) {
        {
            for (int i = 0; i < validators.length; i++) {
                Validator validator = Validator.getValidator(validators[i]);
                if (validator != null) {
                    if (value != null && value.toString().trim() != "") {
                        validator.validate(cName, value.toString());
                    } else {
                        if (Validator.NOT_BLANK.equals(validator)) {
                            validator.validate(cName, null);
                        }
                    }
                }
                else {
                    logger.error("验证器[" + validators[i] + "],在Validator.java文件中没有定义，请检查！");
                }
            }

        }
    }

    protected static class ParamInfo{
        private String paramName;

        private String name;

        private Validator[] validators;

        public ParamInfo(){
            super();
        }
        public ParamInfo(String paramName) {
            super();
            this.paramName = paramName;
        }

        public String getParamName() {
            return paramName;
        }

        public void setParamName(String paramName) {
            this.paramName = paramName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Validator[] getValidators() {
            return validators;
        }

        public void setValidators(Validator[] validators) {
            this.validators = validators;
        }
    }
}
