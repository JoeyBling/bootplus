package io.github.common.typehandler;

import io.github.common.enums.EnumUtil;
import io.github.common.enums.IEnum;
import io.github.entity.enums.SysMenuTypeEnum;
import io.github.util.file.FileTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自定义枚举类型转换处理
 * mapper里字段到枚举类的映射。
 * <p>
 * 用法一:
 * 入库：#{enumDataField, typeHandler=io.github.common.typehandler.IEnumTypeHandler}
 * <p>
 * 出库：
 * <resultMap>
 * <result property="enumDataField" column="enum_data_field" javaType="com.xxx.MyEnum" typeHandler="io.github.common.typehandler.IEnumTypeHandler"/>
 * </resultMap>
 * <p>
 * 用法二： 1）在mybatis-config.xml中指定handler:
 * <typeHandlers>
 * <typeHandler handler="io.github.common.typehandler.IEnumTypeHandler" javaType="com.xxx.MyEnum"/>
 * </typeHandlers>
 * <p>
 * 2)在MyClassMapper.xml里直接select/update/insert。
 * <p>
 * eg.https://github.com/waterystone/spring-mybatis-test
 *
 * @author Created by 思伟 on 2020/4/1
 * @see io.github.common.enums.IEnum
 * 默认枚举处理类
 * @see org.apache.ibatis.type.EnumTypeHandler
 * @see TypeHandlerRegistry
 */
/* 数据库中的数据类型 */
//@MappedJdbcTypes(JdbcType.INTEGER)
/* 转化后的数据类型 */
@MappedTypes(value = {SysMenuTypeEnum.class, FileTypeEnum.class})
@Slf4j
public class IEnumTypeHandler<E extends Enum<?> & IEnum> extends BaseTypeHandler<E> implements MyTypeHandler {
    private Class<E> clazz;

    public IEnumTypeHandler(Class<E> enumType) {
        if (enumType == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.clazz = enumType;
        log.debug("自定义MyBatis类型转换器注册成功:{}", this.toString());
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getKey());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return EnumUtil.keyOf(clazz, rs.getString(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return EnumUtil.keyOf(clazz, rs.getString(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return EnumUtil.keyOf(clazz, cs.getString(columnIndex));
    }

    @Override
    public String toString() {
        return "IEnumTypeHandler{" +
                "clazz=" + clazz +
                '}';
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }
}