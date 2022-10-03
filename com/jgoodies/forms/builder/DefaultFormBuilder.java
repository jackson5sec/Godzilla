/*     */ package com.jgoodies.forms.builder;
/*     */ 
/*     */ import com.jgoodies.common.internal.StringResourceAccessor;
/*     */ import com.jgoodies.forms.internal.AbstractBuilder;
/*     */ import com.jgoodies.forms.layout.ConstantSize;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import com.jgoodies.forms.layout.FormSpecs;
/*     */ import com.jgoodies.forms.layout.RowSpec;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public final class DefaultFormBuilder
/*     */   extends I15dPanelBuilder
/*     */ {
/* 231 */   private RowSpec defaultRowSpec = FormSpecs.PREF_ROWSPEC;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 239 */   private RowSpec lineGapSpec = FormSpecs.LINE_GAP_ROWSPEC;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   private RowSpec paragraphGapSpec = FormSpecs.PARAGRAPH_GAP_ROWSPEC;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 256 */   private int leadingColumnOffset = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean rowGroupingEnabled = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormBuilder(FormLayout layout) {
/* 278 */     this(layout, new JPanel(null));
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
/*     */   public DefaultFormBuilder(FormLayout layout, JPanel container) {
/* 292 */     this(layout, (StringResourceAccessor)null, container);
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
/*     */   public DefaultFormBuilder(FormLayout layout, ResourceBundle bundle) {
/* 307 */     super(layout, bundle);
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
/*     */   public DefaultFormBuilder(FormLayout layout, ResourceBundle bundle, JPanel container) {
/* 323 */     super(layout, bundle, container);
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
/*     */   public DefaultFormBuilder(FormLayout layout, StringResourceAccessor localizer) {
/* 337 */     super(layout, localizer);
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
/*     */   public DefaultFormBuilder(FormLayout layout, StringResourceAccessor localizer, JPanel container) {
/* 352 */     super(layout, localizer, container);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormBuilder background(Color background) {
/* 360 */     super.background(background);
/* 361 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormBuilder border(Border border) {
/* 367 */     super.border(border);
/* 368 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormBuilder border(String emptyBorderSpec) {
/* 374 */     super.border(emptyBorderSpec);
/* 375 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormBuilder padding(EmptyBorder padding) {
/* 381 */     super.padding(padding);
/* 382 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormBuilder padding(String paddingSpec, Object... args) {
/* 388 */     super.padding(paddingSpec, new Object[0]);
/* 389 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormBuilder opaque(boolean b) {
/* 395 */     super.opaque(b);
/* 396 */     return this;
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
/*     */   public DefaultFormBuilder defaultRowSpec(RowSpec defaultRowSpec) {
/* 409 */     this.defaultRowSpec = defaultRowSpec;
/* 410 */     return this;
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
/*     */   public DefaultFormBuilder lineGapSize(ConstantSize lineGapSize) {
/* 428 */     RowSpec rowSpec = RowSpec.createGap(lineGapSize);
/* 429 */     this.lineGapSpec = rowSpec;
/* 430 */     return this;
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
/*     */   public DefaultFormBuilder paragraphGapSize(ConstantSize paragraphGapSize) {
/* 448 */     RowSpec rowSpec = RowSpec.createGap(paragraphGapSize);
/* 449 */     this.paragraphGapSpec = rowSpec;
/* 450 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormBuilder leadingColumnOffset(int columnOffset) {
/* 460 */     this.leadingColumnOffset = columnOffset;
/* 461 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormBuilder rowGroupingEnabled(boolean enabled) {
/* 471 */     this.rowGroupingEnabled = enabled;
/* 472 */     return this;
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
/*     */   public final void appendLineGapRow() {
/* 485 */     appendRow(this.lineGapSpec);
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
/*     */   public void append(Component component) {
/* 498 */     append(component, 1);
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
/*     */   public void append(Component component, int columnSpan) {
/* 510 */     ensureCursorColumnInGrid();
/* 511 */     ensureHasGapRow(this.lineGapSpec);
/* 512 */     ensureHasComponentLine();
/*     */     
/* 514 */     add(component, createLeftAdjustedConstraints(columnSpan));
/* 515 */     nextColumn(columnSpan + 1);
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
/*     */   public void append(Component c1, Component c2) {
/* 527 */     append(c1);
/* 528 */     append(c2);
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
/*     */   public void append(Component c1, Component c2, Component c3) {
/* 541 */     append(c1);
/* 542 */     append(c2);
/* 543 */     append(c3);
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
/*     */   public JLabel append(String textWithMnemonic) {
/* 556 */     JLabel label = getComponentFactory().createLabel(textWithMnemonic);
/* 557 */     append(label);
/* 558 */     return label;
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
/*     */   public JLabel append(String textWithMnemonic, Component component) {
/* 574 */     return append(textWithMnemonic, component, 1);
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
/*     */   public JLabel append(String textWithMnemonic, Component c, boolean nextLine) {
/* 593 */     JLabel label = append(textWithMnemonic, c);
/* 594 */     if (nextLine) {
/* 595 */       nextLine();
/*     */     }
/* 597 */     return label;
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
/*     */   public JLabel append(String textWithMnemonic, Component c, int columnSpan) {
/* 615 */     JLabel label = append(textWithMnemonic);
/* 616 */     label.setLabelFor(c);
/* 617 */     append(c, columnSpan);
/* 618 */     return label;
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
/*     */   public JLabel append(String textWithMnemonic, Component c1, Component c2) {
/* 635 */     JLabel label = append(textWithMnemonic, c1);
/* 636 */     append(c2);
/* 637 */     return label;
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
/*     */   public JLabel append(String textWithMnemonic, Component c1, Component c2, int colSpan) {
/* 655 */     JLabel label = append(textWithMnemonic, c1);
/* 656 */     append(c2, colSpan);
/* 657 */     return label;
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
/*     */   public JLabel append(String textWithMnemonic, Component c1, Component c2, Component c3) {
/* 675 */     JLabel label = append(textWithMnemonic, c1, c2);
/* 676 */     append(c3);
/* 677 */     return label;
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
/*     */   public JLabel append(String textWithMnemonic, Component c1, Component c2, Component c3, Component c4) {
/* 696 */     JLabel label = append(textWithMnemonic, c1, c2, c3);
/* 697 */     append(c4);
/* 698 */     return label;
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
/*     */   public JLabel appendI15d(String resourceKey) {
/* 712 */     return append(getResourceString(resourceKey));
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
/*     */   public JLabel appendI15d(String resourceKey, Component component) {
/* 728 */     return append(getResourceString(resourceKey), component, 1);
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
/*     */   public JLabel appendI15d(String resourceKey, Component component, boolean nextLine) {
/* 746 */     return append(getResourceString(resourceKey), component, nextLine);
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
/*     */   public JLabel appendI15d(String resourceKey, Component c, int columnSpan) {
/* 765 */     return append(getResourceString(resourceKey), c, columnSpan);
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
/*     */   public JLabel appendI15d(String resourceKey, Component c1, Component c2) {
/* 783 */     return append(getResourceString(resourceKey), c1, c2);
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
/*     */   public JLabel appendI15d(String resourceKey, Component c1, Component c2, int colSpan) {
/* 802 */     return append(getResourceString(resourceKey), c1, c2, colSpan);
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
/*     */   public JLabel appendI15d(String resourceKey, Component c1, Component c2, Component c3) {
/* 821 */     return append(getResourceString(resourceKey), c1, c2, c3);
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
/*     */   public JLabel appendI15d(String resourceKey, Component c1, Component c2, Component c3, Component c4) {
/* 841 */     return append(getResourceString(resourceKey), c1, c2, c3, c4);
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
/*     */   public JLabel appendTitle(String textWithMnemonic) {
/* 854 */     JLabel titleLabel = getComponentFactory().createTitle(textWithMnemonic);
/* 855 */     append(titleLabel);
/* 856 */     return titleLabel;
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
/*     */   public JLabel appendI15dTitle(String resourceKey) {
/* 868 */     return appendTitle(getResourceString(resourceKey));
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
/*     */   public JComponent appendSeparator() {
/* 880 */     return appendSeparator("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JComponent appendSeparator(String text) {
/* 891 */     ensureCursorColumnInGrid();
/* 892 */     ensureHasGapRow(this.paragraphGapSpec);
/* 893 */     ensureHasComponentLine();
/*     */     
/* 895 */     setColumn(super.getLeadingColumn());
/* 896 */     int columnSpan = getColumnCount();
/* 897 */     setColumnSpan(getColumnCount());
/* 898 */     JComponent titledSeparator = addSeparator(text);
/* 899 */     setColumnSpan(1);
/* 900 */     nextColumn(columnSpan);
/* 901 */     return titledSeparator;
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
/*     */   public JComponent appendI15dSeparator(String resourceKey) {
/* 913 */     return appendSeparator(getResourceString(resourceKey));
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
/*     */   protected int getLeadingColumn() {
/* 927 */     int column = super.getLeadingColumn();
/* 928 */     return column + this.leadingColumnOffset * getColumnIncrementSign();
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
/*     */   private void ensureCursorColumnInGrid() {
/* 940 */     if ((isLeftToRight() && getColumn() > getColumnCount()) || (!isLeftToRight() && getColumn() < 1))
/*     */     {
/* 942 */       nextLine();
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
/*     */   private void ensureHasGapRow(RowSpec gapRowSpec) {
/* 955 */     if (getRow() == 1 || getRow() <= getRowCount()) {
/*     */       return;
/*     */     }
/*     */     
/* 959 */     if (getRow() <= getRowCount()) {
/* 960 */       RowSpec rowSpec = getCursorRowSpec();
/* 961 */       if (rowSpec == gapRowSpec) {
/*     */         return;
/*     */       }
/*     */     } 
/* 965 */     appendRow(gapRowSpec);
/* 966 */     nextLine();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void ensureHasComponentLine() {
/* 975 */     if (getRow() <= getRowCount()) {
/*     */       return;
/*     */     }
/* 978 */     appendRow(this.defaultRowSpec);
/* 979 */     if (this.rowGroupingEnabled) {
/* 980 */       getLayout().addGroupedRow(getRow());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RowSpec getCursorRowSpec() {
/* 991 */     return getLayout().getRowSpec(getRow());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\builder\DefaultFormBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */