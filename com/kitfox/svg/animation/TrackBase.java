/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGElementException;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.util.ArrayList;
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
/*     */ public abstract class TrackBase
/*     */ {
/*     */   protected final String attribName;
/*     */   protected final int attribType;
/*     */   protected final SVGElement parent;
/*  62 */   final ArrayList<AnimationElement> animEvents = new ArrayList<AnimationElement>();
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
/*     */   public TrackBase(SVGElement parent, AnimationElement ele) throws SVGElementException {
/*  76 */     this(parent, ele.getAttribName(), ele.getAttribType());
/*     */   }
/*     */ 
/*     */   
/*     */   public TrackBase(SVGElement parent, String attribName, int attribType) throws SVGElementException {
/*  81 */     this.parent = parent;
/*  82 */     this.attribName = attribName;
/*  83 */     this.attribType = attribType;
/*     */ 
/*     */     
/*  86 */     if (attribType == 2 && 
/*  87 */       !parent.hasAttribute(attribName, 0) && 
/*  88 */       !parent.hasAttribute(attribName, 1)) {
/*     */       
/*  90 */       parent.addAttribute(attribName, 0, "");
/*     */     }
/*  92 */     else if (!parent.hasAttribute(attribName, attribType)) {
/*     */       
/*  94 */       parent.addAttribute(attribName, attribType, "");
/*     */     } 
/*     */   }
/*     */   
/*  98 */   public String getAttribName() { return this.attribName; } public int getAttribType() {
/*  99 */     return this.attribType;
/*     */   }
/*     */   
/*     */   public void addElement(AnimationElement ele) {
/* 103 */     this.animEvents.add(ele);
/*     */   }
/*     */   
/*     */   public abstract boolean getValue(StyleAttribute paramStyleAttribute, double paramDouble) throws SVGException;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\TrackBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */