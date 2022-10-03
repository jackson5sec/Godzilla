/*     */ package com.jgoodies.forms.internal;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.forms.layout.ColumnSpec;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import com.jgoodies.forms.layout.FormSpecs;
/*     */ import com.jgoodies.forms.layout.RowSpec;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.border.Border;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractButtonPanelBuilder<B extends AbstractButtonPanelBuilder<B>>
/*     */   extends AbstractBuilder<B>
/*     */ {
/*     */   private boolean leftToRight;
/*     */   protected boolean focusGrouped = false;
/*     */   
/*     */   protected AbstractButtonPanelBuilder(FormLayout layout, JPanel container) {
/* 109 */     super(layout, container);
/* 110 */     opaque(false);
/* 111 */     ComponentOrientation orientation = container.getComponentOrientation();
/* 112 */     this.leftToRight = (orientation.isLeftToRight() || !orientation.isHorizontal());
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
/*     */   public final JPanel build() {
/* 127 */     if (!this.focusGrouped) {
/* 128 */       List<AbstractButton> buttons = new ArrayList<AbstractButton>();
/* 129 */       for (Component component : getPanel().getComponents()) {
/* 130 */         if (component instanceof AbstractButton) {
/* 131 */           buttons.add((AbstractButton)component);
/*     */         }
/*     */       } 
/* 134 */       FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons.<AbstractButton>toArray(new AbstractButton[0]));
/* 135 */       this.focusGrouped = true;
/*     */     } 
/* 137 */     return getPanel();
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
/*     */   @Deprecated
/*     */   public final void setBackground(Color background) {
/* 154 */     getPanel().setBackground(background);
/* 155 */     opaque(true);
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
/*     */   @Deprecated
/*     */   public final void setBorder(Border border) {
/* 170 */     getPanel().setBorder(border);
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
/*     */   @Deprecated
/*     */   public final void setOpaque(boolean b) {
/* 187 */     getPanel().setOpaque(b);
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
/*     */   public final boolean isLeftToRight() {
/* 205 */     return this.leftToRight;
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
/*     */   public final void setLeftToRight(boolean b) {
/* 220 */     this.leftToRight = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void nextColumn() {
/* 230 */     nextColumn(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void nextColumn(int columns) {
/* 240 */     this.currentCellConstraints.gridX += columns * getColumnIncrementSign();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final int getColumn() {
/* 245 */     return this.currentCellConstraints.gridX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int getRow() {
/* 255 */     return this.currentCellConstraints.gridY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void nextRow() {
/* 263 */     nextRow(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void nextRow(int rows) {
/* 273 */     this.currentCellConstraints.gridY += rows;
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
/*     */   protected final void appendColumn(ColumnSpec columnSpec) {
/* 285 */     getLayout().appendColumn(columnSpec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void appendGlueColumn() {
/* 296 */     appendColumn(FormSpecs.GLUE_COLSPEC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void appendRelatedComponentsGapColumn() {
/* 307 */     appendColumn(FormSpecs.RELATED_GAP_COLSPEC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void appendUnrelatedComponentsGapColumn() {
/* 318 */     appendColumn(FormSpecs.UNRELATED_GAP_COLSPEC);
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
/*     */   protected final void appendRow(RowSpec rowSpec) {
/* 330 */     getLayout().appendRow(rowSpec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void appendGlueRow() {
/* 341 */     appendRow(FormSpecs.GLUE_ROWSPEC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void appendRelatedComponentsGapRow() {
/* 352 */     appendRow(FormSpecs.RELATED_GAP_ROWSPEC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void appendUnrelatedComponentsGapRow() {
/* 363 */     appendRow(FormSpecs.UNRELATED_GAP_ROWSPEC);
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
/*     */   protected final Component add(Component component) {
/* 378 */     getPanel().add(component, this.currentCellConstraints);
/* 379 */     this.focusGrouped = false;
/* 380 */     return component;
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
/*     */   protected abstract AbstractButtonPanelBuilder addButton(JComponent paramJComponent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractButtonPanelBuilder addButton(JComponent... buttons) {
/* 414 */     Preconditions.checkNotNull(buttons, "The button array must not be null.");
/* 415 */     Preconditions.checkArgument((buttons.length > 0), "The button array must not be empty.");
/* 416 */     boolean needsGap = false;
/* 417 */     for (JComponent button : buttons) {
/* 418 */       if (button == null) {
/* 419 */         addUnrelatedGap();
/* 420 */         needsGap = false;
/*     */       } else {
/*     */         
/* 423 */         if (needsGap) {
/* 424 */           addRelatedGap();
/*     */         }
/* 426 */         addButton(button);
/* 427 */         needsGap = true;
/*     */       } 
/* 429 */     }  return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractButtonPanelBuilder addButton(Action... actions) {
/* 440 */     Preconditions.checkNotNull(actions, "The Action array must not be null.");
/* 441 */     int length = actions.length;
/* 442 */     Preconditions.checkArgument((length > 0), "The Action array must not be empty.");
/* 443 */     JButton[] buttons = new JButton[length];
/* 444 */     for (int i = 0; i < length; i++) {
/* 445 */       Action action = actions[i];
/* 446 */       buttons[i] = (action == null) ? null : createButton(action);
/*     */     } 
/* 448 */     return addButton((JComponent[])buttons);
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
/*     */   protected abstract AbstractButtonPanelBuilder addRelatedGap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract AbstractButtonPanelBuilder addUnrelatedGap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JButton createButton(Action action) {
/* 481 */     return getComponentFactory().createButton(action);
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
/*     */   private int getColumnIncrementSign() {
/* 494 */     return isLeftToRight() ? 1 : -1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\internal\AbstractButtonPanelBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */