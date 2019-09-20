package com.lt.basic.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Repository;

/**
 * @author lantian
 */
@Aspect
@Repository
public class SlaveManager {
	
	@Pointcut("@annotation(com.lt.basic.annotation.UseSlave)")
	private void useSlave(){}
	
	@Pointcut("@annotation(com.lt.basic.annotation.UseMaster)")
	private void useMaster(){}
	
	@Around("useMaster()")
	public Object useMaster(ProceedingJoinPoint joinPoint) throws Throwable{
		DataSourceContextHolder.setDataSourceKey("master");
		
		try{
			return joinPoint.proceed();
		}catch(Throwable e){
			throw e;
		}finally{
			DataSourceContextHolder.clearDataSourceKey();
		}
	}
	
	@Around("useSlave()")
	public Object useSlave(ProceedingJoinPoint joinPoint) throws Throwable{
		String key = DataSourceContextHolder.getRuntimeDataSourceKey();
		if(!DataSourceContextHolder.MASTER_KEY.equals(key)){
			DataSourceContextHolder.setDataSourceKey(DataSourceContextHolder.SLAVE_KEY);
		}
		
		try{
			return joinPoint.proceed();
		}catch(Throwable e){
			throw e;
		}finally{
			DataSourceContextHolder.clearDataSourceKey();
		}
	}
}
