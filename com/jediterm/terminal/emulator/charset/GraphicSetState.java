/*     */ package com.jediterm.terminal.emulator.charset;
/*     */ 
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GraphicSetState
/*     */ {
/*  18 */   private final GraphicSet[] myGraphicSets = new GraphicSet[4]; public GraphicSetState() {
/*  19 */     for (int i = 0; i < this.myGraphicSets.length; i++) {
/*  20 */       this.myGraphicSets[i] = new GraphicSet(i);
/*     */     }
/*     */     
/*  23 */     resetState();
/*     */   }
/*     */ 
/*     */   
/*     */   private GraphicSet myGL;
/*     */   
/*     */   private GraphicSet myGR;
/*     */   private GraphicSet myGlOverride;
/*     */   
/*     */   public void designateGraphicSet(@NotNull GraphicSet graphicSet, char designator) {
/*  33 */     if (graphicSet == null) $$$reportNull$$$0(0);  graphicSet.setDesignation(CharacterSet.valueOf(designator));
/*     */   }
/*     */ 
/*     */   
/*     */   public void designateGraphicSet(int num, CharacterSet characterSet) {
/*  38 */     getGraphicSet(num).setDesignation(characterSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public GraphicSet getGL() {
/*  46 */     GraphicSet result = this.myGL;
/*  47 */     if (this.myGlOverride != null) {
/*  48 */       result = this.myGlOverride;
/*  49 */       this.myGlOverride = null;
/*     */     } 
/*  51 */     if (result == null) $$$reportNull$$$0(1);  return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public GraphicSet getGR() {
/*  59 */     if (this.myGR == null) $$$reportNull$$$0(2);  return this.myGR;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public GraphicSet getGraphicSet(int index) {
/*  69 */     if (this.myGraphicSets[index % 4] == null) $$$reportNull$$$0(3);  return this.myGraphicSets[index % 4];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char map(char ch) {
/*  79 */     return CharacterSets.getChar(ch, getGL(), getGR());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void overrideGL(int index) {
/*  88 */     this.myGlOverride = getGraphicSet(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetState() {
/*  95 */     for (int i = 0; i < this.myGraphicSets.length; i++) {
/*  96 */       this.myGraphicSets[i].setDesignation(CharacterSet.valueOf((i == 1) ? 48 : 66));
/*     */     }
/*  98 */     this.myGL = this.myGraphicSets[0];
/*  99 */     this.myGR = this.myGraphicSets[1];
/* 100 */     this.myGlOverride = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGL(int index) {
/* 109 */     this.myGL = getGraphicSet(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGR(int index) {
/* 118 */     this.myGR = getGraphicSet(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGLOverrideIndex() {
/* 123 */     return (this.myGlOverride != null) ? this.myGlOverride.getIndex() : -1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\emulator\charset\GraphicSetState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */