/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import java.awt.Rectangle;
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
/*    */ public final class LwIntroRectangleProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   public LwIntroRectangleProperty(String name) {
/* 27 */     super(name, "java.awt.Rectangle");
/*    */   }
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 31 */     int x = LwXmlReader.getRequiredInt(element, "x");
/* 32 */     int y = LwXmlReader.getRequiredInt(element, "y");
/* 33 */     int width = LwXmlReader.getRequiredInt(element, "width");
/* 34 */     int height = LwXmlReader.getRequiredInt(element, "height");
/* 35 */     return new Rectangle(x, y, width, height);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntroRectangleProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */