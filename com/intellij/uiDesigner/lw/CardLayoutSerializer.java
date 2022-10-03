/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import java.awt.CardLayout;
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
/*    */ public class CardLayoutSerializer
/*    */   extends LayoutSerializer
/*    */ {
/* 28 */   public static final CardLayoutSerializer INSTANCE = new CardLayoutSerializer();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void readLayout(Element element, LwContainer container) {
/* 34 */     int hGap = LwXmlReader.getOptionalInt(element, "hgap", 0);
/* 35 */     int vGap = LwXmlReader.getOptionalInt(element, "vgap", 0);
/* 36 */     container.setLayout(new CardLayout(hGap, vGap));
/*    */   }
/*    */   
/*    */   void readChildConstraints(Element constraintsElement, LwComponent component) {
/* 40 */     Element cardChild = LwXmlReader.getRequiredChild(constraintsElement, "card");
/* 41 */     String name = LwXmlReader.getRequiredString(cardChild, "name");
/* 42 */     component.setCustomLayoutConstraints(name);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\CardLayoutSerializer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */