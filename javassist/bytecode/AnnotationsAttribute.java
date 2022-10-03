/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javassist.bytecode.annotation.Annotation;
/*     */ import javassist.bytecode.annotation.AnnotationMemberValue;
/*     */ import javassist.bytecode.annotation.AnnotationsWriter;
/*     */ import javassist.bytecode.annotation.ArrayMemberValue;
/*     */ import javassist.bytecode.annotation.BooleanMemberValue;
/*     */ import javassist.bytecode.annotation.ByteMemberValue;
/*     */ import javassist.bytecode.annotation.CharMemberValue;
/*     */ import javassist.bytecode.annotation.ClassMemberValue;
/*     */ import javassist.bytecode.annotation.DoubleMemberValue;
/*     */ import javassist.bytecode.annotation.EnumMemberValue;
/*     */ import javassist.bytecode.annotation.FloatMemberValue;
/*     */ import javassist.bytecode.annotation.IntegerMemberValue;
/*     */ import javassist.bytecode.annotation.LongMemberValue;
/*     */ import javassist.bytecode.annotation.MemberValue;
/*     */ import javassist.bytecode.annotation.ShortMemberValue;
/*     */ import javassist.bytecode.annotation.StringMemberValue;
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
/*     */ public class AnnotationsAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String visibleTag = "RuntimeVisibleAnnotations";
/*     */   public static final String invisibleTag = "RuntimeInvisibleAnnotations";
/*     */   
/*     */   public AnnotationsAttribute(ConstPool cp, String attrname, byte[] info) {
/* 142 */     super(cp, attrname, info);
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
/*     */   public AnnotationsAttribute(ConstPool cp, String attrname) {
/* 157 */     this(cp, attrname, new byte[] { 0, 0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AnnotationsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 166 */     super(cp, n, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int numAnnotations() {
/* 173 */     return ByteArray.readU16bit(this.info, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/* 181 */     Copier copier = new Copier(this.info, this.constPool, newCp, classnames);
/*     */     try {
/* 183 */       copier.annotationArray();
/* 184 */       return new AnnotationsAttribute(newCp, getName(), copier.close());
/*     */     }
/* 186 */     catch (Exception e) {
/* 187 */       throw new RuntimeException(e);
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
/*     */   public Annotation getAnnotation(String type) {
/* 201 */     Annotation[] annotations = getAnnotations();
/* 202 */     for (int i = 0; i < annotations.length; i++) {
/* 203 */       if (annotations[i].getTypeName().equals(type)) {
/* 204 */         return annotations[i];
/*     */       }
/*     */     } 
/* 207 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnnotation(Annotation annotation) {
/* 217 */     String type = annotation.getTypeName();
/* 218 */     Annotation[] annotations = getAnnotations();
/* 219 */     for (int i = 0; i < annotations.length; i++) {
/* 220 */       if (annotations[i].getTypeName().equals(type)) {
/* 221 */         annotations[i] = annotation;
/* 222 */         setAnnotations(annotations);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 227 */     Annotation[] newlist = new Annotation[annotations.length + 1];
/* 228 */     System.arraycopy(annotations, 0, newlist, 0, annotations.length);
/* 229 */     newlist[annotations.length] = annotation;
/* 230 */     setAnnotations(newlist);
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
/*     */   public boolean removeAnnotation(String type) {
/* 243 */     Annotation[] annotations = getAnnotations();
/* 244 */     for (int i = 0; i < annotations.length; i++) {
/* 245 */       if (annotations[i].getTypeName().equals(type)) {
/* 246 */         Annotation[] newlist = new Annotation[annotations.length - 1];
/* 247 */         System.arraycopy(annotations, 0, newlist, 0, i);
/* 248 */         if (i < annotations.length - 1) {
/* 249 */           System.arraycopy(annotations, i + 1, newlist, i, annotations.length - i - 1);
/*     */         }
/*     */         
/* 252 */         setAnnotations(newlist);
/* 253 */         return true;
/*     */       } 
/*     */     } 
/* 256 */     return false;
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
/*     */   public Annotation[] getAnnotations() {
/*     */     try {
/* 270 */       return (new Parser(this.info, this.constPool)).parseAnnotations();
/*     */     }
/* 272 */     catch (Exception e) {
/* 273 */       throw new RuntimeException(e);
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
/*     */   public void setAnnotations(Annotation[] annotations) {
/* 285 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 286 */     AnnotationsWriter writer = new AnnotationsWriter(output, this.constPool);
/*     */     try {
/* 288 */       int n = annotations.length;
/* 289 */       writer.numAnnotations(n);
/* 290 */       for (int i = 0; i < n; i++) {
/* 291 */         annotations[i].write(writer);
/*     */       }
/* 293 */       writer.close();
/*     */     }
/* 295 */     catch (IOException e) {
/* 296 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 299 */     set(output.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAnnotation(Annotation annotation) {
/* 310 */     setAnnotations(new Annotation[] { annotation });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void renameClass(String oldname, String newname) {
/* 319 */     Map<String, String> map = new HashMap<>();
/* 320 */     map.put(oldname, newname);
/* 321 */     renameClass(map);
/*     */   }
/*     */ 
/*     */   
/*     */   void renameClass(Map<String, String> classnames) {
/* 326 */     Renamer renamer = new Renamer(this.info, getConstPool(), classnames);
/*     */     try {
/* 328 */       renamer.annotationArray();
/* 329 */     } catch (Exception e) {
/* 330 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   void getRefClasses(Map<String, String> classnames) {
/* 335 */     renameClass(classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 342 */     Annotation[] a = getAnnotations();
/* 343 */     StringBuilder sbuf = new StringBuilder();
/* 344 */     int i = 0;
/* 345 */     while (i < a.length) {
/* 346 */       sbuf.append(a[i++].toString());
/* 347 */       if (i != a.length) {
/* 348 */         sbuf.append(", ");
/*     */       }
/*     */     } 
/* 351 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */   static class Walker {
/*     */     byte[] info;
/*     */     
/*     */     Walker(byte[] attrInfo) {
/* 358 */       this.info = attrInfo;
/*     */     }
/*     */     
/*     */     final void parameters() throws Exception {
/* 362 */       int numParam = this.info[0] & 0xFF;
/* 363 */       parameters(numParam, 1);
/*     */     }
/*     */     
/*     */     void parameters(int numParam, int pos) throws Exception {
/* 367 */       for (int i = 0; i < numParam; i++)
/* 368 */         pos = annotationArray(pos); 
/*     */     }
/*     */     
/*     */     final void annotationArray() throws Exception {
/* 372 */       annotationArray(0);
/*     */     }
/*     */     
/*     */     final int annotationArray(int pos) throws Exception {
/* 376 */       int num = ByteArray.readU16bit(this.info, pos);
/* 377 */       return annotationArray(pos + 2, num);
/*     */     }
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 381 */       for (int i = 0; i < num; i++) {
/* 382 */         pos = annotation(pos);
/*     */       }
/* 384 */       return pos;
/*     */     }
/*     */     
/*     */     final int annotation(int pos) throws Exception {
/* 388 */       int type = ByteArray.readU16bit(this.info, pos);
/* 389 */       int numPairs = ByteArray.readU16bit(this.info, pos + 2);
/* 390 */       return annotation(pos + 4, type, numPairs);
/*     */     }
/*     */     
/*     */     int annotation(int pos, int type, int numPairs) throws Exception {
/* 394 */       for (int j = 0; j < numPairs; j++) {
/* 395 */         pos = memberValuePair(pos);
/*     */       }
/* 397 */       return pos;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final int memberValuePair(int pos) throws Exception {
/* 404 */       int nameIndex = ByteArray.readU16bit(this.info, pos);
/* 405 */       return memberValuePair(pos + 2, nameIndex);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int memberValuePair(int pos, int nameIndex) throws Exception {
/* 412 */       return memberValue(pos);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final int memberValue(int pos) throws Exception {
/* 419 */       int tag = this.info[pos] & 0xFF;
/* 420 */       if (tag == 101) {
/* 421 */         int typeNameIndex = ByteArray.readU16bit(this.info, pos + 1);
/* 422 */         int constNameIndex = ByteArray.readU16bit(this.info, pos + 3);
/* 423 */         enumMemberValue(pos, typeNameIndex, constNameIndex);
/* 424 */         return pos + 5;
/*     */       } 
/* 426 */       if (tag == 99) {
/* 427 */         int i = ByteArray.readU16bit(this.info, pos + 1);
/* 428 */         classMemberValue(pos, i);
/* 429 */         return pos + 3;
/*     */       } 
/* 431 */       if (tag == 64)
/* 432 */         return annotationMemberValue(pos + 1); 
/* 433 */       if (tag == 91) {
/* 434 */         int num = ByteArray.readU16bit(this.info, pos + 1);
/* 435 */         return arrayMemberValue(pos + 3, num);
/*     */       } 
/*     */       
/* 438 */       int index = ByteArray.readU16bit(this.info, pos + 1);
/* 439 */       constValueMember(tag, index);
/* 440 */       return pos + 3;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void constValueMember(int tag, int index) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void classMemberValue(int pos, int index) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int annotationMemberValue(int pos) throws Exception {
/* 465 */       return annotation(pos);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int arrayMemberValue(int pos, int num) throws Exception {
/* 472 */       for (int i = 0; i < num; i++) {
/* 473 */         pos = memberValue(pos);
/*     */       }
/*     */       
/* 476 */       return pos;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Renamer
/*     */     extends Walker
/*     */   {
/*     */     ConstPool cpool;
/*     */ 
/*     */     
/*     */     Map<String, String> classnames;
/*     */ 
/*     */ 
/*     */     
/*     */     Renamer(byte[] info, ConstPool cp, Map<String, String> map) {
/* 494 */       super(info);
/* 495 */       this.cpool = cp;
/* 496 */       this.classnames = map;
/*     */     }
/*     */ 
/*     */     
/*     */     int annotation(int pos, int type, int numPairs) throws Exception {
/* 501 */       renameType(pos - 4, type);
/* 502 */       return super.annotation(pos, type, numPairs);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {
/* 509 */       renameType(pos + 1, typeNameIndex);
/* 510 */       super.enumMemberValue(pos, typeNameIndex, constNameIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     void classMemberValue(int pos, int index) throws Exception {
/* 515 */       renameType(pos + 1, index);
/* 516 */       super.classMemberValue(pos, index);
/*     */     }
/*     */     
/*     */     private void renameType(int pos, int index) {
/* 520 */       String name = this.cpool.getUtf8Info(index);
/* 521 */       String newName = Descriptor.rename(name, this.classnames);
/* 522 */       if (!name.equals(newName)) {
/* 523 */         int index2 = this.cpool.addUtf8Info(newName);
/* 524 */         ByteArray.write16bit(index2, this.info, pos);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class Copier
/*     */     extends Walker
/*     */   {
/*     */     ByteArrayOutputStream output;
/*     */ 
/*     */     
/*     */     AnnotationsWriter writer;
/*     */     
/*     */     ConstPool srcPool;
/*     */     
/*     */     ConstPool destPool;
/*     */     
/*     */     Map<String, String> classnames;
/*     */ 
/*     */     
/*     */     Copier(byte[] info, ConstPool src, ConstPool dest, Map<String, String> map) {
/* 547 */       this(info, src, dest, map, true);
/*     */     }
/*     */     
/*     */     Copier(byte[] info, ConstPool src, ConstPool dest, Map<String, String> map, boolean makeWriter) {
/* 551 */       super(info);
/* 552 */       this.output = new ByteArrayOutputStream();
/* 553 */       if (makeWriter) {
/* 554 */         this.writer = new AnnotationsWriter(this.output, dest);
/*     */       }
/* 556 */       this.srcPool = src;
/* 557 */       this.destPool = dest;
/* 558 */       this.classnames = map;
/*     */     }
/*     */     
/*     */     byte[] close() throws IOException {
/* 562 */       this.writer.close();
/* 563 */       return this.output.toByteArray();
/*     */     }
/*     */ 
/*     */     
/*     */     void parameters(int numParam, int pos) throws Exception {
/* 568 */       this.writer.numParameters(numParam);
/* 569 */       super.parameters(numParam, pos);
/*     */     }
/*     */ 
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 574 */       this.writer.numAnnotations(num);
/* 575 */       return super.annotationArray(pos, num);
/*     */     }
/*     */ 
/*     */     
/*     */     int annotation(int pos, int type, int numPairs) throws Exception {
/* 580 */       this.writer.annotation(copyType(type), numPairs);
/* 581 */       return super.annotation(pos, type, numPairs);
/*     */     }
/*     */ 
/*     */     
/*     */     int memberValuePair(int pos, int nameIndex) throws Exception {
/* 586 */       this.writer.memberValuePair(copy(nameIndex));
/* 587 */       return super.memberValuePair(pos, nameIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     void constValueMember(int tag, int index) throws Exception {
/* 592 */       this.writer.constValueIndex(tag, copy(index));
/* 593 */       super.constValueMember(tag, index);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {
/* 600 */       this.writer.enumConstValue(copyType(typeNameIndex), copy(constNameIndex));
/* 601 */       super.enumMemberValue(pos, typeNameIndex, constNameIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     void classMemberValue(int pos, int index) throws Exception {
/* 606 */       this.writer.classInfoIndex(copyType(index));
/* 607 */       super.classMemberValue(pos, index);
/*     */     }
/*     */ 
/*     */     
/*     */     int annotationMemberValue(int pos) throws Exception {
/* 612 */       this.writer.annotationValue();
/* 613 */       return super.annotationMemberValue(pos);
/*     */     }
/*     */ 
/*     */     
/*     */     int arrayMemberValue(int pos, int num) throws Exception {
/* 618 */       this.writer.arrayValue(num);
/* 619 */       return super.arrayMemberValue(pos, num);
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
/*     */     int copy(int srcIndex) {
/* 632 */       return this.srcPool.copy(srcIndex, this.destPool, this.classnames);
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
/*     */     int copyType(int srcIndex) {
/* 646 */       String name = this.srcPool.getUtf8Info(srcIndex);
/* 647 */       String newName = Descriptor.rename(name, this.classnames);
/* 648 */       return this.destPool.addUtf8Info(newName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class Parser
/*     */     extends Walker
/*     */   {
/*     */     ConstPool pool;
/*     */     
/*     */     Annotation[][] allParams;
/*     */     
/*     */     Annotation[] allAnno;
/*     */     
/*     */     Annotation currentAnno;
/*     */     
/*     */     MemberValue currentMember;
/*     */     
/*     */     Parser(byte[] info, ConstPool cp) {
/* 667 */       super(info);
/* 668 */       this.pool = cp;
/*     */     }
/*     */     
/*     */     Annotation[][] parseParameters() throws Exception {
/* 672 */       parameters();
/* 673 */       return this.allParams;
/*     */     }
/*     */     
/*     */     Annotation[] parseAnnotations() throws Exception {
/* 677 */       annotationArray();
/* 678 */       return this.allAnno;
/*     */     }
/*     */     
/*     */     MemberValue parseMemberValue() throws Exception {
/* 682 */       memberValue(0);
/* 683 */       return this.currentMember;
/*     */     }
/*     */ 
/*     */     
/*     */     void parameters(int numParam, int pos) throws Exception {
/* 688 */       Annotation[][] params = new Annotation[numParam][];
/* 689 */       for (int i = 0; i < numParam; i++) {
/* 690 */         pos = annotationArray(pos);
/* 691 */         params[i] = this.allAnno;
/*     */       } 
/*     */       
/* 694 */       this.allParams = params;
/*     */     }
/*     */ 
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 699 */       Annotation[] array = new Annotation[num];
/* 700 */       for (int i = 0; i < num; i++) {
/* 701 */         pos = annotation(pos);
/* 702 */         array[i] = this.currentAnno;
/*     */       } 
/*     */       
/* 705 */       this.allAnno = array;
/* 706 */       return pos;
/*     */     }
/*     */ 
/*     */     
/*     */     int annotation(int pos, int type, int numPairs) throws Exception {
/* 711 */       this.currentAnno = new Annotation(type, this.pool);
/* 712 */       return super.annotation(pos, type, numPairs);
/*     */     }
/*     */ 
/*     */     
/*     */     int memberValuePair(int pos, int nameIndex) throws Exception {
/* 717 */       pos = super.memberValuePair(pos, nameIndex);
/* 718 */       this.currentAnno.addMemberValue(nameIndex, this.currentMember);
/* 719 */       return pos; } void constValueMember(int tag, int index) throws Exception { ByteMemberValue byteMemberValue; CharMemberValue charMemberValue; DoubleMemberValue doubleMemberValue; FloatMemberValue floatMemberValue;
/*     */       IntegerMemberValue integerMemberValue;
/*     */       LongMemberValue longMemberValue;
/*     */       ShortMemberValue shortMemberValue;
/*     */       BooleanMemberValue booleanMemberValue;
/*     */       StringMemberValue stringMemberValue;
/* 725 */       ConstPool cp = this.pool;
/* 726 */       switch (tag) {
/*     */         case 66:
/* 728 */           byteMemberValue = new ByteMemberValue(index, cp);
/*     */           break;
/*     */         case 67:
/* 731 */           charMemberValue = new CharMemberValue(index, cp);
/*     */           break;
/*     */         case 68:
/* 734 */           doubleMemberValue = new DoubleMemberValue(index, cp);
/*     */           break;
/*     */         case 70:
/* 737 */           floatMemberValue = new FloatMemberValue(index, cp);
/*     */           break;
/*     */         case 73:
/* 740 */           integerMemberValue = new IntegerMemberValue(index, cp);
/*     */           break;
/*     */         case 74:
/* 743 */           longMemberValue = new LongMemberValue(index, cp);
/*     */           break;
/*     */         case 83:
/* 746 */           shortMemberValue = new ShortMemberValue(index, cp);
/*     */           break;
/*     */         case 90:
/* 749 */           booleanMemberValue = new BooleanMemberValue(index, cp);
/*     */           break;
/*     */         case 115:
/* 752 */           stringMemberValue = new StringMemberValue(index, cp);
/*     */           break;
/*     */         default:
/* 755 */           throw new RuntimeException("unknown tag:" + tag);
/*     */       } 
/*     */       
/* 758 */       this.currentMember = (MemberValue)stringMemberValue;
/* 759 */       super.constValueMember(tag, index); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {
/* 766 */       this.currentMember = (MemberValue)new EnumMemberValue(typeNameIndex, constNameIndex, this.pool);
/*     */       
/* 768 */       super.enumMemberValue(pos, typeNameIndex, constNameIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     void classMemberValue(int pos, int index) throws Exception {
/* 773 */       this.currentMember = (MemberValue)new ClassMemberValue(index, this.pool);
/* 774 */       super.classMemberValue(pos, index);
/*     */     }
/*     */ 
/*     */     
/*     */     int annotationMemberValue(int pos) throws Exception {
/* 779 */       Annotation anno = this.currentAnno;
/* 780 */       pos = super.annotationMemberValue(pos);
/* 781 */       this.currentMember = (MemberValue)new AnnotationMemberValue(this.currentAnno, this.pool);
/* 782 */       this.currentAnno = anno;
/* 783 */       return pos;
/*     */     }
/*     */ 
/*     */     
/*     */     int arrayMemberValue(int pos, int num) throws Exception {
/* 788 */       ArrayMemberValue amv = new ArrayMemberValue(this.pool);
/* 789 */       MemberValue[] elements = new MemberValue[num];
/* 790 */       for (int i = 0; i < num; i++) {
/* 791 */         pos = memberValue(pos);
/* 792 */         elements[i] = this.currentMember;
/*     */       } 
/*     */       
/* 795 */       amv.setValue(elements);
/* 796 */       this.currentMember = (MemberValue)amv;
/* 797 */       return pos;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\AnnotationsAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */