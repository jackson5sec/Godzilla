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
/*    */ public class LwIntroComponentProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   public LwIntroComponentProperty(String name, String propertyClassName) {
/* 26 */     super(name, propertyClassName);
/*    */   }
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 30 */     return LwXmlReader.getRequiredString(element, "value");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntroComponentProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */