/*     */ package javassist.bytecode.stackmap;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ExceptionTable;
/*     */ import javassist.bytecode.MethodInfo;
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
/*     */ public class BasicBlock
/*     */ {
/*     */   protected int position;
/*     */   protected int length;
/*     */   protected int incoming;
/*     */   protected BasicBlock[] exit;
/*     */   protected boolean stop;
/*     */   protected Catch toCatch;
/*     */   
/*     */   static class JsrBytecode
/*     */     extends BadBytecode
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     JsrBytecode() {
/*  43 */       super("JSR");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BasicBlock(int pos) {
/*  53 */     this.position = pos;
/*  54 */     this.length = 0;
/*  55 */     this.incoming = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BasicBlock find(BasicBlock[] blocks, int pos) throws BadBytecode {
/*  61 */     for (BasicBlock b : blocks) {
/*  62 */       if (b.position <= pos && pos < b.position + b.length)
/*  63 */         return b; 
/*     */     } 
/*  65 */     throw new BadBytecode("no basic block at " + pos);
/*     */   }
/*     */   
/*     */   public static class Catch { public Catch next;
/*     */     public BasicBlock body;
/*     */     public int typeIndex;
/*     */     
/*     */     Catch(BasicBlock b, int i, Catch c) {
/*  73 */       this.body = b;
/*  74 */       this.typeIndex = i;
/*  75 */       this.next = c;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  81 */     StringBuffer sbuf = new StringBuffer();
/*  82 */     String cname = getClass().getName();
/*  83 */     int i = cname.lastIndexOf('.');
/*  84 */     sbuf.append((i < 0) ? cname : cname.substring(i + 1));
/*  85 */     sbuf.append("[");
/*  86 */     toString2(sbuf);
/*  87 */     sbuf.append("]");
/*  88 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */   protected void toString2(StringBuffer sbuf) {
/*  92 */     sbuf.append("pos=").append(this.position).append(", len=")
/*  93 */       .append(this.length).append(", in=").append(this.incoming)
/*  94 */       .append(", exit{");
/*  95 */     if (this.exit != null)
/*  96 */       for (BasicBlock b : this.exit) {
/*  97 */         sbuf.append(b.position).append(",");
/*     */       } 
/*  99 */     sbuf.append("}, {");
/* 100 */     Catch th = this.toCatch;
/* 101 */     while (th != null) {
/* 102 */       sbuf.append("(").append(th.body.position).append(", ")
/* 103 */         .append(th.typeIndex).append("), ");
/* 104 */       th = th.next;
/*     */     } 
/*     */     
/* 107 */     sbuf.append("}");
/*     */   }
/*     */ 
/*     */   
/*     */   static class Mark
/*     */     implements Comparable<Mark>
/*     */   {
/*     */     int position;
/*     */     
/*     */     BasicBlock block;
/*     */     BasicBlock[] jump;
/*     */     boolean alwaysJmp;
/*     */     int size;
/*     */     BasicBlock.Catch catcher;
/*     */     
/*     */     Mark(int p) {
/* 123 */       this.position = p;
/* 124 */       this.block = null;
/* 125 */       this.jump = null;
/* 126 */       this.alwaysJmp = false;
/* 127 */       this.size = 0;
/* 128 */       this.catcher = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Mark obj) {
/* 133 */       if (null == obj)
/* 134 */         return -1; 
/* 135 */       return this.position - obj.position;
/*     */     }
/*     */     
/*     */     void setJump(BasicBlock[] bb, int s, boolean always) {
/* 139 */       this.jump = bb;
/* 140 */       this.size = s;
/* 141 */       this.alwaysJmp = always;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Maker
/*     */   {
/*     */     protected BasicBlock makeBlock(int pos) {
/* 150 */       return new BasicBlock(pos);
/*     */     }
/*     */     
/*     */     protected BasicBlock[] makeArray(int size) {
/* 154 */       return new BasicBlock[size];
/*     */     }
/*     */     
/*     */     private BasicBlock[] makeArray(BasicBlock b) {
/* 158 */       BasicBlock[] array = makeArray(1);
/* 159 */       array[0] = b;
/* 160 */       return array;
/*     */     }
/*     */     
/*     */     private BasicBlock[] makeArray(BasicBlock b1, BasicBlock b2) {
/* 164 */       BasicBlock[] array = makeArray(2);
/* 165 */       array[0] = b1;
/* 166 */       array[1] = b2;
/* 167 */       return array;
/*     */     }
/*     */     
/*     */     public BasicBlock[] make(MethodInfo minfo) throws BadBytecode {
/* 171 */       CodeAttribute ca = minfo.getCodeAttribute();
/* 172 */       if (ca == null) {
/* 173 */         return null;
/*     */       }
/* 175 */       CodeIterator ci = ca.iterator();
/* 176 */       return make(ci, 0, ci.getCodeLength(), ca.getExceptionTable());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BasicBlock[] make(CodeIterator ci, int begin, int end, ExceptionTable et) throws BadBytecode {
/* 183 */       Map<Integer, BasicBlock.Mark> marks = makeMarks(ci, begin, end, et);
/* 184 */       BasicBlock[] bb = makeBlocks(marks);
/* 185 */       addCatchers(bb, et);
/* 186 */       return bb;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private BasicBlock.Mark makeMark(Map<Integer, BasicBlock.Mark> table, int pos) {
/* 192 */       return makeMark0(table, pos, true, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private BasicBlock.Mark makeMark(Map<Integer, BasicBlock.Mark> table, int pos, BasicBlock[] jump, int size, boolean always) {
/* 200 */       BasicBlock.Mark m = makeMark0(table, pos, false, false);
/* 201 */       m.setJump(jump, size, always);
/* 202 */       return m;
/*     */     }
/*     */ 
/*     */     
/*     */     private BasicBlock.Mark makeMark0(Map<Integer, BasicBlock.Mark> table, int pos, boolean isBlockBegin, boolean isTarget) {
/* 207 */       Integer p = Integer.valueOf(pos);
/* 208 */       BasicBlock.Mark m = table.get(p);
/* 209 */       if (m == null) {
/* 210 */         m = new BasicBlock.Mark(pos);
/* 211 */         table.put(p, m);
/*     */       } 
/*     */       
/* 214 */       if (isBlockBegin) {
/* 215 */         if (m.block == null) {
/* 216 */           m.block = makeBlock(pos);
/*     */         }
/* 218 */         if (isTarget) {
/* 219 */           m.block.incoming++;
/*     */         }
/*     */       } 
/* 222 */       return m;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Map<Integer, BasicBlock.Mark> makeMarks(CodeIterator ci, int begin, int end, ExceptionTable et) throws BadBytecode {
/* 229 */       ci.begin();
/* 230 */       ci.move(begin);
/* 231 */       Map<Integer, BasicBlock.Mark> marks = new HashMap<>();
/* 232 */       while (ci.hasNext()) {
/* 233 */         int index = ci.next();
/* 234 */         if (index >= end) {
/*     */           break;
/*     */         }
/* 237 */         int op = ci.byteAt(index);
/* 238 */         if ((153 <= op && op <= 166) || op == 198 || op == 199) {
/*     */           
/* 240 */           BasicBlock.Mark to = makeMark(marks, index + ci.s16bitAt(index + 1));
/* 241 */           BasicBlock.Mark next = makeMark(marks, index + 3);
/* 242 */           makeMark(marks, index, makeArray(to.block, next.block), 3, false); continue;
/*     */         } 
/* 244 */         if (167 <= op && op <= 171) {
/* 245 */           int pos; int low; int ncases; int high; BasicBlock[] to; int i; int p; BasicBlock[] arrayOfBasicBlock1; int n; int j; int k; int m; int i1; switch (op) {
/*     */             case 167:
/* 247 */               makeGoto(marks, index, index + ci.s16bitAt(index + 1), 3);
/*     */               continue;
/*     */             case 168:
/* 250 */               makeJsr(marks, index, index + ci.s16bitAt(index + 1), 3);
/*     */               continue;
/*     */             case 169:
/* 253 */               makeMark(marks, index, null, 2, true);
/*     */               continue;
/*     */             case 170:
/* 256 */               pos = (index & 0xFFFFFFFC) + 4;
/* 257 */               low = ci.s32bitAt(pos + 4);
/* 258 */               high = ci.s32bitAt(pos + 8);
/* 259 */               i = high - low + 1;
/* 260 */               arrayOfBasicBlock1 = makeArray(i + 1);
/* 261 */               arrayOfBasicBlock1[0] = (makeMark(marks, index + ci.s32bitAt(pos))).block;
/* 262 */               j = pos + 12;
/* 263 */               m = j + i * 4;
/* 264 */               i1 = 1;
/* 265 */               while (j < m) {
/* 266 */                 arrayOfBasicBlock1[i1++] = (makeMark(marks, index + ci.s32bitAt(j))).block;
/* 267 */                 j += 4;
/*     */               } 
/* 269 */               makeMark(marks, index, arrayOfBasicBlock1, m - index, true);
/*     */               continue;
/*     */             case 171:
/* 272 */               pos = (index & 0xFFFFFFFC) + 4;
/* 273 */               ncases = ci.s32bitAt(pos + 4);
/* 274 */               to = makeArray(ncases + 1);
/* 275 */               to[0] = (makeMark(marks, index + ci.s32bitAt(pos))).block;
/* 276 */               p = pos + 8 + 4;
/* 277 */               n = p + ncases * 8 - 4;
/* 278 */               k = 1;
/* 279 */               while (p < n) {
/* 280 */                 to[k++] = (makeMark(marks, index + ci.s32bitAt(p))).block;
/* 281 */                 p += 8;
/*     */               } 
/* 283 */               makeMark(marks, index, to, n - index, true); continue;
/*     */           }  continue;
/*     */         } 
/* 286 */         if ((172 <= op && op <= 177) || op == 191) {
/* 287 */           makeMark(marks, index, null, 1, true); continue;
/* 288 */         }  if (op == 200) {
/* 289 */           makeGoto(marks, index, index + ci.s32bitAt(index + 1), 5); continue;
/* 290 */         }  if (op == 201) {
/* 291 */           makeJsr(marks, index, index + ci.s32bitAt(index + 1), 5); continue;
/* 292 */         }  if (op == 196 && ci.byteAt(index + 1) == 169) {
/* 293 */           makeMark(marks, index, null, 4, true);
/*     */         }
/*     */       } 
/* 296 */       if (et != null) {
/* 297 */         int i = et.size();
/* 298 */         while (--i >= 0) {
/* 299 */           makeMark0(marks, et.startPc(i), true, false);
/* 300 */           makeMark(marks, et.handlerPc(i));
/*     */         } 
/*     */       } 
/*     */       
/* 304 */       return marks;
/*     */     }
/*     */     
/*     */     private void makeGoto(Map<Integer, BasicBlock.Mark> marks, int pos, int target, int size) {
/* 308 */       BasicBlock.Mark to = makeMark(marks, target);
/* 309 */       BasicBlock[] jumps = makeArray(to.block);
/* 310 */       makeMark(marks, pos, jumps, size, true);
/*     */     }
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
/*     */     protected void makeJsr(Map<Integer, BasicBlock.Mark> marks, int pos, int target, int size) throws BadBytecode {
/* 324 */       throw new BasicBlock.JsrBytecode();
/*     */     }
/*     */     private BasicBlock[] makeBlocks(Map<Integer, BasicBlock.Mark> markTable) {
/*     */       BasicBlock prev;
/* 328 */       BasicBlock.Mark[] marks = (BasicBlock.Mark[])markTable.values().toArray((Object[])new BasicBlock.Mark[markTable.size()]);
/* 329 */       Arrays.sort((Object[])marks);
/* 330 */       List<BasicBlock> blocks = new ArrayList<>();
/* 331 */       int i = 0;
/*     */       
/* 333 */       if (marks.length > 0 && (marks[0]).position == 0 && (marks[0]).block != null) {
/* 334 */         prev = getBBlock(marks[i++]);
/*     */       } else {
/* 336 */         prev = makeBlock(0);
/*     */       } 
/* 338 */       blocks.add(prev);
/* 339 */       while (i < marks.length) {
/* 340 */         BasicBlock.Mark m = marks[i++];
/* 341 */         BasicBlock bb = getBBlock(m);
/* 342 */         if (bb == null) {
/*     */           
/* 344 */           if (prev.length > 0) {
/*     */             
/* 346 */             prev = makeBlock(prev.position + prev.length);
/* 347 */             blocks.add(prev);
/*     */           } 
/*     */           
/* 350 */           prev.length = m.position + m.size - prev.position;
/* 351 */           prev.exit = m.jump;
/* 352 */           prev.stop = m.alwaysJmp;
/*     */           
/*     */           continue;
/*     */         } 
/* 356 */         if (prev.length == 0) {
/* 357 */           prev.length = m.position - prev.position;
/* 358 */           bb.incoming++;
/* 359 */           prev.exit = makeArray(bb);
/*     */ 
/*     */         
/*     */         }
/* 363 */         else if (prev.position + prev.length < m.position) {
/*     */           
/* 365 */           prev = makeBlock(prev.position + prev.length);
/* 366 */           blocks.add(prev);
/* 367 */           prev.length = m.position - prev.position;
/*     */ 
/*     */           
/* 370 */           prev.stop = true;
/* 371 */           prev.exit = makeArray(bb);
/*     */         } 
/*     */ 
/*     */         
/* 375 */         blocks.add(bb);
/* 376 */         prev = bb;
/*     */       } 
/*     */ 
/*     */       
/* 380 */       return blocks.<BasicBlock>toArray(makeArray(blocks.size()));
/*     */     }
/*     */     
/*     */     private static BasicBlock getBBlock(BasicBlock.Mark m) {
/* 384 */       BasicBlock b = m.block;
/* 385 */       if (b != null && m.size > 0) {
/* 386 */         b.exit = m.jump;
/* 387 */         b.length = m.size;
/* 388 */         b.stop = m.alwaysJmp;
/*     */       } 
/*     */       
/* 391 */       return b;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void addCatchers(BasicBlock[] blocks, ExceptionTable et) throws BadBytecode {
/* 397 */       if (et == null) {
/*     */         return;
/*     */       }
/* 400 */       int i = et.size();
/* 401 */       while (--i >= 0) {
/* 402 */         BasicBlock handler = BasicBlock.find(blocks, et.handlerPc(i));
/* 403 */         int start = et.startPc(i);
/* 404 */         int end = et.endPc(i);
/* 405 */         int type = et.catchType(i);
/* 406 */         handler.incoming--;
/* 407 */         for (int k = 0; k < blocks.length; k++) {
/* 408 */           BasicBlock bb = blocks[k];
/* 409 */           int iPos = bb.position;
/* 410 */           if (start <= iPos && iPos < end) {
/* 411 */             bb.toCatch = new BasicBlock.Catch(handler, type, bb.toCatch);
/* 412 */             handler.incoming++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\stackmap\BasicBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */