/*      */ package javassist.bytecode;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CodeIterator
/*      */   implements Opcode
/*      */ {
/*      */   protected CodeAttribute codeAttr;
/*      */   protected byte[] bytecode;
/*      */   protected int endPos;
/*      */   protected int currentPos;
/*      */   protected int mark;
/*      */   protected int mark2;
/*      */   
/*      */   protected CodeIterator(CodeAttribute ca) {
/*   58 */     this.codeAttr = ca;
/*   59 */     this.bytecode = ca.getCode();
/*   60 */     begin();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void begin() {
/*   67 */     this.currentPos = this.mark = this.mark2 = 0;
/*   68 */     this.endPos = getCodeLength();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void move(int index) {
/*   84 */     this.currentPos = index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMark(int index) {
/*   98 */     this.mark = index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMark2(int index) {
/*  112 */     this.mark2 = index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMark() {
/*  123 */     return this.mark;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMark2() {
/*  133 */     return this.mark2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CodeAttribute get() {
/*  139 */     return this.codeAttr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCodeLength() {
/*  146 */     return this.bytecode.length;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int byteAt(int index) {
/*  152 */     return this.bytecode[index] & 0xFF;
/*      */   }
/*      */ 
/*      */   
/*      */   public int signedByteAt(int index) {
/*  157 */     return this.bytecode[index];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeByte(int value, int index) {
/*  163 */     this.bytecode[index] = (byte)value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int u16bitAt(int index) {
/*  170 */     return ByteArray.readU16bit(this.bytecode, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int s16bitAt(int index) {
/*  177 */     return ByteArray.readS16bit(this.bytecode, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write16bit(int value, int index) {
/*  184 */     ByteArray.write16bit(value, this.bytecode, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int s32bitAt(int index) {
/*  191 */     return ByteArray.read32bit(this.bytecode, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write32bit(int value, int index) {
/*  198 */     ByteArray.write32bit(value, this.bytecode, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(byte[] code, int index) {
/*  207 */     int len = code.length;
/*  208 */     for (int j = 0; j < len; j++) {
/*  209 */       this.bytecode[index++] = code[j];
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasNext() {
/*  215 */     return (this.currentPos < this.endPos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int next() throws BadBytecode {
/*  228 */     int pos = this.currentPos;
/*  229 */     this.currentPos = nextOpcode(this.bytecode, pos);
/*  230 */     return pos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lookAhead() {
/*  242 */     return this.currentPos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int skipConstructor() throws BadBytecode {
/*  264 */     return skipSuperConstructor0(-1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int skipSuperConstructor() throws BadBytecode {
/*  286 */     return skipSuperConstructor0(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int skipThisConstructor() throws BadBytecode {
/*  308 */     return skipSuperConstructor0(1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int skipSuperConstructor0(int skipThis) throws BadBytecode {
/*  314 */     begin();
/*  315 */     ConstPool cp = this.codeAttr.getConstPool();
/*  316 */     String thisClassName = this.codeAttr.getDeclaringClass();
/*  317 */     int nested = 0;
/*  318 */     while (hasNext()) {
/*  319 */       int index = next();
/*  320 */       int c = byteAt(index);
/*  321 */       if (c == 187) {
/*  322 */         nested++; continue;
/*  323 */       }  if (c == 183) {
/*  324 */         int mref = ByteArray.readU16bit(this.bytecode, index + 1);
/*  325 */         if (cp.getMethodrefName(mref).equals("<init>") && 
/*  326 */           --nested < 0) {
/*  327 */           if (skipThis < 0) {
/*  328 */             return index;
/*      */           }
/*  330 */           String cname = cp.getMethodrefClassName(mref);
/*  331 */           if (cname.equals(thisClassName) == ((skipThis > 0))) {
/*  332 */             return index;
/*      */           }
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  339 */     begin();
/*  340 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int insert(byte[] code) throws BadBytecode {
/*  364 */     return insert0(this.currentPos, code, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insert(int pos, byte[] code) throws BadBytecode {
/*  389 */     insert0(pos, code, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int insertAt(int pos, byte[] code) throws BadBytecode {
/*  413 */     return insert0(pos, code, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int insertEx(byte[] code) throws BadBytecode {
/*  437 */     return insert0(this.currentPos, code, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertEx(int pos, byte[] code) throws BadBytecode {
/*  462 */     insert0(pos, code, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int insertExAt(int pos, byte[] code) throws BadBytecode {
/*  486 */     return insert0(pos, code, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int insert0(int pos, byte[] code, boolean exclusive) throws BadBytecode {
/*  496 */     int len = code.length;
/*  497 */     if (len <= 0) {
/*  498 */       return pos;
/*      */     }
/*      */     
/*  501 */     pos = (insertGapAt(pos, len, exclusive)).position;
/*      */     
/*  503 */     int p = pos;
/*  504 */     for (int j = 0; j < len; j++) {
/*  505 */       this.bytecode[p++] = code[j];
/*      */     }
/*  507 */     return pos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int insertGap(int length) throws BadBytecode {
/*  526 */     return (insertGapAt(this.currentPos, length, false)).position;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int insertGap(int pos, int length) throws BadBytecode {
/*  546 */     return (insertGapAt(pos, length, false)).length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int insertExGap(int length) throws BadBytecode {
/*  565 */     return (insertGapAt(this.currentPos, length, true)).position;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int insertExGap(int pos, int length) throws BadBytecode {
/*  585 */     return (insertGapAt(pos, length, true)).length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Gap
/*      */   {
/*      */     public int position;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Gap insertGapAt(int pos, int length, boolean exclusive) throws BadBytecode {
/*      */     byte[] c;
/*      */     int length2;
/*  644 */     Gap gap = new Gap();
/*  645 */     if (length <= 0) {
/*  646 */       gap.position = pos;
/*  647 */       gap.length = 0;
/*  648 */       return gap;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  653 */     if (this.bytecode.length + length > 32767) {
/*      */       
/*  655 */       c = insertGapCore0w(this.bytecode, pos, length, exclusive, 
/*  656 */           get().getExceptionTable(), this.codeAttr, gap);
/*  657 */       pos = gap.position;
/*  658 */       length2 = length;
/*      */     } else {
/*      */       
/*  661 */       int cur = this.currentPos;
/*  662 */       c = insertGapCore0(this.bytecode, pos, length, exclusive, 
/*  663 */           get().getExceptionTable(), this.codeAttr);
/*      */       
/*  665 */       length2 = c.length - this.bytecode.length;
/*  666 */       gap.position = pos;
/*  667 */       gap.length = length2;
/*  668 */       if (cur >= pos) {
/*  669 */         this.currentPos = cur + length2;
/*      */       }
/*  671 */       if (this.mark > pos || (this.mark == pos && exclusive)) {
/*  672 */         this.mark += length2;
/*      */       }
/*  674 */       if (this.mark2 > pos || (this.mark2 == pos && exclusive)) {
/*  675 */         this.mark2 += length2;
/*      */       }
/*      */     } 
/*  678 */     this.codeAttr.setCode(c);
/*  679 */     this.bytecode = c;
/*  680 */     this.endPos = getCodeLength();
/*  681 */     updateCursors(pos, length2);
/*  682 */     return gap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateCursors(int pos, int length) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insert(ExceptionTable et, int offset) {
/*  705 */     this.codeAttr.getExceptionTable().add(0, et, offset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int append(byte[] code) {
/*  715 */     int size = getCodeLength();
/*  716 */     int len = code.length;
/*  717 */     if (len <= 0) {
/*  718 */       return size;
/*      */     }
/*  720 */     appendGap(len);
/*  721 */     byte[] dest = this.bytecode;
/*  722 */     for (int i = 0; i < len; i++) {
/*  723 */       dest[i + size] = code[i];
/*      */     }
/*  725 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendGap(int gapLength) {
/*  734 */     byte[] code = this.bytecode;
/*  735 */     int codeLength = code.length;
/*  736 */     byte[] newcode = new byte[codeLength + gapLength];
/*      */     
/*      */     int i;
/*  739 */     for (i = 0; i < codeLength; i++) {
/*  740 */       newcode[i] = code[i];
/*      */     }
/*  742 */     for (i = codeLength; i < codeLength + gapLength; i++) {
/*  743 */       newcode[i] = 0;
/*      */     }
/*  745 */     this.codeAttr.setCode(newcode);
/*  746 */     this.bytecode = newcode;
/*  747 */     this.endPos = getCodeLength();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void append(ExceptionTable et, int offset) {
/*  759 */     ExceptionTable table = this.codeAttr.getExceptionTable();
/*  760 */     table.add(table.size(), et, offset);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  765 */   private static final int[] opcodeLength = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 0, 0, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 5, 5, 3, 2, 3, 1, 1, 3, 3, 1, 1, 0, 4, 3, 3, 5, 5 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int nextOpcode(byte[] code, int index) throws BadBytecode {
/*      */     int opcode;
/*      */     try {
/*  788 */       opcode = code[index] & 0xFF;
/*      */     }
/*  790 */     catch (IndexOutOfBoundsException e) {
/*  791 */       throw new BadBytecode("invalid opcode address");
/*      */     } 
/*      */     
/*      */     try {
/*  795 */       int len = opcodeLength[opcode];
/*  796 */       if (len > 0)
/*  797 */         return index + len; 
/*  798 */       if (opcode == 196) {
/*  799 */         if (code[index + 1] == -124) {
/*  800 */           return index + 6;
/*      */         }
/*  802 */         return index + 4;
/*  803 */       }  int index2 = (index & 0xFFFFFFFC) + 8;
/*  804 */       if (opcode == 171) {
/*  805 */         int npairs = ByteArray.read32bit(code, index2);
/*  806 */         return index2 + npairs * 8 + 4;
/*      */       } 
/*  808 */       if (opcode == 170) {
/*  809 */         int low = ByteArray.read32bit(code, index2);
/*  810 */         int high = ByteArray.read32bit(code, index2 + 4);
/*  811 */         return index2 + (high - low + 1) * 4 + 8;
/*      */       }
/*      */     
/*  814 */     } catch (IndexOutOfBoundsException indexOutOfBoundsException) {}
/*      */ 
/*      */ 
/*      */     
/*  818 */     throw new BadBytecode(opcode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class AlignmentException
/*      */     extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] insertGapCore0(byte[] code, int where, int gapLength, boolean exclusive, ExceptionTable etable, CodeAttribute ca) throws BadBytecode {
/*  847 */     if (gapLength <= 0) {
/*  848 */       return code;
/*      */     }
/*      */     try {
/*  851 */       return insertGapCore1(code, where, gapLength, exclusive, etable, ca);
/*      */     }
/*  853 */     catch (AlignmentException e) {
/*      */       try {
/*  855 */         return insertGapCore1(code, where, gapLength + 3 & 0xFFFFFFFC, exclusive, etable, ca);
/*      */       
/*      */       }
/*  858 */       catch (AlignmentException e2) {
/*  859 */         throw new RuntimeException("fatal error?");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] insertGapCore1(byte[] code, int where, int gapLength, boolean exclusive, ExceptionTable etable, CodeAttribute ca) throws BadBytecode, AlignmentException {
/*  869 */     int codeLength = code.length;
/*  870 */     byte[] newcode = new byte[codeLength + gapLength];
/*  871 */     insertGap2(code, where, gapLength, codeLength, newcode, exclusive);
/*  872 */     etable.shiftPc(where, gapLength, exclusive);
/*      */     
/*  874 */     LineNumberAttribute na = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
/*  875 */     if (na != null) {
/*  876 */       na.shiftPc(where, gapLength, exclusive);
/*      */     }
/*  878 */     LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/*      */     
/*  880 */     if (va != null) {
/*  881 */       va.shiftPc(where, gapLength, exclusive);
/*      */     }
/*      */     
/*  884 */     LocalVariableAttribute vta = (LocalVariableAttribute)ca.getAttribute("LocalVariableTypeTable");
/*      */     
/*  886 */     if (vta != null) {
/*  887 */       vta.shiftPc(where, gapLength, exclusive);
/*      */     }
/*  889 */     StackMapTable smt = (StackMapTable)ca.getAttribute("StackMapTable");
/*  890 */     if (smt != null) {
/*  891 */       smt.shiftPc(where, gapLength, exclusive);
/*      */     }
/*  893 */     StackMap sm = (StackMap)ca.getAttribute("StackMap");
/*  894 */     if (sm != null) {
/*  895 */       sm.shiftPc(where, gapLength, exclusive);
/*      */     }
/*  897 */     return newcode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void insertGap2(byte[] code, int where, int gapLength, int endPos, byte[] newcode, boolean exclusive) throws BadBytecode, AlignmentException {
/*  905 */     int i = 0;
/*  906 */     int j = 0;
/*  907 */     for (; i < endPos; i = nextPos) {
/*  908 */       if (i == where) {
/*  909 */         int j2 = j + gapLength;
/*  910 */         while (j < j2) {
/*  911 */           newcode[j++] = 0;
/*      */         }
/*      */       } 
/*  914 */       int nextPos = nextOpcode(code, i);
/*  915 */       int inst = code[i] & 0xFF;
/*      */       
/*  917 */       if ((153 <= inst && inst <= 168) || inst == 198 || inst == 199) {
/*      */ 
/*      */         
/*  920 */         int offset = code[i + 1] << 8 | code[i + 2] & 0xFF;
/*  921 */         offset = newOffset(i, offset, where, gapLength, exclusive);
/*  922 */         newcode[j] = code[i];
/*  923 */         ByteArray.write16bit(offset, newcode, j + 1);
/*  924 */         j += 3;
/*      */       }
/*  926 */       else if (inst == 200 || inst == 201) {
/*      */         
/*  928 */         int offset = ByteArray.read32bit(code, i + 1);
/*  929 */         offset = newOffset(i, offset, where, gapLength, exclusive);
/*  930 */         newcode[j++] = code[i];
/*  931 */         ByteArray.write32bit(offset, newcode, j);
/*  932 */         j += 4;
/*      */       }
/*  934 */       else if (inst == 170) {
/*  935 */         if (i != j && (gapLength & 0x3) != 0) {
/*  936 */           throw new AlignmentException();
/*      */         }
/*  938 */         int i2 = (i & 0xFFFFFFFC) + 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  945 */         j = copyGapBytes(newcode, j, code, i, i2);
/*      */         
/*  947 */         int defaultbyte = newOffset(i, ByteArray.read32bit(code, i2), where, gapLength, exclusive);
/*      */         
/*  949 */         ByteArray.write32bit(defaultbyte, newcode, j);
/*  950 */         int lowbyte = ByteArray.read32bit(code, i2 + 4);
/*  951 */         ByteArray.write32bit(lowbyte, newcode, j + 4);
/*  952 */         int highbyte = ByteArray.read32bit(code, i2 + 8);
/*  953 */         ByteArray.write32bit(highbyte, newcode, j + 8);
/*  954 */         j += 12;
/*  955 */         int i0 = i2 + 12;
/*  956 */         i2 = i0 + (highbyte - lowbyte + 1) * 4;
/*  957 */         while (i0 < i2) {
/*  958 */           int offset = newOffset(i, ByteArray.read32bit(code, i0), where, gapLength, exclusive);
/*      */           
/*  960 */           ByteArray.write32bit(offset, newcode, j);
/*  961 */           j += 4;
/*  962 */           i0 += 4;
/*      */         }
/*      */       
/*  965 */       } else if (inst == 171) {
/*  966 */         if (i != j && (gapLength & 0x3) != 0) {
/*  967 */           throw new AlignmentException();
/*      */         }
/*  969 */         int i2 = (i & 0xFFFFFFFC) + 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  977 */         j = copyGapBytes(newcode, j, code, i, i2);
/*      */         
/*  979 */         int defaultbyte = newOffset(i, ByteArray.read32bit(code, i2), where, gapLength, exclusive);
/*      */         
/*  981 */         ByteArray.write32bit(defaultbyte, newcode, j);
/*  982 */         int npairs = ByteArray.read32bit(code, i2 + 4);
/*  983 */         ByteArray.write32bit(npairs, newcode, j + 4);
/*  984 */         j += 8;
/*  985 */         int i0 = i2 + 8;
/*  986 */         i2 = i0 + npairs * 8;
/*  987 */         while (i0 < i2) {
/*  988 */           ByteArray.copy32bit(code, i0, newcode, j);
/*  989 */           int offset = newOffset(i, 
/*  990 */               ByteArray.read32bit(code, i0 + 4), where, gapLength, exclusive);
/*      */           
/*  992 */           ByteArray.write32bit(offset, newcode, j + 4);
/*  993 */           j += 8;
/*  994 */           i0 += 8;
/*      */         } 
/*      */       } else {
/*      */         
/*  998 */         while (i < nextPos)
/*  999 */           newcode[j++] = code[i++]; 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static int copyGapBytes(byte[] newcode, int j, byte[] code, int i, int iEnd) {
/* 1005 */     switch (iEnd - i) {
/*      */       case 4:
/* 1007 */         newcode[j++] = code[i++];
/*      */       case 3:
/* 1009 */         newcode[j++] = code[i++];
/*      */       case 2:
/* 1011 */         newcode[j++] = code[i++];
/*      */       case 1:
/* 1013 */         newcode[j++] = code[i++];
/*      */         break;
/*      */     } 
/*      */     
/* 1017 */     return j;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int newOffset(int i, int offset, int where, int gapLength, boolean exclusive) {
/* 1022 */     int target = i + offset;
/* 1023 */     if (i < where) {
/* 1024 */       if (where < target || (exclusive && where == target)) {
/* 1025 */         offset += gapLength;
/*      */       }
/* 1027 */     } else if (i == where) {
/*      */ 
/*      */       
/* 1030 */       if (target < where) {
/* 1031 */         offset -= gapLength;
/*      */       }
/*      */     }
/* 1034 */     else if (target < where || (!exclusive && where == target)) {
/* 1035 */       offset -= gapLength;
/*      */     } 
/* 1037 */     return offset;
/*      */   }
/*      */ 
/*      */   
/*      */   static class Pointers
/*      */   {
/*      */     int cursor;
/*      */     int mark0;
/*      */     int mark;
/*      */     int mark2;
/*      */     ExceptionTable etable;
/*      */     
/*      */     Pointers(int cur, int m, int m2, int m0, ExceptionTable et, CodeAttribute ca) {
/* 1050 */       this.cursor = cur;
/* 1051 */       this.mark = m;
/* 1052 */       this.mark2 = m2;
/* 1053 */       this.mark0 = m0;
/* 1054 */       this.etable = et;
/* 1055 */       this.line = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
/* 1056 */       this.vars = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/* 1057 */       this.types = (LocalVariableAttribute)ca.getAttribute("LocalVariableTypeTable");
/* 1058 */       this.stack = (StackMapTable)ca.getAttribute("StackMapTable");
/* 1059 */       this.stack2 = (StackMap)ca.getAttribute("StackMap");
/*      */     }
/*      */     LineNumberAttribute line; LocalVariableAttribute vars; LocalVariableAttribute types; StackMapTable stack; StackMap stack2;
/*      */     void shiftPc(int where, int gapLength, boolean exclusive) throws BadBytecode {
/* 1063 */       if (where < this.cursor || (where == this.cursor && exclusive)) {
/* 1064 */         this.cursor += gapLength;
/*      */       }
/* 1066 */       if (where < this.mark || (where == this.mark && exclusive)) {
/* 1067 */         this.mark += gapLength;
/*      */       }
/* 1069 */       if (where < this.mark2 || (where == this.mark2 && exclusive)) {
/* 1070 */         this.mark2 += gapLength;
/*      */       }
/* 1072 */       if (where < this.mark0 || (where == this.mark0 && exclusive)) {
/* 1073 */         this.mark0 += gapLength;
/*      */       }
/* 1075 */       this.etable.shiftPc(where, gapLength, exclusive);
/* 1076 */       if (this.line != null) {
/* 1077 */         this.line.shiftPc(where, gapLength, exclusive);
/*      */       }
/* 1079 */       if (this.vars != null) {
/* 1080 */         this.vars.shiftPc(where, gapLength, exclusive);
/*      */       }
/* 1082 */       if (this.types != null) {
/* 1083 */         this.types.shiftPc(where, gapLength, exclusive);
/*      */       }
/* 1085 */       if (this.stack != null) {
/* 1086 */         this.stack.shiftPc(where, gapLength, exclusive);
/*      */       }
/* 1088 */       if (this.stack2 != null)
/* 1089 */         this.stack2.shiftPc(where, gapLength, exclusive); 
/*      */     }
/*      */     
/*      */     void shiftForSwitch(int where, int gapLength) throws BadBytecode {
/* 1093 */       if (this.stack != null) {
/* 1094 */         this.stack.shiftForSwitch(where, gapLength);
/*      */       }
/* 1096 */       if (this.stack2 != null) {
/* 1097 */         this.stack2.shiftForSwitch(where, gapLength);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] changeLdcToLdcW(byte[] code, ExceptionTable etable, CodeAttribute ca, CodeAttribute.LdcEntry ldcs) throws BadBytecode {
/* 1108 */     Pointers pointers = new Pointers(0, 0, 0, 0, etable, ca);
/* 1109 */     List<Branch> jumps = makeJumpList(code, code.length, pointers);
/* 1110 */     while (ldcs != null) {
/* 1111 */       addLdcW(ldcs, jumps);
/* 1112 */       ldcs = ldcs.next;
/*      */     } 
/*      */     
/* 1115 */     byte[] r = insertGap2w(code, 0, 0, false, jumps, pointers);
/* 1116 */     return r;
/*      */   }
/*      */   
/*      */   private static void addLdcW(CodeAttribute.LdcEntry ldcs, List<Branch> jumps) {
/* 1120 */     int where = ldcs.where;
/* 1121 */     LdcW ldcw = new LdcW(where, ldcs.index);
/* 1122 */     int s = jumps.size();
/* 1123 */     for (int i = 0; i < s; i++) {
/* 1124 */       if (where < ((Branch)jumps.get(i)).orgPos) {
/* 1125 */         jumps.add(i, ldcw);
/*      */         return;
/*      */       } 
/*      */     } 
/* 1129 */     jumps.add(ldcw);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] insertGapCore0w(byte[] code, int where, int gapLength, boolean exclusive, ExceptionTable etable, CodeAttribute ca, Gap newWhere) throws BadBytecode {
/* 1149 */     if (gapLength <= 0) {
/* 1150 */       return code;
/*      */     }
/* 1152 */     Pointers pointers = new Pointers(this.currentPos, this.mark, this.mark2, where, etable, ca);
/* 1153 */     List<Branch> jumps = makeJumpList(code, code.length, pointers);
/* 1154 */     byte[] r = insertGap2w(code, where, gapLength, exclusive, jumps, pointers);
/* 1155 */     this.currentPos = pointers.cursor;
/* 1156 */     this.mark = pointers.mark;
/* 1157 */     this.mark2 = pointers.mark2;
/* 1158 */     int where2 = pointers.mark0;
/* 1159 */     if (where2 == this.currentPos && !exclusive) {
/* 1160 */       this.currentPos += gapLength;
/*      */     }
/* 1162 */     if (exclusive) {
/* 1163 */       where2 -= gapLength;
/*      */     }
/* 1165 */     newWhere.position = where2;
/* 1166 */     newWhere.length = gapLength;
/* 1167 */     return r;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] insertGap2w(byte[] code, int where, int gapLength, boolean exclusive, List<Branch> jumps, Pointers ptrs) throws BadBytecode {
/* 1174 */     if (gapLength > 0) {
/* 1175 */       ptrs.shiftPc(where, gapLength, exclusive);
/* 1176 */       for (Branch b : jumps) {
/* 1177 */         b.shift(where, gapLength, exclusive);
/*      */       }
/*      */     } 
/* 1180 */     boolean unstable = true;
/*      */     do {
/* 1182 */       while (unstable) {
/* 1183 */         unstable = false;
/* 1184 */         for (Branch b : jumps) {
/* 1185 */           if (b.expanded()) {
/* 1186 */             unstable = true;
/* 1187 */             int p = b.pos;
/* 1188 */             int delta = b.deltaSize();
/* 1189 */             ptrs.shiftPc(p, delta, false);
/* 1190 */             for (Branch bb : jumps) {
/* 1191 */               bb.shift(p, delta, false);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/* 1196 */       for (Branch b : jumps) {
/* 1197 */         int diff = b.gapChanged();
/* 1198 */         if (diff > 0) {
/* 1199 */           unstable = true;
/* 1200 */           int p = b.pos;
/* 1201 */           ptrs.shiftPc(p, diff, false);
/* 1202 */           for (Branch bb : jumps)
/* 1203 */             bb.shift(p, diff, false); 
/*      */         } 
/*      */       } 
/* 1206 */     } while (unstable);
/*      */     
/* 1208 */     return makeExapndedCode(code, jumps, where, gapLength);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static List<Branch> makeJumpList(byte[] code, int endPos, Pointers ptrs) throws BadBytecode {
/* 1214 */     List<Branch> jumps = new ArrayList<>();
/*      */     int i;
/* 1216 */     for (i = 0; i < endPos; i = nextPos) {
/* 1217 */       int nextPos = nextOpcode(code, i);
/* 1218 */       int inst = code[i] & 0xFF;
/*      */       
/* 1220 */       if ((153 <= inst && inst <= 168) || inst == 198 || inst == 199) {
/*      */         Branch b;
/*      */         
/* 1223 */         int offset = code[i + 1] << 8 | code[i + 2] & 0xFF;
/*      */         
/* 1225 */         if (inst == 167 || inst == 168) {
/* 1226 */           b = new Jump16(i, offset);
/*      */         } else {
/* 1228 */           b = new If16(i, offset);
/*      */         } 
/* 1230 */         jumps.add(b);
/*      */       }
/* 1232 */       else if (inst == 200 || inst == 201) {
/*      */         
/* 1234 */         int offset = ByteArray.read32bit(code, i + 1);
/* 1235 */         jumps.add(new Jump32(i, offset));
/*      */       }
/* 1237 */       else if (inst == 170) {
/* 1238 */         int i2 = (i & 0xFFFFFFFC) + 4;
/* 1239 */         int defaultbyte = ByteArray.read32bit(code, i2);
/* 1240 */         int lowbyte = ByteArray.read32bit(code, i2 + 4);
/* 1241 */         int highbyte = ByteArray.read32bit(code, i2 + 8);
/* 1242 */         int i0 = i2 + 12;
/* 1243 */         int size = highbyte - lowbyte + 1;
/* 1244 */         int[] offsets = new int[size];
/* 1245 */         for (int j = 0; j < size; j++) {
/* 1246 */           offsets[j] = ByteArray.read32bit(code, i0);
/* 1247 */           i0 += 4;
/*      */         } 
/*      */         
/* 1250 */         jumps.add(new Table(i, defaultbyte, lowbyte, highbyte, offsets, ptrs));
/*      */       }
/* 1252 */       else if (inst == 171) {
/* 1253 */         int i2 = (i & 0xFFFFFFFC) + 4;
/* 1254 */         int defaultbyte = ByteArray.read32bit(code, i2);
/* 1255 */         int npairs = ByteArray.read32bit(code, i2 + 4);
/* 1256 */         int i0 = i2 + 8;
/* 1257 */         int[] matches = new int[npairs];
/* 1258 */         int[] offsets = new int[npairs];
/* 1259 */         for (int j = 0; j < npairs; j++) {
/* 1260 */           matches[j] = ByteArray.read32bit(code, i0);
/* 1261 */           offsets[j] = ByteArray.read32bit(code, i0 + 4);
/* 1262 */           i0 += 8;
/*      */         } 
/*      */         
/* 1265 */         jumps.add(new Lookup(i, defaultbyte, matches, offsets, ptrs));
/*      */       } 
/*      */     } 
/*      */     
/* 1269 */     return jumps;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] makeExapndedCode(byte[] code, List<Branch> jumps, int where, int gapLength) throws BadBytecode {
/*      */     Branch b;
/* 1276 */     int bpos, n = jumps.size();
/* 1277 */     int size = code.length + gapLength;
/* 1278 */     for (Branch branch : jumps) {
/* 1279 */       size += branch.deltaSize();
/*      */     }
/* 1281 */     byte[] newcode = new byte[size];
/* 1282 */     int src = 0, dest = 0, bindex = 0;
/* 1283 */     int len = code.length;
/*      */ 
/*      */     
/* 1286 */     if (0 < n) {
/* 1287 */       b = jumps.get(0);
/* 1288 */       bpos = b.orgPos;
/*      */     } else {
/*      */       
/* 1291 */       b = null;
/* 1292 */       bpos = len;
/*      */     } 
/*      */     
/* 1295 */     while (src < len) {
/* 1296 */       if (src == where) {
/* 1297 */         int pos2 = dest + gapLength;
/* 1298 */         while (dest < pos2) {
/* 1299 */           newcode[dest++] = 0;
/*      */         }
/*      */       } 
/* 1302 */       if (src != bpos) {
/* 1303 */         newcode[dest++] = code[src++]; continue;
/*      */       } 
/* 1305 */       int s = b.write(src, code, dest, newcode);
/* 1306 */       src += s;
/* 1307 */       dest += s + b.deltaSize();
/* 1308 */       if (++bindex < n) {
/* 1309 */         b = jumps.get(bindex);
/* 1310 */         bpos = b.orgPos;
/*      */         continue;
/*      */       } 
/* 1313 */       b = null;
/* 1314 */       bpos = len;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1319 */     return newcode;
/*      */   }
/*      */   static abstract class Branch { int pos; int orgPos;
/*      */     
/*      */     Branch(int p) {
/* 1324 */       this.pos = this.orgPos = p;
/*      */     } void shift(int where, int gapLength, boolean exclusive) {
/* 1326 */       if (where < this.pos || (where == this.pos && exclusive)) {
/* 1327 */         this.pos += gapLength;
/*      */       }
/*      */     }
/*      */     
/*      */     static int shiftOffset(int i, int offset, int where, int gapLength, boolean exclusive) {
/* 1332 */       int target = i + offset;
/* 1333 */       if (i < where) {
/* 1334 */         if (where < target || (exclusive && where == target)) {
/* 1335 */           offset += gapLength;
/*      */         }
/* 1337 */       } else if (i == where) {
/*      */ 
/*      */         
/* 1340 */         if (target < where && exclusive) {
/* 1341 */           offset -= gapLength;
/* 1342 */         } else if (where < target && !exclusive) {
/* 1343 */           offset += gapLength;
/*      */         }
/*      */       
/* 1346 */       } else if (target < where || (!exclusive && where == target)) {
/* 1347 */         offset -= gapLength;
/*      */       } 
/* 1349 */       return offset;
/*      */     }
/*      */     
/* 1352 */     boolean expanded() { return false; }
/* 1353 */     int gapChanged() { return 0; } int deltaSize() {
/* 1354 */       return 0;
/*      */     }
/*      */     
/*      */     abstract int write(int param1Int1, byte[] param1ArrayOfbyte1, int param1Int2, byte[] param1ArrayOfbyte2) throws BadBytecode; }
/*      */ 
/*      */   
/*      */   static class LdcW
/*      */     extends Branch {
/*      */     int index;
/*      */     boolean state;
/*      */     
/*      */     LdcW(int p, int i) {
/* 1366 */       super(p);
/* 1367 */       this.index = i;
/* 1368 */       this.state = true;
/*      */     }
/*      */ 
/*      */     
/*      */     boolean expanded() {
/* 1373 */       if (this.state) {
/* 1374 */         this.state = false;
/* 1375 */         return true;
/*      */       } 
/* 1377 */       return false;
/*      */     }
/*      */     
/*      */     int deltaSize() {
/* 1381 */       return 1;
/*      */     }
/*      */     
/*      */     int write(int srcPos, byte[] code, int destPos, byte[] newcode) {
/* 1385 */       newcode[destPos] = 19;
/* 1386 */       ByteArray.write16bit(this.index, newcode, destPos + 1);
/* 1387 */       return 2;
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class Branch16 extends Branch {
/*      */     int offset;
/*      */     int state;
/*      */     static final int BIT16 = 0;
/*      */     static final int EXPAND = 1;
/*      */     static final int BIT32 = 2;
/*      */     
/*      */     Branch16(int p, int off) {
/* 1399 */       super(p);
/* 1400 */       this.offset = off;
/* 1401 */       this.state = 0;
/*      */     }
/*      */ 
/*      */     
/*      */     void shift(int where, int gapLength, boolean exclusive) {
/* 1406 */       this.offset = shiftOffset(this.pos, this.offset, where, gapLength, exclusive);
/* 1407 */       super.shift(where, gapLength, exclusive);
/* 1408 */       if (this.state == 0 && (
/* 1409 */         this.offset < -32768 || 32767 < this.offset)) {
/* 1410 */         this.state = 1;
/*      */       }
/*      */     }
/*      */     
/*      */     boolean expanded() {
/* 1415 */       if (this.state == 1) {
/* 1416 */         this.state = 2;
/* 1417 */         return true;
/*      */       } 
/* 1419 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     abstract int deltaSize();
/*      */     
/*      */     abstract void write32(int param1Int1, byte[] param1ArrayOfbyte1, int param1Int2, byte[] param1ArrayOfbyte2);
/*      */     
/*      */     int write(int src, byte[] code, int dest, byte[] newcode) {
/* 1428 */       if (this.state == 2) {
/* 1429 */         write32(src, code, dest, newcode);
/*      */       } else {
/* 1431 */         newcode[dest] = code[src];
/* 1432 */         ByteArray.write16bit(this.offset, newcode, dest + 1);
/*      */       } 
/*      */       
/* 1435 */       return 3;
/*      */     }
/*      */   }
/*      */   
/*      */   static class Jump16
/*      */     extends Branch16 {
/*      */     Jump16(int p, int off) {
/* 1442 */       super(p, off);
/*      */     }
/*      */ 
/*      */     
/*      */     int deltaSize() {
/* 1447 */       return (this.state == 2) ? 2 : 0;
/*      */     }
/*      */ 
/*      */     
/*      */     void write32(int src, byte[] code, int dest, byte[] newcode) {
/* 1452 */       newcode[dest] = (byte)(((code[src] & 0xFF) == 167) ? 200 : 201);
/* 1453 */       ByteArray.write32bit(this.offset, newcode, dest + 1);
/*      */     }
/*      */   }
/*      */   
/*      */   static class If16
/*      */     extends Branch16 {
/*      */     If16(int p, int off) {
/* 1460 */       super(p, off);
/*      */     }
/*      */ 
/*      */     
/*      */     int deltaSize() {
/* 1465 */       return (this.state == 2) ? 5 : 0;
/*      */     }
/*      */ 
/*      */     
/*      */     void write32(int src, byte[] code, int dest, byte[] newcode) {
/* 1470 */       newcode[dest] = (byte)opcode(code[src] & 0xFF);
/* 1471 */       newcode[dest + 1] = 0;
/* 1472 */       newcode[dest + 2] = 8;
/* 1473 */       newcode[dest + 3] = -56;
/* 1474 */       ByteArray.write32bit(this.offset - 3, newcode, dest + 4);
/*      */     }
/*      */     
/*      */     int opcode(int op) {
/* 1478 */       if (op == 198)
/* 1479 */         return 199; 
/* 1480 */       if (op == 199)
/* 1481 */         return 198; 
/* 1482 */       if ((op - 153 & 0x1) == 0)
/* 1483 */         return op + 1; 
/* 1484 */       return op - 1;
/*      */     }
/*      */   }
/*      */   
/*      */   static class Jump32 extends Branch {
/*      */     int offset;
/*      */     
/*      */     Jump32(int p, int off) {
/* 1492 */       super(p);
/* 1493 */       this.offset = off;
/*      */     }
/*      */ 
/*      */     
/*      */     void shift(int where, int gapLength, boolean exclusive) {
/* 1498 */       this.offset = shiftOffset(this.pos, this.offset, where, gapLength, exclusive);
/* 1499 */       super.shift(where, gapLength, exclusive);
/*      */     }
/*      */ 
/*      */     
/*      */     int write(int src, byte[] code, int dest, byte[] newcode) {
/* 1504 */       newcode[dest] = code[src];
/* 1505 */       ByteArray.write32bit(this.offset, newcode, dest + 1);
/* 1506 */       return 5;
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class Switcher extends Branch { int gap;
/*      */     int defaultByte;
/*      */     int[] offsets;
/*      */     CodeIterator.Pointers pointers;
/*      */     
/*      */     Switcher(int pos, int defaultByte, int[] offsets, CodeIterator.Pointers ptrs) {
/* 1516 */       super(pos);
/* 1517 */       this.gap = 3 - (pos & 0x3);
/* 1518 */       this.defaultByte = defaultByte;
/* 1519 */       this.offsets = offsets;
/* 1520 */       this.pointers = ptrs;
/*      */     }
/*      */ 
/*      */     
/*      */     void shift(int where, int gapLength, boolean exclusive) {
/* 1525 */       int p = this.pos;
/* 1526 */       this.defaultByte = shiftOffset(p, this.defaultByte, where, gapLength, exclusive);
/* 1527 */       int num = this.offsets.length;
/* 1528 */       for (int i = 0; i < num; i++) {
/* 1529 */         this.offsets[i] = shiftOffset(p, this.offsets[i], where, gapLength, exclusive);
/*      */       }
/* 1531 */       super.shift(where, gapLength, exclusive);
/*      */     }
/*      */ 
/*      */     
/*      */     int gapChanged() {
/* 1536 */       int newGap = 3 - (this.pos & 0x3);
/* 1537 */       if (newGap > this.gap) {
/* 1538 */         int diff = newGap - this.gap;
/* 1539 */         this.gap = newGap;
/* 1540 */         return diff;
/*      */       } 
/*      */       
/* 1543 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     int deltaSize() {
/* 1548 */       return this.gap - 3 - (this.orgPos & 0x3);
/*      */     }
/*      */ 
/*      */     
/*      */     int write(int src, byte[] code, int dest, byte[] newcode) throws BadBytecode {
/* 1553 */       int padding = 3 - (this.pos & 0x3);
/* 1554 */       int nops = this.gap - padding;
/* 1555 */       int bytecodeSize = 5 + 3 - (this.orgPos & 0x3) + tableSize();
/* 1556 */       if (nops > 0) {
/* 1557 */         adjustOffsets(bytecodeSize, nops);
/*      */       }
/* 1559 */       newcode[dest++] = code[src];
/* 1560 */       while (padding-- > 0) {
/* 1561 */         newcode[dest++] = 0;
/*      */       }
/* 1563 */       ByteArray.write32bit(this.defaultByte, newcode, dest);
/* 1564 */       int size = write2(dest + 4, newcode);
/* 1565 */       dest += size + 4;
/* 1566 */       while (nops-- > 0) {
/* 1567 */         newcode[dest++] = 0;
/*      */       }
/* 1569 */       return 5 + 3 - (this.orgPos & 0x3) + size;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract int write2(int param1Int, byte[] param1ArrayOfbyte);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract int tableSize();
/*      */ 
/*      */ 
/*      */     
/*      */     void adjustOffsets(int size, int nops) throws BadBytecode {
/* 1585 */       this.pointers.shiftForSwitch(this.pos + size, nops);
/* 1586 */       if (this.defaultByte == size) {
/* 1587 */         this.defaultByte -= nops;
/*      */       }
/* 1589 */       for (int i = 0; i < this.offsets.length; i++) {
/* 1590 */         if (this.offsets[i] == size)
/* 1591 */           this.offsets[i] = this.offsets[i] - nops; 
/*      */       } 
/*      */     } }
/*      */   
/*      */   static class Table extends Switcher { int low;
/*      */     int high;
/*      */     
/*      */     Table(int pos, int defaultByte, int low, int high, int[] offsets, CodeIterator.Pointers ptrs) {
/* 1599 */       super(pos, defaultByte, offsets, ptrs);
/* 1600 */       this.low = low;
/* 1601 */       this.high = high;
/*      */     }
/*      */ 
/*      */     
/*      */     int write2(int dest, byte[] newcode) {
/* 1606 */       ByteArray.write32bit(this.low, newcode, dest);
/* 1607 */       ByteArray.write32bit(this.high, newcode, dest + 4);
/* 1608 */       int n = this.offsets.length;
/* 1609 */       dest += 8;
/* 1610 */       for (int i = 0; i < n; i++) {
/* 1611 */         ByteArray.write32bit(this.offsets[i], newcode, dest);
/* 1612 */         dest += 4;
/*      */       } 
/*      */       
/* 1615 */       return 8 + 4 * n;
/*      */     }
/*      */     
/*      */     int tableSize() {
/* 1619 */       return 8 + 4 * this.offsets.length;
/*      */     } }
/*      */   
/*      */   static class Lookup extends Switcher {
/*      */     int[] matches;
/*      */     
/*      */     Lookup(int pos, int defaultByte, int[] matches, int[] offsets, CodeIterator.Pointers ptrs) {
/* 1626 */       super(pos, defaultByte, offsets, ptrs);
/* 1627 */       this.matches = matches;
/*      */     }
/*      */ 
/*      */     
/*      */     int write2(int dest, byte[] newcode) {
/* 1632 */       int n = this.matches.length;
/* 1633 */       ByteArray.write32bit(n, newcode, dest);
/* 1634 */       dest += 4;
/* 1635 */       for (int i = 0; i < n; i++) {
/* 1636 */         ByteArray.write32bit(this.matches[i], newcode, dest);
/* 1637 */         ByteArray.write32bit(this.offsets[i], newcode, dest + 4);
/* 1638 */         dest += 8;
/*      */       } 
/*      */       
/* 1641 */       return 4 + 8 * n;
/*      */     }
/*      */     
/*      */     int tableSize() {
/* 1645 */       return 4 + 8 * this.matches.length;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\CodeIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */