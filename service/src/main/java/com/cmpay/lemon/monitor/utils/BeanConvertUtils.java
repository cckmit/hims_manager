package com.cmpay.lemon.monitor.utils;

import com.cmpay.lemon.common.exception.BusinessException;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class BeanConvertUtils {

    private static Logger logger = LoggerFactory.getLogger(BeanConvertUtils.class);

    /**
     * 对象拷贝
     *
     * @param dest   目标对象
     * @param source 源对象
     * @return void
     * @throws BusinessException
     */
    public static <T1, T2> void convert(T1 dest, T2 source) throws BusinessException {
        convert(dest, source, new DefaultConvert());
    }

    public static <T1, T2> void convert(T1 dest, T2 source, Converter converter) throws BusinessException {
        if (null == dest || null == source) {
            return;
        }
        try {
            BeanCopier copier = BeanCopier.create(source.getClass(), dest.getClass(), converter != null);
            copier.copy(source, dest, converter);
        } catch (Exception e) {
            logger.error("", e);
            throw new BusinessException("系统异常");
        }
    }

    /**
     * 批量转换
     *
     * @param data
     * @param clazz
     * @return List<T2>
     * @throws BusinessException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T2, T1> List<T2> convertList(List<T1> data, Class<T2> clazz) throws BusinessException {
        return convertList(data, clazz, new DefaultConvert());
    }


    /**
     * 批量转换
     *
     * @param data
     * @param clazz
     * @param converter
     * @return List<T2>
     * @throws BusinessException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T2, T1> List<T2> convertList(List<T1> data, Class<T2> clazz, Converter converter) throws BusinessException {
        if (data == null) {
            return null;
        }

        List<T2> t2 = new ArrayList<T2>();
        if (null != data && data.size() > 0) {
            BeanCopier copier = BeanCopier.create(data.get(0).getClass(), clazz, converter != null);
            for (T1 t1 : data) {
                T2 _t2;
                try {
                    _t2 = clazz.newInstance();
                } catch (Exception e) {
                    throw new BusinessException("系统异常");
                }
                copier.copy(t1, _t2, converter);
                t2.add(_t2);
            }
        }
        return t2;
    }

    static class DefaultConvert implements Converter {

        @SuppressWarnings("rawtypes")
        @Override
        public Object convert(Object value, Class target, Object context) {
            if (value instanceof Integer) {
                return new Integer(NumberUtils.toInt(value.toString()));
            } else if (value instanceof Float) {
                return new Float(NumberUtils.toFloat(value.toString()));
            } else if (value instanceof Double) {
                return new Double(NumberUtils.toDouble(value.toString()));
            } else if (value instanceof Short) {
                return new Short(NumberUtils.toShort(value.toString()));
            } else if (value instanceof BigDecimal) {
                return (BigDecimal) value;
            }
            return value;
        }

    }
}
