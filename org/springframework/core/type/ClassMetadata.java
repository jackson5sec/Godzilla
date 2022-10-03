/*    */ package org.springframework.core.type;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ClassMetadata
/*    */ {
/*    */   String getClassName();
/*    */   
/*    */   boolean isInterface();
/*    */   
/*    */   boolean isAnnotation();
/*    */   
/*    */   boolean isAbstract();
/*    */   
/*    */   default boolean isConcrete() {
/* 59 */     return (!isInterface() && !isAbstract());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean isFinal();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean isIndependent();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean hasEnclosingClass() {
/* 82 */     return (getEnclosingClassName() != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   String getEnclosingClassName();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean hasSuperClass() {
/* 96 */     return (getSuperClassName() != null);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   String getSuperClassName();
/*    */   
/*    */   String[] getInterfaceNames();
/*    */   
/*    */   String[] getMemberClassNames();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\ClassMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */