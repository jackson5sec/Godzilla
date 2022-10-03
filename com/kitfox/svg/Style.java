/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import com.kitfox.svg.xml.StyleSheet;
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
/*     */ public class Style
/*     */   extends SVGElement
/*     */ {
/*     */   public static final String TAG_NAME = "style";
/*     */   String type;
/*  53 */   StringBuffer text = new StringBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   StyleSheet styleSheet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  67 */     return "style";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddText(SVGLoaderHelper helper, String text) {
/*  76 */     this.text.append(text);
/*     */ 
/*     */     
/*  79 */     this.styleSheet = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  85 */     super.build();
/*     */     
/*  87 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  89 */     if (getPres(sty.setName("type")))
/*     */     {
/*  91 */       this.type = sty.getStringValue();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean updateTime(double curTime) throws SVGException {
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public StyleSheet getStyleSheet() {
/* 104 */     if (this.styleSheet == null && this.text.length() > 0)
/*     */     {
/* 106 */       this.styleSheet = StyleSheet.parseSheet(this.text.toString());
/*     */     }
/* 108 */     return this.styleSheet;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Style.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */