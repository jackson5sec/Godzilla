/*      */ package com.jgoodies.forms.layout;
/*      */ 
/*      */ import com.jgoodies.common.base.Objects;
/*      */ import com.jgoodies.common.base.Preconditions;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager2;
/*      */ import java.awt.Rectangle;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.swing.JComponent;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class FormLayout
/*      */   implements LayoutManager2, Serializable
/*      */ {
/*      */   private final List<ColumnSpec> colSpecs;
/*      */   private final List<RowSpec> rowSpecs;
/*      */   private int[][] colGroupIndices;
/*      */   private int[][] rowGroupIndices;
/*      */   private final Map<Component, CellConstraints> constraintMap;
/*      */   private boolean honorsVisibility = true;
/*      */   private transient List<Component>[] colComponents;
/*      */   private transient List<Component>[] rowComponents;
/*      */   private final ComponentSizeCache componentSizeCache;
/*      */   private final Measure minimumWidthMeasure;
/*      */   private final Measure minimumHeightMeasure;
/*      */   private final Measure preferredWidthMeasure;
/*      */   private final Measure preferredHeightMeasure;
/*      */   
/*      */   public FormLayout() {
/*  244 */     this(new ColumnSpec[0], new RowSpec[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormLayout(String encodedColumnSpecs) {
/*  278 */     this(encodedColumnSpecs, LayoutMap.getRoot());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormLayout(String encodedColumnSpecs, LayoutMap layoutMap) {
/*  318 */     this(ColumnSpec.decodeSpecs(encodedColumnSpecs, layoutMap), new RowSpec[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormLayout(String encodedColumnSpecs, String encodedRowSpecs) {
/*  355 */     this(encodedColumnSpecs, encodedRowSpecs, LayoutMap.getRoot());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormLayout(String encodedColumnSpecs, String encodedRowSpecs, LayoutMap layoutMap) {
/*  398 */     this(ColumnSpec.decodeSpecs(encodedColumnSpecs, layoutMap), RowSpec.decodeSpecs(encodedRowSpecs, layoutMap));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormLayout(ColumnSpec[] colSpecs) {
/*  414 */     this(colSpecs, new RowSpec[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormLayout(ColumnSpec[] colSpecs, RowSpec[] rowSpecs) {
/*  427 */     Preconditions.checkNotNull(colSpecs, "The column specifications must not be null.");
/*  428 */     Preconditions.checkNotNull(rowSpecs, "The row specifications must not be null.");
/*  429 */     this.colSpecs = new ArrayList<ColumnSpec>(Arrays.asList(colSpecs));
/*  430 */     this.rowSpecs = new ArrayList<RowSpec>(Arrays.asList(rowSpecs));
/*  431 */     this.colGroupIndices = new int[0][];
/*  432 */     this.rowGroupIndices = new int[0][];
/*  433 */     int initialCapacity = colSpecs.length * rowSpecs.length / 4;
/*  434 */     this.constraintMap = new HashMap<Component, CellConstraints>(initialCapacity);
/*  435 */     this.componentSizeCache = new ComponentSizeCache(initialCapacity);
/*  436 */     this.minimumWidthMeasure = new MinimumWidthMeasure(this.componentSizeCache);
/*  437 */     this.minimumHeightMeasure = new MinimumHeightMeasure(this.componentSizeCache);
/*  438 */     this.preferredWidthMeasure = new PreferredWidthMeasure(this.componentSizeCache);
/*  439 */     this.preferredHeightMeasure = new PreferredHeightMeasure(this.componentSizeCache);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColumnCount() {
/*  451 */     return this.colSpecs.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ColumnSpec getColumnSpec(int columnIndex) {
/*  463 */     return this.colSpecs.get(columnIndex - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColumnSpec(int columnIndex, ColumnSpec columnSpec) {
/*  476 */     Preconditions.checkNotNull(columnSpec, "The column spec must not be null.");
/*  477 */     this.colSpecs.set(columnIndex - 1, columnSpec);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendColumn(ColumnSpec columnSpec) {
/*  489 */     Preconditions.checkNotNull(columnSpec, "The column spec must not be null.");
/*  490 */     this.colSpecs.add(columnSpec);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertColumn(int columnIndex, ColumnSpec columnSpec) {
/*  511 */     if (columnIndex < 1 || columnIndex > getColumnCount()) {
/*  512 */       throw new IndexOutOfBoundsException("The column index " + columnIndex + "must be in the range [1, " + getColumnCount() + "].");
/*      */     }
/*      */ 
/*      */     
/*  516 */     this.colSpecs.add(columnIndex - 1, columnSpec);
/*  517 */     shiftComponentsHorizontally(columnIndex, false);
/*  518 */     adjustGroupIndices(this.colGroupIndices, columnIndex, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeColumn(int columnIndex) {
/*  556 */     if (columnIndex < 1 || columnIndex > getColumnCount()) {
/*  557 */       throw new IndexOutOfBoundsException("The column index " + columnIndex + " must be in the range [1, " + getColumnCount() + "].");
/*      */     }
/*      */ 
/*      */     
/*  561 */     this.colSpecs.remove(columnIndex - 1);
/*  562 */     shiftComponentsHorizontally(columnIndex, true);
/*  563 */     adjustGroupIndices(this.colGroupIndices, columnIndex, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRowCount() {
/*  573 */     return this.rowSpecs.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RowSpec getRowSpec(int rowIndex) {
/*  585 */     return this.rowSpecs.get(rowIndex - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRowSpec(int rowIndex, RowSpec rowSpec) {
/*  598 */     Preconditions.checkNotNull(rowSpec, "The row spec must not be null.");
/*  599 */     this.rowSpecs.set(rowIndex - 1, rowSpec);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendRow(RowSpec rowSpec) {
/*  610 */     Preconditions.checkNotNull(rowSpec, "The row spec must not be null.");
/*  611 */     this.rowSpecs.add(rowSpec);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertRow(int rowIndex, RowSpec rowSpec) {
/*  631 */     if (rowIndex < 1 || rowIndex > getRowCount()) {
/*  632 */       throw new IndexOutOfBoundsException("The row index " + rowIndex + " must be in the range [1, " + getRowCount() + "].");
/*      */     }
/*      */ 
/*      */     
/*  636 */     this.rowSpecs.add(rowIndex - 1, rowSpec);
/*  637 */     shiftComponentsVertically(rowIndex, false);
/*  638 */     adjustGroupIndices(this.rowGroupIndices, rowIndex, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeRow(int rowIndex) {
/*  673 */     if (rowIndex < 1 || rowIndex > getRowCount()) {
/*  674 */       throw new IndexOutOfBoundsException("The row index " + rowIndex + "must be in the range [1, " + getRowCount() + "].");
/*      */     }
/*      */ 
/*      */     
/*  678 */     this.rowSpecs.remove(rowIndex - 1);
/*  679 */     shiftComponentsVertically(rowIndex, true);
/*  680 */     adjustGroupIndices(this.rowGroupIndices, rowIndex, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void shiftComponentsHorizontally(int columnIndex, boolean remove) {
/*  693 */     int offset = remove ? -1 : 1;
/*  694 */     for (Map.Entry<Component, CellConstraints> element : this.constraintMap.entrySet()) {
/*  695 */       Map.Entry entry = element;
/*  696 */       CellConstraints constraints = (CellConstraints)entry.getValue();
/*  697 */       int x1 = constraints.gridX;
/*  698 */       int w = constraints.gridWidth;
/*  699 */       int x2 = x1 + w - 1;
/*  700 */       if (x1 == columnIndex && remove) {
/*  701 */         throw new IllegalStateException("The removed column " + columnIndex + " must not contain component origins.\n" + "Illegal component=" + entry.getKey());
/*      */       }
/*      */ 
/*      */       
/*  705 */       if (x1 >= columnIndex) {
/*  706 */         constraints.gridX += offset; continue;
/*  707 */       }  if (x2 >= columnIndex) {
/*  708 */         constraints.gridWidth += offset;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void shiftComponentsVertically(int rowIndex, boolean remove) {
/*  722 */     int offset = remove ? -1 : 1;
/*  723 */     for (Map.Entry<Component, CellConstraints> element : this.constraintMap.entrySet()) {
/*  724 */       Map.Entry entry = element;
/*  725 */       CellConstraints constraints = (CellConstraints)entry.getValue();
/*  726 */       int y1 = constraints.gridY;
/*  727 */       int h = constraints.gridHeight;
/*  728 */       int y2 = y1 + h - 1;
/*  729 */       if (y1 == rowIndex && remove) {
/*  730 */         throw new IllegalStateException("The removed row " + rowIndex + " must not contain component origins.\n" + "Illegal component=" + entry.getKey());
/*      */       }
/*      */ 
/*      */       
/*  734 */       if (y1 >= rowIndex) {
/*  735 */         constraints.gridY += offset; continue;
/*  736 */       }  if (y2 >= rowIndex) {
/*  737 */         constraints.gridHeight += offset;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void adjustGroupIndices(int[][] allGroupIndices, int modifiedIndex, boolean remove) {
/*  753 */     int offset = remove ? -1 : 1;
/*  754 */     for (int[] allGroupIndice : allGroupIndices) {
/*  755 */       int[] groupIndices = allGroupIndice;
/*  756 */       for (int i = 0; i < groupIndices.length; i++) {
/*  757 */         int index = groupIndices[i];
/*  758 */         if (index == modifiedIndex && remove) {
/*  759 */           throw new IllegalStateException("The removed index " + modifiedIndex + " must not be grouped.");
/*      */         }
/*  761 */         if (index >= modifiedIndex) {
/*  762 */           groupIndices[i] = groupIndices[i] + offset;
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CellConstraints getConstraints(Component component) {
/*  782 */     return (CellConstraints)getConstraints0(component).clone();
/*      */   }
/*      */ 
/*      */   
/*      */   private CellConstraints getConstraints0(Component component) {
/*  787 */     Preconditions.checkNotNull(component, "The component must not be null.");
/*  788 */     CellConstraints constraints = this.constraintMap.get(component);
/*  789 */     Preconditions.checkState((constraints != null), "The component has not been added to the container.");
/*  790 */     return constraints;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConstraints(Component component, CellConstraints constraints) {
/*  803 */     Preconditions.checkNotNull(component, "The component must not be null.");
/*  804 */     Preconditions.checkNotNull(constraints, "The constraints must not be null.");
/*  805 */     constraints.ensureValidGridBounds(getColumnCount(), getRowCount());
/*  806 */     this.constraintMap.put(component, (CellConstraints)constraints.clone());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeConstraints(Component component) {
/*  816 */     this.constraintMap.remove(component);
/*  817 */     this.componentSizeCache.removeEntry(component);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[][] getColumnGroups() {
/*  829 */     return deepClone(this.colGroupIndices);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColumnGroups(int[][] groupOfIndices) {
/*  853 */     setColumnGroupsImpl(groupOfIndices, true);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setColumnGroupsImpl(int[][] groupOfIndices, boolean checkIndices) {
/*  858 */     int maxColumn = getColumnCount();
/*  859 */     boolean[] usedIndices = new boolean[maxColumn + 1];
/*  860 */     for (int group = 0; group < groupOfIndices.length; group++) {
/*  861 */       int[] indices = groupOfIndices[group];
/*  862 */       if (checkIndices) {
/*  863 */         Preconditions.checkArgument((indices.length >= 2), "Each indice group must contain at least two indices.");
/*      */       }
/*  865 */       for (int indice : indices) {
/*  866 */         int colIndex = indice;
/*  867 */         if (colIndex < 1 || colIndex > maxColumn) {
/*  868 */           throw new IndexOutOfBoundsException("Invalid column group index " + colIndex + " in group " + (group + 1));
/*      */         }
/*      */ 
/*      */         
/*  872 */         if (usedIndices[colIndex]) {
/*  873 */           throw new IllegalArgumentException("Column index " + colIndex + " must not be used in multiple column groups.");
/*      */         }
/*      */         
/*  876 */         usedIndices[colIndex] = true;
/*      */       } 
/*      */     } 
/*  879 */     this.colGroupIndices = deepClone(groupOfIndices);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColumnGroup(int... indices) {
/*  902 */     Preconditions.checkNotNull(indices, "The %1$s must not be null.", new Object[] { "column group indices" });
/*  903 */     Preconditions.checkArgument((indices.length >= 2), "You must specify at least two indices.");
/*  904 */     setColumnGroups(new int[][] { indices });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addGroupedColumn(int columnIndex) {
/*  915 */     int[][] newColGroups = getColumnGroups();
/*      */     
/*  917 */     if (newColGroups.length == 0) {
/*  918 */       newColGroups = new int[][] { { columnIndex } };
/*      */     } else {
/*  920 */       int lastGroupIndex = newColGroups.length - 1;
/*  921 */       int[] lastGroup = newColGroups[lastGroupIndex];
/*  922 */       int groupSize = lastGroup.length;
/*  923 */       int[] newLastGroup = new int[groupSize + 1];
/*  924 */       System.arraycopy(lastGroup, 0, newLastGroup, 0, groupSize);
/*  925 */       newLastGroup[groupSize] = columnIndex;
/*  926 */       newColGroups[lastGroupIndex] = newLastGroup;
/*      */     } 
/*  928 */     setColumnGroupsImpl(newColGroups, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[][] getRowGroups() {
/*  937 */     return deepClone(this.rowGroupIndices);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRowGroups(int[][] groupOfIndices) {
/*  961 */     setRowGroupsImpl(groupOfIndices, true);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setRowGroupsImpl(int[][] groupOfIndices, boolean checkIndices) {
/*  966 */     int rowCount = getRowCount();
/*  967 */     boolean[] usedIndices = new boolean[rowCount + 1];
/*  968 */     for (int group = 0; group < groupOfIndices.length; group++) {
/*  969 */       int[] indices = groupOfIndices[group];
/*  970 */       if (checkIndices) {
/*  971 */         Preconditions.checkArgument((indices.length >= 2), "Each indice group must contain at least two indices.");
/*      */       }
/*  973 */       for (int indice : indices) {
/*  974 */         int rowIndex = indice;
/*  975 */         if (rowIndex < 1 || rowIndex > rowCount) {
/*  976 */           throw new IndexOutOfBoundsException("Invalid row group index " + rowIndex + " in group " + (group + 1));
/*      */         }
/*      */ 
/*      */         
/*  980 */         if (usedIndices[rowIndex]) {
/*  981 */           throw new IllegalArgumentException("Row index " + rowIndex + " must not be used in multiple row groups.");
/*      */         }
/*      */         
/*  984 */         usedIndices[rowIndex] = true;
/*      */       } 
/*      */     } 
/*  987 */     this.rowGroupIndices = deepClone(groupOfIndices);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRowGroup(int... indices) {
/* 1010 */     Preconditions.checkNotNull(indices, "The %1$s must not be null.", new Object[] { "row group indices" });
/* 1011 */     Preconditions.checkArgument((indices.length >= 2), "You must specify at least two indices.");
/* 1012 */     setRowGroups(new int[][] { indices });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addGroupedRow(int rowIndex) {
/* 1023 */     int[][] newRowGroups = getRowGroups();
/*      */     
/* 1025 */     if (newRowGroups.length == 0) {
/* 1026 */       newRowGroups = new int[][] { { rowIndex } };
/*      */     } else {
/* 1028 */       int lastGroupIndex = newRowGroups.length - 1;
/* 1029 */       int[] lastGroup = newRowGroups[lastGroupIndex];
/* 1030 */       int groupSize = lastGroup.length;
/* 1031 */       int[] newLastGroup = new int[groupSize + 1];
/* 1032 */       System.arraycopy(lastGroup, 0, newLastGroup, 0, groupSize);
/* 1033 */       newLastGroup[groupSize] = rowIndex;
/* 1034 */       newRowGroups[lastGroupIndex] = newLastGroup;
/*      */     } 
/* 1036 */     setRowGroupsImpl(newRowGroups, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getHonorsVisibility() {
/* 1055 */     return this.honorsVisibility;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHonorsVisibility(boolean b) {
/* 1091 */     boolean oldHonorsVisibility = getHonorsVisibility();
/* 1092 */     if (oldHonorsVisibility == b) {
/*      */       return;
/*      */     }
/* 1095 */     this.honorsVisibility = b;
/* 1096 */     Set<Component> componentSet = this.constraintMap.keySet();
/* 1097 */     if (componentSet.isEmpty()) {
/*      */       return;
/*      */     }
/* 1100 */     Component firstComponent = componentSet.iterator().next();
/* 1101 */     Container container = firstComponent.getParent();
/* 1102 */     invalidateAndRepaint(container);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHonorsVisibility(Component component, Boolean b) {
/* 1122 */     CellConstraints constraints = getConstraints0(component);
/* 1123 */     if (Objects.equals(b, constraints.honorsVisibility)) {
/*      */       return;
/*      */     }
/* 1126 */     constraints.honorsVisibility = b;
/* 1127 */     invalidateAndRepaint(component.getParent());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLayoutComponent(String name, Component component) {
/* 1143 */     throw new UnsupportedOperationException("Use #addLayoutComponent(Component, Object) instead.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLayoutComponent(Component comp, Object constraints) {
/* 1161 */     Preconditions.checkNotNull(constraints, "The constraints must not be null.");
/* 1162 */     if (constraints instanceof String) {
/* 1163 */       setConstraints(comp, new CellConstraints((String)constraints));
/* 1164 */     } else if (constraints instanceof CellConstraints) {
/* 1165 */       setConstraints(comp, (CellConstraints)constraints);
/*      */     } else {
/* 1167 */       throw new IllegalArgumentException("Illegal constraint type " + constraints.getClass());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeLayoutComponent(Component comp) {
/* 1182 */     removeConstraints(comp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Dimension minimumLayoutSize(Container parent) {
/* 1201 */     return computeLayoutSize(parent, this.minimumWidthMeasure, this.minimumHeightMeasure);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Dimension preferredLayoutSize(Container parent) {
/* 1219 */     return computeLayoutSize(parent, this.preferredWidthMeasure, this.preferredHeightMeasure);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Dimension maximumLayoutSize(Container target) {
/* 1236 */     return new Dimension(2147483647, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getLayoutAlignmentX(Container parent) {
/* 1251 */     return 0.5F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getLayoutAlignmentY(Container parent) {
/* 1266 */     return 0.5F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void invalidateLayout(Container target) {
/* 1278 */     invalidateCaches();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void layoutContainer(Container parent) {
/* 1307 */     synchronized (parent.getTreeLock()) {
/* 1308 */       initializeColAndRowComponentLists();
/* 1309 */       Dimension size = parent.getSize();
/*      */       
/* 1311 */       Insets insets = parent.getInsets();
/* 1312 */       int totalWidth = size.width - insets.left - insets.right;
/* 1313 */       int totalHeight = size.height - insets.top - insets.bottom;
/*      */       
/* 1315 */       int[] x = computeGridOrigins(parent, totalWidth, insets.left, this.colSpecs, (List[])this.colComponents, this.colGroupIndices, this.minimumWidthMeasure, this.preferredWidthMeasure);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1323 */       int[] y = computeGridOrigins(parent, totalHeight, insets.top, this.rowSpecs, (List[])this.rowComponents, this.rowGroupIndices, this.minimumHeightMeasure, this.preferredHeightMeasure);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1332 */       layoutComponents(x, y);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initializeColAndRowComponentLists() {
/* 1347 */     this.colComponents = (List<Component>[])new List[getColumnCount()]; int i;
/* 1348 */     for (i = 0; i < getColumnCount(); i++) {
/* 1349 */       this.colComponents[i] = new ArrayList<Component>();
/*      */     }
/*      */     
/* 1352 */     this.rowComponents = (List<Component>[])new List[getRowCount()];
/* 1353 */     for (i = 0; i < getRowCount(); i++) {
/* 1354 */       this.rowComponents[i] = new ArrayList<Component>();
/*      */     }
/*      */     
/* 1357 */     for (Map.Entry<Component, CellConstraints> element : this.constraintMap.entrySet()) {
/* 1358 */       Map.Entry entry = element;
/* 1359 */       Component component = (Component)entry.getKey();
/* 1360 */       CellConstraints constraints = (CellConstraints)entry.getValue();
/* 1361 */       if (takeIntoAccount(component, constraints)) {
/* 1362 */         if (constraints.gridWidth == 1) {
/* 1363 */           this.colComponents[constraints.gridX - 1].add(component);
/*      */         }
/*      */         
/* 1366 */         if (constraints.gridHeight == 1) {
/* 1367 */           this.rowComponents[constraints.gridY - 1].add(component);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Dimension computeLayoutSize(Container parent, Measure defaultWidthMeasure, Measure defaultHeightMeasure) {
/* 1386 */     synchronized (parent.getTreeLock()) {
/* 1387 */       initializeColAndRowComponentLists();
/* 1388 */       int[] colWidths = maximumSizes(parent, this.colSpecs, (List[])this.colComponents, this.minimumWidthMeasure, this.preferredWidthMeasure, defaultWidthMeasure);
/*      */ 
/*      */ 
/*      */       
/* 1392 */       int[] rowHeights = maximumSizes(parent, this.rowSpecs, (List[])this.rowComponents, this.minimumHeightMeasure, this.preferredHeightMeasure, defaultHeightMeasure);
/*      */ 
/*      */ 
/*      */       
/* 1396 */       int[] groupedWidths = groupedSizes(this.colGroupIndices, colWidths);
/* 1397 */       int[] groupedHeights = groupedSizes(this.rowGroupIndices, rowHeights);
/*      */ 
/*      */       
/* 1400 */       int[] xOrigins = computeOrigins(groupedWidths, 0);
/* 1401 */       int[] yOrigins = computeOrigins(groupedHeights, 0);
/*      */       
/* 1403 */       int width1 = sum(groupedWidths);
/* 1404 */       int height1 = sum(groupedHeights);
/* 1405 */       int maxWidth = width1;
/* 1406 */       int maxHeight = height1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1415 */       int[] maxFixedSizeColsTable = computeMaximumFixedSpanTable(this.colSpecs);
/* 1416 */       int[] maxFixedSizeRowsTable = computeMaximumFixedSpanTable(this.rowSpecs);
/*      */       
/* 1418 */       for (Map.Entry<Component, CellConstraints> element : this.constraintMap.entrySet()) {
/* 1419 */         Map.Entry entry = element;
/* 1420 */         Component component = (Component)entry.getKey();
/* 1421 */         CellConstraints constraints = (CellConstraints)entry.getValue();
/* 1422 */         if (!takeIntoAccount(component, constraints)) {
/*      */           continue;
/*      */         }
/*      */         
/* 1426 */         if (constraints.gridWidth > 1 && constraints.gridWidth > maxFixedSizeColsTable[constraints.gridX - 1]) {
/*      */ 
/*      */           
/* 1429 */           int compWidth = defaultWidthMeasure.sizeOf(component);
/*      */           
/* 1431 */           int gridX1 = constraints.gridX - 1;
/* 1432 */           int gridX2 = gridX1 + constraints.gridWidth;
/* 1433 */           int lead = xOrigins[gridX1];
/* 1434 */           int trail = width1 - xOrigins[gridX2];
/* 1435 */           int myWidth = lead + compWidth + trail;
/* 1436 */           if (myWidth > maxWidth) {
/* 1437 */             maxWidth = myWidth;
/*      */           }
/*      */         } 
/*      */         
/* 1441 */         if (constraints.gridHeight > 1 && constraints.gridHeight > maxFixedSizeRowsTable[constraints.gridY - 1]) {
/*      */ 
/*      */           
/* 1444 */           int compHeight = defaultHeightMeasure.sizeOf(component);
/*      */           
/* 1446 */           int gridY1 = constraints.gridY - 1;
/* 1447 */           int gridY2 = gridY1 + constraints.gridHeight;
/* 1448 */           int lead = yOrigins[gridY1];
/* 1449 */           int trail = height1 - yOrigins[gridY2];
/* 1450 */           int myHeight = lead + compHeight + trail;
/* 1451 */           if (myHeight > maxHeight) {
/* 1452 */             maxHeight = myHeight;
/*      */           }
/*      */         } 
/*      */       } 
/* 1456 */       Insets insets = parent.getInsets();
/* 1457 */       int width = maxWidth + insets.left + insets.right;
/* 1458 */       int height = maxHeight + insets.top + insets.bottom;
/* 1459 */       return new Dimension(width, height);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[] computeGridOrigins(Container container, int totalSize, int offset, List formSpecs, List[] componentLists, int[][] groupIndices, Measure minMeasure, Measure prefMeasure) {
/* 1486 */     int[] minSizes = maximumSizes(container, formSpecs, componentLists, minMeasure, prefMeasure, minMeasure);
/*      */     
/* 1488 */     int[] prefSizes = maximumSizes(container, formSpecs, componentLists, minMeasure, prefMeasure, prefMeasure);
/*      */ 
/*      */     
/* 1491 */     int[] groupedMinSizes = groupedSizes(groupIndices, minSizes);
/* 1492 */     int[] groupedPrefSizes = groupedSizes(groupIndices, prefSizes);
/* 1493 */     int totalMinSize = sum(groupedMinSizes);
/* 1494 */     int totalPrefSize = sum(groupedPrefSizes);
/* 1495 */     int[] compressedSizes = compressedSizes(formSpecs, totalSize, totalMinSize, totalPrefSize, groupedMinSizes, prefSizes);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1501 */     int[] groupedSizes = groupedSizes(groupIndices, compressedSizes);
/* 1502 */     int totalGroupedSize = sum(groupedSizes);
/* 1503 */     int[] sizes = distributedSizes(formSpecs, totalSize, totalGroupedSize, groupedSizes);
/*      */ 
/*      */ 
/*      */     
/* 1507 */     return computeOrigins(sizes, offset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[] computeOrigins(int[] sizes, int offset) {
/* 1519 */     int count = sizes.length;
/* 1520 */     int[] origins = new int[count + 1];
/* 1521 */     origins[0] = offset;
/* 1522 */     for (int i = 1; i <= count; i++) {
/* 1523 */       origins[i] = origins[i - 1] + sizes[i - 1];
/*      */     }
/* 1525 */     return origins;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void layoutComponents(int[] x, int[] y) {
/* 1545 */     Rectangle cellBounds = new Rectangle();
/* 1546 */     for (Map.Entry<Component, CellConstraints> element : this.constraintMap.entrySet()) {
/* 1547 */       Map.Entry entry = element;
/* 1548 */       Component component = (Component)entry.getKey();
/* 1549 */       CellConstraints constraints = (CellConstraints)entry.getValue();
/*      */       
/* 1551 */       int gridX = constraints.gridX - 1;
/* 1552 */       int gridY = constraints.gridY - 1;
/* 1553 */       int gridWidth = constraints.gridWidth;
/* 1554 */       int gridHeight = constraints.gridHeight;
/* 1555 */       cellBounds.x = x[gridX];
/* 1556 */       cellBounds.y = y[gridY];
/* 1557 */       cellBounds.width = x[gridX + gridWidth] - cellBounds.x;
/* 1558 */       cellBounds.height = y[gridY + gridHeight] - cellBounds.y;
/*      */       
/* 1560 */       constraints.setBounds(component, this, cellBounds, this.minimumWidthMeasure, this.minimumHeightMeasure, this.preferredWidthMeasure, this.preferredHeightMeasure);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void invalidateCaches() {
/* 1571 */     this.componentSizeCache.invalidate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[] maximumSizes(Container container, List<FormSpec> formSpecs, List[] componentLists, Measure minMeasure, Measure prefMeasure, Measure defaultMeasure) {
/* 1594 */     int size = formSpecs.size();
/* 1595 */     int[] result = new int[size];
/* 1596 */     for (int i = 0; i < size; i++) {
/* 1597 */       FormSpec formSpec = formSpecs.get(i);
/* 1598 */       result[i] = formSpec.maximumSize(container, componentLists[i], minMeasure, prefMeasure, defaultMeasure);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1604 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[] compressedSizes(List<FormSpec> formSpecs, int totalSize, int totalMinSize, int totalPrefSize, int[] minSizes, int[] prefSizes) {
/* 1630 */     if (totalSize < totalMinSize) {
/* 1631 */       return minSizes;
/*      */     }
/*      */     
/* 1634 */     if (totalSize >= totalPrefSize) {
/* 1635 */       return prefSizes;
/*      */     }
/*      */     
/* 1638 */     int count = formSpecs.size();
/* 1639 */     int[] sizes = new int[count];
/*      */     
/* 1641 */     double totalCompressionSpace = (totalPrefSize - totalSize);
/* 1642 */     double maxCompressionSpace = (totalPrefSize - totalMinSize);
/* 1643 */     double compressionFactor = totalCompressionSpace / maxCompressionSpace;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1649 */     for (int i = 0; i < count; i++) {
/* 1650 */       FormSpec formSpec = formSpecs.get(i);
/* 1651 */       sizes[i] = prefSizes[i];
/* 1652 */       if (formSpec.getSize().compressible()) {
/* 1653 */         sizes[i] = sizes[i] - (int)Math.round((prefSizes[i] - minSizes[i]) * compressionFactor);
/*      */       }
/*      */     } 
/*      */     
/* 1657 */     return sizes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[] groupedSizes(int[][] groups, int[] rawSizes) {
/* 1671 */     if (groups == null || groups.length == 0) {
/* 1672 */       return rawSizes;
/*      */     }
/*      */ 
/*      */     
/* 1676 */     int[] sizes = new int[rawSizes.length];
/* 1677 */     for (int i = 0; i < sizes.length; i++) {
/* 1678 */       sizes[i] = rawSizes[i];
/*      */     }
/*      */ 
/*      */     
/* 1682 */     for (int[] groupIndices : groups) {
/* 1683 */       int groupMaxSize = 0;
/*      */       
/* 1685 */       for (int groupIndice : groupIndices) {
/* 1686 */         int index = groupIndice - 1;
/* 1687 */         groupMaxSize = Math.max(groupMaxSize, sizes[index]);
/*      */       } 
/*      */       
/* 1690 */       for (int groupIndice : groupIndices) {
/* 1691 */         int index = groupIndice - 1;
/* 1692 */         sizes[index] = groupMaxSize;
/*      */       } 
/*      */     } 
/* 1695 */     return sizes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[] distributedSizes(List<FormSpec> formSpecs, int totalSize, int totalPrefSize, int[] inputSizes) {
/* 1712 */     double totalFreeSpace = (totalSize - totalPrefSize);
/*      */     
/* 1714 */     if (totalFreeSpace < 0.0D) {
/* 1715 */       return inputSizes;
/*      */     }
/*      */ 
/*      */     
/* 1719 */     int count = formSpecs.size();
/* 1720 */     double totalWeight = 0.0D;
/* 1721 */     for (int i = 0; i < count; i++) {
/* 1722 */       FormSpec formSpec = formSpecs.get(i);
/* 1723 */       totalWeight += formSpec.getResizeWeight();
/*      */     } 
/*      */ 
/*      */     
/* 1727 */     if (totalWeight == 0.0D) {
/* 1728 */       return inputSizes;
/*      */     }
/*      */     
/* 1731 */     int[] sizes = new int[count];
/*      */     
/* 1733 */     double restSpace = totalFreeSpace;
/* 1734 */     int roundedRestSpace = (int)totalFreeSpace;
/* 1735 */     for (int j = 0; j < count; j++) {
/* 1736 */       FormSpec formSpec = formSpecs.get(j);
/* 1737 */       double weight = formSpec.getResizeWeight();
/* 1738 */       if (weight == 0.0D) {
/* 1739 */         sizes[j] = inputSizes[j];
/*      */       } else {
/* 1741 */         double roundingCorrection = restSpace - roundedRestSpace;
/* 1742 */         double extraSpace = totalFreeSpace * weight / totalWeight;
/* 1743 */         double correctedExtraSpace = extraSpace - roundingCorrection;
/* 1744 */         int roundedExtraSpace = (int)Math.round(correctedExtraSpace);
/* 1745 */         sizes[j] = inputSizes[j] + roundedExtraSpace;
/* 1746 */         restSpace -= extraSpace;
/* 1747 */         roundedRestSpace -= roundedExtraSpace;
/*      */       } 
/*      */     } 
/* 1750 */     return sizes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[] computeMaximumFixedSpanTable(List<FormSpec> formSpecs) {
/* 1779 */     int size = formSpecs.size();
/* 1780 */     int[] table = new int[size];
/* 1781 */     int maximumFixedSpan = Integer.MAX_VALUE;
/* 1782 */     for (int i = size - 1; i >= 0; i--) {
/* 1783 */       FormSpec spec = formSpecs.get(i);
/* 1784 */       if (spec.canGrow()) {
/* 1785 */         maximumFixedSpan = 0;
/*      */       }
/* 1787 */       table[i] = maximumFixedSpan;
/* 1788 */       if (maximumFixedSpan < Integer.MAX_VALUE) {
/* 1789 */         maximumFixedSpan++;
/*      */       }
/*      */     } 
/* 1792 */     return table;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int sum(int[] sizes) {
/* 1805 */     int sum = 0;
/* 1806 */     for (int i = sizes.length - 1; i >= 0; i--) {
/* 1807 */       sum += sizes[i];
/*      */     }
/* 1809 */     return sum;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void invalidateAndRepaint(Container container) {
/* 1814 */     if (container == null) {
/*      */       return;
/*      */     }
/* 1817 */     if (container instanceof JComponent) {
/* 1818 */       ((JComponent)container).revalidate();
/*      */     } else {
/* 1820 */       container.invalidate();
/*      */     } 
/* 1822 */     container.repaint();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean takeIntoAccount(Component component, CellConstraints cc) {
/* 1840 */     return (component.isVisible() || (cc.honorsVisibility == null && !getHonorsVisibility()) || Boolean.FALSE.equals(cc.honorsVisibility));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface Measure
/*      */   {
/*      */     int sizeOf(Component param1Component);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class CachingMeasure
/*      */     implements Measure, Serializable
/*      */   {
/*      */     protected final FormLayout.ComponentSizeCache cache;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private CachingMeasure(FormLayout.ComponentSizeCache cache) {
/* 1880 */       this.cache = cache;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class MinimumWidthMeasure
/*      */     extends CachingMeasure
/*      */   {
/*      */     private MinimumWidthMeasure(FormLayout.ComponentSizeCache cache) {
/* 1891 */       super(cache);
/*      */     }
/*      */     
/*      */     public int sizeOf(Component c) {
/* 1895 */       return (this.cache.getMinimumSize(c)).width;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class MinimumHeightMeasure
/*      */     extends CachingMeasure
/*      */   {
/*      */     private MinimumHeightMeasure(FormLayout.ComponentSizeCache cache) {
/* 1905 */       super(cache);
/*      */     }
/*      */     
/*      */     public int sizeOf(Component c) {
/* 1909 */       return (this.cache.getMinimumSize(c)).height;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class PreferredWidthMeasure
/*      */     extends CachingMeasure
/*      */   {
/*      */     private PreferredWidthMeasure(FormLayout.ComponentSizeCache cache) {
/* 1919 */       super(cache);
/*      */     }
/*      */     
/*      */     public int sizeOf(Component c) {
/* 1923 */       return (this.cache.getPreferredSize(c)).width;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class PreferredHeightMeasure
/*      */     extends CachingMeasure
/*      */   {
/*      */     private PreferredHeightMeasure(FormLayout.ComponentSizeCache cache) {
/* 1933 */       super(cache);
/*      */     }
/*      */     
/*      */     public int sizeOf(Component c) {
/* 1937 */       return (this.cache.getPreferredSize(c)).height;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class ComponentSizeCache
/*      */     implements Serializable
/*      */   {
/*      */     private final Map<Component, Dimension> minimumSizes;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final Map<Component, Dimension> preferredSizes;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ComponentSizeCache(int initialCapacity) {
/* 1962 */       this.minimumSizes = new HashMap<Component, Dimension>(initialCapacity);
/* 1963 */       this.preferredSizes = new HashMap<Component, Dimension>(initialCapacity);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void invalidate() {
/* 1970 */       this.minimumSizes.clear();
/* 1971 */       this.preferredSizes.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Dimension getMinimumSize(Component component) {
/* 1983 */       Dimension size = this.minimumSizes.get(component);
/* 1984 */       if (size == null) {
/* 1985 */         size = component.getMinimumSize();
/* 1986 */         this.minimumSizes.put(component, size);
/*      */       } 
/* 1988 */       return size;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Dimension getPreferredSize(Component component) {
/* 2000 */       Dimension size = this.preferredSizes.get(component);
/* 2001 */       if (size == null) {
/* 2002 */         size = component.getPreferredSize();
/* 2003 */         this.preferredSizes.put(component, size);
/*      */       } 
/* 2005 */       return size;
/*      */     }
/*      */     
/*      */     void removeEntry(Component component) {
/* 2009 */       this.minimumSizes.remove(component);
/* 2010 */       this.preferredSizes.remove(component);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LayoutInfo getLayoutInfo(Container parent) {
/* 2031 */     synchronized (parent.getTreeLock()) {
/* 2032 */       initializeColAndRowComponentLists();
/* 2033 */       Dimension size = parent.getSize();
/*      */       
/* 2035 */       Insets insets = parent.getInsets();
/* 2036 */       int totalWidth = size.width - insets.left - insets.right;
/* 2037 */       int totalHeight = size.height - insets.top - insets.bottom;
/*      */       
/* 2039 */       int[] x = computeGridOrigins(parent, totalWidth, insets.left, this.colSpecs, (List[])this.colComponents, this.colGroupIndices, this.minimumWidthMeasure, this.preferredWidthMeasure);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2047 */       int[] y = computeGridOrigins(parent, totalHeight, insets.top, this.rowSpecs, (List[])this.rowComponents, this.rowGroupIndices, this.minimumHeightMeasure, this.preferredHeightMeasure);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2055 */       return new LayoutInfo(x, y);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class LayoutInfo
/*      */   {
/*      */     public final int[] columnOrigins;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int[] rowOrigins;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private LayoutInfo(int[] xOrigins, int[] yOrigins) {
/* 2076 */       this.columnOrigins = xOrigins;
/* 2077 */       this.rowOrigins = yOrigins;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getX() {
/* 2086 */       return this.columnOrigins[0];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getY() {
/* 2095 */       return this.rowOrigins[0];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getWidth() {
/* 2105 */       return this.columnOrigins[this.columnOrigins.length - 1] - this.columnOrigins[0];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getHeight() {
/* 2114 */       return this.rowOrigins[this.rowOrigins.length - 1] - this.rowOrigins[0];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[][] deepClone(int[][] array) {
/* 2133 */     int[][] result = new int[array.length][];
/* 2134 */     for (int i = 0; i < result.length; i++) {
/* 2135 */       result[i] = (int[])array[i].clone();
/*      */     }
/* 2137 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 2152 */     invalidateCaches();
/* 2153 */     out.defaultWriteObject();
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\layout\FormLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */