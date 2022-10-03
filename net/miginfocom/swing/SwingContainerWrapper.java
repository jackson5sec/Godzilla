/*     */ package net.miginfocom.swing;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics2D;
/*     */ import net.miginfocom.layout.ComponentWrapper;
/*     */ import net.miginfocom.layout.ContainerWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SwingContainerWrapper
/*     */   extends SwingComponentWrapper
/*     */   implements ContainerWrapper
/*     */ {
/*  51 */   private static final Color DB_CELL_OUTLINE = new Color(255, 0, 0);
/*     */ 
/*     */   
/*     */   public SwingContainerWrapper(Container c) {
/*  55 */     super(c);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ComponentWrapper[] getComponents() {
/*  61 */     Container c = (Container)getComponent();
/*  62 */     ComponentWrapper[] cws = new ComponentWrapper[c.getComponentCount()];
/*  63 */     for (int i = 0; i < cws.length; i++)
/*  64 */       cws[i] = new SwingComponentWrapper(c.getComponent(i)); 
/*  65 */     return cws;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getComponentCount() {
/*  71 */     return ((Container)getComponent()).getComponentCount();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getLayout() {
/*  77 */     return ((Container)getComponent()).getLayout();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isLeftToRight() {
/*  83 */     return ((Container)getComponent()).getComponentOrientation().isLeftToRight();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void paintDebugCell(int x, int y, int width, int height) {
/*  89 */     Component c = (Component)getComponent();
/*  90 */     if (!c.isShowing()) {
/*     */       return;
/*     */     }
/*  93 */     Graphics2D g = (Graphics2D)c.getGraphics();
/*  94 */     if (g == null) {
/*     */       return;
/*     */     }
/*  97 */     g.setStroke(new BasicStroke(1.0F, 2, 0, 10.0F, new float[] { 2.0F, 3.0F }, 0.0F));
/*  98 */     g.setPaint(DB_CELL_OUTLINE);
/*  99 */     g.drawRect(x, y, width - 1, height - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getComponentType(boolean disregardScrollPane) {
/* 105 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLayoutHashCode() {
/* 112 */     long n = System.nanoTime();
/* 113 */     int h = super.getLayoutHashCode();
/*     */     
/* 115 */     if (isLeftToRight()) {
/* 116 */       h += 416343;
/*     */     }
/* 118 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\swing\SwingContainerWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */