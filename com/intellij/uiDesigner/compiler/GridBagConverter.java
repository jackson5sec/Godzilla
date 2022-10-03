/*     */ package com.intellij.uiDesigner.compiler;
/*     */ 
/*     */ import com.intellij.uiDesigner.core.AbstractLayout;
/*     */ import com.intellij.uiDesigner.core.GridConstraints;
/*     */ import com.intellij.uiDesigner.core.GridLayoutManager;
/*     */ import com.intellij.uiDesigner.lw.IComponent;
/*     */ import com.intellij.uiDesigner.lw.LwComponent;
/*     */ import com.intellij.uiDesigner.lw.LwContainer;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public class GridBagConverter
/*     */ {
/*     */   private Insets myInsets;
/*     */   private int myHGap;
/*     */   private int myVGap;
/*     */   private boolean mySameSizeHorz;
/*     */   private boolean mySameSizeVert;
/*  39 */   private ArrayList myComponents = new ArrayList();
/*  40 */   private ArrayList myConstraints = new ArrayList();
/*  41 */   private int myLastRow = -1;
/*  42 */   private int myLastCol = -1;
/*     */   
/*     */   public GridBagConverter() {
/*  45 */     this.myInsets = new Insets(0, 0, 0, 0);
/*     */   }
/*     */   
/*     */   public GridBagConverter(Insets insets, int hgap, int vgap, boolean sameSizeHorz, boolean sameSizeVert) {
/*  49 */     this.myInsets = insets;
/*  50 */     this.myHGap = hgap;
/*  51 */     this.myVGap = vgap;
/*  52 */     this.mySameSizeHorz = sameSizeHorz;
/*  53 */     this.mySameSizeVert = sameSizeVert;
/*     */   }
/*     */   
/*     */   public void addComponent(JComponent component, GridConstraints constraints) {
/*  57 */     this.myComponents.add(component);
/*  58 */     this.myConstraints.add(constraints);
/*     */   }
/*     */   
/*     */   public static void prepareConstraints(LwContainer container, Map idToConstraintsMap) {
/*  62 */     GridLayoutManager gridLayout = (GridLayoutManager)container.getLayout();
/*  63 */     GridBagConverter converter = new GridBagConverter(gridLayout.getMargin(), getGap(container, true), getGap(container, false), gridLayout.isSameSizeHorizontally(), gridLayout.isSameSizeVertically());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     for (int i = 0; i < container.getComponentCount(); i++) {
/*  69 */       LwComponent component = (LwComponent)container.getComponent(i);
/*  70 */       if (component instanceof com.intellij.uiDesigner.lw.LwHSpacer || component instanceof com.intellij.uiDesigner.lw.LwVSpacer) {
/*  71 */         GridConstraints constraints = component.getConstraints().store();
/*  72 */         constraints.setHSizePolicy(constraints.getHSizePolicy() & 0xFFFFFFFB);
/*  73 */         constraints.setVSizePolicy(constraints.getVSizePolicy() & 0xFFFFFFFB);
/*  74 */         converter.addComponent(null, constraints);
/*     */       } else {
/*     */         
/*  77 */         converter.addComponent(null, component.getConstraints());
/*     */       } 
/*     */     } 
/*  80 */     Result[] results = converter.convert();
/*  81 */     int componentIndex = 0;
/*  82 */     for (int j = 0; j < results.length; j++) {
/*  83 */       if (!(results[j]).isFillerPanel) {
/*  84 */         LwComponent component = (LwComponent)container.getComponent(componentIndex++);
/*  85 */         idToConstraintsMap.put(component.getId(), results[j]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getGap(LwContainer container, boolean horizontal) {
/*  92 */     while (container != null) {
/*  93 */       LayoutManager layout = container.getLayout();
/*  94 */       if (layout instanceof AbstractLayout) {
/*  95 */         AbstractLayout aLayout = (AbstractLayout)layout;
/*  96 */         int gap = horizontal ? aLayout.getHGap() : aLayout.getVGap();
/*  97 */         if (gap >= 0) {
/*  98 */           return gap;
/*     */         }
/*     */       } 
/* 101 */       container = container.getParent();
/*     */     } 
/* 103 */     return horizontal ? 10 : 5;
/*     */   }
/*     */   
/*     */   public static class Result {
/*     */     public JComponent component;
/*     */     public boolean isFillerPanel;
/*     */     public GridBagConstraints constraints;
/*     */     public Dimension preferredSize;
/*     */     public Dimension minimumSize;
/*     */     public Dimension maximumSize;
/*     */     
/*     */     public Result(JComponent component) {
/* 115 */       this.component = component;
/* 116 */       this.constraints = new GridBagConstraints();
/*     */     }
/*     */   }
/*     */   
/*     */   public Result[] convert() {
/* 121 */     ArrayList results = new ArrayList();
/* 122 */     for (int i = 0; i < this.myComponents.size(); i++) {
/* 123 */       results.add(convert(this.myComponents.get(i), this.myConstraints.get(i)));
/*     */     }
/*     */     
/* 126 */     Result[] resultArray = results.<Result>toArray(new Result[results.size()]);
/* 127 */     if (this.myHGap > 0 || this.myVGap > 0) {
/* 128 */       applyGaps(resultArray);
/*     */     }
/* 130 */     if (this.mySameSizeHorz) {
/* 131 */       makeSameSizes(resultArray, true);
/*     */     }
/* 133 */     if (this.mySameSizeVert) {
/* 134 */       makeSameSizes(resultArray, false);
/*     */     }
/*     */     
/* 137 */     return resultArray;
/*     */   }
/*     */   
/*     */   private void applyGaps(Result[] resultArray) {
/* 141 */     int leftGap = this.myHGap / 2;
/* 142 */     int rightGap = this.myHGap - this.myHGap / 2;
/* 143 */     int topGap = this.myVGap / 2;
/* 144 */     int bottomGap = this.myVGap - this.myVGap / 2;
/* 145 */     for (int i = 0; i < resultArray.length; i++) {
/* 146 */       Result result = resultArray[i];
/* 147 */       if (result.constraints.gridx > 0) {
/* 148 */         result.constraints.insets.left += leftGap;
/*     */       }
/* 150 */       if (result.constraints.gridx + result.constraints.gridwidth - 1 < this.myLastCol) {
/* 151 */         result.constraints.insets.right += rightGap;
/*     */       }
/* 153 */       if (result.constraints.gridy > 0) {
/* 154 */         result.constraints.insets.top += topGap;
/*     */       }
/* 156 */       if (result.constraints.gridy + result.constraints.gridheight - 1 < this.myLastRow) {
/* 157 */         result.constraints.insets.bottom += bottomGap;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void makeSameSizes(Result[] resultArray, boolean horizontal) {
/* 163 */     int minimum = -1;
/* 164 */     int preferred = -1; int i;
/* 165 */     for (i = 0; i < resultArray.length; i++) {
/* 166 */       Result result = resultArray[i];
/* 167 */       Dimension minSize = (result.minimumSize != null || result.component == null) ? result.minimumSize : result.component.getMinimumSize();
/*     */ 
/*     */       
/* 170 */       Dimension prefSize = (result.preferredSize != null || result.component == null) ? result.preferredSize : result.component.getPreferredSize();
/*     */ 
/*     */       
/* 173 */       if (minSize != null) {
/* 174 */         minimum = Math.max(minimum, horizontal ? minSize.width : minSize.height);
/*     */       }
/* 176 */       if (prefSize != null) {
/* 177 */         preferred = Math.max(preferred, horizontal ? prefSize.width : prefSize.height);
/*     */       }
/*     */     } 
/*     */     
/* 181 */     if (minimum >= 0 || preferred >= 0) {
/* 182 */       for (i = 0; i < resultArray.length; i++) {
/* 183 */         Result result = resultArray[i];
/*     */         
/* 185 */         if ((result.minimumSize != null || result.component != null) && minimum >= 0) {
/* 186 */           if (result.minimumSize == null) {
/* 187 */             result.minimumSize = result.component.getMinimumSize();
/*     */           }
/* 189 */           if (horizontal) {
/* 190 */             result.minimumSize.width = minimum;
/*     */           } else {
/*     */             
/* 193 */             result.minimumSize.height = minimum;
/*     */           } 
/*     */         } 
/*     */         
/* 197 */         if ((result.preferredSize != null || result.component != null) && preferred >= 0) {
/* 198 */           if (result.preferredSize == null) {
/* 199 */             result.preferredSize = result.component.getPreferredSize();
/*     */           }
/* 201 */           if (horizontal) {
/* 202 */             result.preferredSize.width = preferred;
/*     */           } else {
/*     */             
/* 205 */             result.preferredSize.height = preferred;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private Result convert(JComponent component, GridConstraints constraints) {
/* 213 */     Result result = new Result(component);
/*     */     
/* 215 */     int endRow = constraints.getRow() + constraints.getRowSpan() - 1;
/* 216 */     this.myLastRow = Math.max(this.myLastRow, endRow);
/* 217 */     int endCol = constraints.getColumn() + constraints.getColSpan() - 1;
/* 218 */     this.myLastCol = Math.max(this.myLastCol, endCol);
/*     */     
/* 220 */     int indent = 10 * constraints.getIndent();
/*     */     
/* 222 */     constraintsToGridBag(constraints, result.constraints);
/* 223 */     result.constraints.weightx = getWeight(constraints, true);
/* 224 */     result.constraints.weighty = getWeight(constraints, false);
/* 225 */     result.constraints.insets = new Insets(this.myInsets.top, this.myInsets.left + indent, this.myInsets.bottom, this.myInsets.right);
/*     */     
/* 227 */     Dimension minSize = constraints.myMinimumSize;
/* 228 */     if (component != null && minSize.width <= 0 && minSize.height <= 0) {
/* 229 */       minSize = component.getMinimumSize();
/*     */     }
/*     */     
/* 232 */     if ((constraints.getHSizePolicy() & 0x1) == 0) {
/* 233 */       minSize.width = (constraints.myPreferredSize.width > 0 || component == null) ? constraints.myPreferredSize.width : (component.getPreferredSize()).width;
/*     */     }
/*     */ 
/*     */     
/* 237 */     if ((constraints.getVSizePolicy() & 0x1) == 0) {
/* 238 */       minSize.height = (constraints.myPreferredSize.height > 0 || component == null) ? constraints.myPreferredSize.height : (component.getPreferredSize()).height;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 243 */     if (minSize.width != -1 || minSize.height != -1) {
/* 244 */       result.minimumSize = minSize;
/*     */     }
/*     */     
/* 247 */     if (constraints.myPreferredSize.width > 0 && constraints.myPreferredSize.height > 0) {
/* 248 */       result.preferredSize = constraints.myPreferredSize;
/*     */     }
/* 250 */     if (constraints.myMaximumSize.width > 0 && constraints.myMaximumSize.height > 0) {
/* 251 */       result.maximumSize = constraints.myMaximumSize;
/*     */     }
/*     */     
/* 254 */     return result;
/*     */   }
/*     */   
/*     */   public static GridBagConstraints getGridBagConstraints(IComponent component) {
/*     */     GridBagConstraints gbc;
/* 259 */     if (component.getCustomLayoutConstraints() instanceof GridBagConstraints) {
/* 260 */       gbc = (GridBagConstraints)component.getCustomLayoutConstraints();
/*     */     } else {
/*     */       
/* 263 */       gbc = new GridBagConstraints();
/*     */     } 
/* 265 */     constraintsToGridBag(component.getConstraints(), gbc);
/* 266 */     return gbc;
/*     */   }
/*     */   
/*     */   public static void constraintsToGridBag(GridConstraints constraints, GridBagConstraints result) {
/* 270 */     result.gridx = constraints.getColumn();
/* 271 */     result.gridy = constraints.getRow();
/* 272 */     result.gridwidth = constraints.getColSpan();
/* 273 */     result.gridheight = constraints.getRowSpan();
/* 274 */     switch (constraints.getFill()) { case 1:
/* 275 */         result.fill = 2; break;
/* 276 */       case 2: result.fill = 3; break;
/* 277 */       case 3: result.fill = 1; break;
/* 278 */       default: result.fill = 0; break; }
/*     */     
/* 280 */     switch (constraints.getAnchor()) { case 9:
/* 281 */         result.anchor = 18; break;
/* 282 */       case 1: result.anchor = 11; break;
/* 283 */       case 5: result.anchor = 12; break;
/* 284 */       case 4: result.anchor = 13; break;
/* 285 */       case 6: result.anchor = 14; break;
/* 286 */       case 2: result.anchor = 15; break;
/* 287 */       case 10: result.anchor = 16; break;
/* 288 */       case 8: result.anchor = 17;
/*     */         break; }
/*     */   
/*     */   }
/*     */   private double getWeight(GridConstraints constraints, boolean horizontal) {
/* 293 */     int policy = horizontal ? constraints.getHSizePolicy() : constraints.getVSizePolicy();
/* 294 */     if ((policy & 0x4) != 0) {
/* 295 */       return 1.0D;
/*     */     }
/* 297 */     boolean canGrow = ((policy & 0x2) != 0);
/* 298 */     for (Iterator iterator = this.myConstraints.iterator(); iterator.hasNext(); ) {
/* 299 */       GridConstraints otherConstraints = iterator.next();
/*     */       
/* 301 */       if (!constraintsIntersect(horizontal, constraints, otherConstraints)) {
/* 302 */         int otherPolicy = horizontal ? otherConstraints.getHSizePolicy() : otherConstraints.getVSizePolicy();
/* 303 */         if ((otherPolicy & 0x4) != 0) {
/* 304 */           return 0.0D;
/*     */         }
/* 306 */         if (!canGrow && (otherPolicy & 0x2) != 0) {
/* 307 */           return 0.0D;
/*     */         }
/*     */       } 
/*     */     } 
/* 311 */     return 1.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean constraintsIntersect(boolean horizontal, GridConstraints constraints, GridConstraints otherConstraints) {
/* 317 */     int start = constraints.getCell(!horizontal);
/* 318 */     int end = start + constraints.getSpan(!horizontal) - 1;
/* 319 */     int otherStart = otherConstraints.getCell(!horizontal);
/* 320 */     int otherEnd = otherStart + otherConstraints.getSpan(!horizontal) - 1;
/* 321 */     return (start <= otherEnd && otherStart <= end);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\GridBagConverter.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */