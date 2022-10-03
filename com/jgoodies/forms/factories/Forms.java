/*     */ package com.jgoodies.forms.factories;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.forms.builder.ButtonBarBuilder;
/*     */ import com.jgoodies.forms.builder.ButtonStackBuilder;
/*     */ import com.jgoodies.forms.builder.PanelBuilder;
/*     */ import com.jgoodies.forms.internal.FocusTraversalUtilsAccessor;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JRadioButton;
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
/*     */ public final class Forms
/*     */ {
/*     */   public static JComponent single(String columnSpec, String rowSpec, JComponent component) {
/*  89 */     Preconditions.checkNotBlank(columnSpec, "The %1$s must not be null, empty, or whitespace.", new Object[] { "column specification" });
/*  90 */     Preconditions.checkNotBlank(rowSpec, "The %1$s must not be null, empty, or whitespace.", new Object[] { "row specification" });
/*  91 */     Preconditions.checkNotNull(component, "The %1$s must not be null.", new Object[] { "component" });
/*  92 */     FormLayout layout = new FormLayout(columnSpec, rowSpec);
/*  93 */     PanelBuilder builder = new PanelBuilder(layout);
/*  94 */     builder.add(component, CC.xy(1, 1));
/*  95 */     return builder.build();
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
/*     */   public static JComponent centered(JComponent component) {
/* 112 */     return single("center:pref:grow", "c:p:g", component);
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
/*     */   public static JComponent border(Border border, JComponent component) {
/* 130 */     JComponent container = single("fill:pref", "f:p", component);
/* 131 */     container.setBorder(border);
/* 132 */     return container;
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
/*     */   @Deprecated
/*     */   public static JComponent border(String emptyBorderSpec, JComponent component) {
/* 152 */     return padding(component, emptyBorderSpec, new Object[0]);
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
/*     */   public static JComponent padding(JComponent component, EmptyBorder padding) {
/* 170 */     JComponent container = single("fill:pref", "f:p", component);
/* 171 */     container.setBorder(padding);
/* 172 */     return container;
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
/*     */   public static JComponent padding(JComponent component, String paddingSpec, Object... args) {
/* 194 */     return padding(component, Paddings.createPadding(paddingSpec, args));
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
/*     */   public static JComponent horizontal(String gapColSpec, JComponent... components) {
/* 215 */     Preconditions.checkNotBlank(gapColSpec, "The %1$s must not be null, empty, or whitespace.", new Object[] { "gap column specification" });
/* 216 */     Preconditions.checkNotNull(components, "The %1$s must not be null.", new Object[] { "component array" });
/* 217 */     Preconditions.checkArgument((components.length > 1), "You must provide more than one component.");
/* 218 */     FormLayout layout = new FormLayout((components.length - 1) + "*(pref, " + gapColSpec + "), pref", "p");
/*     */ 
/*     */     
/* 221 */     PanelBuilder builder = new PanelBuilder(layout);
/* 222 */     int column = 1;
/* 223 */     for (JComponent component : components) {
/* 224 */       builder.add(component, CC.xy(column, 1));
/* 225 */       column += 2;
/*     */     } 
/* 227 */     return builder.build();
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
/*     */   public static JComponent vertical(String gapRowSpec, JComponent... components) {
/* 248 */     Preconditions.checkNotBlank(gapRowSpec, "The %1$s must not be null, empty, or whitespace.", new Object[] { "gap row specification" });
/* 249 */     Preconditions.checkNotNull(components, "The %1$s must not be null.", new Object[] { "component array" });
/* 250 */     Preconditions.checkArgument((components.length > 1), "You must provide more than one component.");
/* 251 */     FormLayout layout = new FormLayout("pref", (components.length - 1) + "*(p, " + gapRowSpec + "), p");
/*     */ 
/*     */     
/* 254 */     PanelBuilder builder = new PanelBuilder(layout);
/* 255 */     int row = 1;
/* 256 */     for (JComponent component : components) {
/* 257 */       builder.add(component, CC.xy(1, row));
/* 258 */       row += 2;
/*     */     } 
/* 260 */     return builder.build();
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
/*     */   public static JComponent buttonBar(JComponent... buttons) {
/* 284 */     return ButtonBarBuilder.create().addButton(buttons).build();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static JComponent buttonStack(JComponent... buttons) {
/* 312 */     return ButtonStackBuilder.create().addButton(buttons).build();
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
/*     */   public static JComponent checkBoxBar(JCheckBox... checkBoxes) {
/* 334 */     return buildGroupedButtonBar((AbstractButton[])checkBoxes);
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
/*     */   public static JComponent checkBoxStack(JCheckBox... checkBoxes) {
/* 356 */     return buildGroupedButtonStack((AbstractButton[])checkBoxes);
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
/*     */   public static JComponent radioButtonBar(JRadioButton... radioButtons) {
/* 376 */     return buildGroupedButtonBar((AbstractButton[])radioButtons);
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
/*     */   public static JComponent radioButtonStack(JRadioButton... radioButtons) {
/* 398 */     return buildGroupedButtonStack((AbstractButton[])radioButtons);
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
/*     */   private static JComponent buildGroupedButtonBar(AbstractButton... buttons) {
/* 412 */     Preconditions.checkArgument((buttons.length > 1), "You must provide more than one button.");
/* 413 */     FormLayout layout = new FormLayout(String.format("pref, %s*($rgap, pref)", new Object[] { Integer.valueOf(buttons.length - 1) }), "p");
/*     */ 
/*     */     
/* 416 */     PanelBuilder builder = new PanelBuilder(layout);
/* 417 */     int column = 1;
/* 418 */     for (AbstractButton button : buttons) {
/* 419 */       builder.add(button, CC.xy(column, 1));
/* 420 */       column += 2;
/*     */     } 
/* 422 */     FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
/* 423 */     return builder.build();
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
/*     */   private static JComponent buildGroupedButtonStack(AbstractButton... buttons) {
/* 435 */     Preconditions.checkArgument((buttons.length > 1), "You must provide more than one button.");
/* 436 */     FormLayout layout = new FormLayout("pref", String.format("p, %s*(0, p)", new Object[] { Integer.valueOf(buttons.length - 1) }));
/*     */ 
/*     */     
/* 439 */     PanelBuilder builder = new PanelBuilder(layout);
/* 440 */     int row = 1;
/* 441 */     for (AbstractButton button : buttons) {
/* 442 */       builder.add(button, CC.xy(1, row));
/* 443 */       row += 2;
/*     */     } 
/* 445 */     FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
/* 446 */     return builder.build();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\factories\Forms.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */