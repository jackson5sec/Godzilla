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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Defs
/*    */   extends TransformableElement
/*    */ {
/*    */   public static final String TAG_NAME = "defs";
/*    */   
/*    */   public String getTagName() {
/* 57 */     return "defs";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
/* 67 */     super.loaderAddChild(helper, child);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean updateTime(double curTime) throws SVGException {
/* 75 */     boolean stateChange = false;
/* 76 */     for (SVGElement ele : this.children) {
/* 77 */       stateChange = (stateChange || ele.updateTime(curTime));
/*    */     }
/*    */     
/* 80 */     return (super.updateTime(curTime) || stateChange);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Defs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */