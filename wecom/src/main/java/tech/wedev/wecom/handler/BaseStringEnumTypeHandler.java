package tech.wedev.wecom.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import tech.wedev.wecom.enums.BaseStringEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseStringEnumTypeHandler<E extends BaseStringEnum> extends BaseTypeHandler<E> {
    private Class<E> type;

    public BaseStringEnumTypeHandler(Class<E> enumType) {
        if (enumType == null) {
            throw new IllegalStateException("Type argument cannot be null");
        }
        if (!enumType.isEnum()) {
            throw new IllegalStateException("Type argument cannot be null, name: " + enumType.getName());
        }
        this.type = enumType;
    }

    public BaseStringEnumTypeHandler() {

    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    private E getBaseIntegerEnum(int code, boolean wasNull) {
        if (wasNull || type == null) {
            return null;
        }
        final Object[] enumConstants = type.getEnumConstants();
        for (int i = 0; i < enumConstants.length; i++) {
            final E enumConstant = (E) enumConstants[i];
            if (enumConstant.getCode().equals(code)) {
                return enumConstant;
            }
        }
        return null;
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getBaseIntegerEnum(rs.getInt(columnName), rs.wasNull());
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getBaseIntegerEnum(rs.getInt(columnIndex), rs.wasNull());
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getBaseIntegerEnum(cs.getInt(columnIndex), cs.wasNull());
    }
}
