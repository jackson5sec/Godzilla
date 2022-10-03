/*     */ package org.apache.log4j.varia;
/*     */ 
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.spi.Filter;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LevelRangeFilter
/*     */   extends Filter
/*     */ {
/*     */   boolean acceptOnMatch = false;
/*     */   Level levelMin;
/*     */   Level levelMax;
/*     */   
/*     */   public int decide(LoggingEvent event) {
/*  69 */     if (this.levelMin != null && 
/*  70 */       !event.getLevel().isGreaterOrEqual((Priority)this.levelMin))
/*     */     {
/*  72 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*  76 */     if (this.levelMax != null && 
/*  77 */       event.getLevel().toInt() > this.levelMax.toInt())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*  82 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*  86 */     if (this.acceptOnMatch)
/*     */     {
/*     */       
/*  89 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*  93 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevelMax() {
/* 101 */     return this.levelMax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevelMin() {
/* 109 */     return this.levelMin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAcceptOnMatch() {
/* 117 */     return this.acceptOnMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevelMax(Level levelMax) {
/* 125 */     this.levelMax = levelMax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevelMin(Level levelMin) {
/* 133 */     this.levelMin = levelMin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAcceptOnMatch(boolean acceptOnMatch) {
/* 141 */     this.acceptOnMatch = acceptOnMatch;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\varia\LevelRangeFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */