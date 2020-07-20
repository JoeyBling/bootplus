package io.github.common.typehandler;

import com.alibaba.fastjson.JSON;
import io.github.common.enums.IEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mapper里json型字段到类的映射。
 * 用法一:
 * 入库：#{jsonDataField, typeHandler=com.adu.spring_test.mybatis.typehandler.JsonTypeHandler}
 * 出库：
 * <resultMap>
 * <result property="jsonDataField" column="json_data_field" javaType="com.xxx.MyClass" typeHandler="com.adu.spring_test.mybatis.typehandler.JsonTypeHandler"/>
 * </resultMap>
 * <p>
 * 用法二：
 * 1）在mybatis-config.xml中指定handler:
 * <typeHandlers>
 * <typeHandler handler="com.adu.spring_test.mybatis.typehandler.JsonTypeHandler" javaType="com.xxx.MyClass"/>
 * </typeHandlers>
 * 2)在MyClassMapper.xml里直接select/update/insert。
 *
 * @author Created by 思伟 on 2020/4/1
 */
/* 数据库中的数据类型 */
//@MappedJdbcTypes(JdbcType.VARCHAR)
/* 转化后的数据类型 */
@MappedTypes(value = {IEnum.class})
@Slf4j
public class JsonTypeHandler<T extends Object> extends BaseTypeHandler<T> implements MyTypeHandler {

    private Class<T> clazz;

    public JsonTypeHandler() {
        log.debug("自定义MyBatis类型转换器注册成功:{}", this.toString());
    }

    public JsonTypeHandler(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.clazz = clazz;
        log.debug("自定义MyBatis类型转换器注册成功:{}", this.toString());
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, this.toJson(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.toObject(rs.getString(columnName), clazz);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.toObject(rs.getString(columnIndex), clazz);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.toObject(cs.getString(columnIndex), clazz);
    }

    private String toJson(T object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private T toObject(String content, Class<T> clazz) {
        if (content != null && !content.isEmpty()) {
            try {
                return JSON.parseObject(content, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "JsonTypeHandler{" +
                "clazz=" + clazz +
                '}';
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }

}
