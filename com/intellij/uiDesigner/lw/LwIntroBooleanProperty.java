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
/*    */ public final class LwIntroBooleanProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   public LwIntroBooleanProperty(String name) {
/* 23 */     super(name, Boolean.class.getName());
/*    */   }
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 27 */     return Boolean.valueOf(LwXmlReader.getRequiredString(element, "value"));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntroBooleanProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */