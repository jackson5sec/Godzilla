/*     */ package org.fife.rsta.ac.java.classreader.constantpool;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstantPoolInfoFactory
/*     */   implements ConstantTypes
/*     */ {
/*     */   public static ConstantPoolInfo readConstantPoolInfo(ClassFile cf, DataInputStream in) throws IOException {
/*     */     ConstantPoolInfo cpi;
/*     */     int nameIndex, highBytes, lowBytes, classIndex, nameAndTypeIndex, bytes, descriptorIndex, stringIndex, count;
/*     */     byte[] byteArray;
/*  33 */     int referenceKind, referenceIndex, bootstrapMethodAttrIndex, tag = in.read();
/*     */     
/*  35 */     switch (tag) {
/*     */       
/*     */       case 7:
/*  38 */         nameIndex = in.readUnsignedShort();
/*  39 */         cpi = new ConstantClassInfo(nameIndex);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 122 */         return cpi;case 6: highBytes = in.readInt(); lowBytes = in.readInt(); cpi = new ConstantDoubleInfo(highBytes, lowBytes); return cpi;case 9: classIndex = in.readUnsignedShort(); nameAndTypeIndex = in.readUnsignedShort(); cpi = new ConstantFieldrefInfo(classIndex, nameAndTypeIndex); return cpi;case 4: bytes = in.readInt(); cpi = new ConstantFloatInfo(bytes); return cpi;case 3: bytes = in.readInt(); cpi = new ConstantIntegerInfo(bytes); return cpi;case 11: classIndex = in.readUnsignedShort(); nameAndTypeIndex = in.readUnsignedShort(); cpi = new ConstantInterfaceMethodrefInfo(classIndex, nameAndTypeIndex); return cpi;case 5: highBytes = in.readInt(); lowBytes = in.readInt(); cpi = new ConstantLongInfo(highBytes, lowBytes); return cpi;case 10: classIndex = in.readUnsignedShort(); nameAndTypeIndex = in.readUnsignedShort(); cpi = new ConstantMethodrefInfo(classIndex, nameAndTypeIndex); return cpi;case 12: nameIndex = in.readUnsignedShort(); descriptorIndex = in.readUnsignedShort(); cpi = new ConstantNameAndTypeInfo(nameIndex, descriptorIndex); return cpi;case 8: stringIndex = in.readUnsignedShort(); cpi = new ConstantStringInfo(cf, stringIndex); return cpi;case 1: count = in.readUnsignedShort(); byteArray = new byte[count]; in.readFully(byteArray); cpi = new ConstantUtf8Info(byteArray); return cpi;case 15: referenceKind = in.read(); referenceIndex = in.readUnsignedShort(); cpi = new ConstantMethodHandleInfo(referenceKind, referenceIndex); return cpi;case 16: descriptorIndex = in.readUnsignedShort(); cpi = new ConstantMethodTypeInfo(descriptorIndex); return cpi;case 18: bootstrapMethodAttrIndex = in.readUnsignedShort(); nameAndTypeIndex = in.readUnsignedShort(); cpi = new ConstantInvokeDynamicInfo(bootstrapMethodAttrIndex, nameAndTypeIndex); return cpi;
/*     */     } 
/*     */     throw new IOException("Unknown tag for constant pool info: " + tag);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantPoolInfoFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */