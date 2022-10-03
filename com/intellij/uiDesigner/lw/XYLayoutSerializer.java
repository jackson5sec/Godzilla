/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import com.intellij.uiDesigner.shared.XYLayoutManager;
/*    */ import java.awt.LayoutManager;
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
/*    */ public class XYLayoutSerializer
/*    */   extends LayoutSerializer
/*    */ {
/* 28 */   static XYLayoutSerializer INSTANCE = new XYLayoutSerializer();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void readLayout(Element element, LwContainer container) {
/* 34 */     container.setLayout((LayoutManager)new XYLayoutManager());
/*    */   }
/*    */   
/*    */   void readChildConstraints(Element constraintsElement, LwComponent component) {
/* 38 */     Element xyElement = LwXmlReader.getChild(constraintsElement, "xy");
/* 39 */     if (xyElement != null)
/* 40 */       component.setBounds(new Rectangle(LwXmlReader.getRequiredInt(xyElement, "x"), LwXmlReader.getRequiredInt(xyElement, "y"), LwXmlReader.getRequiredInt(xyElement, "width"), LwXmlReader.getRequiredInt(xyElement, "height"))); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\XYLayoutSerializer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */