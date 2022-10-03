/*     */ package com.jgoodies.forms.internal;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.forms.FormsSetup;
/*     */ import com.jgoodies.forms.factories.ComponentFactory;
/*     */ import com.jgoodies.forms.factories.Paddings;
/*     */ import com.jgoodies.forms.layout.CellConstraints;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.LayoutManager;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractBuilder<B extends AbstractBuilder<B>>
/*     */ {
/*     */   private final JPanel panel;
/*     */   private final FormLayout layout;
/*     */   protected final CellConstraints currentCellConstraints;
/*     */   private ComponentFactory componentFactory;
/*     */   
/*     */   protected AbstractBuilder(FormLayout layout, JPanel panel) {
/* 103 */     this.layout = (FormLayout)Preconditions.checkNotNull(layout, "The %1$s must not be null.", new Object[] { "layout" });
/* 104 */     this.panel = (JPanel)Preconditions.checkNotNull(panel, "The %1$s must not be null.", new Object[] { "panel" });
/* 105 */     panel.setLayout((LayoutManager)layout);
/* 106 */     this.currentCellConstraints = new CellConstraints();
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
/*     */   public final JPanel getPanel() {
/* 120 */     return this.panel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract JPanel build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final Container getContainer() {
/* 136 */     return this.panel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FormLayout getLayout() {
/* 146 */     return this.layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getColumnCount() {
/* 156 */     return getLayout().getColumnCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getRowCount() {
/* 166 */     return getLayout().getRowCount();
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
/*     */   public B background(Color background) {
/* 180 */     getPanel().setBackground(background);
/* 181 */     opaque(true);
/* 182 */     return (B)this;
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
/*     */   public B border(Border border) {
/* 194 */     getPanel().setBorder(border);
/* 195 */     return (B)this;
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
/*     */   
/*     */   @Deprecated
/*     */   public B border(String paddingSpec) {
/* 219 */     padding((EmptyBorder)Paddings.createPadding(paddingSpec, new Object[0]));
/* 220 */     return (B)this;
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
/*     */   public B padding(EmptyBorder padding) {
/* 234 */     getPanel().setBorder(padding);
/* 235 */     return (B)this;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public B padding(String paddingSpec, Object... args) {
/* 260 */     padding((EmptyBorder)Paddings.createPadding(paddingSpec, args));
/* 261 */     return (B)this;
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
/*     */   public B opaque(boolean b) {
/* 273 */     getPanel().setOpaque(b);
/* 274 */     return (B)this;
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
/*     */   public final ComponentFactory getComponentFactory() {
/* 290 */     if (this.componentFactory == null) {
/* 291 */       this.componentFactory = createComponentFactory();
/*     */     }
/* 293 */     return this.componentFactory;
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
/*     */   public final void setComponentFactory(ComponentFactory newFactory) {
/* 307 */     this.componentFactory = newFactory;
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
/*     */   protected ComponentFactory createComponentFactory() {
/* 323 */     return FormsSetup.getComponentFactoryDefault();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\internal\AbstractBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */