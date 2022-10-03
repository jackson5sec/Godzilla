/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import org.jdom.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LwIntroCharProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   public LwIntroCharProperty(String name) {
/* 11 */     super(name, Character.class.getName());
/*    */   }
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 15 */     return Character.valueOf(LwXmlReader.getRequiredString(element, "value").charAt(0));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntroCharProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */