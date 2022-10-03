/*     */ package org.mozilla.javascript.optimizer;
/*     */ import java.util.BitSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.mozilla.javascript.Node;
/*     */ import org.mozilla.javascript.ObjArray;
/*     */ import org.mozilla.javascript.ObjToIntMap;
/*     */ import org.mozilla.javascript.ast.Jump;
/*     */ 
/*     */ class Block {
/*     */   private Block[] itsSuccessors;
/*     */   private Block[] itsPredecessors;
/*     */   private int itsStartNodeIndex;
/*     */   private int itsEndNodeIndex;
/*     */   private int itsBlockID;
/*     */   private BitSet itsLiveOnEntrySet;
/*     */   private BitSet itsLiveOnExitSet;
/*     */   private BitSet itsUseBeforeDefSet;
/*     */   private BitSet itsNotDefSet;
/*     */   static final boolean DEBUG = false;
/*     */   private static int debug_blockCount;
/*     */   
/*     */   private static class FatBlock {
/*     */     private static Block[] reduceToArray(ObjToIntMap map) {
/*  25 */       Block[] result = null;
/*  26 */       if (!map.isEmpty()) {
/*  27 */         result = new Block[map.size()];
/*  28 */         int i = 0;
/*  29 */         ObjToIntMap.Iterator iter = map.newIterator();
/*  30 */         iter.start(); for (; !iter.done(); iter.next()) {
/*  31 */           FatBlock fb = (FatBlock)iter.getKey();
/*  32 */           result[i++] = fb.realBlock;
/*     */         } 
/*     */       } 
/*  35 */       return result;
/*     */     }
/*     */     
/*  38 */     void addSuccessor(FatBlock b) { this.successors.put(b, 0); } void addPredecessor(FatBlock b) {
/*  39 */       this.predecessors.put(b, 0);
/*     */     }
/*  41 */     Block[] getSuccessors() { return reduceToArray(this.successors); } Block[] getPredecessors() {
/*  42 */       return reduceToArray(this.predecessors);
/*     */     }
/*     */     
/*  45 */     private ObjToIntMap successors = new ObjToIntMap();
/*     */     
/*  47 */     private ObjToIntMap predecessors = new ObjToIntMap();
/*     */     Block realBlock;
/*     */     
/*     */     private FatBlock() {}
/*     */   }
/*     */   
/*     */   Block(int startNodeIndex, int endNodeIndex) {
/*  54 */     this.itsStartNodeIndex = startNodeIndex;
/*  55 */     this.itsEndNodeIndex = endNodeIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   static void runFlowAnalyzes(OptFunctionNode fn, Node[] statementNodes) {
/*  60 */     int paramCount = fn.fnode.getParamCount();
/*  61 */     int varCount = fn.fnode.getParamAndVarCount();
/*  62 */     int[] varTypes = new int[varCount];
/*     */     int i;
/*  64 */     for (i = 0; i != paramCount; i++) {
/*  65 */       varTypes[i] = 3;
/*     */     }
/*     */ 
/*     */     
/*  69 */     for (i = paramCount; i != varCount; i++) {
/*  70 */       varTypes[i] = 0;
/*     */     }
/*     */     
/*  73 */     Block[] theBlocks = buildBlocks(statementNodes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     reachingDefDataFlow(fn, statementNodes, theBlocks, varTypes);
/*  83 */     typeFlow(fn, statementNodes, theBlocks, varTypes);
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
/*  96 */     for (int j = paramCount; j != varCount; j++) {
/*  97 */       if (varTypes[j] == 1) {
/*  98 */         fn.setIsNumberVar(j);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Block[] buildBlocks(Node[] statementNodes) {
/* 107 */     Map<Node, FatBlock> theTargetBlocks = new HashMap<Node, FatBlock>();
/* 108 */     ObjArray theBlocks = new ObjArray();
/*     */ 
/*     */     
/* 111 */     int beginNodeIndex = 0;
/*     */     int i;
/* 113 */     for (i = 0; i < statementNodes.length; i++) {
/* 114 */       FatBlock fb; switch (statementNodes[i].getType()) {
/*     */         
/*     */         case 131:
/* 117 */           if (i != beginNodeIndex) {
/* 118 */             FatBlock fatBlock = newFatBlock(beginNodeIndex, i - 1);
/* 119 */             if (statementNodes[beginNodeIndex].getType() == 131) {
/* 120 */               theTargetBlocks.put(statementNodes[beginNodeIndex], fatBlock);
/*     */             }
/* 122 */             theBlocks.add(fatBlock);
/*     */             
/* 124 */             beginNodeIndex = i;
/*     */           } 
/*     */           break;
/*     */ 
/*     */         
/*     */         case 5:
/*     */         case 6:
/*     */         case 7:
/* 132 */           fb = newFatBlock(beginNodeIndex, i);
/* 133 */           if (statementNodes[beginNodeIndex].getType() == 131) {
/* 134 */             theTargetBlocks.put(statementNodes[beginNodeIndex], fb);
/*     */           }
/* 136 */           theBlocks.add(fb);
/*     */           
/* 138 */           beginNodeIndex = i + 1;
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 144 */     if (beginNodeIndex != statementNodes.length) {
/* 145 */       FatBlock fb = newFatBlock(beginNodeIndex, statementNodes.length - 1);
/* 146 */       if (statementNodes[beginNodeIndex].getType() == 131) {
/* 147 */         theTargetBlocks.put(statementNodes[beginNodeIndex], fb);
/*     */       }
/* 149 */       theBlocks.add(fb);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 154 */     for (i = 0; i < theBlocks.size(); i++) {
/* 155 */       FatBlock fb = (FatBlock)theBlocks.get(i);
/*     */       
/* 157 */       Node blockEndNode = statementNodes[fb.realBlock.itsEndNodeIndex];
/* 158 */       int blockEndNodeType = blockEndNode.getType();
/*     */       
/* 160 */       if (blockEndNodeType != 5 && i < theBlocks.size() - 1) {
/* 161 */         FatBlock fallThruTarget = (FatBlock)theBlocks.get(i + 1);
/* 162 */         fb.addSuccessor(fallThruTarget);
/* 163 */         fallThruTarget.addPredecessor(fb);
/*     */       } 
/*     */ 
/*     */       
/* 167 */       if (blockEndNodeType == 7 || blockEndNodeType == 6 || blockEndNodeType == 5) {
/*     */ 
/*     */         
/* 170 */         Node target = ((Jump)blockEndNode).target;
/* 171 */         FatBlock branchTargetBlock = theTargetBlocks.get(target);
/* 172 */         target.putProp(6, branchTargetBlock.realBlock);
/* 173 */         fb.addSuccessor(branchTargetBlock);
/* 174 */         branchTargetBlock.addPredecessor(fb);
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     Block[] result = new Block[theBlocks.size()];
/*     */     
/* 180 */     for (int j = 0; j < theBlocks.size(); j++) {
/* 181 */       FatBlock fb = (FatBlock)theBlocks.get(j);
/* 182 */       Block b = fb.realBlock;
/* 183 */       b.itsSuccessors = fb.getSuccessors();
/* 184 */       b.itsPredecessors = fb.getPredecessors();
/* 185 */       b.itsBlockID = j;
/* 186 */       result[j] = b;
/*     */     } 
/*     */     
/* 189 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static FatBlock newFatBlock(int startNodeIndex, int endNodeIndex) {
/* 194 */     FatBlock fb = new FatBlock();
/* 195 */     fb.realBlock = new Block(startNodeIndex, endNodeIndex);
/* 196 */     return fb;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String toString(Block[] blockList, Node[] statementNodes) {
/* 201 */     return null;
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
/*     */   private static void reachingDefDataFlow(OptFunctionNode fn, Node[] statementNodes, Block[] theBlocks, int[] varTypes) {
/* 246 */     for (int i = 0; i < theBlocks.length; i++) {
/* 247 */       theBlocks[i].initLiveOnEntrySets(fn, statementNodes);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 254 */     boolean[] visit = new boolean[theBlocks.length];
/* 255 */     boolean[] doneOnce = new boolean[theBlocks.length];
/* 256 */     int vIndex = theBlocks.length - 1;
/* 257 */     boolean needRescan = false;
/* 258 */     visit[vIndex] = true; while (true) {
/*     */       int j;
/* 260 */       if (visit[vIndex] || !doneOnce[vIndex]) {
/* 261 */         doneOnce[vIndex] = true;
/* 262 */         visit[vIndex] = false;
/* 263 */         if (theBlocks[vIndex].doReachedUseDataFlow()) {
/* 264 */           Block[] pred = (theBlocks[vIndex]).itsPredecessors;
/* 265 */           if (pred != null) {
/* 266 */             for (int k = 0; k < pred.length; k++) {
/* 267 */               int index = (pred[k]).itsBlockID;
/* 268 */               visit[index] = true;
/* 269 */               j = needRescan | ((index > vIndex) ? 1 : 0);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 274 */       if (vIndex == 0) {
/* 275 */         if (j != 0) {
/* 276 */           vIndex = theBlocks.length - 1;
/* 277 */           j = 0;
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 282 */       vIndex--;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     theBlocks[0].markAnyTypeVariables(varTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void typeFlow(OptFunctionNode fn, Node[] statementNodes, Block[] theBlocks, int[] varTypes) {
/* 297 */     boolean[] visit = new boolean[theBlocks.length];
/* 298 */     boolean[] doneOnce = new boolean[theBlocks.length];
/* 299 */     int vIndex = 0;
/* 300 */     boolean needRescan = false;
/* 301 */     visit[vIndex] = true; while (true) {
/*     */       int i;
/* 303 */       if (visit[vIndex] || !doneOnce[vIndex]) {
/* 304 */         doneOnce[vIndex] = true;
/* 305 */         visit[vIndex] = false;
/* 306 */         if (theBlocks[vIndex].doTypeFlow(fn, statementNodes, varTypes)) {
/*     */           
/* 308 */           Block[] succ = (theBlocks[vIndex]).itsSuccessors;
/* 309 */           if (succ != null) {
/* 310 */             for (int j = 0; j < succ.length; j++) {
/* 311 */               int index = (succ[j]).itsBlockID;
/* 312 */               visit[index] = true;
/* 313 */               i = needRescan | ((index < vIndex) ? 1 : 0);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 318 */       if (vIndex == theBlocks.length - 1) {
/* 319 */         if (i != 0) {
/* 320 */           vIndex = 0;
/* 321 */           i = 0;
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 326 */       vIndex++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean assignType(int[] varTypes, int index, int type) {
/* 333 */     int prev = varTypes[index];
/* 334 */     varTypes[index] = varTypes[index] | type; return (prev != (varTypes[index] | type));
/*     */   }
/*     */ 
/*     */   
/*     */   private void markAnyTypeVariables(int[] varTypes) {
/* 339 */     for (int i = 0; i != varTypes.length; i++) {
/* 340 */       if (this.itsLiveOnEntrySet.get(i)) {
/* 341 */         assignType(varTypes, i, 3);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void lookForVariableAccess(OptFunctionNode fn, Node n) {
/*     */     int i;
/*     */     Node node1, lhs;
/*     */     int varIndex;
/*     */     Node rhs;
/* 357 */     switch (n.getType()) {
/*     */ 
/*     */ 
/*     */       
/*     */       case 137:
/* 362 */         i = fn.fnode.getIndexForNameNode(n);
/* 363 */         if (i > -1 && !this.itsNotDefSet.get(i)) {
/* 364 */           this.itsUseBeforeDefSet.set(i);
/*     */         }
/*     */         return;
/*     */       
/*     */       case 106:
/*     */       case 107:
/* 370 */         node1 = n.getFirstChild();
/* 371 */         if (node1.getType() == 55) {
/* 372 */           int j = fn.getVarIndex(node1);
/* 373 */           if (!this.itsNotDefSet.get(j))
/* 374 */             this.itsUseBeforeDefSet.set(j); 
/* 375 */           this.itsNotDefSet.set(j);
/*     */         } else {
/* 377 */           lookForVariableAccess(fn, node1);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 56:
/*     */       case 156:
/* 384 */         lhs = n.getFirstChild();
/* 385 */         rhs = lhs.getNext();
/* 386 */         lookForVariableAccess(fn, rhs);
/* 387 */         this.itsNotDefSet.set(fn.getVarIndex(n));
/*     */         return;
/*     */ 
/*     */       
/*     */       case 55:
/* 392 */         varIndex = fn.getVarIndex(n);
/* 393 */         if (!this.itsNotDefSet.get(varIndex)) {
/* 394 */           this.itsUseBeforeDefSet.set(varIndex);
/*     */         }
/*     */         return;
/*     */     } 
/* 398 */     Node child = n.getFirstChild();
/* 399 */     while (child != null) {
/* 400 */       lookForVariableAccess(fn, child);
/* 401 */       child = child.getNext();
/*     */     } 
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
/*     */   private void initLiveOnEntrySets(OptFunctionNode fn, Node[] statementNodes) {
/* 414 */     int listLength = fn.getVarCount();
/* 415 */     this.itsUseBeforeDefSet = new BitSet(listLength);
/* 416 */     this.itsNotDefSet = new BitSet(listLength);
/* 417 */     this.itsLiveOnEntrySet = new BitSet(listLength);
/* 418 */     this.itsLiveOnExitSet = new BitSet(listLength);
/* 419 */     for (int i = this.itsStartNodeIndex; i <= this.itsEndNodeIndex; i++) {
/* 420 */       Node n = statementNodes[i];
/* 421 */       lookForVariableAccess(fn, n);
/*     */     } 
/* 423 */     this.itsNotDefSet.flip(0, listLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean doReachedUseDataFlow() {
/* 434 */     this.itsLiveOnExitSet.clear();
/* 435 */     if (this.itsSuccessors != null) {
/* 436 */       for (int i = 0; i < this.itsSuccessors.length; i++) {
/* 437 */         this.itsLiveOnExitSet.or((this.itsSuccessors[i]).itsLiveOnEntrySet);
/*     */       }
/*     */     }
/* 440 */     return updateEntrySet(this.itsLiveOnEntrySet, this.itsLiveOnExitSet, this.itsUseBeforeDefSet, this.itsNotDefSet);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean updateEntrySet(BitSet entrySet, BitSet exitSet, BitSet useBeforeDef, BitSet notDef) {
/* 446 */     int card = entrySet.cardinality();
/* 447 */     entrySet.or(exitSet);
/* 448 */     entrySet.and(notDef);
/* 449 */     entrySet.or(useBeforeDef);
/* 450 */     return (entrySet.cardinality() != card);
/*     */   } private static int findExpressionType(OptFunctionNode fn, Node n, int[] varTypes) {
/*     */     Node node1;
/*     */     Node ifTrue;
/*     */     Node child;
/*     */     int i;
/*     */     Node ifFalse;
/*     */     int lType;
/*     */     int j;
/*     */     int ifTrueType;
/*     */     int rType;
/*     */     int ifFalseType;
/* 462 */     switch (n.getType()) {
/*     */       case 40:
/* 464 */         return 1;
/*     */       
/*     */       case 30:
/*     */       case 38:
/*     */       case 70:
/* 469 */         return 3;
/*     */       
/*     */       case 33:
/*     */       case 36:
/*     */       case 39:
/*     */       case 43:
/* 475 */         return 3;
/*     */       
/*     */       case 55:
/* 478 */         return varTypes[fn.getVarIndex(n)];
/*     */       
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 27:
/*     */       case 28:
/*     */       case 29:
/*     */       case 106:
/*     */       case 107:
/* 495 */         return 1;
/*     */ 
/*     */       
/*     */       case 126:
/* 499 */         return 3;
/*     */ 
/*     */       
/*     */       case 12:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 17:
/*     */       case 26:
/*     */       case 31:
/*     */       case 44:
/*     */       case 45:
/*     */       case 46:
/*     */       case 47:
/*     */       case 52:
/*     */       case 53:
/*     */       case 69:
/* 517 */         return 3;
/*     */ 
/*     */       
/*     */       case 32:
/*     */       case 41:
/*     */       case 137:
/* 523 */         return 3;
/*     */       
/*     */       case 42:
/*     */       case 48:
/*     */       case 65:
/*     */       case 66:
/*     */       case 157:
/* 530 */         return 3;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 21:
/* 536 */         node1 = n.getFirstChild();
/* 537 */         i = findExpressionType(fn, node1, varTypes);
/* 538 */         j = findExpressionType(fn, node1.getNext(), varTypes);
/* 539 */         return i | j;
/*     */ 
/*     */       
/*     */       case 102:
/* 543 */         ifTrue = n.getFirstChild().getNext();
/* 544 */         ifFalse = ifTrue.getNext();
/* 545 */         ifTrueType = findExpressionType(fn, ifTrue, varTypes);
/* 546 */         ifFalseType = findExpressionType(fn, ifFalse, varTypes);
/* 547 */         return ifTrueType | ifFalseType;
/*     */ 
/*     */       
/*     */       case 8:
/*     */       case 35:
/*     */       case 37:
/*     */       case 56:
/*     */       case 89:
/*     */       case 156:
/* 556 */         return findExpressionType(fn, n.getLastChild(), varTypes);
/*     */       
/*     */       case 104:
/*     */       case 105:
/* 560 */         child = n.getFirstChild();
/* 561 */         lType = findExpressionType(fn, child, varTypes);
/* 562 */         rType = findExpressionType(fn, child.getNext(), varTypes);
/* 563 */         return lType | rType;
/*     */     } 
/*     */ 
/*     */     
/* 567 */     return 3;
/*     */   }
/*     */   
/*     */   private static boolean findDefPoints(OptFunctionNode fn, Node n, int[] varTypes) {
/*     */     Node rValue;
/*     */     int theType, i;
/* 573 */     boolean result = false;
/* 574 */     Node first = n.getFirstChild();
/* 575 */     for (Node next = first; next != null; next = next.getNext()) {
/* 576 */       result |= findDefPoints(fn, next, varTypes);
/*     */     }
/* 578 */     switch (n.getType()) {
/*     */       case 106:
/*     */       case 107:
/* 581 */         if (first.getType() == 55) {
/*     */           
/* 583 */           int j = fn.getVarIndex(first);
/* 584 */           if (!fn.fnode.getParamAndVarConst()[j]) {
/* 585 */             result |= assignType(varTypes, j, 1);
/*     */           }
/*     */         } 
/*     */         break;
/*     */       case 56:
/*     */       case 156:
/* 591 */         rValue = first.getNext();
/* 592 */         theType = findExpressionType(fn, rValue, varTypes);
/* 593 */         i = fn.getVarIndex(n);
/* 594 */         if (n.getType() != 56 || !fn.fnode.getParamAndVarConst()[i])
/*     */         {
/* 596 */           result |= assignType(varTypes, i, theType);
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 601 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean doTypeFlow(OptFunctionNode fn, Node[] statementNodes, int[] varTypes) {
/* 607 */     boolean changed = false;
/*     */     
/* 609 */     for (int i = this.itsStartNodeIndex; i <= this.itsEndNodeIndex; i++) {
/* 610 */       Node n = statementNodes[i];
/* 611 */       if (n != null) {
/* 612 */         changed |= findDefPoints(fn, n, varTypes);
/*     */       }
/*     */     } 
/*     */     
/* 616 */     return changed;
/*     */   }
/*     */   
/*     */   private void printLiveOnEntrySet(OptFunctionNode fn) {}
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\optimizer\Block.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */