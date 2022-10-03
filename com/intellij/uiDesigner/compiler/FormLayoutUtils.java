/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import com.jgoodies.forms.layout.FormLayout;
/*    */ import com.jgoodies.forms.layout.FormSpec;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FormLayoutUtils
/*    */ {
/*    */   public static String getEncodedRowSpecs(FormLayout formLayout) {
/* 33 */     StringBuffer result = new StringBuffer();
/* 34 */     for (int i = 1; i <= formLayout.getRowCount(); i++) {
/* 35 */       if (result.length() > 0) {
/* 36 */         result.append(",");
/*    */       }
/* 38 */       result.append(getEncodedSpec((FormSpec)formLayout.getRowSpec(i)));
/*    */     } 
/* 40 */     return result.toString();
/*    */   }
/*    */   
/*    */   public static String getEncodedColumnSpecs(FormLayout formLayout) {
/* 44 */     StringBuffer result = new StringBuffer();
/* 45 */     for (int i = 1; i <= formLayout.getColumnCount(); i++) {
/* 46 */       if (result.length() > 0) {
/* 47 */         result.append(",");
/*    */       }
/* 49 */       result.append(getEncodedSpec((FormSpec)formLayout.getColumnSpec(i)));
/*    */     } 
/* 51 */     return result.toString();
/*    */   }
/*    */   
/*    */   public static String getEncodedSpec(FormSpec formSpec) {
/* 55 */     String result = formSpec.toString();
/*    */     while (true) {
/* 57 */       int pos = result.indexOf("dluX");
/* 58 */       if (pos < 0) {
/* 59 */         pos = result.indexOf("dluY");
/*    */       }
/* 61 */       if (pos < 0) {
/*    */         break;
/*    */       }
/* 64 */       result = result.substring(0, pos + 3) + result.substring(pos + 4);
/*    */     } 
/*    */ 
/*    */     
/* 68 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\FormLayoutUtils.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */