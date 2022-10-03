/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.net.URI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class A
/*     */   extends Group
/*     */ {
/*     */   public static final String TAG_NAME = "a";
/*     */   URI href;
/*     */   String title;
/*     */   
/*     */   public String getTagName() {
/*  61 */     return "a";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  67 */     super.build();
/*     */     
/*  69 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  71 */     if (getPres(sty.setName("xlink:href")))
/*     */     {
/*  73 */       this.href = sty.getURIValue(getXMLBase());
/*     */     }
/*     */     
/*  76 */     if (getPres(sty.setName("xlink:title")))
/*     */     {
/*  78 */       this.title = sty.getStringValue();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean updateTime(double curTime) throws SVGException {
/*  91 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */ 
/*     */     
/*  95 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  97 */     if (getPres(sty.setName("xlink:href")))
/*     */     {
/*  99 */       this.href = sty.getURIValue(getXMLBase());
/*     */     }
/*     */     
/* 102 */     if (getPres(sty.setName("xlink:title")))
/*     */     {
/* 104 */       this.title = sty.getStringValue();
/*     */     }
/*     */     
/* 107 */     return changeState;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\A.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */