/*     */ package com.intellij.uiDesigner.core;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DimensionInfo
/*     */ {
/*     */   private final int[] myCell;
/*     */   private final int[] mySpan;
/*     */   protected final LayoutState myLayoutState;
/*     */   private final int[] myStretches;
/*     */   private final int[] mySpansAfterElimination;
/*     */   private final int[] myCellSizePolicies;
/*     */   private final int myGap;
/*     */   
/*     */   public DimensionInfo(LayoutState layoutState, int gap) {
/*  36 */     if (layoutState == null) {
/*  37 */       throw new IllegalArgumentException("layoutState cannot be null");
/*     */     }
/*  39 */     if (gap < 0) {
/*  40 */       throw new IllegalArgumentException("invalid gap: " + gap);
/*     */     }
/*  42 */     this.myLayoutState = layoutState;
/*  43 */     this.myGap = gap;
/*     */     
/*  45 */     this.myCell = new int[layoutState.getComponentCount()];
/*  46 */     this.mySpan = new int[layoutState.getComponentCount()];
/*     */     
/*  48 */     for (int i = 0; i < layoutState.getComponentCount(); i++) {
/*  49 */       GridConstraints c = layoutState.getConstraints(i);
/*  50 */       this.myCell[i] = getOriginalCell(c);
/*  51 */       this.mySpan[i] = getOriginalSpan(c);
/*     */     } 
/*     */     
/*  54 */     this.myStretches = new int[getCellCount()];
/*  55 */     Arrays.fill(this.myStretches, 1);
/*     */ 
/*     */     
/*  58 */     ArrayList<Integer> eliminated = new ArrayList<Integer>();
/*  59 */     this.mySpansAfterElimination = (int[])this.mySpan.clone();
/*  60 */     Util.eliminate((int[])this.myCell.clone(), this.mySpansAfterElimination, eliminated);
/*     */     
/*  62 */     this.myCellSizePolicies = new int[getCellCount()];
/*  63 */     for (int j = 0; j < this.myCellSizePolicies.length; j++) {
/*  64 */       this.myCellSizePolicies[j] = getCellSizePolicyImpl(j, eliminated);
/*     */     }
/*     */   }
/*     */   
/*     */   public final int getComponentCount() {
/*  69 */     return this.myLayoutState.getComponentCount();
/*     */   }
/*     */   
/*     */   public final Component getComponent(int componentIndex) {
/*  73 */     return this.myLayoutState.getComponent(componentIndex);
/*     */   }
/*     */   
/*     */   public final GridConstraints getConstraints(int componentIndex) {
/*  77 */     return this.myLayoutState.getConstraints(componentIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract int getCellCount();
/*     */ 
/*     */   
/*     */   public abstract int getPreferredWidth(int paramInt);
/*     */   
/*     */   public final int getCell(int componentIndex) {
/*  87 */     return this.myCell[componentIndex];
/*     */   } public abstract int getMinimumWidth(int paramInt);
/*     */   public abstract DimensionInfo getDimensionInfo(GridLayoutManager paramGridLayoutManager);
/*     */   public final int getSpan(int componentIndex) {
/*  91 */     return this.mySpan[componentIndex];
/*     */   }
/*     */   
/*     */   public final int getStretch(int cellIndex) {
/*  95 */     return this.myStretches[cellIndex];
/*     */   }
/*     */   protected abstract int getOriginalCell(GridConstraints paramGridConstraints);
/*     */   
/*     */   protected abstract int getOriginalSpan(GridConstraints paramGridConstraints);
/*     */   
/*     */   abstract int getSizePolicy(int paramInt);
/*     */   
/*     */   abstract int getChildLayoutCellCount(GridLayoutManager paramGridLayoutManager);
/*     */   
/*     */   public final int getGap() {
/* 106 */     return this.myGap;
/*     */   }
/*     */   
/*     */   public boolean componentBelongsCell(int componentIndex, int cellIndex) {
/* 110 */     int componentStartCell = getCell(componentIndex);
/* 111 */     int span = getSpan(componentIndex);
/* 112 */     return (componentStartCell <= cellIndex && cellIndex < componentStartCell + span);
/*     */   }
/*     */   
/*     */   public final int getCellSizePolicy(int cellIndex) {
/* 116 */     return this.myCellSizePolicies[cellIndex];
/*     */   }
/*     */   
/*     */   private int getCellSizePolicyImpl(int cellIndex, ArrayList<Integer> eliminatedCells) {
/* 120 */     int policyFromChild = getCellSizePolicyFromInheriting(cellIndex);
/* 121 */     if (policyFromChild != -1) {
/* 122 */       return policyFromChild;
/*     */     }
/* 124 */     for (int i = eliminatedCells.size() - 1; i >= 0; i--) {
/* 125 */       if (cellIndex == ((Integer)eliminatedCells.get(i)).intValue()) {
/* 126 */         return 1;
/*     */       }
/*     */     } 
/*     */     
/* 130 */     return calcCellSizePolicy(cellIndex);
/*     */   }
/*     */   
/*     */   private int calcCellSizePolicy(int cellIndex) {
/* 134 */     boolean canShrink = true;
/* 135 */     boolean canGrow = false;
/* 136 */     boolean wantGrow = false;
/*     */     
/* 138 */     boolean weakCanGrow = true;
/* 139 */     boolean weakWantGrow = true;
/*     */     
/* 141 */     int countOfBelongingComponents = 0;
/*     */     
/* 143 */     for (int i = 0; i < getComponentCount(); i++) {
/* 144 */       if (componentBelongsCell(i, cellIndex)) {
/*     */ 
/*     */ 
/*     */         
/* 148 */         countOfBelongingComponents++;
/*     */         
/* 150 */         int p = getSizePolicy(i);
/*     */         
/* 152 */         boolean thisCanShrink = ((p & 0x1) != 0);
/* 153 */         boolean thisCanGrow = ((p & 0x2) != 0);
/* 154 */         boolean thisWantGrow = ((p & 0x4) != 0);
/*     */         
/* 156 */         if (getCell(i) == cellIndex && this.mySpansAfterElimination[i] == 1) {
/* 157 */           canShrink &= thisCanShrink;
/* 158 */           canGrow |= thisCanGrow;
/* 159 */           wantGrow |= thisWantGrow;
/*     */         } 
/*     */         
/* 162 */         if (!thisCanGrow) {
/* 163 */           weakCanGrow = false;
/*     */         }
/* 165 */         if (!thisWantGrow) {
/* 166 */           weakWantGrow = false;
/*     */         }
/*     */       } 
/*     */     } 
/* 170 */     return (
/* 171 */       canShrink ? 1 : 0) | (
/* 172 */       (canGrow || (countOfBelongingComponents > 0 && weakCanGrow)) ? 2 : 0) | ((
/* 173 */       wantGrow || (countOfBelongingComponents > 0 && weakWantGrow)) ? 4 : 0);
/*     */   }
/*     */   
/*     */   private int getCellSizePolicyFromInheriting(int cellIndex) {
/* 177 */     int nonInheritingComponentsInCell = 0;
/* 178 */     int policyFromInheriting = -1;
/* 179 */     for (int i = getComponentCount() - 1; i >= 0; i--) {
/* 180 */       if (componentBelongsCell(i, cellIndex)) {
/*     */ 
/*     */         
/* 183 */         Component child = getComponent(i);
/* 184 */         GridConstraints c = getConstraints(i);
/* 185 */         Container container = findAlignedChild(child, c);
/* 186 */         if (container != null) {
/* 187 */           GridLayoutManager grid = (GridLayoutManager)container.getLayout();
/* 188 */           grid.validateInfos(container);
/* 189 */           DimensionInfo info = getDimensionInfo(grid);
/* 190 */           int policy = info.calcCellSizePolicy(cellIndex - getOriginalCell(c));
/* 191 */           if (policyFromInheriting == -1) {
/* 192 */             policyFromInheriting = policy;
/*     */           } else {
/*     */             
/* 195 */             policyFromInheriting |= policy;
/*     */           }
/*     */         
/* 198 */         } else if (getOriginalCell(c) == cellIndex && getOriginalSpan(c) == 1 && !(child instanceof Spacer)) {
/* 199 */           nonInheritingComponentsInCell++;
/*     */         } 
/*     */       } 
/* 202 */     }  if (nonInheritingComponentsInCell > 0) {
/* 203 */       return -1;
/*     */     }
/* 205 */     return policyFromInheriting;
/*     */   }
/*     */   
/*     */   public static Container findAlignedChild(Component child, GridConstraints c) {
/* 209 */     if (c.isUseParentLayout() && child instanceof Container) {
/* 210 */       Container container = (Container)child;
/* 211 */       if (container.getLayout() instanceof GridLayoutManager) {
/* 212 */         return container;
/*     */       }
/* 214 */       if (container.getComponentCount() == 1 && container.getComponent(0) instanceof Container) {
/*     */ 
/*     */         
/* 217 */         Container childContainer = (Container)container.getComponent(0);
/* 218 */         if (childContainer.getLayout() instanceof GridLayoutManager) {
/* 219 */           return childContainer;
/*     */         }
/*     */       } 
/*     */     } 
/* 223 */     return null;
/*     */   }
/*     */   
/*     */   protected final Dimension getPreferredSize(int componentIndex) {
/* 227 */     Dimension size = this.myLayoutState.myPreferredSizes[componentIndex];
/* 228 */     if (size == null) {
/* 229 */       size = Util.getPreferredSize(this.myLayoutState.getComponent(componentIndex), this.myLayoutState.getConstraints(componentIndex), true);
/* 230 */       this.myLayoutState.myPreferredSizes[componentIndex] = size;
/*     */     } 
/* 232 */     return size;
/*     */   }
/*     */   
/*     */   protected final Dimension getMinimumSize(int componentIndex) {
/* 236 */     Dimension size = this.myLayoutState.myMinimumSizes[componentIndex];
/* 237 */     if (size == null) {
/* 238 */       size = Util.getMinimumSize(this.myLayoutState.getComponent(componentIndex), this.myLayoutState.getConstraints(componentIndex), true);
/* 239 */       this.myLayoutState.myMinimumSizes[componentIndex] = size;
/*     */     } 
/* 241 */     return size;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\core\DimensionInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */