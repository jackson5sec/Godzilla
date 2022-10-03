/*      */ package net.miginfocom.layout;
/*      */ 
/*      */ import java.io.Externalizable;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectOutput;
/*      */ import java.io.ObjectStreamException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class LC
/*      */   implements Externalizable
/*      */ {
/*   44 */   private int wrapAfter = 2097051;
/*      */   
/*   46 */   private Boolean leftToRight = null;
/*      */   
/*   48 */   private UnitValue[] insets = null;
/*      */   
/*   50 */   private UnitValue alignX = null; private UnitValue alignY = null;
/*      */   
/*   52 */   private BoundSize gridGapX = null, gridGapY = null;
/*      */   
/*   54 */   private BoundSize width = BoundSize.NULL_SIZE, height = BoundSize.NULL_SIZE;
/*      */   
/*   56 */   private BoundSize packW = BoundSize.NULL_SIZE; private BoundSize packH = BoundSize.NULL_SIZE;
/*      */   
/*   58 */   private float pwAlign = 0.5F; private float phAlign = 1.0F;
/*      */   
/*   60 */   private int debugMillis = 0;
/*      */   
/*   62 */   private int hideMode = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean noCache = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean flowX = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fillX = false, fillY = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean topToBottom = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean noGrid = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean visualPadding = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoCache() {
/*   93 */     return this.noCache;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNoCache(boolean b) {
/*  102 */     this.noCache = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final UnitValue getAlignX() {
/*  112 */     return this.alignX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAlignX(UnitValue uv) {
/*  122 */     this.alignX = uv;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final UnitValue getAlignY() {
/*  132 */     return this.alignY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAlignY(UnitValue uv) {
/*  142 */     this.alignY = uv;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getDebugMillis() {
/*  150 */     return this.debugMillis;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDebugMillis(int millis) {
/*  158 */     this.debugMillis = millis;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isFillX() {
/*  166 */     return this.fillX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFillX(boolean b) {
/*  174 */     this.fillX = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isFillY() {
/*  182 */     return this.fillY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFillY(boolean b) {
/*  190 */     this.fillY = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isFlowX() {
/*  200 */     return this.flowX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFlowX(boolean b) {
/*  210 */     this.flowX = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BoundSize getGridGapX() {
/*  218 */     return this.gridGapX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setGridGapX(BoundSize x) {
/*  226 */     this.gridGapX = x;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BoundSize getGridGapY() {
/*  234 */     return this.gridGapY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setGridGapY(BoundSize y) {
/*  242 */     this.gridGapY = y;
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
/*      */   public final int getHideMode() {
/*  254 */     return this.hideMode;
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
/*      */   public final void setHideMode(int mode) {
/*  266 */     if (mode < 0 || mode > 3) {
/*  267 */       throw new IllegalArgumentException("Wrong hideMode: " + mode);
/*      */     }
/*  269 */     this.hideMode = mode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final UnitValue[] getInsets() {
/*  279 */     (new UnitValue[4])[0] = this.insets[0]; (new UnitValue[4])[1] = this.insets[1]; (new UnitValue[4])[2] = this.insets[2]; (new UnitValue[4])[3] = this.insets[3]; return (this.insets != null) ? new UnitValue[4] : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setInsets(UnitValue[] ins) {
/*  290 */     (new UnitValue[4])[0] = ins[0]; (new UnitValue[4])[1] = ins[1]; (new UnitValue[4])[2] = ins[2]; (new UnitValue[4])[3] = ins[3]; this.insets = (ins != null) ? new UnitValue[4] : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Boolean getLeftToRight() {
/*  300 */     return this.leftToRight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setLeftToRight(Boolean b) {
/*  310 */     this.leftToRight = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isNoGrid() {
/*  318 */     return this.noGrid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNoGrid(boolean b) {
/*  326 */     this.noGrid = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTopToBottom() {
/*  334 */     return this.topToBottom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTopToBottom(boolean b) {
/*  342 */     this.topToBottom = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isVisualPadding() {
/*  350 */     return this.visualPadding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setVisualPadding(boolean b) {
/*  358 */     this.visualPadding = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getWrapAfter() {
/*  367 */     return this.wrapAfter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setWrapAfter(int count) {
/*  376 */     this.wrapAfter = count;
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
/*      */   public final BoundSize getPackWidth() {
/*  392 */     return this.packW;
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
/*      */   public final void setPackWidth(BoundSize size) {
/*  408 */     this.packW = (size != null) ? size : BoundSize.NULL_SIZE;
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
/*      */   public final BoundSize getPackHeight() {
/*  424 */     return this.packH;
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
/*      */   public final void setPackHeight(BoundSize size) {
/*  440 */     this.packH = (size != null) ? size : BoundSize.NULL_SIZE;
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
/*      */   public final float getPackHeightAlign() {
/*  453 */     return this.phAlign;
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
/*      */   public final void setPackHeightAlign(float align) {
/*  465 */     this.phAlign = Math.max(0.0F, Math.min(1.0F, align));
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
/*      */   public final float getPackWidthAlign() {
/*  477 */     return this.pwAlign;
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
/*      */   public final void setPackWidthAlign(float align) {
/*  489 */     this.pwAlign = Math.max(0.0F, Math.min(1.0F, align));
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
/*      */   public final BoundSize getWidth() {
/*  501 */     return this.width;
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
/*      */   public final void setWidth(BoundSize size) {
/*  513 */     this.width = (size != null) ? size : BoundSize.NULL_SIZE;
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
/*      */   public final BoundSize getHeight() {
/*  525 */     return this.height;
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
/*      */   public final void setHeight(BoundSize size) {
/*  537 */     this.height = (size != null) ? size : BoundSize.NULL_SIZE;
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
/*      */   public final LC pack() {
/*  555 */     return pack("pref", "pref");
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
/*      */   public final LC pack(String width, String height) {
/*  571 */     setPackWidth((width != null) ? ConstraintParser.parseBoundSize(width, false, true) : BoundSize.NULL_SIZE);
/*  572 */     setPackHeight((height != null) ? ConstraintParser.parseBoundSize(height, false, false) : BoundSize.NULL_SIZE);
/*  573 */     return this;
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
/*      */   public final LC packAlign(float alignX, float alignY) {
/*  589 */     setPackWidthAlign(alignX);
/*  590 */     setPackHeightAlign(alignY);
/*  591 */     return this;
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
/*      */   public final LC wrap() {
/*  603 */     setWrapAfter(0);
/*  604 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC wrapAfter(int count) {
/*  615 */     setWrapAfter(count);
/*  616 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC noCache() {
/*  626 */     setNoCache(true);
/*  627 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC flowY() {
/*  637 */     setFlowX(false);
/*  638 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC flowX() {
/*  648 */     setFlowX(true);
/*  649 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC fill() {
/*  660 */     setFillX(true);
/*  661 */     setFillY(true);
/*  662 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC fillX() {
/*  672 */     setFillX(true);
/*  673 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC fillY() {
/*  683 */     setFillY(true);
/*  684 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC leftToRight(boolean b) {
/*  695 */     setLeftToRight(b ? Boolean.TRUE : Boolean.FALSE);
/*  696 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC rightToLeft() {
/*  707 */     setLeftToRight(Boolean.FALSE);
/*  708 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC bottomToTop() {
/*  718 */     setTopToBottom(false);
/*  719 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC topToBottom() {
/*  730 */     setTopToBottom(true);
/*  731 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC noGrid() {
/*  741 */     setNoGrid(true);
/*  742 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC noVisualPadding() {
/*  752 */     setVisualPadding(false);
/*  753 */     return this;
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
/*      */   public final LC insetsAll(String allSides) {
/*  766 */     UnitValue insH = ConstraintParser.parseUnitValue(allSides, true);
/*  767 */     UnitValue insV = ConstraintParser.parseUnitValue(allSides, false);
/*  768 */     this.insets = new UnitValue[] { insV, insH, insV, insH };
/*  769 */     return this;
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
/*      */   public final LC insets(String s) {
/*  782 */     this.insets = ConstraintParser.parseInsets(s, true);
/*  783 */     return this;
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
/*      */   public final LC insets(String top, String left, String bottom, String right) {
/*  802 */     this
/*      */ 
/*      */ 
/*      */       
/*  806 */       .insets = new UnitValue[] { ConstraintParser.parseUnitValue(top, false), ConstraintParser.parseUnitValue(left, true), ConstraintParser.parseUnitValue(bottom, false), ConstraintParser.parseUnitValue(right, true) };
/*  807 */     return this;
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
/*      */   public final LC alignX(String align) {
/*  820 */     setAlignX(ConstraintParser.parseUnitValueOrAlign(align, true, null));
/*  821 */     return this;
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
/*      */   public final LC alignY(String align) {
/*  833 */     setAlignY(ConstraintParser.parseUnitValueOrAlign(align, false, null));
/*  834 */     return this;
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
/*      */   public final LC align(String ax, String ay) {
/*  848 */     if (ax != null) {
/*  849 */       alignX(ax);
/*      */     }
/*  851 */     if (ay != null) {
/*  852 */       alignY(ay);
/*      */     }
/*  854 */     return this;
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
/*      */   public final LC gridGapX(String boundsSize) {
/*  868 */     setGridGapX(ConstraintParser.parseBoundSize(boundsSize, true, true));
/*  869 */     return this;
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
/*      */   public final LC gridGapY(String boundsSize) {
/*  883 */     setGridGapY(ConstraintParser.parseBoundSize(boundsSize, true, false));
/*  884 */     return this;
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
/*      */   public final LC gridGap(String gapx, String gapy) {
/*  900 */     if (gapx != null) {
/*  901 */       gridGapX(gapx);
/*      */     }
/*  903 */     if (gapy != null) {
/*  904 */       gridGapY(gapy);
/*      */     }
/*  906 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC debug() {
/*  915 */     setDebugMillis(300);
/*  916 */     return this;
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
/*      */   public final LC debug(int repaintMillis) {
/*  928 */     setDebugMillis(repaintMillis);
/*  929 */     return this;
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
/*      */   public final LC hideMode(int mode) {
/*  945 */     setHideMode(mode);
/*  946 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC minWidth(String width) {
/*  957 */     setWidth(LayoutUtil.derive(getWidth(), ConstraintParser.parseUnitValue(width, true), null, null));
/*  958 */     return this;
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
/*      */   public final LC width(String width) {
/*  970 */     setWidth(ConstraintParser.parseBoundSize(width, false, true));
/*  971 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC maxWidth(String width) {
/*  982 */     setWidth(LayoutUtil.derive(getWidth(), null, null, ConstraintParser.parseUnitValue(width, true)));
/*  983 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC minHeight(String height) {
/*  994 */     setHeight(LayoutUtil.derive(getHeight(), ConstraintParser.parseUnitValue(height, false), null, null));
/*  995 */     return this;
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
/*      */   public final LC height(String height) {
/* 1007 */     setHeight(ConstraintParser.parseBoundSize(height, false, false));
/* 1008 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LC maxHeight(String height) {
/* 1019 */     setHeight(LayoutUtil.derive(getHeight(), null, null, ConstraintParser.parseUnitValue(height, false)));
/* 1020 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object readResolve() throws ObjectStreamException {
/* 1029 */     return LayoutUtil.getSerializedObject(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 1035 */     LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeExternal(ObjectOutput out) throws IOException {
/* 1041 */     if (getClass() == LC.class)
/* 1042 */       LayoutUtil.writeAsXML(out, this); 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\LC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */