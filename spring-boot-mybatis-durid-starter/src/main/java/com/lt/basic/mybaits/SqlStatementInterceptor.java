package com.lt.basic.mybaits;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Transaction;
import com.fangdd.logtrace.TraceId;
import com.fangdd.logtrace.TraceIdThreadLocal;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * SQL拦截器
 *
 * @author Jiahao li
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
        ResultHandler.class})})
public class SqlStatementInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(SqlStatementInterceptor.class);

    @SuppressWarnings("unused")
    private Properties properties;

    @Override
    public Object intercept(Invocation arg0) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement)arg0.getArgs()[0];
        Object parameter = null;
        if (arg0.getArgs().length > 1) {
            parameter = arg0.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();

        Transaction t = null;
        String methodName = null;
        try {
            methodName = this.getMethodName(mappedStatement);
            t = Cat.newTransaction(CatConstants.TYPE_SQL, methodName);
            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            logger.error("SqlStatementInterceptor.intercept error.", e);
            Cat.logError(e);
        }

        try {
            Object returnValue = arg0.proceed();

            // 次数try-catch包裹,以防cat打点发生异常,影响正常流程
            try {
                String sql = getSql(configuration, boundSql);
                logger.info("{} exec sql: {}", methodName, sql);
                TraceId traceId = TraceIdThreadLocal.get();
                if (t != null) {
                    t.addData(sql);
                    if (traceId != null) {
                        t.addData("fddTraceId", traceId.toString());
                    }
                }
            } catch (Exception e) {
                logger.error("SqlStatementInterceptor.intercept error.", e);
                Cat.logError(e);
            }
            return returnValue;
        } catch (Exception e) {
            logger.error("SqlStatementInterceptor.intercept error.", e);
            if (t != null) {
                t.setStatus(e);
            }
            throw e;
        } finally {
            if (t != null) {
                t.complete();
            }
        }
    }

    public static String getSql(Configuration configuration, BoundSql boundSql) {
        String sql = showSql(configuration, boundSql);
        StringBuilder str = new StringBuilder(100);
        str.append(sql);
        str.append(" ");
        return str.toString();
    }

    public static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return Matcher.quoteReplacement(value);
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties(Properties arg0) {
        this.properties = arg0;
    }

    private String getMethodName(MappedStatement mappedStatement) {
        String[] strArr = mappedStatement.getId().split("\\.");
        if (strArr.length > 1) {
            String methodName = strArr[strArr.length - 2] + "." + strArr[strArr.length - 1];
            return methodName;
        }
        return "UnknowMethod";
    }
}
