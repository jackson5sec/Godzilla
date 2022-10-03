/*     */ package com.intellij.uiDesigner.core;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.util.Arrays;
/*     */ import javax.swing.JComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GridLayoutManager
/*     */   extends AbstractLayout
/*     */ {
/*  26 */   private int myMinCellSize = 20;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int[] myRowStretches;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int[] myColumnStretches;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int[] myYs;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int[] myHeights;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int[] myXs;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int[] myWidths;
/*     */ 
/*     */ 
/*     */   
/*     */   private LayoutState myLayoutState;
/*     */ 
/*     */ 
/*     */   
/*     */   DimensionInfo myHorizontalInfo;
/*     */ 
/*     */ 
/*     */   
/*     */   DimensionInfo myVerticalInfo;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mySameSizeHorizontally;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mySameSizeVertically;
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final Object DESIGN_TIME_INSETS = new Object();
/*     */   
/*     */   private static final int SKIP_ROW = 1;
/*     */   private static final int SKIP_COL = 2;
/*     */   
/*     */   public GridLayoutManager(int rowCount, int columnCount) {
/*  81 */     if (columnCount < 1) {
/*  82 */       throw new IllegalArgumentException("wrong columnCount: " + columnCount);
/*     */     }
/*  84 */     if (rowCount < 1) {
/*  85 */       throw new IllegalArgumentException("wrong rowCount: " + rowCount);
/*     */     }
/*     */     
/*  88 */     this.myRowStretches = new int[rowCount]; int i;
/*  89 */     for (i = 0; i < rowCount; i++) {
/*  90 */       this.myRowStretches[i] = 1;
/*     */     }
/*  92 */     this.myColumnStretches = new int[columnCount];
/*  93 */     for (i = 0; i < columnCount; i++) {
/*  94 */       this.myColumnStretches[i] = 1;
/*     */     }
/*     */     
/*  97 */     this.myXs = new int[columnCount];
/*  98 */     this.myWidths = new int[columnCount];
/*     */     
/* 100 */     this.myYs = new int[rowCount];
/* 101 */     this.myHeights = new int[rowCount];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridLayoutManager(int rowCount, int columnCount, Insets margin, int hGap, int vGap) {
/* 108 */     this(rowCount, columnCount);
/* 109 */     setMargin(margin);
/* 110 */     setHGap(hGap);
/* 111 */     setVGap(vGap);
/* 112 */     this.myMinCellSize = 0;
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
/*     */   public GridLayoutManager(int rowCount, int columnCount, Insets margin, int hGap, int vGap, boolean sameSizeHorizontally, boolean sameSizeVertically) {
/* 127 */     this(rowCount, columnCount, margin, hGap, vGap);
/* 128 */     this.mySameSizeHorizontally = sameSizeHorizontally;
/* 129 */     this.mySameSizeVertically = sameSizeVertically;
/*     */   }
/*     */   
/*     */   public void addLayoutComponent(Component comp, Object constraints) {
/* 133 */     GridConstraints c = (GridConstraints)constraints;
/* 134 */     int row = c.getRow();
/* 135 */     int rowSpan = c.getRowSpan();
/* 136 */     int rowCount = getRowCount();
/* 137 */     if (row < 0 || row >= rowCount) {
/* 138 */       throw new IllegalArgumentException("wrong row: " + row);
/*     */     }
/* 140 */     if (row + rowSpan - 1 >= rowCount) {
/* 141 */       throw new IllegalArgumentException("wrong row span: " + rowSpan + "; row=" + row + " rowCount=" + rowCount);
/*     */     }
/* 143 */     int column = c.getColumn();
/* 144 */     int colSpan = c.getColSpan();
/* 145 */     int columnCount = getColumnCount();
/* 146 */     if (column < 0 || column >= columnCount) {
/* 147 */       throw new IllegalArgumentException("wrong column: " + column);
/*     */     }
/* 149 */     if (column + colSpan - 1 >= columnCount) {
/* 150 */       throw new IllegalArgumentException("wrong col span: " + colSpan + "; column=" + column + " columnCount=" + columnCount);
/*     */     }
/*     */     
/* 153 */     super.addLayoutComponent(comp, constraints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRowCount() {
/* 160 */     return this.myRowStretches.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnCount() {
/* 167 */     return this.myColumnStretches.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRowStretch(int rowIndex) {
/* 175 */     return this.myRowStretches[rowIndex];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRowStretch(int rowIndex, int stretch) {
/* 183 */     if (stretch < 1) {
/* 184 */       throw new IllegalArgumentException("wrong stretch: " + stretch);
/*     */     }
/* 186 */     this.myRowStretches[rowIndex] = stretch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnStretch(int columnIndex) {
/* 194 */     return this.myColumnStretches[columnIndex];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColumnStretch(int columnIndex, int stretch) {
/* 202 */     if (stretch < 1) {
/* 203 */       throw new IllegalArgumentException("wrong stretch: " + stretch);
/*     */     }
/* 205 */     this.myColumnStretches[columnIndex] = stretch;
/*     */   }
/*     */   
/*     */   public Dimension maximumLayoutSize(Container target) {
/* 209 */     return new Dimension(2147483647, 2147483647);
/*     */   }
/*     */   
/*     */   public Dimension minimumLayoutSize(Container container) {
/* 213 */     validateInfos(container);
/*     */ 
/*     */     
/* 216 */     DimensionInfo horizontalInfo = this.myHorizontalInfo;
/* 217 */     DimensionInfo verticalInfo = this.myVerticalInfo;
/*     */     
/* 219 */     Dimension result = getTotalGap(container, horizontalInfo, verticalInfo);
/*     */     
/* 221 */     int[] widths = getMinSizes(horizontalInfo);
/* 222 */     if (this.mySameSizeHorizontally) {
/* 223 */       makeSameSizes(widths);
/*     */     }
/* 225 */     result.width += sum(widths);
/*     */     
/* 227 */     int[] heights = getMinSizes(verticalInfo);
/* 228 */     if (this.mySameSizeVertically) {
/* 229 */       makeSameSizes(heights);
/*     */     }
/* 231 */     result.height += sum(heights);
/*     */     
/* 233 */     return result;
/*     */   }
/*     */   
/*     */   private static void makeSameSizes(int[] widths) {
/* 237 */     int max = widths[0];
/* 238 */     for (int width : widths) {
/* 239 */       max = Math.max(width, max);
/*     */     }
/*     */     
/* 242 */     Arrays.fill(widths, max);
/*     */   }
/*     */   
/*     */   private static int[] getSameSizes(DimensionInfo info, int totalWidth) {
/* 246 */     int[] widths = new int[info.getCellCount()];
/*     */     
/* 248 */     int average = totalWidth / widths.length;
/* 249 */     int rest = totalWidth % widths.length;
/*     */     
/* 251 */     for (int i = 0; i < widths.length; i++) {
/* 252 */       widths[i] = average;
/* 253 */       if (rest > 0) {
/* 254 */         widths[i] = widths[i] + 1;
/* 255 */         rest--;
/*     */       } 
/*     */     } 
/*     */     
/* 259 */     return widths;
/*     */   }
/*     */   
/*     */   public Dimension preferredLayoutSize(Container container) {
/* 263 */     validateInfos(container);
/*     */ 
/*     */     
/* 266 */     DimensionInfo horizontalInfo = this.myHorizontalInfo;
/* 267 */     DimensionInfo verticalInfo = this.myVerticalInfo;
/*     */     
/* 269 */     Dimension result = getTotalGap(container, horizontalInfo, verticalInfo);
/*     */     
/* 271 */     int[] widths = getPrefSizes(horizontalInfo);
/* 272 */     if (this.mySameSizeHorizontally) {
/* 273 */       makeSameSizes(widths);
/*     */     }
/* 275 */     result.width += sum(widths);
/*     */     
/* 277 */     int[] heights = getPrefSizes(verticalInfo);
/* 278 */     if (this.mySameSizeVertically) {
/* 279 */       makeSameSizes(heights);
/*     */     }
/* 281 */     result.height += sum(heights);
/*     */     
/* 283 */     return result;
/*     */   }
/*     */   
/*     */   private static int sum(int[] ints) {
/* 287 */     int result = 0;
/* 288 */     for (int i = ints.length - 1; i >= 0; i--) {
/* 289 */       result += ints[i];
/*     */     }
/* 291 */     return result;
/*     */   }
/*     */   
/*     */   private Dimension getTotalGap(Container container, DimensionInfo hInfo, DimensionInfo vInfo) {
/* 295 */     Insets insets = getInsets(container);
/* 296 */     return new Dimension(insets.left + insets.right + 
/* 297 */         countGap(hInfo, 0, hInfo.getCellCount()) + this.myMargin.left + this.myMargin.right, insets.top + insets.bottom + 
/* 298 */         countGap(vInfo, 0, vInfo.getCellCount()) + this.myMargin.top + this.myMargin.bottom);
/*     */   }
/*     */   
/*     */   private static int getDesignTimeInsets(Container container) {
/* 302 */     while (container != null) {
/* 303 */       if (container instanceof JComponent) {
/* 304 */         Integer designTimeInsets = (Integer)((JComponent)container).getClientProperty(DESIGN_TIME_INSETS);
/* 305 */         if (designTimeInsets != null) {
/* 306 */           return designTimeInsets.intValue();
/*     */         }
/*     */       } 
/* 309 */       container = container.getParent();
/*     */     } 
/* 311 */     return 0;
/*     */   }
/*     */   
/*     */   private static Insets getInsets(Container container) {
/* 315 */     Insets insets = container.getInsets();
/* 316 */     int insetsValue = getDesignTimeInsets(container);
/* 317 */     if (insetsValue != 0) {
/* 318 */       return new Insets(insets.top + insetsValue, insets.left + insetsValue, insets.bottom + insetsValue, insets.right + insetsValue);
/*     */     }
/*     */     
/* 321 */     return insets;
/*     */   }
/*     */   
/*     */   private static int countGap(DimensionInfo info, int startCell, int cellCount) {
/* 325 */     int counter = 0;
/* 326 */     int cellIndex = startCell + cellCount - 2;
/* 327 */     for (; cellIndex >= startCell; 
/* 328 */       cellIndex--) {
/* 329 */       if (shouldAddGapAfterCell(info, cellIndex)) {
/* 330 */         counter++;
/*     */       }
/*     */     } 
/* 333 */     return counter * info.getGap();
/*     */   }
/*     */   
/*     */   private static boolean shouldAddGapAfterCell(DimensionInfo info, int cellIndex) {
/* 337 */     if (cellIndex < 0 || cellIndex >= info.getCellCount()) {
/* 338 */       throw new IllegalArgumentException("wrong cellIndex: " + cellIndex + "; cellCount=" + info.getCellCount());
/*     */     }
/*     */     
/* 341 */     boolean endsInThis = false;
/* 342 */     boolean startsInNext = false;
/*     */     
/* 344 */     int indexOfNextNotEmpty = -1; int i;
/* 345 */     for (i = cellIndex + 1; i < info.getCellCount(); i++) {
/* 346 */       if (!isCellEmpty(info, i)) {
/* 347 */         indexOfNextNotEmpty = i;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 352 */     for (i = 0; i < info.getComponentCount(); i++) {
/* 353 */       Component component = info.getComponent(i);
/* 354 */       if (!(component instanceof Spacer)) {
/*     */ 
/*     */ 
/*     */         
/* 358 */         if (info.componentBelongsCell(i, cellIndex) && 
/* 359 */           DimensionInfo.findAlignedChild(component, info.getConstraints(i)) != null) {
/* 360 */           return true;
/*     */         }
/*     */         
/* 363 */         if (info.getCell(i) == indexOfNextNotEmpty) {
/* 364 */           startsInNext = true;
/*     */         }
/*     */         
/* 367 */         if (info.getCell(i) + info.getSpan(i) - 1 == cellIndex) {
/* 368 */           endsInThis = true;
/*     */         }
/*     */       } 
/*     */     } 
/* 372 */     return (startsInNext && endsInThis);
/*     */   }
/*     */   
/*     */   private static boolean isCellEmpty(DimensionInfo info, int cellIndex) {
/* 376 */     if (cellIndex < 0 || cellIndex >= info.getCellCount()) {
/* 377 */       throw new IllegalArgumentException("wrong cellIndex: " + cellIndex + "; cellCount=" + info.getCellCount());
/*     */     }
/* 379 */     for (int i = 0; i < info.getComponentCount(); i++) {
/* 380 */       Component component = info.getComponent(i);
/* 381 */       if (info.getCell(i) == cellIndex && !(component instanceof Spacer)) {
/* 382 */         return false;
/*     */       }
/*     */     } 
/* 385 */     return true;
/*     */   }
/*     */   
/*     */   public void layoutContainer(Container container) {
/* 389 */     validateInfos(container);
/*     */ 
/*     */     
/* 392 */     LayoutState layoutState = this.myLayoutState;
/* 393 */     DimensionInfo horizontalInfo = this.myHorizontalInfo;
/* 394 */     DimensionInfo verticalInfo = this.myVerticalInfo;
/*     */     
/* 396 */     Insets insets = getInsets(container);
/*     */     
/* 398 */     int skipLayout = checkSetSizesFromParent(container, insets);
/*     */     
/* 400 */     Dimension gap = getTotalGap(container, horizontalInfo, verticalInfo);
/*     */     
/* 402 */     Dimension size = container.getSize();
/* 403 */     size.width -= gap.width;
/* 404 */     size.height -= gap.height;
/*     */     
/* 406 */     Dimension prefSize = preferredLayoutSize(container);
/* 407 */     prefSize.width -= gap.width;
/* 408 */     prefSize.height -= gap.height;
/*     */     
/* 410 */     Dimension minSize = minimumLayoutSize(container);
/* 411 */     minSize.width -= gap.width;
/* 412 */     minSize.height -= gap.height;
/*     */ 
/*     */     
/* 415 */     if ((skipLayout & 0x1) == 0) {
/*     */       int[] heights;
/* 417 */       if (this.mySameSizeVertically) {
/* 418 */         heights = getSameSizes(verticalInfo, Math.max(size.height, minSize.height));
/*     */       
/*     */       }
/* 421 */       else if (size.height < prefSize.height) {
/* 422 */         heights = getMinSizes(verticalInfo);
/* 423 */         new_doIt(heights, 0, verticalInfo.getCellCount(), size.height, verticalInfo, true);
/*     */       } else {
/*     */         
/* 426 */         heights = getPrefSizes(verticalInfo);
/* 427 */         new_doIt(heights, 0, verticalInfo.getCellCount(), size.height, verticalInfo, false);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 432 */       int y = insets.top + this.myMargin.top;
/* 433 */       for (int j = 0; j < heights.length; j++) {
/* 434 */         this.myYs[j] = y;
/* 435 */         this.myHeights[j] = heights[j];
/* 436 */         y += heights[j];
/* 437 */         if (shouldAddGapAfterCell(verticalInfo, j)) {
/* 438 */           y += verticalInfo.getGap();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 443 */     if ((skipLayout & 0x2) == 0) {
/*     */       int[] widths;
/*     */       
/* 446 */       if (this.mySameSizeHorizontally) {
/* 447 */         widths = getSameSizes(horizontalInfo, Math.max(size.width, minSize.width));
/*     */       
/*     */       }
/* 450 */       else if (size.width < prefSize.width) {
/* 451 */         widths = getMinSizes(horizontalInfo);
/* 452 */         new_doIt(widths, 0, horizontalInfo.getCellCount(), size.width, horizontalInfo, true);
/*     */       } else {
/*     */         
/* 455 */         widths = getPrefSizes(horizontalInfo);
/* 456 */         new_doIt(widths, 0, horizontalInfo.getCellCount(), size.width, horizontalInfo, false);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 461 */       int x = insets.left + this.myMargin.left;
/* 462 */       for (int j = 0; j < widths.length; j++) {
/* 463 */         this.myXs[j] = x;
/* 464 */         this.myWidths[j] = widths[j];
/* 465 */         x += widths[j];
/* 466 */         if (shouldAddGapAfterCell(horizontalInfo, j)) {
/* 467 */           x += horizontalInfo.getGap();
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 473 */     for (int i = 0; i < layoutState.getComponentCount(); i++) {
/* 474 */       GridConstraints c = layoutState.getConstraints(i);
/* 475 */       Component component = layoutState.getComponent(i);
/*     */       
/* 477 */       int column = horizontalInfo.getCell(i);
/* 478 */       int colSpan = horizontalInfo.getSpan(i);
/* 479 */       int row = verticalInfo.getCell(i);
/* 480 */       int rowSpan = verticalInfo.getSpan(i);
/*     */       
/* 482 */       int cellWidth = this.myXs[column + colSpan - 1] + this.myWidths[column + colSpan - 1] - this.myXs[column];
/* 483 */       int cellHeight = this.myYs[row + rowSpan - 1] + this.myHeights[row + rowSpan - 1] - this.myYs[row];
/*     */       
/* 485 */       Dimension componentSize = new Dimension(cellWidth, cellHeight);
/*     */       
/* 487 */       if ((c.getFill() & 0x1) == 0) {
/* 488 */         componentSize.width = Math.min(componentSize.width, horizontalInfo.getPreferredWidth(i));
/*     */       }
/*     */       
/* 491 */       if ((c.getFill() & 0x2) == 0) {
/* 492 */         componentSize.height = Math.min(componentSize.height, verticalInfo.getPreferredWidth(i));
/*     */       }
/*     */       
/* 495 */       Util.adjustSize(component, c, componentSize);
/*     */       
/* 497 */       int dx = 0;
/* 498 */       int dy = 0;
/*     */       
/* 500 */       if ((c.getAnchor() & 0x4) != 0) {
/* 501 */         dx = cellWidth - componentSize.width;
/*     */       }
/* 503 */       else if ((c.getAnchor() & 0x8) == 0) {
/* 504 */         dx = (cellWidth - componentSize.width) / 2;
/*     */       } 
/*     */       
/* 507 */       if ((c.getAnchor() & 0x2) != 0) {
/* 508 */         dy = cellHeight - componentSize.height;
/*     */       }
/* 510 */       else if ((c.getAnchor() & 0x1) == 0) {
/* 511 */         dy = (cellHeight - componentSize.height) / 2;
/*     */       } 
/*     */       
/* 514 */       int indent = 10 * c.getIndent();
/* 515 */       componentSize.width -= indent;
/* 516 */       dx += indent;
/*     */       
/* 518 */       component.setBounds(this.myXs[column] + dx, this.myYs[row] + dy, componentSize.width, componentSize.height);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int checkSetSizesFromParent(Container container, Insets insets) {
/* 523 */     int skipLayout = 0;
/*     */     
/* 525 */     GridLayoutManager parentGridLayout = null;
/* 526 */     GridConstraints parentGridConstraints = null;
/*     */ 
/*     */     
/* 529 */     Container parent = container.getParent();
/* 530 */     if (parent != null) {
/* 531 */       if (parent.getLayout() instanceof GridLayoutManager) {
/* 532 */         parentGridLayout = (GridLayoutManager)parent.getLayout();
/* 533 */         parentGridConstraints = parentGridLayout.getConstraintsForComponent(container);
/*     */       } else {
/*     */         
/* 536 */         Container parent2 = parent.getParent();
/* 537 */         if (parent2 != null && parent2.getLayout() instanceof GridLayoutManager) {
/* 538 */           parentGridLayout = (GridLayoutManager)parent2.getLayout();
/* 539 */           parentGridConstraints = parentGridLayout.getConstraintsForComponent(parent);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 544 */     if (parentGridLayout != null && parentGridConstraints.isUseParentLayout()) {
/* 545 */       if (this.myRowStretches.length == parentGridConstraints.getRowSpan()) {
/* 546 */         int row = parentGridConstraints.getRow();
/* 547 */         this.myYs[0] = insets.top + this.myMargin.top;
/* 548 */         this.myHeights[0] = parentGridLayout.myHeights[row] - this.myYs[0];
/* 549 */         for (int i = 1; i < this.myRowStretches.length; i++) {
/* 550 */           this.myYs[i] = parentGridLayout.myYs[i + row] - parentGridLayout.myYs[row];
/* 551 */           this.myHeights[i] = parentGridLayout.myHeights[i + row];
/*     */         } 
/* 553 */         this.myHeights[this.myRowStretches.length - 1] = this.myHeights[this.myRowStretches.length - 1] - insets.bottom + this.myMargin.bottom;
/* 554 */         skipLayout |= 0x1;
/*     */       } 
/* 556 */       if (this.myColumnStretches.length == parentGridConstraints.getColSpan()) {
/* 557 */         int col = parentGridConstraints.getColumn();
/* 558 */         this.myXs[0] = insets.left + this.myMargin.left;
/* 559 */         this.myWidths[0] = parentGridLayout.myWidths[col] - this.myXs[0];
/* 560 */         for (int i = 1; i < this.myColumnStretches.length; i++) {
/* 561 */           this.myXs[i] = parentGridLayout.myXs[i + col] - parentGridLayout.myXs[col];
/* 562 */           this.myWidths[i] = parentGridLayout.myWidths[i + col];
/*     */         } 
/* 564 */         this.myWidths[this.myColumnStretches.length - 1] = this.myWidths[this.myColumnStretches.length - 1] - insets.right + this.myMargin.right;
/* 565 */         skipLayout |= 0x2;
/*     */       } 
/*     */     } 
/* 568 */     return skipLayout;
/*     */   }
/*     */   
/*     */   public void invalidateLayout(Container container) {
/* 572 */     this.myLayoutState = null;
/* 573 */     this.myHorizontalInfo = null;
/* 574 */     this.myVerticalInfo = null;
/*     */   }
/*     */   
/*     */   void validateInfos(Container container) {
/* 578 */     if (this.myLayoutState == null) {
/*     */       
/* 580 */       this.myLayoutState = new LayoutState(this, (getDesignTimeInsets(container) == 0));
/* 581 */       this.myHorizontalInfo = new HorizontalInfo(this.myLayoutState, getHGapImpl(container));
/* 582 */       this.myVerticalInfo = new VerticalInfo(this.myLayoutState, getVGapImpl(container));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getXs() {
/* 590 */     return this.myXs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getWidths() {
/* 597 */     return this.myWidths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getYs() {
/* 604 */     return this.myYs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getHeights() {
/* 611 */     return this.myHeights;
/*     */   }
/*     */   
/*     */   public int[] getCoords(boolean isRow) {
/* 615 */     return isRow ? this.myYs : this.myXs;
/*     */   }
/*     */   
/*     */   public int[] getSizes(boolean isRow) {
/* 619 */     return isRow ? this.myHeights : this.myWidths;
/*     */   }
/*     */   
/*     */   private int[] getMinSizes(DimensionInfo info) {
/* 623 */     return getMinOrPrefSizes(info, true);
/*     */   }
/*     */   
/*     */   private int[] getPrefSizes(DimensionInfo info) {
/* 627 */     return getMinOrPrefSizes(info, false);
/*     */   }
/*     */   
/*     */   private int[] getMinOrPrefSizes(DimensionInfo info, boolean min) {
/* 631 */     int[] widths = new int[info.getCellCount()];
/* 632 */     Arrays.fill(widths, this.myMinCellSize);
/*     */ 
/*     */     
/* 635 */     for (int i = info.getComponentCount() - 1; i >= 0; i--) {
/* 636 */       if (info.getSpan(i) == 1) {
/*     */ 
/*     */ 
/*     */         
/* 640 */         int size = min ? getMin2(info, i) : Math.max(info.getMinimumWidth(i), info.getPreferredWidth(i));
/* 641 */         int cell = info.getCell(i);
/* 642 */         int gap = countGap(info, cell, info.getSpan(i));
/* 643 */         size = Math.max(size - gap, 0);
/*     */         
/* 645 */         widths[cell] = Math.max(widths[cell], size);
/*     */       } 
/*     */     } 
/*     */     
/* 649 */     updateSizesFromChildren(info, min, widths);
/*     */ 
/*     */ 
/*     */     
/* 653 */     boolean[] toProcess = new boolean[info.getCellCount()];
/*     */     
/* 655 */     for (int j = info.getComponentCount() - 1; j >= 0; j--) {
/* 656 */       int size = min ? getMin2(info, j) : Math.max(info.getMinimumWidth(j), info.getPreferredWidth(j));
/*     */       
/* 658 */       int span = info.getSpan(j);
/* 659 */       int cell = info.getCell(j);
/*     */       
/* 661 */       int gap = countGap(info, cell, span);
/* 662 */       size = Math.max(size - gap, 0);
/*     */       
/* 664 */       Arrays.fill(toProcess, false);
/*     */       
/* 666 */       int curSize = 0;
/* 667 */       for (int k = 0; k < span; k++) {
/* 668 */         curSize += widths[k + cell];
/* 669 */         toProcess[k + cell] = true;
/*     */       } 
/*     */       
/* 672 */       if (curSize < size) {
/*     */ 
/*     */ 
/*     */         
/* 676 */         boolean[] higherPriorityCells = new boolean[toProcess.length];
/* 677 */         getCellsWithHigherPriorities(info, toProcess, higherPriorityCells, false, widths);
/*     */         
/* 679 */         distribute(higherPriorityCells, info, size - curSize, widths);
/*     */       } 
/*     */     } 
/* 682 */     return widths;
/*     */   }
/*     */   
/*     */   private static void updateSizesFromChildren(DimensionInfo info, boolean min, int[] widths) {
/* 686 */     for (int i = info.getComponentCount() - 1; i >= 0; i--) {
/* 687 */       Component child = info.getComponent(i);
/* 688 */       GridConstraints c = info.getConstraints(i);
/* 689 */       if (c.isUseParentLayout() && child instanceof Container) {
/* 690 */         Container container = (Container)child;
/* 691 */         if (container.getLayout() instanceof GridLayoutManager) {
/* 692 */           updateSizesFromChild(info, min, widths, container, i);
/*     */         }
/* 694 */         else if (container.getComponentCount() == 1 && container.getComponent(0) instanceof Container) {
/*     */ 
/*     */           
/* 697 */           Container childContainer = (Container)container.getComponent(0);
/* 698 */           if (childContainer.getLayout() instanceof GridLayoutManager) {
/* 699 */             updateSizesFromChild(info, min, widths, childContainer, i);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void updateSizesFromChild(DimensionInfo info, boolean min, int[] widths, Container container, int childIndex) {
/* 711 */     GridLayoutManager childLayout = (GridLayoutManager)container.getLayout();
/* 712 */     if (info.getSpan(childIndex) == info.getChildLayoutCellCount(childLayout)) {
/* 713 */       childLayout.validateInfos(container);
/*     */ 
/*     */       
/* 716 */       DimensionInfo childInfo = (info instanceof HorizontalInfo) ? childLayout.myHorizontalInfo : childLayout.myVerticalInfo;
/* 717 */       int[] sizes = childLayout.getMinOrPrefSizes(childInfo, min);
/* 718 */       int cell = info.getCell(childIndex);
/* 719 */       for (int j = 0; j < sizes.length; j++) {
/* 720 */         widths[cell + j] = Math.max(widths[cell + j], sizes[j]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getMin2(DimensionInfo info, int componentIndex) {
/*     */     int s;
/* 728 */     if ((info.getSizePolicy(componentIndex) & 0x1) != 0) {
/* 729 */       s = info.getMinimumWidth(componentIndex);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 734 */       s = Math.max(info.getMinimumWidth(componentIndex), info.getPreferredWidth(componentIndex));
/*     */     } 
/* 736 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void new_doIt(int[] widths, int cell, int span, int minWidth, DimensionInfo info, boolean checkPrefs) {
/* 743 */     int toDistribute = minWidth;
/*     */     
/* 745 */     for (int i = cell; i < cell + span; i++) {
/* 746 */       toDistribute -= widths[i];
/*     */     }
/* 748 */     if (toDistribute <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 752 */     boolean[] allowedCells = new boolean[info.getCellCount()];
/* 753 */     for (int j = cell; j < cell + span; j++) {
/* 754 */       allowedCells[j] = true;
/*     */     }
/*     */     
/* 757 */     boolean[] higherPriorityCells = new boolean[info.getCellCount()];
/* 758 */     getCellsWithHigherPriorities(info, allowedCells, higherPriorityCells, checkPrefs, widths);
/*     */     
/* 760 */     distribute(higherPriorityCells, info, toDistribute, widths);
/*     */   }
/*     */   
/*     */   private static void distribute(boolean[] higherPriorityCells, DimensionInfo info, int toDistribute, int[] widths) {
/* 764 */     int stretches = 0;
/* 765 */     for (int i = 0; i < info.getCellCount(); i++) {
/* 766 */       if (higherPriorityCells[i]) {
/* 767 */         stretches += info.getStretch(i);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 772 */     int toDistributeFrozen = toDistribute;
/* 773 */     for (int j = 0; j < info.getCellCount(); j++) {
/* 774 */       if (higherPriorityCells[j]) {
/*     */ 
/*     */ 
/*     */         
/* 778 */         int addon = toDistributeFrozen * info.getStretch(j) / stretches;
/* 779 */         widths[j] = widths[j] + addon;
/*     */         
/* 781 */         toDistribute -= addon;
/*     */       } 
/*     */     } 
/*     */     
/* 785 */     if (toDistribute != 0) {
/* 786 */       for (int k = 0; k < info.getCellCount(); k++) {
/* 787 */         if (higherPriorityCells[k]) {
/*     */ 
/*     */ 
/*     */           
/* 791 */           widths[k] = widths[k] + 1;
/* 792 */           toDistribute--;
/*     */           
/* 794 */           if (toDistribute == 0) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 800 */     if (toDistribute != 0) {
/* 801 */       throw new IllegalStateException("toDistribute = " + toDistribute);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getCellsWithHigherPriorities(DimensionInfo info, boolean[] allowedCells, boolean[] higherPriorityCells, boolean checkPrefs, int[] widths) {
/* 812 */     Arrays.fill(higherPriorityCells, false);
/*     */     
/* 814 */     int foundCells = 0;
/*     */     
/* 816 */     if (checkPrefs) {
/*     */       
/* 818 */       int[] prefs = getMinOrPrefSizes(info, false);
/* 819 */       for (int i = 0; i < allowedCells.length; i++) {
/* 820 */         if (allowedCells[i])
/*     */         {
/*     */           
/* 823 */           if (!isCellEmpty(info, i) && prefs[i] > widths[i]) {
/* 824 */             higherPriorityCells[i] = true;
/* 825 */             foundCells++;
/*     */           } 
/*     */         }
/*     */       } 
/* 829 */       if (foundCells > 0) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/*     */     int cell;
/* 835 */     for (cell = 0; cell < allowedCells.length; cell++) {
/* 836 */       if (allowedCells[cell])
/*     */       {
/*     */         
/* 839 */         if ((info.getCellSizePolicy(cell) & 0x4) != 0) {
/* 840 */           higherPriorityCells[cell] = true;
/* 841 */           foundCells++;
/*     */         } 
/*     */       }
/*     */     } 
/* 845 */     if (foundCells > 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 850 */     for (cell = 0; cell < allowedCells.length; cell++) {
/* 851 */       if (allowedCells[cell])
/*     */       {
/*     */         
/* 854 */         if ((info.getCellSizePolicy(cell) & 0x2) != 0) {
/* 855 */           higherPriorityCells[cell] = true;
/* 856 */           foundCells++;
/*     */         } 
/*     */       }
/*     */     } 
/* 860 */     if (foundCells > 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 865 */     for (cell = 0; cell < allowedCells.length; cell++) {
/* 866 */       if (allowedCells[cell])
/*     */       {
/*     */         
/* 869 */         if (!isCellEmpty(info, cell)) {
/* 870 */           higherPriorityCells[cell] = true;
/* 871 */           foundCells++;
/*     */         } 
/*     */       }
/*     */     } 
/* 875 */     if (foundCells > 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 880 */     for (cell = 0; cell < allowedCells.length; cell++) {
/* 881 */       if (allowedCells[cell])
/*     */       {
/*     */         
/* 884 */         higherPriorityCells[cell] = true; } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isSameSizeHorizontally() {
/* 889 */     return this.mySameSizeHorizontally;
/*     */   }
/*     */   
/*     */   public boolean isSameSizeVertically() {
/* 893 */     return this.mySameSizeVertically;
/*     */   }
/*     */   
/*     */   public void setSameSizeHorizontally(boolean sameSizeHorizontally) {
/* 897 */     this.mySameSizeHorizontally = sameSizeHorizontally;
/*     */   }
/*     */   
/*     */   public void setSameSizeVertically(boolean sameSizeVertically) {
/* 901 */     this.mySameSizeVertically = sameSizeVertically;
/*     */   }
/*     */   
/*     */   public int[] getHorizontalGridLines() {
/* 905 */     return getGridLines(this.myYs, this.myHeights);
/*     */   }
/*     */   
/*     */   public int[] getVerticalGridLines() {
/* 909 */     return getGridLines(this.myXs, this.myWidths);
/*     */   }
/*     */   
/*     */   private static int[] getGridLines(int[] pos, int[] heights) {
/* 913 */     int[] result = new int[pos.length + 1];
/* 914 */     result[0] = pos[0];
/* 915 */     for (int i = 0; i < pos.length - 1; i++) {
/* 916 */       result[i + 1] = (pos[i] + heights[i] + pos[i + 1]) / 2;
/*     */     }
/* 918 */     result[pos.length] = pos[pos.length - 1] + heights[pos.length - 1];
/* 919 */     return result;
/*     */   }
/*     */   
/*     */   public int getCellCount(boolean isRow) {
/* 923 */     return isRow ? getRowCount() : getColumnCount();
/*     */   }
/*     */   
/*     */   public int getCellSizePolicy(boolean isRow, int cellIndex) {
/* 927 */     DimensionInfo info = isRow ? this.myVerticalInfo : this.myHorizontalInfo;
/* 928 */     if (info == null)
/*     */     {
/* 930 */       return 0;
/*     */     }
/* 932 */     return info.getCellSizePolicy(cellIndex);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\core\GridLayoutManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */