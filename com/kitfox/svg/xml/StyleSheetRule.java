/*    */ package com.kitfox.svg.xml;
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
/*    */ public class StyleSheetRule
/*    */ {
/*    */   final String styleName;
/*    */   final String tag;
/*    */   final String className;
/*    */   
/*    */   public StyleSheetRule(String styleName, String tag, String className) {
/* 19 */     this.styleName = styleName;
/* 20 */     this.tag = tag;
/* 21 */     this.className = className;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 27 */     int hash = 7;
/* 28 */     hash = 13 * hash + ((this.styleName != null) ? this.styleName.hashCode() : 0);
/* 29 */     hash = 13 * hash + ((this.tag != null) ? this.tag.hashCode() : 0);
/* 30 */     hash = 13 * hash + ((this.className != null) ? this.className.hashCode() : 0);
/* 31 */     return hash;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 37 */     if (obj == null)
/*    */     {
/* 39 */       return false;
/*    */     }
/* 41 */     if (getClass() != obj.getClass())
/*    */     {
/* 43 */       return false;
/*    */     }
/* 45 */     StyleSheetRule other = (StyleSheetRule)obj;
/* 46 */     if ((this.styleName == null) ? (other.styleName != null) : !this.styleName.equals(other.styleName))
/*    */     {
/* 48 */       return false;
/*    */     }
/* 50 */     if ((this.tag == null) ? (other.tag != null) : !this.tag.equals(other.tag))
/*    */     {
/* 52 */       return false;
/*    */     }
/* 54 */     if ((this.className == null) ? (other.className != null) : !this.className.equals(other.className))
/*    */     {
/* 56 */       return false;
/*    */     }
/* 58 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\xml\StyleSheetRule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */