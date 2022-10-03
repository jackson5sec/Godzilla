/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import java.awt.FlowLayout;
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
/*    */ public class FlowLayoutSerializer
/*    */   extends LayoutSerializer
/*    */ {
/* 28 */   public static final FlowLayoutSerializer INSTANCE = new FlowLayoutSerializer();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void readLayout(Element element, LwContainer container) {
/* 34 */     int hGap = LwXmlReader.getOptionalInt(element, "hgap", 5);
/* 35 */     int vGap = LwXmlReader.getOptionalInt(element, "vgap", 5);
/* 36 */     int flowAlign = LwXmlReader.getOptionalInt(element, "flow-align", 1);
/* 37 */     container.setLayout(new FlowLayout(flowAlign, hGap, vGap));
/*    */   }
/*    */   
/*    */   void readChildConstraints(Element constraintsElement, LwComponent component) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\FlowLayoutSerializer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */