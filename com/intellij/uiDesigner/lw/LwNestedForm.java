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
/*    */ 
/*    */ public class LwNestedForm
/*    */   extends LwComponent
/*    */ {
/*    */   private String myFormFileName;
/*    */   
/*    */   public LwNestedForm() {
/* 29 */     super("");
/*    */   }
/*    */   
/*    */   public void read(Element element, PropertiesProvider provider) throws Exception {
/* 33 */     this.myFormFileName = LwXmlReader.getRequiredString(element, "form-file");
/* 34 */     readBase(element);
/* 35 */     readConstraints(element);
/*    */   }
/*    */   
/*    */   public String getFormFileName() {
/* 39 */     return this.myFormFileName;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwNestedForm.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */