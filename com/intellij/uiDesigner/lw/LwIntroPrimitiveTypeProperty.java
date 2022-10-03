/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import org.jdom.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LwIntroPrimitiveTypeProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   private final Class myValueClass;
/*    */   
/*    */   public LwIntroPrimitiveTypeProperty(String name, Class valueClass) {
/* 13 */     super(name, valueClass.getName());
/* 14 */     this.myValueClass = valueClass;
/*    */   }
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 18 */     return LwXmlReader.getRequiredPrimitiveTypeValue(element, "value", this.myValueClass);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntroPrimitiveTypeProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */