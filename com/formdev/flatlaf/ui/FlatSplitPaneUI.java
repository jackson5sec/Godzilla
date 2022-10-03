/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.ToolTipManager;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneDivider;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatSplitPaneUI
/*     */   extends BasicSplitPaneUI
/*     */ {
/*     */   protected String arrowType;
/*     */   private Boolean continuousLayout;
/*     */   protected Color oneTouchArrowColor;
/*     */   protected Color oneTouchHoverArrowColor;
/*     */   protected Color oneTouchPressedArrowColor;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  74 */     return new FlatSplitPaneUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  79 */     this.arrowType = UIManager.getString("Component.arrowType");
/*     */ 
/*     */ 
/*     */     
/*  83 */     this.oneTouchArrowColor = UIManager.getColor("SplitPaneDivider.oneTouchArrowColor");
/*  84 */     this.oneTouchHoverArrowColor = UIManager.getColor("SplitPaneDivider.oneTouchHoverArrowColor");
/*  85 */     this.oneTouchPressedArrowColor = UIManager.getColor("SplitPaneDivider.oneTouchPressedArrowColor");
/*     */     
/*  87 */     super.installDefaults();
/*     */     
/*  89 */     this.continuousLayout = (Boolean)UIManager.get("SplitPane.continuousLayout");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/*  94 */     super.uninstallDefaults();
/*     */     
/*  96 */     this.oneTouchArrowColor = null;
/*  97 */     this.oneTouchHoverArrowColor = null;
/*  98 */     this.oneTouchPressedArrowColor = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isContinuousLayout() {
/* 103 */     return (super.isContinuousLayout() || (this.continuousLayout != null && Boolean.TRUE.equals(this.continuousLayout)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicSplitPaneDivider createDefaultDivider() {
/* 108 */     return new FlatSplitPaneDivider(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class FlatSplitPaneDivider
/*     */     extends BasicSplitPaneDivider
/*     */   {
/* 116 */     protected final String style = UIManager.getString("SplitPaneDivider.style");
/* 117 */     protected final Color gripColor = UIManager.getColor("SplitPaneDivider.gripColor");
/* 118 */     protected final int gripDotCount = FlatUIUtils.getUIInt("SplitPaneDivider.gripDotCount", 3);
/* 119 */     protected final int gripDotSize = FlatUIUtils.getUIInt("SplitPaneDivider.gripDotSize", 3);
/* 120 */     protected final int gripGap = FlatUIUtils.getUIInt("SplitPaneDivider.gripGap", 2);
/*     */     
/*     */     protected FlatSplitPaneDivider(BasicSplitPaneUI ui) {
/* 123 */       super(ui);
/*     */       
/* 125 */       setLayout(new FlatDividerLayout());
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDividerSize(int newSize) {
/* 130 */       super.setDividerSize(UIScale.scale(newSize));
/*     */     }
/*     */ 
/*     */     
/*     */     protected JButton createLeftOneTouchButton() {
/* 135 */       return new FlatOneTouchButton(true);
/*     */     }
/*     */ 
/*     */     
/*     */     protected JButton createRightOneTouchButton() {
/* 140 */       return new FlatOneTouchButton(false);
/*     */     }
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 145 */       super.propertyChange(e);
/*     */       
/* 147 */       switch (e.getPropertyName()) {
/*     */         
/*     */         case "dividerLocation":
/* 150 */           revalidate();
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void paint(Graphics g) {
/* 157 */       super.paint(g);
/*     */       
/* 159 */       if ("plain".equals(this.style)) {
/*     */         return;
/*     */       }
/* 162 */       Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
/*     */       
/* 164 */       g.setColor(this.gripColor);
/* 165 */       paintGrip(g, 0, 0, getWidth(), getHeight());
/*     */       
/* 167 */       FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
/*     */     }
/*     */     
/*     */     protected void paintGrip(Graphics g, int x, int y, int width, int height) {
/* 171 */       FlatUIUtils.paintGrip(g, x, y, width, height, 
/* 172 */           (this.splitPane.getOrientation() == 0), this.gripDotCount, this.gripDotSize, this.gripGap, true);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isLeftCollapsed() {
/* 177 */       int location = this.splitPane.getDividerLocation();
/* 178 */       Insets insets = this.splitPane.getInsets();
/* 179 */       return (this.orientation == 0) ? ((location == insets.top)) : ((location == insets.left));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isRightCollapsed() {
/* 185 */       int location = this.splitPane.getDividerLocation();
/* 186 */       Insets insets = this.splitPane.getInsets();
/* 187 */       return (this.orientation == 0) ? (
/* 188 */         (location == this.splitPane.getHeight() - getHeight() - insets.bottom)) : (
/* 189 */         (location == this.splitPane.getWidth() - getWidth() - insets.right));
/*     */     }
/*     */ 
/*     */     
/*     */     protected class FlatOneTouchButton
/*     */       extends FlatArrowButton
/*     */     {
/*     */       protected final boolean left;
/*     */ 
/*     */       
/*     */       protected FlatOneTouchButton(boolean left) {
/* 200 */         super(1, FlatSplitPaneUI.this.arrowType, FlatSplitPaneUI.this.oneTouchArrowColor, (Color)null, FlatSplitPaneUI.this.oneTouchHoverArrowColor, (Color)null, FlatSplitPaneUI.this.oneTouchPressedArrowColor, (Color)null);
/*     */         
/* 202 */         setCursor(Cursor.getPredefinedCursor(0));
/* 203 */         ToolTipManager.sharedInstance().registerComponent(this);
/*     */         
/* 205 */         this.left = left;
/*     */       }
/*     */ 
/*     */       
/*     */       public int getDirection() {
/* 210 */         return (FlatSplitPaneUI.FlatSplitPaneDivider.this.orientation == 0) ? (this.left ? 1 : 5) : (this.left ? 7 : 3);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public String getToolTipText(MouseEvent e) {
/* 229 */         String key = (FlatSplitPaneUI.FlatSplitPaneDivider.this.orientation == 0) ? (this.left ? (FlatSplitPaneUI.FlatSplitPaneDivider.this.isRightCollapsed() ? "SplitPaneDivider.expandBottomToolTipText" : "SplitPaneDivider.collapseTopToolTipText") : (FlatSplitPaneUI.FlatSplitPaneDivider.this.isLeftCollapsed() ? "SplitPaneDivider.expandTopToolTipText" : "SplitPaneDivider.collapseBottomToolTipText")) : (this.left ? (FlatSplitPaneUI.FlatSplitPaneDivider.this.isRightCollapsed() ? "SplitPaneDivider.expandRightToolTipText" : "SplitPaneDivider.collapseLeftToolTipText") : (FlatSplitPaneUI.FlatSplitPaneDivider.this.isLeftCollapsed() ? "SplitPaneDivider.expandLeftToolTipText" : "SplitPaneDivider.collapseRightToolTipText"));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 234 */         Object value = FlatSplitPaneUI.FlatSplitPaneDivider.this.splitPane.getClientProperty(key);
/* 235 */         if (value instanceof String) {
/* 236 */           return (String)value;
/*     */         }
/*     */         
/* 239 */         return UIManager.getString(key, getLocale());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected class FlatDividerLayout
/*     */       extends BasicSplitPaneDivider.DividerLayout
/*     */     {
/*     */       public void layoutContainer(Container c) {
/* 250 */         super.layoutContainer(c);
/*     */         
/* 252 */         if (FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton == null || FlatSplitPaneUI.FlatSplitPaneDivider.this.rightButton == null || !FlatSplitPaneUI.FlatSplitPaneDivider.this.splitPane.isOneTouchExpandable()) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 257 */         int extraSize = UIScale.scale(4);
/* 258 */         if (FlatSplitPaneUI.FlatSplitPaneDivider.this.orientation == 0) {
/* 259 */           FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.setSize(FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.getWidth() + extraSize, FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.getHeight());
/* 260 */           FlatSplitPaneUI.FlatSplitPaneDivider.this.rightButton.setBounds(FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.getX() + FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.getWidth(), FlatSplitPaneUI.FlatSplitPaneDivider.this.rightButton.getY(), FlatSplitPaneUI.FlatSplitPaneDivider.this
/* 261 */               .rightButton.getWidth() + extraSize, FlatSplitPaneUI.FlatSplitPaneDivider.this.rightButton.getHeight());
/*     */         } else {
/* 263 */           FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.setSize(FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.getWidth(), FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.getHeight() + extraSize);
/* 264 */           FlatSplitPaneUI.FlatSplitPaneDivider.this.rightButton.setBounds(FlatSplitPaneUI.FlatSplitPaneDivider.this.rightButton.getX(), FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.getY() + FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.getHeight(), FlatSplitPaneUI.FlatSplitPaneDivider.this
/* 265 */               .rightButton.getWidth(), FlatSplitPaneUI.FlatSplitPaneDivider.this.rightButton.getHeight() + extraSize);
/*     */         } 
/*     */ 
/*     */         
/* 269 */         boolean leftCollapsed = FlatSplitPaneUI.FlatSplitPaneDivider.this.isLeftCollapsed();
/* 270 */         if (leftCollapsed)
/* 271 */           FlatSplitPaneUI.FlatSplitPaneDivider.this.rightButton.setLocation(FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.getLocation()); 
/* 272 */         FlatSplitPaneUI.FlatSplitPaneDivider.this.leftButton.setVisible(!leftCollapsed);
/* 273 */         FlatSplitPaneUI.FlatSplitPaneDivider.this.rightButton.setVisible(!FlatSplitPaneUI.FlatSplitPaneDivider.this.isRightCollapsed());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatSplitPaneUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */