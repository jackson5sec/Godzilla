/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class ExceptionTable
/*     */   implements Cloneable
/*     */ {
/*     */   private ConstPool constPool;
/*     */   private List<ExceptionTableEntry> entries;
/*     */   
/*     */   public ExceptionTable(ConstPool cp) {
/*  53 */     this.constPool = cp;
/*  54 */     this.entries = new ArrayList<>();
/*     */   }
/*     */   
/*     */   ExceptionTable(ConstPool cp, DataInputStream in) throws IOException {
/*  58 */     this.constPool = cp;
/*  59 */     int length = in.readUnsignedShort();
/*  60 */     List<ExceptionTableEntry> list = new ArrayList<>(length);
/*  61 */     for (int i = 0; i < length; i++) {
/*  62 */       int start = in.readUnsignedShort();
/*  63 */       int end = in.readUnsignedShort();
/*  64 */       int handle = in.readUnsignedShort();
/*  65 */       int type = in.readUnsignedShort();
/*  66 */       list.add(new ExceptionTableEntry(start, end, handle, type));
/*     */     } 
/*     */     
/*  69 */     this.entries = list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/*  79 */     ExceptionTable r = (ExceptionTable)super.clone();
/*  80 */     r.entries = new ArrayList<>(this.entries);
/*  81 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  89 */     return this.entries.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int startPc(int nth) {
/*  98 */     return ((ExceptionTableEntry)this.entries.get(nth)).startPc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartPc(int nth, int value) {
/* 108 */     ((ExceptionTableEntry)this.entries.get(nth)).startPc = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int endPc(int nth) {
/* 117 */     return ((ExceptionTableEntry)this.entries.get(nth)).endPc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndPc(int nth, int value) {
/* 127 */     ((ExceptionTableEntry)this.entries.get(nth)).endPc = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int handlerPc(int nth) {
/* 136 */     return ((ExceptionTableEntry)this.entries.get(nth)).handlerPc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlerPc(int nth, int value) {
/* 146 */     ((ExceptionTableEntry)this.entries.get(nth)).handlerPc = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int catchType(int nth) {
/* 157 */     return ((ExceptionTableEntry)this.entries.get(nth)).catchType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatchType(int nth, int value) {
/* 167 */     ((ExceptionTableEntry)this.entries.get(nth)).catchType = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int index, ExceptionTable table, int offset) {
/* 178 */     int len = table.size();
/* 179 */     while (--len >= 0) {
/* 180 */       ExceptionTableEntry e = table.entries.get(len);
/* 181 */       add(index, e.startPc + offset, e.endPc + offset, e.handlerPc + offset, e.catchType);
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
/*     */   public void add(int index, int start, int end, int handler, int type) {
/* 196 */     if (start < end) {
/* 197 */       this.entries.add(index, new ExceptionTableEntry(start, end, handler, type));
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
/*     */   public void add(int start, int end, int handler, int type) {
/* 210 */     if (start < end) {
/* 211 */       this.entries.add(new ExceptionTableEntry(start, end, handler, type));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(int index) {
/* 220 */     this.entries.remove(index);
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
/*     */   public ExceptionTable copy(ConstPool newCp, Map<String, String> classnames) {
/* 233 */     ExceptionTable et = new ExceptionTable(newCp);
/* 234 */     ConstPool srcCp = this.constPool;
/* 235 */     for (ExceptionTableEntry e : this.entries) {
/* 236 */       int type = srcCp.copy(e.catchType, newCp, classnames);
/* 237 */       et.add(e.startPc, e.endPc, e.handlerPc, type);
/*     */     } 
/*     */     
/* 240 */     return et;
/*     */   }
/*     */   
/*     */   void shiftPc(int where, int gapLength, boolean exclusive) {
/* 244 */     for (ExceptionTableEntry e : this.entries) {
/* 245 */       e.startPc = shiftPc(e.startPc, where, gapLength, exclusive);
/* 246 */       e.endPc = shiftPc(e.endPc, where, gapLength, exclusive);
/* 247 */       e.handlerPc = shiftPc(e.handlerPc, where, gapLength, exclusive);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int shiftPc(int pc, int where, int gapLength, boolean exclusive) {
/* 253 */     if (pc > where || (exclusive && pc == where)) {
/* 254 */       pc += gapLength;
/*     */     }
/* 256 */     return pc;
/*     */   }
/*     */   
/*     */   void write(DataOutputStream out) throws IOException {
/* 260 */     out.writeShort(size());
/* 261 */     for (ExceptionTableEntry e : this.entries) {
/* 262 */       out.writeShort(e.startPc);
/* 263 */       out.writeShort(e.endPc);
/* 264 */       out.writeShort(e.handlerPc);
/* 265 */       out.writeShort(e.catchType);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ExceptionTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */