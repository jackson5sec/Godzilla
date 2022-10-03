/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGLoaderHelper;
/*     */ import com.kitfox.svg.animation.parser.AnimTimeParser;
/*     */ import com.kitfox.svg.xml.ColorTable;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Color;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
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
/*     */ 
/*     */ public class AnimateColor
/*     */   extends AnimateBase
/*     */   implements AnimateColorIface
/*     */ {
/*     */   public static final String TAG_NAME = "animateColor";
/*     */   private Color fromValue;
/*     */   private Color toValue;
/*     */   
/*     */   public String getTagName() {
/*  70 */     return "animateColor";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
/*  77 */     super.loaderStartElement(helper, attrs, parent);
/*     */     
/*  79 */     String strn = attrs.getValue("from");
/*  80 */     this.fromValue = ColorTable.parseColor(strn);
/*     */     
/*  82 */     strn = attrs.getValue("to");
/*  83 */     this.toValue = ColorTable.parseColor(strn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color evalColor(double interp) {
/*  93 */     int r1 = this.fromValue.getRed();
/*  94 */     int g1 = this.fromValue.getGreen();
/*  95 */     int b1 = this.fromValue.getBlue();
/*  96 */     int r2 = this.toValue.getRed();
/*  97 */     int g2 = this.toValue.getGreen();
/*  98 */     int b2 = this.toValue.getBlue();
/*  99 */     double invInterp = 1.0D - interp;
/*     */     
/* 101 */     return new Color((int)(r1 * invInterp + r2 * interp), (int)(g1 * invInterp + g2 * interp), (int)(b1 * invInterp + b2 * interp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rebuild(AnimTimeParser animTimeParser) throws SVGException {
/* 109 */     super.rebuild(animTimeParser);
/*     */     
/* 111 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 113 */     if (getPres(sty.setName("from"))) {
/*     */       
/* 115 */       String strn = sty.getStringValue();
/* 116 */       this.fromValue = ColorTable.parseColor(strn);
/*     */     } 
/*     */     
/* 119 */     if (getPres(sty.setName("to"))) {
/*     */       
/* 121 */       String strn = sty.getStringValue();
/* 122 */       this.toValue = ColorTable.parseColor(strn);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getFromValue() {
/* 131 */     return this.fromValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFromValue(Color fromValue) {
/* 139 */     this.fromValue = fromValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getToValue() {
/* 147 */     return this.toValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToValue(Color toValue) {
/* 155 */     this.toValue = toValue;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\AnimateColor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */