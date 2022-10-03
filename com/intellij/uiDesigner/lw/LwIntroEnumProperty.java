/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class LwIntroEnumProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   private final Class myEnumClass;
/*    */   
/*    */   public LwIntroEnumProperty(String name, Class enumClass) {
/* 31 */     super(name, enumClass.getName());
/* 32 */     this.myEnumClass = enumClass;
/*    */   }
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 36 */     String value = element.getAttributeValue("value");
/* 37 */     Method method = this.myEnumClass.getMethod("valueOf", new Class[] { String.class });
/* 38 */     return method.invoke(null, new Object[] { value });
/*    */   }
/*    */   
/*    */   public String getCodeGenPropertyClassName() {
/* 42 */     return "java.lang.Enum";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntroEnumProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */