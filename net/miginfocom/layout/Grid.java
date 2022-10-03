/*      */ package net.miginfocom.layout;
/*      */ 
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.TreeSet;
/*      */ import java.util.WeakHashMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Grid
/*      */ {
/*      */   public static final boolean TEST_GAPS = true;
/*   46 */   private static final Float[] GROW_100 = new Float[] { ResizeConstraint.WEIGHT_100 };
/*      */   
/*   48 */   private static final DimConstraint DOCK_DIM_CONSTRAINT = new DimConstraint(); private static final int MAX_GRID = 30000; private static final int MAX_DOCK_GRID = 32767;
/*      */   static {
/*   50 */     DOCK_DIM_CONSTRAINT.setGrowPriority(0);
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
/*   64 */   private static final ResizeConstraint GAP_RC_CONST = new ResizeConstraint(200, ResizeConstraint.WEIGHT_100, 50, null);
/*   65 */   private static final ResizeConstraint GAP_RC_CONST_PUSH = new ResizeConstraint(200, ResizeConstraint.WEIGHT_100, 50, ResizeConstraint.WEIGHT_100);
/*      */ 
/*      */ 
/*      */   
/*   69 */   private static final CC DEF_CC = new CC();
/*      */   private final LC lc;
/*      */   private final ContainerWrapper container;
/*      */   private final LinkedHashMap<Integer, Cell> grid;
/*      */   private HashMap<Integer, BoundSize> wrapGapMap; private final TreeSet<Integer> rowIndexes; private final TreeSet<Integer> colIndexes; private final AC rowConstr; private final AC colConstr; private FlowSizeSpec colFlowSpecs; private FlowSizeSpec rowFlowSpecs; private final ArrayList<LinkedDimGroup>[] colGroupLists; private final ArrayList<LinkedDimGroup>[] rowGroupLists; private int[] width; private int[] height; private ArrayList<int[]> debugRects; private HashMap<String, Boolean> linkTargetIDs; private final int dockOffY; private final int dockOffX; private final Float[] pushXs; private final Float[] pushYs; private final ArrayList<LayoutCallback> callbackList; private int lastRefWidth; private int lastRefHeight; private void ensureIndexSizes(int colCount, int rowCount) { int i; for (i = 0; i < colCount; i++)
/*      */       this.colIndexes.add(Integer.valueOf(i));  for (i = 0; i < rowCount; i++)
/*      */       this.rowIndexes.add(Integer.valueOf(i));  } private static CC getCC(ComponentWrapper comp, Map<? extends ComponentWrapper, CC> ccMap) { CC cc = ccMap.get(comp); return (cc != null) ? cc : DEF_CC; } private void addLinkIDs(CC cc) { String[] linkIDs = cc.getLinkTargets(); for (String linkID : linkIDs) {
/*      */       if (this.linkTargetIDs == null)
/*      */         this.linkTargetIDs = new HashMap<>(); 
/*      */       this.linkTargetIDs.put(linkID, null);
/*      */     }  } public void invalidateContainerSize() { this.colFlowSpecs = null;
/*      */     invalidateComponentSizes(); } public Grid(ContainerWrapper container, LC lc, AC rowConstr, AC colConstr, Map<? extends ComponentWrapper, CC> ccMap, ArrayList<LayoutCallback> callbackList) { int j, k;
/*   81 */     this.grid = new LinkedHashMap<>();
/*      */     
/*   83 */     this.wrapGapMap = null;
/*      */ 
/*      */ 
/*      */     
/*   87 */     this.rowIndexes = new TreeSet<>(); this.colIndexes = new TreeSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   95 */     this.colFlowSpecs = null; this.rowFlowSpecs = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  104 */     this.width = null; this.height = null;
/*      */ 
/*      */ 
/*      */     
/*  108 */     this.debugRects = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  116 */     this.linkTargetIDs = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  615 */     this.lastRefWidth = 0; this.lastRefHeight = 0; this.lc = lc; this.rowConstr = rowConstr; this.colConstr = colConstr; this.container = container; this.callbackList = callbackList; int wrap = (lc.getWrapAfter() != 0) ? lc.getWrapAfter() : ((lc.isFlowX() ? colConstr : rowConstr).getConstaints()).length; boolean useVisualPadding = lc.isVisualPadding(); ComponentWrapper[] comps = container.getComponents(); boolean hasTagged = false; boolean hasPushX = false, hasPushY = false; boolean hitEndOfRow = false; int[] cellXY = new int[2]; ArrayList<int[]> spannedRects = (ArrayList)new ArrayList<>(2); DimConstraint[] specs = (lc.isFlowX() ? rowConstr : colConstr).getConstaints(); int sizeGroupsX = 0, sizeGroupsY = 0; int[] dockInsets = null; LinkHandler.clearTemporaryBounds(container.getLayout()); for (int i = 0; i < comps.length; ) { ComponentWrapper comp = comps[i]; CC rootCc = getCC(comp, ccMap); addLinkIDs(rootCc); int hideMode = comp.isVisible() ? -1 : ((rootCc.getHideMode() != -1) ? rootCc.getHideMode() : lc.getHideMode()); if (hideMode == 3) { setLinkedBounds(comp, rootCc, comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight(), rootCc.isExternal()); i++; continue; }  if (rootCc.getHorizontal().getSizeGroup() != null) sizeGroupsX++;  if (rootCc.getVertical().getSizeGroup() != null) sizeGroupsY++;  if (getPos(comp, rootCc) != null || rootCc.isExternal()) { CompWrap cw = new CompWrap(comp, rootCc, hideMode, useVisualPadding); Cell cell1 = this.grid.get((Object)null); if (cell1 == null) { this.grid.put(null, new Cell(cw)); } else { cell1.compWraps.add(cw); }  if (!rootCc.isBoundsInGrid() || rootCc.isExternal()) { setLinkedBounds(comp, rootCc, comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight(), rootCc.isExternal()); i++; continue; }  }  if (rootCc.getDockSide() != -1) { if (dockInsets == null) dockInsets = new int[] { -32767, -32767, 32767, 32767 };  addDockingCell(dockInsets, rootCc.getDockSide(), new CompWrap(comp, rootCc, hideMode, useVisualPadding)); i++; continue; }  Boolean cellFlowX = rootCc.getFlowX(); Cell cell = null; if (rootCc.isNewline()) { wrap(cellXY, rootCc.getNewlineGapSize()); } else if (hitEndOfRow) { wrap(cellXY, null); }  hitEndOfRow = false; boolean isRowInGridMode = (!lc.isNoGrid() && !((DimConstraint)LayoutUtil.getIndexSafe((Object[])specs, lc.isFlowX() ? cellXY[1] : cellXY[0])).isNoGrid()); int cx = rootCc.getCellX(); int cy = rootCc.getCellY(); if ((cx < 0 || cy < 0) && isRowInGridMode && rootCc.getSkip() == 0) { while (!isCellFree(cellXY[1], cellXY[0], spannedRects)) { if (Math.abs(increase(cellXY, 1)) >= wrap) wrap(cellXY, null);  }  } else { if (cx >= 0 && cy >= 0) { if (cy >= 0) { cellXY[0] = cx; cellXY[1] = cy; } else if (lc.isFlowX()) { cellXY[0] = cx; } else { cellXY[1] = cx; }  ensureIndexSizes(cx, cy); }  cell = getCell(cellXY[1], cellXY[0]); }  for (int s = 0, skipCount = rootCc.getSkip(); s < skipCount;) { while (true) { if (Math.abs(increase(cellXY, 1)) >= wrap) wrap(cellXY, null);  if (isCellFree(cellXY[1], cellXY[0], spannedRects)) s++;  }  }  if (cell == null) { int spanx = Math.min((!isRowInGridMode && lc.isFlowX()) ? 2097051 : rootCc.getSpanX(), 30000 - cellXY[0]); int spany = Math.min((!isRowInGridMode && !lc.isFlowX()) ? 2097051 : rootCc.getSpanY(), 30000 - cellXY[1]); cell = new Cell(spanx, spany, (cellFlowX != null) ? cellFlowX.booleanValue() : lc.isFlowX()); setCell(cellXY[1], cellXY[0], cell); if (spanx > 1 || spany > 1) spannedRects.add(new int[] { cellXY[0], cellXY[1], spanx, spany });  }  boolean wrapHandled = false; int splitLeft = isRowInGridMode ? (rootCc.getSplit() - 1) : 2097051; boolean splitExit = false; boolean spanRestOfRow = ((lc.isFlowX() ? rootCc.getSpanX() : rootCc.getSpanY()) == 2097051); for (; splitLeft >= 0 && i < comps.length; splitLeft--) { ComponentWrapper compAdd = comps[i]; CC cc = getCC(compAdd, ccMap); addLinkIDs(cc); boolean visible = compAdd.isVisible(); hideMode = visible ? -1 : ((cc.getHideMode() != -1) ? cc.getHideMode() : lc.getHideMode()); if (cc.isExternal() || hideMode == 3) { i++; splitLeft++; } else { j = hasPushX | (((visible || hideMode > 1) && cc.getPushX() != null) ? 1 : 0); k = hasPushY | (((visible || hideMode > 1) && cc.getPushY() != null) ? 1 : 0); if (cc != rootCc) { if (cc.isNewline() || !cc.isBoundsInGrid() || cc.getDockSide() != -1) break;  if (splitLeft > 0 && cc.getSkip() > 0) { splitExit = true; break; }  }  CompWrap cw = new CompWrap(compAdd, cc, hideMode, useVisualPadding); cell.compWraps.add(cw); Cell cell1 = cell; cell1.hasTagged = cell1.hasTagged | ((cc.getTag() != null)); hasTagged |= cell.hasTagged; if (cc != rootCc) { if (cc.getHorizontal().getSizeGroup() != null) sizeGroupsX++;  if (cc.getVertical().getSizeGroup() != null) sizeGroupsY++;  }  i++; if (cc.isWrap() || (spanRestOfRow && splitLeft == 0)) { if (cc.isWrap()) { wrap(cellXY, cc.getWrapGapSize()); } else { hitEndOfRow = true; }  wrapHandled = true; break; }  }  }  if (!wrapHandled && isRowInGridMode) { int span = lc.isFlowX() ? cell.spanx : cell.spany; if (Math.abs(lc.isFlowX() ? cellXY[0] : cellXY[1]) + span >= wrap) { hitEndOfRow = true; continue; }  increase(cellXY, splitExit ? (span - 1) : span); }  }  if (sizeGroupsX > 0 || sizeGroupsY > 0) { HashMap<String, int[]> sizeGroupMapX = (sizeGroupsX > 0) ? (HashMap)new HashMap<>(sizeGroupsX) : null; HashMap<String, int[]> sizeGroupMapY = (sizeGroupsY > 0) ? (HashMap)new HashMap<>(sizeGroupsY) : null; ArrayList<CompWrap> sizeGroupCWs = new ArrayList<>(Math.max(sizeGroupsX, sizeGroupsY)); for (Cell cell : this.grid.values()) { for (int m = 0; m < cell.compWraps.size(); m++) { CompWrap cw = cell.compWraps.get(m); String sgx = cw.cc.getHorizontal().getSizeGroup(); String sgy = cw.cc.getVertical().getSizeGroup(); if (sgx != null || sgy != null) { if (sgx != null && sizeGroupMapX != null) addToSizeGroup(sizeGroupMapX, sgx, cw.getSizes(true));  if (sgy != null && sizeGroupMapY != null) addToSizeGroup(sizeGroupMapY, sgy, cw.getSizes(false));  sizeGroupCWs.add(cw); }  }  }  for (CompWrap cw : sizeGroupCWs) { if (sizeGroupMapX != null) cw.setForcedSizes(sizeGroupMapX.get(cw.cc.getHorizontal().getSizeGroup()), true);  if (sizeGroupMapY != null) cw.setForcedSizes(sizeGroupMapY.get(cw.cc.getVertical().getSizeGroup()), false);  }  }  if (hasTagged) sortCellsByPlatform(this.grid.values(), container);  boolean ltr = LayoutUtil.isLeftToRight(lc, container); for (Cell cell : this.grid.values()) { ArrayList<CompWrap> cws = cell.compWraps; for (int m = 0, lastI = cws.size() - 1; m <= lastI; m++) { CompWrap cw = cws.get(m); ComponentWrapper cwBef = (m > 0) ? (cws.get(m - 1)).comp : null; ComponentWrapper cwAft = (m < lastI) ? (cws.get(m + 1)).comp : null; String tag = getCC(cw.comp, ccMap).getTag(); CC ccBef = (cwBef != null) ? getCC(cwBef, ccMap) : null; CC ccAft = (cwAft != null) ? getCC(cwAft, ccMap) : null; cw.calcGaps(cwBef, ccBef, cwAft, ccAft, tag, cell.flowx, ltr); }  }  this.dockOffX = getDockInsets(this.colIndexes); this.dockOffY = getDockInsets(this.rowIndexes); ensureIndexSizes(colConstr.getCount(), rowConstr.getCount()); this.colGroupLists = divideIntoLinkedGroups(false); this.rowGroupLists = divideIntoLinkedGroups(true); this.pushXs = (j != 0 || lc.isFillX()) ? getDefaultPushWeights(false) : null; this.pushYs = (k != 0 || lc.isFillY()) ? getDefaultPushWeights(true) : null; if (LayoutUtil.isDesignTime(container)) saveGrid(container, this.grid);  }
/*      */   private void invalidateComponentSizes() { for (Cell cell : this.grid.values()) { for (CompWrap compWrap : cell.compWraps)
/*      */         compWrap.invalidateSizes();  }  }
/*      */   public boolean layout(int[] bounds, UnitValue alignX, UnitValue alignY, boolean debug, boolean notUsed) { return layoutImpl(bounds, alignX, alignY, debug, false); }
/*  619 */   public boolean layout(int[] bounds, UnitValue alignX, UnitValue alignY, boolean debug) { return layoutImpl(bounds, alignX, alignY, debug, false); } private void checkSizeCalcs(int refWidth, int refHeight) { if (this.colFlowSpecs == null) {
/*  620 */       calcGridSizes(refWidth, refHeight);
/*      */     }
/*  622 */     if ((refWidth > 0 && refWidth != this.lastRefWidth) || (refHeight > 0 && refHeight != this.lastRefHeight)) {
/*  623 */       int[] refBounds = { 0, 0, (refWidth > 0) ? refWidth : this.width[1], (refHeight > 0) ? refHeight : this.height[1] };
/*  624 */       layoutImpl(refBounds, null, null, false, true);
/*  625 */       calcGridSizes(refWidth, refHeight);
/*      */     } 
/*      */     
/*  628 */     this.lastRefWidth = refWidth;
/*  629 */     this.lastRefHeight = refHeight; } private boolean layoutImpl(int[] bounds, UnitValue alignX, UnitValue alignY, boolean debug, boolean trialRun) { if (debug) this.debugRects = (ArrayList)new ArrayList<>();  if (this.colFlowSpecs == null) checkSizeCalcs(bounds[2], bounds[3]);  resetLinkValues(true, true); layoutInOneDim(bounds[2], alignX, false, this.pushXs); layoutInOneDim(bounds[3], alignY, true, this.pushYs); HashMap<String, Integer> endGrpXMap = null, endGrpYMap = null; int compCount = this.container.getComponentCount(); boolean addVisualPadding = this.lc.isVisualPadding(); boolean layoutAgain = false; if (compCount > 0) for (int j = 0; j < ((this.linkTargetIDs != null) ? 2 : 1); j++) { boolean doAgain; int count = 0; do { doAgain = false; for (Cell cell : this.grid.values()) { for (CompWrap cw : cell.compWraps) { if (j == 0) { doAgain |= doAbsoluteCorrections(cw, bounds); if (!doAgain) { if (cw.cc.getHorizontal().getEndGroup() != null) endGrpXMap = addToEndGroup(endGrpXMap, cw.cc.getHorizontal().getEndGroup(), cw.x + cw.w);  if (cw.cc.getVertical().getEndGroup() != null) endGrpYMap = addToEndGroup(endGrpYMap, cw.cc.getVertical().getEndGroup(), cw.y + cw.h);  }  if (this.linkTargetIDs != null && (this.linkTargetIDs.containsKey("visual") || this.linkTargetIDs.containsKey("container"))) layoutAgain = true;  }  if (this.linkTargetIDs == null || j == 1) { if (cw.cc.getHorizontal().getEndGroup() != null) cw.w = ((Integer)endGrpXMap.get(cw.cc.getHorizontal().getEndGroup())).intValue() - cw.x;  if (cw.cc.getVertical().getEndGroup() != null) cw.h = ((Integer)endGrpYMap.get(cw.cc.getVertical().getEndGroup())).intValue() - cw.y;  CompWrap compWrap = cw; compWrap.x = compWrap.x + bounds[0]; compWrap = cw; compWrap.y = compWrap.y + bounds[1]; if (!trialRun) cw.transferBounds(addVisualPadding);  if (this.callbackList != null) for (LayoutCallback callback : this.callbackList) callback.correctBounds(cw.comp);   }  }  }  clearGroupLinkBounds(); if (++count > (compCount << 3) + 10) { System.err.println("Unstable cyclic dependency in absolute linked values."); break; }  } while (doAgain); }   if (debug) for (Cell cell : this.grid.values()) { ArrayList<CompWrap> compWraps = cell.compWraps; for (CompWrap cw : compWraps) { LinkedDimGroup hGrp = getGroupContaining(this.colGroupLists, cw); LinkedDimGroup vGrp = getGroupContaining(this.rowGroupLists, cw); if (hGrp != null && vGrp != null) this.debugRects.add(new int[] { LinkedDimGroup.access$1800(hGrp) + bounds[0] - (LinkedDimGroup.access$1900(hGrp) ? LinkedDimGroup.access$2000(hGrp) : 0), LinkedDimGroup.access$1800(vGrp) + bounds[1] - (LinkedDimGroup.access$1900(vGrp) ? LinkedDimGroup.access$2000(vGrp) : 0), LinkedDimGroup.access$2000(hGrp), LinkedDimGroup.access$2000(vGrp) });  }  }   return layoutAgain; } public void paintDebug() { if (this.debugRects != null) { this.container.paintDebugOutline(this.lc.isVisualPadding()); ArrayList<int[]> painted = (ArrayList)new ArrayList<>(); for (int[] r : this.debugRects) { if (!painted.contains(r)) { this.container.paintDebugCell(r[0], r[1], r[2], r[3]); painted.add(r); }  }  for (Cell cell : this.grid.values()) { ArrayList<CompWrap> compWraps = cell.compWraps; for (CompWrap compWrap : compWraps) compWrap.comp.paintDebugOutline(this.lc.isVisualPadding());  }  }  }
/*      */   public ContainerWrapper getContainer() { return this.container; }
/*      */   public final int[] getWidth() { return getWidth(this.lastRefHeight); }
/*      */   public final int[] getWidth(int refHeight) { checkSizeCalcs(this.lastRefWidth, refHeight); return (int[])this.width.clone(); }
/*      */   public final int[] getHeight() { return getHeight(this.lastRefWidth); }
/*      */   public final int[] getHeight(int refWidth) { checkSizeCalcs(refWidth, this.lastRefHeight); return (int[])this.height.clone(); }
/*  635 */   private void calcGridSizes(int refWidth, int refHeight) { FlowSizeSpec colSpecs = calcRowsOrColsSizes(true, refWidth);
/*  636 */     FlowSizeSpec rowSpecs = calcRowsOrColsSizes(false, refHeight);
/*      */     
/*  638 */     this.colFlowSpecs = colSpecs;
/*  639 */     this.rowFlowSpecs = rowSpecs;
/*      */     
/*  641 */     this.width = getMinPrefMaxSumSize(true, colSpecs.sizes);
/*  642 */     this.height = getMinPrefMaxSumSize(false, rowSpecs.sizes);
/*      */     
/*  644 */     if (this.linkTargetIDs == null) {
/*  645 */       resetLinkValues(false, true);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  650 */       layout(new int[] { 0, 0, refWidth, refHeight }, null, null, false);
/*  651 */       resetLinkValues(false, false);
/*      */     } 
/*      */     
/*  654 */     adjustSizeForAbsolute(true);
/*  655 */     adjustSizeForAbsolute(false); }
/*      */ 
/*      */ 
/*      */   
/*      */   private UnitValue[] getPos(ComponentWrapper cw, CC cc) {
/*  660 */     UnitValue[] callbackPos = null;
/*  661 */     if (this.callbackList != null) {
/*  662 */       for (int j = 0; j < this.callbackList.size() && callbackPos == null; j++) {
/*  663 */         callbackPos = ((LayoutCallback)this.callbackList.get(j)).getPosition(cw);
/*      */       }
/*      */     }
/*      */     
/*  667 */     UnitValue[] ccPos = cc.getPos();
/*  668 */     if (callbackPos == null || ccPos == null) {
/*  669 */       return (callbackPos != null) ? callbackPos : ccPos;
/*      */     }
/*      */     
/*  672 */     for (int i = 0; i < 4; i++) {
/*  673 */       UnitValue cbUv = callbackPos[i];
/*  674 */       if (cbUv != null) {
/*  675 */         ccPos[i] = cbUv;
/*      */       }
/*      */     } 
/*  678 */     return ccPos;
/*      */   }
/*      */ 
/*      */   
/*      */   private BoundSize[] getCallbackSize(ComponentWrapper cw) {
/*  683 */     if (this.callbackList != null)
/*  684 */       for (LayoutCallback callback : this.callbackList) {
/*  685 */         BoundSize[] bs = callback.getSize(cw);
/*  686 */         if (bs != null) {
/*  687 */           return bs;
/*      */         }
/*      */       }  
/*  690 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getDockInsets(TreeSet<Integer> set) {
/*  695 */     int c = 0;
/*  696 */     for (Integer i : set) {
/*  697 */       if (i.intValue() < -30000) {
/*  698 */         c++;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  703 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean setLinkedBounds(ComponentWrapper cw, CC cc, int x, int y, int w, int h, boolean external) {
/*  714 */     String id = (cc.getId() != null) ? cc.getId() : cw.getLinkId();
/*  715 */     if (id == null) {
/*  716 */       return false;
/*      */     }
/*  718 */     String gid = null;
/*  719 */     int grIx = id.indexOf('.');
/*  720 */     if (grIx != -1) {
/*  721 */       gid = id.substring(0, grIx);
/*  722 */       id = id.substring(grIx + 1);
/*      */     } 
/*      */     
/*  725 */     Object lay = this.container.getLayout();
/*  726 */     boolean changed = false;
/*  727 */     if (external || (this.linkTargetIDs != null && this.linkTargetIDs.containsKey(id))) {
/*  728 */       changed = LinkHandler.setBounds(lay, id, x, y, w, h, !external, false);
/*      */     }
/*  730 */     if (gid != null && (external || (this.linkTargetIDs != null && this.linkTargetIDs.containsKey(gid)))) {
/*  731 */       if (this.linkTargetIDs == null) {
/*  732 */         this.linkTargetIDs = new HashMap<>(4);
/*      */       }
/*  734 */       this.linkTargetIDs.put(gid, Boolean.TRUE);
/*  735 */       changed |= LinkHandler.setBounds(lay, gid, x, y, w, h, !external, true);
/*      */     } 
/*      */     
/*  738 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int increase(int[] p, int cnt) {
/*  748 */     return this.lc.isFlowX() ? (p[0] = p[0] + cnt) : (p[1] = p[1] + cnt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void wrap(int[] cellXY, BoundSize gapSize) {
/*  757 */     boolean flowx = this.lc.isFlowX();
/*  758 */     cellXY[0] = flowx ? 0 : (cellXY[0] + 1);
/*  759 */     cellXY[1] = flowx ? (cellXY[1] + 1) : 0;
/*      */     
/*  761 */     if (gapSize != null) {
/*  762 */       if (this.wrapGapMap == null) {
/*  763 */         this.wrapGapMap = new HashMap<>(8);
/*      */       }
/*  765 */       this.wrapGapMap.put(Integer.valueOf(cellXY[flowx ? 1 : 0]), gapSize);
/*      */     } 
/*      */ 
/*      */     
/*  769 */     if (flowx) {
/*  770 */       this.rowIndexes.add(Integer.valueOf(cellXY[1]));
/*      */     } else {
/*  772 */       this.colIndexes.add(Integer.valueOf(cellXY[0]));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void sortCellsByPlatform(Collection<Cell> cells, ContainerWrapper parent) {
/*  782 */     String order = PlatformDefaults.getButtonOrder();
/*  783 */     String orderLo = order.toLowerCase();
/*      */     
/*  785 */     int unrelSize = PlatformDefaults.convertToPixels(1.0F, "u", true, 0.0F, parent, null);
/*      */     
/*  787 */     if (unrelSize == -87654312) {
/*  788 */       throw new IllegalArgumentException("'unrelated' not recognized by PlatformDefaults!");
/*      */     }
/*  790 */     int[] gapUnrel = { unrelSize, unrelSize, -2147471302 };
/*  791 */     int[] flGap = { 0, 0, -2147471302 };
/*      */     
/*  793 */     for (Cell cell : cells) {
/*  794 */       if (!cell.hasTagged) {
/*      */         continue;
/*      */       }
/*  797 */       CompWrap prevCW = null;
/*  798 */       boolean nextUnrel = false;
/*  799 */       boolean nextPush = false;
/*  800 */       ArrayList<CompWrap> sortedList = new ArrayList<>(cell.compWraps.size());
/*      */       
/*  802 */       for (int i = 0, iSz = orderLo.length(); i < iSz; i++) {
/*  803 */         char c = orderLo.charAt(i);
/*  804 */         if (c == '+' || c == '_') {
/*  805 */           nextUnrel = true;
/*  806 */           if (c == '+')
/*  807 */             nextPush = true; 
/*      */         } else {
/*  809 */           String tag = PlatformDefaults.getTagForChar(c);
/*  810 */           if (tag != null) {
/*  811 */             for (int j = 0, jSz = cell.compWraps.size(); j < jSz; j++) {
/*  812 */               CompWrap cw = cell.compWraps.get(j);
/*  813 */               if (tag.equals(cw.cc.getTag())) {
/*  814 */                 if (Character.isUpperCase(order.charAt(i))) {
/*  815 */                   cw.adjustMinHorSizeUp((int)PlatformDefaults.getMinimumButtonWidthIncludingPadding(0.0F, parent, cw.comp));
/*      */                 }
/*  817 */                 sortedList.add(cw);
/*      */                 
/*  819 */                 if (nextUnrel) {
/*  820 */                   ((prevCW != null) ? prevCW : cw).mergeGapSizes(gapUnrel, cell.flowx, (prevCW == null));
/*  821 */                   if (nextPush) {
/*  822 */                     cw.forcedPushGaps = 1;
/*  823 */                     nextUnrel = false;
/*  824 */                     nextPush = false;
/*      */                   } 
/*      */                 } 
/*      */ 
/*      */                 
/*  829 */                 if (c == 'u')
/*  830 */                   nextUnrel = true; 
/*  831 */                 prevCW = cw;
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  839 */       if (sortedList.size() > 0) {
/*  840 */         CompWrap cw = sortedList.get(sortedList.size() - 1);
/*  841 */         if (nextUnrel) {
/*  842 */           cw.mergeGapSizes(gapUnrel, cell.flowx, false);
/*  843 */           if (nextPush) {
/*  844 */             CompWrap compWrap = cw; compWrap.forcedPushGaps = compWrap.forcedPushGaps | 0x2;
/*      */           } 
/*      */         } 
/*      */         
/*  848 */         if (cw.cc.getHorizontal().getGapAfter() == null) {
/*  849 */           cw.setGaps(flGap, 3);
/*      */         }
/*  851 */         cw = sortedList.get(0);
/*  852 */         if (cw.cc.getHorizontal().getGapBefore() == null) {
/*  853 */           cw.setGaps(flGap, 1);
/*      */         }
/*      */       } 
/*      */       
/*  857 */       if (cell.compWraps.size() == sortedList.size()) {
/*  858 */         cell.compWraps.clear();
/*      */       } else {
/*  860 */         cell.compWraps.removeAll(sortedList);
/*      */       } 
/*  862 */       cell.compWraps.addAll(sortedList);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private Float[] getDefaultPushWeights(boolean isRows) {
/*  868 */     ArrayList<LinkedDimGroup>[] groupLists = isRows ? this.rowGroupLists : this.colGroupLists;
/*      */     
/*  870 */     Float[] pushWeightArr = GROW_100;
/*  871 */     for (int i = 0, ix = 1; i < groupLists.length; i++, ix += 2) {
/*  872 */       ArrayList<LinkedDimGroup> grps = groupLists[i];
/*  873 */       Float rowPushWeight = null;
/*  874 */       for (LinkedDimGroup grp : grps) {
/*  875 */         for (int c = 0; c < grp._compWraps.size(); c++) {
/*  876 */           CompWrap cw = grp._compWraps.get(c);
/*  877 */           int hideMode = cw.comp.isVisible() ? -1 : ((cw.cc.getHideMode() != -1) ? cw.cc.getHideMode() : this.lc.getHideMode());
/*      */           
/*  879 */           Float pushWeight = (hideMode < 2) ? (isRows ? cw.cc.getPushY() : cw.cc.getPushX()) : null;
/*  880 */           if (rowPushWeight == null || (pushWeight != null && pushWeight.floatValue() > rowPushWeight.floatValue())) {
/*  881 */             rowPushWeight = pushWeight;
/*      */           }
/*      */         } 
/*      */       } 
/*  885 */       if (rowPushWeight != null) {
/*  886 */         if (pushWeightArr == GROW_100)
/*  887 */           pushWeightArr = new Float[(groupLists.length << 1) + 1]; 
/*  888 */         pushWeightArr[ix] = rowPushWeight;
/*      */       } 
/*      */     } 
/*      */     
/*  892 */     return pushWeightArr;
/*      */   }
/*      */ 
/*      */   
/*      */   private void clearGroupLinkBounds() {
/*  897 */     if (this.linkTargetIDs == null) {
/*      */       return;
/*      */     }
/*  900 */     for (Map.Entry<String, Boolean> o : this.linkTargetIDs.entrySet()) {
/*  901 */       if (o.getValue() == Boolean.TRUE) {
/*  902 */         LinkHandler.clearBounds(this.container.getLayout(), o.getKey());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void resetLinkValues(boolean parentSize, boolean compLinks) {
/*  908 */     Object lay = this.container.getLayout();
/*  909 */     if (compLinks) {
/*  910 */       LinkHandler.clearTemporaryBounds(lay);
/*      */     }
/*  912 */     boolean defIns = !hasDocks();
/*      */     
/*  914 */     int parW = parentSize ? this.lc.getWidth().constrain(this.container.getWidth(), getParentSize(this.container, true), this.container) : 0;
/*  915 */     int parH = parentSize ? this.lc.getHeight().constrain(this.container.getHeight(), getParentSize(this.container, false), this.container) : 0;
/*      */     
/*  917 */     int insX = LayoutUtil.getInsets(this.lc, 0, defIns).getPixels(0.0F, this.container, null);
/*  918 */     int insY = LayoutUtil.getInsets(this.lc, 1, defIns).getPixels(0.0F, this.container, null);
/*  919 */     int visW = parW - insX - LayoutUtil.getInsets(this.lc, 2, defIns).getPixels(0.0F, this.container, null);
/*  920 */     int visH = parH - insY - LayoutUtil.getInsets(this.lc, 3, defIns).getPixels(0.0F, this.container, null);
/*      */     
/*  922 */     LinkHandler.setBounds(lay, "visual", insX, insY, visW, visH, true, false);
/*  923 */     LinkHandler.setBounds(lay, "container", 0, 0, parW, parH, true, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static LinkedDimGroup getGroupContaining(ArrayList<LinkedDimGroup>[] groupLists, CompWrap cw) {
/*  934 */     for (ArrayList<LinkedDimGroup> groups : groupLists) {
/*  935 */       for (LinkedDimGroup group : groups) {
/*  936 */         ArrayList<CompWrap> cwList = group._compWraps;
/*  937 */         for (CompWrap aCwList : cwList) {
/*  938 */           if (aCwList == cw)
/*  939 */             return group; 
/*      */         } 
/*      */       } 
/*      */     } 
/*  943 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean doAbsoluteCorrections(CompWrap cw, int[] bounds) {
/*  948 */     boolean changed = false;
/*      */     
/*  950 */     int[] stSz = getAbsoluteDimBounds(cw, bounds[2], true);
/*  951 */     if (stSz != null) {
/*  952 */       cw.setDimBounds(stSz[0], stSz[1], true);
/*      */     }
/*  954 */     stSz = getAbsoluteDimBounds(cw, bounds[3], false);
/*  955 */     if (stSz != null) {
/*  956 */       cw.setDimBounds(stSz[0], stSz[1], false);
/*      */     }
/*      */     
/*  959 */     if (this.linkTargetIDs != null) {
/*  960 */       changed = setLinkedBounds(cw.comp, cw.cc, cw.x, cw.y, cw.w, cw.h, false);
/*      */     }
/*  962 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void adjustSizeForAbsolute(boolean isHor) {
/*  969 */     int[] curSizes = isHor ? this.width : this.height;
/*      */     
/*  971 */     Cell absCell = this.grid.get((Object)null);
/*  972 */     if (absCell == null || absCell.compWraps.size() == 0) {
/*      */       return;
/*      */     }
/*  975 */     ArrayList<CompWrap> cws = absCell.compWraps;
/*      */     
/*  977 */     int maxEnd = 0;
/*  978 */     for (int j = 0, cwSz = absCell.compWraps.size(); j < cwSz + 3; j++) {
/*  979 */       boolean doAgain = false;
/*  980 */       for (int i = 0; i < cwSz; i++) {
/*  981 */         CompWrap cw = cws.get(i);
/*  982 */         int[] stSz = getAbsoluteDimBounds(cw, 0, isHor);
/*  983 */         int end = stSz[0] + stSz[1];
/*  984 */         if (maxEnd < end) {
/*  985 */           maxEnd = end;
/*      */         }
/*      */         
/*  988 */         if (this.linkTargetIDs != null)
/*  989 */           doAgain |= setLinkedBounds(cw.comp, cw.cc, stSz[0], stSz[0], stSz[1], stSz[1], false); 
/*      */       } 
/*  991 */       if (!doAgain) {
/*      */         break;
/*      */       }
/*      */       
/*  995 */       maxEnd = 0;
/*  996 */       clearGroupLinkBounds();
/*      */     } 
/*      */     
/*  999 */     maxEnd += LayoutUtil.getInsets(this.lc, isHor ? 3 : 2, !hasDocks()).getPixels(0.0F, this.container, null);
/*      */     
/* 1001 */     if (curSizes[0] < maxEnd)
/* 1002 */       curSizes[0] = maxEnd; 
/* 1003 */     if (curSizes[1] < maxEnd) {
/* 1004 */       curSizes[1] = maxEnd;
/*      */     }
/*      */   }
/*      */   
/*      */   private int[] getAbsoluteDimBounds(CompWrap cw, int refSize, boolean isHor) {
/* 1009 */     if (cw.cc.isExternal()) {
/* 1010 */       if (isHor) {
/* 1011 */         return new int[] { CompWrap.access$1000(cw).getX(), CompWrap.access$1000(cw).getWidth() };
/*      */       }
/* 1013 */       return new int[] { CompWrap.access$1000(cw).getY(), CompWrap.access$1000(cw).getHeight() };
/*      */     } 
/*      */ 
/*      */     
/* 1017 */     UnitValue[] pad = cw.cc.getPadding();
/*      */ 
/*      */     
/* 1020 */     UnitValue[] pos = getPos(cw.comp, cw.cc);
/* 1021 */     if (pos == null && pad == null) {
/* 1022 */       return null;
/*      */     }
/*      */     
/* 1025 */     int st = isHor ? cw.x : cw.y;
/* 1026 */     int sz = isHor ? cw.w : cw.h;
/*      */ 
/*      */     
/* 1029 */     if (pos != null) {
/* 1030 */       UnitValue stUV = pos[isHor ? 0 : 1];
/* 1031 */       UnitValue endUV = pos[isHor ? 2 : 3];
/*      */       
/* 1033 */       int minSz = cw.getSize(0, isHor);
/* 1034 */       int maxSz = cw.getSize(2, isHor);
/* 1035 */       sz = Math.min(Math.max(cw.getSize(1, isHor), minSz), maxSz);
/*      */       
/* 1037 */       if (stUV != null) {
/* 1038 */         st = stUV.getPixels((stUV.getUnit() == 12) ? sz : refSize, this.container, cw.comp);
/*      */         
/* 1040 */         if (endUV != null) {
/* 1041 */           sz = Math.min(Math.max((isHor ? (cw.x + cw.w) : (cw.y + cw.h)) - st, minSz), maxSz);
/*      */         }
/*      */       } 
/* 1044 */       if (endUV != null) {
/* 1045 */         if (stUV != null) {
/* 1046 */           sz = Math.min(Math.max(endUV.getPixels(refSize, this.container, cw.comp) - st, minSz), maxSz);
/*      */         } else {
/* 1048 */           st = endUV.getPixels(refSize, this.container, cw.comp) - sz;
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1054 */     if (pad != null) {
/* 1055 */       UnitValue uv = pad[isHor ? 1 : 0];
/* 1056 */       int p = (uv != null) ? uv.getPixels(refSize, this.container, cw.comp) : 0;
/* 1057 */       st += p;
/* 1058 */       uv = pad[isHor ? 3 : 2];
/* 1059 */       sz += -p + ((uv != null) ? uv.getPixels(refSize, this.container, cw.comp) : 0);
/*      */     } 
/*      */     
/* 1062 */     return new int[] { st, sz };
/*      */   }
/*      */ 
/*      */   
/*      */   private void layoutInOneDim(int refSize, UnitValue align, boolean isRows, Float[] defaultPushWeights) {
/* 1067 */     if (isRows ? this.lc.isTopToBottom() : LayoutUtil.isLeftToRight(this.lc, this.container)) {  } else {  }  boolean fromEnd = true;
/* 1068 */     DimConstraint[] primDCs = (isRows ? this.rowConstr : this.colConstr).getConstaints();
/* 1069 */     FlowSizeSpec fss = isRows ? this.rowFlowSpecs : this.colFlowSpecs;
/* 1070 */     ArrayList<LinkedDimGroup>[] rowCols = isRows ? this.rowGroupLists : this.colGroupLists;
/*      */     
/* 1072 */     int[] rowColSizes = LayoutUtil.calculateSerial(fss.sizes, fss.resConstsInclGaps, defaultPushWeights, 1, refSize);
/*      */     
/* 1074 */     if (LayoutUtil.isDesignTime(this.container)) {
/* 1075 */       TreeSet<Integer> indexes = isRows ? this.rowIndexes : this.colIndexes;
/* 1076 */       int[] ixArr = new int[indexes.size()];
/* 1077 */       int ix = 0;
/* 1078 */       for (Integer integer : indexes) {
/* 1079 */         ixArr[ix++] = integer.intValue();
/*      */       }
/* 1081 */       putSizesAndIndexes(this.container.getComponent(), rowColSizes, ixArr, isRows);
/*      */     } 
/*      */     
/* 1084 */     int curPos = (align != null) ? align.getPixels((refSize - LayoutUtil.sum(rowColSizes)), this.container, null) : 0;
/*      */     
/* 1086 */     if (fromEnd) {
/* 1087 */       curPos = refSize - curPos;
/*      */     }
/* 1089 */     for (int i = 0; i < rowCols.length; i++) {
/* 1090 */       ArrayList<LinkedDimGroup> linkedGroups = rowCols[i];
/* 1091 */       int scIx = i - (isRows ? this.dockOffY : this.dockOffX);
/*      */       
/* 1093 */       int bIx = i << 1;
/* 1094 */       int bIx2 = bIx + 1;
/*      */       
/* 1096 */       curPos += fromEnd ? -rowColSizes[bIx] : rowColSizes[bIx];
/*      */       
/* 1098 */       DimConstraint primDC = (scIx >= 0) ? primDCs[(scIx >= primDCs.length) ? (primDCs.length - 1) : scIx] : DOCK_DIM_CONSTRAINT;
/*      */       
/* 1100 */       int rowSize = rowColSizes[bIx2];
/*      */       
/* 1102 */       for (LinkedDimGroup group : linkedGroups) {
/* 1103 */         int groupSize = rowSize;
/* 1104 */         if (group.span > 1) {
/* 1105 */           groupSize = LayoutUtil.sum(rowColSizes, bIx2, Math.min((group.span << 1) - 1, rowColSizes.length - bIx2 - 1));
/*      */         }
/* 1107 */         group.layout(primDC, curPos, groupSize, group.span);
/*      */       } 
/*      */       
/* 1110 */       curPos += fromEnd ? -rowSize : rowSize;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void addToSizeGroup(HashMap<String, int[]> sizeGroups, String sizeGroup, int[] size) {
/* 1116 */     int[] sgSize = sizeGroups.get(sizeGroup);
/* 1117 */     if (sgSize == null) {
/* 1118 */       sizeGroups.put(sizeGroup, new int[] { size[0], size[1], size[2] });
/*      */     } else {
/* 1120 */       sgSize[0] = Math.max(size[0], sgSize[0]);
/* 1121 */       sgSize[1] = Math.max(size[1], sgSize[1]);
/* 1122 */       sgSize[2] = Math.min(size[2], sgSize[2]);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static HashMap<String, Integer> addToEndGroup(HashMap<String, Integer> endGroups, String endGroup, int end) {
/* 1128 */     if (endGroup != null) {
/* 1129 */       if (endGroups == null) {
/* 1130 */         endGroups = new HashMap<>(4);
/*      */       }
/* 1132 */       Integer oldEnd = endGroups.get(endGroup);
/* 1133 */       if (oldEnd == null || end > oldEnd.intValue())
/* 1134 */         endGroups.put(endGroup, Integer.valueOf(end)); 
/*      */     } 
/* 1136 */     return endGroups;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private FlowSizeSpec calcRowsOrColsSizes(boolean isHor, int containerSize) {
/* 1146 */     ArrayList<LinkedDimGroup>[] groupsLists = isHor ? this.colGroupLists : this.rowGroupLists;
/* 1147 */     Float[] defPush = isHor ? this.pushXs : this.pushYs;
/*      */     
/* 1149 */     if (containerSize <= 0) {
/* 1150 */       containerSize = isHor ? this.container.getWidth() : this.container.getHeight();
/*      */     }
/* 1152 */     BoundSize cSz = isHor ? this.lc.getWidth() : this.lc.getHeight();
/* 1153 */     if (!cSz.isUnset()) {
/* 1154 */       containerSize = cSz.constrain(containerSize, getParentSize(this.container, isHor), this.container);
/*      */     }
/* 1156 */     DimConstraint[] primDCs = (isHor ? this.colConstr : this.rowConstr).getConstaints();
/* 1157 */     TreeSet<Integer> primIndexes = isHor ? this.colIndexes : this.rowIndexes;
/*      */     
/* 1159 */     int[][] rowColBoundSizes = new int[primIndexes.size()][];
/* 1160 */     HashMap<String, int[]> sizeGroupMap = (HashMap)new HashMap<>(4);
/* 1161 */     DimConstraint[] allDCs = new DimConstraint[primIndexes.size()];
/*      */     
/* 1163 */     Iterator<Integer> primIt = primIndexes.iterator(); int r;
/* 1164 */     for (r = 0; r < rowColBoundSizes.length; r++) {
/* 1165 */       int cellIx = ((Integer)primIt.next()).intValue();
/* 1166 */       int[] rowColSizes = new int[3];
/*      */       
/* 1168 */       if (cellIx >= -30000 && cellIx <= 30000) {
/* 1169 */         allDCs[r] = primDCs[(cellIx >= primDCs.length) ? (primDCs.length - 1) : cellIx];
/*      */       } else {
/* 1171 */         allDCs[r] = DOCK_DIM_CONSTRAINT;
/*      */       } 
/*      */       
/* 1174 */       ArrayList<LinkedDimGroup> groups = groupsLists[r];
/*      */ 
/*      */ 
/*      */       
/* 1178 */       int[] groupSizes = { getTotalGroupsSizeParallel(groups, 0, false), getTotalGroupsSizeParallel(groups, 1, false), 2097051 };
/*      */ 
/*      */       
/* 1181 */       correctMinMax(groupSizes);
/* 1182 */       BoundSize dimSize = allDCs[r].getSize();
/*      */       
/* 1184 */       for (int sType = 0; sType <= 2; sType++) {
/*      */         
/* 1186 */         int rowColSize = groupSizes[sType];
/*      */         
/* 1188 */         UnitValue uv = dimSize.getSize(sType);
/* 1189 */         if (uv != null) {
/*      */           
/* 1191 */           int unit = uv.getUnit();
/* 1192 */           if (unit == 14) {
/* 1193 */             rowColSize = groupSizes[1];
/* 1194 */           } else if (unit == 13) {
/* 1195 */             rowColSize = groupSizes[0];
/* 1196 */           } else if (unit == 15) {
/* 1197 */             rowColSize = groupSizes[2];
/*      */           } else {
/* 1199 */             rowColSize = uv.getPixels(containerSize, this.container, null);
/*      */           } 
/* 1201 */         } else if (cellIx >= -30000 && cellIx <= 30000 && rowColSize == 0) {
/* 1202 */           rowColSize = LayoutUtil.isDesignTime(this.container) ? LayoutUtil.getDesignTimeEmptySize() : 0;
/*      */         } 
/*      */         
/* 1205 */         rowColSizes[sType] = rowColSize;
/*      */       } 
/*      */       
/* 1208 */       correctMinMax(rowColSizes);
/* 1209 */       addToSizeGroup(sizeGroupMap, allDCs[r].getSizeGroup(), rowColSizes);
/*      */       
/* 1211 */       rowColBoundSizes[r] = rowColSizes;
/*      */     } 
/*      */ 
/*      */     
/* 1215 */     if (sizeGroupMap.size() > 0) {
/* 1216 */       for (r = 0; r < rowColBoundSizes.length; r++) {
/* 1217 */         if (allDCs[r].getSizeGroup() != null) {
/* 1218 */           rowColBoundSizes[r] = sizeGroupMap.get(allDCs[r].getSizeGroup());
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 1223 */     ResizeConstraint[] resConstrs = getRowResizeConstraints(allDCs);
/*      */     
/* 1225 */     boolean[] fillInPushGaps = new boolean[allDCs.length + 1];
/* 1226 */     int[][] gapSizes = getRowGaps(allDCs, containerSize, isHor, fillInPushGaps);
/*      */     
/* 1228 */     FlowSizeSpec fss = mergeSizesGapsAndResConstrs(resConstrs, fillInPushGaps, rowColBoundSizes, gapSizes);
/*      */ 
/*      */     
/* 1231 */     adjustMinPrefForSpanningComps(allDCs, defPush, fss, groupsLists);
/*      */     
/* 1233 */     return fss;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getParentSize(ComponentWrapper cw, boolean isHor) {
/* 1238 */     ContainerWrapper p = cw.getParent();
/* 1239 */     return (p != null) ? (isHor ? cw.getWidth() : cw.getHeight()) : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private int[] getMinPrefMaxSumSize(boolean isHor, int[][] sizes) {
/* 1244 */     int[] retSizes = new int[3];
/*      */     
/* 1246 */     BoundSize sz = isHor ? this.lc.getWidth() : this.lc.getHeight();
/*      */     
/* 1248 */     for (int i = 0; i < sizes.length; i++) {
/* 1249 */       if (sizes[i] != null) {
/* 1250 */         int[] size = sizes[i];
/* 1251 */         for (int sType = 0; sType <= 2; sType++) {
/* 1252 */           if (sz.getSize(sType) != null) {
/* 1253 */             if (i == 0)
/* 1254 */               retSizes[sType] = sz.getSize(sType).getPixels(getParentSize(this.container, isHor), this.container, null); 
/*      */           } else {
/* 1256 */             int s = size[sType];
/*      */             
/* 1258 */             if (s != -2147471302) {
/* 1259 */               if (sType == 1) {
/* 1260 */                 int bnd = size[2];
/* 1261 */                 if (bnd != -2147471302 && bnd < s) {
/* 1262 */                   s = bnd;
/*      */                 }
/* 1264 */                 bnd = size[0];
/* 1265 */                 if (bnd > s) {
/* 1266 */                   s = bnd;
/*      */                 }
/*      */               } 
/* 1269 */               retSizes[sType] = retSizes[sType] + s;
/*      */             } 
/*      */ 
/*      */             
/* 1273 */             if (size[2] == -2147471302 || retSizes[2] > 2097051) {
/* 1274 */               retSizes[2] = 2097051;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1280 */     correctMinMax(retSizes);
/*      */     
/* 1282 */     return retSizes;
/*      */   }
/*      */ 
/*      */   
/*      */   private static ResizeConstraint[] getRowResizeConstraints(DimConstraint[] specs) {
/* 1287 */     ResizeConstraint[] resConsts = new ResizeConstraint[specs.length];
/* 1288 */     for (int i = 0; i < resConsts.length; i++)
/* 1289 */       resConsts[i] = (specs[i]).resize; 
/* 1290 */     return resConsts;
/*      */   }
/*      */ 
/*      */   
/*      */   private static ResizeConstraint[] getComponentResizeConstraints(ArrayList<CompWrap> compWraps, boolean isHor) {
/* 1295 */     ResizeConstraint[] resConsts = new ResizeConstraint[compWraps.size()];
/* 1296 */     for (int i = 0; i < resConsts.length; i++) {
/* 1297 */       CC fc = (compWraps.get(i)).cc;
/* 1298 */       resConsts[i] = (fc.getDimConstraint(isHor)).resize;
/*      */ 
/*      */       
/* 1301 */       int dock = fc.getDockSide();
/* 1302 */       if (isHor ? (dock == 0 || dock == 2) : (dock == 1 || dock == 3)) {
/* 1303 */         ResizeConstraint dc = resConsts[i];
/* 1304 */         resConsts[i] = new ResizeConstraint(dc.shrinkPrio, dc.shrink, dc.growPrio, ResizeConstraint.WEIGHT_100);
/*      */       } 
/*      */     } 
/* 1307 */     return resConsts;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean[] getComponentGapPush(ArrayList<CompWrap> compWraps, boolean isHor) {
/* 1313 */     boolean[] barr = new boolean[compWraps.size() + 1];
/* 1314 */     for (int i = 0; i < barr.length; i++) {
/*      */       
/* 1316 */       boolean push = (i > 0 && ((CompWrap)compWraps.get(i - 1)).isPushGap(isHor, false));
/*      */       
/* 1318 */       if (!push && i < barr.length - 1) {
/* 1319 */         push = ((CompWrap)compWraps.get(i)).isPushGap(isHor, true);
/*      */       }
/* 1321 */       barr[i] = push;
/*      */     } 
/* 1323 */     return barr;
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
/*      */   private int[][] getRowGaps(DimConstraint[] specs, int refSize, boolean isHor, boolean[] fillInPushGaps) {
/* 1335 */     BoundSize defGap = isHor ? this.lc.getGridGapX() : this.lc.getGridGapY();
/* 1336 */     if (defGap == null)
/* 1337 */       defGap = isHor ? PlatformDefaults.getGridGapX() : PlatformDefaults.getGridGapY(); 
/* 1338 */     int[] defGapArr = defGap.getPixelSizes(refSize, this.container, null);
/*      */     
/* 1340 */     boolean defIns = !hasDocks();
/*      */     
/* 1342 */     UnitValue firstGap = LayoutUtil.getInsets(this.lc, isHor ? 1 : 0, defIns);
/* 1343 */     UnitValue lastGap = LayoutUtil.getInsets(this.lc, isHor ? 3 : 2, defIns);
/*      */     
/* 1345 */     int[][] retValues = new int[specs.length + 1][];
/*      */     
/* 1347 */     for (int i = 0, wgIx = 0; i < retValues.length; i++) {
/* 1348 */       DimConstraint specBefore = (i > 0) ? specs[i - 1] : null;
/* 1349 */       DimConstraint specAfter = (i < specs.length) ? specs[i] : null;
/*      */ 
/*      */       
/* 1352 */       boolean edgeBefore = (specBefore == DOCK_DIM_CONSTRAINT || specBefore == null);
/* 1353 */       boolean edgeAfter = (specAfter == DOCK_DIM_CONSTRAINT || specAfter == null);
/* 1354 */       if (!edgeBefore || !edgeAfter) {
/*      */ 
/*      */         
/* 1357 */         BoundSize wrapGapSize = (this.wrapGapMap == null || isHor == this.lc.isFlowX()) ? null : this.wrapGapMap.get(Integer.valueOf(wgIx++));
/*      */         
/* 1359 */         if (wrapGapSize == null) {
/*      */           
/* 1361 */           int[] gapBefore = (specBefore != null) ? specBefore.getRowGaps(this.container, null, refSize, false) : null;
/* 1362 */           int[] gapAfter = (specAfter != null) ? specAfter.getRowGaps(this.container, null, refSize, true) : null;
/*      */           
/* 1364 */           if (edgeBefore && gapAfter == null && firstGap != null) {
/*      */             
/* 1366 */             int bef = firstGap.getPixels(refSize, this.container, null);
/* 1367 */             (new int[3])[0] = bef; (new int[3])[1] = bef; (new int[3])[2] = bef; retValues[i] = new int[3];
/*      */           }
/* 1369 */           else if (edgeAfter && gapBefore == null && firstGap != null) {
/*      */             
/* 1371 */             int aft = lastGap.getPixels(refSize, this.container, null);
/* 1372 */             (new int[3])[0] = aft; (new int[3])[1] = aft; (new int[3])[2] = aft; retValues[i] = new int[3];
/*      */           } else {
/*      */             
/* 1375 */             (new int[3])[0] = defGapArr[0]; (new int[3])[1] = defGapArr[1]; (new int[3])[2] = defGapArr[2]; retValues[i] = (gapAfter != gapBefore) ? mergeSizes(gapAfter, gapBefore) : new int[3];
/*      */           } 
/*      */           
/* 1378 */           if ((specBefore != null && specBefore.isGapAfterPush()) || (specAfter != null && specAfter.isGapBeforePush())) {
/* 1379 */             fillInPushGaps[i] = true;
/*      */           }
/*      */         } else {
/* 1382 */           if (wrapGapSize.isUnset()) {
/* 1383 */             (new int[3])[0] = defGapArr[0]; (new int[3])[1] = defGapArr[1]; (new int[3])[2] = defGapArr[2]; retValues[i] = new int[3];
/*      */           } else {
/* 1385 */             retValues[i] = wrapGapSize.getPixelSizes(refSize, this.container, null);
/*      */           } 
/* 1387 */           fillInPushGaps[i] = wrapGapSize.getGapPush();
/*      */         } 
/*      */       } 
/* 1390 */     }  return retValues;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int[][] getGaps(ArrayList<CompWrap> compWraps, boolean isHor) {
/* 1395 */     int compCount = compWraps.size();
/* 1396 */     int[][] retValues = new int[compCount + 1][];
/*      */     
/* 1398 */     retValues[0] = ((CompWrap)compWraps.get(0)).getGaps(isHor, true);
/* 1399 */     for (int i = 0; i < compCount; i++) {
/* 1400 */       int[] gap1 = ((CompWrap)compWraps.get(i)).getGaps(isHor, false);
/* 1401 */       int[] gap2 = (i < compCount - 1) ? ((CompWrap)compWraps.get(i + 1)).getGaps(isHor, true) : null;
/*      */       
/* 1403 */       retValues[i + 1] = mergeSizes(gap1, gap2);
/*      */     } 
/*      */     
/* 1406 */     return retValues;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean hasDocks() {
/* 1411 */     return (this.dockOffX > 0 || this.dockOffY > 0 || ((Integer)this.rowIndexes.last()).intValue() > 30000 || ((Integer)this.colIndexes.last()).intValue() > 30000);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void adjustMinPrefForSpanningComps(DimConstraint[] specs, Float[] defPush, FlowSizeSpec fss, ArrayList<LinkedDimGroup>[] groupsLists) {
/* 1422 */     for (int r = groupsLists.length - 1; r >= 0; r--) {
/* 1423 */       ArrayList<LinkedDimGroup> groups = groupsLists[r];
/*      */       
/* 1425 */       for (LinkedDimGroup group : groups) {
/* 1426 */         if (group.span == 1) {
/*      */           continue;
/*      */         }
/* 1429 */         int[] sizes = group.getMinPrefMax();
/* 1430 */         for (int s = 0; s <= 1; s++) {
/* 1431 */           int cSize = sizes[s];
/* 1432 */           if (cSize != -2147471302) {
/*      */ 
/*      */             
/* 1435 */             int rowSize = 0;
/* 1436 */             int sIx = (r << 1) + 1;
/* 1437 */             int len = Math.min(group.span << 1, fss.sizes.length - sIx) - 1;
/* 1438 */             for (int j = sIx; j < sIx + len; j++) {
/* 1439 */               int sz = fss.sizes[j][s];
/* 1440 */               if (sz != -2147471302) {
/* 1441 */                 rowSize += sz;
/*      */               }
/*      */             } 
/* 1444 */             if (rowSize < cSize && len > 0) {
/* 1445 */               for (int eagerness = 0, newRowSize = 0; eagerness < 4 && newRowSize < cSize; eagerness++) {
/* 1446 */                 newRowSize = fss.expandSizes(specs, defPush, cSize, sIx, len, s, eagerness);
/*      */               }
/*      */             }
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
/*      */   private ArrayList<LinkedDimGroup>[] divideIntoLinkedGroups(boolean isRows) {
/* 1460 */     if (isRows ? this.lc.isTopToBottom() : LayoutUtil.isLeftToRight(this.lc, this.container)) {  } else {  }  boolean fromEnd = true;
/* 1461 */     TreeSet<Integer> primIndexes = isRows ? this.rowIndexes : this.colIndexes;
/* 1462 */     TreeSet<Integer> secIndexes = isRows ? this.colIndexes : this.rowIndexes;
/* 1463 */     DimConstraint[] primDCs = (isRows ? this.rowConstr : this.colConstr).getConstaints();
/*      */ 
/*      */     
/* 1466 */     ArrayList[] arrayOfArrayList = new ArrayList[primIndexes.size()];
/*      */     
/* 1468 */     int gIx = 0;
/* 1469 */     for (Iterator<Integer> iterator = primIndexes.iterator(); iterator.hasNext(); ) { DimConstraint dc; int i = ((Integer)iterator.next()).intValue();
/*      */ 
/*      */       
/* 1472 */       if (i >= -30000 && i <= 30000) {
/* 1473 */         dc = primDCs[(i >= primDCs.length) ? (primDCs.length - 1) : i];
/*      */       } else {
/* 1475 */         dc = DOCK_DIM_CONSTRAINT;
/*      */       } 
/*      */       
/* 1478 */       ArrayList<LinkedDimGroup> groupList = new ArrayList<>(4);
/* 1479 */       arrayOfArrayList[gIx++] = groupList;
/*      */       
/* 1481 */       for (Integer ix : secIndexes) {
/* 1482 */         Cell cell = isRows ? getCell(i, ix.intValue()) : getCell(ix.intValue(), i);
/* 1483 */         if (cell == null || cell.compWraps.size() == 0) {
/*      */           continue;
/*      */         }
/* 1486 */         int span = isRows ? cell.spany : cell.spanx;
/* 1487 */         if (span > 1) {
/* 1488 */           span = convertSpanToSparseGrid(i, span, primIndexes);
/*      */         }
/* 1490 */         boolean isPar = (cell.flowx == isRows);
/*      */         
/* 1492 */         if ((!isPar && cell.compWraps.size() > 1) || span > 1) {
/*      */           
/* 1494 */           int linkType = isPar ? 1 : 0;
/* 1495 */           LinkedDimGroup lg = new LinkedDimGroup("p," + ix, span, linkType, !isRows, fromEnd);
/* 1496 */           lg.setCompWraps(cell.compWraps);
/* 1497 */           groupList.add(lg); continue;
/*      */         } 
/* 1499 */         for (int cwIx = 0; cwIx < cell.compWraps.size(); cwIx++) {
/* 1500 */           CompWrap cw = cell.compWraps.get(cwIx);
/* 1501 */           boolean rowBaselineAlign = (isRows && this.lc.isTopToBottom() && dc.getAlignOrDefault(!isRows) == UnitValue.BASELINE_IDENTITY);
/* 1502 */           boolean isBaseline = (isRows && cw.isBaselineAlign(rowBaselineAlign));
/*      */           
/* 1504 */           String linkCtx = isBaseline ? "baseline" : null;
/*      */ 
/*      */           
/* 1507 */           boolean foundList = false;
/* 1508 */           for (int glIx = 0, lastGl = groupList.size() - 1; glIx <= lastGl; glIx++) {
/* 1509 */             LinkedDimGroup group = groupList.get(glIx);
/* 1510 */             if (group.linkCtx == linkCtx || (linkCtx != null && linkCtx.equals(group.linkCtx))) {
/* 1511 */               group.addCompWrap(cw);
/* 1512 */               foundList = true;
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/*      */           
/* 1518 */           if (!foundList) {
/* 1519 */             int linkType = isBaseline ? 2 : 1;
/* 1520 */             LinkedDimGroup lg = new LinkedDimGroup(linkCtx, 1, linkType, !isRows, fromEnd);
/* 1521 */             lg.addCompWrap(cw);
/* 1522 */             groupList.add(lg);
/*      */           } 
/*      */         } 
/*      */       }  }
/*      */ 
/*      */     
/* 1528 */     return (ArrayList<LinkedDimGroup>[])arrayOfArrayList;
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
/*      */   private static int convertSpanToSparseGrid(int curIx, int span, TreeSet<Integer> indexes) {
/* 1541 */     int lastIx = curIx + span;
/* 1542 */     int retSpan = 1;
/*      */     
/* 1544 */     for (Integer ix : indexes) {
/* 1545 */       if (ix.intValue() <= curIx) {
/*      */         continue;
/*      */       }
/* 1548 */       if (ix.intValue() >= lastIx) {
/*      */         break;
/*      */       }
/* 1551 */       retSpan++;
/*      */     } 
/* 1553 */     return retSpan;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isCellFree(int r, int c, ArrayList<int[]> occupiedRects) {
/* 1558 */     if (getCell(r, c) != null) {
/* 1559 */       return false;
/*      */     }
/* 1561 */     for (int[] rect : occupiedRects) {
/* 1562 */       if (rect[0] <= c && rect[1] <= r && rect[0] + rect[2] > c && rect[1] + rect[3] > r)
/* 1563 */         return false; 
/*      */     } 
/* 1565 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private Cell getCell(int r, int c) {
/* 1570 */     return this.grid.get(Integer.valueOf((r << 16) + (c & 0xFFFF)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void setCell(int r, int c, Cell cell) {
/* 1575 */     if (c < 0 || r < 0) {
/* 1576 */       throw new IllegalArgumentException("Cell position cannot be negative. row: " + r + ", col: " + c);
/*      */     }
/* 1578 */     if (c > 30000 || r > 30000) {
/* 1579 */       throw new IllegalArgumentException("Cell position out of bounds. Out of cells. row: " + r + ", col: " + c);
/*      */     }
/* 1581 */     this.rowIndexes.add(Integer.valueOf(r));
/* 1582 */     this.colIndexes.add(Integer.valueOf(c));
/*      */     
/* 1584 */     this.grid.put(Integer.valueOf((r << 16) + (c & 0xFFFF)), cell);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addDockingCell(int[] dockInsets, int side, CompWrap cw) {
/* 1594 */     int r, c, spanx = 1, spany = 1;
/* 1595 */     switch (side) {
/*      */       case 0:
/*      */       case 2:
/* 1598 */         dockInsets[0] = dockInsets[0] + 1; dockInsets[2] = dockInsets[2] - 1; r = (side == 0) ? dockInsets[0] : dockInsets[2];
/* 1599 */         c = dockInsets[1];
/* 1600 */         spanx = dockInsets[3] - dockInsets[1] + 1;
/* 1601 */         this.colIndexes.add(Integer.valueOf(dockInsets[3]));
/*      */         break;
/*      */       
/*      */       case 1:
/*      */       case 3:
/* 1606 */         dockInsets[1] = dockInsets[1] + 1; dockInsets[3] = dockInsets[3] - 1; c = (side == 1) ? dockInsets[1] : dockInsets[3];
/* 1607 */         r = dockInsets[0];
/* 1608 */         spany = dockInsets[2] - dockInsets[0] + 1;
/* 1609 */         this.rowIndexes.add(Integer.valueOf(dockInsets[2]));
/*      */         break;
/*      */       
/*      */       default:
/* 1613 */         throw new IllegalArgumentException("Internal error 123.");
/*      */     } 
/*      */     
/* 1616 */     this.rowIndexes.add(Integer.valueOf(r));
/* 1617 */     this.colIndexes.add(Integer.valueOf(c));
/*      */     
/* 1619 */     this.grid.put(Integer.valueOf((r << 16) + (c & 0xFFFF)), new Cell(cw, spanx, spany, (spanx > 1)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Cell
/*      */   {
/*      */     private final int spanx;
/*      */     private final int spany;
/*      */     private final boolean flowx;
/* 1628 */     private final ArrayList<Grid.CompWrap> compWraps = new ArrayList<>(2);
/*      */     
/*      */     private boolean hasTagged = false;
/*      */ 
/*      */     
/*      */     private Cell(Grid.CompWrap cw) {
/* 1634 */       this(cw, 1, 1, true);
/*      */     }
/*      */ 
/*      */     
/*      */     private Cell(int spanx, int spany, boolean flowx) {
/* 1639 */       this((Grid.CompWrap)null, spanx, spany, flowx);
/*      */     }
/*      */ 
/*      */     
/*      */     private Cell(Grid.CompWrap cw, int spanx, int spany, boolean flowx) {
/* 1644 */       if (cw != null)
/* 1645 */         this.compWraps.add(cw); 
/* 1646 */       this.spanx = spanx;
/* 1647 */       this.spany = spany;
/* 1648 */       this.flowx = flowx;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class LinkedDimGroup
/*      */   {
/*      */     private static final int TYPE_SERIAL = 0;
/*      */     
/*      */     private static final int TYPE_PARALLEL = 1;
/*      */     
/*      */     private static final int TYPE_BASELINE = 2;
/*      */     private final String linkCtx;
/*      */     private final int span;
/*      */     private final int linkType;
/*      */     private final boolean isHor;
/*      */     private final boolean fromEnd;
/* 1665 */     private final ArrayList<Grid.CompWrap> _compWraps = new ArrayList<>(4);
/*      */     
/* 1667 */     private int lStart = 0, lSize = 0;
/*      */ 
/*      */     
/*      */     private LinkedDimGroup(String linkCtx, int span, int linkType, boolean isHor, boolean fromEnd) {
/* 1671 */       this.linkCtx = linkCtx;
/* 1672 */       this.span = span;
/* 1673 */       this.linkType = linkType;
/* 1674 */       this.isHor = isHor;
/* 1675 */       this.fromEnd = fromEnd;
/*      */     }
/*      */ 
/*      */     
/*      */     private void addCompWrap(Grid.CompWrap cw) {
/* 1680 */       this._compWraps.add(cw);
/*      */     }
/*      */ 
/*      */     
/*      */     private void setCompWraps(ArrayList<Grid.CompWrap> cws) {
/* 1685 */       if (this._compWraps != cws) {
/* 1686 */         this._compWraps.clear();
/* 1687 */         this._compWraps.addAll(cws);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void layout(DimConstraint dc, int start, int size, int spanCount) {
/* 1693 */       this.lStart = start;
/* 1694 */       this.lSize = size;
/*      */       
/* 1696 */       if (this._compWraps.isEmpty()) {
/*      */         return;
/*      */       }
/* 1699 */       ContainerWrapper parent = (this._compWraps.get(0)).comp.getParent();
/* 1700 */       if (this.linkType == 1) {
/* 1701 */         Grid.layoutParallel(parent, this._compWraps, dc, start, size, this.isHor, this.fromEnd);
/* 1702 */       } else if (this.linkType == 2) {
/* 1703 */         Grid.layoutBaseline(parent, this._compWraps, dc, start, size, 1, spanCount);
/*      */       } else {
/* 1705 */         Grid.layoutSerial(parent, this._compWraps, dc, start, size, this.isHor, spanCount, this.fromEnd);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int[] getMinPrefMax() {
/* 1714 */       int[] sizes = new int[3];
/* 1715 */       if (!this._compWraps.isEmpty()) {
/* 1716 */         for (int sType = 0; sType <= 1; sType++) {
/* 1717 */           if (this.linkType == 1) {
/* 1718 */             sizes[sType] = Grid.getTotalSizeParallel(this._compWraps, sType, this.isHor);
/* 1719 */           } else if (this.linkType == 2) {
/* 1720 */             Grid.AboveBelow aboveBelow = Grid.getBaselineAboveBelow(this._compWraps, sType, false);
/* 1721 */             sizes[sType] = aboveBelow.sum();
/*      */           } else {
/* 1723 */             sizes[sType] = Grid.getTotalSizeSerial(this._compWraps, sType, this.isHor);
/*      */           } 
/*      */         } 
/* 1726 */         sizes[2] = 2097051;
/*      */       } 
/* 1728 */       return sizes;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private final class CompWrap
/*      */   {
/*      */     private final ComponentWrapper comp;
/*      */     
/*      */     private final CC cc;
/*      */     
/*      */     private final int eHideMode;
/*      */     
/*      */     private final boolean useVisualPadding;
/*      */     
/*      */     private boolean sizesOk = false;
/*      */     
/*      */     private boolean isAbsolute;
/*      */     
/*      */     private int[][] gaps;
/* 1748 */     private final int[] horSizes = new int[3];
/* 1749 */     private final int[] verSizes = new int[3];
/*      */     
/* 1751 */     private int x = -2147471302, y = -2147471302, w = -2147471302, h = -2147471302;
/*      */     
/* 1753 */     private int forcedPushGaps = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private CompWrap(ComponentWrapper c, CC cc, int eHideMode, boolean useVisualPadding) {
/* 1763 */       this.comp = c;
/* 1764 */       this.cc = cc;
/* 1765 */       this.eHideMode = eHideMode;
/* 1766 */       this.useVisualPadding = useVisualPadding;
/* 1767 */       this.isAbsolute = (cc.getHorizontal().getSize().isAbsolute() && cc.getVertical().getSize().isAbsolute());
/*      */       
/* 1769 */       if (eHideMode > 1) {
/* 1770 */         this.gaps = new int[4][];
/* 1771 */         for (int i = 0; i < this.gaps.length; i++) {
/* 1772 */           this.gaps[i] = new int[3];
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     private int[] getSizes(boolean isHor) {
/* 1778 */       validateSize();
/* 1779 */       return isHor ? this.horSizes : this.verSizes;
/*      */     }
/*      */ 
/*      */     
/*      */     private void validateSize() {
/* 1784 */       BoundSize[] callbackSz = Grid.this.getCallbackSize(this.comp);
/*      */       
/* 1786 */       if (this.isAbsolute && this.sizesOk && callbackSz == null) {
/*      */         return;
/*      */       }
/* 1789 */       if (this.eHideMode <= 0) {
/* 1790 */         int contentBias = this.comp.getContentBias();
/*      */         
/* 1792 */         int sizeHint = (contentBias == -1) ? -1 : ((contentBias == 0) ? ((this.w != -2147471302) ? this.w : this.comp.getWidth()) : ((this.h != -2147471302) ? this.h : this.comp.getHeight()));
/*      */         
/* 1794 */         BoundSize hBS = (callbackSz != null && callbackSz[0] != null) ? callbackSz[0] : this.cc.getHorizontal().getSize();
/* 1795 */         BoundSize vBS = (callbackSz != null && callbackSz[1] != null) ? callbackSz[1] : this.cc.getVertical().getSize();
/*      */         
/* 1797 */         for (int i = 0; i <= 2; i++) {
/* 1798 */           switch (contentBias) {
/*      */             
/*      */             default:
/* 1801 */               this.horSizes[i] = getSize(hBS, i, true, this.useVisualPadding, -1);
/* 1802 */               this.verSizes[i] = getSize(vBS, i, false, this.useVisualPadding, -1);
/*      */               break;
/*      */             case 0:
/* 1805 */               this.horSizes[i] = getSize(hBS, i, true, this.useVisualPadding, -1);
/* 1806 */               this.verSizes[i] = getSize(vBS, i, false, this.useVisualPadding, (sizeHint > 0) ? sizeHint : this.horSizes[i]);
/*      */               break;
/*      */             case 1:
/* 1809 */               this.verSizes[i] = getSize(vBS, i, false, this.useVisualPadding, -1);
/* 1810 */               this.horSizes[i] = getSize(hBS, i, true, this.useVisualPadding, (sizeHint > 0) ? sizeHint : this.verSizes[i]);
/*      */               break;
/*      */           } 
/*      */         
/*      */         } 
/* 1815 */         Grid.correctMinMax(this.horSizes);
/* 1816 */         Grid.correctMinMax(this.verSizes);
/*      */       } else {
/* 1818 */         Arrays.fill(this.horSizes, 0);
/* 1819 */         Arrays.fill(this.verSizes, 0);
/*      */       } 
/* 1821 */       this.sizesOk = true;
/*      */     }
/*      */ 
/*      */     
/*      */     private int getSize(BoundSize uvs, int sizeType, boolean isHor, boolean useVP, int sizeHint) {
/*      */       int size;
/* 1827 */       if (uvs == null || uvs.getSize(sizeType) == null) {
/* 1828 */         switch (sizeType) {
/*      */           case 0:
/* 1830 */             size = isHor ? this.comp.getMinimumWidth(sizeHint) : this.comp.getMinimumHeight(sizeHint);
/*      */             break;
/*      */           case 1:
/* 1833 */             size = isHor ? this.comp.getPreferredWidth(sizeHint) : this.comp.getPreferredHeight(sizeHint);
/*      */             break;
/*      */           default:
/* 1836 */             size = isHor ? this.comp.getMaximumWidth(sizeHint) : this.comp.getMaximumHeight(sizeHint);
/*      */             break;
/*      */         } 
/* 1839 */         if (useVP) {
/*      */           
/* 1841 */           int[] visualPadding = this.comp.getVisualPadding();
/*      */ 
/*      */           
/* 1844 */           if (visualPadding != null && visualPadding.length > 0)
/* 1845 */             size -= isHor ? (visualPadding[1] + visualPadding[3]) : (visualPadding[0] + visualPadding[2]); 
/*      */         } 
/*      */       } else {
/* 1848 */         ContainerWrapper par = this.comp.getParent();
/* 1849 */         float refValue = isHor ? par.getWidth() : par.getHeight();
/* 1850 */         size = uvs.getSize(sizeType).getPixels(refValue, par, this.comp);
/*      */       } 
/* 1852 */       return size;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void calcGaps(ComponentWrapper before, CC befCC, ComponentWrapper after, CC aftCC, String tag, boolean flowX, boolean isLTR) {
/* 1858 */       ContainerWrapper par = this.comp.getParent();
/* 1859 */       int parW = par.getWidth();
/* 1860 */       int parH = par.getHeight();
/*      */       
/* 1862 */       BoundSize befGap = (before != null) ? (flowX ? befCC.getHorizontal() : befCC.getVertical()).getGapAfter() : null;
/* 1863 */       BoundSize aftGap = (after != null) ? (flowX ? aftCC.getHorizontal() : aftCC.getVertical()).getGapBefore() : null;
/*      */       
/* 1865 */       mergeGapSizes(this.cc.getVertical().getComponentGaps(par, this.comp, befGap, flowX ? null : before, tag, parH, 0, isLTR), false, true);
/* 1866 */       mergeGapSizes(this.cc.getHorizontal().getComponentGaps(par, this.comp, befGap, flowX ? before : null, tag, parW, 1, isLTR), true, true);
/* 1867 */       mergeGapSizes(this.cc.getVertical().getComponentGaps(par, this.comp, aftGap, flowX ? null : after, tag, parH, 2, isLTR), false, false);
/* 1868 */       mergeGapSizes(this.cc.getHorizontal().getComponentGaps(par, this.comp, aftGap, flowX ? after : null, tag, parW, 3, isLTR), true, false);
/*      */     }
/*      */ 
/*      */     
/*      */     private void setDimBounds(int start, int size, boolean isHor) {
/* 1873 */       if (isHor) {
/* 1874 */         if (start != this.x || this.w != size) {
/* 1875 */           this.x = start;
/* 1876 */           this.w = size;
/* 1877 */           if (this.comp.getContentBias() == 0) {
/* 1878 */             invalidateSizes();
/*      */           }
/*      */         } 
/* 1881 */       } else if (start != this.y || this.h != size) {
/* 1882 */         this.y = start;
/* 1883 */         this.h = size;
/* 1884 */         if (this.comp.getContentBias() == 1) {
/* 1885 */           invalidateSizes();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void invalidateSizes() {
/* 1892 */       this.sizesOk = false;
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean isPushGap(boolean isHor, boolean isBefore) {
/* 1897 */       if (isHor && ((isBefore ? 1 : 2) & this.forcedPushGaps) != 0) {
/* 1898 */         return true;
/*      */       }
/* 1900 */       DimConstraint dc = this.cc.getDimConstraint(isHor);
/* 1901 */       BoundSize s = isBefore ? dc.getGapBefore() : dc.getGapAfter();
/* 1902 */       return (s != null && s.getGapPush());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void transferBounds(boolean addVisualPadding) {
/* 1909 */       if (this.cc.isExternal()) {
/*      */         return;
/*      */       }
/* 1912 */       int compX = this.x;
/* 1913 */       int compY = this.y;
/* 1914 */       int compW = this.w;
/* 1915 */       int compH = this.h;
/*      */       
/* 1917 */       if (addVisualPadding) {
/*      */         
/* 1919 */         int[] visualPadding = this.comp.getVisualPadding();
/* 1920 */         if (visualPadding != null) {
/*      */           
/* 1922 */           compX -= visualPadding[1];
/* 1923 */           compY -= visualPadding[0];
/* 1924 */           compW += visualPadding[1] + visualPadding[3];
/* 1925 */           compH += visualPadding[0] + visualPadding[2];
/*      */         } 
/*      */       } 
/*      */       
/* 1929 */       this.comp.setBounds(compX, compY, compW, compH);
/*      */     }
/*      */ 
/*      */     
/*      */     private void setForcedSizes(int[] sizes, boolean isHor) {
/* 1934 */       if (sizes == null) {
/*      */         return;
/*      */       }
/* 1937 */       System.arraycopy(sizes, 0, getSizes(isHor), 0, 3);
/* 1938 */       this.sizesOk = true;
/*      */     }
/*      */ 
/*      */     
/*      */     private void setGaps(int[] minPrefMax, int ix) {
/* 1943 */       if (this.gaps == null) {
/* 1944 */         this.gaps = new int[][] { null, null, null, null };
/*      */       }
/* 1946 */       this.gaps[ix] = minPrefMax;
/*      */     }
/*      */ 
/*      */     
/*      */     private void mergeGapSizes(int[] sizes, boolean isHor, boolean isTL) {
/* 1951 */       if (this.gaps == null) {
/* 1952 */         this.gaps = new int[][] { null, null, null, null };
/*      */       }
/* 1954 */       if (sizes == null) {
/*      */         return;
/*      */       }
/* 1957 */       int gapIX = getGapIx(isHor, isTL);
/* 1958 */       int[] oldGaps = this.gaps[gapIX];
/* 1959 */       if (oldGaps == null) {
/* 1960 */         oldGaps = new int[] { 0, 0, 2097051 };
/* 1961 */         this.gaps[gapIX] = oldGaps;
/*      */       } 
/*      */       
/* 1964 */       oldGaps[0] = Math.max(sizes[0], oldGaps[0]);
/* 1965 */       oldGaps[1] = Math.max(sizes[1], oldGaps[1]);
/* 1966 */       oldGaps[2] = Math.min(sizes[2], oldGaps[2]);
/*      */     }
/*      */ 
/*      */     
/*      */     private int getGapIx(boolean isHor, boolean isTL) {
/* 1971 */       return isHor ? (isTL ? 1 : 3) : (isTL ? 0 : 2);
/*      */     }
/*      */ 
/*      */     
/*      */     private int getSizeInclGaps(int sizeType, boolean isHor) {
/* 1976 */       return filter(sizeType, getGapBefore(sizeType, isHor) + getSize(sizeType, isHor) + getGapAfter(sizeType, isHor));
/*      */     }
/*      */ 
/*      */     
/*      */     private int getSize(int sizeType, boolean isHor) {
/* 1981 */       return filter(sizeType, getSizes(isHor)[sizeType]);
/*      */     }
/*      */ 
/*      */     
/*      */     private int getGapBefore(int sizeType, boolean isHor) {
/* 1986 */       int[] gaps = getGaps(isHor, true);
/* 1987 */       return (gaps != null) ? filter(sizeType, gaps[sizeType]) : 0;
/*      */     }
/*      */ 
/*      */     
/*      */     private int getGapAfter(int sizeType, boolean isHor) {
/* 1992 */       int[] gaps = getGaps(isHor, false);
/* 1993 */       return (gaps != null) ? filter(sizeType, gaps[sizeType]) : 0;
/*      */     }
/*      */ 
/*      */     
/*      */     private int[] getGaps(boolean isHor, boolean isTL) {
/* 1998 */       return this.gaps[getGapIx(isHor, isTL)];
/*      */     }
/*      */ 
/*      */     
/*      */     private int filter(int sizeType, int size) {
/* 2003 */       if (size == -2147471302)
/* 2004 */         return (sizeType != 2) ? 0 : 2097051; 
/* 2005 */       return Grid.constrainSize(size);
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean isBaselineAlign(boolean defValue) {
/* 2010 */       Float g = this.cc.getVertical().getGrow();
/* 2011 */       if (g != null && g.intValue() != 0) {
/* 2012 */         return false;
/*      */       }
/* 2014 */       UnitValue al = this.cc.getVertical().getAlign();
/* 2015 */       return (((al != null) ? (al == UnitValue.BASELINE_IDENTITY) : defValue) && this.comp.hasBaseline());
/*      */     }
/*      */ 
/*      */     
/*      */     private int getBaseline(int sizeType) {
/* 2020 */       return this.comp.getBaseline(getSize(sizeType, true), getSize(sizeType, false));
/*      */     }
/*      */ 
/*      */     
/*      */     void adjustMinHorSizeUp(int minSize) {
/* 2025 */       int[] sz = getSizes(true);
/* 2026 */       if (sz[0] < minSize)
/* 2027 */         sz[0] = minSize; 
/* 2028 */       Grid.correctMinMax(sz);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void layoutBaseline(ContainerWrapper parent, ArrayList<CompWrap> compWraps, DimConstraint dc, int start, int size, int sizeType, int spanCount) {
/* 2038 */     AboveBelow aboveBelow = getBaselineAboveBelow(compWraps, sizeType, true);
/* 2039 */     int blRowSize = aboveBelow.sum();
/*      */     
/* 2041 */     CC cc = (compWraps.get(0)).cc;
/*      */ 
/*      */     
/* 2044 */     UnitValue align = cc.getVertical().getAlign();
/* 2045 */     if (spanCount == 1 && align == null)
/* 2046 */       align = dc.getAlignOrDefault(false); 
/* 2047 */     if (align == UnitValue.BASELINE_IDENTITY) {
/* 2048 */       align = UnitValue.CENTER;
/*      */     }
/* 2050 */     int offset = start + aboveBelow.maxAbove + ((align != null) ? Math.max(0, align.getPixels((size - blRowSize), parent, null)) : 0);
/* 2051 */     for (CompWrap cw : compWraps) {
/* 2052 */       CompWrap compWrap1 = cw; compWrap1.y = compWrap1.y + offset;
/* 2053 */       if (cw.y + cw.h > start + size) {
/* 2054 */         cw.h = start + size - cw.y;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void layoutSerial(ContainerWrapper parent, ArrayList<CompWrap> compWraps, DimConstraint dc, int start, int size, boolean isHor, int spanCount, boolean fromEnd) {
/* 2060 */     FlowSizeSpec fss = mergeSizesGapsAndResConstrs(
/* 2061 */         getComponentResizeConstraints(compWraps, isHor), 
/* 2062 */         getComponentGapPush(compWraps, isHor), 
/* 2063 */         getComponentSizes(compWraps, isHor), 
/* 2064 */         getGaps(compWraps, isHor));
/*      */     
/* 2066 */     Float[] pushW = dc.isFill() ? GROW_100 : null;
/* 2067 */     int[] sizes = LayoutUtil.calculateSerial(fss.sizes, fss.resConstsInclGaps, pushW, 1, size);
/* 2068 */     setCompWrapBounds(parent, sizes, compWraps, dc.getAlignOrDefault(isHor), start, size, isHor, fromEnd);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setCompWrapBounds(ContainerWrapper parent, int[] allSizes, ArrayList<CompWrap> compWraps, UnitValue rowAlign, int start, int size, boolean isHor, boolean fromEnd) {
/* 2073 */     int totSize = LayoutUtil.sum(allSizes);
/* 2074 */     CC cc = (compWraps.get(0)).cc;
/* 2075 */     UnitValue align = correctAlign(cc, rowAlign, isHor, fromEnd);
/*      */     
/* 2077 */     int cSt = start;
/* 2078 */     int slack = size - totSize;
/* 2079 */     if (slack > 0 && align != null) {
/* 2080 */       int al = Math.min(slack, Math.max(0, align.getPixels(slack, parent, null)));
/* 2081 */       cSt += fromEnd ? -al : al;
/*      */     } 
/*      */     
/* 2084 */     for (int i = 0, bIx = 0, iSz = compWraps.size(); i < iSz; i++) {
/* 2085 */       CompWrap cw = compWraps.get(i);
/* 2086 */       if (fromEnd) {
/* 2087 */         cSt -= allSizes[bIx++];
/* 2088 */         cw.setDimBounds(cSt - allSizes[bIx], allSizes[bIx], isHor);
/* 2089 */         cSt -= allSizes[bIx++];
/*      */       } else {
/* 2091 */         cSt += allSizes[bIx++];
/* 2092 */         cw.setDimBounds(cSt, allSizes[bIx], isHor);
/* 2093 */         cSt += allSizes[bIx++];
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void layoutParallel(ContainerWrapper parent, ArrayList<CompWrap> compWraps, DimConstraint dc, int start, int size, boolean isHor, boolean fromEnd) {
/* 2100 */     int[][] sizes = new int[compWraps.size()][];
/*      */     
/* 2102 */     for (int i = 0; i < sizes.length; i++) {
/* 2103 */       CompWrap cw = compWraps.get(i);
/*      */       
/* 2105 */       DimConstraint cDc = cw.cc.getDimConstraint(isHor);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2110 */       ResizeConstraint[] resConstr = { cw.isPushGap(isHor, true) ? GAP_RC_CONST_PUSH : GAP_RC_CONST, cDc.resize, cw.isPushGap(isHor, false) ? GAP_RC_CONST_PUSH : GAP_RC_CONST };
/*      */ 
/*      */ 
/*      */       
/* 2114 */       int[][] sz = { cw.getGaps(isHor, true), cw.getSizes(isHor), cw.getGaps(isHor, false) };
/*      */ 
/*      */       
/* 2117 */       Float[] pushW = dc.isFill() ? GROW_100 : null;
/*      */       
/* 2119 */       sizes[i] = LayoutUtil.calculateSerial(sz, resConstr, pushW, 1, size);
/*      */     } 
/*      */     
/* 2122 */     UnitValue rowAlign = dc.getAlignOrDefault(isHor);
/* 2123 */     setCompWrapBounds(parent, sizes, compWraps, rowAlign, start, size, isHor, fromEnd);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setCompWrapBounds(ContainerWrapper parent, int[][] sizes, ArrayList<CompWrap> compWraps, UnitValue rowAlign, int start, int size, boolean isHor, boolean fromEnd) {
/* 2128 */     for (int i = 0; i < sizes.length; i++) {
/* 2129 */       CompWrap cw = compWraps.get(i);
/*      */       
/* 2131 */       UnitValue align = correctAlign(cw.cc, rowAlign, isHor, fromEnd);
/*      */       
/* 2133 */       int[] cSizes = sizes[i];
/* 2134 */       int gapBef = cSizes[0];
/* 2135 */       int cSize = cSizes[1];
/* 2136 */       int gapAft = cSizes[2];
/*      */       
/* 2138 */       int cSt = fromEnd ? (start - gapBef) : (start + gapBef);
/* 2139 */       int slack = size - cSize - gapBef - gapAft;
/* 2140 */       if (slack > 0 && align != null) {
/* 2141 */         int al = Math.min(slack, Math.max(0, align.getPixels(slack, parent, null)));
/* 2142 */         cSt += fromEnd ? -al : al;
/*      */       } 
/*      */       
/* 2145 */       cw.setDimBounds(fromEnd ? (cSt - cSize) : cSt, cSize, isHor);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static UnitValue correctAlign(CC cc, UnitValue rowAlign, boolean isHor, boolean fromEnd) {
/* 2151 */     UnitValue align = (isHor ? cc.getHorizontal() : cc.getVertical()).getAlign();
/* 2152 */     if (align == null)
/* 2153 */       align = rowAlign; 
/* 2154 */     if (align == UnitValue.BASELINE_IDENTITY) {
/* 2155 */       align = UnitValue.CENTER;
/*      */     }
/* 2157 */     if (fromEnd)
/* 2158 */       if (align == UnitValue.LEFT) {
/* 2159 */         align = UnitValue.RIGHT;
/* 2160 */       } else if (align == UnitValue.RIGHT) {
/* 2161 */         align = UnitValue.LEFT;
/*      */       }  
/* 2163 */     return align;
/*      */   }
/*      */   
/*      */   private static class AboveBelow {
/*      */     int maxAbove;
/*      */     int maxBelow;
/*      */     
/*      */     AboveBelow(int maxAbove, int maxBelow) {
/* 2171 */       this.maxAbove = maxAbove;
/* 2172 */       this.maxBelow = maxBelow;
/*      */     }
/*      */     
/*      */     int sum() {
/* 2176 */       return this.maxAbove + this.maxBelow;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static AboveBelow getBaselineAboveBelow(ArrayList<CompWrap> compWraps, int sType, boolean centerBaseline) {
/* 2182 */     int maxAbove = Integer.MIN_VALUE;
/* 2183 */     int maxBelow = Integer.MIN_VALUE;
/* 2184 */     for (CompWrap cw : compWraps) {
/* 2185 */       int height = cw.getSize(sType, false);
/* 2186 */       if (height >= 2097051) {
/* 2187 */         return new AboveBelow(1048525, 1048525);
/*      */       }
/* 2189 */       int baseline = cw.getBaseline(sType);
/* 2190 */       int above = baseline + cw.getGapBefore(sType, false);
/* 2191 */       maxAbove = Math.max(above, maxAbove);
/* 2192 */       maxBelow = Math.max(height - baseline + cw.getGapAfter(sType, false), maxBelow);
/*      */       
/* 2194 */       if (centerBaseline)
/* 2195 */         cw.setDimBounds(-baseline, height, false); 
/*      */     } 
/* 2197 */     return new AboveBelow(maxAbove, maxBelow);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getTotalSizeParallel(ArrayList<CompWrap> compWraps, int sType, boolean isHor) {
/* 2202 */     int size = (sType == 2) ? 2097051 : 0;
/*      */     
/* 2204 */     for (CompWrap cw : compWraps) {
/* 2205 */       int cwSize = cw.getSizeInclGaps(sType, isHor);
/* 2206 */       if (cwSize >= 2097051) {
/* 2207 */         return 2097051;
/*      */       }
/* 2209 */       if ((sType == 2) ? (cwSize < size) : (cwSize > size))
/* 2210 */         size = cwSize; 
/*      */     } 
/* 2212 */     return constrainSize(size);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getTotalSizeSerial(ArrayList<CompWrap> compWraps, int sType, boolean isHor) {
/* 2217 */     int totSize = 0;
/* 2218 */     for (int i = 0, iSz = compWraps.size(), lastGapAfter = 0; i < iSz; i++) {
/* 2219 */       CompWrap wrap = compWraps.get(i);
/* 2220 */       int gapBef = wrap.getGapBefore(sType, isHor);
/* 2221 */       if (gapBef > lastGapAfter) {
/* 2222 */         totSize += gapBef - lastGapAfter;
/*      */       }
/* 2224 */       totSize += wrap.getSize(sType, isHor);
/* 2225 */       totSize += lastGapAfter = wrap.getGapAfter(sType, isHor);
/*      */       
/* 2227 */       if (totSize >= 2097051)
/* 2228 */         return 2097051; 
/*      */     } 
/* 2230 */     return constrainSize(totSize);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getTotalGroupsSizeParallel(ArrayList<LinkedDimGroup> groups, int sType, boolean countSpanning) {
/* 2235 */     int size = (sType == 2) ? 2097051 : 0;
/* 2236 */     for (LinkedDimGroup group : groups) {
/* 2237 */       if (countSpanning || group.span == 1) {
/* 2238 */         int grpSize = group.getMinPrefMax()[sType];
/* 2239 */         if (grpSize >= 2097051) {
/* 2240 */           return 2097051;
/*      */         }
/* 2242 */         if ((sType == 2) ? (grpSize < size) : (grpSize > size))
/* 2243 */           size = grpSize; 
/*      */       } 
/*      */     } 
/* 2246 */     return constrainSize(size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[][] getComponentSizes(ArrayList<CompWrap> compWraps, boolean isHor) {
/* 2256 */     int[][] compSizes = new int[compWraps.size()][];
/* 2257 */     for (int i = 0; i < compSizes.length; i++)
/* 2258 */       compSizes[i] = ((CompWrap)compWraps.get(i)).getSizes(isHor); 
/* 2259 */     return compSizes;
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
/*      */   private static FlowSizeSpec mergeSizesGapsAndResConstrs(ResizeConstraint[] resConstr, boolean[] gapPush, int[][] minPrefMaxSizes, int[][] gapSizes) {
/* 2271 */     int[][] sizes = new int[(minPrefMaxSizes.length << 1) + 1][];
/* 2272 */     ResizeConstraint[] resConstsInclGaps = new ResizeConstraint[sizes.length];
/*      */     
/* 2274 */     sizes[0] = gapSizes[0]; int i, crIx;
/* 2275 */     for (i = 0, crIx = 1; i < minPrefMaxSizes.length; i++, crIx += 2) {
/*      */ 
/*      */       
/* 2278 */       resConstsInclGaps[crIx] = resConstr[i];
/* 2279 */       sizes[crIx] = minPrefMaxSizes[i];
/*      */       
/* 2281 */       sizes[crIx + 1] = gapSizes[i + 1];
/*      */       
/* 2283 */       if (sizes[crIx - 1] != null) {
/* 2284 */         resConstsInclGaps[crIx - 1] = gapPush[(i < gapPush.length) ? i : (gapPush.length - 1)] ? GAP_RC_CONST_PUSH : GAP_RC_CONST;
/*      */       }
/* 2286 */       if (i == minPrefMaxSizes.length - 1 && sizes[crIx + 1] != null) {
/* 2287 */         resConstsInclGaps[crIx + 1] = gapPush[(i + 1 < gapPush.length) ? (i + 1) : (gapPush.length - 1)] ? GAP_RC_CONST_PUSH : GAP_RC_CONST;
/*      */       }
/*      */     } 
/*      */     
/* 2291 */     for (i = 0; i < sizes.length; i++) {
/* 2292 */       if (sizes[i] == null) {
/* 2293 */         sizes[i] = new int[3];
/*      */       }
/*      */     } 
/* 2296 */     return new FlowSizeSpec(sizes, resConstsInclGaps);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int[] mergeSizes(int[] oldValues, int[] newValues) {
/* 2301 */     if (oldValues == null) {
/* 2302 */       return newValues;
/*      */     }
/* 2304 */     if (newValues == null) {
/* 2305 */       return oldValues;
/*      */     }
/* 2307 */     int[] ret = new int[oldValues.length];
/* 2308 */     for (int i = 0; i < ret.length; i++) {
/* 2309 */       ret[i] = mergeSizes(oldValues[i], newValues[i], true);
/*      */     }
/* 2311 */     return ret;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int mergeSizes(int oldValue, int newValue, boolean toMax) {
/* 2316 */     if (oldValue == -2147471302 || oldValue == newValue) {
/* 2317 */       return newValue;
/*      */     }
/* 2319 */     if (newValue == -2147471302) {
/* 2320 */       return oldValue;
/*      */     }
/* 2322 */     return (toMax != ((oldValue > newValue))) ? newValue : oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int constrainSize(int s) {
/* 2327 */     return (s > 0) ? ((s < 2097051) ? s : 2097051) : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void correctMinMax(int[] s) {
/* 2332 */     if (s[0] > s[2]) {
/* 2333 */       s[0] = s[2];
/*      */     }
/* 2335 */     if (s[1] < s[0]) {
/* 2336 */       s[1] = s[0];
/*      */     }
/* 2338 */     if (s[1] > s[2]) {
/* 2339 */       s[1] = s[2];
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class FlowSizeSpec
/*      */   {
/*      */     private final int[][] sizes;
/*      */     private final ResizeConstraint[] resConstsInclGaps;
/*      */     
/*      */     private FlowSizeSpec(int[][] sizes, ResizeConstraint[] resConstsInclGaps) {
/* 2349 */       this.sizes = sizes;
/* 2350 */       this.resConstsInclGaps = resConstsInclGaps;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int expandSizes(DimConstraint[] specs, Float[] defGrow, int targetSize, int fromIx, int len, int sizeType, int eagerness) {
/* 2371 */       ResizeConstraint[] resConstr = new ResizeConstraint[len];
/* 2372 */       int[][] sizesToExpand = new int[len][];
/* 2373 */       for (int i = 0; i < len; i++) {
/* 2374 */         int[] minPrefMax = this.sizes[i + fromIx];
/* 2375 */         (new int[3])[0] = minPrefMax[sizeType]; (new int[3])[1] = minPrefMax[1]; (new int[3])[2] = minPrefMax[2]; sizesToExpand[i] = new int[3];
/*      */         
/* 2377 */         if (eagerness <= 1 && i % 2 == 0) {
/* 2378 */           int cIx = i + fromIx - 1 >> 1;
/* 2379 */           DimConstraint spec = (DimConstraint)LayoutUtil.getIndexSafe((Object[])specs, cIx);
/*      */           
/* 2381 */           BoundSize sz = spec.getSize();
/* 2382 */           if ((sizeType == 0 && sz.getMin() != null && sz.getMin().getUnit() != 13) || (sizeType == 1 && sz
/* 2383 */             .getPreferred() != null && sz.getPreferred().getUnit() != 14)) {
/*      */             continue;
/*      */           }
/*      */         } 
/* 2387 */         resConstr[i] = (ResizeConstraint)LayoutUtil.getIndexSafe((Object[])this.resConstsInclGaps, i + fromIx);
/*      */         continue;
/*      */       } 
/* 2390 */       Float[] growW = (eagerness == 1 || eagerness == 3) ? Grid.extractSubArray(specs, defGrow, fromIx, len) : null;
/* 2391 */       int[] newSizes = LayoutUtil.calculateSerial(sizesToExpand, resConstr, growW, 1, targetSize);
/* 2392 */       int newSize = 0;
/*      */       
/* 2394 */       for (int j = 0; j < len; j++) {
/* 2395 */         int s = newSizes[j];
/* 2396 */         this.sizes[j + fromIx][sizeType] = s;
/* 2397 */         newSize += s;
/*      */       } 
/* 2399 */       return newSize;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static Float[] extractSubArray(DimConstraint[] specs, Float[] arr, int ix, int len) {
/* 2405 */     if (arr == null || arr.length < ix + len) {
/* 2406 */       Float[] growLastArr = new Float[len];
/*      */ 
/*      */       
/* 2409 */       for (int i = ix + len - 1; i >= 0; i -= 2) {
/* 2410 */         int specIx = i >> 1;
/* 2411 */         if (specs[specIx] != DOCK_DIM_CONSTRAINT) {
/* 2412 */           growLastArr[i - ix] = ResizeConstraint.WEIGHT_100;
/* 2413 */           return growLastArr;
/*      */         } 
/*      */       } 
/* 2416 */       return growLastArr;
/*      */     } 
/*      */     
/* 2419 */     Float[] newArr = new Float[len];
/* 2420 */     System.arraycopy(arr, ix, newArr, 0, len);
/* 2421 */     return newArr;
/*      */   }
/*      */   
/* 2424 */   private static WeakHashMap<Object, int[][]>[] PARENT_ROWCOL_SIZES_MAP = null;
/*      */ 
/*      */   
/*      */   private static synchronized void putSizesAndIndexes(Object parComp, int[] sizes, int[] ixArr, boolean isRows) {
/* 2428 */     if (PARENT_ROWCOL_SIZES_MAP == null) {
/* 2429 */       PARENT_ROWCOL_SIZES_MAP = (WeakHashMap<Object, int[][]>[])new WeakHashMap[] { new WeakHashMap<>(4), new WeakHashMap<>(4) };
/*      */     }
/* 2431 */     PARENT_ROWCOL_SIZES_MAP[isRows ? 0 : 1].put(parComp, new int[][] { ixArr, sizes });
/*      */   }
/*      */ 
/*      */   
/*      */   static synchronized int[][] getSizesAndIndexes(Object parComp, boolean isRows) {
/* 2436 */     if (PARENT_ROWCOL_SIZES_MAP == null) {
/* 2437 */       return (int[][])null;
/*      */     }
/* 2439 */     return PARENT_ROWCOL_SIZES_MAP[isRows ? 0 : 1].get(parComp);
/*      */   }
/*      */   
/* 2442 */   private static WeakHashMap<Object, ArrayList<WeakCell>> PARENT_GRIDPOS_MAP = null;
/*      */   
/*      */   private static synchronized void saveGrid(ComponentWrapper parComp, LinkedHashMap<Integer, Cell> grid) {
/* 2445 */     if (PARENT_GRIDPOS_MAP == null) {
/* 2446 */       PARENT_GRIDPOS_MAP = new WeakHashMap<>(4);
/*      */     }
/* 2448 */     ArrayList<WeakCell> weakCells = new ArrayList<>(grid.size());
/*      */     
/* 2450 */     for (Map.Entry<Integer, Cell> e : grid.entrySet()) {
/* 2451 */       Cell cell = e.getValue();
/* 2452 */       Integer xyInt = e.getKey();
/* 2453 */       if (xyInt != null) {
/* 2454 */         int x = xyInt.intValue() << 16 >> 16;
/* 2455 */         int y = xyInt.intValue() >> 16;
/*      */         
/* 2457 */         for (CompWrap cw : cell.compWraps) {
/* 2458 */           weakCells.add(new WeakCell(cw.comp.getComponent(), x, y, cell.spanx, cell.spany));
/*      */         }
/*      */       } 
/*      */     } 
/* 2462 */     PARENT_GRIDPOS_MAP.put(parComp.getComponent(), weakCells);
/*      */   }
/*      */ 
/*      */   
/*      */   static synchronized HashMap<Object, int[]> getGridPositions(Object parComp) {
/* 2467 */     ArrayList<WeakCell> weakCells = (PARENT_GRIDPOS_MAP != null) ? PARENT_GRIDPOS_MAP.get(parComp) : null;
/* 2468 */     if (weakCells == null) {
/* 2469 */       return null;
/*      */     }
/* 2471 */     HashMap<Object, int[]> retMap = (HashMap)new HashMap<>();
/*      */     
/* 2473 */     for (WeakCell wc : weakCells) {
/* 2474 */       Object component = wc.componentRef.get();
/* 2475 */       if (component != null) {
/* 2476 */         retMap.put(component, new int[] { WeakCell.access$5800(wc), WeakCell.access$5900(wc), WeakCell.access$6000(wc), WeakCell.access$6100(wc) });
/*      */       }
/*      */     } 
/* 2479 */     return retMap;
/*      */   }
/*      */   
/*      */   private static class WeakCell { private final WeakReference<Object> componentRef;
/*      */     private final int x;
/*      */     private final int y;
/*      */     private final int spanX;
/*      */     private final int spanY;
/*      */     
/*      */     private WeakCell(Object component, int x, int y, int spanX, int spanY) {
/* 2489 */       this.componentRef = new WeakReference(component);
/* 2490 */       this.x = x;
/* 2491 */       this.y = y;
/* 2492 */       this.spanX = spanX;
/* 2493 */       this.spanY = spanY;
/*      */     } }
/*      */ 
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\Grid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */