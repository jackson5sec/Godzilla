/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import com.intellij.uiDesigner.compiler.GridBagConverter;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.GridBagLayout;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GridBagLayoutSerializer
/*    */   extends GridLayoutSerializer
/*    */ {
/* 35 */   public static GridBagLayoutSerializer INSTANCE = new GridBagLayoutSerializer();
/*    */   
/*    */   void readLayout(Element element, LwContainer container) {
/* 38 */     container.setLayout(new GridBagLayout());
/*    */   }
/*    */   
/*    */   void readChildConstraints(Element constraintsElement, LwComponent component) {
/* 42 */     super.readChildConstraints(constraintsElement, component);
/* 43 */     GridBagConstraints gbc = new GridBagConstraints();
/* 44 */     GridBagConverter.constraintsToGridBag(component.getConstraints(), gbc);
/* 45 */     Element gridBagElement = LwXmlReader.getChild(constraintsElement, "gridbag");
/* 46 */     if (gridBagElement != null) {
/* 47 */       if (gridBagElement.getAttributeValue("top") != null) {
/* 48 */         gbc.insets = LwXmlReader.readInsets(gridBagElement);
/*    */       }
/* 50 */       gbc.weightx = LwXmlReader.getOptionalDouble(gridBagElement, "weightx", 0.0D);
/* 51 */       gbc.weighty = LwXmlReader.getOptionalDouble(gridBagElement, "weighty", 0.0D);
/* 52 */       gbc.ipadx = LwXmlReader.getOptionalInt(gridBagElement, "ipadx", 0);
/* 53 */       gbc.ipady = LwXmlReader.getOptionalInt(gridBagElement, "ipady", 0);
/*    */     } 
/* 55 */     component.setCustomLayoutConstraints(gbc);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\GridBagLayoutSerializer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */