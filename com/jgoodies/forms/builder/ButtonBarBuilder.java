/*     */ package com.jgoodies.forms.builder;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.forms.internal.AbstractButtonPanelBuilder;
/*     */ import com.jgoodies.forms.layout.ColumnSpec;
/*     */ import com.jgoodies.forms.layout.ConstantSize;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import com.jgoodies.forms.layout.FormSpecs;
/*     */ import com.jgoodies.forms.layout.RowSpec;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ButtonBarBuilder
/*     */   extends AbstractButtonPanelBuilder<ButtonBarBuilder>
/*     */ {
/* 104 */   private static final ColumnSpec[] COL_SPECS = new ColumnSpec[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   private static final RowSpec[] ROW_SPECS = new RowSpec[] { RowSpec.decode("center:pref") };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonBarBuilder() {
/* 120 */     this(new JPanel(null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonBarBuilder(JPanel panel) {
/* 130 */     super(new FormLayout(COL_SPECS, ROW_SPECS), panel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ButtonBarBuilder create() {
/* 140 */     return new ButtonBarBuilder();
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
/*     */   public ButtonBarBuilder addButton(JComponent button) {
/* 161 */     Preconditions.checkNotNull(button, "The button to add must not be null.");
/* 162 */     getLayout().appendColumn(FormSpecs.BUTTON_COLSPEC);
/* 163 */     add(button);
/* 164 */     nextColumn();
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonBarBuilder addButton(JComponent... buttons) {
/* 171 */     super.addButton(buttons);
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonBarBuilder addButton(Action... actions) {
/* 178 */     super.addButton(actions);
/* 179 */     return this;
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
/*     */   public ButtonBarBuilder addFixed(JComponent component) {
/* 196 */     getLayout().appendColumn(FormSpecs.PREF_COLSPEC);
/* 197 */     add(component);
/* 198 */     nextColumn();
/* 199 */     return this;
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
/*     */   public ButtonBarBuilder addGrowing(JComponent component) {
/* 213 */     getLayout().appendColumn(FormSpecs.GROWING_BUTTON_COLSPEC);
/* 214 */     add(component);
/* 215 */     nextColumn();
/* 216 */     return this;
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
/*     */   public ButtonBarBuilder addGlue() {
/* 229 */     appendGlueColumn();
/* 230 */     nextColumn();
/* 231 */     return this;
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
/*     */   public ButtonBarBuilder addRelatedGap() {
/* 244 */     appendRelatedComponentsGapColumn();
/* 245 */     nextColumn();
/* 246 */     return this;
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
/*     */   public ButtonBarBuilder addUnrelatedGap() {
/* 259 */     appendUnrelatedComponentsGapColumn();
/* 260 */     nextColumn();
/* 261 */     return this;
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
/*     */   public ButtonBarBuilder addStrut(ConstantSize width) {
/* 277 */     getLayout().appendColumn(ColumnSpec.createGap(width));
/* 278 */     nextColumn();
/* 279 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\builder\ButtonBarBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */