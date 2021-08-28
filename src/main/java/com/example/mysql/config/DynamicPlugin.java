package com.example.mysql.config;

import com.example.mysql.DBContextHolder;
import com.example.mysql.enums.DBTypeEnum;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xiahui on 2018/3/12.
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class, CacheKey.class, BoundSql.class}
        )})
public class DynamicPlugin implements Interceptor {

    private static final String MASTER_SUFFIX = "dsMaster";
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    private static final Map<String, DBTypeEnum> cacheMap = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if (!synchronizationActive) {
            Object[] objects = invocation.getArgs();
            MappedStatement ms = (MappedStatement) objects[0];

            DBTypeEnum dbTypeEnum = null;

            if ((dbTypeEnum = cacheMap.get(ms.getId())) == null) {
                //读方法
                if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                    //!selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
                    //增加MASTER_SUFFIX，如果查询id前缀为master开头则默认走主库
                    if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX) || ms.getId().contains(MASTER_SUFFIX)) {
                        dbTypeEnum = DBTypeEnum.MASTER;
                    } else {
                        BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                        String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                        if (sql.matches(REGEX)) {
                            dbTypeEnum = DBTypeEnum.MASTER;
                        } else {
                            dbTypeEnum = DBTypeEnum.SLAVE;
                        }
                    }
                } else {
                    dbTypeEnum = DBTypeEnum.MASTER;
                }

                System.out.println(String.format("method [%s] use [%s] datasource, SqlCommandType [%s]..", ms.getId(), dbTypeEnum.name(), ms.getSqlCommandType().name()));
                cacheMap.put(ms.getId(), dbTypeEnum);
            }
            DBContextHolder.set(dbTypeEnum);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        //
    }
}
