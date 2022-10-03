/*     */ package org.fife.rsta.ui;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CollapsibleSectionPanel
/*     */   extends JPanel
/*     */ {
/*     */   private List<BottomComponentInfo> bottomComponentInfos;
/*     */   private BottomComponentInfo currentBci;
/*     */   private boolean animate;
/*     */   private Timer timer;
/*     */   private int tick;
/*  47 */   private int totalTicks = 10;
/*     */ 
/*     */   
/*     */   private boolean down;
/*     */   
/*     */   private boolean firstTick;
/*     */   
/*     */   private static final int FRAME_MILLIS = 10;
/*     */ 
/*     */   
/*     */   public CollapsibleSectionPanel() {
/*  58 */     this(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CollapsibleSectionPanel(boolean animate) {
/*  68 */     super(new BorderLayout());
/*  69 */     this.bottomComponentInfos = new ArrayList<>();
/*  70 */     installKeystrokes();
/*  71 */     this.animate = animate;
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
/*     */   public void addBottomComponent(JComponent comp) {
/*  84 */     addBottomComponent((KeyStroke)null, comp);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Action addBottomComponent(KeyStroke ks, JComponent comp) {
/* 106 */     BottomComponentInfo bci = new BottomComponentInfo(comp);
/* 107 */     this.bottomComponentInfos.add(bci);
/*     */     
/* 109 */     Action action = null;
/* 110 */     if (ks != null) {
/* 111 */       InputMap im = getInputMap(1);
/*     */       
/* 113 */       im.put(ks, ks);
/* 114 */       action = new ShowBottomComponentAction(ks, bci);
/* 115 */       getActionMap().put(ks, action);
/*     */     } 
/* 117 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createTimer() {
/* 123 */     this.timer = new Timer(10, e -> {
/*     */           this.tick++;
/*     */           
/*     */           if (this.tick == this.totalTicks) {
/*     */             this.timer.stop();
/*     */             
/*     */             this.timer = null;
/*     */             
/*     */             this.tick = 0;
/*     */             
/*     */             Dimension finalSize = this.down ? new Dimension(0, 0) : this.currentBci.getRealPreferredSize();
/*     */             
/*     */             this.currentBci.component.setPreferredSize(finalSize);
/*     */             
/*     */             if (this.down) {
/*     */               remove(this.currentBci.component);
/*     */               this.currentBci = null;
/*     */             } 
/*     */           } else {
/*     */             if (this.firstTick) {
/*     */               if (this.down) {
/*     */                 focusMainComponent();
/*     */               } else {
/*     */                 this.currentBci.component.requestFocusInWindow();
/*     */               } 
/*     */               this.firstTick = false;
/*     */             } 
/*     */             float proportion = !this.down ? (this.tick / this.totalTicks) : (1.0F - this.tick / this.totalTicks);
/*     */             Dimension size = new Dimension(this.currentBci.getRealPreferredSize());
/*     */             size.height = (int)(size.height * proportion);
/*     */             this.currentBci.component.setPreferredSize(size);
/*     */           } 
/*     */           revalidate();
/*     */           repaint();
/*     */         });
/* 158 */     this.timer.setRepeats(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void focusMainComponent() {
/* 167 */     Component center = ((BorderLayout)getLayout()).getLayoutComponent("Center");
/* 168 */     if (center instanceof JScrollPane) {
/* 169 */       center = ((JScrollPane)center).getViewport().getView();
/*     */     }
/* 171 */     center.requestFocusInWindow();
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
/*     */   public JComponent getDisplayedBottomComponent() {
/* 184 */     if (this.currentBci != null && (this.timer == null || !this.timer.isRunning())) {
/* 185 */       return this.currentBci.component;
/*     */     }
/* 187 */     return null;
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
/*     */   public void hideBottomComponent() {
/* 199 */     if (this.currentBci == null) {
/*     */       return;
/*     */     }
/* 202 */     if (!this.animate) {
/* 203 */       remove(this.currentBci.component);
/* 204 */       revalidate();
/* 205 */       repaint();
/* 206 */       this.currentBci = null;
/* 207 */       focusMainComponent();
/*     */       
/*     */       return;
/*     */     } 
/* 211 */     if (this.timer != null) {
/* 212 */       if (this.down) {
/*     */         return;
/*     */       }
/* 215 */       this.timer.stop();
/* 216 */       this.tick = this.totalTicks - this.tick;
/*     */     } 
/* 218 */     this.down = true;
/* 219 */     this.firstTick = true;
/*     */     
/* 221 */     createTimer();
/* 222 */     this.timer.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void installKeystrokes() {
/* 232 */     InputMap im = getInputMap(1);
/* 233 */     ActionMap am = getActionMap();
/*     */     
/* 235 */     im.put(KeyStroke.getKeyStroke(27, 0), "onEscape");
/* 236 */     am.put("onEscape", new HideBottomComponentAction());
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
/*     */   public void setAnimationTime(int millis) {
/* 248 */     if (millis < 0) {
/* 249 */       throw new IllegalArgumentException("millis must be >= 0");
/*     */     }
/* 251 */     this.totalTicks = Math.max(millis / 10, 1);
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
/*     */   private void showBottomComponent(BottomComponentInfo bci) {
/* 264 */     if (bci.equals(this.currentBci)) {
/* 265 */       this.currentBci.component.requestFocusInWindow();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 270 */     if (this.currentBci != null) {
/* 271 */       remove(this.currentBci.component);
/*     */     }
/* 273 */     this.currentBci = bci;
/* 274 */     add(this.currentBci.component, "South");
/* 275 */     if (!this.animate) {
/* 276 */       this.currentBci.component.requestFocusInWindow();
/* 277 */       revalidate();
/* 278 */       repaint();
/*     */       
/*     */       return;
/*     */     } 
/* 282 */     if (this.timer != null) {
/* 283 */       this.timer.stop();
/*     */     }
/* 285 */     this.tick = 0;
/* 286 */     this.down = false;
/* 287 */     this.firstTick = true;
/*     */ 
/*     */     
/* 290 */     createTimer();
/* 291 */     this.timer.start();
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
/*     */   public void showBottomComponent(JComponent comp) {
/* 306 */     BottomComponentInfo info = null;
/* 307 */     for (BottomComponentInfo bci : this.bottomComponentInfos) {
/* 308 */       if (bci.component == comp) {
/* 309 */         info = bci;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 314 */     if (info != null) {
/* 315 */       showBottomComponent(info);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 323 */     super.updateUI();
/* 324 */     if (this.bottomComponentInfos != null) {
/* 325 */       for (BottomComponentInfo info : this.bottomComponentInfos) {
/* 326 */         if (!info.component.isDisplayable()) {
/* 327 */           SwingUtilities.updateComponentTreeUI(info.component);
/*     */         }
/* 329 */         info.uiUpdated();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BottomComponentInfo
/*     */   {
/*     */     private JComponent component;
/*     */     
/*     */     private Dimension preferredSize;
/*     */ 
/*     */     
/*     */     BottomComponentInfo(JComponent component) {
/* 344 */       this.component = component;
/*     */     }
/*     */     
/*     */     Dimension getRealPreferredSize() {
/* 348 */       if (this.preferredSize == null) {
/* 349 */         this.preferredSize = this.component.getPreferredSize();
/*     */       }
/* 351 */       return this.preferredSize;
/*     */     }
/*     */ 
/*     */     
/*     */     private void uiUpdated() {
/* 356 */       this.component.setPreferredSize((Dimension)null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class HideBottomComponentAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private HideBottomComponentAction() {}
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 369 */       CollapsibleSectionPanel.this.hideBottomComponent();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ShowBottomComponentAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private CollapsibleSectionPanel.BottomComponentInfo bci;
/*     */ 
/*     */ 
/*     */     
/*     */     ShowBottomComponentAction(KeyStroke ks, CollapsibleSectionPanel.BottomComponentInfo bci) {
/* 383 */       putValue("AcceleratorKey", ks);
/* 384 */       this.bci = bci;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 389 */       CollapsibleSectionPanel.this.showBottomComponent(this.bci);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\CollapsibleSectionPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */