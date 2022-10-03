/*     */ package com.jgoodies.forms.internal;
/*     */ 
/*     */ import com.jgoodies.forms.layout.CellConstraints;
/*     */ import com.jgoodies.forms.layout.ColumnSpec;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import com.jgoodies.forms.layout.FormSpecs;
/*     */ import com.jgoodies.forms.layout.RowSpec;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
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
/*     */ public abstract class AbstractFormBuilder<B extends AbstractFormBuilder<B>>
/*     */   extends AbstractBuilder<B>
/*     */ {
/*     */   private boolean leftToRight;
/*     */   
/*     */   protected AbstractFormBuilder(FormLayout layout, JPanel panel) {
/*  86 */     super(layout, panel);
/*  87 */     ComponentOrientation orientation = panel.getComponentOrientation();
/*  88 */     this.leftToRight = (orientation.isLeftToRight() || !orientation.isHorizontal());
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
/*     */   public final boolean isLeftToRight() {
/* 107 */     return this.leftToRight;
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
/* 122 */     this.leftToRight = b;
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
/*     */   public final int getColumn() {
/* 134 */     return this.currentCellConstraints.gridX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setColumn(int column) {
/* 144 */     this.currentCellConstraints.gridX = column;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getRow() {
/* 154 */     return this.currentCellConstraints.gridY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setRow(int row) {
/* 164 */     this.currentCellConstraints.gridY = row;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setColumnSpan(int columnSpan) {
/* 174 */     this.currentCellConstraints.gridWidth = columnSpan;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setRowSpan(int rowSpan) {
/* 184 */     this.currentCellConstraints.gridHeight = rowSpan;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setOrigin(int column, int row) {
/* 195 */     setColumn(column);
/* 196 */     setRow(row);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setExtent(int columnSpan, int rowSpan) {
/* 207 */     setColumnSpan(columnSpan);
/* 208 */     setRowSpan(rowSpan);
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
/*     */   public final void setBounds(int column, int row, int columnSpan, int rowSpan) {
/* 222 */     setColumn(column);
/* 223 */     setRow(row);
/* 224 */     setColumnSpan(columnSpan);
/* 225 */     setRowSpan(rowSpan);
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
/*     */   public final void setHAlignment(CellConstraints.Alignment alignment) {
/* 237 */     (cellConstraints()).hAlign = alignment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setVAlignment(CellConstraints.Alignment alignment) {
/* 246 */     (cellConstraints()).vAlign = alignment;
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
/*     */   public final void setAlignment(CellConstraints.Alignment hAlign, CellConstraints.Alignment vAlign) {
/* 258 */     setHAlignment(hAlign);
/* 259 */     setVAlignment(vAlign);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void nextColumn() {
/* 269 */     nextColumn(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void nextColumn(int columns) {
/* 279 */     (cellConstraints()).gridX += columns * getColumnIncrementSign();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void nextRow() {
/* 287 */     nextRow(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void nextRow(int rows) {
/* 297 */     (cellConstraints()).gridY += rows;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void nextLine() {
/* 306 */     nextLine(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void nextLine(int lines) {
/* 317 */     nextRow(lines);
/* 318 */     setColumn(getLeadingColumn());
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
/*     */   public final void appendColumn(ColumnSpec columnSpec) {
/* 332 */     getLayout().appendColumn(columnSpec);
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
/*     */   public final void appendColumn(String encodedColumnSpec) {
/* 345 */     appendColumn(ColumnSpec.decode(encodedColumnSpec));
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
/*     */   public final void appendGlueColumn() {
/* 357 */     appendColumn(FormSpecs.GLUE_COLSPEC);
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
/*     */   public final void appendLabelComponentsGapColumn() {
/* 372 */     appendColumn(FormSpecs.LABEL_COMPONENT_GAP_COLSPEC);
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
/*     */   public final void appendRelatedComponentsGapColumn() {
/* 384 */     appendColumn(FormSpecs.RELATED_GAP_COLSPEC);
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
/*     */   public final void appendUnrelatedComponentsGapColumn() {
/* 396 */     appendColumn(FormSpecs.UNRELATED_GAP_COLSPEC);
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
/*     */   public final void appendRow(RowSpec rowSpec) {
/* 410 */     getLayout().appendRow(rowSpec);
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
/*     */   public final void appendRow(String encodedRowSpec) {
/* 423 */     appendRow(RowSpec.decode(encodedRowSpec));
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
/*     */   public final void appendGlueRow() {
/* 435 */     appendRow(FormSpecs.GLUE_ROWSPEC);
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
/*     */   public final void appendRelatedComponentsGapRow() {
/* 447 */     appendRow(FormSpecs.RELATED_GAP_ROWSPEC);
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
/*     */   public final void appendUnrelatedComponentsGapRow() {
/* 459 */     appendRow(FormSpecs.UNRELATED_GAP_ROWSPEC);
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
/*     */   public final void appendParagraphGapRow() {
/* 473 */     appendRow(FormSpecs.PARAGRAPH_GAP_ROWSPEC);
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
/*     */   public Component add(Component component, CellConstraints cellConstraints) {
/* 487 */     getPanel().add(component, cellConstraints);
/* 488 */     return component;
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
/*     */   public final Component add(Component component, String encodedCellConstraints) {
/* 500 */     getPanel().add(component, new CellConstraints(encodedCellConstraints));
/* 501 */     return component;
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
/*     */   public final Component add(Component component) {
/* 519 */     add(component, cellConstraints());
/* 520 */     return component;
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
/*     */   protected final CellConstraints cellConstraints() {
/* 533 */     return this.currentCellConstraints;
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
/*     */   protected int getLeadingColumn() {
/* 546 */     return isLeftToRight() ? 1 : getColumnCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int getColumnIncrementSign() {
/* 557 */     return isLeftToRight() ? 1 : -1;
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
/*     */   protected final CellConstraints createLeftAdjustedConstraints(int columnSpan) {
/* 570 */     int firstColumn = isLeftToRight() ? getColumn() : (getColumn() + 1 - columnSpan);
/*     */ 
/*     */     
/* 573 */     return new CellConstraints(firstColumn, getRow(), columnSpan, (cellConstraints()).gridHeight);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\internal\AbstractFormBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */