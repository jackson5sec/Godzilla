/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
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
/*     */ public class SubroutineScanner
/*     */   implements Opcode
/*     */ {
/*     */   private Subroutine[] subroutines;
/*  38 */   Map<Integer, Subroutine> subTable = new HashMap<>();
/*  39 */   Set<Integer> done = new HashSet<>();
/*     */ 
/*     */   
/*     */   public Subroutine[] scan(MethodInfo method) throws BadBytecode {
/*  43 */     CodeAttribute code = method.getCodeAttribute();
/*  44 */     CodeIterator iter = code.iterator();
/*     */     
/*  46 */     this.subroutines = new Subroutine[code.getCodeLength()];
/*  47 */     this.subTable.clear();
/*  48 */     this.done.clear();
/*     */     
/*  50 */     scan(0, iter, null);
/*     */     
/*  52 */     ExceptionTable exceptions = code.getExceptionTable();
/*  53 */     for (int i = 0; i < exceptions.size(); i++) {
/*  54 */       int handler = exceptions.handlerPc(i);
/*     */ 
/*     */       
/*  57 */       scan(handler, iter, this.subroutines[exceptions.startPc(i)]);
/*     */     } 
/*     */     
/*  60 */     return this.subroutines;
/*     */   }
/*     */   
/*     */   private void scan(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
/*     */     boolean next;
/*  65 */     if (this.done.contains(Integer.valueOf(pos))) {
/*     */       return;
/*     */     }
/*  68 */     this.done.add(Integer.valueOf(pos));
/*     */     
/*  70 */     int old = iter.lookAhead();
/*  71 */     iter.move(pos);
/*     */ 
/*     */     
/*     */     do {
/*  75 */       pos = iter.next();
/*  76 */       next = (scanOp(pos, iter, sub) && iter.hasNext());
/*  77 */     } while (next);
/*     */     
/*  79 */     iter.move(old);
/*     */   }
/*     */   
/*     */   private boolean scanOp(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
/*  83 */     this.subroutines[pos] = sub;
/*     */     
/*  85 */     int opcode = iter.byteAt(pos);
/*     */     
/*  87 */     if (opcode == 170) {
/*  88 */       scanTableSwitch(pos, iter, sub);
/*     */       
/*  90 */       return false;
/*     */     } 
/*     */     
/*  93 */     if (opcode == 171) {
/*  94 */       scanLookupSwitch(pos, iter, sub);
/*     */       
/*  96 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 100 */     if (Util.isReturn(opcode) || opcode == 169 || opcode == 191) {
/* 101 */       return false;
/*     */     }
/* 103 */     if (Util.isJumpInstruction(opcode)) {
/* 104 */       int target = Util.getJumpTarget(pos, iter);
/* 105 */       if (opcode == 168 || opcode == 201) {
/* 106 */         Subroutine s = this.subTable.get(Integer.valueOf(target));
/* 107 */         if (s == null) {
/* 108 */           s = new Subroutine(target, pos);
/* 109 */           this.subTable.put(Integer.valueOf(target), s);
/* 110 */           scan(target, iter, s);
/*     */         } else {
/* 112 */           s.addCaller(pos);
/*     */         } 
/*     */       } else {
/* 115 */         scan(target, iter, sub);
/*     */ 
/*     */         
/* 118 */         if (Util.isGoto(opcode)) {
/* 119 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 123 */     return true;
/*     */   }
/*     */   
/*     */   private void scanLookupSwitch(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
/* 127 */     int index = (pos & 0xFFFFFFFC) + 4;
/*     */     
/* 129 */     scan(pos + iter.s32bitAt(index), iter, sub);
/* 130 */     index += 4; int npairs = iter.s32bitAt(index);
/* 131 */     index += 4; int end = npairs * 8 + index;
/*     */ 
/*     */     
/* 134 */     for (index += 4; index < end; index += 8) {
/* 135 */       int target = iter.s32bitAt(index) + pos;
/* 136 */       scan(target, iter, sub);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void scanTableSwitch(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
/* 142 */     int index = (pos & 0xFFFFFFFC) + 4;
/*     */     
/* 144 */     scan(pos + iter.s32bitAt(index), iter, sub);
/* 145 */     index += 4; int low = iter.s32bitAt(index);
/* 146 */     index += 4; int high = iter.s32bitAt(index);
/* 147 */     index += 4; int end = (high - low + 1) * 4 + index;
/*     */ 
/*     */     
/* 150 */     for (; index < end; index += 4) {
/* 151 */       int target = iter.s32bitAt(index) + pos;
/* 152 */       scan(target, iter, sub);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\SubroutineScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */