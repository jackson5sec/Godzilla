/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import java.awt.Dimension;
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
/*    */ public final class LwIntroDimensionProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   public LwIntroDimensionProperty(String name) {
/* 27 */     super(name, "java.awt.Dimension");
/*    */   }
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 31 */     int width = LwXmlReader.getRequiredInt(element, "width");
/* 32 */     int height = LwXmlReader.getRequiredInt(element, "height");
/* 33 */     return new Dimension(width, height);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntroDimensionProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */