package tech.wedev.wecom.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import tech.wedev.wecom.enums.BaseIntegerEnum;
import tech.wedev.wecom.enums.BaseStringEnum;
import tech.wedev.wecom.utils.ReflectUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * CommonProvider使用
 */
public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (ReflectUtils.isSuperInterface(type, BaseIntegerEnum.class)) {
            BaseIntegerEnumTypeHandler baseIntegerEnumTypeHandler = new BaseIntegerEnumTypeHandler(type);
            baseIntegerEnumTypeHandler.setNonNullParameter(ps, i, (BaseIntegerEnum) parameter, jdbcType);
            return;
        } else if (ReflectUtils.isSuperInterface(type, BaseStringEnum.class)) {
            BaseStringEnumTypeHandler baseStringEnumTypeHandler = new BaseStringEnumTypeHandler(type);
            baseStringEnumTypeHandler.setNonNullParameter(ps, i, (BaseStringEnum) parameter, jdbcType);
            return;
        }

        if (jdbcType == null) {
            ps.setString(i, parameter.name());
        } else {
            ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (ReflectUtils.isSuperInterface(type, BaseIntegerEnum.class)) {
            BaseIntegerEnumTypeHandler baseIntegerEnumTypeHandler = new BaseIntegerEnumTypeHandler(type);
            return (E) baseIntegerEnumTypeHandler.getNullableResult(rs, columnName);
        } else if (ReflectUtils.isSuperInterface(type, BaseStringEnum.class)) {
            BaseStringEnumTypeHandler baseStringEnumTypeHandler = new BaseStringEnumTypeHandler(type);
            return (E) baseStringEnumTypeHandler.getNullableResult(rs, columnName);
        }

        String s = rs.getString(columnName);
        return s == null ? null : Enum.valueOf(type, s);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (ReflectUtils.isSuperInterface(type, BaseIntegerEnum.class)) {
            BaseIntegerEnumTypeHandler baseIntegerEnumTypeHandler = new BaseIntegerEnumTypeHandler(type);
            return (E) baseIntegerEnumTypeHandler.getNullableResult(rs, columnIndex);
        } else if (ReflectUtils.isSuperInterface(type, BaseStringEnum.class)) {
            BaseStringEnumTypeHandler baseStringEnumTypeHandler = new BaseStringEnumTypeHandler(type);
            return (E) baseStringEnumTypeHandler.getNullableResult(rs, columnIndex);
        }

        String s = rs.getString(columnIndex);
        return s == null ? null : Enum.valueOf(type, s);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (ReflectUtils.isSuperInterface(type, BaseIntegerEnum.class)) {
            BaseIntegerEnumTypeHandler baseIntegerEnumTypeHandler = new BaseIntegerEnumTypeHandler(type);
            return (E) baseIntegerEnumTypeHandler.getNullableResult(cs, columnIndex);
        } else if (ReflectUtils.isSuperInterface(type, BaseStringEnum.class)) {
            BaseStringEnumTypeHandler baseStringEnumTypeHandler = new BaseStringEnumTypeHandler(type);
            return (E) baseStringEnumTypeHandler.getNullableResult(cs, columnIndex);
        }

        String s = cs.getString(columnIndex);
        return s == null ? null : Enum.valueOf(type, s);
    }
}