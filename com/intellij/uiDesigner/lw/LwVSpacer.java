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
/*    */ public final class LwVSpacer
/*    */   extends LwAtomicComponent
/*    */ {
/*    */   public LwVSpacer() throws Exception {
/* 26 */     super("com.intellij.uiDesigner.core.Spacer");
/*    */   }
/*    */   
/*    */   public void read(Element element, PropertiesProvider provider) throws Exception {
/* 30 */     readBase(element);
/* 31 */     readConstraints(element);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwVSpacer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */