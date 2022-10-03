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
/*    */ 
/*    */ public class LwAtomicComponent
/*    */   extends LwComponent
/*    */ {
/*    */   public LwAtomicComponent(String className) {
/* 26 */     super(className);
/*    */   }
/*    */   
/*    */   public void read(Element element, PropertiesProvider provider) throws Exception {
/* 30 */     readBase(element);
/* 31 */     readConstraints(element);
/* 32 */     readProperties(element, provider);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwAtomicComponent.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */