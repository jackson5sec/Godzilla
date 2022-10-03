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
/*    */ public final class LwRbIntroStringProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   public LwRbIntroStringProperty(String name) {
/* 26 */     super(name, String.class.getName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 33 */     StringDescriptor descriptor = LwXmlReader.getStringDescriptor(element, "value", "resource-bundle", "key");
/*    */ 
/*    */ 
/*    */     
/* 37 */     if (descriptor == null) {
/* 38 */       throw new IllegalArgumentException("String descriptor value required");
/*    */     }
/* 40 */     return descriptor;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwRbIntroStringProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */