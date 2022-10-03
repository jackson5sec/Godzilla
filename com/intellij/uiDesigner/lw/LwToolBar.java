/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
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
/*    */ public class LwToolBar
/*    */   extends LwContainer
/*    */ {
/*    */   public LwToolBar(String className) {
/* 28 */     super(className);
/*    */   }
/*    */   
/*    */   protected LayoutManager createInitialLayout() {
/* 32 */     return null;
/*    */   }
/*    */   
/*    */   public void read(Element element, PropertiesProvider provider) throws Exception {
/* 36 */     readNoLayout(element, provider);
/*    */   }
/*    */   
/*    */   protected void readConstraintsForChild(Element element, LwComponent component) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwToolBar.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */