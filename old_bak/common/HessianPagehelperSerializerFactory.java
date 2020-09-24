package com.tynet.frame.core.serializer.hessian;

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;
import com.github.pagehelper.Page;

/**
 * Mybatis-PageHelper分页对象在hessian传输的序列化方法
 * 
 * @author fulei
 * 
 */
public class HessianPagehelperSerializerFactory extends SerializerFactory {

    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        if (Page.class.isAssignableFrom(cl)) {
            return getMySerializer();
        }
        return super.getSerializer(cl);
    }

    @Override
    public Serializer getObjectSerializer(Class<?> cl) throws HessianProtocolException {
        if (Page.class.isAssignableFrom(cl)) { // 入口Page
            return getMySerializer();
        }
        return super.getObjectSerializer(cl);
    }

    @Override
    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        if (Page.class.equals(cl)) {
            return getMyDeserializer();
        }
        return super.getDeserializer(cl);
    }

    @Override
    public Deserializer getObjectDeserializer(String type) throws HessianProtocolException {
        if (Page.class.getName().equals(type)) {
            return getMyDeserializer();
        }
        return super.getObjectDeserializer(type);
    }

    @Override
    public Deserializer getObjectDeserializer(String type, Class cl) throws HessianProtocolException {
        if (cl != null && Page.class.isAssignableFrom(cl)) {
            return getMyDeserializer();
        } else if (Page.class.getName().equals(type)) {
            return getMyDeserializer();
        }
        return super.getObjectDeserializer(type, cl);
    }
    
    private Serializer getMySerializer() {
        return new PagehelperSerializer();
    }
    
    private Deserializer getMyDeserializer() {
        return new PagehelperDeserializer(Page.class);
    }
}
