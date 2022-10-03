/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.VolatileImage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VolatileImageBackgroundPainterStrategy
/*     */   extends ImageBackgroundPainterStrategy
/*     */ {
/*     */   private VolatileImage bgImage;
/*     */   
/*     */   public VolatileImageBackgroundPainterStrategy(RTextAreaBase ta) {
/*  49 */     super(ta);
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintImage(Graphics g, int x, int y) {
/*  64 */     if (this.bgImage != null) {
/*     */       do {
/*  66 */         int rc = this.bgImage.validate(null);
/*  67 */         if (rc == 1)
/*     */         {
/*     */ 
/*     */           
/*  71 */           renderImage(this.bgImage.getWidth(), this.bgImage.getHeight(), 
/*  72 */               getScalingHint());
/*     */         }
/*  74 */         g.drawImage(this.bgImage, x, y, null);
/*  75 */       } while (this.bgImage.contentsLost());
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
/*     */ 
/*     */   
/*     */   private void renderImage(int width, int height, int hint) {
/*  90 */     Image master = getMasterImage();
/*  91 */     if (master != null) {
/*     */       do {
/*  93 */         Image i = master.getScaledInstance(width, height, hint);
/*  94 */         this.tracker.addImage(i, 1);
/*     */         try {
/*  96 */           this.tracker.waitForID(1);
/*  97 */         } catch (InterruptedException e) {
/*  98 */           e.printStackTrace();
/*  99 */           this.bgImage = null;
/*     */           return;
/*     */         } finally {
/* 102 */           this.tracker.removeImage(i, 1);
/*     */         } 
/* 104 */         this.bgImage.getGraphics().drawImage(i, 0, 0, null);
/* 105 */         this.tracker.addImage(this.bgImage, 0);
/*     */         try {
/* 107 */           this.tracker.waitForID(0);
/* 108 */         } catch (InterruptedException e) {
/* 109 */           e.printStackTrace();
/* 110 */           this.bgImage = null;
/*     */           return;
/*     */         } finally {
/* 113 */           this.tracker.removeImage(this.bgImage, 0);
/*     */         } 
/* 115 */       } while (this.bgImage.contentsLost());
/*     */     } else {
/*     */       
/* 118 */       this.bgImage = null;
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
/*     */   
/*     */   protected void rescaleImage(int width, int height, int hint) {
/* 132 */     this.bgImage = getRTextAreaBase().createVolatileImage(width, height);
/* 133 */     if (this.bgImage != null)
/* 134 */       renderImage(width, height, hint); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\VolatileImageBackgroundPainterStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */