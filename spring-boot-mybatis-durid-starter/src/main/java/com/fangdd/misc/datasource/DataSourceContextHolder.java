package com.fangdd.misc.datasource;

/**
 * @author lantian
 */
public class DataSourceContextHolder {
    public static String MASTER_KEY	= "master";
	public static String SLAVE_KEY	= "slave"; 
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
 
   public static void setDataSourceKey(String dataSourceKey) {
      contextHolder.set(dataSourceKey);
   }
 
   public static String getDataSourceKey() {
	  String dataSourceKey = (String) contextHolder.get();
      return dataSourceKey == null ? MASTER_KEY : (String) contextHolder.get();
   }
   
   public static String getRuntimeDataSourceKey(){
	   return (String) contextHolder.get();
   }
 
   public static void clearDataSourceKey() {
      contextHolder.remove();
   }

}
