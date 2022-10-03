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
/*    */ public class LwIntroFontProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   public LwIntroFontProperty(String name) {
/* 25 */     super(name, "java.awt.Font");
/*    */   }
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 29 */     return LwXmlReader.getFontDescriptor(element);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntroFontProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */