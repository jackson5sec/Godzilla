/*     */ package net.miginfocom.layout;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.ObjectStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DimConstraint
/*     */   implements Externalizable
/*     */ {
/*  48 */   final ResizeConstraint resize = new ResizeConstraint();
/*     */ 
/*     */ 
/*     */   
/*  52 */   private String sizeGroup = null;
/*     */   
/*  54 */   private BoundSize size = BoundSize.NULL_SIZE;
/*     */   
/*  56 */   private BoundSize gapBefore = null; private BoundSize gapAfter = null;
/*     */   
/*  58 */   private UnitValue align = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private String endGroup = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean fill = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean noGrid = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGrowPriority() {
/*  85 */     return this.resize.growPrio;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGrowPriority(int p) {
/*  95 */     this.resize.growPrio = p;
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
/*     */   public Float getGrow() {
/* 110 */     return this.resize.grow;
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
/*     */   public void setGrow(Float weight) {
/* 125 */     this.resize.grow = weight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getShrinkPriority() {
/* 135 */     return this.resize.shrinkPrio;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShrinkPriority(int p) {
/* 145 */     this.resize.shrinkPrio = p;
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
/*     */   public Float getShrink() {
/* 160 */     return this.resize.shrink;
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
/*     */   public void setShrink(Float weight) {
/* 175 */     this.resize.shrink = weight;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnitValue getAlignOrDefault(boolean isCols) {
/* 180 */     if (this.align != null) {
/* 181 */       return this.align;
/*     */     }
/* 183 */     if (isCols) {
/* 184 */       return UnitValue.LEADING;
/*     */     }
/* 186 */     return (this.fill || !PlatformDefaults.getDefaultRowAlignmentBaseline()) ? UnitValue.CENTER : UnitValue.BASELINE_IDENTITY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnitValue getAlign() {
/* 196 */     return this.align;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlign(UnitValue uv) {
/* 206 */     this.align = uv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundSize getGapAfter() {
/* 217 */     return this.gapAfter;
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
/*     */   public void setGapAfter(BoundSize size) {
/* 229 */     this.gapAfter = size;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean hasGapAfter() {
/* 234 */     return (this.gapAfter != null && !this.gapAfter.isUnset());
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isGapAfterPush() {
/* 239 */     return (this.gapAfter != null && this.gapAfter.getGapPush());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundSize getGapBefore() {
/* 250 */     return this.gapBefore;
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
/*     */   public void setGapBefore(BoundSize size) {
/* 262 */     this.gapBefore = size;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean hasGapBefore() {
/* 267 */     return (this.gapBefore != null && !this.gapBefore.isUnset());
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isGapBeforePush() {
/* 272 */     return (this.gapBefore != null && this.gapBefore.getGapPush());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundSize getSize() {
/* 283 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(BoundSize size) {
/* 293 */     if (size != null)
/* 294 */       size.checkNotLinked(); 
/* 295 */     this.size = size;
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
/*     */   public String getSizeGroup() {
/* 309 */     return this.sizeGroup;
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
/*     */   public void setSizeGroup(String s) {
/* 323 */     this.sizeGroup = s;
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
/*     */   public String getEndGroup() {
/* 338 */     return this.endGroup;
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
/*     */   public void setEndGroup(String s) {
/* 351 */     this.endGroup = s;
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
/*     */   public boolean isFill() {
/* 364 */     return this.fill;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFill(boolean b) {
/* 375 */     this.fill = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNoGrid() {
/* 386 */     return this.noGrid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoGrid(boolean b) {
/* 397 */     this.noGrid = b;
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
/*     */   int[] getRowGaps(ContainerWrapper parent, BoundSize defGap, int refSize, boolean before) {
/* 410 */     BoundSize gap = before ? this.gapBefore : this.gapAfter;
/* 411 */     if (gap == null || gap.isUnset()) {
/* 412 */       gap = defGap;
/*     */     }
/* 414 */     if (gap == null || gap.isUnset()) {
/* 415 */       return null;
/*     */     }
/* 417 */     int[] ret = new int[3];
/* 418 */     for (int i = 0; i <= 2; i++) {
/* 419 */       UnitValue uv = gap.getSize(i);
/* 420 */       ret[i] = (uv != null) ? uv.getPixels(refSize, parent, null) : -2147471302;
/*     */     } 
/* 422 */     return ret;
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
/*     */   int[] getComponentGaps(ContainerWrapper parent, ComponentWrapper comp, BoundSize adjGap, ComponentWrapper adjacentComp, String tag, int refSize, int adjacentSide, boolean isLTR) {
/* 439 */     BoundSize gap = (adjacentSide < 2) ? this.gapBefore : this.gapAfter;
/*     */     
/* 441 */     boolean hasGap = (gap != null && gap.getGapPush());
/* 442 */     if ((gap == null || gap.isUnset()) && (adjGap == null || adjGap.isUnset()) && comp != null) {
/* 443 */       gap = PlatformDefaults.getDefaultComponentGap(comp, adjacentComp, adjacentSide + 1, tag, isLTR);
/*     */     }
/* 445 */     if (gap == null) {
/* 446 */       (new int[3])[0] = 0; (new int[3])[1] = 0; (new int[3])[2] = -2147471302; return hasGap ? new int[3] : null;
/*     */     } 
/* 448 */     int[] ret = new int[3];
/* 449 */     for (int i = 0; i <= 2; i++) {
/* 450 */       UnitValue uv = gap.getSize(i);
/* 451 */       ret[i] = (uv != null) ? uv.getPixels(refSize, parent, null) : -2147471302;
/*     */     } 
/* 453 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolve() throws ObjectStreamException {
/* 462 */     return LayoutUtil.getSerializedObject(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 468 */     LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 474 */     if (getClass() == DimConstraint.class)
/* 475 */       LayoutUtil.writeAsXML(out, this); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\DimConstraint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */