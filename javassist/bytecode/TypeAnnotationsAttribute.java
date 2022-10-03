/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javassist.bytecode.annotation.AnnotationsWriter;
/*     */ import javassist.bytecode.annotation.TypeAnnotationsWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeAnnotationsAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String visibleTag = "RuntimeVisibleTypeAnnotations";
/*     */   public static final String invisibleTag = "RuntimeInvisibleTypeAnnotations";
/*     */   
/*     */   public TypeAnnotationsAttribute(ConstPool cp, String attrname, byte[] info) {
/*  39 */     super(cp, attrname, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TypeAnnotationsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  48 */     super(cp, n, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int numAnnotations() {
/*  55 */     return ByteArray.readU16bit(this.info, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/*  63 */     Copier copier = new Copier(this.info, this.constPool, newCp, classnames);
/*     */     try {
/*  65 */       copier.annotationArray();
/*  66 */       return new TypeAnnotationsAttribute(newCp, getName(), copier.close());
/*     */     }
/*  68 */     catch (Exception e) {
/*  69 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void renameClass(String oldname, String newname) {
/*  79 */     Map<String, String> map = new HashMap<>();
/*  80 */     map.put(oldname, newname);
/*  81 */     renameClass(map);
/*     */   }
/*     */ 
/*     */   
/*     */   void renameClass(Map<String, String> classnames) {
/*  86 */     Renamer renamer = new Renamer(this.info, getConstPool(), classnames);
/*     */     try {
/*  88 */       renamer.annotationArray();
/*  89 */     } catch (Exception e) {
/*  90 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   void getRefClasses(Map<String, String> classnames) {
/*  95 */     renameClass(classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class TAWalker
/*     */     extends AnnotationsAttribute.Walker
/*     */   {
/*     */     TypeAnnotationsAttribute.SubWalker subWalker;
/*     */ 
/*     */     
/*     */     TAWalker(byte[] attrInfo) {
/* 107 */       super(attrInfo);
/* 108 */       this.subWalker = new TypeAnnotationsAttribute.SubWalker(attrInfo);
/*     */     }
/*     */ 
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 113 */       for (int i = 0; i < num; i++) {
/* 114 */         int targetType = this.info[pos] & 0xFF;
/* 115 */         pos = this.subWalker.targetInfo(pos + 1, targetType);
/* 116 */         pos = this.subWalker.typePath(pos);
/* 117 */         pos = annotation(pos);
/*     */       } 
/*     */       
/* 120 */       return pos;
/*     */     }
/*     */   }
/*     */   
/*     */   static class SubWalker {
/*     */     byte[] info;
/*     */     
/*     */     SubWalker(byte[] attrInfo) {
/* 128 */       this.info = attrInfo; } final int targetInfo(int pos, int type) throws Exception { int j; int param; int i; int len; int index;
/*     */       int offset;
/*     */       int bound;
/*     */       int k;
/* 132 */       switch (type) {
/*     */         case 0:
/*     */         case 1:
/* 135 */           j = this.info[pos] & 0xFF;
/* 136 */           typeParameterTarget(pos, type, j);
/* 137 */           return pos + 1;
/*     */         case 16:
/* 139 */           j = ByteArray.readU16bit(this.info, pos);
/* 140 */           supertypeTarget(pos, j);
/* 141 */           return pos + 2;
/*     */         case 17:
/*     */         case 18:
/* 144 */           param = this.info[pos] & 0xFF;
/* 145 */           bound = this.info[pos + 1] & 0xFF;
/* 146 */           typeParameterBoundTarget(pos, type, param, bound);
/* 147 */           return pos + 2;
/*     */         case 19:
/*     */         case 20:
/*     */         case 21:
/* 151 */           emptyTarget(pos, type);
/* 152 */           return pos;
/*     */         case 22:
/* 154 */           i = this.info[pos] & 0xFF;
/* 155 */           formalParameterTarget(pos, i);
/* 156 */           return pos + 1;
/*     */         case 23:
/* 158 */           i = ByteArray.readU16bit(this.info, pos);
/* 159 */           throwsTarget(pos, i);
/* 160 */           return pos + 2;
/*     */         case 64:
/*     */         case 65:
/* 163 */           len = ByteArray.readU16bit(this.info, pos);
/* 164 */           return localvarTarget(pos + 2, type, len);
/*     */         case 66:
/* 166 */           index = ByteArray.readU16bit(this.info, pos);
/* 167 */           catchTarget(pos, index);
/* 168 */           return pos + 2;
/*     */         case 67:
/*     */         case 68:
/*     */         case 69:
/*     */         case 70:
/* 173 */           offset = ByteArray.readU16bit(this.info, pos);
/* 174 */           offsetTarget(pos, type, offset);
/* 175 */           return pos + 2;
/*     */         case 71:
/*     */         case 72:
/*     */         case 73:
/*     */         case 74:
/*     */         case 75:
/* 181 */           offset = ByteArray.readU16bit(this.info, pos);
/* 182 */           k = this.info[pos + 2] & 0xFF;
/* 183 */           typeArgumentTarget(pos, type, offset, k);
/* 184 */           return pos + 3;
/*     */       } 
/* 186 */       throw new RuntimeException("invalid target type: " + type); }
/*     */ 
/*     */ 
/*     */     
/*     */     void typeParameterTarget(int pos, int targetType, int typeParameterIndex) throws Exception {}
/*     */ 
/*     */     
/*     */     void supertypeTarget(int pos, int superTypeIndex) throws Exception {}
/*     */ 
/*     */     
/*     */     void typeParameterBoundTarget(int pos, int targetType, int typeParameterIndex, int boundIndex) throws Exception {}
/*     */     
/*     */     void emptyTarget(int pos, int targetType) throws Exception {}
/*     */     
/*     */     void formalParameterTarget(int pos, int formalParameterIndex) throws Exception {}
/*     */     
/*     */     void throwsTarget(int pos, int throwsTypeIndex) throws Exception {}
/*     */     
/*     */     int localvarTarget(int pos, int targetType, int tableLength) throws Exception {
/* 205 */       for (int i = 0; i < tableLength; i++) {
/* 206 */         int start = ByteArray.readU16bit(this.info, pos);
/* 207 */         int length = ByteArray.readU16bit(this.info, pos + 2);
/* 208 */         int index = ByteArray.readU16bit(this.info, pos + 4);
/* 209 */         localvarTarget(pos, targetType, start, length, index);
/* 210 */         pos += 6;
/*     */       } 
/*     */       
/* 213 */       return pos;
/*     */     }
/*     */ 
/*     */     
/*     */     void localvarTarget(int pos, int targetType, int startPc, int length, int index) throws Exception {}
/*     */ 
/*     */     
/*     */     void catchTarget(int pos, int exceptionTableIndex) throws Exception {}
/*     */     
/*     */     void offsetTarget(int pos, int targetType, int offset) throws Exception {}
/*     */     
/*     */     void typeArgumentTarget(int pos, int targetType, int offset, int typeArgumentIndex) throws Exception {}
/*     */     
/*     */     final int typePath(int pos) throws Exception {
/* 227 */       int len = this.info[pos++] & 0xFF;
/* 228 */       return typePath(pos, len);
/*     */     }
/*     */     
/*     */     int typePath(int pos, int pathLength) throws Exception {
/* 232 */       for (int i = 0; i < pathLength; i++) {
/* 233 */         int kind = this.info[pos] & 0xFF;
/* 234 */         int index = this.info[pos + 1] & 0xFF;
/* 235 */         typePath(pos, kind, index);
/* 236 */         pos += 2;
/*     */       } 
/*     */       
/* 239 */       return pos;
/*     */     }
/*     */     
/*     */     void typePath(int pos, int typePathKind, int typeArgumentIndex) throws Exception {}
/*     */   }
/*     */   
/*     */   static class Renamer extends AnnotationsAttribute.Renamer {
/*     */     TypeAnnotationsAttribute.SubWalker sub;
/*     */     
/*     */     Renamer(byte[] attrInfo, ConstPool cp, Map<String, String> map) {
/* 249 */       super(attrInfo, cp, map);
/* 250 */       this.sub = new TypeAnnotationsAttribute.SubWalker(attrInfo);
/*     */     }
/*     */ 
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 255 */       for (int i = 0; i < num; i++) {
/* 256 */         int targetType = this.info[pos] & 0xFF;
/* 257 */         pos = this.sub.targetInfo(pos + 1, targetType);
/* 258 */         pos = this.sub.typePath(pos);
/* 259 */         pos = annotation(pos);
/*     */       } 
/*     */       
/* 262 */       return pos;
/*     */     }
/*     */   }
/*     */   
/*     */   static class Copier extends AnnotationsAttribute.Copier {
/*     */     TypeAnnotationsAttribute.SubCopier sub;
/*     */     
/*     */     Copier(byte[] attrInfo, ConstPool src, ConstPool dest, Map<String, String> map) {
/* 270 */       super(attrInfo, src, dest, map, false);
/* 271 */       TypeAnnotationsWriter w = new TypeAnnotationsWriter(this.output, dest);
/* 272 */       this.writer = (AnnotationsWriter)w;
/* 273 */       this.sub = new TypeAnnotationsAttribute.SubCopier(attrInfo, src, dest, map, w);
/*     */     }
/*     */ 
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 278 */       this.writer.numAnnotations(num);
/* 279 */       for (int i = 0; i < num; i++) {
/* 280 */         int targetType = this.info[pos] & 0xFF;
/* 281 */         pos = this.sub.targetInfo(pos + 1, targetType);
/* 282 */         pos = this.sub.typePath(pos);
/* 283 */         pos = annotation(pos);
/*     */       } 
/*     */       
/* 286 */       return pos;
/*     */     }
/*     */   }
/*     */   
/*     */   static class SubCopier
/*     */     extends SubWalker {
/*     */     ConstPool srcPool;
/*     */     ConstPool destPool;
/*     */     Map<String, String> classnames;
/*     */     TypeAnnotationsWriter writer;
/*     */     
/*     */     SubCopier(byte[] attrInfo, ConstPool src, ConstPool dest, Map<String, String> map, TypeAnnotationsWriter w) {
/* 298 */       super(attrInfo);
/* 299 */       this.srcPool = src;
/* 300 */       this.destPool = dest;
/* 301 */       this.classnames = map;
/* 302 */       this.writer = w;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void typeParameterTarget(int pos, int targetType, int typeParameterIndex) throws Exception {
/* 309 */       this.writer.typeParameterTarget(targetType, typeParameterIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     void supertypeTarget(int pos, int superTypeIndex) throws Exception {
/* 314 */       this.writer.supertypeTarget(superTypeIndex);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void typeParameterBoundTarget(int pos, int targetType, int typeParameterIndex, int boundIndex) throws Exception {
/* 322 */       this.writer.typeParameterBoundTarget(targetType, typeParameterIndex, boundIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     void emptyTarget(int pos, int targetType) throws Exception {
/* 327 */       this.writer.emptyTarget(targetType);
/*     */     }
/*     */ 
/*     */     
/*     */     void formalParameterTarget(int pos, int formalParameterIndex) throws Exception {
/* 332 */       this.writer.formalParameterTarget(formalParameterIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     void throwsTarget(int pos, int throwsTypeIndex) throws Exception {
/* 337 */       this.writer.throwsTarget(throwsTypeIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     int localvarTarget(int pos, int targetType, int tableLength) throws Exception {
/* 342 */       this.writer.localVarTarget(targetType, tableLength);
/* 343 */       return super.localvarTarget(pos, targetType, tableLength);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void localvarTarget(int pos, int targetType, int startPc, int length, int index) throws Exception {
/* 350 */       this.writer.localVarTargetTable(startPc, length, index);
/*     */     }
/*     */ 
/*     */     
/*     */     void catchTarget(int pos, int exceptionTableIndex) throws Exception {
/* 355 */       this.writer.catchTarget(exceptionTableIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     void offsetTarget(int pos, int targetType, int offset) throws Exception {
/* 360 */       this.writer.offsetTarget(targetType, offset);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void typeArgumentTarget(int pos, int targetType, int offset, int typeArgumentIndex) throws Exception {
/* 367 */       this.writer.typeArgumentTarget(targetType, offset, typeArgumentIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     int typePath(int pos, int pathLength) throws Exception {
/* 372 */       this.writer.typePath(pathLength);
/* 373 */       return super.typePath(pos, pathLength);
/*     */     }
/*     */ 
/*     */     
/*     */     void typePath(int pos, int typePathKind, int typeArgumentIndex) throws Exception {
/* 378 */       this.writer.typePathPath(typePathKind, typeArgumentIndex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\TypeAnnotationsAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */