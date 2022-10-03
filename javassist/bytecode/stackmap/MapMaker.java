/*     */ package javassist.bytecode.stackmap;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javassist.ClassPool;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.ByteArray;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.StackMap;
/*     */ import javassist.bytecode.StackMapTable;
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
/*     */ public class MapMaker
/*     */   extends Tracer
/*     */ {
/*     */   public static StackMapTable make(ClassPool classes, MethodInfo minfo) throws BadBytecode {
/*     */     TypedBlock[] blocks;
/*  99 */     CodeAttribute ca = minfo.getCodeAttribute();
/* 100 */     if (ca == null) {
/* 101 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 105 */       blocks = TypedBlock.makeBlocks(minfo, ca, true);
/*     */     }
/* 107 */     catch (JsrBytecode e) {
/* 108 */       return null;
/*     */     } 
/*     */     
/* 111 */     if (blocks == null) {
/* 112 */       return null;
/*     */     }
/* 114 */     MapMaker mm = new MapMaker(classes, minfo, ca);
/*     */     try {
/* 116 */       mm.make(blocks, ca.getCode());
/*     */     }
/* 118 */     catch (BadBytecode bb) {
/* 119 */       throw new BadBytecode(minfo, bb);
/*     */     } 
/*     */     
/* 122 */     return mm.toStackMap(blocks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StackMap make2(ClassPool classes, MethodInfo minfo) throws BadBytecode {
/*     */     TypedBlock[] blocks;
/* 133 */     CodeAttribute ca = minfo.getCodeAttribute();
/* 134 */     if (ca == null) {
/* 135 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 139 */       blocks = TypedBlock.makeBlocks(minfo, ca, true);
/*     */     }
/* 141 */     catch (JsrBytecode e) {
/* 142 */       return null;
/*     */     } 
/*     */     
/* 145 */     if (blocks == null) {
/* 146 */       return null;
/*     */     }
/* 148 */     MapMaker mm = new MapMaker(classes, minfo, ca);
/*     */     try {
/* 150 */       mm.make(blocks, ca.getCode());
/*     */     }
/* 152 */     catch (BadBytecode bb) {
/* 153 */       throw new BadBytecode(minfo, bb);
/*     */     } 
/* 155 */     return mm.toStackMap2(minfo.getConstPool(), blocks);
/*     */   }
/*     */   
/*     */   public MapMaker(ClassPool classes, MethodInfo minfo, CodeAttribute ca) {
/* 159 */     super(classes, minfo.getConstPool(), ca
/* 160 */         .getMaxStack(), ca.getMaxLocals(), 
/* 161 */         TypedBlock.getRetType(minfo.getDescriptor()));
/*     */   }
/*     */   protected MapMaker(MapMaker old) {
/* 164 */     super(old);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void make(TypedBlock[] blocks, byte[] code) throws BadBytecode {
/* 172 */     make(code, blocks[0]);
/* 173 */     findDeadCatchers(code, blocks);
/*     */     try {
/* 175 */       fixTypes(code, blocks);
/* 176 */     } catch (NotFoundException e) {
/* 177 */       throw new BadBytecode("failed to resolve types", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void make(byte[] code, TypedBlock tb) throws BadBytecode {
/* 186 */     copyTypeData(tb.stackTop, tb.stackTypes, this.stackTypes);
/* 187 */     this.stackTop = tb.stackTop;
/* 188 */     copyTypeData(tb.localsTypes.length, tb.localsTypes, this.localsTypes);
/*     */     
/* 190 */     traceException(code, tb.toCatch);
/*     */     
/* 192 */     int pos = tb.position;
/* 193 */     int end = pos + tb.length;
/* 194 */     while (pos < end) {
/* 195 */       pos += doOpcode(pos, code);
/* 196 */       traceException(code, tb.toCatch);
/*     */     } 
/*     */     
/* 199 */     if (tb.exit != null) {
/* 200 */       for (int i = 0; i < tb.exit.length; i++) {
/* 201 */         TypedBlock e = (TypedBlock)tb.exit[i];
/* 202 */         if (e.alreadySet()) {
/* 203 */           mergeMap(e, true);
/*     */         } else {
/* 205 */           recordStackMap(e);
/* 206 */           MapMaker maker = new MapMaker(this);
/* 207 */           maker.make(code, e);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void traceException(byte[] code, BasicBlock.Catch handler) throws BadBytecode {
/* 216 */     while (handler != null) {
/* 217 */       TypedBlock tb = (TypedBlock)handler.body;
/* 218 */       if (tb.alreadySet()) {
/* 219 */         mergeMap(tb, false);
/* 220 */         if (tb.stackTop < 1) {
/* 221 */           throw new BadBytecode("bad catch clause: " + handler.typeIndex);
/*     */         }
/* 223 */         tb.stackTypes[0] = merge(toExceptionType(handler.typeIndex), tb.stackTypes[0]);
/*     */       }
/*     */       else {
/*     */         
/* 227 */         recordStackMap(tb, handler.typeIndex);
/* 228 */         MapMaker maker = new MapMaker(this);
/* 229 */         maker.make(code, tb);
/*     */       } 
/*     */       
/* 232 */       handler = handler.next;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void mergeMap(TypedBlock dest, boolean mergeStack) throws BadBytecode {
/* 237 */     int n = this.localsTypes.length; int i;
/* 238 */     for (i = 0; i < n; i++) {
/* 239 */       dest.localsTypes[i] = merge(validateTypeData(this.localsTypes, n, i), dest.localsTypes[i]);
/*     */     }
/*     */     
/* 242 */     if (mergeStack) {
/* 243 */       n = this.stackTop;
/* 244 */       for (i = 0; i < n; i++)
/* 245 */         dest.stackTypes[i] = merge(this.stackTypes[i], dest.stackTypes[i]); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private TypeData merge(TypeData src, TypeData target) throws BadBytecode {
/* 250 */     if (src == target)
/* 251 */       return target; 
/* 252 */     if (target instanceof TypeData.ClassName || target instanceof TypeData.BasicType)
/*     */     {
/* 254 */       return target; } 
/* 255 */     if (target instanceof TypeData.AbsTypeVar) {
/* 256 */       ((TypeData.AbsTypeVar)target).merge(src);
/* 257 */       return target;
/*     */     } 
/*     */     
/* 260 */     throw new RuntimeException("fatal: this should never happen");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void recordStackMap(TypedBlock target) throws BadBytecode {
/* 266 */     TypeData[] tStackTypes = TypeData.make(this.stackTypes.length);
/* 267 */     int st = this.stackTop;
/* 268 */     recordTypeData(st, this.stackTypes, tStackTypes);
/* 269 */     recordStackMap0(target, st, tStackTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void recordStackMap(TypedBlock target, int exceptionType) throws BadBytecode {
/* 275 */     TypeData[] tStackTypes = TypeData.make(this.stackTypes.length);
/* 276 */     tStackTypes[0] = toExceptionType(exceptionType).join();
/* 277 */     recordStackMap0(target, 1, tStackTypes);
/*     */   }
/*     */   
/*     */   private TypeData.ClassName toExceptionType(int exceptionType) {
/*     */     String type;
/* 282 */     if (exceptionType == 0) {
/* 283 */       type = "java.lang.Throwable";
/*     */     } else {
/* 285 */       type = this.cpool.getClassInfo(exceptionType);
/*     */     } 
/* 287 */     return new TypeData.ClassName(type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void recordStackMap0(TypedBlock target, int st, TypeData[] tStackTypes) throws BadBytecode {
/* 293 */     int n = this.localsTypes.length;
/* 294 */     TypeData[] tLocalsTypes = TypeData.make(n);
/* 295 */     int k = recordTypeData(n, this.localsTypes, tLocalsTypes);
/* 296 */     target.setStackMap(st, tStackTypes, k, tLocalsTypes);
/*     */   }
/*     */   
/*     */   protected static int recordTypeData(int n, TypeData[] srcTypes, TypeData[] destTypes) {
/* 300 */     int k = -1;
/* 301 */     for (int i = 0; i < n; i++) {
/* 302 */       TypeData t = validateTypeData(srcTypes, n, i);
/* 303 */       destTypes[i] = t.join();
/* 304 */       if (t != TOP) {
/* 305 */         k = i + 1;
/*     */       }
/*     */     } 
/* 308 */     return k + 1;
/*     */   }
/*     */   
/*     */   protected static void copyTypeData(int n, TypeData[] srcTypes, TypeData[] destTypes) {
/* 312 */     for (int i = 0; i < n; i++)
/* 313 */       destTypes[i] = srcTypes[i]; 
/*     */   }
/*     */   
/*     */   private static TypeData validateTypeData(TypeData[] data, int length, int index) {
/* 317 */     TypeData td = data[index];
/* 318 */     if (td.is2WordType() && index + 1 < length && 
/* 319 */       data[index + 1] != TOP) {
/* 320 */       return TOP;
/*     */     }
/* 322 */     return td;
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
/*     */   private void findDeadCatchers(byte[] code, TypedBlock[] blocks) throws BadBytecode {
/* 334 */     int len = blocks.length;
/* 335 */     for (int i = 0; i < len; i++) {
/* 336 */       TypedBlock block = blocks[i];
/* 337 */       if (!block.alreadySet()) {
/* 338 */         fixDeadcode(code, block);
/* 339 */         BasicBlock.Catch handler = block.toCatch;
/* 340 */         if (handler != null) {
/* 341 */           TypedBlock tb = (TypedBlock)handler.body;
/* 342 */           if (!tb.alreadySet()) {
/*     */ 
/*     */             
/* 345 */             recordStackMap(tb, handler.typeIndex);
/* 346 */             fixDeadcode(code, tb);
/* 347 */             tb.incoming = 1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void fixDeadcode(byte[] code, TypedBlock block) throws BadBytecode {
/* 356 */     int pos = block.position;
/* 357 */     int len = block.length - 3;
/* 358 */     if (len < 0) {
/*     */       
/* 360 */       if (len == -1) {
/* 361 */         code[pos] = 0;
/*     */       }
/* 363 */       code[pos + block.length - 1] = -65;
/* 364 */       block.incoming = 1;
/* 365 */       recordStackMap(block, 0);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 371 */     block.incoming = 0;
/*     */     
/* 373 */     for (int k = 0; k < len; k++) {
/* 374 */       code[pos + k] = 0;
/*     */     }
/* 376 */     code[pos + len] = -89;
/* 377 */     ByteArray.write16bit(-len, code, pos + len + 1);
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
/*     */   private void fixTypes(byte[] code, TypedBlock[] blocks) throws NotFoundException, BadBytecode {
/* 390 */     List<TypeData> preOrder = new ArrayList<>();
/* 391 */     int len = blocks.length;
/* 392 */     int index = 0;
/* 393 */     for (int i = 0; i < len; i++) {
/* 394 */       TypedBlock block = blocks[i];
/* 395 */       if (block.alreadySet()) {
/* 396 */         int n = block.localsTypes.length; int j;
/* 397 */         for (j = 0; j < n; j++) {
/* 398 */           index = block.localsTypes[j].dfs(preOrder, index, this.classPool);
/*     */         }
/* 400 */         n = block.stackTop;
/* 401 */         for (j = 0; j < n; j++) {
/* 402 */           index = block.stackTypes[j].dfs(preOrder, index, this.classPool);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public StackMapTable toStackMap(TypedBlock[] blocks) {
/* 410 */     StackMapTable.Writer writer = new StackMapTable.Writer(32);
/* 411 */     int n = blocks.length;
/* 412 */     TypedBlock prev = blocks[0];
/* 413 */     int offsetDelta = prev.length;
/* 414 */     if (prev.incoming > 0) {
/* 415 */       writer.sameFrame(0);
/* 416 */       offsetDelta--;
/*     */     } 
/*     */     
/* 419 */     for (int i = 1; i < n; i++) {
/* 420 */       TypedBlock bb = blocks[i];
/* 421 */       if (isTarget(bb, blocks[i - 1])) {
/* 422 */         bb.resetNumLocals();
/* 423 */         int diffL = stackMapDiff(prev.numLocals, prev.localsTypes, bb.numLocals, bb.localsTypes);
/*     */         
/* 425 */         toStackMapBody(writer, bb, diffL, offsetDelta, prev);
/* 426 */         offsetDelta = bb.length - 1;
/* 427 */         prev = bb;
/*     */       }
/* 429 */       else if (bb.incoming == 0) {
/*     */         
/* 431 */         writer.sameFrame(offsetDelta);
/* 432 */         offsetDelta = bb.length - 1;
/*     */       } else {
/*     */         
/* 435 */         offsetDelta += bb.length;
/*     */       } 
/*     */     } 
/* 438 */     return writer.toStackMapTable(this.cpool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isTarget(TypedBlock cur, TypedBlock prev) {
/* 445 */     int in = cur.incoming;
/* 446 */     if (in > 1)
/* 447 */       return true; 
/* 448 */     if (in < 1) {
/* 449 */       return false;
/*     */     }
/* 451 */     return prev.stop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void toStackMapBody(StackMapTable.Writer writer, TypedBlock bb, int diffL, int offsetDelta, TypedBlock prev) {
/* 459 */     int stackTop = bb.stackTop;
/* 460 */     if (stackTop == 0) {
/* 461 */       if (diffL == 0) {
/* 462 */         writer.sameFrame(offsetDelta);
/*     */         return;
/*     */       } 
/* 465 */       if (0 > diffL && diffL >= -3) {
/* 466 */         writer.chopFrame(offsetDelta, -diffL);
/*     */         return;
/*     */       } 
/* 469 */       if (0 < diffL && diffL <= 3) {
/* 470 */         int[] data = new int[diffL];
/* 471 */         int[] tags = fillStackMap(bb.numLocals - prev.numLocals, prev.numLocals, data, bb.localsTypes);
/*     */ 
/*     */         
/* 474 */         writer.appendFrame(offsetDelta, tags, data);
/*     */         return;
/*     */       } 
/*     */     } else {
/* 478 */       if (stackTop == 1 && diffL == 0) {
/* 479 */         TypeData td = bb.stackTypes[0];
/* 480 */         writer.sameLocals(offsetDelta, td.getTypeTag(), td.getTypeData(this.cpool));
/*     */         return;
/*     */       } 
/* 483 */       if (stackTop == 2 && diffL == 0) {
/* 484 */         TypeData td = bb.stackTypes[0];
/* 485 */         if (td.is2WordType()) {
/*     */           
/* 487 */           writer.sameLocals(offsetDelta, td.getTypeTag(), td.getTypeData(this.cpool));
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 492 */     int[] sdata = new int[stackTop];
/* 493 */     int[] stags = fillStackMap(stackTop, 0, sdata, bb.stackTypes);
/* 494 */     int[] ldata = new int[bb.numLocals];
/* 495 */     int[] ltags = fillStackMap(bb.numLocals, 0, ldata, bb.localsTypes);
/* 496 */     writer.fullFrame(offsetDelta, ltags, ldata, stags, sdata);
/*     */   }
/*     */   
/*     */   private int[] fillStackMap(int num, int offset, int[] data, TypeData[] types) {
/* 500 */     int realNum = diffSize(types, offset, offset + num);
/* 501 */     ConstPool cp = this.cpool;
/* 502 */     int[] tags = new int[realNum];
/* 503 */     int j = 0;
/* 504 */     for (int i = 0; i < num; i++) {
/* 505 */       TypeData td = types[offset + i];
/* 506 */       tags[j] = td.getTypeTag();
/* 507 */       data[j] = td.getTypeData(cp);
/* 508 */       if (td.is2WordType()) {
/* 509 */         i++;
/*     */       }
/* 511 */       j++;
/*     */     } 
/*     */     
/* 514 */     return tags;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int stackMapDiff(int oldTdLen, TypeData[] oldTd, int newTdLen, TypeData[] newTd) {
/* 520 */     int len, diff = newTdLen - oldTdLen;
/*     */     
/* 522 */     if (diff > 0) {
/* 523 */       len = oldTdLen;
/*     */     } else {
/* 525 */       len = newTdLen;
/*     */     } 
/* 527 */     if (stackMapEq(oldTd, newTd, len)) {
/* 528 */       if (diff > 0) {
/* 529 */         return diffSize(newTd, len, newTdLen);
/*     */       }
/* 531 */       return -diffSize(oldTd, len, oldTdLen);
/* 532 */     }  return -100;
/*     */   }
/*     */   
/*     */   private static boolean stackMapEq(TypeData[] oldTd, TypeData[] newTd, int len) {
/* 536 */     for (int i = 0; i < len; i++) {
/* 537 */       if (!oldTd[i].eq(newTd[i])) {
/* 538 */         return false;
/*     */       }
/*     */     } 
/* 541 */     return true;
/*     */   }
/*     */   
/*     */   private static int diffSize(TypeData[] types, int offset, int len) {
/* 545 */     int num = 0;
/* 546 */     while (offset < len) {
/* 547 */       TypeData td = types[offset++];
/* 548 */       num++;
/* 549 */       if (td.is2WordType()) {
/* 550 */         offset++;
/*     */       }
/*     */     } 
/* 553 */     return num;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public StackMap toStackMap2(ConstPool cp, TypedBlock[] blocks) {
/* 559 */     StackMap.Writer writer = new StackMap.Writer();
/* 560 */     int n = blocks.length;
/* 561 */     boolean[] effective = new boolean[n];
/* 562 */     TypedBlock prev = blocks[0];
/*     */ 
/*     */     
/* 565 */     effective[0] = (prev.incoming > 0);
/*     */     
/* 567 */     int num = effective[0] ? 1 : 0; int i;
/* 568 */     for (i = 1; i < n; i++) {
/* 569 */       TypedBlock bb = blocks[i];
/* 570 */       effective[i] = isTarget(bb, blocks[i - 1]); if (isTarget(bb, blocks[i - 1])) {
/* 571 */         bb.resetNumLocals();
/* 572 */         prev = bb;
/* 573 */         num++;
/*     */       } 
/*     */     } 
/*     */     
/* 577 */     if (num == 0) {
/* 578 */       return null;
/*     */     }
/* 580 */     writer.write16bit(num);
/* 581 */     for (i = 0; i < n; i++) {
/* 582 */       if (effective[i])
/* 583 */         writeStackFrame(writer, cp, (blocks[i]).position, blocks[i]); 
/*     */     } 
/* 585 */     return writer.toStackMap(cp);
/*     */   }
/*     */   
/*     */   private void writeStackFrame(StackMap.Writer writer, ConstPool cp, int offset, TypedBlock tb) {
/* 589 */     writer.write16bit(offset);
/* 590 */     writeVerifyTypeInfo(writer, cp, tb.localsTypes, tb.numLocals);
/* 591 */     writeVerifyTypeInfo(writer, cp, tb.stackTypes, tb.stackTop);
/*     */   }
/*     */   
/*     */   private void writeVerifyTypeInfo(StackMap.Writer writer, ConstPool cp, TypeData[] types, int num) {
/* 595 */     int numDWord = 0; int i;
/* 596 */     for (i = 0; i < num; i++) {
/* 597 */       TypeData td = types[i];
/* 598 */       if (td != null && td.is2WordType()) {
/* 599 */         numDWord++;
/* 600 */         i++;
/*     */       } 
/*     */     } 
/*     */     
/* 604 */     writer.write16bit(num - numDWord);
/* 605 */     for (i = 0; i < num; i++) {
/* 606 */       TypeData td = types[i];
/* 607 */       writer.writeVerifyTypeInfo(td.getTypeTag(), td.getTypeData(cp));
/* 608 */       if (td.is2WordType())
/* 609 */         i++; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\stackmap\MapMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */