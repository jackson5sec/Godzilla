/*    */ package com.google.common.reflect;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import java.lang.reflect.Type;
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
/*    */ abstract class TypeCapture<T>
/*    */ {
/*    */   final Type capture() {
/* 31 */     Type superclass = getClass().getGenericSuperclass();
/* 32 */     Preconditions.checkArgument(superclass instanceof ParameterizedType, "%s isn't parameterized", superclass);
/* 33 */     return ((ParameterizedType)superclass).getActualTypeArguments()[0];
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\TypeCapture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */