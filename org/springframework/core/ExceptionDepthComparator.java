/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExceptionDepthComparator
/*    */   implements Comparator<Class<? extends Throwable>>
/*    */ {
/*    */   private final Class<? extends Throwable> targetException;
/*    */   
/*    */   public ExceptionDepthComparator(Throwable exception) {
/* 43 */     Assert.notNull(exception, "Target exception must not be null");
/* 44 */     this.targetException = (Class)exception.getClass();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExceptionDepthComparator(Class<? extends Throwable> exceptionType) {
/* 52 */     Assert.notNull(exceptionType, "Target exception type must not be null");
/* 53 */     this.targetException = exceptionType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(Class<? extends Throwable> o1, Class<? extends Throwable> o2) {
/* 59 */     int depth1 = getDepth(o1, this.targetException, 0);
/* 60 */     int depth2 = getDepth(o2, this.targetException, 0);
/* 61 */     return depth1 - depth2;
/*    */   }
/*    */   
/*    */   private int getDepth(Class<?> declaredException, Class<?> exceptionToMatch, int depth) {
/* 65 */     if (exceptionToMatch.equals(declaredException))
/*    */     {
/* 67 */       return depth;
/*    */     }
/*    */     
/* 70 */     if (exceptionToMatch == Throwable.class) {
/* 71 */       return Integer.MAX_VALUE;
/*    */     }
/* 73 */     return getDepth(declaredException, exceptionToMatch.getSuperclass(), depth + 1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Class<? extends Throwable> findClosestMatch(Collection<Class<? extends Throwable>> exceptionTypes, Throwable targetException) {
/* 86 */     Assert.notEmpty(exceptionTypes, "Exception types must not be empty");
/* 87 */     if (exceptionTypes.size() == 1) {
/* 88 */       return exceptionTypes.iterator().next();
/*    */     }
/* 90 */     List<Class<? extends Throwable>> handledExceptions = new ArrayList<>(exceptionTypes);
/* 91 */     handledExceptions.sort(new ExceptionDepthComparator(targetException));
/* 92 */     return handledExceptions.get(0);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\ExceptionDepthComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */