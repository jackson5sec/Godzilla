/*    */ package util;
/*    */ 
/*    */ import core.ApplicationContext;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class Log
/*    */ {
/*    */   public static void log(String data, Object... values) {
/* 10 */     echo("*", String.format(data, values));
/*    */   }
/*    */   public static void error(Throwable exception) {
/* 13 */     String stackTrace = "";
/* 14 */     StackTraceElement[] elements = exception.getStackTrace();
/* 15 */     for (int i = 0; i < elements.length; i++) {
/* 16 */       stackTrace = stackTrace + elements[i] + "->";
/*    */     }
/* 18 */     if (stackTrace.length() > 2) {
/* 19 */       stackTrace = stackTrace.substring(0, stackTrace.length() - 2);
/*    */     }
/* 21 */     echo("!", String.format("%s stackTrace: %s", new Object[] { exception.getMessage(), stackTrace }));
/*    */   }
/*    */   public static void error(String data) {
/* 24 */     echo("!", data);
/*    */   }
/*    */   private static void echo(String identification, String message) {
/* 27 */     String data = null;
/* 28 */     if (ApplicationContext.isOpenC("isSuperLog")) {
/* 29 */       data = String.format("[%s] Time:%s LastStackTrace:%s ThreadId:%s Message: %s", new Object[] { identification, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()), getLastStackTrace(), Long.valueOf(Thread.currentThread().getId()), message });
/*    */     } else {
/* 31 */       data = String.format("[%s] Time:%s ThreadId:%s Message: %s", new Object[] { identification, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()), Long.valueOf(Thread.currentThread().getId()), message });
/*    */     } 
/* 33 */     System.out.println(data);
/*    */   }
/*    */   
/*    */   private static String getLastStackTrace() {
/* 37 */     String className = Thread.currentThread().getStackTrace()[4].getClassName();
/* 38 */     String methodName = Thread.currentThread().getStackTrace()[4].getMethodName();
/* 39 */     int line = Thread.currentThread().getStackTrace()[4].getLineNumber();
/* 40 */     return String.format("%s->%s<->%s", new Object[] { className, methodName, Integer.valueOf(line) });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\Log.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */