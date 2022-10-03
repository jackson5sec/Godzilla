/*      */ package com.jgoodies.forms.layout;
/*      */ 
/*      */ import com.jgoodies.common.base.Preconditions;
/*      */ import java.awt.Component;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Rectangle;
/*      */ import java.io.Serializable;
/*      */ import java.util.Locale;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class CellConstraints
/*      */   implements Cloneable, Serializable
/*      */ {
/*  104 */   public static final Alignment DEFAULT = new Alignment("default", 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  110 */   public static final Alignment FILL = new Alignment("fill", 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  116 */   public static final Alignment LEFT = new Alignment("left", 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  122 */   public static final Alignment RIGHT = new Alignment("right", 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  128 */   public static final Alignment CENTER = new Alignment("center", 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  134 */   public static final Alignment TOP = new Alignment("top", 1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  140 */   public static final Alignment BOTTOM = new Alignment("bottom", 1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  147 */   private static final Alignment[] VALUES = new Alignment[] { DEFAULT, FILL, LEFT, RIGHT, CENTER, TOP, BOTTOM };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  153 */   private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int gridX;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int gridY;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int gridWidth;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int gridHeight;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Alignment hAlign;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Alignment vAlign;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Insets insets;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Boolean honorsVisibility;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CellConstraints() {
/*  212 */     this(1, 1);
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
/*      */   public CellConstraints(int gridX, int gridY) {
/*  229 */     this(gridX, gridY, 1, 1);
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
/*      */   public CellConstraints(int gridX, int gridY, Alignment hAlign, Alignment vAlign) {
/*  249 */     this(gridX, gridY, 1, 1, hAlign, vAlign, EMPTY_INSETS);
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
/*      */   public CellConstraints(int gridX, int gridY, int gridWidth, int gridHeight) {
/*  268 */     this(gridX, gridY, gridWidth, gridHeight, DEFAULT, DEFAULT);
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
/*      */   public CellConstraints(int gridX, int gridY, int gridWidth, int gridHeight, Alignment hAlign, Alignment vAlign) {
/*  290 */     this(gridX, gridY, gridWidth, gridHeight, hAlign, vAlign, EMPTY_INSETS);
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
/*      */   public CellConstraints(int gridX, int gridY, int gridWidth, int gridHeight, Alignment hAlign, Alignment vAlign, Insets insets) {
/*  316 */     this.gridX = gridX;
/*  317 */     this.gridY = gridY;
/*  318 */     this.gridWidth = gridWidth;
/*  319 */     this.gridHeight = gridHeight;
/*  320 */     this.hAlign = hAlign;
/*  321 */     this.vAlign = vAlign;
/*  322 */     this.insets = insets;
/*  323 */     if (gridX <= 0) {
/*  324 */       throw new IndexOutOfBoundsException("The grid x must be a positive number.");
/*      */     }
/*  326 */     if (gridY <= 0) {
/*  327 */       throw new IndexOutOfBoundsException("The grid y must be a positive number.");
/*      */     }
/*  329 */     if (gridWidth <= 0) {
/*  330 */       throw new IndexOutOfBoundsException("The grid width must be a positive number.");
/*      */     }
/*  332 */     if (gridHeight <= 0) {
/*  333 */       throw new IndexOutOfBoundsException("The grid height must be a positive number.");
/*      */     }
/*  335 */     Preconditions.checkNotNull(hAlign, "The horizontal alignment must not be null.");
/*  336 */     Preconditions.checkNotNull(vAlign, "The vertical alignment must not be null.");
/*  337 */     ensureValidOrientations(hAlign, vAlign);
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
/*      */   public CellConstraints(String encodedConstraints) {
/*  355 */     this();
/*  356 */     initFromConstraints(encodedConstraints);
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
/*      */   public CellConstraints translate(int dx, int dy) {
/*  374 */     return new CellConstraints(this.gridX + dx, this.gridY + dy, this.gridWidth, this.gridHeight, this.hAlign, this.vAlign, this.insets);
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
/*      */   public CellConstraints xy(int col, int row) {
/*  394 */     return xywh(col, row, 1, 1);
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
/*      */   public CellConstraints xy(int col, int row, String encodedAlignments) {
/*  417 */     return xywh(col, row, 1, 1, encodedAlignments);
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
/*      */   public CellConstraints xy(int col, int row, Alignment colAlign, Alignment rowAlign) {
/*  437 */     return xywh(col, row, 1, 1, colAlign, rowAlign);
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
/*      */   public CellConstraints xyw(int col, int row, int colSpan) {
/*  456 */     return xywh(col, row, colSpan, 1, DEFAULT, DEFAULT);
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
/*      */   public CellConstraints xyw(int col, int row, int colSpan, String encodedAlignments) {
/*  481 */     return xywh(col, row, colSpan, 1, encodedAlignments);
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
/*      */   public CellConstraints xyw(int col, int row, int colSpan, Alignment colAlign, Alignment rowAlign) {
/*  505 */     return xywh(col, row, colSpan, 1, colAlign, rowAlign);
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
/*      */   public CellConstraints xywh(int col, int row, int colSpan, int rowSpan) {
/*  524 */     return xywh(col, row, colSpan, rowSpan, DEFAULT, DEFAULT);
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
/*      */   public CellConstraints xywh(int col, int row, int colSpan, int rowSpan, String encodedAlignments) {
/*  549 */     CellConstraints result = xywh(col, row, colSpan, rowSpan);
/*  550 */     result.setAlignments(encodedAlignments, true);
/*  551 */     return result;
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
/*      */   public CellConstraints xywh(int col, int row, int colSpan, int rowSpan, Alignment colAlign, Alignment rowAlign) {
/*  575 */     this.gridX = col;
/*  576 */     this.gridY = row;
/*  577 */     this.gridWidth = colSpan;
/*  578 */     this.gridHeight = rowSpan;
/*  579 */     this.hAlign = colAlign;
/*  580 */     this.vAlign = rowAlign;
/*  581 */     ensureValidOrientations(this.hAlign, this.vAlign);
/*  582 */     return this;
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
/*      */   public CellConstraints rc(int row, int col) {
/*  604 */     return rchw(row, col, 1, 1);
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
/*      */   public CellConstraints rc(int row, int col, String encodedAlignments) {
/*  629 */     return rchw(row, col, 1, 1, encodedAlignments);
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
/*      */   public CellConstraints rc(int row, int col, Alignment rowAlign, Alignment colAlign) {
/*  652 */     return rchw(row, col, 1, 1, rowAlign, colAlign);
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
/*      */   public CellConstraints rcw(int row, int col, int colSpan) {
/*  673 */     return rchw(row, col, 1, colSpan, DEFAULT, DEFAULT);
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
/*      */   public CellConstraints rcw(int row, int col, int colSpan, String encodedAlignments) {
/*  701 */     return rchw(row, col, 1, colSpan, encodedAlignments);
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
/*      */   public CellConstraints rcw(int row, int col, int colSpan, Alignment rowAlign, Alignment colAlign) {
/*  728 */     return rchw(row, col, 1, colSpan, rowAlign, colAlign);
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
/*      */   public CellConstraints rchw(int row, int col, int rowSpan, int colSpan) {
/*  749 */     return rchw(row, col, rowSpan, colSpan, DEFAULT, DEFAULT);
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
/*      */   public CellConstraints rchw(int row, int col, int rowSpan, int colSpan, String encodedAlignments) {
/*  776 */     CellConstraints result = rchw(row, col, rowSpan, colSpan);
/*  777 */     result.setAlignments(encodedAlignments, false);
/*  778 */     return result;
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
/*      */   public CellConstraints rchw(int row, int col, int rowSpan, int colSpan, Alignment rowAlign, Alignment colAlign) {
/*  805 */     return xywh(col, row, colSpan, rowSpan, colAlign, rowAlign);
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
/*      */   private void initFromConstraints(String encodedConstraints) {
/*  829 */     StringTokenizer tokenizer = new StringTokenizer(encodedConstraints, " ,");
/*  830 */     int argCount = tokenizer.countTokens();
/*  831 */     Preconditions.checkArgument((argCount == 2 || argCount == 4 || argCount == 6), "You must provide 2, 4 or 6 arguments.");
/*      */     
/*  833 */     Integer nextInt = decodeInt(tokenizer.nextToken());
/*  834 */     Preconditions.checkArgument((nextInt != null), "First cell constraint element must be a number.");
/*      */     
/*  836 */     this.gridX = nextInt.intValue();
/*  837 */     Preconditions.checkArgument((this.gridX > 0), "The grid x must be a positive number.");
/*  838 */     nextInt = decodeInt(tokenizer.nextToken());
/*  839 */     Preconditions.checkArgument((nextInt != null), "Second cell constraint element must be a number.");
/*      */     
/*  841 */     this.gridY = nextInt.intValue();
/*  842 */     Preconditions.checkArgument((this.gridY > 0), "The grid y must be a positive number.");
/*  843 */     if (!tokenizer.hasMoreTokens()) {
/*      */       return;
/*      */     }
/*      */     
/*  847 */     String token = tokenizer.nextToken();
/*  848 */     nextInt = decodeInt(token);
/*  849 */     if (nextInt != null) {
/*      */ 
/*      */       
/*  852 */       this.gridWidth = nextInt.intValue();
/*  853 */       if (this.gridWidth <= 0) {
/*  854 */         throw new IndexOutOfBoundsException("The grid width must be a positive number.");
/*      */       }
/*      */       
/*  857 */       nextInt = decodeInt(tokenizer.nextToken());
/*  858 */       if (nextInt == null) {
/*  859 */         throw new IllegalArgumentException("Fourth cell constraint element must be like third.");
/*      */       }
/*      */       
/*  862 */       this.gridHeight = nextInt.intValue();
/*  863 */       if (this.gridHeight <= 0) {
/*  864 */         throw new IndexOutOfBoundsException("The grid height must be a positive number.");
/*      */       }
/*      */ 
/*      */       
/*  868 */       if (!tokenizer.hasMoreTokens()) {
/*      */         return;
/*      */       }
/*  871 */       token = tokenizer.nextToken();
/*      */     } 
/*      */     
/*  874 */     this.hAlign = decodeAlignment(token);
/*  875 */     this.vAlign = decodeAlignment(tokenizer.nextToken());
/*  876 */     ensureValidOrientations(this.hAlign, this.vAlign);
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
/*      */   private void setAlignments(String encodedAlignments, boolean horizontalThenVertical) {
/*  901 */     StringTokenizer tokenizer = new StringTokenizer(encodedAlignments, " ,");
/*  902 */     Alignment first = decodeAlignment(tokenizer.nextToken());
/*  903 */     Alignment second = decodeAlignment(tokenizer.nextToken());
/*  904 */     this.hAlign = horizontalThenVertical ? first : second;
/*  905 */     this.vAlign = horizontalThenVertical ? second : first;
/*  906 */     ensureValidOrientations(this.hAlign, this.vAlign);
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
/*      */   private static Integer decodeInt(String token) {
/*      */     try {
/*  919 */       return Integer.decode(token);
/*  920 */     } catch (NumberFormatException e) {
/*  921 */       return null;
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
/*      */   private static Alignment decodeAlignment(String encodedAlignment) {
/*  934 */     return Alignment.valueOf(encodedAlignment);
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
/*      */   void ensureValidGridBounds(int colCount, int rowCount) {
/*  948 */     if (this.gridX <= 0) {
/*  949 */       throw new IndexOutOfBoundsException("The column index " + this.gridX + " must be positive.");
/*      */     }
/*      */     
/*  952 */     if (this.gridX > colCount) {
/*  953 */       throw new IndexOutOfBoundsException("The column index " + this.gridX + " must be less than or equal to " + colCount + ".");
/*      */     }
/*      */ 
/*      */     
/*  957 */     if (this.gridX + this.gridWidth - 1 > colCount) {
/*  958 */       throw new IndexOutOfBoundsException("The grid width " + this.gridWidth + " must be less than or equal to " + (colCount - this.gridX + 1) + ".");
/*      */     }
/*      */ 
/*      */     
/*  962 */     if (this.gridY <= 0) {
/*  963 */       throw new IndexOutOfBoundsException("The row index " + this.gridY + " must be positive.");
/*      */     }
/*      */     
/*  966 */     if (this.gridY > rowCount) {
/*  967 */       throw new IndexOutOfBoundsException("The row index " + this.gridY + " must be less than or equal to " + rowCount + ".");
/*      */     }
/*      */ 
/*      */     
/*  971 */     if (this.gridY + this.gridHeight - 1 > rowCount) {
/*  972 */       throw new IndexOutOfBoundsException("The grid height " + this.gridHeight + " must be less than or equal to " + (rowCount - this.gridY + 1) + ".");
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
/*      */   private static void ensureValidOrientations(Alignment horizontalAlignment, Alignment verticalAlignment) {
/*  988 */     if (!horizontalAlignment.isHorizontal()) {
/*  989 */       throw new IllegalArgumentException("The horizontal alignment must be one of: left, center, right, fill, default.");
/*      */     }
/*  991 */     if (!verticalAlignment.isVertical()) {
/*  992 */       throw new IllegalArgumentException("The vertical alignment must be one of: top, center, bottom, fill, default.");
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
/*      */   void setBounds(Component c, FormLayout layout, Rectangle cellBounds, FormLayout.Measure minWidthMeasure, FormLayout.Measure minHeightMeasure, FormLayout.Measure prefWidthMeasure, FormLayout.Measure prefHeightMeasure) {
/* 1016 */     ColumnSpec colSpec = (this.gridWidth == 1) ? layout.getColumnSpec(this.gridX) : null;
/* 1017 */     RowSpec rowSpec = (this.gridHeight == 1) ? layout.getRowSpec(this.gridY) : null;
/* 1018 */     Alignment concreteHAlign = concreteAlignment(this.hAlign, colSpec);
/* 1019 */     Alignment concreteVAlign = concreteAlignment(this.vAlign, rowSpec);
/* 1020 */     Insets concreteInsets = (this.insets != null) ? this.insets : EMPTY_INSETS;
/* 1021 */     int cellX = cellBounds.x + concreteInsets.left;
/* 1022 */     int cellY = cellBounds.y + concreteInsets.top;
/* 1023 */     int cellW = cellBounds.width - concreteInsets.left - concreteInsets.right;
/* 1024 */     int cellH = cellBounds.height - concreteInsets.top - concreteInsets.bottom;
/* 1025 */     int compW = componentSize(c, colSpec, cellW, minWidthMeasure, prefWidthMeasure);
/*      */     
/* 1027 */     int compH = componentSize(c, rowSpec, cellH, minHeightMeasure, prefHeightMeasure);
/*      */     
/* 1029 */     int x = origin(concreteHAlign, cellX, cellW, compW);
/* 1030 */     int y = origin(concreteVAlign, cellY, cellH, compH);
/* 1031 */     int w = extent(concreteHAlign, cellW, compW);
/* 1032 */     int h = extent(concreteVAlign, cellH, compH);
/* 1033 */     c.setBounds(x, y, w, h);
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
/*      */   private static Alignment concreteAlignment(Alignment cellAlignment, FormSpec formSpec) {
/* 1055 */     return (formSpec == null) ? ((cellAlignment == DEFAULT) ? FILL : cellAlignment) : usedAlignment(cellAlignment, formSpec);
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
/*      */   private static Alignment usedAlignment(Alignment cellAlignment, FormSpec formSpec) {
/* 1072 */     if (cellAlignment != DEFAULT)
/*      */     {
/* 1074 */       return cellAlignment;
/*      */     }
/* 1076 */     FormSpec.DefaultAlignment defaultAlignment = formSpec.getDefaultAlignment();
/* 1077 */     if (defaultAlignment == FormSpec.FILL_ALIGN) {
/* 1078 */       return FILL;
/*      */     }
/* 1080 */     if (defaultAlignment == ColumnSpec.LEFT)
/* 1081 */       return LEFT; 
/* 1082 */     if (defaultAlignment == FormSpec.CENTER_ALIGN)
/* 1083 */       return CENTER; 
/* 1084 */     if (defaultAlignment == ColumnSpec.RIGHT)
/* 1085 */       return RIGHT; 
/* 1086 */     if (defaultAlignment == RowSpec.TOP) {
/* 1087 */       return TOP;
/*      */     }
/* 1089 */     return BOTTOM;
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
/*      */   private static int componentSize(Component component, FormSpec formSpec, int cellSize, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure) {
/* 1110 */     if (formSpec == null)
/* 1111 */       return prefMeasure.sizeOf(component); 
/* 1112 */     if (formSpec.getSize() == Sizes.MINIMUM)
/* 1113 */       return minMeasure.sizeOf(component); 
/* 1114 */     if (formSpec.getSize() == Sizes.PREFERRED) {
/* 1115 */       return prefMeasure.sizeOf(component);
/*      */     }
/* 1117 */     return Math.min(cellSize, prefMeasure.sizeOf(component));
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
/*      */   private static int origin(Alignment alignment, int cellOrigin, int cellSize, int componentSize) {
/* 1135 */     if (alignment == RIGHT || alignment == BOTTOM)
/* 1136 */       return cellOrigin + cellSize - componentSize; 
/* 1137 */     if (alignment == CENTER) {
/* 1138 */       return cellOrigin + (cellSize - componentSize) / 2;
/*      */     }
/* 1140 */     return cellOrigin;
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
/*      */   private static int extent(Alignment alignment, int cellSize, int componentSize) {
/* 1154 */     return (alignment == FILL) ? cellSize : componentSize;
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
/*      */   public Object clone() {
/*      */     try {
/* 1170 */       CellConstraints c = (CellConstraints)super.clone();
/* 1171 */       c.insets = (Insets)this.insets.clone();
/* 1172 */       return c;
/* 1173 */     } catch (CloneNotSupportedException e) {
/*      */       
/* 1175 */       throw new InternalError();
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
/*      */   public String toString() {
/* 1187 */     StringBuffer buffer = new StringBuffer("CellConstraints");
/* 1188 */     buffer.append("[x=");
/* 1189 */     buffer.append(this.gridX);
/* 1190 */     buffer.append("; y=");
/* 1191 */     buffer.append(this.gridY);
/* 1192 */     buffer.append("; w=");
/* 1193 */     buffer.append(this.gridWidth);
/* 1194 */     buffer.append("; h=");
/* 1195 */     buffer.append(this.gridHeight);
/* 1196 */     buffer.append("; hAlign=");
/* 1197 */     buffer.append(this.hAlign);
/* 1198 */     buffer.append("; vAlign=");
/* 1199 */     buffer.append(this.vAlign);
/* 1200 */     if (!EMPTY_INSETS.equals(this.insets)) {
/* 1201 */       buffer.append("; insets=");
/* 1202 */       buffer.append(this.insets);
/*      */     } 
/* 1204 */     buffer.append("; honorsVisibility=");
/* 1205 */     buffer.append(this.honorsVisibility);
/*      */     
/* 1207 */     buffer.append(']');
/* 1208 */     return buffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toShortString() {
/* 1218 */     return toShortString(null);
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
/*      */   public String toShortString(FormLayout layout) {
/* 1233 */     StringBuffer buffer = new StringBuffer("(");
/* 1234 */     buffer.append(formatInt(this.gridX));
/* 1235 */     buffer.append(", ");
/* 1236 */     buffer.append(formatInt(this.gridY));
/* 1237 */     buffer.append(", ");
/* 1238 */     buffer.append(formatInt(this.gridWidth));
/* 1239 */     buffer.append(", ");
/* 1240 */     buffer.append(formatInt(this.gridHeight));
/* 1241 */     buffer.append(", \"");
/* 1242 */     buffer.append(this.hAlign.abbreviation());
/* 1243 */     if (this.hAlign == DEFAULT && layout != null) {
/* 1244 */       buffer.append('=');
/* 1245 */       ColumnSpec colSpec = (this.gridWidth == 1) ? layout.getColumnSpec(this.gridX) : null;
/*      */ 
/*      */       
/* 1248 */       buffer.append(concreteAlignment(this.hAlign, colSpec).abbreviation());
/*      */     } 
/* 1250 */     buffer.append(", ");
/* 1251 */     buffer.append(this.vAlign.abbreviation());
/* 1252 */     if (this.vAlign == DEFAULT && layout != null) {
/* 1253 */       buffer.append('=');
/* 1254 */       RowSpec rowSpec = (this.gridHeight == 1) ? layout.getRowSpec(this.gridY) : null;
/*      */ 
/*      */       
/* 1257 */       buffer.append(concreteAlignment(this.vAlign, rowSpec).abbreviation());
/*      */     } 
/* 1259 */     buffer.append("\"");
/* 1260 */     if (!EMPTY_INSETS.equals(this.insets)) {
/* 1261 */       buffer.append(", ");
/* 1262 */       buffer.append(this.insets);
/*      */     } 
/* 1264 */     if (this.honorsVisibility != null) {
/* 1265 */       buffer.append(this.honorsVisibility.booleanValue() ? "honors visibility" : "ignores visibility");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1270 */     buffer.append(')');
/* 1271 */     return buffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Alignment
/*      */     implements Serializable
/*      */   {
/*      */     private static final int HORIZONTAL = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final int VERTICAL = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final int BOTH = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final transient String name;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final transient int orientation;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static Alignment valueOf(String nameOrAbbreviation) {
/*      */       String str = nameOrAbbreviation.toLowerCase(Locale.ENGLISH);
/*      */       if (str.equals("d") || str.equals("default")) {
/*      */         return CellConstraints.DEFAULT;
/*      */       }
/*      */       if (str.equals("f") || str.equals("fill")) {
/*      */         return CellConstraints.FILL;
/*      */       }
/*      */       if (str.equals("c") || str.equals("center")) {
/*      */         return CellConstraints.CENTER;
/*      */       }
/*      */       if (str.equals("l") || str.equals("left")) {
/*      */         return CellConstraints.LEFT;
/*      */       }
/*      */       if (str.equals("r") || str.equals("right")) {
/*      */         return CellConstraints.RIGHT;
/*      */       }
/*      */       if (str.equals("t") || str.equals("top")) {
/*      */         return CellConstraints.TOP;
/*      */       }
/*      */       if (str.equals("b") || str.equals("bottom")) {
/*      */         return CellConstraints.BOTTOM;
/*      */       }
/*      */       throw new IllegalArgumentException("Invalid alignment " + nameOrAbbreviation + ". Must be one of: left, center, right, top, bottom, " + "fill, default, l, c, r, t, b, f, d.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*      */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public char abbreviation() {
/*      */       return this.name.charAt(0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Alignment(String name, int orientation) {
/* 1352 */       this.ordinal = nextOrdinal++;
/*      */       this.name = name;
/*      */       this.orientation = orientation; } private Object readResolve() {
/* 1355 */       return CellConstraints.VALUES[this.ordinal];
/*      */     }
/*      */     private boolean isHorizontal() {
/*      */       return (this.orientation != 1);
/*      */     }
/*      */     private boolean isVertical() {
/*      */       return (this.orientation != 0);
/*      */     }
/*      */     
/*      */     private static int nextOrdinal = 0;
/*      */     private final int ordinal; }
/*      */   
/*      */   private static String formatInt(int number) {
/* 1368 */     String str = Integer.toString(number);
/* 1369 */     return (number < 10) ? (" " + str) : str;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\layout\CellConstraints.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */