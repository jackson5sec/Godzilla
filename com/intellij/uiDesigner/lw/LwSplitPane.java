/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import com.intellij.uiDesigner.compiler.UnexpectedFormElementException;
/*    */ import java.awt.LayoutManager;
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
/*    */ public final class LwSplitPane
/*    */   extends LwContainer
/*    */ {
/*    */   public static final String POSITION_LEFT = "left";
/*    */   public static final String POSITION_RIGHT = "right";
/*    */   
/*    */   public LwSplitPane(String className) {
/* 32 */     super(className);
/*    */   }
/*    */   
/*    */   protected LayoutManager createInitialLayout() {
/* 36 */     return null;
/*    */   }
/*    */   
/*    */   public void read(Element element, PropertiesProvider provider) throws Exception {
/* 40 */     readNoLayout(element, provider);
/*    */   }
/*    */   
/*    */   protected void readConstraintsForChild(Element element, LwComponent component) {
/* 44 */     Element constraintsElement = LwXmlReader.getRequiredChild(element, "constraints");
/* 45 */     Element splitterChild = LwXmlReader.getRequiredChild(constraintsElement, "splitpane");
/* 46 */     String position = LwXmlReader.getRequiredString(splitterChild, "position");
/* 47 */     if ("left".equals(position)) {
/* 48 */       component.setCustomLayoutConstraints("left");
/*    */     }
/* 50 */     else if ("right".equals(position)) {
/* 51 */       component.setCustomLayoutConstraints("right");
/*    */     } else {
/*    */       
/* 54 */       throw new UnexpectedFormElementException("unexpected position: " + position);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwSplitPane.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */