package dev.rennen.jdbc.core.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * 将 JDBC 里向 PreparedStatement 中传参数的代码进行包装 <br/> 2025/2/3 17:38
 *
 * @author rennen.dev
 */
public class ArgumentPreparedStatementSetter {

    // 参数数组
    private final Object[] args;

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    //设置SQL参数
    public void setValues(PreparedStatement pstmt) throws SQLException {
        if (this.args != null) {
            for (int i = 0; i < this.args.length; i++) {
                Object arg = this.args[i];
                doSetValue(pstmt, i + 1, arg);
            }
        }
    }

    //对某个参数，设置参数值
    protected void doSetValue(PreparedStatement pstmt, int parameterPosition, Object argValue) throws SQLException {
        //判断参数类型，调用相应的JDBC set方法
        if (argValue instanceof String strArg) {
            pstmt.setString(parameterPosition, strArg);
        } else if (argValue instanceof Integer integerArg) {
            pstmt.setInt(parameterPosition, integerArg);
        } else if (argValue instanceof Date dateArg) {
            pstmt.setDate(parameterPosition, new java.sql.Date(dateArg.getTime()));
        }
    }
}
