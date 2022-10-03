/*     */ package net.miginfocom.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnimSpec
/*     */   implements Serializable
/*     */ {
/*  45 */   public static final AnimSpec DEF = new AnimSpec(0, 0, 0.2F, 0.2F);
/*     */ 
/*     */   
/*     */   private final int prio;
/*     */ 
/*     */   
/*     */   private final int durMillis;
/*     */ 
/*     */   
/*     */   private final float easeIn;
/*     */   
/*     */   private final float easeOut;
/*     */ 
/*     */   
/*     */   public AnimSpec(int prio, int durMillis, float easeIn, float easeOut) {
/*  60 */     this.prio = prio;
/*  61 */     this.durMillis = durMillis;
/*  62 */     this.easeIn = LayoutUtil.clamp(easeIn, 0.0F, 1.0F);
/*  63 */     this.easeOut = LayoutUtil.clamp(easeOut, 0.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPriority() {
/*  72 */     return this.prio;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDurationMillis(int defMillis) {
/*  81 */     return (this.durMillis > 0) ? this.durMillis : defMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDurationMillis() {
/*  89 */     return this.durMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getEaseIn() {
/*  97 */     return this.easeIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getEaseOut() {
/* 105 */     return this.easeOut;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\AnimSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */