/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javassist.bytecode.ConstPool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeAnnotationsWriter
/*     */   extends AnnotationsWriter
/*     */ {
/*     */   public TypeAnnotationsWriter(OutputStream os, ConstPool cp) {
/*  23 */     super(os, cp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void numAnnotations(int num) throws IOException {
/*  33 */     super.numAnnotations(num);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void typeParameterTarget(int targetType, int typeParameterIndex) throws IOException {
/*  43 */     this.output.write(targetType);
/*  44 */     this.output.write(typeParameterIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void supertypeTarget(int supertypeIndex) throws IOException {
/*  54 */     this.output.write(16);
/*  55 */     write16bit(supertypeIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void typeParameterBoundTarget(int targetType, int typeParameterIndex, int boundIndex) throws IOException {
/*  65 */     this.output.write(targetType);
/*  66 */     this.output.write(typeParameterIndex);
/*  67 */     this.output.write(boundIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void emptyTarget(int targetType) throws IOException {
/*  75 */     this.output.write(targetType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void formalParameterTarget(int formalParameterIndex) throws IOException {
/*  85 */     this.output.write(22);
/*  86 */     this.output.write(formalParameterIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void throwsTarget(int throwsTypeIndex) throws IOException {
/*  96 */     this.output.write(23);
/*  97 */     write16bit(throwsTypeIndex);
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
/*     */   public void localVarTarget(int targetType, int tableLength) throws IOException {
/* 109 */     this.output.write(targetType);
/* 110 */     write16bit(tableLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void localVarTargetTable(int startPc, int length, int index) throws IOException {
/* 120 */     write16bit(startPc);
/* 121 */     write16bit(length);
/* 122 */     write16bit(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void catchTarget(int exceptionTableIndex) throws IOException {
/* 132 */     this.output.write(66);
/* 133 */     write16bit(exceptionTableIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void offsetTarget(int targetType, int offset) throws IOException {
/* 143 */     this.output.write(targetType);
/* 144 */     write16bit(offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void typeArgumentTarget(int targetType, int offset, int type_argument_index) throws IOException {
/* 154 */     this.output.write(targetType);
/* 155 */     write16bit(offset);
/* 156 */     this.output.write(type_argument_index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void typePath(int pathLength) throws IOException {
/* 163 */     this.output.write(pathLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void typePathPath(int typePathKind, int typeArgumentIndex) throws IOException {
/* 172 */     this.output.write(typePathKind);
/* 173 */     this.output.write(typeArgumentIndex);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\TypeAnnotationsWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */