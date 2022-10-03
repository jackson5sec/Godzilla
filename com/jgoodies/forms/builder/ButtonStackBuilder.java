/*     */ package com.jgoodies.forms.builder;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.forms.internal.AbstractButtonPanelBuilder;
/*     */ import com.jgoodies.forms.layout.ColumnSpec;
/*     */ import com.jgoodies.forms.layout.ConstantSize;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import com.jgoodies.forms.layout.FormSpecs;
/*     */ import com.jgoodies.forms.layout.RowSpec;
/*     */ import com.jgoodies.forms.layout.Size;
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
/*     */ public final class ButtonStackBuilder
/*     */   extends AbstractButtonPanelBuilder<ButtonStackBuilder>
/*     */ {
/*  92 */   private static final ColumnSpec[] COL_SPECS = new ColumnSpec[] { FormSpecs.BUTTON_COLSPEC };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   private static final RowSpec[] ROW_SPECS = new RowSpec[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonStackBuilder() {
/* 109 */     this(new JPanel(null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonStackBuilder(JPanel panel) {
/* 120 */     super(new FormLayout(COL_SPECS, ROW_SPECS), panel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ButtonStackBuilder create() {
/* 131 */     return new ButtonStackBuilder();
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
/*     */   public ButtonStackBuilder addButton(JComponent button) {
/* 152 */     Preconditions.checkNotNull(button, "The button must not be null.");
/* 153 */     getLayout().appendRow(FormSpecs.PREF_ROWSPEC);
/* 154 */     add(button);
/* 155 */     nextRow();
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonStackBuilder addButton(JComponent... buttons) {
/* 162 */     super.addButton(buttons);
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonStackBuilder addButton(Action... actions) {
/* 171 */     super.addButton(actions);
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonStackBuilder addFixed(JComponent component) {
/* 182 */     getLayout().appendRow(FormSpecs.PREF_ROWSPEC);
/* 183 */     add(component);
/* 184 */     nextRow();
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonStackBuilder addGlue() {
/* 196 */     appendGlueRow();
/* 197 */     nextRow();
/* 198 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonStackBuilder addRelatedGap() {
/* 204 */     appendRelatedComponentsGapRow();
/* 205 */     nextRow();
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonStackBuilder addUnrelatedGap() {
/* 212 */     appendUnrelatedComponentsGapRow();
/* 213 */     nextRow();
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonStackBuilder addStrut(ConstantSize size) {
/* 224 */     getLayout().appendRow(new RowSpec(RowSpec.TOP, (Size)size, 0.0D));
/*     */ 
/*     */     
/* 227 */     nextRow();
/* 228 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\builder\ButtonStackBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */