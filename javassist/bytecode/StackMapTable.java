/*      */ package javassist.bytecode;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.Map;
/*      */ import javassist.CannotCompileException;
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
/*      */ public class StackMapTable
/*      */   extends AttributeInfo
/*      */ {
/*      */   public static final String tag = "StackMapTable";
/*      */   public static final int TOP = 0;
/*      */   public static final int INTEGER = 1;
/*      */   public static final int FLOAT = 2;
/*      */   public static final int DOUBLE = 3;
/*      */   public static final int LONG = 4;
/*      */   public static final int NULL = 5;
/*      */   public static final int THIS = 6;
/*      */   public static final int OBJECT = 7;
/*      */   public static final int UNINIT = 8;
/*      */   
/*      */   StackMapTable(ConstPool cp, byte[] newInfo) {
/*   48 */     super(cp, "StackMapTable", newInfo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   StackMapTable(ConstPool cp, int name_id, DataInputStream in) throws IOException {
/*   54 */     super(cp, name_id, in);
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
/*      */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) throws RuntimeCopyException {
/*      */     try {
/*   71 */       return new StackMapTable(newCp, (new Copier(this.constPool, this.info, newCp, classnames))
/*   72 */           .doit());
/*      */     }
/*   74 */     catch (BadBytecode e) {
/*   75 */       throw new RuntimeCopyException("bad bytecode. fatal?");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class RuntimeCopyException
/*      */     extends RuntimeException
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public RuntimeCopyException(String s) {
/*   91 */       super(s);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void write(DataOutputStream out) throws IOException {
/*   97 */     super.write(out);
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
/*      */   public static class Walker
/*      */   {
/*      */     byte[] info;
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
/*      */     int numOfEntries;
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
/*      */     public Walker(StackMapTable smt) {
/*  159 */       this(smt.get());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Walker(byte[] data) {
/*  171 */       this.info = data;
/*  172 */       this.numOfEntries = ByteArray.readU16bit(data, 0);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final int size() {
/*  178 */       return this.numOfEntries;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void parse() throws BadBytecode {
/*  184 */       int n = this.numOfEntries;
/*  185 */       int pos = 2;
/*  186 */       for (int i = 0; i < n; i++) {
/*  187 */         pos = stackMapFrames(pos, i);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int stackMapFrames(int pos, int nth) throws BadBytecode {
/*  200 */       int type = this.info[pos] & 0xFF;
/*  201 */       if (type < 64)
/*  202 */       { sameFrame(pos, type);
/*  203 */         pos++; }
/*      */       
/*  205 */       else if (type < 128)
/*  206 */       { pos = sameLocals(pos, type); }
/*  207 */       else { if (type < 247) {
/*  208 */           throw new BadBytecode("bad frame_type " + type + " in StackMapTable (pos: " + pos + ", frame no.:" + nth + ")");
/*      */         }
/*      */ 
/*      */         
/*  212 */         if (type == 247) {
/*  213 */           pos = sameLocals(pos, type);
/*  214 */         } else if (type < 251) {
/*  215 */           int offset = ByteArray.readU16bit(this.info, pos + 1);
/*  216 */           chopFrame(pos, offset, 251 - type);
/*  217 */           pos += 3;
/*      */         }
/*  219 */         else if (type == 251) {
/*  220 */           int offset = ByteArray.readU16bit(this.info, pos + 1);
/*  221 */           sameFrame(pos, offset);
/*  222 */           pos += 3;
/*      */         }
/*  224 */         else if (type < 255) {
/*  225 */           pos = appendFrame(pos, type);
/*      */         } else {
/*  227 */           pos = fullFrame(pos);
/*      */         }  }
/*  229 */        return pos;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void sameFrame(int pos, int offsetDelta) throws BadBytecode {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int sameLocals(int pos, int type) throws BadBytecode {
/*  243 */       int offset, top = pos;
/*      */       
/*  245 */       if (type < 128) {
/*  246 */         offset = type - 64;
/*      */       } else {
/*  248 */         offset = ByteArray.readU16bit(this.info, pos + 1);
/*  249 */         pos += 2;
/*      */       } 
/*      */       
/*  252 */       int tag = this.info[pos + 1] & 0xFF;
/*  253 */       int data = 0;
/*  254 */       if (tag == 7 || tag == 8) {
/*  255 */         data = ByteArray.readU16bit(this.info, pos + 2);
/*  256 */         objectOrUninitialized(tag, data, pos + 2);
/*  257 */         pos += 2;
/*      */       } 
/*      */       
/*  260 */       sameLocals(top, offset, tag, data);
/*  261 */       return pos + 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) throws BadBytecode {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void chopFrame(int pos, int offsetDelta, int k) throws BadBytecode {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int appendFrame(int pos, int type) throws BadBytecode {
/*  289 */       int k = type - 251;
/*  290 */       int offset = ByteArray.readU16bit(this.info, pos + 1);
/*  291 */       int[] tags = new int[k];
/*  292 */       int[] data = new int[k];
/*  293 */       int p = pos + 3;
/*  294 */       for (int i = 0; i < k; i++) {
/*  295 */         int tag = this.info[p] & 0xFF;
/*  296 */         tags[i] = tag;
/*  297 */         if (tag == 7 || tag == 8) {
/*  298 */           data[i] = ByteArray.readU16bit(this.info, p + 1);
/*  299 */           objectOrUninitialized(tag, data[i], p + 1);
/*  300 */           p += 3;
/*      */         } else {
/*      */           
/*  303 */           data[i] = 0;
/*  304 */           p++;
/*      */         } 
/*      */       } 
/*      */       
/*  308 */       appendFrame(pos, offset, tags, data);
/*  309 */       return p;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) throws BadBytecode {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int fullFrame(int pos) throws BadBytecode {
/*  325 */       int offset = ByteArray.readU16bit(this.info, pos + 1);
/*  326 */       int numOfLocals = ByteArray.readU16bit(this.info, pos + 3);
/*  327 */       int[] localsTags = new int[numOfLocals];
/*  328 */       int[] localsData = new int[numOfLocals];
/*  329 */       int p = verifyTypeInfo(pos + 5, numOfLocals, localsTags, localsData);
/*  330 */       int numOfItems = ByteArray.readU16bit(this.info, p);
/*  331 */       int[] itemsTags = new int[numOfItems];
/*  332 */       int[] itemsData = new int[numOfItems];
/*  333 */       p = verifyTypeInfo(p + 2, numOfItems, itemsTags, itemsData);
/*  334 */       fullFrame(pos, offset, localsTags, localsData, itemsTags, itemsData);
/*  335 */       return p;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) throws BadBytecode {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int verifyTypeInfo(int pos, int n, int[] tags, int[] data) {
/*  355 */       for (int i = 0; i < n; i++) {
/*  356 */         int tag = this.info[pos++] & 0xFF;
/*  357 */         tags[i] = tag;
/*  358 */         if (tag == 7 || tag == 8) {
/*  359 */           data[i] = ByteArray.readU16bit(this.info, pos);
/*  360 */           objectOrUninitialized(tag, data[i], pos);
/*  361 */           pos += 2;
/*      */         } 
/*      */       } 
/*      */       
/*  365 */       return pos;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void objectOrUninitialized(int tag, int data, int pos) {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class SimpleCopy
/*      */     extends Walker
/*      */   {
/*      */     private StackMapTable.Writer writer;
/*      */ 
/*      */ 
/*      */     
/*      */     public SimpleCopy(byte[] data) {
/*  383 */       super(data);
/*  384 */       this.writer = new StackMapTable.Writer(data.length);
/*      */     }
/*      */     
/*      */     public byte[] doit() throws BadBytecode {
/*  388 */       parse();
/*  389 */       return this.writer.toByteArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public void sameFrame(int pos, int offsetDelta) {
/*  394 */       this.writer.sameFrame(offsetDelta);
/*      */     }
/*      */ 
/*      */     
/*      */     public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
/*  399 */       this.writer.sameLocals(offsetDelta, stackTag, copyData(stackTag, stackData));
/*      */     }
/*      */ 
/*      */     
/*      */     public void chopFrame(int pos, int offsetDelta, int k) {
/*  404 */       this.writer.chopFrame(offsetDelta, k);
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) {
/*  409 */       this.writer.appendFrame(offsetDelta, tags, copyData(tags, data));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/*  415 */       this.writer.fullFrame(offsetDelta, localTags, copyData(localTags, localData), stackTags, 
/*  416 */           copyData(stackTags, stackData));
/*      */     }
/*      */     
/*      */     protected int copyData(int tag, int data) {
/*  420 */       return data;
/*      */     }
/*      */     
/*      */     protected int[] copyData(int[] tags, int[] data) {
/*  424 */       return data;
/*      */     } }
/*      */   
/*      */   static class Copier extends SimpleCopy {
/*      */     private ConstPool srcPool;
/*      */     private ConstPool destPool;
/*      */     private Map<String, String> classnames;
/*      */     
/*      */     public Copier(ConstPool src, byte[] data, ConstPool dest, Map<String, String> names) {
/*  433 */       super(data);
/*  434 */       this.srcPool = src;
/*  435 */       this.destPool = dest;
/*  436 */       this.classnames = names;
/*      */     }
/*      */ 
/*      */     
/*      */     protected int copyData(int tag, int data) {
/*  441 */       if (tag == 7)
/*  442 */         return this.srcPool.copy(data, this.destPool, this.classnames); 
/*  443 */       return data;
/*      */     }
/*      */ 
/*      */     
/*      */     protected int[] copyData(int[] tags, int[] data) {
/*  448 */       int[] newData = new int[data.length];
/*  449 */       for (int i = 0; i < data.length; i++) {
/*  450 */         if (tags[i] == 7) {
/*  451 */           newData[i] = this.srcPool.copy(data[i], this.destPool, this.classnames);
/*      */         } else {
/*  453 */           newData[i] = data[i];
/*      */         } 
/*  455 */       }  return newData;
/*      */     }
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
/*      */   public void insertLocal(int index, int tag, int classInfo) throws BadBytecode {
/*  476 */     byte[] data = (new InsertLocal(get(), index, tag, classInfo)).doit();
/*  477 */     set(data);
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
/*      */   public static int typeTagOf(char descriptor) {
/*  490 */     switch (descriptor) {
/*      */       case 'D':
/*  492 */         return 3;
/*      */       case 'F':
/*  494 */         return 2;
/*      */       case 'J':
/*  496 */         return 4;
/*      */       case 'L':
/*      */       case '[':
/*  499 */         return 7;
/*      */     } 
/*      */     
/*  502 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   static class InsertLocal
/*      */     extends SimpleCopy
/*      */   {
/*      */     private int varIndex;
/*      */     
/*      */     private int varTag;
/*      */     
/*      */     private int varData;
/*      */     
/*      */     public InsertLocal(byte[] data, int varIndex, int varTag, int varData) {
/*  516 */       super(data);
/*  517 */       this.varIndex = varIndex;
/*  518 */       this.varTag = varTag;
/*  519 */       this.varData = varData;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/*  525 */       int len = localTags.length;
/*  526 */       if (len < this.varIndex) {
/*  527 */         super.fullFrame(pos, offsetDelta, localTags, localData, stackTags, stackData);
/*      */         
/*      */         return;
/*      */       } 
/*  531 */       int typeSize = (this.varTag == 4 || this.varTag == 3) ? 2 : 1;
/*  532 */       int[] localTags2 = new int[len + typeSize];
/*  533 */       int[] localData2 = new int[len + typeSize];
/*  534 */       int index = this.varIndex;
/*  535 */       int j = 0;
/*  536 */       for (int i = 0; i < len; i++) {
/*  537 */         if (j == index) {
/*  538 */           j += typeSize;
/*      */         }
/*  540 */         localTags2[j] = localTags[i];
/*  541 */         localData2[j++] = localData[i];
/*      */       } 
/*      */       
/*  544 */       localTags2[index] = this.varTag;
/*  545 */       localData2[index] = this.varData;
/*  546 */       if (typeSize > 1) {
/*  547 */         localTags2[index + 1] = 0;
/*  548 */         localData2[index + 1] = 0;
/*      */       } 
/*      */       
/*  551 */       super.fullFrame(pos, offsetDelta, localTags2, localData2, stackTags, stackData);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Writer
/*      */   {
/*      */     ByteArrayOutputStream output;
/*      */ 
/*      */     
/*      */     int numOfEntries;
/*      */ 
/*      */ 
/*      */     
/*      */     public Writer(int size) {
/*  567 */       this.output = new ByteArrayOutputStream(size);
/*  568 */       this.numOfEntries = 0;
/*  569 */       this.output.write(0);
/*  570 */       this.output.write(0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] toByteArray() {
/*  577 */       byte[] b = this.output.toByteArray();
/*  578 */       ByteArray.write16bit(this.numOfEntries, b, 0);
/*  579 */       return b;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public StackMapTable toStackMapTable(ConstPool cp) {
/*  590 */       return new StackMapTable(cp, toByteArray());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void sameFrame(int offsetDelta) {
/*  597 */       this.numOfEntries++;
/*  598 */       if (offsetDelta < 64) {
/*  599 */         this.output.write(offsetDelta);
/*      */       } else {
/*  601 */         this.output.write(251);
/*  602 */         write16(offsetDelta);
/*      */       } 
/*      */     }
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
/*      */     public void sameLocals(int offsetDelta, int tag, int data) {
/*  618 */       this.numOfEntries++;
/*  619 */       if (offsetDelta < 64) {
/*  620 */         this.output.write(offsetDelta + 64);
/*      */       } else {
/*  622 */         this.output.write(247);
/*  623 */         write16(offsetDelta);
/*      */       } 
/*      */       
/*  626 */       writeTypeInfo(tag, data);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void chopFrame(int offsetDelta, int k) {
/*  635 */       this.numOfEntries++;
/*  636 */       this.output.write(251 - k);
/*  637 */       write16(offsetDelta);
/*      */     }
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
/*      */     public void appendFrame(int offsetDelta, int[] tags, int[] data) {
/*  654 */       this.numOfEntries++;
/*  655 */       int k = tags.length;
/*  656 */       this.output.write(k + 251);
/*  657 */       write16(offsetDelta);
/*  658 */       for (int i = 0; i < k; i++) {
/*  659 */         writeTypeInfo(tags[i], data[i]);
/*      */       }
/*      */     }
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
/*      */     public void fullFrame(int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/*  683 */       this.numOfEntries++;
/*  684 */       this.output.write(255);
/*  685 */       write16(offsetDelta);
/*  686 */       int n = localTags.length;
/*  687 */       write16(n); int i;
/*  688 */       for (i = 0; i < n; i++) {
/*  689 */         writeTypeInfo(localTags[i], localData[i]);
/*      */       }
/*  691 */       n = stackTags.length;
/*  692 */       write16(n);
/*  693 */       for (i = 0; i < n; i++)
/*  694 */         writeTypeInfo(stackTags[i], stackData[i]); 
/*      */     }
/*      */     
/*      */     private void writeTypeInfo(int tag, int data) {
/*  698 */       this.output.write(tag);
/*  699 */       if (tag == 7 || tag == 8)
/*  700 */         write16(data); 
/*      */     }
/*      */     
/*      */     private void write16(int value) {
/*  704 */       this.output.write(value >>> 8 & 0xFF);
/*  705 */       this.output.write(value & 0xFF);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void println(PrintWriter w) {
/*  713 */     Printer.print(this, w);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void println(PrintStream ps) {
/*  722 */     Printer.print(this, new PrintWriter(ps, true));
/*      */   }
/*      */ 
/*      */   
/*      */   static class Printer
/*      */     extends Walker
/*      */   {
/*      */     private PrintWriter writer;
/*      */     private int offset;
/*      */     
/*      */     public static void print(StackMapTable smt, PrintWriter writer) {
/*      */       try {
/*  734 */         (new Printer(smt.get(), writer)).parse();
/*      */       }
/*  736 */       catch (BadBytecode e) {
/*  737 */         writer.println(e.getMessage());
/*      */       } 
/*      */     }
/*      */     
/*      */     Printer(byte[] data, PrintWriter pw) {
/*  742 */       super(data);
/*  743 */       this.writer = pw;
/*  744 */       this.offset = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public void sameFrame(int pos, int offsetDelta) {
/*  749 */       this.offset += offsetDelta + 1;
/*  750 */       this.writer.println(this.offset + " same frame: " + offsetDelta);
/*      */     }
/*      */ 
/*      */     
/*      */     public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
/*  755 */       this.offset += offsetDelta + 1;
/*  756 */       this.writer.println(this.offset + " same locals: " + offsetDelta);
/*  757 */       printTypeInfo(stackTag, stackData);
/*      */     }
/*      */ 
/*      */     
/*      */     public void chopFrame(int pos, int offsetDelta, int k) {
/*  762 */       this.offset += offsetDelta + 1;
/*  763 */       this.writer.println(this.offset + " chop frame: " + offsetDelta + ",    " + k + " last locals");
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) {
/*  768 */       this.offset += offsetDelta + 1;
/*  769 */       this.writer.println(this.offset + " append frame: " + offsetDelta);
/*  770 */       for (int i = 0; i < tags.length; i++) {
/*  771 */         printTypeInfo(tags[i], data[i]);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/*  777 */       this.offset += offsetDelta + 1;
/*  778 */       this.writer.println(this.offset + " full frame: " + offsetDelta);
/*  779 */       this.writer.println("[locals]"); int i;
/*  780 */       for (i = 0; i < localTags.length; i++) {
/*  781 */         printTypeInfo(localTags[i], localData[i]);
/*      */       }
/*  783 */       this.writer.println("[stack]");
/*  784 */       for (i = 0; i < stackTags.length; i++)
/*  785 */         printTypeInfo(stackTags[i], stackData[i]); 
/*      */     }
/*      */     
/*      */     private void printTypeInfo(int tag, int data) {
/*  789 */       String msg = null;
/*  790 */       switch (tag) {
/*      */         case 0:
/*  792 */           msg = "top";
/*      */           break;
/*      */         case 1:
/*  795 */           msg = "integer";
/*      */           break;
/*      */         case 2:
/*  798 */           msg = "float";
/*      */           break;
/*      */         case 3:
/*  801 */           msg = "double";
/*      */           break;
/*      */         case 4:
/*  804 */           msg = "long";
/*      */           break;
/*      */         case 5:
/*  807 */           msg = "null";
/*      */           break;
/*      */         case 6:
/*  810 */           msg = "this";
/*      */           break;
/*      */         case 7:
/*  813 */           msg = "object (cpool_index " + data + ")";
/*      */           break;
/*      */         case 8:
/*  816 */           msg = "uninitialized (offset " + data + ")";
/*      */           break;
/*      */       } 
/*      */       
/*  820 */       this.writer.print("    ");
/*  821 */       this.writer.println(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void shiftPc(int where, int gapSize, boolean exclusive) throws BadBytecode {
/*  828 */     (new OffsetShifter(this, where, gapSize)).parse();
/*  829 */     (new Shifter(this, where, gapSize, exclusive)).doit();
/*      */   }
/*      */   
/*      */   static class OffsetShifter extends Walker { int where;
/*      */     int gap;
/*      */     
/*      */     public OffsetShifter(StackMapTable smt, int where, int gap) {
/*  836 */       super(smt);
/*  837 */       this.where = where;
/*  838 */       this.gap = gap;
/*      */     }
/*      */ 
/*      */     
/*      */     public void objectOrUninitialized(int tag, int data, int pos) {
/*  843 */       if (tag == 8 && 
/*  844 */         this.where <= data)
/*  845 */         ByteArray.write16bit(data + this.gap, this.info, pos); 
/*      */     } }
/*      */   
/*      */   static class Shifter extends Walker {
/*      */     private StackMapTable stackMap;
/*      */     int where;
/*      */     int gap;
/*      */     int position;
/*      */     byte[] updatedInfo;
/*      */     boolean exclusive;
/*      */     
/*      */     public Shifter(StackMapTable smt, int where, int gap, boolean exclusive) {
/*  857 */       super(smt);
/*  858 */       this.stackMap = smt;
/*  859 */       this.where = where;
/*  860 */       this.gap = gap;
/*  861 */       this.position = 0;
/*  862 */       this.updatedInfo = null;
/*  863 */       this.exclusive = exclusive;
/*      */     }
/*      */     
/*      */     public void doit() throws BadBytecode {
/*  867 */       parse();
/*  868 */       if (this.updatedInfo != null) {
/*  869 */         this.stackMap.set(this.updatedInfo);
/*      */       }
/*      */     }
/*      */     
/*      */     public void sameFrame(int pos, int offsetDelta) {
/*  874 */       update(pos, offsetDelta, 0, 251);
/*      */     }
/*      */ 
/*      */     
/*      */     public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
/*  879 */       update(pos, offsetDelta, 64, 247);
/*      */     }
/*      */     void update(int pos, int offsetDelta, int base, int entry) {
/*      */       boolean match;
/*  883 */       int oldPos = this.position;
/*  884 */       this.position = oldPos + offsetDelta + ((oldPos == 0) ? 0 : 1);
/*      */       
/*  886 */       if (this.exclusive) {
/*      */ 
/*      */ 
/*      */         
/*  890 */         match = ((oldPos == 0 && this.where == 0) || (oldPos < this.where && this.where <= this.position));
/*      */       } else {
/*      */         
/*  893 */         match = (oldPos <= this.where && this.where < this.position);
/*      */       } 
/*  895 */       if (match) {
/*  896 */         int current = this.info[pos] & 0xFF;
/*  897 */         int newDelta = offsetDelta + this.gap;
/*  898 */         this.position += this.gap;
/*  899 */         if (newDelta < 64) {
/*  900 */           this.info[pos] = (byte)(newDelta + base);
/*  901 */         } else if (offsetDelta < 64 && current != entry) {
/*  902 */           byte[] newinfo = insertGap(this.info, pos, 2);
/*  903 */           newinfo[pos] = (byte)entry;
/*  904 */           ByteArray.write16bit(newDelta, newinfo, pos + 1);
/*  905 */           this.updatedInfo = newinfo;
/*      */         } else {
/*      */           
/*  908 */           ByteArray.write16bit(newDelta, this.info, pos + 1);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     static byte[] insertGap(byte[] info, int where, int gap) {
/*  913 */       int len = info.length;
/*  914 */       byte[] newinfo = new byte[len + gap];
/*  915 */       for (int i = 0; i < len; i++) {
/*  916 */         newinfo[i + ((i < where) ? 0 : gap)] = info[i];
/*      */       }
/*  918 */       return newinfo;
/*      */     }
/*      */ 
/*      */     
/*      */     public void chopFrame(int pos, int offsetDelta, int k) {
/*  923 */       update(pos, offsetDelta);
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) {
/*  928 */       update(pos, offsetDelta);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/*  934 */       update(pos, offsetDelta);
/*      */     }
/*      */     void update(int pos, int offsetDelta) {
/*      */       boolean match;
/*  938 */       int oldPos = this.position;
/*  939 */       this.position = oldPos + offsetDelta + ((oldPos == 0) ? 0 : 1);
/*      */       
/*  941 */       if (this.exclusive) {
/*  942 */         match = ((oldPos == 0 && this.where == 0) || (oldPos < this.where && this.where <= this.position));
/*      */       } else {
/*      */         
/*  945 */         match = (oldPos <= this.where && this.where < this.position);
/*      */       } 
/*  947 */       if (match) {
/*  948 */         int newDelta = offsetDelta + this.gap;
/*  949 */         ByteArray.write16bit(newDelta, this.info, pos + 1);
/*  950 */         this.position += this.gap;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void shiftForSwitch(int where, int gapSize) throws BadBytecode {
/*  959 */     (new SwitchShifter(this, where, gapSize)).doit();
/*      */   }
/*      */   
/*      */   static class SwitchShifter extends Shifter {
/*      */     SwitchShifter(StackMapTable smt, int where, int gap) {
/*  964 */       super(smt, where, gap, false);
/*      */     }
/*      */ 
/*      */     
/*      */     void update(int pos, int offsetDelta, int base, int entry) {
/*  969 */       int oldPos = this.position;
/*  970 */       this.position = oldPos + offsetDelta + ((oldPos == 0) ? 0 : 1);
/*  971 */       int newDelta = offsetDelta;
/*  972 */       if (this.where == this.position) {
/*  973 */         newDelta = offsetDelta - this.gap;
/*  974 */       } else if (this.where == oldPos) {
/*  975 */         newDelta = offsetDelta + this.gap;
/*      */       } else {
/*      */         return;
/*      */       } 
/*  979 */       if (offsetDelta < 64) {
/*  980 */         if (newDelta < 64) {
/*  981 */           this.info[pos] = (byte)(newDelta + base);
/*      */         } else {
/*  983 */           byte[] newinfo = insertGap(this.info, pos, 2);
/*  984 */           newinfo[pos] = (byte)entry;
/*  985 */           ByteArray.write16bit(newDelta, newinfo, pos + 1);
/*  986 */           this.updatedInfo = newinfo;
/*      */         }
/*      */       
/*  989 */       } else if (newDelta < 64) {
/*  990 */         byte[] newinfo = deleteGap(this.info, pos, 2);
/*  991 */         newinfo[pos] = (byte)(newDelta + base);
/*  992 */         this.updatedInfo = newinfo;
/*      */       } else {
/*      */         
/*  995 */         ByteArray.write16bit(newDelta, this.info, pos + 1);
/*      */       } 
/*      */     }
/*      */     static byte[] deleteGap(byte[] info, int where, int gap) {
/*  999 */       where += gap;
/* 1000 */       int len = info.length;
/* 1001 */       byte[] newinfo = new byte[len - gap];
/* 1002 */       for (int i = 0; i < len; i++) {
/* 1003 */         newinfo[i - ((i < where) ? 0 : gap)] = info[i];
/*      */       }
/* 1005 */       return newinfo;
/*      */     }
/*      */ 
/*      */     
/*      */     void update(int pos, int offsetDelta) {
/* 1010 */       int oldPos = this.position;
/* 1011 */       this.position = oldPos + offsetDelta + ((oldPos == 0) ? 0 : 1);
/* 1012 */       int newDelta = offsetDelta;
/* 1013 */       if (this.where == this.position) {
/* 1014 */         newDelta = offsetDelta - this.gap;
/* 1015 */       } else if (this.where == oldPos) {
/* 1016 */         newDelta = offsetDelta + this.gap;
/*      */       } else {
/*      */         return;
/*      */       } 
/* 1020 */       ByteArray.write16bit(newDelta, this.info, pos + 1);
/*      */     }
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
/*      */   public void removeNew(int where) throws CannotCompileException {
/*      */     try {
/* 1035 */       byte[] data = (new NewRemover(get(), where)).doit();
/* 1036 */       set(data);
/*      */     }
/* 1038 */     catch (BadBytecode e) {
/* 1039 */       throw new CannotCompileException("bad stack map table", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   static class NewRemover extends SimpleCopy {
/*      */     int posOfNew;
/*      */     
/*      */     public NewRemover(byte[] data, int pos) {
/* 1047 */       super(data);
/* 1048 */       this.posOfNew = pos;
/*      */     }
/*      */ 
/*      */     
/*      */     public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
/* 1053 */       if (stackTag == 8 && stackData == this.posOfNew) {
/* 1054 */         sameFrame(pos, offsetDelta);
/*      */       } else {
/* 1056 */         super.sameLocals(pos, offsetDelta, stackTag, stackData);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/* 1062 */       int n = stackTags.length - 1;
/* 1063 */       for (int i = 0; i < n; i++) {
/* 1064 */         if (stackTags[i] == 8 && stackData[i] == this.posOfNew && stackTags[i + 1] == 8 && stackData[i + 1] == this.posOfNew) {
/*      */           
/* 1066 */           n++;
/* 1067 */           int[] stackTags2 = new int[n - 2];
/* 1068 */           int[] stackData2 = new int[n - 2];
/* 1069 */           int k = 0;
/* 1070 */           for (int j = 0; j < n; j++) {
/* 1071 */             if (j == i) {
/* 1072 */               j++;
/*      */             } else {
/* 1074 */               stackTags2[k] = stackTags[j];
/* 1075 */               stackData2[k++] = stackData[j];
/*      */             } 
/*      */           } 
/* 1078 */           stackTags = stackTags2;
/* 1079 */           stackData = stackData2;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1083 */       super.fullFrame(pos, offsetDelta, localTags, localData, stackTags, stackData);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\StackMapTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */