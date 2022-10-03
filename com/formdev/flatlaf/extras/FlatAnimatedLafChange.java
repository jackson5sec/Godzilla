/*     */ package com.formdev.flatlaf.extras;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatSystemProperties;
/*     */ import com.formdev.flatlaf.util.Animator;
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Window;
/*     */ import java.awt.image.VolatileImage;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.RootPaneContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatAnimatedLafChange
/*     */ {
/*  51 */   public static int duration = 160;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static int resolution = 40;
/*     */   
/*     */   private static Animator animator;
/*  59 */   private static final Map<JLayeredPane, JComponent> oldUIsnapshots = new WeakHashMap<>();
/*  60 */   private static final Map<JLayeredPane, JComponent> newUIsnapshots = new WeakHashMap<>();
/*     */ 
/*     */   
/*     */   private static float alpha;
/*     */   
/*     */   private static boolean inShowSnapshot;
/*     */ 
/*     */   
/*     */   public static void showSnapshot() {
/*  69 */     if (!FlatSystemProperties.getBoolean("flatlaf.animatedLafChange", true)) {
/*     */       return;
/*     */     }
/*     */     
/*  73 */     if (animator != null) {
/*  74 */       animator.stop();
/*     */     }
/*  76 */     alpha = 1.0F;
/*     */ 
/*     */     
/*  79 */     showSnapshot(true, oldUIsnapshots);
/*     */   }
/*     */   
/*     */   private static void showSnapshot(final boolean useAlpha, Map<JLayeredPane, JComponent> map) {
/*  83 */     inShowSnapshot = true;
/*     */ 
/*     */     
/*  86 */     Window[] windows = Window.getWindows();
/*  87 */     for (Window window : windows) {
/*  88 */       if (window instanceof RootPaneContainer && window.isShowing()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  93 */         final VolatileImage snapshot = window.createVolatileImage(window.getWidth(), window.getHeight());
/*  94 */         if (snapshot != null) {
/*     */ 
/*     */ 
/*     */           
/*  98 */           JLayeredPane layeredPane = ((RootPaneContainer)window).getLayeredPane();
/*  99 */           layeredPane.paint(snapshot.getGraphics());
/*     */ 
/*     */ 
/*     */           
/* 103 */           JComponent snapshotLayer = new JComponent()
/*     */             {
/*     */               public void paint(Graphics g) {
/* 106 */                 if (FlatAnimatedLafChange.inShowSnapshot || snapshot.contentsLost()) {
/*     */                   return;
/*     */                 }
/* 109 */                 if (useAlpha)
/* 110 */                   ((Graphics2D)g).setComposite(AlphaComposite.getInstance(3, FlatAnimatedLafChange.alpha)); 
/* 111 */                 g.drawImage(snapshot, 0, 0, null);
/*     */               }
/*     */ 
/*     */               
/*     */               public void removeNotify() {
/* 116 */                 super.removeNotify();
/*     */ 
/*     */                 
/* 119 */                 snapshot.flush();
/*     */               }
/*     */             };
/* 122 */           if (!useAlpha)
/* 123 */             snapshotLayer.setOpaque(true); 
/* 124 */           snapshotLayer.setSize(layeredPane.getSize());
/*     */ 
/*     */           
/* 127 */           layeredPane.add(snapshotLayer, Integer.valueOf(JLayeredPane.DRAG_LAYER.intValue() + (useAlpha ? 2 : 1)));
/* 128 */           map.put(layeredPane, snapshotLayer);
/*     */         } 
/*     */       } 
/* 131 */     }  inShowSnapshot = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void hideSnapshotWithAnimation() {
/* 140 */     if (!FlatSystemProperties.getBoolean("flatlaf.animatedLafChange", true)) {
/*     */       return;
/*     */     }
/* 143 */     if (oldUIsnapshots.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 147 */     showSnapshot(false, newUIsnapshots);
/*     */ 
/*     */     
/* 150 */     animator = new Animator(duration, fraction -> {
/*     */           if (fraction < 0.1D || fraction > 0.9D) {
/*     */             return;
/*     */           }
/*     */           
/*     */           alpha = 1.0F - fraction;
/*     */           
/*     */           for (Map.Entry<JLayeredPane, JComponent> e : oldUIsnapshots.entrySet()) {
/*     */             if (((JLayeredPane)e.getKey()).isShowing()) {
/*     */               ((JComponent)e.getValue()).repaint();
/*     */             }
/*     */           } 
/*     */         }() -> {
/*     */           hideSnapshot();
/*     */           animator = null;
/*     */         });
/* 166 */     animator.setResolution(resolution);
/* 167 */     animator.start();
/*     */   }
/*     */   
/*     */   private static void hideSnapshot() {
/* 171 */     hideSnapshot(oldUIsnapshots);
/* 172 */     hideSnapshot(newUIsnapshots);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void hideSnapshot(Map<JLayeredPane, JComponent> map) {
/* 177 */     for (Map.Entry<JLayeredPane, JComponent> e : map.entrySet()) {
/* 178 */       ((JLayeredPane)e.getKey()).remove(e.getValue());
/* 179 */       ((JLayeredPane)e.getKey()).repaint();
/*     */     } 
/*     */     
/* 182 */     map.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void stop() {
/* 189 */     if (animator != null) {
/* 190 */       animator.stop();
/*     */     } else {
/* 192 */       hideSnapshot();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\FlatAnimatedLafChange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */