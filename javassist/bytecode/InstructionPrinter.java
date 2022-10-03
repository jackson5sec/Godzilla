/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import javassist.CtMethod;
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
/*     */ public class InstructionPrinter
/*     */   implements Opcode
/*     */ {
/*  29 */   private static final String[] opcodes = Mnemonic.OPCODE;
/*     */ 
/*     */   
/*     */   private final PrintStream stream;
/*     */ 
/*     */   
/*     */   public InstructionPrinter(PrintStream stream) {
/*  36 */     this.stream = stream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void print(CtMethod method, PrintStream stream) {
/*  43 */     (new InstructionPrinter(stream)).print(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(CtMethod method) {
/*  50 */     MethodInfo info = method.getMethodInfo2();
/*  51 */     ConstPool pool = info.getConstPool();
/*  52 */     CodeAttribute code = info.getCodeAttribute();
/*  53 */     if (code == null) {
/*     */       return;
/*     */     }
/*  56 */     CodeIterator iterator = code.iterator();
/*  57 */     while (iterator.hasNext()) {
/*     */       int pos;
/*     */       try {
/*  60 */         pos = iterator.next();
/*  61 */       } catch (BadBytecode e) {
/*  62 */         throw new RuntimeException(e);
/*     */       } 
/*     */       
/*  65 */       this.stream.println(pos + ": " + instructionString(iterator, pos, pool));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String instructionString(CodeIterator iter, int pos, ConstPool pool) {
/*  74 */     int opcode = iter.byteAt(pos);
/*     */     
/*  76 */     if (opcode > opcodes.length || opcode < 0) {
/*  77 */       throw new IllegalArgumentException("Invalid opcode, opcode: " + opcode + " pos: " + pos);
/*     */     }
/*  79 */     String opstring = opcodes[opcode];
/*  80 */     switch (opcode) {
/*     */       case 16:
/*  82 */         return opstring + " " + iter.byteAt(pos + 1);
/*     */       case 17:
/*  84 */         return opstring + " " + iter.s16bitAt(pos + 1);
/*     */       case 18:
/*  86 */         return opstring + " " + ldc(pool, iter.byteAt(pos + 1));
/*     */       case 19:
/*     */       case 20:
/*  89 */         return opstring + " " + ldc(pool, iter.u16bitAt(pos + 1));
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 54:
/*     */       case 55:
/*     */       case 56:
/*     */       case 57:
/*     */       case 58:
/* 100 */         return opstring + " " + iter.byteAt(pos + 1);
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/*     */       case 159:
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/*     */       case 198:
/*     */       case 199:
/* 117 */         return opstring + " " + (iter.s16bitAt(pos + 1) + pos);
/*     */       case 132:
/* 119 */         return opstring + " " + iter.byteAt(pos + 1) + ", " + iter.signedByteAt(pos + 2);
/*     */       case 167:
/*     */       case 168:
/* 122 */         return opstring + " " + (iter.s16bitAt(pos + 1) + pos);
/*     */       case 169:
/* 124 */         return opstring + " " + iter.byteAt(pos + 1);
/*     */       case 170:
/* 126 */         return tableSwitch(iter, pos);
/*     */       case 171:
/* 128 */         return lookupSwitch(iter, pos);
/*     */       case 178:
/*     */       case 179:
/*     */       case 180:
/*     */       case 181:
/* 133 */         return opstring + " " + fieldInfo(pool, iter.u16bitAt(pos + 1));
/*     */       case 182:
/*     */       case 183:
/*     */       case 184:
/* 137 */         return opstring + " " + methodInfo(pool, iter.u16bitAt(pos + 1));
/*     */       case 185:
/* 139 */         return opstring + " " + interfaceMethodInfo(pool, iter.u16bitAt(pos + 1));
/*     */       case 186:
/* 141 */         return opstring + " " + iter.u16bitAt(pos + 1);
/*     */       case 187:
/* 143 */         return opstring + " " + classInfo(pool, iter.u16bitAt(pos + 1));
/*     */       case 188:
/* 145 */         return opstring + " " + arrayInfo(iter.byteAt(pos + 1));
/*     */       case 189:
/*     */       case 192:
/* 148 */         return opstring + " " + classInfo(pool, iter.u16bitAt(pos + 1));
/*     */       case 196:
/* 150 */         return wide(iter, pos);
/*     */       case 197:
/* 152 */         return opstring + " " + classInfo(pool, iter.u16bitAt(pos + 1));
/*     */       case 200:
/*     */       case 201:
/* 155 */         return opstring + " " + (iter.s32bitAt(pos + 1) + pos);
/*     */     } 
/* 157 */     return opstring;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String wide(CodeIterator iter, int pos) {
/* 163 */     int opcode = iter.byteAt(pos + 1);
/* 164 */     int index = iter.u16bitAt(pos + 2);
/* 165 */     switch (opcode) {
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 54:
/*     */       case 55:
/*     */       case 56:
/*     */       case 57:
/*     */       case 58:
/*     */       case 132:
/*     */       case 169:
/* 178 */         return opcodes[opcode] + " " + index;
/*     */     } 
/* 180 */     throw new RuntimeException("Invalid WIDE operand");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String arrayInfo(int type) {
/* 186 */     switch (type) {
/*     */       case 4:
/* 188 */         return "boolean";
/*     */       case 5:
/* 190 */         return "char";
/*     */       case 8:
/* 192 */         return "byte";
/*     */       case 9:
/* 194 */         return "short";
/*     */       case 10:
/* 196 */         return "int";
/*     */       case 11:
/* 198 */         return "long";
/*     */       case 6:
/* 200 */         return "float";
/*     */       case 7:
/* 202 */         return "double";
/*     */     } 
/* 204 */     throw new RuntimeException("Invalid array type");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String classInfo(ConstPool pool, int index) {
/* 210 */     return "#" + index + " = Class " + pool.getClassInfo(index);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String interfaceMethodInfo(ConstPool pool, int index) {
/* 215 */     return "#" + index + " = Method " + pool
/* 216 */       .getInterfaceMethodrefClassName(index) + "." + pool
/* 217 */       .getInterfaceMethodrefName(index) + "(" + pool
/* 218 */       .getInterfaceMethodrefType(index) + ")";
/*     */   }
/*     */   
/*     */   private static String methodInfo(ConstPool pool, int index) {
/* 222 */     return "#" + index + " = Method " + pool
/* 223 */       .getMethodrefClassName(index) + "." + pool
/* 224 */       .getMethodrefName(index) + "(" + pool
/* 225 */       .getMethodrefType(index) + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   private static String fieldInfo(ConstPool pool, int index) {
/* 230 */     return "#" + index + " = Field " + pool
/* 231 */       .getFieldrefClassName(index) + "." + pool
/* 232 */       .getFieldrefName(index) + "(" + pool
/* 233 */       .getFieldrefType(index) + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   private static String lookupSwitch(CodeIterator iter, int pos) {
/* 238 */     StringBuffer buffer = new StringBuffer("lookupswitch {\n");
/* 239 */     int index = (pos & 0xFFFFFFFC) + 4;
/*     */     
/* 241 */     buffer.append("\t\tdefault: ").append(pos + iter.s32bitAt(index)).append("\n");
/* 242 */     index += 4; int npairs = iter.s32bitAt(index);
/* 243 */     index += 4; int end = npairs * 8 + index;
/*     */     
/* 245 */     for (; index < end; index += 8) {
/* 246 */       int match = iter.s32bitAt(index);
/* 247 */       int target = iter.s32bitAt(index + 4) + pos;
/* 248 */       buffer.append("\t\t").append(match).append(": ").append(target).append("\n");
/*     */     } 
/*     */     
/* 251 */     buffer.setCharAt(buffer.length() - 1, '}');
/* 252 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String tableSwitch(CodeIterator iter, int pos) {
/* 257 */     StringBuffer buffer = new StringBuffer("tableswitch {\n");
/* 258 */     int index = (pos & 0xFFFFFFFC) + 4;
/*     */     
/* 260 */     buffer.append("\t\tdefault: ").append(pos + iter.s32bitAt(index)).append("\n");
/* 261 */     index += 4; int low = iter.s32bitAt(index);
/* 262 */     index += 4; int high = iter.s32bitAt(index);
/* 263 */     index += 4; int end = (high - low + 1) * 4 + index;
/*     */ 
/*     */     
/* 266 */     for (int key = low; index < end; index += 4, key++) {
/* 267 */       int target = iter.s32bitAt(index) + pos;
/* 268 */       buffer.append("\t\t").append(key).append(": ").append(target).append("\n");
/*     */     } 
/*     */     
/* 271 */     buffer.setCharAt(buffer.length() - 1, '}');
/* 272 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String ldc(ConstPool pool, int index) {
/* 277 */     int tag = pool.getTag(index);
/* 278 */     switch (tag) {
/*     */       case 8:
/* 280 */         return "#" + index + " = \"" + pool.getStringInfo(index) + "\"";
/*     */       case 3:
/* 282 */         return "#" + index + " = int " + pool.getIntegerInfo(index);
/*     */       case 4:
/* 284 */         return "#" + index + " = float " + pool.getFloatInfo(index);
/*     */       case 5:
/* 286 */         return "#" + index + " = long " + pool.getLongInfo(index);
/*     */       case 6:
/* 288 */         return "#" + index + " = double " + pool.getDoubleInfo(index);
/*     */       case 7:
/* 290 */         return classInfo(pool, index);
/*     */     } 
/* 292 */     throw new RuntimeException("bad LDC: " + tag);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\InstructionPrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */