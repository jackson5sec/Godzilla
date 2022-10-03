/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.springframework.expression.AccessException;
/*    */ import org.springframework.expression.ConstructorExecutor;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ReflectionUtils;
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
/*    */ public class ReflectiveConstructorExecutor
/*    */   implements ConstructorExecutor
/*    */ {
/*    */   private final Constructor<?> ctor;
/*    */   @Nullable
/*    */   private final Integer varargsPosition;
/*    */   
/*    */   public ReflectiveConstructorExecutor(Constructor<?> ctor) {
/* 45 */     this.ctor = ctor;
/* 46 */     if (ctor.isVarArgs()) {
/* 47 */       this.varargsPosition = Integer.valueOf(ctor.getParameterCount() - 1);
/*    */     } else {
/*    */       
/* 50 */       this.varargsPosition = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public TypedValue execute(EvaluationContext context, Object... arguments) throws AccessException {
/*    */     try {
/* 57 */       ReflectionHelper.convertArguments(context
/* 58 */           .getTypeConverter(), arguments, this.ctor, this.varargsPosition);
/* 59 */       if (this.ctor.isVarArgs()) {
/* 60 */         arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(this.ctor
/* 61 */             .getParameterTypes(), arguments);
/*    */       }
/* 63 */       ReflectionUtils.makeAccessible(this.ctor);
/* 64 */       return new TypedValue(this.ctor.newInstance(arguments));
/*    */     }
/* 66 */     catch (Exception ex) {
/* 67 */       throw new AccessException("Problem invoking constructor: " + this.ctor, ex);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Constructor<?> getConstructor() {
/* 72 */     return this.ctor;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\ReflectiveConstructorExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */