/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGElementException;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.geom.AffineTransform;
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
/*     */ public class TrackTransform
/*     */   extends TrackBase
/*     */ {
/*     */   public TrackTransform(AnimationElement ele) throws SVGElementException {
/*  57 */     super(ele.getParent(), ele);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getValue(StyleAttribute attrib, double curTime) throws SVGException {
/*  63 */     AffineTransform retVal = new AffineTransform();
/*  64 */     retVal = getValue(retVal, curTime);
/*     */ 
/*     */ 
/*     */     
/*  68 */     double[] mat = new double[6];
/*  69 */     retVal.getMatrix(mat);
/*  70 */     attrib.setStringValue("matrix(" + mat[0] + " " + mat[1] + " " + mat[2] + " " + mat[3] + " " + mat[4] + " " + mat[5] + ")");
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AffineTransform getValue(AffineTransform retVal, double curTime) throws SVGException {
/*  77 */     StyleAttribute attr = null;
/*  78 */     switch (this.attribType) {
/*     */       
/*     */       case 0:
/*  81 */         attr = this.parent.getStyleAbsolute(this.attribName);
/*  82 */         retVal.setTransform(SVGElement.parseSingleTransform(attr.getStringValue()));
/*     */         break;
/*     */       case 1:
/*  85 */         attr = this.parent.getPresAbsolute(this.attribName);
/*  86 */         retVal.setTransform(SVGElement.parseSingleTransform(attr.getStringValue()));
/*     */         break;
/*     */       case 2:
/*  89 */         attr = this.parent.getStyleAbsolute(this.attribName);
/*  90 */         if (attr == null) attr = this.parent.getPresAbsolute(this.attribName); 
/*  91 */         retVal.setTransform(SVGElement.parseSingleTransform(attr.getStringValue()));
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  97 */     AnimationTimeEval state = new AnimationTimeEval();
/*  98 */     AffineTransform xform = new AffineTransform();
/*     */     
/* 100 */     for (AnimationElement animationElement : this.animEvents) {
/* 101 */       AnimateXform ele = (AnimateXform)animationElement;
/* 102 */       ele.evalParametric(state, curTime);
/*     */ 
/*     */       
/* 105 */       if (Double.isNaN(state.interp))
/*     */         continue; 
/* 107 */       switch (ele.getAdditiveType()) {
/*     */         
/*     */         case 1:
/* 110 */           retVal.concatenate(ele.eval(xform, state.interp));
/*     */         
/*     */         case 0:
/* 113 */           retVal.setTransform(ele.eval(xform, state.interp));
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 119 */     return retVal;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\TrackTransform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */