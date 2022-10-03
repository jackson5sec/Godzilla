/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.ExceptionTable;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.Opcode;
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
/*     */ public class Analyzer
/*     */   implements Opcode
/*     */ {
/*  85 */   private final SubroutineScanner scanner = new SubroutineScanner();
/*     */   private CtClass clazz;
/*     */   private ExceptionInfo[] exceptions;
/*     */   private Frame[] frames;
/*     */   private Subroutine[] subroutines;
/*     */   
/*     */   private static class ExceptionInfo {
/*     */     private int end;
/*     */     private int handler;
/*     */     private int start;
/*     */     private Type type;
/*     */     
/*     */     private ExceptionInfo(int start, int end, int handler, Type type) {
/*  98 */       this.start = start;
/*  99 */       this.end = end;
/* 100 */       this.handler = handler;
/* 101 */       this.type = type;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Frame[] analyze(CtClass clazz, MethodInfo method) throws BadBytecode {
/* 121 */     this.clazz = clazz;
/* 122 */     CodeAttribute codeAttribute = method.getCodeAttribute();
/*     */     
/* 124 */     if (codeAttribute == null) {
/* 125 */       return null;
/*     */     }
/* 127 */     int maxLocals = codeAttribute.getMaxLocals();
/* 128 */     int maxStack = codeAttribute.getMaxStack();
/* 129 */     int codeLength = codeAttribute.getCodeLength();
/*     */     
/* 131 */     CodeIterator iter = codeAttribute.iterator();
/* 132 */     IntQueue queue = new IntQueue();
/*     */     
/* 134 */     this.exceptions = buildExceptionInfo(method);
/* 135 */     this.subroutines = this.scanner.scan(method);
/*     */     
/* 137 */     Executor executor = new Executor(clazz.getClassPool(), method.getConstPool());
/* 138 */     this.frames = new Frame[codeLength];
/* 139 */     this.frames[iter.lookAhead()] = firstFrame(method, maxLocals, maxStack);
/* 140 */     queue.add(iter.next());
/* 141 */     while (!queue.isEmpty()) {
/* 142 */       analyzeNextEntry(method, iter, queue, executor);
/*     */     }
/*     */     
/* 145 */     return this.frames;
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
/*     */   public Frame[] analyze(CtMethod method) throws BadBytecode {
/* 163 */     return analyze(method.getDeclaringClass(), method.getMethodInfo2());
/*     */   }
/*     */ 
/*     */   
/*     */   private void analyzeNextEntry(MethodInfo method, CodeIterator iter, IntQueue queue, Executor executor) throws BadBytecode {
/* 168 */     int pos = queue.take();
/* 169 */     iter.move(pos);
/* 170 */     iter.next();
/*     */     
/* 172 */     Frame frame = this.frames[pos].copy();
/* 173 */     Subroutine subroutine = this.subroutines[pos];
/*     */     
/*     */     try {
/* 176 */       executor.execute(method, pos, iter, frame, subroutine);
/* 177 */     } catch (RuntimeException e) {
/* 178 */       throw new BadBytecode(e.getMessage() + "[pos = " + pos + "]", e);
/*     */     } 
/*     */     
/* 181 */     int opcode = iter.byteAt(pos);
/*     */     
/* 183 */     if (opcode == 170) {
/* 184 */       mergeTableSwitch(queue, pos, iter, frame);
/* 185 */     } else if (opcode == 171) {
/* 186 */       mergeLookupSwitch(queue, pos, iter, frame);
/* 187 */     } else if (opcode == 169) {
/* 188 */       mergeRet(queue, iter, pos, frame, subroutine);
/* 189 */     } else if (Util.isJumpInstruction(opcode)) {
/* 190 */       int target = Util.getJumpTarget(pos, iter);
/*     */       
/* 192 */       if (Util.isJsr(opcode)) {
/*     */         
/* 194 */         mergeJsr(queue, this.frames[pos], this.subroutines[target], pos, lookAhead(iter, pos));
/* 195 */       } else if (!Util.isGoto(opcode)) {
/* 196 */         merge(queue, frame, lookAhead(iter, pos));
/*     */       } 
/*     */       
/* 199 */       merge(queue, frame, target);
/* 200 */     } else if (opcode != 191 && !Util.isReturn(opcode)) {
/*     */       
/* 202 */       merge(queue, frame, lookAhead(iter, pos));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     mergeExceptionHandlers(queue, method, pos, frame);
/*     */   }
/*     */   
/*     */   private ExceptionInfo[] buildExceptionInfo(MethodInfo method) {
/* 212 */     ConstPool constPool = method.getConstPool();
/* 213 */     ClassPool classes = this.clazz.getClassPool();
/*     */     
/* 215 */     ExceptionTable table = method.getCodeAttribute().getExceptionTable();
/* 216 */     ExceptionInfo[] exceptions = new ExceptionInfo[table.size()];
/* 217 */     for (int i = 0; i < table.size(); i++) {
/* 218 */       Type type; int index = table.catchType(i);
/*     */       
/*     */       try {
/* 221 */         type = (index == 0) ? Type.THROWABLE : Type.get(classes.get(constPool.getClassInfo(index)));
/* 222 */       } catch (NotFoundException e) {
/* 223 */         throw new IllegalStateException(e.getMessage());
/*     */       } 
/*     */       
/* 226 */       exceptions[i] = new ExceptionInfo(table.startPc(i), table.endPc(i), table.handlerPc(i), type);
/*     */     } 
/*     */     
/* 229 */     return exceptions;
/*     */   }
/*     */   private Frame firstFrame(MethodInfo method, int maxLocals, int maxStack) {
/*     */     CtClass[] parameters;
/* 233 */     int pos = 0;
/*     */     
/* 235 */     Frame first = new Frame(maxLocals, maxStack);
/* 236 */     if ((method.getAccessFlags() & 0x8) == 0) {
/* 237 */       first.setLocal(pos++, Type.get(this.clazz));
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 242 */       parameters = Descriptor.getParameterTypes(method.getDescriptor(), this.clazz.getClassPool());
/* 243 */     } catch (NotFoundException e) {
/* 244 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 247 */     for (int i = 0; i < parameters.length; i++) {
/* 248 */       Type type = zeroExtend(Type.get(parameters[i]));
/* 249 */       first.setLocal(pos++, type);
/* 250 */       if (type.getSize() == 2) {
/* 251 */         first.setLocal(pos++, Type.TOP);
/*     */       }
/*     */     } 
/* 254 */     return first;
/*     */   }
/*     */   
/*     */   private int getNext(CodeIterator iter, int of, int restore) throws BadBytecode {
/* 258 */     iter.move(of);
/* 259 */     iter.next();
/* 260 */     int next = iter.lookAhead();
/* 261 */     iter.move(restore);
/* 262 */     iter.next();
/*     */     
/* 264 */     return next;
/*     */   }
/*     */   
/*     */   private int lookAhead(CodeIterator iter, int pos) throws BadBytecode {
/* 268 */     if (!iter.hasNext()) {
/* 269 */       throw new BadBytecode("Execution falls off end! [pos = " + pos + "]");
/*     */     }
/* 271 */     return iter.lookAhead();
/*     */   }
/*     */   
/*     */   private void merge(IntQueue queue, Frame frame, int target) {
/*     */     boolean changed;
/* 276 */     Frame old = this.frames[target];
/*     */ 
/*     */     
/* 279 */     if (old == null) {
/* 280 */       this.frames[target] = frame.copy();
/* 281 */       changed = true;
/*     */     } else {
/* 283 */       changed = old.merge(frame);
/*     */     } 
/*     */     
/* 286 */     if (changed) {
/* 287 */       queue.add(target);
/*     */     }
/*     */   }
/*     */   
/*     */   private void mergeExceptionHandlers(IntQueue queue, MethodInfo method, int pos, Frame frame) {
/* 292 */     for (int i = 0; i < this.exceptions.length; i++) {
/* 293 */       ExceptionInfo exception = this.exceptions[i];
/*     */ 
/*     */       
/* 296 */       if (pos >= exception.start && pos < exception.end) {
/* 297 */         Frame newFrame = frame.copy();
/* 298 */         newFrame.clearStack();
/* 299 */         newFrame.push(exception.type);
/* 300 */         merge(queue, newFrame, exception.handler);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void mergeJsr(IntQueue queue, Frame frame, Subroutine sub, int pos, int next) throws BadBytecode {
/* 306 */     if (sub == null) {
/* 307 */       throw new BadBytecode("No subroutine at jsr target! [pos = " + pos + "]");
/*     */     }
/* 309 */     Frame old = this.frames[next];
/* 310 */     boolean changed = false;
/*     */     
/* 312 */     if (old == null) {
/* 313 */       old = this.frames[next] = frame.copy();
/* 314 */       changed = true;
/*     */     } else {
/* 316 */       for (int i = 0; i < frame.localsLength(); i++) {
/*     */         
/* 318 */         if (!sub.isAccessed(i)) {
/* 319 */           Type oldType = old.getLocal(i);
/* 320 */           Type newType = frame.getLocal(i);
/* 321 */           if (oldType == null) {
/* 322 */             old.setLocal(i, newType);
/* 323 */             changed = true;
/*     */           }
/*     */           else {
/*     */             
/* 327 */             newType = oldType.merge(newType);
/*     */             
/* 329 */             old.setLocal(i, newType);
/* 330 */             if (!newType.equals(oldType) || newType.popChanged())
/* 331 */               changed = true; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 336 */     if (!old.isJsrMerged()) {
/* 337 */       old.setJsrMerged(true);
/* 338 */       changed = true;
/*     */     } 
/*     */     
/* 341 */     if (changed && old.isRetMerged()) {
/* 342 */       queue.add(next);
/*     */     }
/*     */   }
/*     */   
/*     */   private void mergeLookupSwitch(IntQueue queue, int pos, CodeIterator iter, Frame frame) throws BadBytecode {
/* 347 */     int index = (pos & 0xFFFFFFFC) + 4;
/*     */     
/* 349 */     merge(queue, frame, pos + iter.s32bitAt(index));
/* 350 */     index += 4; int npairs = iter.s32bitAt(index);
/* 351 */     index += 4; int end = npairs * 8 + index;
/*     */ 
/*     */     
/* 354 */     for (index += 4; index < end; index += 8) {
/* 355 */       int target = iter.s32bitAt(index) + pos;
/* 356 */       merge(queue, frame, target);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void mergeRet(IntQueue queue, CodeIterator iter, int pos, Frame frame, Subroutine subroutine) throws BadBytecode {
/* 361 */     if (subroutine == null) {
/* 362 */       throw new BadBytecode("Ret on no subroutine! [pos = " + pos + "]");
/*     */     }
/* 364 */     for (Iterator<Integer> iterator = subroutine.callers().iterator(); iterator.hasNext(); ) { int caller = ((Integer)iterator.next()).intValue();
/* 365 */       int returnLoc = getNext(iter, caller, pos);
/* 366 */       boolean changed = false;
/*     */       
/* 368 */       Frame old = this.frames[returnLoc];
/* 369 */       if (old == null) {
/* 370 */         old = this.frames[returnLoc] = frame.copyStack();
/* 371 */         changed = true;
/*     */       } else {
/* 373 */         changed = old.mergeStack(frame);
/*     */       } 
/*     */       
/* 376 */       for (Iterator<Integer> iterator1 = subroutine.accessed().iterator(); iterator1.hasNext(); ) { int index = ((Integer)iterator1.next()).intValue();
/* 377 */         Type oldType = old.getLocal(index);
/* 378 */         Type newType = frame.getLocal(index);
/* 379 */         if (oldType != newType) {
/* 380 */           old.setLocal(index, newType);
/* 381 */           changed = true;
/*     */         }  }
/*     */ 
/*     */       
/* 385 */       if (!old.isRetMerged()) {
/* 386 */         old.setRetMerged(true);
/* 387 */         changed = true;
/*     */       } 
/*     */       
/* 390 */       if (changed && old.isJsrMerged()) {
/* 391 */         queue.add(returnLoc);
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void mergeTableSwitch(IntQueue queue, int pos, CodeIterator iter, Frame frame) throws BadBytecode {
/* 398 */     int index = (pos & 0xFFFFFFFC) + 4;
/*     */     
/* 400 */     merge(queue, frame, pos + iter.s32bitAt(index));
/* 401 */     index += 4; int low = iter.s32bitAt(index);
/* 402 */     index += 4; int high = iter.s32bitAt(index);
/* 403 */     index += 4; int end = (high - low + 1) * 4 + index;
/*     */ 
/*     */     
/* 406 */     for (; index < end; index += 4) {
/* 407 */       int target = iter.s32bitAt(index) + pos;
/* 408 */       merge(queue, frame, target);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Type zeroExtend(Type type) {
/* 413 */     if (type == Type.SHORT || type == Type.BYTE || type == Type.CHAR || type == Type.BOOLEAN) {
/* 414 */       return Type.INTEGER;
/*     */     }
/* 416 */     return type;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\Analyzer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */