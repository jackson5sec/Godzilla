/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.StringJoiner;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ abstract class FormatHelper
/*    */ {
/*    */   public static String formatMethodForMessage(String name, List<TypeDescriptor> argumentTypes) {
/* 40 */     StringJoiner sj = new StringJoiner(",", "(", ")");
/* 41 */     for (TypeDescriptor typeDescriptor : argumentTypes) {
/* 42 */       if (typeDescriptor != null) {
/* 43 */         sj.add(formatClassNameForMessage(typeDescriptor.getType()));
/*    */         continue;
/*    */       } 
/* 46 */       sj.add(formatClassNameForMessage(null));
/*    */     } 
/*    */     
/* 49 */     return name + sj.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String formatClassNameForMessage(@Nullable Class<?> clazz) {
/* 60 */     return (clazz != null) ? ClassUtils.getQualifiedName(clazz) : "null";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\FormatHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */