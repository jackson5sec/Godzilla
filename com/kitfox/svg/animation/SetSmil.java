/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGLoaderHelper;
/*     */ import com.kitfox.svg.animation.parser.AnimTimeParser;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
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
/*     */ 
/*     */ 
/*     */ public class SetSmil
/*     */   extends AnimationElement
/*     */ {
/*     */   public static final String TAG_NAME = "set";
/*     */   private String toValue;
/*     */   
/*     */   public String getTagName() {
/*  68 */     return "set";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
/*  75 */     super.loaderStartElement(helper, attrs, parent);
/*     */     
/*  77 */     this.toValue = attrs.getValue("to");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rebuild(AnimTimeParser animTimeParser) throws SVGException {
/*  83 */     super.rebuild(animTimeParser);
/*     */     
/*  85 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  87 */     if (getPres(sty.setName("to"))) {
/*     */       
/*  89 */       String newVal = sty.getStringValue();
/*  90 */       this.toValue = newVal;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToValue() {
/*  99 */     return this.toValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToValue(String toValue) {
/* 107 */     this.toValue = toValue;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\SetSmil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */