/*    */ package com.kitfox.svg;
/*    */ 
/*    */ import com.kitfox.svg.xml.StyleAttribute;
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
/*    */ public class Hkern
/*    */   extends SVGElement
/*    */ {
/*    */   public static final String TAG_NAME = "hkern";
/*    */   String u1;
/*    */   String u2;
/*    */   int k;
/*    */   
/*    */   public String getTagName() {
/* 55 */     return "hkern";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void build() throws SVGException {
/* 61 */     super.build();
/*    */     
/* 63 */     StyleAttribute sty = new StyleAttribute();
/*    */ 
/*    */ 
/*    */     
/* 67 */     if (getPres(sty.setName("u1")))
/*    */     {
/* 69 */       this.u1 = sty.getStringValue();
/*    */     }
/*    */     
/* 72 */     if (getPres(sty.setName("u2")))
/*    */     {
/* 74 */       this.u2 = sty.getStringValue();
/*    */     }
/*    */     
/* 77 */     if (getPres(sty.setName("k")))
/*    */     {
/* 79 */       this.k = sty.getIntValue();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean updateTime(double curTime) throws SVGException {
/* 87 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Hkern.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */