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
/*    */ public class LwIntroColorProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   public LwIntroColorProperty(String name) {
/* 25 */     super(name, "java.awt.Color");
/*    */   }
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 29 */     return LwXmlReader.getColorDescriptor(element);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntroColorProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */