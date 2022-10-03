/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.expression.PropertyAccessor;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AstUtils
/*    */ {
/*    */   public static List<PropertyAccessor> getPropertyAccessorsToTry(@Nullable Class<?> targetType, List<PropertyAccessor> propertyAccessors) {
/* 48 */     List<PropertyAccessor> specificAccessors = new ArrayList<>();
/* 49 */     List<PropertyAccessor> generalAccessors = new ArrayList<>();
/* 50 */     for (PropertyAccessor resolver : propertyAccessors) {
/* 51 */       Class<?>[] targets = resolver.getSpecificTargetClasses();
/* 52 */       if (targets == null) {
/* 53 */         generalAccessors.add(resolver);
/*    */         continue;
/*    */       } 
/* 56 */       if (targetType != null) {
/* 57 */         for (Class<?> clazz : targets) {
/* 58 */           if (clazz == targetType) {
/* 59 */             specificAccessors.add(resolver);
/*    */           }
/* 61 */           else if (clazz.isAssignableFrom(targetType)) {
/*    */             
/* 63 */             generalAccessors.add(resolver);
/*    */           } 
/*    */         } 
/*    */       }
/*    */     } 
/*    */     
/* 69 */     List<PropertyAccessor> resolvers = new ArrayList<>(specificAccessors.size() + generalAccessors.size());
/* 70 */     resolvers.addAll(specificAccessors);
/* 71 */     resolvers.addAll(generalAccessors);
/* 72 */     return resolvers;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\AstUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */