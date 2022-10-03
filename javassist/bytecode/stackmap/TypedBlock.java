/*     */ package javassist.bytecode.stackmap;
/*     */ 
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.ConstPool;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypedBlock
/*     */   extends BasicBlock
/*     */ {
/*     */   public int stackTop;
/*     */   public int numLocals;
/*     */   public TypeData[] localsTypes;
/*     */   public TypeData[] stackTypes;
/*     */   
/*     */   public static TypedBlock[] makeBlocks(MethodInfo minfo, CodeAttribute ca, boolean optimize) throws BadBytecode {
/*  43 */     TypedBlock[] blocks = (TypedBlock[])(new Maker()).make(minfo);
/*  44 */     if (optimize && blocks.length < 2 && (
/*  45 */       blocks.length == 0 || (blocks[0]).incoming == 0)) {
/*  46 */       return null;
/*     */     }
/*  48 */     ConstPool pool = minfo.getConstPool();
/*  49 */     boolean isStatic = ((minfo.getAccessFlags() & 0x8) != 0);
/*  50 */     blocks[0].initFirstBlock(ca.getMaxStack(), ca.getMaxLocals(), pool
/*  51 */         .getClassName(), minfo.getDescriptor(), isStatic, minfo
/*  52 */         .isConstructor());
/*  53 */     return blocks;
/*     */   }
/*     */   
/*     */   protected TypedBlock(int pos) {
/*  57 */     super(pos);
/*  58 */     this.localsTypes = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void toString2(StringBuffer sbuf) {
/*  63 */     super.toString2(sbuf);
/*  64 */     sbuf.append(",\n stack={");
/*  65 */     printTypes(sbuf, this.stackTop, this.stackTypes);
/*  66 */     sbuf.append("}, locals={");
/*  67 */     printTypes(sbuf, this.numLocals, this.localsTypes);
/*  68 */     sbuf.append('}');
/*     */   }
/*     */ 
/*     */   
/*     */   private void printTypes(StringBuffer sbuf, int size, TypeData[] types) {
/*  73 */     if (types == null) {
/*     */       return;
/*     */     }
/*  76 */     for (int i = 0; i < size; i++) {
/*  77 */       if (i > 0) {
/*  78 */         sbuf.append(", ");
/*     */       }
/*  80 */       TypeData td = types[i];
/*  81 */       sbuf.append((td == null) ? "<>" : td.toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean alreadySet() {
/*  86 */     return (this.localsTypes != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStackMap(int st, TypeData[] stack, int nl, TypeData[] locals) throws BadBytecode {
/*  92 */     this.stackTop = st;
/*  93 */     this.stackTypes = stack;
/*  94 */     this.numLocals = nl;
/*  95 */     this.localsTypes = locals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetNumLocals() {
/* 102 */     if (this.localsTypes != null) {
/* 103 */       int nl = this.localsTypes.length;
/* 104 */       while (nl > 0 && this.localsTypes[nl - 1].isBasicType() == TypeTag.TOP && (
/* 105 */         nl <= 1 || 
/* 106 */         !this.localsTypes[nl - 2].is2WordType()))
/*     */       {
/*     */ 
/*     */         
/* 110 */         nl--;
/*     */       }
/*     */       
/* 113 */       this.numLocals = nl;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class Maker
/*     */     extends BasicBlock.Maker {
/*     */     protected BasicBlock makeBlock(int pos) {
/* 120 */       return new TypedBlock(pos);
/*     */     }
/*     */ 
/*     */     
/*     */     protected BasicBlock[] makeArray(int size) {
/* 125 */       return (BasicBlock[])new TypedBlock[size];
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
/*     */   void initFirstBlock(int maxStack, int maxLocals, String className, String methodDesc, boolean isStatic, boolean isConstructor) throws BadBytecode {
/* 143 */     if (methodDesc.charAt(0) != '(') {
/* 144 */       throw new BadBytecode("no method descriptor: " + methodDesc);
/*     */     }
/* 146 */     this.stackTop = 0;
/* 147 */     this.stackTypes = TypeData.make(maxStack);
/* 148 */     TypeData[] locals = TypeData.make(maxLocals);
/* 149 */     if (isConstructor) {
/* 150 */       locals[0] = new TypeData.UninitThis(className);
/* 151 */     } else if (!isStatic) {
/* 152 */       locals[0] = new TypeData.ClassName(className);
/*     */     } 
/* 154 */     int n = isStatic ? -1 : 0;
/* 155 */     int i = 1;
/*     */     try {
/* 157 */       while ((i = descToTag(methodDesc, i, ++n, locals)) > 0) {
/* 158 */         if (locals[n].is2WordType())
/* 159 */           locals[++n] = TypeTag.TOP; 
/*     */       } 
/* 161 */     } catch (StringIndexOutOfBoundsException e) {
/* 162 */       throw new BadBytecode("bad method descriptor: " + methodDesc);
/*     */     } 
/*     */ 
/*     */     
/* 166 */     this.numLocals = n;
/* 167 */     this.localsTypes = locals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int descToTag(String desc, int i, int n, TypeData[] types) throws BadBytecode {
/* 174 */     int i0 = i;
/* 175 */     int arrayDim = 0;
/* 176 */     char c = desc.charAt(i);
/* 177 */     if (c == ')') {
/* 178 */       return 0;
/*     */     }
/* 180 */     while (c == '[') {
/* 181 */       arrayDim++;
/* 182 */       c = desc.charAt(++i);
/*     */     } 
/*     */     
/* 185 */     if (c == 'L') {
/* 186 */       int i2 = desc.indexOf(';', ++i);
/* 187 */       if (arrayDim > 0) {
/* 188 */         types[n] = new TypeData.ClassName(desc.substring(i0, ++i2));
/*     */       } else {
/* 190 */         types[n] = new TypeData.ClassName(desc.substring(i0 + 1, ++i2 - 1)
/* 191 */             .replace('/', '.'));
/* 192 */       }  return i2;
/*     */     } 
/* 194 */     if (arrayDim > 0) {
/* 195 */       types[n] = new TypeData.ClassName(desc.substring(i0, ++i));
/* 196 */       return i;
/*     */     } 
/*     */     
/* 199 */     TypeData t = toPrimitiveTag(c);
/* 200 */     if (t == null) {
/* 201 */       throw new BadBytecode("bad method descriptor: " + desc);
/*     */     }
/* 203 */     types[n] = t;
/* 204 */     return i + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static TypeData toPrimitiveTag(char c) {
/* 209 */     switch (c) {
/*     */       case 'B':
/*     */       case 'C':
/*     */       case 'I':
/*     */       case 'S':
/*     */       case 'Z':
/* 215 */         return TypeTag.INTEGER;
/*     */       case 'J':
/* 217 */         return TypeTag.LONG;
/*     */       case 'F':
/* 219 */         return TypeTag.FLOAT;
/*     */       case 'D':
/* 221 */         return TypeTag.DOUBLE;
/*     */     } 
/*     */     
/* 224 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getRetType(String desc) {
/* 229 */     int i = desc.indexOf(')');
/* 230 */     if (i < 0) {
/* 231 */       return "java.lang.Object";
/*     */     }
/* 233 */     char c = desc.charAt(i + 1);
/* 234 */     if (c == '[')
/* 235 */       return desc.substring(i + 1); 
/* 236 */     if (c == 'L') {
/* 237 */       return desc.substring(i + 2, desc.length() - 1).replace('/', '.');
/*     */     }
/* 239 */     return "java.lang.Object";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\stackmap\TypedBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */