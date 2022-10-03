/*    */ package org.springframework.core.type.filter;
/*    */ 
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
/*    */ public class AssignableTypeFilter
/*    */   extends AbstractTypeHierarchyTraversingFilter
/*    */ {
/*    */   private final Class<?> targetType;
/*    */   
/*    */   public AssignableTypeFilter(Class<?> targetType) {
/* 40 */     super(true, true);
/* 41 */     this.targetType = targetType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Class<?> getTargetType() {
/* 49 */     return this.targetType;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean matchClassName(String className) {
/* 54 */     return this.targetType.getName().equals(className);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Boolean matchSuperClass(String superClassName) {
/* 60 */     return matchTargetType(superClassName);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Boolean matchInterface(String interfaceName) {
/* 66 */     return matchTargetType(interfaceName);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   protected Boolean matchTargetType(String typeName) {
/* 71 */     if (this.targetType.getName().equals(typeName)) {
/* 72 */       return Boolean.valueOf(true);
/*    */     }
/* 74 */     if (Object.class.getName().equals(typeName)) {
/* 75 */       return Boolean.valueOf(false);
/*    */     }
/* 77 */     if (typeName.startsWith("java")) {
/*    */       try {
/* 79 */         Class<?> clazz = ClassUtils.forName(typeName, getClass().getClassLoader());
/* 80 */         return Boolean.valueOf(this.targetType.isAssignableFrom(clazz));
/*    */       }
/* 82 */       catch (Throwable throwable) {}
/*    */     }
/*    */ 
/*    */     
/* 86 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\filter\AssignableTypeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */