/*      */ package net.miginfocom.layout;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ConstraintParser
/*      */ {
/*      */   public static LC parseLayoutConstraint(String s) {
/*   56 */     LC lc = new LC();
/*   57 */     if (s.isEmpty()) {
/*   58 */       return lc;
/*      */     }
/*   60 */     String[] parts = toTrimmedTokens(s, ',');
/*      */ 
/*      */     
/*   63 */     for (int i = 0; i < parts.length; i++) {
/*   64 */       String part = parts[i];
/*   65 */       if (part != null) {
/*      */ 
/*      */         
/*   68 */         int len = part.length();
/*   69 */         if (len == 3 || len == 11) {
/*   70 */           if (part.equals("ltr") || part.equals("rtl") || part.equals("lefttoright") || part.equals("righttoleft")) {
/*   71 */             lc.setLeftToRight((part.charAt(0) == 'l') ? Boolean.TRUE : Boolean.FALSE);
/*   72 */             parts[i] = null;
/*      */           } 
/*      */           
/*   75 */           if (part.equals("ttb") || part.equals("btt") || part.equals("toptobottom") || part.equals("bottomtotop")) {
/*   76 */             lc.setTopToBottom((part.charAt(0) == 't'));
/*   77 */             parts[i] = null;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*   82 */     for (String part : parts) {
/*   83 */       if (part == null || part.length() == 0) {
/*      */         continue;
/*      */       }
/*      */       try {
/*   87 */         int ix = -1;
/*   88 */         char c = part.charAt(0);
/*      */         
/*   90 */         if (c == 'w' || c == 'h') {
/*      */           
/*   92 */           ix = startsWithLenient(part, "wrap", -1, true);
/*   93 */           if (ix > -1) {
/*   94 */             String num = part.substring(ix).trim();
/*   95 */             lc.setWrapAfter((num.length() != 0) ? Integer.parseInt(num) : 0);
/*      */             
/*      */             continue;
/*      */           } 
/*   99 */           boolean isHor = (c == 'w');
/*  100 */           if (isHor && (part.startsWith("w ") || part.startsWith("width "))) {
/*  101 */             String sz = part.substring((part.charAt(1) == ' ') ? 2 : 6).trim();
/*  102 */             lc.setWidth(parseBoundSize(sz, false, true));
/*      */             
/*      */             continue;
/*      */           } 
/*  106 */           if (!isHor && (part.startsWith("h ") || part.startsWith("height "))) {
/*  107 */             String uvStr = part.substring((part.charAt(1) == ' ') ? 2 : 7).trim();
/*  108 */             lc.setHeight(parseBoundSize(uvStr, false, false));
/*      */             
/*      */             continue;
/*      */           } 
/*  112 */           if (part.length() > 5) {
/*  113 */             String sz = part.substring(5).trim();
/*  114 */             if (part.startsWith("wmin ")) {
/*  115 */               lc.minWidth(sz); continue;
/*      */             } 
/*  117 */             if (part.startsWith("wmax ")) {
/*  118 */               lc.maxWidth(sz); continue;
/*      */             } 
/*  120 */             if (part.startsWith("hmin ")) {
/*  121 */               lc.minHeight(sz); continue;
/*      */             } 
/*  123 */             if (part.startsWith("hmax ")) {
/*  124 */               lc.maxHeight(sz);
/*      */               
/*      */               continue;
/*      */             } 
/*      */           } 
/*  129 */           if (part.startsWith("hidemode ")) {
/*  130 */             lc.setHideMode(Integer.parseInt(part.substring(9)));
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  135 */         if (c == 'g') {
/*  136 */           if (part.startsWith("gapx ")) {
/*  137 */             lc.setGridGapX(parseBoundSize(part.substring(5).trim(), true, true));
/*      */             
/*      */             continue;
/*      */           } 
/*  141 */           if (part.startsWith("gapy ")) {
/*  142 */             lc.setGridGapY(parseBoundSize(part.substring(5).trim(), true, false));
/*      */             
/*      */             continue;
/*      */           } 
/*  146 */           if (part.startsWith("gap ")) {
/*  147 */             String[] gaps = toTrimmedTokens(part.substring(4).trim(), ' ');
/*  148 */             lc.setGridGapX(parseBoundSize(gaps[0], true, true));
/*  149 */             lc.setGridGapY((gaps.length > 1) ? parseBoundSize(gaps[1], true, false) : lc.getGridGapX());
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  154 */         if (c == 'd') {
/*  155 */           ix = startsWithLenient(part, "debug", 5, true);
/*  156 */           if (ix > -1) {
/*  157 */             String millis = part.substring(ix).trim();
/*  158 */             lc.setDebugMillis((millis.length() > 0) ? Integer.parseInt(millis) : 1000);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  163 */         if (c == 'n') {
/*  164 */           if (part.equals("nogrid")) {
/*  165 */             lc.setNoGrid(true);
/*      */             
/*      */             continue;
/*      */           } 
/*  169 */           if (part.equals("nocache")) {
/*  170 */             lc.setNoCache(true);
/*      */             
/*      */             continue;
/*      */           } 
/*  174 */           if (part.equals("novisualpadding")) {
/*  175 */             lc.setVisualPadding(false);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  180 */         if (c == 'f') {
/*  181 */           if (part.equals("fill") || part.equals("fillx") || part.equals("filly")) {
/*  182 */             lc.setFillX((part.length() == 4 || part.charAt(4) == 'x'));
/*  183 */             lc.setFillY((part.length() == 4 || part.charAt(4) == 'y'));
/*      */             
/*      */             continue;
/*      */           } 
/*  187 */           if (part.equals("flowy")) {
/*  188 */             lc.setFlowX(false);
/*      */             
/*      */             continue;
/*      */           } 
/*  192 */           if (part.equals("flowx")) {
/*  193 */             lc.setFlowX(true);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  198 */         if (c == 'i') {
/*  199 */           ix = startsWithLenient(part, "insets", 3, true);
/*  200 */           if (ix > -1) {
/*  201 */             String insStr = part.substring(ix).trim();
/*  202 */             UnitValue[] ins = parseInsets(insStr, true);
/*  203 */             LayoutUtil.putCCString(ins, insStr);
/*  204 */             lc.setInsets(ins);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  209 */         if (c == 'a') {
/*  210 */           ix = startsWithLenient(part, new String[] { "aligny", "ay" }, new int[] { 6, 2 }, true);
/*  211 */           if (ix > -1) {
/*  212 */             UnitValue align = parseUnitValueOrAlign(part.substring(ix).trim(), false, null);
/*  213 */             if (align == UnitValue.BASELINE_IDENTITY)
/*  214 */               throw new IllegalArgumentException("'baseline' can not be used to align the whole component group."); 
/*  215 */             lc.setAlignY(align);
/*      */             
/*      */             continue;
/*      */           } 
/*  219 */           ix = startsWithLenient(part, new String[] { "alignx", "ax" }, new int[] { 6, 2 }, true);
/*  220 */           if (ix > -1) {
/*  221 */             lc.setAlignX(parseUnitValueOrAlign(part.substring(ix).trim(), true, null));
/*      */             
/*      */             continue;
/*      */           } 
/*  225 */           ix = startsWithLenient(part, "align", 2, true);
/*  226 */           if (ix > -1) {
/*  227 */             String[] gaps = toTrimmedTokens(part.substring(ix).trim(), ' ');
/*  228 */             lc.setAlignX(parseUnitValueOrAlign(gaps[0], true, null));
/*  229 */             if (gaps.length > 1) {
/*  230 */               UnitValue align = parseUnitValueOrAlign(gaps[1], false, null);
/*  231 */               if (align == UnitValue.BASELINE_IDENTITY)
/*  232 */                 throw new IllegalArgumentException("'baseline' can not be used to align the whole component group."); 
/*  233 */               lc.setAlignY(align);
/*      */             } 
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  239 */         if (c == 'p') {
/*  240 */           if (part.startsWith("packalign ")) {
/*  241 */             String[] packs = toTrimmedTokens(part.substring(10).trim(), ' ');
/*  242 */             lc.setPackWidthAlign((packs[0].length() > 0) ? Float.parseFloat(packs[0]) : 0.5F);
/*  243 */             if (packs.length > 1) {
/*  244 */               lc.setPackHeightAlign(Float.parseFloat(packs[1]));
/*      */             }
/*      */             continue;
/*      */           } 
/*  248 */           if (part.startsWith("pack ") || part.equals("pack")) {
/*  249 */             String ps = part.substring(4).trim();
/*  250 */             String[] packs = toTrimmedTokens((ps.length() > 0) ? ps : "pref pref", ' ');
/*  251 */             lc.setPackWidth(parseBoundSize(packs[0], false, true));
/*  252 */             if (packs.length > 1) {
/*  253 */               lc.setPackHeight(parseBoundSize(packs[1], false, false));
/*      */             }
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  259 */         if (lc.getAlignX() == null) {
/*  260 */           UnitValue alignX = parseAlignKeywords(part, true);
/*  261 */           if (alignX != null) {
/*  262 */             lc.setAlignX(alignX);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  267 */         UnitValue alignY = parseAlignKeywords(part, false);
/*  268 */         if (alignY != null) {
/*  269 */           lc.setAlignY(alignY);
/*      */         }
/*      */         else {
/*      */           
/*  273 */           throw new IllegalArgumentException("Unknown Constraint: '" + part + "'\n");
/*      */         } 
/*  275 */       } catch (Exception ex) {
/*  276 */         throw new IllegalArgumentException("Illegal Constraint: '" + part + "'\n" + ex.getMessage());
/*      */       } 
/*      */       
/*      */       continue;
/*      */     } 
/*      */     
/*  282 */     return lc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AC parseRowConstraints(String s) {
/*  292 */     return parseAxisConstraint(s, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AC parseColumnConstraints(String s) {
/*  302 */     return parseAxisConstraint(s, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static AC parseAxisConstraint(String s, boolean isCols) {
/*  313 */     s = s.trim();
/*      */     
/*  315 */     if (s.length() == 0) {
/*  316 */       return new AC();
/*      */     }
/*  318 */     s = s.toLowerCase();
/*      */     
/*  320 */     ArrayList<String> parts = getRowColAndGapsTrimmed(s);
/*      */     
/*  322 */     BoundSize[] gaps = new BoundSize[(parts.size() >> 1) + 1]; int gIx;
/*  323 */     for (int i = 0, iSz = parts.size(); i < iSz; i += 2, gIx++) {
/*  324 */       gaps[gIx] = parseBoundSize(parts.get(i), true, isCols);
/*      */     }
/*  326 */     DimConstraint[] colSpecs = new DimConstraint[parts.size() >> 1];
/*  327 */     for (int j = 0; j < colSpecs.length; j++, gIx++) {
/*  328 */       if (gIx >= gaps.length - 1) {
/*  329 */         gIx = gaps.length - 2;
/*      */       }
/*  331 */       colSpecs[j] = parseDimConstraint(parts.get((j << 1) + 1), gaps[gIx], gaps[gIx + 1], isCols);
/*      */     } 
/*      */     
/*  334 */     AC ac = new AC();
/*  335 */     ac.setConstaints(colSpecs);
/*      */ 
/*      */ 
/*      */     
/*  339 */     return ac;
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
/*      */   private static DimConstraint parseDimConstraint(String s, BoundSize gapBefore, BoundSize gapAfter, boolean isCols) {
/*  353 */     DimConstraint dimConstraint = new DimConstraint();
/*      */ 
/*      */     
/*  356 */     dimConstraint.setGapBefore(gapBefore);
/*  357 */     dimConstraint.setGapAfter(gapAfter);
/*      */     
/*  359 */     String[] parts = toTrimmedTokens(s, ',');
/*  360 */     for (int i = 0; i < parts.length; i++) {
/*  361 */       String part = parts[i];
/*      */       try {
/*  363 */         if (part.length() == 0) {
/*      */           continue;
/*      */         }
/*  366 */         if (part.equals("fill")) {
/*  367 */           dimConstraint.setFill(true);
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*  372 */         if (part.equals("nogrid")) {
/*  373 */           dimConstraint.setNoGrid(true);
/*      */           
/*      */           continue;
/*      */         } 
/*  377 */         int ix = -1;
/*  378 */         char c = part.charAt(0);
/*      */         
/*  380 */         if (c == 's') {
/*  381 */           ix = startsWithLenient(part, new String[] { "sizegroup", "sg" }, new int[] { 5, 2 }, true);
/*  382 */           if (ix > -1) {
/*  383 */             dimConstraint.setSizeGroup(part.substring(ix).trim());
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/*  388 */           ix = startsWithLenient(part, new String[] { "shrinkprio", "shp" }, new int[] { 10, 3 }, true);
/*  389 */           if (ix > -1) {
/*  390 */             dimConstraint.setShrinkPriority(Integer.parseInt(part.substring(ix).trim()));
/*      */             
/*      */             continue;
/*      */           } 
/*  394 */           ix = startsWithLenient(part, "shrink", 6, true);
/*  395 */           if (ix > -1) {
/*  396 */             dimConstraint.setShrink(parseFloat(part.substring(ix).trim(), ResizeConstraint.WEIGHT_100));
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  401 */         if (c == 'g') {
/*  402 */           ix = startsWithLenient(part, new String[] { "growpriority", "gp" }, new int[] { 5, 2 }, true);
/*  403 */           if (ix > -1) {
/*  404 */             dimConstraint.setGrowPriority(Integer.parseInt(part.substring(ix).trim()));
/*      */             
/*      */             continue;
/*      */           } 
/*  408 */           ix = startsWithLenient(part, "grow", 4, true);
/*  409 */           if (ix > -1) {
/*  410 */             dimConstraint.setGrow(parseFloat(part.substring(ix).trim(), ResizeConstraint.WEIGHT_100));
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  415 */         if (c == 'a') {
/*  416 */           ix = startsWithLenient(part, "align", 2, true);
/*  417 */           if (ix > -1) {
/*      */             
/*  419 */             dimConstraint.setAlign(parseUnitValueOrAlign(part.substring(ix).trim(), isCols, null));
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  424 */         UnitValue align = parseAlignKeywords(part, isCols);
/*  425 */         if (align != null) {
/*      */           
/*  427 */           dimConstraint.setAlign(align);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  432 */           dimConstraint.setSize(parseBoundSize(part, false, isCols));
/*      */         } 
/*  434 */       } catch (Exception ex) {
/*  435 */         throw new IllegalArgumentException("Illegal constraint: '" + part + "'\n" + ex.getMessage());
/*      */       }  continue;
/*      */     } 
/*  438 */     return dimConstraint;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<ComponentWrapper, CC> parseComponentConstraints(Map<ComponentWrapper, String> constrMap) {
/*  447 */     HashMap<ComponentWrapper, CC> flowConstrMap = new HashMap<>();
/*      */     
/*  449 */     for (Iterator<Map.Entry<ComponentWrapper, String>> it = constrMap.entrySet().iterator(); it.hasNext(); ) {
/*  450 */       Map.Entry<ComponentWrapper, String> entry = it.next();
/*  451 */       flowConstrMap.put(entry.getKey(), parseComponentConstraint(entry.getValue()));
/*      */     } 
/*      */     
/*  454 */     return flowConstrMap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CC parseComponentConstraint(String s) {
/*  464 */     CC cc = new CC();
/*      */     
/*  466 */     if (s == null || s.isEmpty()) {
/*  467 */       return cc;
/*      */     }
/*  469 */     String[] parts = toTrimmedTokens(s, ',');
/*      */     
/*  471 */     for (String part : parts) {
/*      */       try {
/*  473 */         if (part.length() == 0) {
/*      */           continue;
/*      */         }
/*  476 */         int ix = -1;
/*  477 */         char c = part.charAt(0);
/*      */         
/*  479 */         if (c == 'n') {
/*  480 */           if (part.equals("north")) {
/*  481 */             cc.setDockSide(0);
/*      */             
/*      */             continue;
/*      */           } 
/*  485 */           if (part.equals("newline")) {
/*  486 */             cc.setNewline(true);
/*      */             
/*      */             continue;
/*      */           } 
/*  490 */           if (part.startsWith("newline ")) {
/*  491 */             String gapSz = part.substring(7).trim();
/*  492 */             cc.setNewlineGapSize(parseBoundSize(gapSz, true, true));
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  497 */         if (c == 'f' && (part.equals("flowy") || part.equals("flowx"))) {
/*  498 */           cc.setFlowX((part.charAt(4) == 'x') ? Boolean.TRUE : Boolean.FALSE);
/*      */           
/*      */           continue;
/*      */         } 
/*  502 */         if (c == 's') {
/*  503 */           ix = startsWithLenient(part, "skip", 4, true);
/*  504 */           if (ix > -1) {
/*  505 */             String num = part.substring(ix).trim();
/*  506 */             cc.setSkip((num.length() != 0) ? Integer.parseInt(num) : 1);
/*      */             
/*      */             continue;
/*      */           } 
/*  510 */           ix = startsWithLenient(part, "split", 5, true);
/*  511 */           if (ix > -1) {
/*  512 */             String split = part.substring(ix).trim();
/*  513 */             cc.setSplit((split.length() > 0) ? Integer.parseInt(split) : 2097051);
/*      */             
/*      */             continue;
/*      */           } 
/*  517 */           if (part.equals("south")) {
/*  518 */             cc.setDockSide(2);
/*      */             
/*      */             continue;
/*      */           } 
/*  522 */           ix = startsWithLenient(part, new String[] { "spany", "sy" }, new int[] { 5, 2 }, true);
/*  523 */           if (ix > -1) {
/*  524 */             cc.setSpanY(parseSpan(part.substring(ix).trim()));
/*      */             
/*      */             continue;
/*      */           } 
/*  528 */           ix = startsWithLenient(part, new String[] { "spanx", "sx" }, new int[] { 5, 2 }, true);
/*  529 */           if (ix > -1) {
/*  530 */             cc.setSpanX(parseSpan(part.substring(ix).trim()));
/*      */             
/*      */             continue;
/*      */           } 
/*  534 */           ix = startsWithLenient(part, "span", 4, true);
/*  535 */           if (ix > -1) {
/*  536 */             String[] spans = toTrimmedTokens(part.substring(ix).trim(), ' ');
/*  537 */             cc.setSpanX((spans[0].length() > 0) ? Integer.parseInt(spans[0]) : 2097051);
/*  538 */             cc.setSpanY((spans.length > 1) ? Integer.parseInt(spans[1]) : 1);
/*      */             
/*      */             continue;
/*      */           } 
/*  542 */           ix = startsWithLenient(part, "shrinkx", 7, true);
/*  543 */           if (ix > -1) {
/*  544 */             cc.getHorizontal().setShrink(parseFloat(part.substring(ix).trim(), ResizeConstraint.WEIGHT_100));
/*      */             
/*      */             continue;
/*      */           } 
/*  548 */           ix = startsWithLenient(part, "shrinky", 7, true);
/*  549 */           if (ix > -1) {
/*  550 */             cc.getVertical().setShrink(parseFloat(part.substring(ix).trim(), ResizeConstraint.WEIGHT_100));
/*      */             
/*      */             continue;
/*      */           } 
/*  554 */           ix = startsWithLenient(part, "shrink", 6, false);
/*  555 */           if (ix > -1) {
/*  556 */             String[] shrinks = toTrimmedTokens(part.substring(ix).trim(), ' ');
/*  557 */             cc.getHorizontal().setShrink(parseFloat(shrinks[0], ResizeConstraint.WEIGHT_100));
/*  558 */             if (shrinks.length > 1) {
/*  559 */               cc.getVertical().setShrink(parseFloat(shrinks[1], ResizeConstraint.WEIGHT_100));
/*      */             }
/*      */             continue;
/*      */           } 
/*  563 */           ix = startsWithLenient(part, new String[] { "shrinkprio", "shp" }, new int[] { 10, 3 }, true);
/*  564 */           if (ix > -1) {
/*  565 */             String sp = part.substring(ix).trim();
/*  566 */             if (sp.startsWith("x") || sp.startsWith("y")) {
/*  567 */               (sp.startsWith("x") ? cc.getHorizontal() : cc.getVertical()).setShrinkPriority(Integer.parseInt(sp.substring(2)));
/*      */             } else {
/*  569 */               String[] shrinks = toTrimmedTokens(sp, ' ');
/*  570 */               cc.getHorizontal().setShrinkPriority(Integer.parseInt(shrinks[0]));
/*  571 */               if (shrinks.length > 1) {
/*  572 */                 cc.getVertical().setShrinkPriority(Integer.parseInt(shrinks[1]));
/*      */               }
/*      */             } 
/*      */             continue;
/*      */           } 
/*  577 */           ix = startsWithLenient(part, new String[] { "sizegroupx", "sizegroupy", "sgx", "sgy" }, new int[] { 9, 9, 2, 2 }, true);
/*  578 */           if (ix > -1) {
/*  579 */             String sg = part.substring(ix).trim();
/*  580 */             char lc = part.charAt(ix - 1);
/*  581 */             if (lc != 'y')
/*  582 */               cc.getHorizontal().setSizeGroup(sg); 
/*  583 */             if (lc != 'x') {
/*  584 */               cc.getVertical().setSizeGroup(sg);
/*      */             }
/*      */             continue;
/*      */           } 
/*      */         } 
/*  589 */         if (c == 'g') {
/*  590 */           ix = startsWithLenient(part, "growx", 5, true);
/*  591 */           if (ix > -1) {
/*  592 */             cc.getHorizontal().setGrow(parseFloat(part.substring(ix).trim(), ResizeConstraint.WEIGHT_100));
/*      */             
/*      */             continue;
/*      */           } 
/*  596 */           ix = startsWithLenient(part, "growy", 5, true);
/*  597 */           if (ix > -1) {
/*  598 */             cc.getVertical().setGrow(parseFloat(part.substring(ix).trim(), ResizeConstraint.WEIGHT_100));
/*      */             
/*      */             continue;
/*      */           } 
/*  602 */           ix = startsWithLenient(part, "grow", 4, false);
/*  603 */           if (ix > -1) {
/*  604 */             String[] grows = toTrimmedTokens(part.substring(ix).trim(), ' ');
/*  605 */             cc.getHorizontal().setGrow(parseFloat(grows[0], ResizeConstraint.WEIGHT_100));
/*  606 */             cc.getVertical().setGrow(parseFloat((grows.length > 1) ? grows[1] : "", ResizeConstraint.WEIGHT_100));
/*      */             
/*      */             continue;
/*      */           } 
/*  610 */           ix = startsWithLenient(part, new String[] { "growprio", "gp" }, new int[] { 8, 2 }, true);
/*  611 */           if (ix > -1) {
/*  612 */             String gp = part.substring(ix).trim();
/*  613 */             char c0 = (gp.length() > 0) ? gp.charAt(0) : ' ';
/*  614 */             if (c0 == 'x' || c0 == 'y') {
/*  615 */               ((c0 == 'x') ? cc.getHorizontal() : cc.getVertical()).setGrowPriority(Integer.parseInt(gp.substring(2)));
/*      */             } else {
/*  617 */               String[] grows = toTrimmedTokens(gp, ' ');
/*  618 */               cc.getHorizontal().setGrowPriority(Integer.parseInt(grows[0]));
/*  619 */               if (grows.length > 1) {
/*  620 */                 cc.getVertical().setGrowPriority(Integer.parseInt(grows[1]));
/*      */               }
/*      */             } 
/*      */             continue;
/*      */           } 
/*  625 */           if (part.startsWith("gap")) {
/*  626 */             BoundSize[] gaps = parseGaps(part);
/*  627 */             if (gaps[0] != null)
/*  628 */               cc.getVertical().setGapBefore(gaps[0]); 
/*  629 */             if (gaps[1] != null)
/*  630 */               cc.getHorizontal().setGapBefore(gaps[1]); 
/*  631 */             if (gaps[2] != null)
/*  632 */               cc.getVertical().setGapAfter(gaps[2]); 
/*  633 */             if (gaps[3] != null) {
/*  634 */               cc.getHorizontal().setGapAfter(gaps[3]);
/*      */             }
/*      */             continue;
/*      */           } 
/*      */         } 
/*  639 */         if (c == 'a') {
/*  640 */           ix = startsWithLenient(part, new String[] { "aligny", "ay" }, new int[] { 6, 2 }, true);
/*  641 */           if (ix > -1) {
/*  642 */             cc.getVertical().setAlign(parseUnitValueOrAlign(part.substring(ix).trim(), false, null));
/*      */             
/*      */             continue;
/*      */           } 
/*  646 */           ix = startsWithLenient(part, new String[] { "alignx", "ax" }, new int[] { 6, 2 }, true);
/*  647 */           if (ix > -1) {
/*  648 */             cc.getHorizontal().setAlign(parseUnitValueOrAlign(part.substring(ix).trim(), true, null));
/*      */             
/*      */             continue;
/*      */           } 
/*  652 */           ix = startsWithLenient(part, "align", 2, true);
/*  653 */           if (ix > -1) {
/*  654 */             String[] gaps = toTrimmedTokens(part.substring(ix).trim(), ' ');
/*  655 */             cc.getHorizontal().setAlign(parseUnitValueOrAlign(gaps[0], true, null));
/*  656 */             if (gaps.length > 1) {
/*  657 */               cc.getVertical().setAlign(parseUnitValueOrAlign(gaps[1], false, null));
/*      */             }
/*      */             continue;
/*      */           } 
/*      */         } 
/*  662 */         if ((c == 'x' || c == 'y') && part.length() > 2) {
/*  663 */           char c2 = part.charAt(1);
/*  664 */           if (c2 == ' ' || (c2 == '2' && part.charAt(2) == ' ')) {
/*  665 */             if (cc.getPos() == null) {
/*  666 */               cc.setPos(new UnitValue[4]);
/*  667 */             } else if (!cc.isBoundsInGrid()) {
/*  668 */               throw new IllegalArgumentException("Cannot combine 'position' with 'x/y/x2/y2' keywords.");
/*      */             } 
/*      */             
/*  671 */             int edge = ((c == 'x') ? 0 : 1) + ((c2 == '2') ? 2 : 0);
/*  672 */             UnitValue[] pos = cc.getPos();
/*  673 */             pos[edge] = parseUnitValue(part.substring(2).trim(), null, (c == 'x'));
/*  674 */             cc.setPos(pos);
/*  675 */             cc.setBoundsInGrid(true);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  680 */         if (c == 'c') {
/*  681 */           ix = startsWithLenient(part, "cell", 4, true);
/*  682 */           if (ix > -1) {
/*  683 */             String[] grs = toTrimmedTokens(part.substring(ix).trim(), ' ');
/*  684 */             if (grs.length < 2)
/*  685 */               throw new IllegalArgumentException("At least two integers must follow " + part); 
/*  686 */             cc.setCellX(Integer.parseInt(grs[0]));
/*  687 */             cc.setCellY(Integer.parseInt(grs[1]));
/*  688 */             if (grs.length > 2)
/*  689 */               cc.setSpanX(Integer.parseInt(grs[2])); 
/*  690 */             if (grs.length > 3) {
/*  691 */               cc.setSpanY(Integer.parseInt(grs[3]));
/*      */             }
/*      */             continue;
/*      */           } 
/*      */         } 
/*  696 */         if (c == 'p') {
/*  697 */           ix = startsWithLenient(part, "pos", 3, true);
/*  698 */           if (ix > -1) {
/*  699 */             if (cc.getPos() != null && cc.isBoundsInGrid()) {
/*  700 */               throw new IllegalArgumentException("Can not combine 'pos' with 'x/y/x2/y2' keywords.");
/*      */             }
/*  702 */             String[] pos = toTrimmedTokens(part.substring(ix).trim(), ' ');
/*  703 */             UnitValue[] bounds = new UnitValue[4];
/*  704 */             for (int j = 0; j < pos.length; j++) {
/*  705 */               bounds[j] = parseUnitValue(pos[j], null, (j % 2 == 0));
/*      */             }
/*  707 */             if ((bounds[0] == null && bounds[2] == null) || (bounds[1] == null && bounds[3] == null)) {
/*  708 */               throw new IllegalArgumentException("Both x and x2 or y and y2 can not be null!");
/*      */             }
/*  710 */             cc.setPos(bounds);
/*  711 */             cc.setBoundsInGrid(false);
/*      */             
/*      */             continue;
/*      */           } 
/*  715 */           ix = startsWithLenient(part, "pad", 3, true);
/*  716 */           if (ix > -1) {
/*  717 */             UnitValue[] p = parseInsets(part.substring(ix).trim(), false);
/*  718 */             cc.setPadding(new UnitValue[] { p[0], (p.length > 1) ? p[1] : null, (p.length > 2) ? p[2] : null, (p.length > 3) ? p[3] : null });
/*      */ 
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */ 
/*      */           
/*  726 */           ix = startsWithLenient(part, "pushx", 5, true);
/*  727 */           if (ix > -1) {
/*  728 */             cc.setPushX(parseFloat(part.substring(ix).trim(), ResizeConstraint.WEIGHT_100));
/*      */             
/*      */             continue;
/*      */           } 
/*  732 */           ix = startsWithLenient(part, "pushy", 5, true);
/*  733 */           if (ix > -1) {
/*  734 */             cc.setPushY(parseFloat(part.substring(ix).trim(), ResizeConstraint.WEIGHT_100));
/*      */             
/*      */             continue;
/*      */           } 
/*  738 */           ix = startsWithLenient(part, "push", 4, false);
/*  739 */           if (ix > -1) {
/*  740 */             String[] pushs = toTrimmedTokens(part.substring(ix).trim(), ' ');
/*  741 */             cc.setPushX(parseFloat(pushs[0], ResizeConstraint.WEIGHT_100));
/*  742 */             cc.setPushY(parseFloat((pushs.length > 1) ? pushs[1] : "", ResizeConstraint.WEIGHT_100));
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  747 */         if (c == 't') {
/*  748 */           ix = startsWithLenient(part, "tag", 3, true);
/*  749 */           if (ix > -1) {
/*  750 */             cc.setTag(part.substring(ix).trim());
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  755 */         if (c == 'w' || c == 'h') {
/*  756 */           if (part.equals("wrap")) {
/*  757 */             cc.setWrap(true);
/*      */             
/*      */             continue;
/*      */           } 
/*  761 */           if (part.startsWith("wrap ")) {
/*  762 */             String gapSz = part.substring(5).trim();
/*  763 */             cc.setWrapGapSize(parseBoundSize(gapSz, true, true));
/*      */             
/*      */             continue;
/*      */           } 
/*  767 */           boolean isHor = (c == 'w');
/*  768 */           if (isHor && (part.startsWith("w ") || part.startsWith("width "))) {
/*  769 */             String uvStr = part.substring((part.charAt(1) == ' ') ? 2 : 6).trim();
/*  770 */             cc.getHorizontal().setSize(parseBoundSize(uvStr, false, true));
/*      */             
/*      */             continue;
/*      */           } 
/*  774 */           if (!isHor && (part.startsWith("h ") || part.startsWith("height "))) {
/*  775 */             String uvStr = part.substring((part.charAt(1) == ' ') ? 2 : 7).trim();
/*  776 */             cc.getVertical().setSize(parseBoundSize(uvStr, false, false));
/*      */             
/*      */             continue;
/*      */           } 
/*  780 */           if (part.startsWith("wmin ") || part.startsWith("wmax ") || part.startsWith("hmin ") || part.startsWith("hmax ")) {
/*  781 */             String uvStr = part.substring(5).trim();
/*  782 */             if (uvStr.length() > 0) {
/*  783 */               UnitValue uv = parseUnitValue(uvStr, null, isHor);
/*  784 */               boolean isMin = (part.charAt(3) == 'n');
/*  785 */               DimConstraint dc = isHor ? cc.getHorizontal() : cc.getVertical();
/*  786 */               dc.setSize(new BoundSize(isMin ? uv : dc
/*  787 */                     .getSize().getMin(), dc
/*  788 */                     .getSize().getPreferred(), isMin ? dc
/*  789 */                     .getSize().getMax() : uv, uvStr));
/*      */ 
/*      */               
/*      */               continue;
/*      */             } 
/*      */           } 
/*      */           
/*  796 */           if (part.equals("west")) {
/*  797 */             cc.setDockSide(1);
/*      */             
/*      */             continue;
/*      */           } 
/*  801 */           if (part.startsWith("hidemode ")) {
/*  802 */             cc.setHideMode(Integer.parseInt(part.substring(9)));
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  807 */         if (c == 'i' && part.startsWith("id ")) {
/*  808 */           cc.setId(part.substring(3).trim());
/*  809 */           int dIx = cc.getId().indexOf('.');
/*  810 */           if (dIx == 0 || dIx == cc.getId().length() - 1) {
/*  811 */             throw new IllegalArgumentException("Dot must not be first or last!");
/*      */           }
/*      */           
/*      */           continue;
/*      */         } 
/*  816 */         if (c == 'e') {
/*  817 */           if (part.equals("east")) {
/*  818 */             cc.setDockSide(3);
/*      */             
/*      */             continue;
/*      */           } 
/*  822 */           if (part.equals("external")) {
/*  823 */             cc.setExternal(true);
/*      */             
/*      */             continue;
/*      */           } 
/*  827 */           ix = startsWithLenient(part, new String[] { "endgroupx", "endgroupy", "egx", "egy" }, new int[] { -1, -1, -1, -1 }, true);
/*  828 */           if (ix > -1) {
/*  829 */             String sg = part.substring(ix).trim();
/*  830 */             char lc = part.charAt(ix - 1);
/*  831 */             DimConstraint dc = (lc == 'x') ? cc.getHorizontal() : cc.getVertical();
/*  832 */             dc.setEndGroup(sg);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  837 */         if (c == 'd') {
/*  838 */           if (part.equals("dock north")) {
/*  839 */             cc.setDockSide(0);
/*      */             continue;
/*      */           } 
/*  842 */           if (part.equals("dock west")) {
/*  843 */             cc.setDockSide(1);
/*      */             continue;
/*      */           } 
/*  846 */           if (part.equals("dock south")) {
/*  847 */             cc.setDockSide(2);
/*      */             continue;
/*      */           } 
/*  850 */           if (part.equals("dock east")) {
/*  851 */             cc.setDockSide(3);
/*      */             
/*      */             continue;
/*      */           } 
/*  855 */           if (part.equals("dock center")) {
/*  856 */             cc.getHorizontal().setGrow(Float.valueOf(100.0F));
/*  857 */             cc.getVertical().setGrow(Float.valueOf(100.0F));
/*  858 */             cc.setPushX(Float.valueOf(100.0F));
/*  859 */             cc.setPushY(Float.valueOf(100.0F));
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  864 */         if (c == 'v') {
/*  865 */           ix = startsWithLenient(part, new String[] { "visualpadding", "vp" }, new int[] { 3, 2 }, true);
/*  866 */           if (ix > -1) {
/*  867 */             UnitValue[] p = parseInsets(part.substring(ix).trim(), false);
/*  868 */             cc.setVisualPadding(new UnitValue[] { p[0], (p.length > 1) ? p[1] : null, (p.length > 2) ? p[2] : null, (p.length > 3) ? p[3] : null });
/*      */ 
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  877 */         UnitValue horAlign = parseAlignKeywords(part, true);
/*  878 */         if (horAlign != null) {
/*  879 */           cc.getHorizontal().setAlign(horAlign);
/*      */         }
/*      */         else {
/*      */           
/*  883 */           UnitValue verAlign = parseAlignKeywords(part, false);
/*  884 */           if (verAlign != null)
/*  885 */           { cc.getVertical().setAlign(verAlign); }
/*      */           
/*      */           else
/*      */           
/*  889 */           { throw new IllegalArgumentException("Unknown keyword."); } 
/*      */         } 
/*  891 */       } catch (Exception ex) {
/*  892 */         throw new IllegalArgumentException("Error parsing Constraint: '" + part + "'", ex);
/*      */       } 
/*      */       
/*      */       continue;
/*      */     } 
/*      */     
/*  898 */     return cc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static UnitValue[] parseInsets(String s, boolean acceptPanel) {
/*  909 */     if (s.length() == 0 || s.equals("dialog") || s.equals("panel")) {
/*  910 */       if (!acceptPanel) {
/*  911 */         throw new IllegalArgumentException("Insets now allowed: " + s + "\n");
/*      */       }
/*  913 */       boolean isPanel = s.startsWith("p");
/*  914 */       UnitValue[] arrayOfUnitValue = new UnitValue[4];
/*  915 */       for (int i = 0; i < 4; i++) {
/*  916 */         arrayOfUnitValue[i] = isPanel ? PlatformDefaults.getPanelInsets(i) : PlatformDefaults.getDialogInsets(i);
/*      */       }
/*  918 */       return arrayOfUnitValue;
/*      */     } 
/*  920 */     String[] insS = toTrimmedTokens(s, ' ');
/*  921 */     UnitValue[] ins = new UnitValue[4];
/*  922 */     for (int j = 0; j < 4; j++) {
/*  923 */       UnitValue insSz = parseUnitValue(insS[(j < insS.length) ? j : (insS.length - 1)], UnitValue.ZERO, (j % 2 == 1));
/*  924 */       ins[j] = (insSz != null) ? insSz : PlatformDefaults.getPanelInsets(j);
/*      */     } 
/*  926 */     return ins;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static BoundSize[] parseGaps(String s) {
/*  937 */     BoundSize[] ret = new BoundSize[4];
/*      */     
/*  939 */     int ix = startsWithLenient(s, "gaptop", -1, true);
/*  940 */     if (ix > -1) {
/*  941 */       s = s.substring(ix).trim();
/*  942 */       ret[0] = parseBoundSize(s, true, false);
/*  943 */       return ret;
/*      */     } 
/*      */     
/*  946 */     ix = startsWithLenient(s, "gapleft", -1, true);
/*  947 */     if (ix > -1) {
/*  948 */       s = s.substring(ix).trim();
/*  949 */       ret[1] = parseBoundSize(s, true, true);
/*  950 */       return ret;
/*      */     } 
/*      */     
/*  953 */     ix = startsWithLenient(s, "gapbottom", -1, true);
/*  954 */     if (ix > -1) {
/*  955 */       s = s.substring(ix).trim();
/*  956 */       ret[2] = parseBoundSize(s, true, false);
/*  957 */       return ret;
/*      */     } 
/*      */     
/*  960 */     ix = startsWithLenient(s, "gapright", -1, true);
/*  961 */     if (ix > -1) {
/*  962 */       s = s.substring(ix).trim();
/*  963 */       ret[3] = parseBoundSize(s, true, true);
/*  964 */       return ret;
/*      */     } 
/*      */     
/*  967 */     ix = startsWithLenient(s, "gapbefore", -1, true);
/*  968 */     if (ix > -1) {
/*  969 */       s = s.substring(ix).trim();
/*  970 */       ret[1] = parseBoundSize(s, true, true);
/*  971 */       return ret;
/*      */     } 
/*      */     
/*  974 */     ix = startsWithLenient(s, "gapafter", -1, true);
/*  975 */     if (ix > -1) {
/*  976 */       s = s.substring(ix).trim();
/*  977 */       ret[3] = parseBoundSize(s, true, true);
/*  978 */       return ret;
/*      */     } 
/*      */     
/*  981 */     ix = startsWithLenient(s, new String[] { "gapx", "gapy" }, (int[])null, true);
/*  982 */     if (ix > -1) {
/*  983 */       boolean x = (s.charAt(3) == 'x');
/*  984 */       String[] gaps = toTrimmedTokens(s.substring(ix).trim(), ' ');
/*  985 */       ret[x ? 1 : 0] = parseBoundSize(gaps[0], true, x);
/*  986 */       if (gaps.length > 1)
/*  987 */         ret[x ? 3 : 2] = parseBoundSize(gaps[1], true, !x); 
/*  988 */       return ret;
/*      */     } 
/*      */     
/*  991 */     ix = startsWithLenient(s, "gap ", 1, true);
/*  992 */     if (ix > -1) {
/*  993 */       String[] gaps = toTrimmedTokens(s.substring(ix).trim(), ' ');
/*      */       
/*  995 */       ret[1] = parseBoundSize(gaps[0], true, true);
/*  996 */       if (gaps.length > 1) {
/*  997 */         ret[3] = parseBoundSize(gaps[1], true, false);
/*  998 */         if (gaps.length > 2) {
/*  999 */           ret[0] = parseBoundSize(gaps[2], true, true);
/* 1000 */           if (gaps.length > 3)
/* 1001 */             ret[2] = parseBoundSize(gaps[3], true, false); 
/*      */         } 
/*      */       } 
/* 1004 */       return ret;
/*      */     } 
/*      */     
/* 1007 */     throw new IllegalArgumentException("Unknown Gap part: '" + s + "'");
/*      */   }
/*      */ 
/*      */   
/*      */   private static int parseSpan(String s) {
/* 1012 */     return (s.length() > 0) ? Integer.parseInt(s) : 2097051;
/*      */   }
/*      */ 
/*      */   
/*      */   private static Float parseFloat(String s, Float nullVal) {
/* 1017 */     return (s.length() > 0) ? new Float(Float.parseFloat(s)) : nullVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BoundSize parseBoundSize(String s, boolean isGap, boolean isHor) {
/* 1028 */     if (s.length() == 0 || s.equals("null") || s.equals("n")) {
/* 1029 */       return null;
/*      */     }
/* 1031 */     String cs = s;
/* 1032 */     boolean push = false;
/* 1033 */     if (s.endsWith("push")) {
/* 1034 */       push = true;
/* 1035 */       int l = s.length();
/* 1036 */       s = s.substring(0, l - (s.endsWith(":push") ? 5 : 4));
/* 1037 */       if (s.length() == 0) {
/* 1038 */         return new BoundSize(null, null, null, true, cs);
/*      */       }
/*      */     } 
/* 1041 */     String[] sizes = toTrimmedTokens(s, ':');
/* 1042 */     String s0 = sizes[0];
/*      */     
/* 1044 */     if (sizes.length == 1) {
/* 1045 */       boolean hasEM = s0.endsWith("!");
/* 1046 */       if (hasEM)
/* 1047 */         s0 = s0.substring(0, s0.length() - 1); 
/* 1048 */       UnitValue uv = parseUnitValue(s0, null, isHor);
/* 1049 */       return new BoundSize((isGap || hasEM) ? uv : null, uv, hasEM ? uv : null, push, cs);
/*      */     } 
/* 1051 */     if (sizes.length == 2)
/* 1052 */       return new BoundSize(parseUnitValue(s0, null, isHor), parseUnitValue(sizes[1], null, isHor), null, push, cs); 
/* 1053 */     if (sizes.length == 3) {
/* 1054 */       return new BoundSize(parseUnitValue(s0, null, isHor), parseUnitValue(sizes[1], null, isHor), parseUnitValue(sizes[2], null, isHor), push, cs);
/*      */     }
/* 1056 */     throw new IllegalArgumentException("Min:Preferred:Max size section must contain 0, 1 or 2 colons. '" + cs + "'");
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
/*      */   public static UnitValue parseUnitValueOrAlign(String s, boolean isHor, UnitValue emptyReplacement) {
/* 1068 */     if (s.length() == 0) {
/* 1069 */       return emptyReplacement;
/*      */     }
/* 1071 */     UnitValue align = parseAlignKeywords(s, isHor);
/* 1072 */     if (align != null) {
/* 1073 */       return align;
/*      */     }
/* 1075 */     return parseUnitValue(s, emptyReplacement, isHor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static UnitValue parseUnitValue(String s, boolean isHor) {
/* 1085 */     return parseUnitValue(s, null, isHor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static UnitValue parseUnitValue(String s, UnitValue emptyReplacement, boolean isHor) {
/* 1096 */     if (s == null || s.length() == 0) {
/* 1097 */       return emptyReplacement;
/*      */     }
/* 1099 */     String cs = s;
/* 1100 */     char c0 = s.charAt(0);
/*      */ 
/*      */     
/* 1103 */     if (c0 == '(' && s.charAt(s.length() - 1) == ')') {
/* 1104 */       s = s.substring(1, s.length() - 1);
/*      */     }
/* 1106 */     if (c0 == 'n' && (s.equals("null") || s.equals("n"))) {
/* 1107 */       return null;
/*      */     }
/* 1109 */     if (c0 == 'i' && s.equals("inf")) {
/* 1110 */       return UnitValue.INF;
/*      */     }
/* 1112 */     int oper = getOper(s);
/* 1113 */     boolean inline = (oper == 101 || oper == 102 || oper == 103 || oper == 104);
/*      */     
/* 1115 */     if (oper != 100) {
/*      */       String[] uvs;
/*      */       
/* 1118 */       if (!inline) {
/* 1119 */         String sub = s.substring(4, s.length() - 1).trim();
/* 1120 */         uvs = toTrimmedTokens(sub, ',');
/* 1121 */         if (uvs.length == 1)
/* 1122 */           return parseUnitValue(sub, null, isHor); 
/*      */       } else {
/*      */         char delim;
/* 1125 */         if (oper == 101) {
/* 1126 */           delim = '+';
/* 1127 */         } else if (oper == 102) {
/* 1128 */           delim = '-';
/* 1129 */         } else if (oper == 103) {
/* 1130 */           delim = '*';
/*      */         } else {
/* 1132 */           delim = '/';
/*      */         } 
/* 1134 */         uvs = toTrimmedTokens(s, delim);
/* 1135 */         if (uvs.length > 2) {
/* 1136 */           String last = uvs[uvs.length - 1];
/* 1137 */           String first = s.substring(0, s.length() - last.length() - 1);
/* 1138 */           uvs = new String[] { first, last };
/*      */         } 
/*      */       } 
/*      */       
/* 1142 */       if (uvs.length != 2) {
/* 1143 */         throw new IllegalArgumentException("Malformed UnitValue: '" + s + "'");
/*      */       }
/* 1145 */       UnitValue sub1 = parseUnitValue(uvs[0], null, isHor);
/* 1146 */       UnitValue sub2 = parseUnitValue(uvs[1], null, isHor);
/*      */       
/* 1148 */       if (sub1 == null || sub2 == null) {
/* 1149 */         throw new IllegalArgumentException("Malformed UnitValue. Must be two sub-values: '" + s + "'");
/*      */       }
/* 1151 */       return new UnitValue(isHor, oper, sub1, sub2, cs);
/*      */     } 
/*      */     try {
/* 1154 */       String[] numParts = getNumTextParts(s);
/* 1155 */       float value = (numParts[0].length() > 0) ? Float.parseFloat(numParts[0]) : 1.0F;
/*      */       
/* 1157 */       return new UnitValue(value, numParts[1], isHor, oper, cs);
/*      */     }
/* 1159 */     catch (Exception e) {
/* 1160 */       throw new IllegalArgumentException("Malformed UnitValue: '" + s + "'", e);
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
/*      */   static UnitValue parseAlignKeywords(String s, boolean isHor) {
/* 1172 */     if (startsWithLenient(s, "center", 1, false) != -1) {
/* 1173 */       return UnitValue.CENTER;
/*      */     }
/* 1175 */     if (isHor) {
/* 1176 */       if (startsWithLenient(s, "left", 1, false) != -1) {
/* 1177 */         return UnitValue.LEFT;
/*      */       }
/* 1179 */       if (startsWithLenient(s, "right", 1, false) != -1) {
/* 1180 */         return UnitValue.RIGHT;
/*      */       }
/* 1182 */       if (startsWithLenient(s, "leading", 4, false) != -1) {
/* 1183 */         return UnitValue.LEADING;
/*      */       }
/* 1185 */       if (startsWithLenient(s, "trailing", 5, false) != -1) {
/* 1186 */         return UnitValue.TRAILING;
/*      */       }
/* 1188 */       if (startsWithLenient(s, "label", 5, false) != -1) {
/* 1189 */         return UnitValue.LABEL;
/*      */       }
/*      */     } else {
/*      */       
/* 1193 */       if (startsWithLenient(s, "baseline", 4, false) != -1) {
/* 1194 */         return UnitValue.BASELINE_IDENTITY;
/*      */       }
/* 1196 */       if (startsWithLenient(s, "top", 1, false) != -1) {
/* 1197 */         return UnitValue.TOP;
/*      */       }
/* 1199 */       if (startsWithLenient(s, "bottom", 1, false) != -1) {
/* 1200 */         return UnitValue.BOTTOM;
/*      */       }
/*      */     } 
/* 1203 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String[] getNumTextParts(String s) {
/* 1213 */     for (int i = 0, iSz = s.length(); i < iSz; i++) {
/* 1214 */       char c = s.charAt(i);
/* 1215 */       if (c == ' ') {
/* 1216 */         throw new IllegalArgumentException("Space in UnitValue: '" + s + "'");
/*      */       }
/* 1218 */       if ((c < '0' || c > '9') && c != '.' && c != '-')
/* 1219 */         return new String[] { s.substring(0, i).trim(), s.substring(i).trim() }; 
/*      */     } 
/* 1221 */     return new String[] { s, "" };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getOper(String s) {
/* 1230 */     int len = s.length();
/* 1231 */     if (len < 3) {
/* 1232 */       return 100;
/*      */     }
/* 1234 */     if (len > 5 && s.charAt(3) == '(' && s.charAt(len - 1) == ')') {
/* 1235 */       if (s.startsWith("min(")) {
/* 1236 */         return 105;
/*      */       }
/* 1238 */       if (s.startsWith("max(")) {
/* 1239 */         return 106;
/*      */       }
/* 1241 */       if (s.startsWith("mid(")) {
/* 1242 */         return 107;
/*      */       }
/*      */     } 
/*      */     
/* 1246 */     for (int j = 0; j < 2; j++) {
/* 1247 */       for (int i = len - 1, p = 0; i > 0; i--) {
/* 1248 */         char c = s.charAt(i);
/* 1249 */         if (c == ')') {
/* 1250 */           p++;
/* 1251 */         } else if (c == '(') {
/* 1252 */           p--;
/* 1253 */         } else if (p == 0) {
/* 1254 */           if (j == 0) {
/* 1255 */             if (c == '+')
/* 1256 */               return 101; 
/* 1257 */             if (c == '-')
/* 1258 */               return 102; 
/*      */           } else {
/* 1260 */             if (c == '*')
/* 1261 */               return 103; 
/* 1262 */             if (c == '/')
/* 1263 */               return 104; 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1268 */     return 100;
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
/*      */   private static int startsWithLenient(String s, String[] matches, int[] minChars, boolean acceptTrailing) {
/* 1287 */     for (int i = 0; i < matches.length; i++) {
/* 1288 */       int minChar = (minChars != null) ? minChars[i] : -1;
/* 1289 */       int ix = startsWithLenient(s, matches[i], minChar, acceptTrailing);
/* 1290 */       if (ix > -1)
/* 1291 */         return ix; 
/*      */     } 
/* 1293 */     return -1;
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
/*      */   private static int startsWithLenient(String s, String match, int minChars, boolean acceptTrailing) {
/* 1309 */     if (s.charAt(0) != match.charAt(0)) {
/* 1310 */       return -1;
/*      */     }
/* 1312 */     if (minChars == -1) {
/* 1313 */       minChars = match.length();
/*      */     }
/* 1315 */     int sSz = s.length();
/* 1316 */     if (sSz < minChars) {
/* 1317 */       return -1;
/*      */     }
/* 1319 */     int mSz = match.length();
/* 1320 */     int sIx = 0;
/* 1321 */     for (int mIx = 0; mIx < mSz; sIx++, mIx++) {
/* 1322 */       while (sIx < sSz && (s.charAt(sIx) == ' ' || s.charAt(sIx) == '_')) {
/* 1323 */         sIx++;
/*      */       }
/* 1325 */       if (sIx >= sSz || s.charAt(sIx) != match.charAt(mIx))
/* 1326 */         return (mIx >= minChars && (acceptTrailing || sIx >= sSz) && (sIx >= sSz || s.charAt(sIx - 1) == ' ')) ? sIx : -1; 
/*      */     } 
/* 1328 */     return (sIx >= sSz || acceptTrailing || s.charAt(sIx) == ' ') ? sIx : -1;
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
/*      */   private static String[] toTrimmedTokens(String s, char sep) {
/* 1344 */     int toks = 0, sSize = s.length();
/* 1345 */     boolean disregardDoubles = (sep == ' ');
/*      */ 
/*      */     
/* 1348 */     int p = 0;
/* 1349 */     for (int i = 0; i < sSize; i++) {
/* 1350 */       char c = s.charAt(i);
/* 1351 */       if (c == '(') {
/* 1352 */         p++;
/* 1353 */       } else if (c == ')') {
/* 1354 */         p--;
/* 1355 */       } else if (p == 0 && c == sep) {
/* 1356 */         toks++;
/* 1357 */         while (disregardDoubles && i < sSize - 1 && s.charAt(i + 1) == ' ')
/* 1358 */           i++; 
/*      */       } 
/* 1360 */       if (p < 0)
/* 1361 */         throw new IllegalArgumentException("Unbalanced parentheses: '" + s + "'"); 
/*      */     } 
/* 1363 */     if (p != 0) {
/* 1364 */       throw new IllegalArgumentException("Unbalanced parentheses: '" + s + "'");
/*      */     }
/* 1366 */     if (toks == 0) {
/* 1367 */       return new String[] { s.trim() };
/*      */     }
/* 1369 */     String[] retArr = new String[toks + 1];
/*      */     
/* 1371 */     int st = 0, pNr = 0;
/* 1372 */     p = 0;
/* 1373 */     for (int j = 0; j < sSize; j++) {
/*      */       
/* 1375 */       char c = s.charAt(j);
/* 1376 */       if (c == '(') {
/* 1377 */         p++;
/* 1378 */       } else if (c == ')') {
/* 1379 */         p--;
/* 1380 */       } else if (p == 0 && c == sep) {
/* 1381 */         retArr[pNr++] = s.substring(st, j).trim();
/* 1382 */         st = j + 1;
/* 1383 */         while (disregardDoubles && j < sSize - 1 && s.charAt(j + 1) == ' ') {
/* 1384 */           j++;
/*      */         }
/*      */       } 
/*      */     } 
/* 1388 */     retArr[pNr++] = s.substring(st, sSize).trim();
/* 1389 */     return retArr;
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
/*      */   private static ArrayList<String> getRowColAndGapsTrimmed(String s) {
/* 1402 */     if (s.indexOf('|') != -1) {
/* 1403 */       s = s.replaceAll("\\|", "][");
/*      */     }
/* 1405 */     ArrayList<String> retList = new ArrayList<>(Math.max(s.length() >> 3, 3));
/* 1406 */     int s0 = 0, s1 = 0;
/* 1407 */     int st = 0;
/* 1408 */     for (int i = 0, iSz = s.length(); i < iSz; i++) {
/* 1409 */       char c = s.charAt(i);
/* 1410 */       if (c == '[') {
/* 1411 */         s0++;
/* 1412 */       } else if (c == ']') {
/* 1413 */         s1++;
/*      */       } else {
/*      */         continue;
/*      */       } 
/*      */       
/* 1418 */       if (s0 != s1 && s0 - 1 != s1) {
/*      */         break;
/*      */       }
/* 1421 */       retList.add(s.substring(st, i).trim());
/* 1422 */       st = i + 1; continue;
/*      */     } 
/* 1424 */     if (s0 != s1) {
/* 1425 */       throw new IllegalArgumentException("'[' and ']' mismatch in row/column format string: " + s);
/*      */     }
/* 1427 */     if (s0 == 0) {
/* 1428 */       retList.add("");
/* 1429 */       retList.add(s);
/* 1430 */       retList.add("");
/* 1431 */     } else if (retList.size() % 2 == 0) {
/* 1432 */       retList.add(s.substring(st, s.length()));
/*      */     } 
/*      */     
/* 1435 */     return retList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String prepare(String s) {
/* 1444 */     return (s != null) ? s.trim().toLowerCase() : "";
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\ConstraintParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */