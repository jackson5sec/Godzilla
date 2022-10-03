/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
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
/*    */ @GwtIncompatible
/*    */ public final class Defaults
/*    */ {
/* 32 */   private static final Double DOUBLE_DEFAULT = Double.valueOf(0.0D);
/* 33 */   private static final Float FLOAT_DEFAULT = Float.valueOf(0.0F);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> T defaultValue(Class<T> type) {
/* 42 */     Preconditions.checkNotNull(type);
/* 43 */     if (type == boolean.class)
/* 44 */       return (T)Boolean.FALSE; 
/* 45 */     if (type == char.class)
/* 46 */       return (T)Character.valueOf(false); 
/* 47 */     if (type == byte.class)
/* 48 */       return (T)Byte.valueOf((byte)0); 
/* 49 */     if (type == short.class)
/* 50 */       return (T)Short.valueOf((short)0); 
/* 51 */     if (type == int.class)
/* 52 */       return (T)Integer.valueOf(0); 
/* 53 */     if (type == long.class)
/* 54 */       return (T)Long.valueOf(0L); 
/* 55 */     if (type == float.class)
/* 56 */       return (T)FLOAT_DEFAULT; 
/* 57 */     if (type == double.class) {
/* 58 */       return (T)DOUBLE_DEFAULT;
/*    */     }
/* 60 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Defaults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */