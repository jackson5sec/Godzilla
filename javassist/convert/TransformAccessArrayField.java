/*     */ package javassist.convert;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CodeConverter;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.analysis.Analyzer;
/*     */ import javassist.bytecode.analysis.Frame;
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
/*     */ public final class TransformAccessArrayField
/*     */   extends Transformer
/*     */ {
/*     */   private final String methodClassname;
/*     */   private final CodeConverter.ArrayAccessReplacementMethodNames names;
/*     */   private Frame[] frames;
/*     */   private int offset;
/*     */   
/*     */   public TransformAccessArrayField(Transformer next, String methodClassname, CodeConverter.ArrayAccessReplacementMethodNames names) throws NotFoundException {
/*  45 */     super(next);
/*  46 */     this.methodClassname = methodClassname;
/*  47 */     this.names = names;
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
/*     */   public void initialize(ConstPool cp, CtClass clazz, MethodInfo minfo) throws CannotCompileException {
/*  63 */     CodeIterator iterator = minfo.getCodeAttribute().iterator();
/*  64 */     while (iterator.hasNext()) {
/*     */       try {
/*  66 */         int pos = iterator.next();
/*  67 */         int c = iterator.byteAt(pos);
/*     */         
/*  69 */         if (c == 50) {
/*  70 */           initFrames(clazz, minfo);
/*     */         }
/*  72 */         if (c == 50 || c == 51 || c == 52 || c == 49 || c == 48 || c == 46 || c == 47 || c == 53) {
/*     */ 
/*     */           
/*  75 */           pos = replace(cp, iterator, pos, c, getLoadReplacementSignature(c)); continue;
/*  76 */         }  if (c == 83 || c == 84 || c == 85 || c == 82 || c == 81 || c == 79 || c == 80 || c == 86)
/*     */         {
/*     */           
/*  79 */           pos = replace(cp, iterator, pos, c, getStoreReplacementSignature(c));
/*     */         }
/*     */       }
/*  82 */       catch (Exception e) {
/*  83 */         throw new CannotCompileException(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clean() {
/*  90 */     this.frames = null;
/*  91 */     this.offset = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int transform(CtClass tclazz, int pos, CodeIterator iterator, ConstPool cp) throws BadBytecode {
/*  98 */     return pos;
/*     */   }
/*     */   
/*     */   private Frame getFrame(int pos) throws BadBytecode {
/* 102 */     return this.frames[pos - this.offset];
/*     */   }
/*     */   
/*     */   private void initFrames(CtClass clazz, MethodInfo minfo) throws BadBytecode {
/* 106 */     if (this.frames == null) {
/* 107 */       this.frames = (new Analyzer()).analyze(clazz, minfo);
/* 108 */       this.offset = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int updatePos(int pos, int increment) {
/* 113 */     if (this.offset > -1) {
/* 114 */       this.offset += increment;
/*     */     }
/* 116 */     return pos + increment;
/*     */   }
/*     */   
/*     */   private String getTopType(int pos) throws BadBytecode {
/* 120 */     Frame frame = getFrame(pos);
/* 121 */     if (frame == null) {
/* 122 */       return null;
/*     */     }
/* 124 */     CtClass clazz = frame.peek().getCtClass();
/* 125 */     return (clazz != null) ? Descriptor.toJvmName(clazz) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   private int replace(ConstPool cp, CodeIterator iterator, int pos, int opcode, String signature) throws BadBytecode {
/* 130 */     String castType = null;
/* 131 */     String methodName = getMethodName(opcode);
/* 132 */     if (methodName != null) {
/*     */       
/* 134 */       if (opcode == 50) {
/* 135 */         castType = getTopType(iterator.lookAhead());
/*     */ 
/*     */ 
/*     */         
/* 139 */         if (castType == null)
/* 140 */           return pos; 
/* 141 */         if ("java/lang/Object".equals(castType)) {
/* 142 */           castType = null;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 147 */       iterator.writeByte(0, pos);
/*     */       
/* 149 */       CodeIterator.Gap gap = iterator.insertGapAt(pos, (castType != null) ? 5 : 2, false);
/* 150 */       pos = gap.position;
/* 151 */       int mi = cp.addClassInfo(this.methodClassname);
/* 152 */       int methodref = cp.addMethodrefInfo(mi, methodName, signature);
/* 153 */       iterator.writeByte(184, pos);
/* 154 */       iterator.write16bit(methodref, pos + 1);
/*     */       
/* 156 */       if (castType != null) {
/* 157 */         int index = cp.addClassInfo(castType);
/* 158 */         iterator.writeByte(192, pos + 3);
/* 159 */         iterator.write16bit(index, pos + 4);
/*     */       } 
/*     */       
/* 162 */       pos = updatePos(pos, gap.length);
/*     */     } 
/*     */     
/* 165 */     return pos;
/*     */   }
/*     */   
/*     */   private String getMethodName(int opcode) {
/* 169 */     String methodName = null;
/* 170 */     switch (opcode) {
/*     */       case 50:
/* 172 */         methodName = this.names.objectRead();
/*     */         break;
/*     */       case 51:
/* 175 */         methodName = this.names.byteOrBooleanRead();
/*     */         break;
/*     */       case 52:
/* 178 */         methodName = this.names.charRead();
/*     */         break;
/*     */       case 49:
/* 181 */         methodName = this.names.doubleRead();
/*     */         break;
/*     */       case 48:
/* 184 */         methodName = this.names.floatRead();
/*     */         break;
/*     */       case 46:
/* 187 */         methodName = this.names.intRead();
/*     */         break;
/*     */       case 53:
/* 190 */         methodName = this.names.shortRead();
/*     */         break;
/*     */       case 47:
/* 193 */         methodName = this.names.longRead();
/*     */         break;
/*     */       case 83:
/* 196 */         methodName = this.names.objectWrite();
/*     */         break;
/*     */       case 84:
/* 199 */         methodName = this.names.byteOrBooleanWrite();
/*     */         break;
/*     */       case 85:
/* 202 */         methodName = this.names.charWrite();
/*     */         break;
/*     */       case 82:
/* 205 */         methodName = this.names.doubleWrite();
/*     */         break;
/*     */       case 81:
/* 208 */         methodName = this.names.floatWrite();
/*     */         break;
/*     */       case 79:
/* 211 */         methodName = this.names.intWrite();
/*     */         break;
/*     */       case 86:
/* 214 */         methodName = this.names.shortWrite();
/*     */         break;
/*     */       case 80:
/* 217 */         methodName = this.names.longWrite();
/*     */         break;
/*     */     } 
/*     */     
/* 221 */     if (methodName.equals("")) {
/* 222 */       methodName = null;
/*     */     }
/* 224 */     return methodName;
/*     */   }
/*     */   
/*     */   private String getLoadReplacementSignature(int opcode) throws BadBytecode {
/* 228 */     switch (opcode) {
/*     */       case 50:
/* 230 */         return "(Ljava/lang/Object;I)Ljava/lang/Object;";
/*     */       case 51:
/* 232 */         return "(Ljava/lang/Object;I)B";
/*     */       case 52:
/* 234 */         return "(Ljava/lang/Object;I)C";
/*     */       case 49:
/* 236 */         return "(Ljava/lang/Object;I)D";
/*     */       case 48:
/* 238 */         return "(Ljava/lang/Object;I)F";
/*     */       case 46:
/* 240 */         return "(Ljava/lang/Object;I)I";
/*     */       case 53:
/* 242 */         return "(Ljava/lang/Object;I)S";
/*     */       case 47:
/* 244 */         return "(Ljava/lang/Object;I)J";
/*     */     } 
/*     */     
/* 247 */     throw new BadBytecode(opcode);
/*     */   }
/*     */   
/*     */   private String getStoreReplacementSignature(int opcode) throws BadBytecode {
/* 251 */     switch (opcode) {
/*     */       case 83:
/* 253 */         return "(Ljava/lang/Object;ILjava/lang/Object;)V";
/*     */       case 84:
/* 255 */         return "(Ljava/lang/Object;IB)V";
/*     */       case 85:
/* 257 */         return "(Ljava/lang/Object;IC)V";
/*     */       case 82:
/* 259 */         return "(Ljava/lang/Object;ID)V";
/*     */       case 81:
/* 261 */         return "(Ljava/lang/Object;IF)V";
/*     */       case 79:
/* 263 */         return "(Ljava/lang/Object;II)V";
/*     */       case 86:
/* 265 */         return "(Ljava/lang/Object;IS)V";
/*     */       case 80:
/* 267 */         return "(Ljava/lang/Object;IJ)V";
/*     */     } 
/*     */     
/* 270 */     throw new BadBytecode(opcode);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\convert\TransformAccessArrayField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */