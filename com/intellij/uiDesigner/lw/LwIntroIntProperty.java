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
/*    */ public final class LwIntroIntProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   public LwIntroIntProperty(String name) {
/* 22 */     super(name, Integer.class.getName());
/*    */   }
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 26 */     return new Integer(LwXmlReader.getRequiredInt(element, "value"));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntroIntProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */