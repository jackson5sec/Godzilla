/*    */ package com.google.common.reflect;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.Type;
/*    */ import java.lang.reflect.TypeVariable;
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
/*    */ @Beta
/*    */ public abstract class TypeParameter<T>
/*    */   extends TypeCapture<T>
/*    */ {
/*    */   final TypeVariable<?> typeVariable;
/*    */   
/*    */   protected TypeParameter() {
/* 43 */     Type type = capture();
/* 44 */     Preconditions.checkArgument(type instanceof TypeVariable, "%s should be a type variable.", type);
/* 45 */     this.typeVariable = (TypeVariable)type;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 50 */     return this.typeVariable.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean equals(Object o) {
/* 55 */     if (o instanceof TypeParameter) {
/* 56 */       TypeParameter<?> that = (TypeParameter)o;
/* 57 */       return this.typeVariable.equals(that.typeVariable);
/*    */     } 
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 64 */     return this.typeVariable.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\TypeParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */