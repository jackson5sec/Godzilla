/*      */ package net.miginfocom.layout;
/*      */ 
/*      */ import java.io.Externalizable;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectOutput;
/*      */ import java.io.ObjectStreamException;
/*      */ import java.util.ArrayList;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class CC
/*      */   implements Externalizable
/*      */ {
/*   43 */   private static final BoundSize DEF_GAP = BoundSize.NULL_SIZE;
/*      */   
/*   45 */   static final String[] DOCK_SIDES = new String[] { "north", "west", "south", "east" };
/*      */ 
/*      */ 
/*      */   
/*   49 */   private int dock = -1;
/*      */   
/*   51 */   private UnitValue[] pos = null;
/*      */   
/*   53 */   private UnitValue[] padding = null;
/*      */   
/*   55 */   private UnitValue[] visualPadding = null;
/*      */   
/*   57 */   private Boolean flowX = null;
/*      */   
/*   59 */   private int skip = 0;
/*      */   
/*   61 */   private int split = 1;
/*      */   
/*   63 */   private int spanX = 1, spanY = 1;
/*      */   
/*   65 */   private int cellX = -1; private int cellY = 0;
/*      */   
/*   67 */   private String tag = null;
/*      */   
/*   69 */   private String id = null;
/*      */   
/*   71 */   private int hideMode = -1;
/*      */   
/*   73 */   private DimConstraint hor = new DimConstraint();
/*      */   
/*   75 */   private DimConstraint ver = new DimConstraint();
/*      */   
/*   77 */   private BoundSize newline = null;
/*      */   
/*   79 */   private BoundSize wrap = null;
/*      */   
/*      */   private boolean boundsInGrid = true;
/*      */   
/*      */   private boolean external = false;
/*      */   
/*   85 */   private Float pushX = null; private Float pushY = null;
/*      */   
/*   87 */   private AnimSpec animSpec = AnimSpec.DEF;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   92 */   private static final String[] EMPTY_ARR = new String[0];
/*      */   
/*   94 */   private transient String[] linkTargets = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String[] getLinkTargets() {
/*  104 */     if (this.linkTargets == null) {
/*  105 */       ArrayList<String> targets = new ArrayList<>(2);
/*      */       
/*  107 */       if (this.pos != null) {
/*  108 */         for (int i = 0; i < this.pos.length; i++) {
/*  109 */           addLinkTargetIDs(targets, this.pos[i]);
/*      */         }
/*      */       }
/*  112 */       this.linkTargets = (targets.size() == 0) ? EMPTY_ARR : targets.<String>toArray(new String[targets.size()]);
/*      */     } 
/*  114 */     return this.linkTargets;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addLinkTargetIDs(ArrayList<String> targets, UnitValue uv) {
/*  119 */     if (uv != null) {
/*  120 */       String linkId = uv.getLinkTargetId();
/*  121 */       if (linkId != null) {
/*  122 */         targets.add(linkId);
/*      */       } else {
/*  124 */         for (int i = uv.getSubUnitCount() - 1; i >= 0; i--) {
/*  125 */           UnitValue subUv = uv.getSubUnitValue(i);
/*  126 */           if (subUv.isLinkedDeep()) {
/*  127 */             addLinkTargetIDs(targets, subUv);
/*      */           }
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
/*      */   public final CC endGroupX(String s) {
/*  146 */     this.hor.setEndGroup(s);
/*  147 */     return this;
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
/*      */   public final CC sizeGroupX(String s) {
/*  159 */     this.hor.setSizeGroup(s);
/*  160 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC minWidth(String size) {
/*  171 */     this.hor.setSize(LayoutUtil.derive(this.hor.getSize(), ConstraintParser.parseUnitValue(size, true), null, null));
/*  172 */     return this;
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
/*      */   public final CC width(String size) {
/*  184 */     this.hor.setSize(ConstraintParser.parseBoundSize(size, false, true));
/*  185 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC maxWidth(String size) {
/*  196 */     this.hor.setSize(LayoutUtil.derive(this.hor.getSize(), null, null, ConstraintParser.parseUnitValue(size, true)));
/*  197 */     return this;
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
/*      */   public final CC gapX(String before, String after) {
/*  210 */     if (before != null) {
/*  211 */       this.hor.setGapBefore(ConstraintParser.parseBoundSize(before, true, true));
/*      */     }
/*  213 */     if (after != null) {
/*  214 */       this.hor.setGapAfter(ConstraintParser.parseBoundSize(after, true, true));
/*      */     }
/*  216 */     return this;
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
/*      */   public final CC alignX(String align) {
/*  228 */     this.hor.setAlign(ConstraintParser.parseUnitValueOrAlign(align, true, null));
/*  229 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC growPrioX(int p) {
/*  240 */     this.hor.setGrowPriority(p);
/*  241 */     return this;
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
/*      */   public final CC growPrio(int... widthHeight) {
/*  253 */     switch (widthHeight.length)
/*      */     { default:
/*  255 */         throw new IllegalArgumentException("Illegal argument count: " + widthHeight.length);
/*      */       case 2:
/*  257 */         growPrioY(widthHeight[1]); break;
/*      */       case 1:
/*  259 */         break; }  growPrioX(widthHeight[0]);
/*      */     
/*  261 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC growX() {
/*  272 */     this.hor.setGrow(ResizeConstraint.WEIGHT_100);
/*  273 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC growX(float w) {
/*  284 */     this.hor.setGrow(new Float(w));
/*  285 */     return this;
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
/*      */   public final CC grow(float... widthHeight) {
/*  297 */     switch (widthHeight.length)
/*      */     { default:
/*  299 */         throw new IllegalArgumentException("Illegal argument count: " + widthHeight.length);
/*      */       case 2:
/*  301 */         growY(widthHeight[1]); break;
/*      */       case 1:
/*  303 */         break; }  growX(widthHeight[0]);
/*      */     
/*  305 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC shrinkPrioX(int p) {
/*  316 */     this.hor.setShrinkPriority(p);
/*  317 */     return this;
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
/*      */   public final CC shrinkPrio(int... widthHeight) {
/*  329 */     switch (widthHeight.length)
/*      */     { default:
/*  331 */         throw new IllegalArgumentException("Illegal argument count: " + widthHeight.length);
/*      */       case 2:
/*  333 */         shrinkPrioY(widthHeight[1]); break;
/*      */       case 1:
/*  335 */         break; }  shrinkPrioX(widthHeight[0]);
/*      */     
/*  337 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC shrinkX(float w) {
/*  348 */     this.hor.setShrink(new Float(w));
/*  349 */     return this;
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
/*      */   public final CC shrink(float... widthHeight) {
/*  361 */     switch (widthHeight.length)
/*      */     { default:
/*  363 */         throw new IllegalArgumentException("Illegal argument count: " + widthHeight.length);
/*      */       case 2:
/*  365 */         shrinkY(widthHeight[1]); break;
/*      */       case 1:
/*  367 */         break; }  shrinkX(widthHeight[0]);
/*      */     
/*  369 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC endGroupY(String s) {
/*  380 */     this.ver.setEndGroup(s);
/*  381 */     return this;
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
/*      */   public final CC endGroup(String... xy) {
/*  393 */     switch (xy.length)
/*      */     { default:
/*  395 */         throw new IllegalArgumentException("Illegal argument count: " + xy.length);
/*      */       case 2:
/*  397 */         endGroupY(xy[1]); break;
/*      */       case 1:
/*  399 */         break; }  endGroupX(xy[0]);
/*      */     
/*  401 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC sizeGroupY(String s) {
/*  412 */     this.ver.setSizeGroup(s);
/*  413 */     return this;
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
/*      */   public final CC sizeGroup(String... xy) {
/*  425 */     switch (xy.length)
/*      */     { default:
/*  427 */         throw new IllegalArgumentException("Illegal argument count: " + xy.length);
/*      */       case 2:
/*  429 */         sizeGroupY(xy[1]); break;
/*      */       case 1:
/*  431 */         break; }  sizeGroupX(xy[0]);
/*      */     
/*  433 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC minHeight(String size) {
/*  444 */     this.ver.setSize(LayoutUtil.derive(this.ver.getSize(), ConstraintParser.parseUnitValue(size, false), null, null));
/*  445 */     return this;
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
/*      */   public final CC height(String size) {
/*  457 */     this.ver.setSize(ConstraintParser.parseBoundSize(size, false, false));
/*  458 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC maxHeight(String size) {
/*  469 */     this.ver.setSize(LayoutUtil.derive(this.ver.getSize(), null, null, ConstraintParser.parseUnitValue(size, false)));
/*  470 */     return this;
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
/*      */   public final CC gapY(String before, String after) {
/*  482 */     if (before != null) {
/*  483 */       this.ver.setGapBefore(ConstraintParser.parseBoundSize(before, true, false));
/*      */     }
/*  485 */     if (after != null) {
/*  486 */       this.ver.setGapAfter(ConstraintParser.parseBoundSize(after, true, false));
/*      */     }
/*  488 */     return this;
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
/*      */   public final CC alignY(String align) {
/*  500 */     this.ver.setAlign(ConstraintParser.parseUnitValueOrAlign(align, false, null));
/*  501 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC growPrioY(int p) {
/*  512 */     this.ver.setGrowPriority(p);
/*  513 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC growY() {
/*  524 */     this.ver.setGrow(ResizeConstraint.WEIGHT_100);
/*  525 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC growY(float w) {
/*  536 */     this.ver.setGrow(Float.valueOf(w));
/*  537 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final CC growY(Float w) {
/*  549 */     this.ver.setGrow(w);
/*  550 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC shrinkPrioY(int p) {
/*  561 */     this.ver.setShrinkPriority(p);
/*  562 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC shrinkY(float w) {
/*  573 */     this.ver.setShrink(new Float(w));
/*  574 */     return this;
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
/*      */   public final CC hideMode(int mode) {
/*  589 */     setHideMode(mode);
/*  590 */     return this;
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
/*      */   public final CC id(String s) {
/*  602 */     setId(s);
/*  603 */     return this;
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
/*      */   public final CC tag(String tag) {
/*  615 */     setTag(tag);
/*  616 */     return this;
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
/*      */   public final CC cell(int... colRowWidthHeight) {
/*  634 */     switch (colRowWidthHeight.length)
/*      */     { default:
/*  636 */         throw new IllegalArgumentException("Illegal argument count: " + colRowWidthHeight.length);
/*      */       case 4:
/*  638 */         setSpanY(colRowWidthHeight[3]);
/*      */       case 3:
/*  640 */         setSpanX(colRowWidthHeight[2]);
/*      */       case 2:
/*  642 */         setCellY(colRowWidthHeight[1]); break;
/*      */       case 1:
/*  644 */         break; }  setCellX(colRowWidthHeight[0]);
/*      */     
/*  646 */     return this;
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
/*      */   public final CC span(int... cells) {
/*  664 */     if (cells == null || cells.length == 0) {
/*  665 */       setSpanX(2097051);
/*  666 */       setSpanY(1);
/*  667 */     } else if (cells.length == 1) {
/*  668 */       setSpanX(cells[0]);
/*  669 */       setSpanY(1);
/*      */     } else {
/*  671 */       setSpanX(cells[0]);
/*  672 */       setSpanY(cells[1]);
/*      */     } 
/*  674 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC gap(String... args) {
/*  684 */     switch (args.length)
/*      */     { default:
/*  686 */         throw new IllegalArgumentException("Illegal argument count: " + args.length);
/*      */       case 4:
/*  688 */         gapBottom(args[3]);
/*      */       case 3:
/*  690 */         gapTop(args[2]);
/*      */       case 2:
/*  692 */         gapRight(args[1]); break;
/*      */       case 1:
/*  694 */         break; }  gapLeft(args[0]);
/*      */     
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
/*      */   
/*      */   public final CC gapBefore(String boundsSize) {
/*  708 */     this.hor.setGapBefore(ConstraintParser.parseBoundSize(boundsSize, true, true));
/*  709 */     return this;
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
/*      */   public final CC gapAfter(String boundsSize) {
/*  721 */     this.hor.setGapAfter(ConstraintParser.parseBoundSize(boundsSize, true, true));
/*  722 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC gapTop(String boundsSize) {
/*  732 */     this.ver.setGapBefore(ConstraintParser.parseBoundSize(boundsSize, true, false));
/*  733 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC gapLeft(String boundsSize) {
/*  743 */     this.hor.setGapBefore(ConstraintParser.parseBoundSize(boundsSize, true, true));
/*  744 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC gapBottom(String boundsSize) {
/*  754 */     this.ver.setGapAfter(ConstraintParser.parseBoundSize(boundsSize, true, false));
/*  755 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC gapRight(String boundsSize) {
/*  765 */     this.hor.setGapAfter(ConstraintParser.parseBoundSize(boundsSize, true, true));
/*  766 */     return this;
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
/*      */   public final CC spanY() {
/*  779 */     return spanY(2097051);
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
/*      */   public final CC spanY(int cells) {
/*  791 */     setSpanY(cells);
/*  792 */     return this;
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
/*      */   public final CC spanX() {
/*  805 */     return spanX(2097051);
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
/*      */   public final CC spanX(int cells) {
/*  817 */     setSpanX(cells);
/*  818 */     return this;
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
/*      */   public final CC push() {
/*  833 */     return pushX().pushY();
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
/*      */   public final CC push(Float weightX, Float weightY) {
/*  850 */     return pushX(weightX).pushY(weightY);
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
/*      */   public final CC pushY() {
/*  862 */     return pushY(ResizeConstraint.WEIGHT_100);
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
/*      */   public final CC pushY(Float weight) {
/*  874 */     setPushY(weight);
/*  875 */     return this;
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
/*      */   public final CC pushX() {
/*  887 */     return pushX(ResizeConstraint.WEIGHT_100);
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
/*      */   public final CC pushX(Float weight) {
/*  899 */     setPushX(weight);
/*  900 */     return this;
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
/*      */   public final CC split(int parts) {
/*  912 */     setSplit(parts);
/*  913 */     return this;
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
/*      */   public final CC split() {
/*  926 */     setSplit(2097051);
/*  927 */     return this;
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
/*      */   public final CC skip(int cells) {
/*  939 */     setSkip(cells);
/*  940 */     return this;
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
/*      */   public final CC skip() {
/*  952 */     setSkip(1);
/*  953 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC external() {
/*  964 */     setExternal(true);
/*  965 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC flowX() {
/*  976 */     setFlowX(Boolean.TRUE);
/*  977 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC flowY() {
/*  988 */     setFlowX(Boolean.FALSE);
/*  989 */     return this;
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
/*      */   public final CC grow() {
/* 1002 */     growX();
/* 1003 */     growY();
/* 1004 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC newline() {
/* 1015 */     setNewline(true);
/* 1016 */     return this;
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
/*      */   public final CC newline(String gapSize) {
/* 1030 */     BoundSize bs = ConstraintParser.parseBoundSize(gapSize, true, (this.flowX != null && !this.flowX.booleanValue()));
/* 1031 */     if (bs != null) {
/* 1032 */       setNewlineGapSize(bs);
/*      */     } else {
/* 1034 */       setNewline(true);
/*      */     } 
/* 1036 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC wrap() {
/* 1047 */     setWrap(true);
/* 1048 */     return this;
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
/*      */   public final CC wrap(String gapSize) {
/* 1062 */     BoundSize bs = ConstraintParser.parseBoundSize(gapSize, true, (this.flowX != null && !this.flowX.booleanValue()));
/* 1063 */     if (bs != null) {
/* 1064 */       setWrapGapSize(bs);
/*      */     } else {
/* 1066 */       setWrap(true);
/*      */     } 
/* 1068 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC dockNorth() {
/* 1079 */     setDockSide(0);
/* 1080 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC dockWest() {
/* 1091 */     setDockSide(1);
/* 1092 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC dockSouth() {
/* 1103 */     setDockSide(2);
/* 1104 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CC dockEast() {
/* 1115 */     setDockSide(3);
/* 1116 */     return this;
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
/*      */   public final CC x(String x) {
/* 1130 */     return corrPos(x, 0);
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
/*      */   public final CC y(String y) {
/* 1144 */     return corrPos(y, 1);
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
/*      */   public final CC x2(String x2) {
/* 1158 */     return corrPos(x2, 2);
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
/*      */   public final CC y2(String y2) {
/* 1172 */     return corrPos(y2, 3);
/*      */   }
/*      */ 
/*      */   
/*      */   private final CC corrPos(String uv, int ix) {
/* 1177 */     UnitValue[] b = getPos();
/* 1178 */     if (b == null) {
/* 1179 */       b = new UnitValue[4];
/*      */     }
/* 1181 */     b[ix] = ConstraintParser.parseUnitValue(uv, (ix % 2 == 0));
/* 1182 */     setPos(b);
/*      */     
/* 1184 */     setBoundsInGrid(true);
/* 1185 */     return this;
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
/*      */   public final CC pos(String x, String y) {
/* 1198 */     UnitValue[] b = getPos();
/* 1199 */     if (b == null) {
/* 1200 */       b = new UnitValue[4];
/*      */     }
/* 1202 */     b[0] = ConstraintParser.parseUnitValue(x, true);
/* 1203 */     b[1] = ConstraintParser.parseUnitValue(y, false);
/* 1204 */     setPos(b);
/*      */     
/* 1206 */     setBoundsInGrid(false);
/* 1207 */     return this;
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
/*      */   public final CC pos(String x, String y, String x2, String y2) {
/* 1222 */     setPos(new UnitValue[] {
/* 1223 */           ConstraintParser.parseUnitValue(x, true), 
/* 1224 */           ConstraintParser.parseUnitValue(y, false), 
/* 1225 */           ConstraintParser.parseUnitValue(x2, true), 
/* 1226 */           ConstraintParser.parseUnitValue(y2, false)
/*      */         });
/* 1228 */     setBoundsInGrid(false);
/* 1229 */     return this;
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
/*      */   public final CC pad(int top, int left, int bottom, int right) {
/* 1244 */     setPadding(new UnitValue[] { new UnitValue(top), new UnitValue(left), new UnitValue(bottom), new UnitValue(right) });
/*      */ 
/*      */     
/* 1247 */     return this;
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
/*      */   public final CC pad(String pad) {
/* 1259 */     setPadding((pad != null) ? ConstraintParser.parseInsets(pad, false) : null);
/* 1260 */     return this;
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
/*      */   public DimConstraint getHorizontal() {
/* 1276 */     return this.hor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHorizontal(DimConstraint h) {
/* 1285 */     this.hor = (h != null) ? h : new DimConstraint();
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
/*      */   public DimConstraint getVertical() {
/* 1297 */     return this.ver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVertical(DimConstraint v) {
/* 1306 */     this.ver = (v != null) ? v : new DimConstraint();
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
/*      */   public DimConstraint getDimConstraint(boolean isHor) {
/* 1318 */     return isHor ? this.hor : this.ver;
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
/*      */   public UnitValue[] getPos() {
/* 1334 */     (new UnitValue[4])[0] = this.pos[0]; (new UnitValue[4])[1] = this.pos[1]; (new UnitValue[4])[2] = this.pos[2]; (new UnitValue[4])[3] = this.pos[3]; return (this.pos != null) ? new UnitValue[4] : null;
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
/*      */   public void setPos(UnitValue[] pos) {
/* 1350 */     (new UnitValue[4])[0] = pos[0]; (new UnitValue[4])[1] = pos[1]; (new UnitValue[4])[2] = pos[2]; (new UnitValue[4])[3] = pos[3]; this.pos = (pos != null) ? new UnitValue[4] : null;
/* 1351 */     this.linkTargets = null;
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
/*      */   public boolean isBoundsInGrid() {
/* 1363 */     return this.boundsInGrid;
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
/*      */   public void setBoundsInGrid(boolean b) {
/* 1375 */     this.boundsInGrid = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCellX() {
/* 1385 */     return this.cellX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCellX(int x) {
/* 1396 */     this.cellX = x;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCellY() {
/* 1406 */     return (this.cellX < 0) ? -1 : this.cellY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCellY(int y) {
/* 1417 */     if (y < 0)
/* 1418 */       this.cellX = -1; 
/* 1419 */     this.cellY = Math.max(y, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDockSide() {
/* 1430 */     return this.dock;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDockSide(int side) {
/* 1441 */     if (side < -1 || side > 3)
/* 1442 */       throw new IllegalArgumentException("Illegal dock side: " + side); 
/* 1443 */     this.dock = side;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isExternal() {
/* 1453 */     return this.external;
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
/*      */   public void setExternal(boolean b) {
/* 1466 */     this.external = b;
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
/*      */   public Boolean getFlowX() {
/* 1479 */     return this.flowX;
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
/*      */   public void setFlowX(Boolean b) {
/* 1492 */     this.flowX = b;
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
/*      */   public int getHideMode() {
/* 1505 */     return this.hideMode;
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
/*      */   public void setHideMode(int mode) {
/* 1517 */     if (mode < -1 || mode > 3) {
/* 1518 */       throw new IllegalArgumentException("Wrong hideMode: " + mode);
/*      */     }
/* 1520 */     this.hideMode = mode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getId() {
/* 1531 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setId(String id) {
/* 1542 */     this.id = id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UnitValue[] getPadding() {
/* 1552 */     (new UnitValue[4])[0] = this.padding[0]; (new UnitValue[4])[1] = this.padding[1]; (new UnitValue[4])[2] = this.padding[2]; (new UnitValue[4])[3] = this.padding[3]; return (this.padding != null) ? new UnitValue[4] : null;
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
/*      */   public void setPadding(UnitValue[] sides) {
/* 1564 */     (new UnitValue[4])[0] = sides[0]; (new UnitValue[4])[1] = sides[1]; (new UnitValue[4])[2] = sides[2]; (new UnitValue[4])[3] = sides[3]; this.padding = (sides != null) ? new UnitValue[4] : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UnitValue[] getVisualPadding() {
/* 1574 */     (new UnitValue[4])[0] = this.visualPadding[0]; (new UnitValue[4])[1] = this.visualPadding[1]; (new UnitValue[4])[2] = this.visualPadding[2]; (new UnitValue[4])[3] = this.visualPadding[3]; return (this.visualPadding != null) ? new UnitValue[4] : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVisualPadding(UnitValue[] sides) {
/* 1584 */     (new UnitValue[4])[0] = sides[0]; (new UnitValue[4])[1] = sides[1]; (new UnitValue[4])[2] = sides[2]; (new UnitValue[4])[3] = sides[3]; this.visualPadding = (sides != null) ? new UnitValue[4] : null;
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
/*      */   public int getSkip() {
/* 1596 */     return this.skip;
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
/*      */   public void setSkip(int cells) {
/* 1608 */     this.skip = cells;
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
/*      */   public int getSpanX() {
/* 1621 */     return this.spanX;
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
/*      */   public void setSpanX(int cells) {
/* 1634 */     this.spanX = cells;
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
/*      */   public int getSpanY() {
/* 1647 */     return this.spanY;
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
/*      */   public void setSpanY(int cells) {
/* 1660 */     this.spanY = cells;
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
/*      */   public Float getPushX() {
/* 1672 */     return this.pushX;
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
/*      */   public void setPushX(Float weight) {
/* 1684 */     this.pushX = weight;
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
/*      */   public Float getPushY() {
/* 1696 */     return this.pushY;
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
/*      */   public void setPushY(Float weight) {
/* 1708 */     this.pushY = weight;
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
/*      */   public int getSplit() {
/* 1723 */     return this.split;
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
/*      */   public void setSplit(int parts) {
/* 1738 */     this.split = parts;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTag() {
/* 1749 */     return this.tag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTag(String tag) {
/* 1760 */     this.tag = tag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWrap() {
/* 1770 */     return (this.wrap != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWrap(boolean b) {
/* 1780 */     this.wrap = b ? ((this.wrap == null) ? DEF_GAP : this.wrap) : null;
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
/*      */   public BoundSize getWrapGapSize() {
/* 1792 */     return (this.wrap == DEF_GAP) ? null : this.wrap;
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
/*      */   public void setWrapGapSize(BoundSize s) {
/* 1804 */     this.wrap = (s == null) ? ((this.wrap != null) ? DEF_GAP : null) : s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNewline() {
/* 1814 */     return (this.newline != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNewline(boolean b) {
/* 1824 */     this.newline = b ? ((this.newline == null) ? DEF_GAP : this.newline) : null;
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
/*      */   public BoundSize getNewlineGapSize() {
/* 1836 */     return (this.newline == DEF_GAP) ? null : this.newline;
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
/*      */   public void setNewlineGapSize(BoundSize s) {
/* 1848 */     this.newline = (s == null) ? ((this.newline != null) ? DEF_GAP : null) : s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnimSpec getAnimSpec() {
/* 1856 */     return this.animSpec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object readResolve() throws ObjectStreamException {
/* 1866 */     return LayoutUtil.getSerializedObject(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 1872 */     LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeExternal(ObjectOutput out) throws IOException {
/* 1878 */     if (getClass() == CC.class)
/* 1879 */       LayoutUtil.writeAsXML(out, this); 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\CC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */