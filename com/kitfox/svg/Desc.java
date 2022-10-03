/*    */ package com.kitfox.svg;
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
/*    */ public class Desc
/*    */   extends SVGElement
/*    */ {
/*    */   public static final String TAG_NAME = "desc";
/* 48 */   StringBuffer text = new StringBuffer();
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
/*    */   public String getTagName() {
/* 60 */     return "desc";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void loaderAddText(SVGLoaderHelper helper, String text) {
/* 69 */     this.text.append(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getText() {
/* 74 */     return this.text.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean updateTime(double curTime) {
/* 80 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Desc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */