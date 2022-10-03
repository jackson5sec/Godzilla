/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGLoaderHelper;
/*     */ import com.kitfox.svg.animation.parser.AnimTimeParser;
/*     */ import com.kitfox.svg.animation.parser.ParseException;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.io.StringReader;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public abstract class AnimateBase
/*     */   extends AnimationElement
/*     */ {
/*  58 */   private double repeatCount = Double.NaN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TimeBase repeatDur;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void evalParametric(AnimationTimeEval state, double curTime) {
/*  69 */     evalParametric(state, curTime, this.repeatCount, (this.repeatDur == null) ? Double.NaN : this.repeatDur.evalTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
/*  76 */     super.loaderStartElement(helper, attrs, parent);
/*     */     
/*  78 */     String repeatDurTime = attrs.getValue("repeatDur");
/*     */ 
/*     */     
/*     */     try {
/*  82 */       if (repeatDurTime != null)
/*     */       {
/*  84 */         helper.animTimeParser.ReInit(new StringReader(repeatDurTime));
/*  85 */         this.repeatDur = helper.animTimeParser.Expr();
/*  86 */         this.repeatDur.setParentElement(this);
/*     */       }
/*     */     
/*  89 */     } catch (Exception e) {
/*     */       
/*  91 */       throw new SAXException(e);
/*     */     } 
/*     */     
/*  94 */     String strn = attrs.getValue("repeatCount");
/*  95 */     if (strn == null) {
/*     */       
/*  97 */       this.repeatCount = 1.0D;
/*     */     }
/*  99 */     else if ("indefinite".equals(strn)) {
/*     */       
/* 101 */       this.repeatCount = Double.POSITIVE_INFINITY;
/*     */     } else {
/*     */ 
/*     */       
/* 105 */       try { this.repeatCount = Double.parseDouble(strn); }
/* 106 */       catch (Exception e) { this.repeatCount = Double.NaN; }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void rebuild(AnimTimeParser animTimeParser) throws SVGException {
/* 113 */     super.rebuild(animTimeParser);
/*     */     
/* 115 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 117 */     if (getPres(sty.setName("repeatDur"))) {
/*     */       
/* 119 */       String strn = sty.getStringValue();
/* 120 */       if (strn != null) {
/*     */         
/* 122 */         animTimeParser.ReInit(new StringReader(strn));
/*     */         
/*     */         try {
/* 125 */           this.repeatDur = animTimeParser.Expr();
/*     */         }
/* 127 */         catch (ParseException ex) {
/*     */           
/* 129 */           Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse '" + strn + "'", (Throwable)ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 135 */     if (getPres(sty.setName("repeatCount"))) {
/*     */       
/* 137 */       String strn = sty.getStringValue();
/* 138 */       if (strn == null) {
/*     */         
/* 140 */         this.repeatCount = 1.0D;
/*     */       }
/* 142 */       else if ("indefinite".equals(strn)) {
/*     */         
/* 144 */         this.repeatCount = Double.POSITIVE_INFINITY;
/*     */       } else {
/*     */ 
/*     */         
/* 148 */         try { this.repeatCount = Double.parseDouble(strn); }
/* 149 */         catch (Exception e) { this.repeatCount = Double.NaN; }
/*     */       
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRepeatCount() {
/* 159 */     return this.repeatCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRepeatCount(double repeatCount) {
/* 167 */     this.repeatCount = repeatCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeBase getRepeatDur() {
/* 175 */     return this.repeatDur;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRepeatDur(TimeBase repeatDur) {
/* 183 */     this.repeatDur = repeatDur;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\AnimateBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */