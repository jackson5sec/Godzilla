/*    */ package com.kitfox.svg.xml;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ public class StyleSheet
/*    */ {
/* 18 */   HashMap<StyleSheetRule, String> ruleMap = new HashMap<StyleSheetRule, String>();
/*    */ 
/*    */ 
/*    */   
/*    */   public static StyleSheet parseSheet(String src) {
/* 23 */     Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "CSS parser not implemented yet");
/*    */     
/* 25 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addStyleRule(StyleSheetRule rule, String value) {
/* 30 */     this.ruleMap.put(rule, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean getStyle(StyleAttribute attrib, String tagName, String cssClass) {
/* 35 */     StyleSheetRule rule = new StyleSheetRule(attrib.getName(), tagName, cssClass);
/* 36 */     String value = this.ruleMap.get(rule);
/*    */     
/* 38 */     if (value != null) {
/*    */       
/* 40 */       attrib.setStringValue(value);
/* 41 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 45 */     rule = new StyleSheetRule(attrib.getName(), null, cssClass);
/* 46 */     value = this.ruleMap.get(rule);
/*    */     
/* 48 */     if (value != null) {
/*    */       
/* 50 */       attrib.setStringValue(value);
/* 51 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 55 */     rule = new StyleSheetRule(attrib.getName(), tagName, null);
/* 56 */     value = this.ruleMap.get(rule);
/*    */     
/* 58 */     if (value != null) {
/*    */       
/* 60 */       attrib.setStringValue(value);
/* 61 */       return true;
/*    */     } 
/*    */     
/* 64 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\xml\StyleSheet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */