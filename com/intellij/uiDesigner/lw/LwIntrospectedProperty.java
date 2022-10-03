/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import org.jdom.Element;
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
/*    */ public abstract class LwIntrospectedProperty
/*    */   implements IProperty
/*    */ {
/*    */   private final String myName;
/*    */   private final String myPropertyClassName;
/*    */   private String myDeclaringClassName;
/*    */   
/*    */   public LwIntrospectedProperty(String name, String propertyClassName) {
/* 30 */     if (name == null) {
/* 31 */       throw new IllegalArgumentException("name cannot be null");
/*    */     }
/* 33 */     if (propertyClassName == null) {
/* 34 */       throw new IllegalArgumentException("propertyClassName cannot be null");
/*    */     }
/*    */     
/* 37 */     this.myName = name;
/* 38 */     this.myPropertyClassName = propertyClassName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 45 */     return this.myName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getPropertyClassName() {
/* 52 */     return this.myPropertyClassName;
/*    */   }
/*    */   
/*    */   public final String getReadMethodName() {
/* 56 */     return "get" + Character.toUpperCase(this.myName.charAt(0)) + this.myName.substring(1);
/*    */   }
/*    */   
/*    */   public final String getWriteMethodName() {
/* 60 */     return "set" + Character.toUpperCase(this.myName.charAt(0)) + this.myName.substring(1);
/*    */   }
/*    */   
/*    */   public String getDeclaringClassName() {
/* 64 */     return this.myDeclaringClassName;
/*    */   }
/*    */   
/*    */   public void setDeclaringClassName(String definingClassName) {
/* 68 */     this.myDeclaringClassName = definingClassName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract Object read(Element paramElement) throws Exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getPropertyValue(IComponent component) {
/* 83 */     return ((LwComponent)component).getPropertyValue(this);
/*    */   }
/*    */   
/*    */   public String getCodeGenPropertyClassName() {
/* 87 */     return getPropertyClassName();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntrospectedProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */