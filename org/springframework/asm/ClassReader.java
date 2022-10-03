/*      */ package org.springframework.asm;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassReader
/*      */ {
/*      */   public static final int SKIP_CODE = 1;
/*      */   public static final int SKIP_DEBUG = 2;
/*      */   public static final int SKIP_FRAMES = 4;
/*      */   public static final int EXPAND_FRAMES = 8;
/*      */   static final int EXPAND_ASM_INSNS = 256;
/*      */   private static final int MAX_BUFFER_SIZE = 1048576;
/*      */   private static final int INPUT_STREAM_DATA_CHUNK_SIZE = 4096;
/*      */   @Deprecated
/*      */   public final byte[] b;
/*      */   public final int header;
/*      */   final byte[] classFileBuffer;
/*      */   private final int[] cpInfoOffsets;
/*      */   private final String[] constantUtf8Values;
/*      */   private final ConstantDynamic[] constantDynamicValues;
/*      */   private final int[] bootstrapMethodOffsets;
/*      */   private final int maxStringLength;
/*      */   
/*      */   public ClassReader(byte[] classFile) {
/*  166 */     this(classFile, 0, classFile.length);
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
/*      */   public ClassReader(byte[] classFileBuffer, int classFileOffset, int classFileLength) {
/*  180 */     this(classFileBuffer, classFileOffset, true);
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
/*      */   ClassReader(byte[] classFileBuffer, int classFileOffset, boolean checkClassVersion) {
/*  193 */     this.classFileBuffer = classFileBuffer;
/*  194 */     this.b = classFileBuffer;
/*      */ 
/*      */     
/*  197 */     if (checkClassVersion && readShort(classFileOffset + 6) > 62) {
/*  198 */       throw new IllegalArgumentException("Unsupported class file major version " + 
/*  199 */           readShort(classFileOffset + 6));
/*      */     }
/*      */ 
/*      */     
/*  203 */     int constantPoolCount = readUnsignedShort(classFileOffset + 8);
/*  204 */     this.cpInfoOffsets = new int[constantPoolCount];
/*  205 */     this.constantUtf8Values = new String[constantPoolCount];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  210 */     int currentCpInfoIndex = 1;
/*  211 */     int currentCpInfoOffset = classFileOffset + 10;
/*  212 */     int currentMaxStringLength = 0;
/*  213 */     boolean hasBootstrapMethods = false;
/*  214 */     boolean hasConstantDynamic = false;
/*      */     
/*  216 */     while (currentCpInfoIndex < constantPoolCount) {
/*  217 */       int cpInfoSize; this.cpInfoOffsets[currentCpInfoIndex++] = currentCpInfoOffset + 1;
/*      */       
/*  219 */       switch (classFileBuffer[currentCpInfoOffset]) {
/*      */         case 3:
/*      */         case 4:
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*      */         case 12:
/*  226 */           cpInfoSize = 5;
/*      */           break;
/*      */         case 17:
/*  229 */           cpInfoSize = 5;
/*  230 */           hasBootstrapMethods = true;
/*  231 */           hasConstantDynamic = true;
/*      */           break;
/*      */         case 18:
/*  234 */           cpInfoSize = 5;
/*  235 */           hasBootstrapMethods = true;
/*      */           break;
/*      */         case 5:
/*      */         case 6:
/*  239 */           cpInfoSize = 9;
/*  240 */           currentCpInfoIndex++;
/*      */           break;
/*      */         case 1:
/*  243 */           cpInfoSize = 3 + readUnsignedShort(currentCpInfoOffset + 1);
/*  244 */           if (cpInfoSize > currentMaxStringLength)
/*      */           {
/*      */ 
/*      */             
/*  248 */             currentMaxStringLength = cpInfoSize;
/*      */           }
/*      */           break;
/*      */         case 15:
/*  252 */           cpInfoSize = 4;
/*      */           break;
/*      */         case 7:
/*      */         case 8:
/*      */         case 16:
/*      */         case 19:
/*      */         case 20:
/*  259 */           cpInfoSize = 3;
/*      */           break;
/*      */         default:
/*  262 */           throw new IllegalArgumentException();
/*      */       } 
/*  264 */       currentCpInfoOffset += cpInfoSize;
/*      */     } 
/*  266 */     this.maxStringLength = currentMaxStringLength;
/*      */     
/*  268 */     this.header = currentCpInfoOffset;
/*      */ 
/*      */     
/*  271 */     this.constantDynamicValues = hasConstantDynamic ? new ConstantDynamic[constantPoolCount] : null;
/*      */ 
/*      */     
/*  274 */     this
/*  275 */       .bootstrapMethodOffsets = hasBootstrapMethods ? readBootstrapMethodsAttribute(currentMaxStringLength) : null;
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
/*      */   public ClassReader(InputStream inputStream) throws IOException {
/*  287 */     this(readStream(inputStream, false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassReader(String className) throws IOException {
/*  298 */     this(
/*  299 */         readStream(
/*  300 */           ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class"), true));
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
/*      */   private static byte[] readStream(InputStream inputStream, boolean close) throws IOException {
/*  313 */     if (inputStream == null) {
/*  314 */       throw new IOException("Class not found");
/*      */     }
/*  316 */     int bufferSize = calculateBufferSize(inputStream);
/*  317 */     try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
/*  318 */       byte[] data = new byte[bufferSize];
/*      */       
/*  320 */       int readCount = 0; int bytesRead;
/*  321 */       while ((bytesRead = inputStream.read(data, 0, bufferSize)) != -1) {
/*  322 */         outputStream.write(data, 0, bytesRead);
/*  323 */         readCount++;
/*      */       } 
/*  325 */       outputStream.flush();
/*  326 */       if (readCount == 1);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  331 */       return outputStream.toByteArray();
/*      */     } finally {
/*  333 */       if (close) {
/*  334 */         inputStream.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private static int calculateBufferSize(InputStream inputStream) throws IOException {
/*  340 */     int expectedLength = inputStream.available();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  347 */     if (expectedLength < 256) {
/*  348 */       return 4096;
/*      */     }
/*  350 */     return Math.min(expectedLength, 1048576);
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
/*      */   public int getAccess() {
/*  365 */     return readUnsignedShort(this.header);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassName() {
/*  376 */     return readClass(this.header + 2, new char[this.maxStringLength]);
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
/*      */   public String getSuperName() {
/*  388 */     return readClass(this.header + 4, new char[this.maxStringLength]);
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
/*      */   public String[] getInterfaces() {
/*  400 */     int currentOffset = this.header + 6;
/*  401 */     int interfacesCount = readUnsignedShort(currentOffset);
/*  402 */     String[] interfaces = new String[interfacesCount];
/*  403 */     if (interfacesCount > 0) {
/*  404 */       char[] charBuffer = new char[this.maxStringLength];
/*  405 */       for (int i = 0; i < interfacesCount; i++) {
/*  406 */         currentOffset += 2;
/*  407 */         interfaces[i] = readClass(currentOffset, charBuffer);
/*      */       } 
/*      */     } 
/*  410 */     return interfaces;
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
/*      */   public void accept(ClassVisitor classVisitor, int parsingOptions) {
/*  426 */     accept(classVisitor, new Attribute[0], parsingOptions);
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
/*      */   public void accept(ClassVisitor classVisitor, Attribute[] attributePrototypes, int parsingOptions) {
/*  447 */     Context context = new Context();
/*  448 */     context.attributePrototypes = attributePrototypes;
/*  449 */     context.parsingOptions = parsingOptions;
/*  450 */     context.charBuffer = new char[this.maxStringLength];
/*      */ 
/*      */     
/*  453 */     char[] charBuffer = context.charBuffer;
/*  454 */     int currentOffset = this.header;
/*  455 */     int accessFlags = readUnsignedShort(currentOffset);
/*  456 */     String thisClass = readClass(currentOffset + 2, charBuffer);
/*  457 */     String superClass = readClass(currentOffset + 4, charBuffer);
/*  458 */     String[] interfaces = new String[readUnsignedShort(currentOffset + 6)];
/*  459 */     currentOffset += 8;
/*  460 */     for (int i = 0; i < interfaces.length; i++) {
/*  461 */       interfaces[i] = readClass(currentOffset, charBuffer);
/*  462 */       currentOffset += 2;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  468 */     int innerClassesOffset = 0;
/*      */     
/*  470 */     int enclosingMethodOffset = 0;
/*      */     
/*  472 */     String signature = null;
/*      */     
/*  474 */     String sourceFile = null;
/*      */     
/*  476 */     String sourceDebugExtension = null;
/*      */     
/*  478 */     int runtimeVisibleAnnotationsOffset = 0;
/*      */     
/*  480 */     int runtimeInvisibleAnnotationsOffset = 0;
/*      */     
/*  482 */     int runtimeVisibleTypeAnnotationsOffset = 0;
/*      */     
/*  484 */     int runtimeInvisibleTypeAnnotationsOffset = 0;
/*      */     
/*  486 */     int moduleOffset = 0;
/*      */     
/*  488 */     int modulePackagesOffset = 0;
/*      */     
/*  490 */     String moduleMainClass = null;
/*      */     
/*  492 */     String nestHostClass = null;
/*      */     
/*  494 */     int nestMembersOffset = 0;
/*      */     
/*  496 */     int permittedSubclassesOffset = 0;
/*      */     
/*  498 */     int recordOffset = 0;
/*      */ 
/*      */     
/*  501 */     Attribute attributes = null;
/*      */     
/*  503 */     int currentAttributeOffset = getFirstAttributeOffset();
/*  504 */     for (int j = readUnsignedShort(currentAttributeOffset - 2); j > 0; j--) {
/*      */       
/*  506 */       String attributeName = readUTF8(currentAttributeOffset, charBuffer);
/*  507 */       int attributeLength = readInt(currentAttributeOffset + 2);
/*  508 */       currentAttributeOffset += 6;
/*      */ 
/*      */       
/*  511 */       if ("SourceFile".equals(attributeName)) {
/*  512 */         sourceFile = readUTF8(currentAttributeOffset, charBuffer);
/*  513 */       } else if ("InnerClasses".equals(attributeName)) {
/*  514 */         innerClassesOffset = currentAttributeOffset;
/*  515 */       } else if ("EnclosingMethod".equals(attributeName)) {
/*  516 */         enclosingMethodOffset = currentAttributeOffset;
/*  517 */       } else if ("NestHost".equals(attributeName)) {
/*  518 */         nestHostClass = readClass(currentAttributeOffset, charBuffer);
/*  519 */       } else if ("NestMembers".equals(attributeName)) {
/*  520 */         nestMembersOffset = currentAttributeOffset;
/*  521 */       } else if ("PermittedSubclasses".equals(attributeName)) {
/*  522 */         permittedSubclassesOffset = currentAttributeOffset;
/*  523 */       } else if ("Signature".equals(attributeName)) {
/*  524 */         signature = readUTF8(currentAttributeOffset, charBuffer);
/*  525 */       } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
/*  526 */         runtimeVisibleAnnotationsOffset = currentAttributeOffset;
/*  527 */       } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
/*  528 */         runtimeVisibleTypeAnnotationsOffset = currentAttributeOffset;
/*  529 */       } else if ("Deprecated".equals(attributeName)) {
/*  530 */         accessFlags |= 0x20000;
/*  531 */       } else if ("Synthetic".equals(attributeName)) {
/*  532 */         accessFlags |= 0x1000;
/*  533 */       } else if ("SourceDebugExtension".equals(attributeName)) {
/*  534 */         if (attributeLength > this.classFileBuffer.length - currentAttributeOffset) {
/*  535 */           throw new IllegalArgumentException();
/*      */         }
/*      */         
/*  538 */         sourceDebugExtension = readUtf(currentAttributeOffset, attributeLength, new char[attributeLength]);
/*  539 */       } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
/*  540 */         runtimeInvisibleAnnotationsOffset = currentAttributeOffset;
/*  541 */       } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
/*  542 */         runtimeInvisibleTypeAnnotationsOffset = currentAttributeOffset;
/*  543 */       } else if ("Record".equals(attributeName)) {
/*  544 */         recordOffset = currentAttributeOffset;
/*  545 */         accessFlags |= 0x10000;
/*  546 */       } else if ("Module".equals(attributeName)) {
/*  547 */         moduleOffset = currentAttributeOffset;
/*  548 */       } else if ("ModuleMainClass".equals(attributeName)) {
/*  549 */         moduleMainClass = readClass(currentAttributeOffset, charBuffer);
/*  550 */       } else if ("ModulePackages".equals(attributeName)) {
/*  551 */         modulePackagesOffset = currentAttributeOffset;
/*  552 */       } else if (!"BootstrapMethods".equals(attributeName)) {
/*      */ 
/*      */         
/*  555 */         Attribute attribute = readAttribute(attributePrototypes, attributeName, currentAttributeOffset, attributeLength, charBuffer, -1, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  563 */         attribute.nextAttribute = attributes;
/*  564 */         attributes = attribute;
/*      */       } 
/*  566 */       currentAttributeOffset += attributeLength;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  571 */     classVisitor.visit(
/*  572 */         readInt(this.cpInfoOffsets[1] - 7), accessFlags, thisClass, signature, superClass, interfaces);
/*      */ 
/*      */     
/*  575 */     if ((parsingOptions & 0x2) == 0 && (sourceFile != null || sourceDebugExtension != null))
/*      */     {
/*  577 */       classVisitor.visitSource(sourceFile, sourceDebugExtension);
/*      */     }
/*      */ 
/*      */     
/*  581 */     if (moduleOffset != 0) {
/*  582 */       readModuleAttributes(classVisitor, context, moduleOffset, modulePackagesOffset, moduleMainClass);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  587 */     if (nestHostClass != null) {
/*  588 */       classVisitor.visitNestHost(nestHostClass);
/*      */     }
/*      */ 
/*      */     
/*  592 */     if (enclosingMethodOffset != 0) {
/*  593 */       String className = readClass(enclosingMethodOffset, charBuffer);
/*  594 */       int methodIndex = readUnsignedShort(enclosingMethodOffset + 2);
/*  595 */       String name = (methodIndex == 0) ? null : readUTF8(this.cpInfoOffsets[methodIndex], charBuffer);
/*  596 */       String type = (methodIndex == 0) ? null : readUTF8(this.cpInfoOffsets[methodIndex] + 2, charBuffer);
/*  597 */       classVisitor.visitOuterClass(className, name, type);
/*      */     } 
/*      */ 
/*      */     
/*  601 */     if (runtimeVisibleAnnotationsOffset != 0) {
/*  602 */       int numAnnotations = readUnsignedShort(runtimeVisibleAnnotationsOffset);
/*  603 */       int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
/*  604 */       while (numAnnotations-- > 0) {
/*      */         
/*  606 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  607 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  610 */         currentAnnotationOffset = readElementValues(classVisitor
/*  611 */             .visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  619 */     if (runtimeInvisibleAnnotationsOffset != 0) {
/*  620 */       int numAnnotations = readUnsignedShort(runtimeInvisibleAnnotationsOffset);
/*  621 */       int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
/*  622 */       while (numAnnotations-- > 0) {
/*      */         
/*  624 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  625 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  628 */         currentAnnotationOffset = readElementValues(classVisitor
/*  629 */             .visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  637 */     if (runtimeVisibleTypeAnnotationsOffset != 0) {
/*  638 */       int numAnnotations = readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
/*  639 */       int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
/*  640 */       while (numAnnotations-- > 0) {
/*      */         
/*  642 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/*  644 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  645 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  648 */         currentAnnotationOffset = readElementValues(classVisitor
/*  649 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
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
/*  661 */     if (runtimeInvisibleTypeAnnotationsOffset != 0) {
/*  662 */       int numAnnotations = readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
/*  663 */       int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
/*  664 */       while (numAnnotations-- > 0) {
/*      */         
/*  666 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/*  668 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  669 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  672 */         currentAnnotationOffset = readElementValues(classVisitor
/*  673 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
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
/*  685 */     while (attributes != null) {
/*      */       
/*  687 */       Attribute nextAttribute = attributes.nextAttribute;
/*  688 */       attributes.nextAttribute = null;
/*  689 */       classVisitor.visitAttribute(attributes);
/*  690 */       attributes = nextAttribute;
/*      */     } 
/*      */ 
/*      */     
/*  694 */     if (nestMembersOffset != 0) {
/*  695 */       int numberOfNestMembers = readUnsignedShort(nestMembersOffset);
/*  696 */       int currentNestMemberOffset = nestMembersOffset + 2;
/*  697 */       while (numberOfNestMembers-- > 0) {
/*  698 */         classVisitor.visitNestMember(readClass(currentNestMemberOffset, charBuffer));
/*  699 */         currentNestMemberOffset += 2;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  704 */     if (permittedSubclassesOffset != 0) {
/*  705 */       int numberOfPermittedSubclasses = readUnsignedShort(permittedSubclassesOffset);
/*  706 */       int currentPermittedSubclassesOffset = permittedSubclassesOffset + 2;
/*  707 */       while (numberOfPermittedSubclasses-- > 0) {
/*  708 */         classVisitor.visitPermittedSubclass(
/*  709 */             readClass(currentPermittedSubclassesOffset, charBuffer));
/*  710 */         currentPermittedSubclassesOffset += 2;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  715 */     if (innerClassesOffset != 0) {
/*  716 */       int numberOfClasses = readUnsignedShort(innerClassesOffset);
/*  717 */       int currentClassesOffset = innerClassesOffset + 2;
/*  718 */       while (numberOfClasses-- > 0) {
/*  719 */         classVisitor.visitInnerClass(
/*  720 */             readClass(currentClassesOffset, charBuffer), 
/*  721 */             readClass(currentClassesOffset + 2, charBuffer), 
/*  722 */             readUTF8(currentClassesOffset + 4, charBuffer), 
/*  723 */             readUnsignedShort(currentClassesOffset + 6));
/*  724 */         currentClassesOffset += 8;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  729 */     if (recordOffset != 0) {
/*  730 */       int recordComponentsCount = readUnsignedShort(recordOffset);
/*  731 */       recordOffset += 2;
/*  732 */       while (recordComponentsCount-- > 0) {
/*  733 */         recordOffset = readRecordComponent(classVisitor, context, recordOffset);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  738 */     int fieldsCount = readUnsignedShort(currentOffset);
/*  739 */     currentOffset += 2;
/*  740 */     while (fieldsCount-- > 0) {
/*  741 */       currentOffset = readField(classVisitor, context, currentOffset);
/*      */     }
/*  743 */     int methodsCount = readUnsignedShort(currentOffset);
/*  744 */     currentOffset += 2;
/*  745 */     while (methodsCount-- > 0) {
/*  746 */       currentOffset = readMethod(classVisitor, context, currentOffset);
/*      */     }
/*      */ 
/*      */     
/*  750 */     classVisitor.visitEnd();
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
/*      */   private void readModuleAttributes(ClassVisitor classVisitor, Context context, int moduleOffset, int modulePackagesOffset, String moduleMainClass) {
/*  775 */     char[] buffer = context.charBuffer;
/*      */ 
/*      */     
/*  778 */     int currentOffset = moduleOffset;
/*  779 */     String moduleName = readModule(currentOffset, buffer);
/*  780 */     int moduleFlags = readUnsignedShort(currentOffset + 2);
/*  781 */     String moduleVersion = readUTF8(currentOffset + 4, buffer);
/*  782 */     currentOffset += 6;
/*  783 */     ModuleVisitor moduleVisitor = classVisitor.visitModule(moduleName, moduleFlags, moduleVersion);
/*  784 */     if (moduleVisitor == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  789 */     if (moduleMainClass != null) {
/*  790 */       moduleVisitor.visitMainClass(moduleMainClass);
/*      */     }
/*      */ 
/*      */     
/*  794 */     if (modulePackagesOffset != 0) {
/*  795 */       int packageCount = readUnsignedShort(modulePackagesOffset);
/*  796 */       int currentPackageOffset = modulePackagesOffset + 2;
/*  797 */       while (packageCount-- > 0) {
/*  798 */         moduleVisitor.visitPackage(readPackage(currentPackageOffset, buffer));
/*  799 */         currentPackageOffset += 2;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  804 */     int requiresCount = readUnsignedShort(currentOffset);
/*  805 */     currentOffset += 2;
/*  806 */     while (requiresCount-- > 0) {
/*      */       
/*  808 */       String requires = readModule(currentOffset, buffer);
/*  809 */       int requiresFlags = readUnsignedShort(currentOffset + 2);
/*  810 */       String requiresVersion = readUTF8(currentOffset + 4, buffer);
/*  811 */       currentOffset += 6;
/*  812 */       moduleVisitor.visitRequire(requires, requiresFlags, requiresVersion);
/*      */     } 
/*      */ 
/*      */     
/*  816 */     int exportsCount = readUnsignedShort(currentOffset);
/*  817 */     currentOffset += 2;
/*  818 */     while (exportsCount-- > 0) {
/*      */ 
/*      */       
/*  821 */       String exports = readPackage(currentOffset, buffer);
/*  822 */       int exportsFlags = readUnsignedShort(currentOffset + 2);
/*  823 */       int exportsToCount = readUnsignedShort(currentOffset + 4);
/*  824 */       currentOffset += 6;
/*  825 */       String[] exportsTo = null;
/*  826 */       if (exportsToCount != 0) {
/*  827 */         exportsTo = new String[exportsToCount];
/*  828 */         for (int i = 0; i < exportsToCount; i++) {
/*  829 */           exportsTo[i] = readModule(currentOffset, buffer);
/*  830 */           currentOffset += 2;
/*      */         } 
/*      */       } 
/*  833 */       moduleVisitor.visitExport(exports, exportsFlags, exportsTo);
/*      */     } 
/*      */ 
/*      */     
/*  837 */     int opensCount = readUnsignedShort(currentOffset);
/*  838 */     currentOffset += 2;
/*  839 */     while (opensCount-- > 0) {
/*      */       
/*  841 */       String opens = readPackage(currentOffset, buffer);
/*  842 */       int opensFlags = readUnsignedShort(currentOffset + 2);
/*  843 */       int opensToCount = readUnsignedShort(currentOffset + 4);
/*  844 */       currentOffset += 6;
/*  845 */       String[] opensTo = null;
/*  846 */       if (opensToCount != 0) {
/*  847 */         opensTo = new String[opensToCount];
/*  848 */         for (int i = 0; i < opensToCount; i++) {
/*  849 */           opensTo[i] = readModule(currentOffset, buffer);
/*  850 */           currentOffset += 2;
/*      */         } 
/*      */       } 
/*  853 */       moduleVisitor.visitOpen(opens, opensFlags, opensTo);
/*      */     } 
/*      */ 
/*      */     
/*  857 */     int usesCount = readUnsignedShort(currentOffset);
/*  858 */     currentOffset += 2;
/*  859 */     while (usesCount-- > 0) {
/*  860 */       moduleVisitor.visitUse(readClass(currentOffset, buffer));
/*  861 */       currentOffset += 2;
/*      */     } 
/*      */ 
/*      */     
/*  865 */     int providesCount = readUnsignedShort(currentOffset);
/*  866 */     currentOffset += 2;
/*  867 */     while (providesCount-- > 0) {
/*      */       
/*  869 */       String provides = readClass(currentOffset, buffer);
/*  870 */       int providesWithCount = readUnsignedShort(currentOffset + 2);
/*  871 */       currentOffset += 4;
/*  872 */       String[] providesWith = new String[providesWithCount];
/*  873 */       for (int i = 0; i < providesWithCount; i++) {
/*  874 */         providesWith[i] = readClass(currentOffset, buffer);
/*  875 */         currentOffset += 2;
/*      */       } 
/*  877 */       moduleVisitor.visitProvide(provides, providesWith);
/*      */     } 
/*      */ 
/*      */     
/*  881 */     moduleVisitor.visitEnd();
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
/*      */   private int readRecordComponent(ClassVisitor classVisitor, Context context, int recordComponentOffset) {
/*  894 */     char[] charBuffer = context.charBuffer;
/*      */     
/*  896 */     int currentOffset = recordComponentOffset;
/*  897 */     String name = readUTF8(currentOffset, charBuffer);
/*  898 */     String descriptor = readUTF8(currentOffset + 2, charBuffer);
/*  899 */     currentOffset += 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  906 */     String signature = null;
/*      */     
/*  908 */     int runtimeVisibleAnnotationsOffset = 0;
/*      */     
/*  910 */     int runtimeInvisibleAnnotationsOffset = 0;
/*      */     
/*  912 */     int runtimeVisibleTypeAnnotationsOffset = 0;
/*      */     
/*  914 */     int runtimeInvisibleTypeAnnotationsOffset = 0;
/*      */ 
/*      */     
/*  917 */     Attribute attributes = null;
/*      */     
/*  919 */     int attributesCount = readUnsignedShort(currentOffset);
/*  920 */     currentOffset += 2;
/*  921 */     while (attributesCount-- > 0) {
/*      */       
/*  923 */       String attributeName = readUTF8(currentOffset, charBuffer);
/*  924 */       int attributeLength = readInt(currentOffset + 2);
/*  925 */       currentOffset += 6;
/*      */ 
/*      */       
/*  928 */       if ("Signature".equals(attributeName)) {
/*  929 */         signature = readUTF8(currentOffset, charBuffer);
/*  930 */       } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
/*  931 */         runtimeVisibleAnnotationsOffset = currentOffset;
/*  932 */       } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
/*  933 */         runtimeVisibleTypeAnnotationsOffset = currentOffset;
/*  934 */       } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
/*  935 */         runtimeInvisibleAnnotationsOffset = currentOffset;
/*  936 */       } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
/*  937 */         runtimeInvisibleTypeAnnotationsOffset = currentOffset;
/*      */       } else {
/*      */         
/*  940 */         Attribute attribute = readAttribute(context.attributePrototypes, attributeName, currentOffset, attributeLength, charBuffer, -1, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  948 */         attribute.nextAttribute = attributes;
/*  949 */         attributes = attribute;
/*      */       } 
/*  951 */       currentOffset += attributeLength;
/*      */     } 
/*      */ 
/*      */     
/*  955 */     RecordComponentVisitor recordComponentVisitor = classVisitor.visitRecordComponent(name, descriptor, signature);
/*  956 */     if (recordComponentVisitor == null) {
/*  957 */       return currentOffset;
/*      */     }
/*      */ 
/*      */     
/*  961 */     if (runtimeVisibleAnnotationsOffset != 0) {
/*  962 */       int numAnnotations = readUnsignedShort(runtimeVisibleAnnotationsOffset);
/*  963 */       int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
/*  964 */       while (numAnnotations-- > 0) {
/*      */         
/*  966 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  967 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  970 */         currentAnnotationOffset = readElementValues(recordComponentVisitor
/*  971 */             .visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  979 */     if (runtimeInvisibleAnnotationsOffset != 0) {
/*  980 */       int numAnnotations = readUnsignedShort(runtimeInvisibleAnnotationsOffset);
/*  981 */       int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
/*  982 */       while (numAnnotations-- > 0) {
/*      */         
/*  984 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  985 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  988 */         currentAnnotationOffset = readElementValues(recordComponentVisitor
/*  989 */             .visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  997 */     if (runtimeVisibleTypeAnnotationsOffset != 0) {
/*  998 */       int numAnnotations = readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
/*  999 */       int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
/* 1000 */       while (numAnnotations-- > 0) {
/*      */         
/* 1002 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/* 1004 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1005 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1008 */         currentAnnotationOffset = readElementValues(recordComponentVisitor
/* 1009 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
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
/* 1021 */     if (runtimeInvisibleTypeAnnotationsOffset != 0) {
/* 1022 */       int numAnnotations = readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
/* 1023 */       int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
/* 1024 */       while (numAnnotations-- > 0) {
/*      */         
/* 1026 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/* 1028 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1029 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1032 */         currentAnnotationOffset = readElementValues(recordComponentVisitor
/* 1033 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
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
/* 1045 */     while (attributes != null) {
/*      */       
/* 1047 */       Attribute nextAttribute = attributes.nextAttribute;
/* 1048 */       attributes.nextAttribute = null;
/* 1049 */       recordComponentVisitor.visitAttribute(attributes);
/* 1050 */       attributes = nextAttribute;
/*      */     } 
/*      */ 
/*      */     
/* 1054 */     recordComponentVisitor.visitEnd();
/* 1055 */     return currentOffset;
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
/*      */   private int readField(ClassVisitor classVisitor, Context context, int fieldInfoOffset) {
/* 1068 */     char[] charBuffer = context.charBuffer;
/*      */ 
/*      */     
/* 1071 */     int currentOffset = fieldInfoOffset;
/* 1072 */     int accessFlags = readUnsignedShort(currentOffset);
/* 1073 */     String name = readUTF8(currentOffset + 2, charBuffer);
/* 1074 */     String descriptor = readUTF8(currentOffset + 4, charBuffer);
/* 1075 */     currentOffset += 6;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1080 */     Object constantValue = null;
/*      */     
/* 1082 */     String signature = null;
/*      */     
/* 1084 */     int runtimeVisibleAnnotationsOffset = 0;
/*      */     
/* 1086 */     int runtimeInvisibleAnnotationsOffset = 0;
/*      */     
/* 1088 */     int runtimeVisibleTypeAnnotationsOffset = 0;
/*      */     
/* 1090 */     int runtimeInvisibleTypeAnnotationsOffset = 0;
/*      */ 
/*      */     
/* 1093 */     Attribute attributes = null;
/*      */     
/* 1095 */     int attributesCount = readUnsignedShort(currentOffset);
/* 1096 */     currentOffset += 2;
/* 1097 */     while (attributesCount-- > 0) {
/*      */       
/* 1099 */       String attributeName = readUTF8(currentOffset, charBuffer);
/* 1100 */       int attributeLength = readInt(currentOffset + 2);
/* 1101 */       currentOffset += 6;
/*      */ 
/*      */       
/* 1104 */       if ("ConstantValue".equals(attributeName)) {
/* 1105 */         int constantvalueIndex = readUnsignedShort(currentOffset);
/* 1106 */         constantValue = (constantvalueIndex == 0) ? null : readConst(constantvalueIndex, charBuffer);
/* 1107 */       } else if ("Signature".equals(attributeName)) {
/* 1108 */         signature = readUTF8(currentOffset, charBuffer);
/* 1109 */       } else if ("Deprecated".equals(attributeName)) {
/* 1110 */         accessFlags |= 0x20000;
/* 1111 */       } else if ("Synthetic".equals(attributeName)) {
/* 1112 */         accessFlags |= 0x1000;
/* 1113 */       } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
/* 1114 */         runtimeVisibleAnnotationsOffset = currentOffset;
/* 1115 */       } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
/* 1116 */         runtimeVisibleTypeAnnotationsOffset = currentOffset;
/* 1117 */       } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
/* 1118 */         runtimeInvisibleAnnotationsOffset = currentOffset;
/* 1119 */       } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
/* 1120 */         runtimeInvisibleTypeAnnotationsOffset = currentOffset;
/*      */       } else {
/*      */         
/* 1123 */         Attribute attribute = readAttribute(context.attributePrototypes, attributeName, currentOffset, attributeLength, charBuffer, -1, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1131 */         attribute.nextAttribute = attributes;
/* 1132 */         attributes = attribute;
/*      */       } 
/* 1134 */       currentOffset += attributeLength;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1139 */     FieldVisitor fieldVisitor = classVisitor.visitField(accessFlags, name, descriptor, signature, constantValue);
/* 1140 */     if (fieldVisitor == null) {
/* 1141 */       return currentOffset;
/*      */     }
/*      */ 
/*      */     
/* 1145 */     if (runtimeVisibleAnnotationsOffset != 0) {
/* 1146 */       int numAnnotations = readUnsignedShort(runtimeVisibleAnnotationsOffset);
/* 1147 */       int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
/* 1148 */       while (numAnnotations-- > 0) {
/*      */         
/* 1150 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1151 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1154 */         currentAnnotationOffset = readElementValues(fieldVisitor
/* 1155 */             .visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1163 */     if (runtimeInvisibleAnnotationsOffset != 0) {
/* 1164 */       int numAnnotations = readUnsignedShort(runtimeInvisibleAnnotationsOffset);
/* 1165 */       int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
/* 1166 */       while (numAnnotations-- > 0) {
/*      */         
/* 1168 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1169 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1172 */         currentAnnotationOffset = readElementValues(fieldVisitor
/* 1173 */             .visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1181 */     if (runtimeVisibleTypeAnnotationsOffset != 0) {
/* 1182 */       int numAnnotations = readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
/* 1183 */       int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
/* 1184 */       while (numAnnotations-- > 0) {
/*      */         
/* 1186 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/* 1188 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1189 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1192 */         currentAnnotationOffset = readElementValues(fieldVisitor
/* 1193 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
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
/* 1205 */     if (runtimeInvisibleTypeAnnotationsOffset != 0) {
/* 1206 */       int numAnnotations = readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
/* 1207 */       int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
/* 1208 */       while (numAnnotations-- > 0) {
/*      */         
/* 1210 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/* 1212 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1213 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1216 */         currentAnnotationOffset = readElementValues(fieldVisitor
/* 1217 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
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
/* 1229 */     while (attributes != null) {
/*      */       
/* 1231 */       Attribute nextAttribute = attributes.nextAttribute;
/* 1232 */       attributes.nextAttribute = null;
/* 1233 */       fieldVisitor.visitAttribute(attributes);
/* 1234 */       attributes = nextAttribute;
/*      */     } 
/*      */ 
/*      */     
/* 1238 */     fieldVisitor.visitEnd();
/* 1239 */     return currentOffset;
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
/*      */   private int readMethod(ClassVisitor classVisitor, Context context, int methodInfoOffset) {
/* 1252 */     char[] charBuffer = context.charBuffer;
/*      */ 
/*      */     
/* 1255 */     int currentOffset = methodInfoOffset;
/* 1256 */     context.currentMethodAccessFlags = readUnsignedShort(currentOffset);
/* 1257 */     context.currentMethodName = readUTF8(currentOffset + 2, charBuffer);
/* 1258 */     context.currentMethodDescriptor = readUTF8(currentOffset + 4, charBuffer);
/* 1259 */     currentOffset += 6;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1264 */     int codeOffset = 0;
/*      */     
/* 1266 */     int exceptionsOffset = 0;
/*      */     
/* 1268 */     String[] exceptions = null;
/*      */     
/* 1270 */     boolean synthetic = false;
/*      */     
/* 1272 */     int signatureIndex = 0;
/*      */     
/* 1274 */     int runtimeVisibleAnnotationsOffset = 0;
/*      */     
/* 1276 */     int runtimeInvisibleAnnotationsOffset = 0;
/*      */     
/* 1278 */     int runtimeVisibleParameterAnnotationsOffset = 0;
/*      */     
/* 1280 */     int runtimeInvisibleParameterAnnotationsOffset = 0;
/*      */     
/* 1282 */     int runtimeVisibleTypeAnnotationsOffset = 0;
/*      */     
/* 1284 */     int runtimeInvisibleTypeAnnotationsOffset = 0;
/*      */     
/* 1286 */     int annotationDefaultOffset = 0;
/*      */     
/* 1288 */     int methodParametersOffset = 0;
/*      */ 
/*      */     
/* 1291 */     Attribute attributes = null;
/*      */     
/* 1293 */     int attributesCount = readUnsignedShort(currentOffset);
/* 1294 */     currentOffset += 2;
/* 1295 */     while (attributesCount-- > 0) {
/*      */       
/* 1297 */       String attributeName = readUTF8(currentOffset, charBuffer);
/* 1298 */       int attributeLength = readInt(currentOffset + 2);
/* 1299 */       currentOffset += 6;
/*      */ 
/*      */       
/* 1302 */       if ("Code".equals(attributeName)) {
/* 1303 */         if ((context.parsingOptions & 0x1) == 0) {
/* 1304 */           codeOffset = currentOffset;
/*      */         }
/* 1306 */       } else if ("Exceptions".equals(attributeName)) {
/* 1307 */         exceptionsOffset = currentOffset;
/* 1308 */         exceptions = new String[readUnsignedShort(exceptionsOffset)];
/* 1309 */         int currentExceptionOffset = exceptionsOffset + 2;
/* 1310 */         for (int i = 0; i < exceptions.length; i++) {
/* 1311 */           exceptions[i] = readClass(currentExceptionOffset, charBuffer);
/* 1312 */           currentExceptionOffset += 2;
/*      */         } 
/* 1314 */       } else if ("Signature".equals(attributeName)) {
/* 1315 */         signatureIndex = readUnsignedShort(currentOffset);
/* 1316 */       } else if ("Deprecated".equals(attributeName)) {
/* 1317 */         context.currentMethodAccessFlags |= 0x20000;
/* 1318 */       } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
/* 1319 */         runtimeVisibleAnnotationsOffset = currentOffset;
/* 1320 */       } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
/* 1321 */         runtimeVisibleTypeAnnotationsOffset = currentOffset;
/* 1322 */       } else if ("AnnotationDefault".equals(attributeName)) {
/* 1323 */         annotationDefaultOffset = currentOffset;
/* 1324 */       } else if ("Synthetic".equals(attributeName)) {
/* 1325 */         synthetic = true;
/* 1326 */         context.currentMethodAccessFlags |= 0x1000;
/* 1327 */       } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
/* 1328 */         runtimeInvisibleAnnotationsOffset = currentOffset;
/* 1329 */       } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
/* 1330 */         runtimeInvisibleTypeAnnotationsOffset = currentOffset;
/* 1331 */       } else if ("RuntimeVisibleParameterAnnotations".equals(attributeName)) {
/* 1332 */         runtimeVisibleParameterAnnotationsOffset = currentOffset;
/* 1333 */       } else if ("RuntimeInvisibleParameterAnnotations".equals(attributeName)) {
/* 1334 */         runtimeInvisibleParameterAnnotationsOffset = currentOffset;
/* 1335 */       } else if ("MethodParameters".equals(attributeName)) {
/* 1336 */         methodParametersOffset = currentOffset;
/*      */       } else {
/*      */         
/* 1339 */         Attribute attribute = readAttribute(context.attributePrototypes, attributeName, currentOffset, attributeLength, charBuffer, -1, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1347 */         attribute.nextAttribute = attributes;
/* 1348 */         attributes = attribute;
/*      */       } 
/* 1350 */       currentOffset += attributeLength;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1355 */     MethodVisitor methodVisitor = classVisitor.visitMethod(context.currentMethodAccessFlags, context.currentMethodName, context.currentMethodDescriptor, (signatureIndex == 0) ? null : 
/*      */ 
/*      */ 
/*      */         
/* 1359 */         readUtf(signatureIndex, charBuffer), exceptions);
/*      */     
/* 1361 */     if (methodVisitor == null) {
/* 1362 */       return currentOffset;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1369 */     if (methodVisitor instanceof MethodWriter) {
/* 1370 */       MethodWriter methodWriter = (MethodWriter)methodVisitor;
/* 1371 */       if (methodWriter.canCopyMethodAttributes(this, synthetic, ((context.currentMethodAccessFlags & 0x20000) != 0), 
/*      */ 
/*      */ 
/*      */           
/* 1375 */           readUnsignedShort(methodInfoOffset + 4), signatureIndex, exceptionsOffset)) {
/*      */ 
/*      */         
/* 1378 */         methodWriter.setMethodAttributesSource(methodInfoOffset, currentOffset - methodInfoOffset);
/* 1379 */         return currentOffset;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1384 */     if (methodParametersOffset != 0 && (context.parsingOptions & 0x2) == 0) {
/* 1385 */       int parametersCount = readByte(methodParametersOffset);
/* 1386 */       int currentParameterOffset = methodParametersOffset + 1;
/* 1387 */       while (parametersCount-- > 0) {
/*      */         
/* 1389 */         methodVisitor.visitParameter(
/* 1390 */             readUTF8(currentParameterOffset, charBuffer), 
/* 1391 */             readUnsignedShort(currentParameterOffset + 2));
/* 1392 */         currentParameterOffset += 4;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1397 */     if (annotationDefaultOffset != 0) {
/* 1398 */       AnnotationVisitor annotationVisitor = methodVisitor.visitAnnotationDefault();
/* 1399 */       readElementValue(annotationVisitor, annotationDefaultOffset, null, charBuffer);
/* 1400 */       if (annotationVisitor != null) {
/* 1401 */         annotationVisitor.visitEnd();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1406 */     if (runtimeVisibleAnnotationsOffset != 0) {
/* 1407 */       int numAnnotations = readUnsignedShort(runtimeVisibleAnnotationsOffset);
/* 1408 */       int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
/* 1409 */       while (numAnnotations-- > 0) {
/*      */         
/* 1411 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1412 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1415 */         currentAnnotationOffset = readElementValues(methodVisitor
/* 1416 */             .visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1424 */     if (runtimeInvisibleAnnotationsOffset != 0) {
/* 1425 */       int numAnnotations = readUnsignedShort(runtimeInvisibleAnnotationsOffset);
/* 1426 */       int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
/* 1427 */       while (numAnnotations-- > 0) {
/*      */         
/* 1429 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1430 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1433 */         currentAnnotationOffset = readElementValues(methodVisitor
/* 1434 */             .visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1442 */     if (runtimeVisibleTypeAnnotationsOffset != 0) {
/* 1443 */       int numAnnotations = readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
/* 1444 */       int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
/* 1445 */       while (numAnnotations-- > 0) {
/*      */         
/* 1447 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/* 1449 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1450 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1453 */         currentAnnotationOffset = readElementValues(methodVisitor
/* 1454 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
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
/* 1466 */     if (runtimeInvisibleTypeAnnotationsOffset != 0) {
/* 1467 */       int numAnnotations = readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
/* 1468 */       int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
/* 1469 */       while (numAnnotations-- > 0) {
/*      */         
/* 1471 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/* 1473 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1474 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1477 */         currentAnnotationOffset = readElementValues(methodVisitor
/* 1478 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
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
/* 1490 */     if (runtimeVisibleParameterAnnotationsOffset != 0) {
/* 1491 */       readParameterAnnotations(methodVisitor, context, runtimeVisibleParameterAnnotationsOffset, true);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1496 */     if (runtimeInvisibleParameterAnnotationsOffset != 0) {
/* 1497 */       readParameterAnnotations(methodVisitor, context, runtimeInvisibleParameterAnnotationsOffset, false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1505 */     while (attributes != null) {
/*      */       
/* 1507 */       Attribute nextAttribute = attributes.nextAttribute;
/* 1508 */       attributes.nextAttribute = null;
/* 1509 */       methodVisitor.visitAttribute(attributes);
/* 1510 */       attributes = nextAttribute;
/*      */     } 
/*      */ 
/*      */     
/* 1514 */     if (codeOffset != 0) {
/* 1515 */       methodVisitor.visitCode();
/* 1516 */       readCode(methodVisitor, context, codeOffset);
/*      */     } 
/*      */ 
/*      */     
/* 1520 */     methodVisitor.visitEnd();
/* 1521 */     return currentOffset;
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
/*      */   private void readCode(MethodVisitor methodVisitor, Context context, int codeOffset) {
/* 1538 */     int currentOffset = codeOffset;
/*      */ 
/*      */     
/* 1541 */     byte[] classBuffer = this.classFileBuffer;
/* 1542 */     char[] charBuffer = context.charBuffer;
/* 1543 */     int maxStack = readUnsignedShort(currentOffset);
/* 1544 */     int maxLocals = readUnsignedShort(currentOffset + 2);
/* 1545 */     int codeLength = readInt(currentOffset + 4);
/* 1546 */     currentOffset += 8;
/* 1547 */     if (codeLength > this.classFileBuffer.length - currentOffset) {
/* 1548 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/*      */     
/* 1552 */     int bytecodeStartOffset = currentOffset;
/* 1553 */     int bytecodeEndOffset = currentOffset + codeLength;
/* 1554 */     Label[] labels = context.currentMethodLabels = new Label[codeLength + 1];
/* 1555 */     while (currentOffset < bytecodeEndOffset) {
/* 1556 */       int numTableEntries, numSwitchCases, bytecodeOffset = currentOffset - bytecodeStartOffset;
/* 1557 */       int opcode = classBuffer[currentOffset] & 0xFF;
/* 1558 */       switch (opcode) {
/*      */         case 0:
/*      */         case 1:
/*      */         case 2:
/*      */         case 3:
/*      */         case 4:
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 8:
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*      */         case 12:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 26:
/*      */         case 27:
/*      */         case 28:
/*      */         case 29:
/*      */         case 30:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 37:
/*      */         case 38:
/*      */         case 39:
/*      */         case 40:
/*      */         case 41:
/*      */         case 42:
/*      */         case 43:
/*      */         case 44:
/*      */         case 45:
/*      */         case 46:
/*      */         case 47:
/*      */         case 48:
/*      */         case 49:
/*      */         case 50:
/*      */         case 51:
/*      */         case 52:
/*      */         case 53:
/*      */         case 59:
/*      */         case 60:
/*      */         case 61:
/*      */         case 62:
/*      */         case 63:
/*      */         case 64:
/*      */         case 65:
/*      */         case 66:
/*      */         case 67:
/*      */         case 68:
/*      */         case 69:
/*      */         case 70:
/*      */         case 71:
/*      */         case 72:
/*      */         case 73:
/*      */         case 74:
/*      */         case 75:
/*      */         case 76:
/*      */         case 77:
/*      */         case 78:
/*      */         case 79:
/*      */         case 80:
/*      */         case 81:
/*      */         case 82:
/*      */         case 83:
/*      */         case 84:
/*      */         case 85:
/*      */         case 86:
/*      */         case 87:
/*      */         case 88:
/*      */         case 89:
/*      */         case 90:
/*      */         case 91:
/*      */         case 92:
/*      */         case 93:
/*      */         case 94:
/*      */         case 95:
/*      */         case 96:
/*      */         case 97:
/*      */         case 98:
/*      */         case 99:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/*      */         case 104:
/*      */         case 105:
/*      */         case 106:
/*      */         case 107:
/*      */         case 108:
/*      */         case 109:
/*      */         case 110:
/*      */         case 111:
/*      */         case 112:
/*      */         case 113:
/*      */         case 114:
/*      */         case 115:
/*      */         case 116:
/*      */         case 117:
/*      */         case 118:
/*      */         case 119:
/*      */         case 120:
/*      */         case 121:
/*      */         case 122:
/*      */         case 123:
/*      */         case 124:
/*      */         case 125:
/*      */         case 126:
/*      */         case 127:
/*      */         case 128:
/*      */         case 129:
/*      */         case 130:
/*      */         case 131:
/*      */         case 133:
/*      */         case 134:
/*      */         case 135:
/*      */         case 136:
/*      */         case 137:
/*      */         case 138:
/*      */         case 139:
/*      */         case 140:
/*      */         case 141:
/*      */         case 142:
/*      */         case 143:
/*      */         case 144:
/*      */         case 145:
/*      */         case 146:
/*      */         case 147:
/*      */         case 148:
/*      */         case 149:
/*      */         case 150:
/*      */         case 151:
/*      */         case 152:
/*      */         case 172:
/*      */         case 173:
/*      */         case 174:
/*      */         case 175:
/*      */         case 176:
/*      */         case 177:
/*      */         case 190:
/*      */         case 191:
/*      */         case 194:
/*      */         case 195:
/* 1706 */           currentOffset++;
/*      */           continue;
/*      */         case 153:
/*      */         case 154:
/*      */         case 155:
/*      */         case 156:
/*      */         case 157:
/*      */         case 158:
/*      */         case 159:
/*      */         case 160:
/*      */         case 161:
/*      */         case 162:
/*      */         case 163:
/*      */         case 164:
/*      */         case 165:
/*      */         case 166:
/*      */         case 167:
/*      */         case 168:
/*      */         case 198:
/*      */         case 199:
/* 1726 */           createLabel(bytecodeOffset + readShort(currentOffset + 1), labels);
/* 1727 */           currentOffset += 3;
/*      */           continue;
/*      */         case 202:
/*      */         case 203:
/*      */         case 204:
/*      */         case 205:
/*      */         case 206:
/*      */         case 207:
/*      */         case 208:
/*      */         case 209:
/*      */         case 210:
/*      */         case 211:
/*      */         case 212:
/*      */         case 213:
/*      */         case 214:
/*      */         case 215:
/*      */         case 216:
/*      */         case 217:
/*      */         case 218:
/*      */         case 219:
/* 1747 */           createLabel(bytecodeOffset + readUnsignedShort(currentOffset + 1), labels);
/* 1748 */           currentOffset += 3;
/*      */           continue;
/*      */         case 200:
/*      */         case 201:
/*      */         case 220:
/* 1753 */           createLabel(bytecodeOffset + readInt(currentOffset + 1), labels);
/* 1754 */           currentOffset += 5;
/*      */           continue;
/*      */         case 196:
/* 1757 */           switch (classBuffer[currentOffset + 1] & 0xFF) {
/*      */             case 21:
/*      */             case 22:
/*      */             case 23:
/*      */             case 24:
/*      */             case 25:
/*      */             case 54:
/*      */             case 55:
/*      */             case 56:
/*      */             case 57:
/*      */             case 58:
/*      */             case 169:
/* 1769 */               currentOffset += 4;
/*      */               continue;
/*      */             case 132:
/* 1772 */               currentOffset += 6;
/*      */               continue;
/*      */           } 
/* 1775 */           throw new IllegalArgumentException();
/*      */ 
/*      */ 
/*      */         
/*      */         case 170:
/* 1780 */           currentOffset += 4 - (bytecodeOffset & 0x3);
/*      */           
/* 1782 */           createLabel(bytecodeOffset + readInt(currentOffset), labels);
/* 1783 */           numTableEntries = readInt(currentOffset + 8) - readInt(currentOffset + 4) + 1;
/* 1784 */           currentOffset += 12;
/*      */           
/* 1786 */           while (numTableEntries-- > 0) {
/* 1787 */             createLabel(bytecodeOffset + readInt(currentOffset), labels);
/* 1788 */             currentOffset += 4;
/*      */           } 
/*      */           continue;
/*      */         
/*      */         case 171:
/* 1793 */           currentOffset += 4 - (bytecodeOffset & 0x3);
/*      */           
/* 1795 */           createLabel(bytecodeOffset + readInt(currentOffset), labels);
/* 1796 */           numSwitchCases = readInt(currentOffset + 4);
/* 1797 */           currentOffset += 8;
/*      */           
/* 1799 */           while (numSwitchCases-- > 0) {
/* 1800 */             createLabel(bytecodeOffset + readInt(currentOffset + 4), labels);
/* 1801 */             currentOffset += 8;
/*      */           } 
/*      */           continue;
/*      */         case 16:
/*      */         case 18:
/*      */         case 21:
/*      */         case 22:
/*      */         case 23:
/*      */         case 24:
/*      */         case 25:
/*      */         case 54:
/*      */         case 55:
/*      */         case 56:
/*      */         case 57:
/*      */         case 58:
/*      */         case 169:
/*      */         case 188:
/* 1818 */           currentOffset += 2;
/*      */           continue;
/*      */         case 17:
/*      */         case 19:
/*      */         case 20:
/*      */         case 132:
/*      */         case 178:
/*      */         case 179:
/*      */         case 180:
/*      */         case 181:
/*      */         case 182:
/*      */         case 183:
/*      */         case 184:
/*      */         case 187:
/*      */         case 189:
/*      */         case 192:
/*      */         case 193:
/* 1835 */           currentOffset += 3;
/*      */           continue;
/*      */         case 185:
/*      */         case 186:
/* 1839 */           currentOffset += 5;
/*      */           continue;
/*      */         case 197:
/* 1842 */           currentOffset += 4;
/*      */           continue;
/*      */       } 
/* 1845 */       throw new IllegalArgumentException();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1851 */     int exceptionTableLength = readUnsignedShort(currentOffset);
/* 1852 */     currentOffset += 2;
/* 1853 */     while (exceptionTableLength-- > 0) {
/* 1854 */       Label start = createLabel(readUnsignedShort(currentOffset), labels);
/* 1855 */       Label end = createLabel(readUnsignedShort(currentOffset + 2), labels);
/* 1856 */       Label handler = createLabel(readUnsignedShort(currentOffset + 4), labels);
/* 1857 */       String catchType = readUTF8(this.cpInfoOffsets[readUnsignedShort(currentOffset + 6)], charBuffer);
/* 1858 */       currentOffset += 8;
/* 1859 */       methodVisitor.visitTryCatchBlock(start, end, handler, catchType);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1868 */     int stackMapFrameOffset = 0;
/*      */     
/* 1870 */     int stackMapTableEndOffset = 0;
/*      */     
/* 1872 */     boolean compressedFrames = true;
/*      */     
/* 1874 */     int localVariableTableOffset = 0;
/*      */     
/* 1876 */     int localVariableTypeTableOffset = 0;
/*      */ 
/*      */     
/* 1879 */     int[] visibleTypeAnnotationOffsets = null;
/*      */ 
/*      */     
/* 1882 */     int[] invisibleTypeAnnotationOffsets = null;
/*      */ 
/*      */     
/* 1885 */     Attribute attributes = null;
/*      */     
/* 1887 */     int attributesCount = readUnsignedShort(currentOffset);
/* 1888 */     currentOffset += 2;
/* 1889 */     while (attributesCount-- > 0) {
/*      */       
/* 1891 */       String attributeName = readUTF8(currentOffset, charBuffer);
/* 1892 */       int attributeLength = readInt(currentOffset + 2);
/* 1893 */       currentOffset += 6;
/* 1894 */       if ("LocalVariableTable".equals(attributeName)) {
/* 1895 */         if ((context.parsingOptions & 0x2) == 0) {
/* 1896 */           localVariableTableOffset = currentOffset;
/*      */           
/* 1898 */           int currentLocalVariableTableOffset = currentOffset;
/* 1899 */           int localVariableTableLength = readUnsignedShort(currentLocalVariableTableOffset);
/* 1900 */           currentLocalVariableTableOffset += 2;
/* 1901 */           while (localVariableTableLength-- > 0) {
/* 1902 */             int startPc = readUnsignedShort(currentLocalVariableTableOffset);
/* 1903 */             createDebugLabel(startPc, labels);
/* 1904 */             int length = readUnsignedShort(currentLocalVariableTableOffset + 2);
/* 1905 */             createDebugLabel(startPc + length, labels);
/*      */             
/* 1907 */             currentLocalVariableTableOffset += 10;
/*      */           } 
/*      */         } 
/* 1910 */       } else if ("LocalVariableTypeTable".equals(attributeName)) {
/* 1911 */         localVariableTypeTableOffset = currentOffset;
/*      */       
/*      */       }
/* 1914 */       else if ("LineNumberTable".equals(attributeName)) {
/* 1915 */         if ((context.parsingOptions & 0x2) == 0) {
/*      */           
/* 1917 */           int currentLineNumberTableOffset = currentOffset;
/* 1918 */           int lineNumberTableLength = readUnsignedShort(currentLineNumberTableOffset);
/* 1919 */           currentLineNumberTableOffset += 2;
/* 1920 */           while (lineNumberTableLength-- > 0) {
/* 1921 */             int startPc = readUnsignedShort(currentLineNumberTableOffset);
/* 1922 */             int lineNumber = readUnsignedShort(currentLineNumberTableOffset + 2);
/* 1923 */             currentLineNumberTableOffset += 4;
/* 1924 */             createDebugLabel(startPc, labels);
/* 1925 */             labels[startPc].addLineNumber(lineNumber);
/*      */           } 
/*      */         } 
/* 1928 */       } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
/*      */         
/* 1930 */         visibleTypeAnnotationOffsets = readTypeAnnotations(methodVisitor, context, currentOffset, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1937 */       else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
/*      */         
/* 1939 */         invisibleTypeAnnotationOffsets = readTypeAnnotations(methodVisitor, context, currentOffset, false);
/*      */       }
/* 1941 */       else if ("StackMapTable".equals(attributeName)) {
/* 1942 */         if ((context.parsingOptions & 0x4) == 0) {
/* 1943 */           stackMapFrameOffset = currentOffset + 2;
/* 1944 */           stackMapTableEndOffset = currentOffset + attributeLength;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1955 */       else if ("StackMap".equals(attributeName)) {
/* 1956 */         if ((context.parsingOptions & 0x4) == 0) {
/* 1957 */           stackMapFrameOffset = currentOffset + 2;
/* 1958 */           stackMapTableEndOffset = currentOffset + attributeLength;
/* 1959 */           compressedFrames = false;
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1967 */         Attribute attribute = readAttribute(context.attributePrototypes, attributeName, currentOffset, attributeLength, charBuffer, codeOffset, labels);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1975 */         attribute.nextAttribute = attributes;
/* 1976 */         attributes = attribute;
/*      */       } 
/* 1978 */       currentOffset += attributeLength;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1983 */     boolean expandFrames = ((context.parsingOptions & 0x8) != 0);
/* 1984 */     if (stackMapFrameOffset != 0) {
/*      */ 
/*      */ 
/*      */       
/* 1988 */       context.currentFrameOffset = -1;
/* 1989 */       context.currentFrameType = 0;
/* 1990 */       context.currentFrameLocalCount = 0;
/* 1991 */       context.currentFrameLocalCountDelta = 0;
/* 1992 */       context.currentFrameLocalTypes = new Object[maxLocals];
/* 1993 */       context.currentFrameStackCount = 0;
/* 1994 */       context.currentFrameStackTypes = new Object[maxStack];
/* 1995 */       if (expandFrames) {
/* 1996 */         computeImplicitFrame(context);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2005 */       for (int offset = stackMapFrameOffset; offset < stackMapTableEndOffset - 2; offset++) {
/* 2006 */         if (classBuffer[offset] == 8) {
/* 2007 */           int potentialBytecodeOffset = readUnsignedShort(offset + 1);
/* 2008 */           if (potentialBytecodeOffset >= 0 && potentialBytecodeOffset < codeLength && (classBuffer[bytecodeStartOffset + potentialBytecodeOffset] & 0xFF) == 187)
/*      */           {
/*      */ 
/*      */             
/* 2012 */             createLabel(potentialBytecodeOffset, labels);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 2017 */     if (expandFrames && (context.parsingOptions & 0x100) != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2024 */       methodVisitor.visitFrame(-1, maxLocals, null, 0, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2032 */     int currentVisibleTypeAnnotationIndex = 0;
/*      */ 
/*      */     
/* 2035 */     int currentVisibleTypeAnnotationBytecodeOffset = getTypeAnnotationBytecodeOffset(visibleTypeAnnotationOffsets, 0);
/*      */ 
/*      */     
/* 2038 */     int currentInvisibleTypeAnnotationIndex = 0;
/*      */ 
/*      */     
/* 2041 */     int currentInvisibleTypeAnnotationBytecodeOffset = getTypeAnnotationBytecodeOffset(invisibleTypeAnnotationOffsets, 0);
/*      */ 
/*      */     
/* 2044 */     boolean insertFrame = false;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2049 */     int wideJumpOpcodeDelta = ((context.parsingOptions & 0x100) == 0) ? 33 : 0;
/*      */ 
/*      */     
/* 2052 */     currentOffset = bytecodeStartOffset;
/* 2053 */     while (currentOffset < bytecodeEndOffset) {
/* 2054 */       Label target, defaultLabel; int cpInfoOffset, low, numPairs, nameAndTypeCpInfoOffset, high, keys[]; String owner, name; Label[] table, values; String str1, descriptor; int i; String str2; int bootstrapMethodOffset; Handle handle; Object[] bootstrapMethodArguments; int j, currentBytecodeOffset = currentOffset - bytecodeStartOffset;
/*      */ 
/*      */       
/* 2057 */       Label currentLabel = labels[currentBytecodeOffset];
/* 2058 */       if (currentLabel != null) {
/* 2059 */         currentLabel.accept(methodVisitor, ((context.parsingOptions & 0x2) == 0));
/*      */       }
/*      */ 
/*      */       
/* 2063 */       while (stackMapFrameOffset != 0 && (context.currentFrameOffset == currentBytecodeOffset || context.currentFrameOffset == -1)) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2068 */         if (context.currentFrameOffset != -1) {
/* 2069 */           if (!compressedFrames || expandFrames) {
/* 2070 */             methodVisitor.visitFrame(-1, context.currentFrameLocalCount, context.currentFrameLocalTypes, context.currentFrameStackCount, context.currentFrameStackTypes);
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/* 2077 */             methodVisitor.visitFrame(context.currentFrameType, context.currentFrameLocalCountDelta, context.currentFrameLocalTypes, context.currentFrameStackCount, context.currentFrameStackTypes);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2086 */           insertFrame = false;
/*      */         } 
/* 2088 */         if (stackMapFrameOffset < stackMapTableEndOffset) {
/*      */           
/* 2090 */           stackMapFrameOffset = readStackMapFrame(stackMapFrameOffset, compressedFrames, expandFrames, context); continue;
/*      */         } 
/* 2092 */         stackMapFrameOffset = 0;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2098 */       if (insertFrame) {
/* 2099 */         if ((context.parsingOptions & 0x8) != 0) {
/* 2100 */           methodVisitor.visitFrame(256, 0, null, 0, null);
/*      */         }
/* 2102 */         insertFrame = false;
/*      */       } 
/*      */ 
/*      */       
/* 2106 */       int opcode = classBuffer[currentOffset] & 0xFF;
/* 2107 */       switch (opcode) {
/*      */         case 0:
/*      */         case 1:
/*      */         case 2:
/*      */         case 3:
/*      */         case 4:
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 8:
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*      */         case 12:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 46:
/*      */         case 47:
/*      */         case 48:
/*      */         case 49:
/*      */         case 50:
/*      */         case 51:
/*      */         case 52:
/*      */         case 53:
/*      */         case 79:
/*      */         case 80:
/*      */         case 81:
/*      */         case 82:
/*      */         case 83:
/*      */         case 84:
/*      */         case 85:
/*      */         case 86:
/*      */         case 87:
/*      */         case 88:
/*      */         case 89:
/*      */         case 90:
/*      */         case 91:
/*      */         case 92:
/*      */         case 93:
/*      */         case 94:
/*      */         case 95:
/*      */         case 96:
/*      */         case 97:
/*      */         case 98:
/*      */         case 99:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/*      */         case 104:
/*      */         case 105:
/*      */         case 106:
/*      */         case 107:
/*      */         case 108:
/*      */         case 109:
/*      */         case 110:
/*      */         case 111:
/*      */         case 112:
/*      */         case 113:
/*      */         case 114:
/*      */         case 115:
/*      */         case 116:
/*      */         case 117:
/*      */         case 118:
/*      */         case 119:
/*      */         case 120:
/*      */         case 121:
/*      */         case 122:
/*      */         case 123:
/*      */         case 124:
/*      */         case 125:
/*      */         case 126:
/*      */         case 127:
/*      */         case 128:
/*      */         case 129:
/*      */         case 130:
/*      */         case 131:
/*      */         case 133:
/*      */         case 134:
/*      */         case 135:
/*      */         case 136:
/*      */         case 137:
/*      */         case 138:
/*      */         case 139:
/*      */         case 140:
/*      */         case 141:
/*      */         case 142:
/*      */         case 143:
/*      */         case 144:
/*      */         case 145:
/*      */         case 146:
/*      */         case 147:
/*      */         case 148:
/*      */         case 149:
/*      */         case 150:
/*      */         case 151:
/*      */         case 152:
/*      */         case 172:
/*      */         case 173:
/*      */         case 174:
/*      */         case 175:
/*      */         case 176:
/*      */         case 177:
/*      */         case 190:
/*      */         case 191:
/*      */         case 194:
/*      */         case 195:
/* 2215 */           methodVisitor.visitInsn(opcode);
/* 2216 */           currentOffset++;
/*      */           break;
/*      */         case 26:
/*      */         case 27:
/*      */         case 28:
/*      */         case 29:
/*      */         case 30:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 37:
/*      */         case 38:
/*      */         case 39:
/*      */         case 40:
/*      */         case 41:
/*      */         case 42:
/*      */         case 43:
/*      */         case 44:
/*      */         case 45:
/* 2238 */           opcode -= 26;
/* 2239 */           methodVisitor.visitVarInsn(21 + (opcode >> 2), opcode & 0x3);
/* 2240 */           currentOffset++;
/*      */           break;
/*      */         case 59:
/*      */         case 60:
/*      */         case 61:
/*      */         case 62:
/*      */         case 63:
/*      */         case 64:
/*      */         case 65:
/*      */         case 66:
/*      */         case 67:
/*      */         case 68:
/*      */         case 69:
/*      */         case 70:
/*      */         case 71:
/*      */         case 72:
/*      */         case 73:
/*      */         case 74:
/*      */         case 75:
/*      */         case 76:
/*      */         case 77:
/*      */         case 78:
/* 2262 */           opcode -= 59;
/* 2263 */           methodVisitor.visitVarInsn(54 + (opcode >> 2), opcode & 0x3);
/* 2264 */           currentOffset++;
/*      */           break;
/*      */         case 153:
/*      */         case 154:
/*      */         case 155:
/*      */         case 156:
/*      */         case 157:
/*      */         case 158:
/*      */         case 159:
/*      */         case 160:
/*      */         case 161:
/*      */         case 162:
/*      */         case 163:
/*      */         case 164:
/*      */         case 165:
/*      */         case 166:
/*      */         case 167:
/*      */         case 168:
/*      */         case 198:
/*      */         case 199:
/* 2284 */           methodVisitor.visitJumpInsn(opcode, labels[currentBytecodeOffset + 
/* 2285 */                 readShort(currentOffset + 1)]);
/* 2286 */           currentOffset += 3;
/*      */           break;
/*      */         case 200:
/*      */         case 201:
/* 2290 */           methodVisitor.visitJumpInsn(opcode - wideJumpOpcodeDelta, labels[currentBytecodeOffset + 
/*      */                 
/* 2292 */                 readInt(currentOffset + 1)]);
/* 2293 */           currentOffset += 5;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 202:
/*      */         case 203:
/*      */         case 204:
/*      */         case 205:
/*      */         case 206:
/*      */         case 207:
/*      */         case 208:
/*      */         case 209:
/*      */         case 210:
/*      */         case 211:
/*      */         case 212:
/*      */         case 213:
/*      */         case 214:
/*      */         case 215:
/*      */         case 216:
/*      */         case 217:
/*      */         case 218:
/*      */         case 219:
/* 2320 */           opcode = (opcode < 218) ? (opcode - 49) : (opcode - 20);
/*      */ 
/*      */ 
/*      */           
/* 2324 */           target = labels[currentBytecodeOffset + readUnsignedShort(currentOffset + 1)];
/* 2325 */           if (opcode == 167 || opcode == 168) {
/*      */             
/* 2327 */             methodVisitor.visitJumpInsn(opcode + 33, target);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 2332 */             opcode = (opcode < 167) ? ((opcode + 1 ^ 0x1) - 1) : (opcode ^ 0x1);
/* 2333 */             Label endif = createLabel(currentBytecodeOffset + 3, labels);
/* 2334 */             methodVisitor.visitJumpInsn(opcode, endif);
/* 2335 */             methodVisitor.visitJumpInsn(200, target);
/*      */ 
/*      */             
/* 2338 */             insertFrame = true;
/*      */           } 
/* 2340 */           currentOffset += 3;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 220:
/* 2345 */           methodVisitor.visitJumpInsn(200, labels[currentBytecodeOffset + 
/* 2346 */                 readInt(currentOffset + 1)]);
/*      */ 
/*      */ 
/*      */           
/* 2350 */           insertFrame = true;
/* 2351 */           currentOffset += 5;
/*      */           break;
/*      */         case 196:
/* 2354 */           opcode = classBuffer[currentOffset + 1] & 0xFF;
/* 2355 */           if (opcode == 132) {
/* 2356 */             methodVisitor.visitIincInsn(
/* 2357 */                 readUnsignedShort(currentOffset + 2), readShort(currentOffset + 4));
/* 2358 */             currentOffset += 6; break;
/*      */           } 
/* 2360 */           methodVisitor.visitVarInsn(opcode, readUnsignedShort(currentOffset + 2));
/* 2361 */           currentOffset += 4;
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 170:
/* 2367 */           currentOffset += 4 - (currentBytecodeOffset & 0x3);
/*      */           
/* 2369 */           defaultLabel = labels[currentBytecodeOffset + readInt(currentOffset)];
/* 2370 */           low = readInt(currentOffset + 4);
/* 2371 */           high = readInt(currentOffset + 8);
/* 2372 */           currentOffset += 12;
/* 2373 */           table = new Label[high - low + 1];
/* 2374 */           for (i = 0; i < table.length; i++) {
/* 2375 */             table[i] = labels[currentBytecodeOffset + readInt(currentOffset)];
/* 2376 */             currentOffset += 4;
/*      */           } 
/* 2378 */           methodVisitor.visitTableSwitchInsn(low, high, defaultLabel, table);
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 171:
/* 2384 */           currentOffset += 4 - (currentBytecodeOffset & 0x3);
/*      */           
/* 2386 */           defaultLabel = labels[currentBytecodeOffset + readInt(currentOffset)];
/* 2387 */           numPairs = readInt(currentOffset + 4);
/* 2388 */           currentOffset += 8;
/* 2389 */           keys = new int[numPairs];
/* 2390 */           values = new Label[numPairs];
/* 2391 */           for (i = 0; i < numPairs; i++) {
/* 2392 */             keys[i] = readInt(currentOffset);
/* 2393 */             values[i] = labels[currentBytecodeOffset + readInt(currentOffset + 4)];
/* 2394 */             currentOffset += 8;
/*      */           } 
/* 2396 */           methodVisitor.visitLookupSwitchInsn(defaultLabel, keys, values);
/*      */           break;
/*      */         
/*      */         case 21:
/*      */         case 22:
/*      */         case 23:
/*      */         case 24:
/*      */         case 25:
/*      */         case 54:
/*      */         case 55:
/*      */         case 56:
/*      */         case 57:
/*      */         case 58:
/*      */         case 169:
/* 2410 */           methodVisitor.visitVarInsn(opcode, classBuffer[currentOffset + 1] & 0xFF);
/* 2411 */           currentOffset += 2;
/*      */           break;
/*      */         case 16:
/*      */         case 188:
/* 2415 */           methodVisitor.visitIntInsn(opcode, classBuffer[currentOffset + 1]);
/* 2416 */           currentOffset += 2;
/*      */           break;
/*      */         case 17:
/* 2419 */           methodVisitor.visitIntInsn(opcode, readShort(currentOffset + 1));
/* 2420 */           currentOffset += 3;
/*      */           break;
/*      */         case 18:
/* 2423 */           methodVisitor.visitLdcInsn(readConst(classBuffer[currentOffset + 1] & 0xFF, charBuffer));
/* 2424 */           currentOffset += 2;
/*      */           break;
/*      */         case 19:
/*      */         case 20:
/* 2428 */           methodVisitor.visitLdcInsn(readConst(readUnsignedShort(currentOffset + 1), charBuffer));
/* 2429 */           currentOffset += 3;
/*      */           break;
/*      */         
/*      */         case 178:
/*      */         case 179:
/*      */         case 180:
/*      */         case 181:
/*      */         case 182:
/*      */         case 183:
/*      */         case 184:
/*      */         case 185:
/* 2440 */           cpInfoOffset = this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)];
/* 2441 */           nameAndTypeCpInfoOffset = this.cpInfoOffsets[readUnsignedShort(cpInfoOffset + 2)];
/* 2442 */           owner = readClass(cpInfoOffset, charBuffer);
/* 2443 */           str1 = readUTF8(nameAndTypeCpInfoOffset, charBuffer);
/* 2444 */           str2 = readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
/* 2445 */           if (opcode < 182) {
/* 2446 */             methodVisitor.visitFieldInsn(opcode, owner, str1, str2);
/*      */           } else {
/* 2448 */             boolean isInterface = (classBuffer[cpInfoOffset - 1] == 11);
/*      */             
/* 2450 */             methodVisitor.visitMethodInsn(opcode, owner, str1, str2, isInterface);
/*      */           } 
/* 2452 */           if (opcode == 185) {
/* 2453 */             currentOffset += 5; break;
/*      */           } 
/* 2455 */           currentOffset += 3;
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 186:
/* 2461 */           cpInfoOffset = this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)];
/* 2462 */           nameAndTypeCpInfoOffset = this.cpInfoOffsets[readUnsignedShort(cpInfoOffset + 2)];
/* 2463 */           name = readUTF8(nameAndTypeCpInfoOffset, charBuffer);
/* 2464 */           descriptor = readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
/* 2465 */           bootstrapMethodOffset = this.bootstrapMethodOffsets[readUnsignedShort(cpInfoOffset)];
/*      */           
/* 2467 */           handle = (Handle)readConst(readUnsignedShort(bootstrapMethodOffset), charBuffer);
/*      */           
/* 2469 */           bootstrapMethodArguments = new Object[readUnsignedShort(bootstrapMethodOffset + 2)];
/* 2470 */           bootstrapMethodOffset += 4;
/* 2471 */           for (j = 0; j < bootstrapMethodArguments.length; j++) {
/* 2472 */             bootstrapMethodArguments[j] = 
/* 2473 */               readConst(readUnsignedShort(bootstrapMethodOffset), charBuffer);
/* 2474 */             bootstrapMethodOffset += 2;
/*      */           } 
/* 2476 */           methodVisitor.visitInvokeDynamicInsn(name, descriptor, handle, bootstrapMethodArguments);
/*      */           
/* 2478 */           currentOffset += 5;
/*      */           break;
/*      */         
/*      */         case 187:
/*      */         case 189:
/*      */         case 192:
/*      */         case 193:
/* 2485 */           methodVisitor.visitTypeInsn(opcode, readClass(currentOffset + 1, charBuffer));
/* 2486 */           currentOffset += 3;
/*      */           break;
/*      */         case 132:
/* 2489 */           methodVisitor.visitIincInsn(classBuffer[currentOffset + 1] & 0xFF, classBuffer[currentOffset + 2]);
/*      */           
/* 2491 */           currentOffset += 3;
/*      */           break;
/*      */         case 197:
/* 2494 */           methodVisitor.visitMultiANewArrayInsn(
/* 2495 */               readClass(currentOffset + 1, charBuffer), classBuffer[currentOffset + 3] & 0xFF);
/* 2496 */           currentOffset += 4;
/*      */           break;
/*      */         default:
/* 2499 */           throw new AssertionError();
/*      */       } 
/*      */ 
/*      */       
/* 2503 */       while (visibleTypeAnnotationOffsets != null && currentVisibleTypeAnnotationIndex < visibleTypeAnnotationOffsets.length && currentVisibleTypeAnnotationBytecodeOffset <= currentBytecodeOffset) {
/*      */ 
/*      */         
/* 2506 */         if (currentVisibleTypeAnnotationBytecodeOffset == currentBytecodeOffset) {
/*      */ 
/*      */           
/* 2509 */           int currentAnnotationOffset = readTypeAnnotationTarget(context, visibleTypeAnnotationOffsets[currentVisibleTypeAnnotationIndex]);
/*      */ 
/*      */           
/* 2512 */           String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 2513 */           currentAnnotationOffset += 2;
/*      */           
/* 2515 */           readElementValues(methodVisitor
/* 2516 */               .visitInsnAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2526 */         currentVisibleTypeAnnotationBytecodeOffset = getTypeAnnotationBytecodeOffset(visibleTypeAnnotationOffsets, ++currentVisibleTypeAnnotationIndex);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2531 */       while (invisibleTypeAnnotationOffsets != null && currentInvisibleTypeAnnotationIndex < invisibleTypeAnnotationOffsets.length && currentInvisibleTypeAnnotationBytecodeOffset <= currentBytecodeOffset) {
/*      */ 
/*      */         
/* 2534 */         if (currentInvisibleTypeAnnotationBytecodeOffset == currentBytecodeOffset) {
/*      */ 
/*      */           
/* 2537 */           int currentAnnotationOffset = readTypeAnnotationTarget(context, invisibleTypeAnnotationOffsets[currentInvisibleTypeAnnotationIndex]);
/*      */ 
/*      */           
/* 2540 */           String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 2541 */           currentAnnotationOffset += 2;
/*      */           
/* 2543 */           readElementValues(methodVisitor
/* 2544 */               .visitInsnAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2554 */         currentInvisibleTypeAnnotationBytecodeOffset = getTypeAnnotationBytecodeOffset(invisibleTypeAnnotationOffsets, ++currentInvisibleTypeAnnotationIndex);
/*      */       } 
/*      */     } 
/*      */     
/* 2558 */     if (labels[codeLength] != null) {
/* 2559 */       methodVisitor.visitLabel(labels[codeLength]);
/*      */     }
/*      */ 
/*      */     
/* 2563 */     if (localVariableTableOffset != 0 && (context.parsingOptions & 0x2) == 0) {
/*      */       
/* 2565 */       int[] typeTable = null;
/* 2566 */       if (localVariableTypeTableOffset != 0) {
/* 2567 */         typeTable = new int[readUnsignedShort(localVariableTypeTableOffset) * 3];
/* 2568 */         currentOffset = localVariableTypeTableOffset + 2;
/* 2569 */         int typeTableIndex = typeTable.length;
/* 2570 */         while (typeTableIndex > 0) {
/*      */           
/* 2572 */           typeTable[--typeTableIndex] = currentOffset + 6;
/* 2573 */           typeTable[--typeTableIndex] = readUnsignedShort(currentOffset + 8);
/* 2574 */           typeTable[--typeTableIndex] = readUnsignedShort(currentOffset);
/* 2575 */           currentOffset += 10;
/*      */         } 
/*      */       } 
/* 2578 */       int localVariableTableLength = readUnsignedShort(localVariableTableOffset);
/* 2579 */       currentOffset = localVariableTableOffset + 2;
/* 2580 */       while (localVariableTableLength-- > 0) {
/* 2581 */         int startPc = readUnsignedShort(currentOffset);
/* 2582 */         int length = readUnsignedShort(currentOffset + 2);
/* 2583 */         String name = readUTF8(currentOffset + 4, charBuffer);
/* 2584 */         String descriptor = readUTF8(currentOffset + 6, charBuffer);
/* 2585 */         int index = readUnsignedShort(currentOffset + 8);
/* 2586 */         currentOffset += 10;
/* 2587 */         String signature = null;
/* 2588 */         if (typeTable != null) {
/* 2589 */           for (int i = 0; i < typeTable.length; i += 3) {
/* 2590 */             if (typeTable[i] == startPc && typeTable[i + 1] == index) {
/* 2591 */               signature = readUTF8(typeTable[i + 2], charBuffer);
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         }
/* 2596 */         methodVisitor.visitLocalVariable(name, descriptor, signature, labels[startPc], labels[startPc + length], index);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2602 */     if (visibleTypeAnnotationOffsets != null) {
/* 2603 */       for (int typeAnnotationOffset : visibleTypeAnnotationOffsets) {
/* 2604 */         int targetType = readByte(typeAnnotationOffset);
/* 2605 */         if (targetType == 64 || targetType == 65) {
/*      */ 
/*      */           
/* 2608 */           currentOffset = readTypeAnnotationTarget(context, typeAnnotationOffset);
/*      */           
/* 2610 */           String annotationDescriptor = readUTF8(currentOffset, charBuffer);
/* 2611 */           currentOffset += 2;
/*      */           
/* 2613 */           readElementValues(methodVisitor
/* 2614 */               .visitLocalVariableAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, context.currentLocalVariableAnnotationRangeStarts, context.currentLocalVariableAnnotationRangeEnds, context.currentLocalVariableAnnotationRangeIndices, annotationDescriptor, true), currentOffset, true, charBuffer);
/*      */         } 
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
/* 2630 */     if (invisibleTypeAnnotationOffsets != null) {
/* 2631 */       for (int typeAnnotationOffset : invisibleTypeAnnotationOffsets) {
/* 2632 */         int targetType = readByte(typeAnnotationOffset);
/* 2633 */         if (targetType == 64 || targetType == 65) {
/*      */ 
/*      */           
/* 2636 */           currentOffset = readTypeAnnotationTarget(context, typeAnnotationOffset);
/*      */           
/* 2638 */           String annotationDescriptor = readUTF8(currentOffset, charBuffer);
/* 2639 */           currentOffset += 2;
/*      */           
/* 2641 */           readElementValues(methodVisitor
/* 2642 */               .visitLocalVariableAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, context.currentLocalVariableAnnotationRangeStarts, context.currentLocalVariableAnnotationRangeEnds, context.currentLocalVariableAnnotationRangeIndices, annotationDescriptor, false), currentOffset, true, charBuffer);
/*      */         } 
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
/* 2658 */     while (attributes != null) {
/*      */       
/* 2660 */       Attribute nextAttribute = attributes.nextAttribute;
/* 2661 */       attributes.nextAttribute = null;
/* 2662 */       methodVisitor.visitAttribute(attributes);
/* 2663 */       attributes = nextAttribute;
/*      */     } 
/*      */ 
/*      */     
/* 2667 */     methodVisitor.visitMaxs(maxStack, maxLocals);
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
/*      */   protected Label readLabel(int bytecodeOffset, Label[] labels) {
/* 2682 */     if (bytecodeOffset >= labels.length) {
/* 2683 */       return new Label();
/*      */     }
/*      */     
/* 2686 */     if (labels[bytecodeOffset] == null) {
/* 2687 */       labels[bytecodeOffset] = new Label();
/*      */     }
/* 2689 */     return labels[bytecodeOffset];
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
/*      */   private Label createLabel(int bytecodeOffset, Label[] labels) {
/* 2702 */     Label label = readLabel(bytecodeOffset, labels);
/* 2703 */     label.flags = (short)(label.flags & 0xFFFFFFFE);
/* 2704 */     return label;
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
/*      */   private void createDebugLabel(int bytecodeOffset, Label[] labels) {
/* 2716 */     if (labels[bytecodeOffset] == null) {
/* 2717 */       (readLabel(bytecodeOffset, labels)).flags = (short)((readLabel(bytecodeOffset, labels)).flags | 0x1);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] readTypeAnnotations(MethodVisitor methodVisitor, Context context, int runtimeTypeAnnotationsOffset, boolean visible) {
/* 2744 */     char[] charBuffer = context.charBuffer;
/* 2745 */     int currentOffset = runtimeTypeAnnotationsOffset;
/*      */     
/* 2747 */     int[] typeAnnotationsOffsets = new int[readUnsignedShort(currentOffset)];
/* 2748 */     currentOffset += 2;
/*      */     
/* 2750 */     for (int i = 0; i < typeAnnotationsOffsets.length; i++) {
/* 2751 */       int tableLength; typeAnnotationsOffsets[i] = currentOffset;
/*      */ 
/*      */       
/* 2754 */       int targetType = readInt(currentOffset);
/* 2755 */       switch (targetType >>> 24) {
/*      */ 
/*      */         
/*      */         case 64:
/*      */         case 65:
/* 2760 */           tableLength = readUnsignedShort(currentOffset + 1);
/* 2761 */           currentOffset += 3;
/* 2762 */           while (tableLength-- > 0) {
/* 2763 */             int startPc = readUnsignedShort(currentOffset);
/* 2764 */             int length = readUnsignedShort(currentOffset + 2);
/*      */             
/* 2766 */             currentOffset += 6;
/* 2767 */             createLabel(startPc, context.currentMethodLabels);
/* 2768 */             createLabel(startPc + length, context.currentMethodLabels);
/*      */           } 
/*      */           break;
/*      */         case 71:
/*      */         case 72:
/*      */         case 73:
/*      */         case 74:
/*      */         case 75:
/* 2776 */           currentOffset += 4;
/*      */           break;
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 23:
/*      */         case 66:
/*      */         case 67:
/*      */         case 68:
/*      */         case 69:
/*      */         case 70:
/* 2787 */           currentOffset += 3;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/* 2797 */           throw new IllegalArgumentException();
/*      */       } 
/*      */ 
/*      */       
/* 2801 */       int pathLength = readByte(currentOffset);
/* 2802 */       if (targetType >>> 24 == 66) {
/*      */         
/* 2804 */         TypePath path = (pathLength == 0) ? null : new TypePath(this.classFileBuffer, currentOffset);
/* 2805 */         currentOffset += 1 + 2 * pathLength;
/*      */         
/* 2807 */         String annotationDescriptor = readUTF8(currentOffset, charBuffer);
/* 2808 */         currentOffset += 2;
/*      */ 
/*      */         
/* 2811 */         currentOffset = readElementValues(methodVisitor
/* 2812 */             .visitTryCatchAnnotation(targetType & 0xFFFFFF00, path, annotationDescriptor, visible), currentOffset, true, charBuffer);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */         
/* 2821 */         currentOffset += 3 + 2 * pathLength;
/*      */ 
/*      */ 
/*      */         
/* 2825 */         currentOffset = readElementValues(null, currentOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */     
/* 2829 */     return typeAnnotationsOffsets;
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
/*      */   private int getTypeAnnotationBytecodeOffset(int[] typeAnnotationOffsets, int typeAnnotationIndex) {
/* 2844 */     if (typeAnnotationOffsets == null || typeAnnotationIndex >= typeAnnotationOffsets.length || 
/*      */       
/* 2846 */       readByte(typeAnnotationOffsets[typeAnnotationIndex]) < 67) {
/* 2847 */       return -1;
/*      */     }
/* 2849 */     return readUnsignedShort(typeAnnotationOffsets[typeAnnotationIndex] + 1);
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
/*      */   private int readTypeAnnotationTarget(Context context, int typeAnnotationOffset) {
/* 2863 */     int tableLength, i, currentOffset = typeAnnotationOffset;
/*      */     
/* 2865 */     int targetType = readInt(typeAnnotationOffset);
/* 2866 */     switch (targetType >>> 24) {
/*      */       case 0:
/*      */       case 1:
/*      */       case 22:
/* 2870 */         targetType &= 0xFFFF0000;
/* 2871 */         currentOffset += 2;
/*      */         break;
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/* 2876 */         targetType &= 0xFF000000;
/* 2877 */         currentOffset++;
/*      */         break;
/*      */       case 64:
/*      */       case 65:
/* 2881 */         targetType &= 0xFF000000;
/* 2882 */         tableLength = readUnsignedShort(currentOffset + 1);
/* 2883 */         currentOffset += 3;
/* 2884 */         context.currentLocalVariableAnnotationRangeStarts = new Label[tableLength];
/* 2885 */         context.currentLocalVariableAnnotationRangeEnds = new Label[tableLength];
/* 2886 */         context.currentLocalVariableAnnotationRangeIndices = new int[tableLength];
/* 2887 */         for (i = 0; i < tableLength; i++) {
/* 2888 */           int startPc = readUnsignedShort(currentOffset);
/* 2889 */           int length = readUnsignedShort(currentOffset + 2);
/* 2890 */           int index = readUnsignedShort(currentOffset + 4);
/* 2891 */           currentOffset += 6;
/* 2892 */           context.currentLocalVariableAnnotationRangeStarts[i] = 
/* 2893 */             createLabel(startPc, context.currentMethodLabels);
/* 2894 */           context.currentLocalVariableAnnotationRangeEnds[i] = 
/* 2895 */             createLabel(startPc + length, context.currentMethodLabels);
/* 2896 */           context.currentLocalVariableAnnotationRangeIndices[i] = index;
/*      */         } 
/*      */         break;
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/* 2904 */         targetType &= 0xFF0000FF;
/* 2905 */         currentOffset += 4;
/*      */         break;
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 23:
/*      */       case 66:
/* 2912 */         targetType &= 0xFFFFFF00;
/* 2913 */         currentOffset += 3;
/*      */         break;
/*      */       case 67:
/*      */       case 68:
/*      */       case 69:
/*      */       case 70:
/* 2919 */         targetType &= 0xFF000000;
/* 2920 */         currentOffset += 3;
/*      */         break;
/*      */       default:
/* 2923 */         throw new IllegalArgumentException();
/*      */     } 
/* 2925 */     context.currentTypeAnnotationTarget = targetType;
/*      */     
/* 2927 */     int pathLength = readByte(currentOffset);
/* 2928 */     context.currentTypeAnnotationTargetPath = (pathLength == 0) ? null : new TypePath(this.classFileBuffer, currentOffset);
/*      */ 
/*      */     
/* 2931 */     return currentOffset + 1 + 2 * pathLength;
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
/*      */   private void readParameterAnnotations(MethodVisitor methodVisitor, Context context, int runtimeParameterAnnotationsOffset, boolean visible) {
/* 2950 */     int currentOffset = runtimeParameterAnnotationsOffset;
/* 2951 */     int numParameters = this.classFileBuffer[currentOffset++] & 0xFF;
/* 2952 */     methodVisitor.visitAnnotableParameterCount(numParameters, visible);
/* 2953 */     char[] charBuffer = context.charBuffer;
/* 2954 */     for (int i = 0; i < numParameters; i++) {
/* 2955 */       int numAnnotations = readUnsignedShort(currentOffset);
/* 2956 */       currentOffset += 2;
/* 2957 */       while (numAnnotations-- > 0) {
/*      */         
/* 2959 */         String annotationDescriptor = readUTF8(currentOffset, charBuffer);
/* 2960 */         currentOffset += 2;
/*      */ 
/*      */         
/* 2963 */         currentOffset = readElementValues(methodVisitor
/* 2964 */             .visitParameterAnnotation(i, annotationDescriptor, visible), currentOffset, true, charBuffer);
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readElementValues(AnnotationVisitor annotationVisitor, int annotationOffset, boolean named, char[] charBuffer) {
/* 2991 */     int currentOffset = annotationOffset;
/*      */     
/* 2993 */     int numElementValuePairs = readUnsignedShort(currentOffset);
/* 2994 */     currentOffset += 2;
/* 2995 */     if (named) {
/*      */       
/* 2997 */       while (numElementValuePairs-- > 0) {
/* 2998 */         String elementName = readUTF8(currentOffset, charBuffer);
/*      */         
/* 3000 */         currentOffset = readElementValue(annotationVisitor, currentOffset + 2, elementName, charBuffer);
/*      */       } 
/*      */     } else {
/*      */       
/* 3004 */       while (numElementValuePairs-- > 0)
/*      */       {
/* 3006 */         currentOffset = readElementValue(annotationVisitor, currentOffset, null, charBuffer);
/*      */       }
/*      */     } 
/* 3009 */     if (annotationVisitor != null) {
/* 3010 */       annotationVisitor.visitEnd();
/*      */     }
/* 3012 */     return currentOffset;
/*      */   }
/*      */   
/*      */   private int readElementValue(AnnotationVisitor annotationVisitor, int elementValueOffset, String elementName, char[] charBuffer) {
/*      */     int numValues;
/*      */     byte[] byteValues;
/*      */     int i;
/*      */     boolean[] booleanValues;
/*      */     int j;
/*      */     short[] shortValues;
/*      */     int k;
/*      */     char[] charValues;
/*      */     int m, intValues[], n;
/*      */     long[] longValues;
/*      */     int i1;
/*      */     float[] floatValues;
/*      */     int i2;
/*      */     double[] doubleValues;
/* 3030 */     int i3, currentOffset = elementValueOffset;
/* 3031 */     if (annotationVisitor == null) {
/* 3032 */       switch (this.classFileBuffer[currentOffset] & 0xFF) {
/*      */         case 101:
/* 3034 */           return currentOffset + 5;
/*      */         case 64:
/* 3036 */           return readElementValues(null, currentOffset + 3, true, charBuffer);
/*      */         case 91:
/* 3038 */           return readElementValues(null, currentOffset + 1, false, charBuffer);
/*      */       } 
/* 3040 */       return currentOffset + 3;
/*      */     } 
/*      */     
/* 3043 */     switch (this.classFileBuffer[currentOffset++] & 0xFF) {
/*      */       case 66:
/* 3045 */         annotationVisitor.visit(elementName, 
/* 3046 */             Byte.valueOf((byte)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset)])));
/* 3047 */         currentOffset += 2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3191 */         return currentOffset;case 67: annotationVisitor.visit(elementName, Character.valueOf((char)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset)]))); currentOffset += 2; return currentOffset;case 68: case 70: case 73: case 74: annotationVisitor.visit(elementName, readConst(readUnsignedShort(currentOffset), charBuffer)); currentOffset += 2; return currentOffset;case 83: annotationVisitor.visit(elementName, Short.valueOf((short)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset)]))); currentOffset += 2; return currentOffset;case 90: annotationVisitor.visit(elementName, (readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset)]) == 0) ? Boolean.FALSE : Boolean.TRUE); currentOffset += 2; return currentOffset;case 115: annotationVisitor.visit(elementName, readUTF8(currentOffset, charBuffer)); currentOffset += 2; return currentOffset;case 101: annotationVisitor.visitEnum(elementName, readUTF8(currentOffset, charBuffer), readUTF8(currentOffset + 2, charBuffer)); currentOffset += 4; return currentOffset;case 99: annotationVisitor.visit(elementName, Type.getType(readUTF8(currentOffset, charBuffer))); currentOffset += 2; return currentOffset;case 64: currentOffset = readElementValues(annotationVisitor.visitAnnotation(elementName, readUTF8(currentOffset, charBuffer)), currentOffset + 2, true, charBuffer); return currentOffset;case 91: numValues = readUnsignedShort(currentOffset); currentOffset += 2; if (numValues == 0) return readElementValues(annotationVisitor.visitArray(elementName), currentOffset - 2, false, charBuffer);  switch (this.classFileBuffer[currentOffset] & 0xFF) { case 66: byteValues = new byte[numValues]; for (i = 0; i < numValues; i++) { byteValues[i] = (byte)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]); currentOffset += 3; }  annotationVisitor.visit(elementName, byteValues); return currentOffset;case 90: booleanValues = new boolean[numValues]; for (j = 0; j < numValues; j++) { booleanValues[j] = (readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]) != 0); currentOffset += 3; }  annotationVisitor.visit(elementName, booleanValues); return currentOffset;case 83: shortValues = new short[numValues]; for (k = 0; k < numValues; k++) { shortValues[k] = (short)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]); currentOffset += 3; }  annotationVisitor.visit(elementName, shortValues); return currentOffset;case 67: charValues = new char[numValues]; for (m = 0; m < numValues; m++) { charValues[m] = (char)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]); currentOffset += 3; }  annotationVisitor.visit(elementName, charValues); return currentOffset;case 73: intValues = new int[numValues]; for (n = 0; n < numValues; n++) { intValues[n] = readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]); currentOffset += 3; }  annotationVisitor.visit(elementName, intValues); return currentOffset;case 74: longValues = new long[numValues]; for (i1 = 0; i1 < numValues; i1++) { longValues[i1] = readLong(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]); currentOffset += 3; }  annotationVisitor.visit(elementName, longValues); return currentOffset;case 70: floatValues = new float[numValues]; for (i2 = 0; i2 < numValues; i2++) { floatValues[i2] = Float.intBitsToFloat(readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)])); currentOffset += 3; }  annotationVisitor.visit(elementName, floatValues); return currentOffset;case 68: doubleValues = new double[numValues]; for (i3 = 0; i3 < numValues; i3++) { doubleValues[i3] = Double.longBitsToDouble(readLong(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)])); currentOffset += 3; }  annotationVisitor.visit(elementName, doubleValues); return currentOffset; }  currentOffset = readElementValues(annotationVisitor.visitArray(elementName), currentOffset - 2, false, charBuffer); return currentOffset;
/*      */     } 
/*      */     throw new IllegalArgumentException();
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
/*      */   private void computeImplicitFrame(Context context) {
/* 3205 */     String methodDescriptor = context.currentMethodDescriptor;
/* 3206 */     Object[] locals = context.currentFrameLocalTypes;
/* 3207 */     int numLocal = 0;
/* 3208 */     if ((context.currentMethodAccessFlags & 0x8) == 0) {
/* 3209 */       if ("<init>".equals(context.currentMethodName)) {
/* 3210 */         locals[numLocal++] = Opcodes.UNINITIALIZED_THIS;
/*      */       } else {
/* 3212 */         locals[numLocal++] = readClass(this.header + 2, context.charBuffer);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 3217 */     int currentMethodDescritorOffset = 1;
/*      */     while (true) {
/* 3219 */       int currentArgumentDescriptorStartOffset = currentMethodDescritorOffset;
/* 3220 */       switch (methodDescriptor.charAt(currentMethodDescritorOffset++)) {
/*      */         case 'B':
/*      */         case 'C':
/*      */         case 'I':
/*      */         case 'S':
/*      */         case 'Z':
/* 3226 */           locals[numLocal++] = Opcodes.INTEGER;
/*      */           continue;
/*      */         case 'F':
/* 3229 */           locals[numLocal++] = Opcodes.FLOAT;
/*      */           continue;
/*      */         case 'J':
/* 3232 */           locals[numLocal++] = Opcodes.LONG;
/*      */           continue;
/*      */         case 'D':
/* 3235 */           locals[numLocal++] = Opcodes.DOUBLE;
/*      */           continue;
/*      */         case '[':
/* 3238 */           while (methodDescriptor.charAt(currentMethodDescritorOffset) == '[') {
/* 3239 */             currentMethodDescritorOffset++;
/*      */           }
/* 3241 */           if (methodDescriptor.charAt(currentMethodDescritorOffset) == 'L') {
/* 3242 */             currentMethodDescritorOffset++;
/* 3243 */             while (methodDescriptor.charAt(currentMethodDescritorOffset) != ';') {
/* 3244 */               currentMethodDescritorOffset++;
/*      */             }
/*      */           } 
/* 3247 */           locals[numLocal++] = methodDescriptor
/* 3248 */             .substring(currentArgumentDescriptorStartOffset, ++currentMethodDescritorOffset);
/*      */           continue;
/*      */         
/*      */         case 'L':
/* 3252 */           while (methodDescriptor.charAt(currentMethodDescritorOffset) != ';') {
/* 3253 */             currentMethodDescritorOffset++;
/*      */           }
/* 3255 */           locals[numLocal++] = methodDescriptor
/* 3256 */             .substring(currentArgumentDescriptorStartOffset + 1, currentMethodDescritorOffset++); continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 3260 */     context.currentFrameLocalCount = numLocal;
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
/*      */   private int readStackMapFrame(int stackMapFrameOffset, boolean compressed, boolean expand, Context context) {
/* 3285 */     int frameType, offsetDelta, currentOffset = stackMapFrameOffset;
/* 3286 */     char[] charBuffer = context.charBuffer;
/* 3287 */     Label[] labels = context.currentMethodLabels;
/*      */     
/* 3289 */     if (compressed) {
/*      */       
/* 3291 */       frameType = this.classFileBuffer[currentOffset++] & 0xFF;
/*      */     } else {
/* 3293 */       frameType = 255;
/* 3294 */       context.currentFrameOffset = -1;
/*      */     } 
/*      */     
/* 3297 */     context.currentFrameLocalCountDelta = 0;
/* 3298 */     if (frameType < 64) {
/* 3299 */       offsetDelta = frameType;
/* 3300 */       context.currentFrameType = 3;
/* 3301 */       context.currentFrameStackCount = 0;
/* 3302 */     } else if (frameType < 128) {
/* 3303 */       offsetDelta = frameType - 64;
/*      */       
/* 3305 */       currentOffset = readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, 0, charBuffer, labels);
/*      */       
/* 3307 */       context.currentFrameType = 4;
/* 3308 */       context.currentFrameStackCount = 1;
/* 3309 */     } else if (frameType >= 247) {
/* 3310 */       offsetDelta = readUnsignedShort(currentOffset);
/* 3311 */       currentOffset += 2;
/* 3312 */       if (frameType == 247) {
/*      */         
/* 3314 */         currentOffset = readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, 0, charBuffer, labels);
/*      */         
/* 3316 */         context.currentFrameType = 4;
/* 3317 */         context.currentFrameStackCount = 1;
/* 3318 */       } else if (frameType >= 248 && frameType < 251) {
/* 3319 */         context.currentFrameType = 2;
/* 3320 */         context.currentFrameLocalCountDelta = 251 - frameType;
/* 3321 */         context.currentFrameLocalCount -= context.currentFrameLocalCountDelta;
/* 3322 */         context.currentFrameStackCount = 0;
/* 3323 */       } else if (frameType == 251) {
/* 3324 */         context.currentFrameType = 3;
/* 3325 */         context.currentFrameStackCount = 0;
/* 3326 */       } else if (frameType < 255) {
/* 3327 */         int local = expand ? context.currentFrameLocalCount : 0;
/* 3328 */         for (int k = frameType - 251; k > 0; k--)
/*      */         {
/* 3330 */           currentOffset = readVerificationTypeInfo(currentOffset, context.currentFrameLocalTypes, local++, charBuffer, labels);
/*      */         }
/*      */         
/* 3333 */         context.currentFrameType = 1;
/* 3334 */         context.currentFrameLocalCountDelta = frameType - 251;
/* 3335 */         context.currentFrameLocalCount += context.currentFrameLocalCountDelta;
/* 3336 */         context.currentFrameStackCount = 0;
/*      */       } else {
/* 3338 */         int numberOfLocals = readUnsignedShort(currentOffset);
/* 3339 */         currentOffset += 2;
/* 3340 */         context.currentFrameType = 0;
/* 3341 */         context.currentFrameLocalCountDelta = numberOfLocals;
/* 3342 */         context.currentFrameLocalCount = numberOfLocals;
/* 3343 */         for (int local = 0; local < numberOfLocals; local++)
/*      */         {
/* 3345 */           currentOffset = readVerificationTypeInfo(currentOffset, context.currentFrameLocalTypes, local, charBuffer, labels);
/*      */         }
/*      */         
/* 3348 */         int numberOfStackItems = readUnsignedShort(currentOffset);
/* 3349 */         currentOffset += 2;
/* 3350 */         context.currentFrameStackCount = numberOfStackItems;
/* 3351 */         for (int stack = 0; stack < numberOfStackItems; stack++)
/*      */         {
/* 3353 */           currentOffset = readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, stack, charBuffer, labels);
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/* 3358 */       throw new IllegalArgumentException();
/*      */     } 
/* 3360 */     context.currentFrameOffset += offsetDelta + 1;
/* 3361 */     createLabel(context.currentFrameOffset, labels);
/* 3362 */     return currentOffset;
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
/*      */   private int readVerificationTypeInfo(int verificationTypeInfoOffset, Object[] frame, int index, char[] charBuffer, Label[] labels) {
/* 3385 */     int currentOffset = verificationTypeInfoOffset;
/* 3386 */     int tag = this.classFileBuffer[currentOffset++] & 0xFF;
/* 3387 */     switch (tag) {
/*      */       case 0:
/* 3389 */         frame[index] = Opcodes.TOP;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3420 */         return currentOffset;case 1: frame[index] = Opcodes.INTEGER; return currentOffset;case 2: frame[index] = Opcodes.FLOAT; return currentOffset;case 3: frame[index] = Opcodes.DOUBLE; return currentOffset;case 4: frame[index] = Opcodes.LONG; return currentOffset;case 5: frame[index] = Opcodes.NULL; return currentOffset;case 6: frame[index] = Opcodes.UNINITIALIZED_THIS; return currentOffset;case 7: frame[index] = readClass(currentOffset, charBuffer); currentOffset += 2; return currentOffset;case 8: frame[index] = createLabel(readUnsignedShort(currentOffset), labels); currentOffset += 2; return currentOffset;
/*      */     } 
/*      */     throw new IllegalArgumentException();
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
/*      */   final int getFirstAttributeOffset() {
/* 3437 */     int currentOffset = this.header + 8 + readUnsignedShort(this.header + 6) * 2;
/*      */ 
/*      */     
/* 3440 */     int fieldsCount = readUnsignedShort(currentOffset);
/* 3441 */     currentOffset += 2;
/*      */     
/* 3443 */     while (fieldsCount-- > 0) {
/*      */ 
/*      */ 
/*      */       
/* 3447 */       int attributesCount = readUnsignedShort(currentOffset + 6);
/* 3448 */       currentOffset += 8;
/*      */       
/* 3450 */       while (attributesCount-- > 0)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 3455 */         currentOffset += 6 + readInt(currentOffset + 2);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 3460 */     int methodsCount = readUnsignedShort(currentOffset);
/* 3461 */     currentOffset += 2;
/* 3462 */     while (methodsCount-- > 0) {
/* 3463 */       int attributesCount = readUnsignedShort(currentOffset + 6);
/* 3464 */       currentOffset += 8;
/* 3465 */       while (attributesCount-- > 0) {
/* 3466 */         currentOffset += 6 + readInt(currentOffset + 2);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 3471 */     return currentOffset + 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] readBootstrapMethodsAttribute(int maxStringLength) {
/* 3482 */     char[] charBuffer = new char[maxStringLength];
/* 3483 */     int currentAttributeOffset = getFirstAttributeOffset();
/* 3484 */     for (int i = readUnsignedShort(currentAttributeOffset - 2); i > 0; i--) {
/*      */       
/* 3486 */       String attributeName = readUTF8(currentAttributeOffset, charBuffer);
/* 3487 */       int attributeLength = readInt(currentAttributeOffset + 2);
/* 3488 */       currentAttributeOffset += 6;
/* 3489 */       if ("BootstrapMethods".equals(attributeName)) {
/*      */         
/* 3491 */         int[] result = new int[readUnsignedShort(currentAttributeOffset)];
/*      */         
/* 3493 */         int currentBootstrapMethodOffset = currentAttributeOffset + 2;
/* 3494 */         for (int j = 0; j < result.length; j++) {
/* 3495 */           result[j] = currentBootstrapMethodOffset;
/*      */ 
/*      */           
/* 3498 */           currentBootstrapMethodOffset += 4 + 
/* 3499 */             readUnsignedShort(currentBootstrapMethodOffset + 2) * 2;
/*      */         } 
/* 3501 */         return result;
/*      */       } 
/* 3503 */       currentAttributeOffset += attributeLength;
/*      */     } 
/* 3505 */     throw new IllegalArgumentException();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Attribute readAttribute(Attribute[] attributePrototypes, String type, int offset, int length, char[] charBuffer, int codeAttributeOffset, Label[] labels) {
/* 3536 */     for (Attribute attributePrototype : attributePrototypes) {
/* 3537 */       if (attributePrototype.type.equals(type)) {
/* 3538 */         return attributePrototype.read(this, offset, length, charBuffer, codeAttributeOffset, labels);
/*      */       }
/*      */     } 
/*      */     
/* 3542 */     return (new Attribute(type)).read(this, offset, length, null, -1, null);
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
/*      */   public int getItemCount() {
/* 3555 */     return this.cpInfoOffsets.length;
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
/*      */   public int getItem(int constantPoolEntryIndex) {
/* 3569 */     return this.cpInfoOffsets[constantPoolEntryIndex];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxStringLength() {
/* 3580 */     return this.maxStringLength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readByte(int offset) {
/* 3591 */     return this.classFileBuffer[offset] & 0xFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readUnsignedShort(int offset) {
/* 3602 */     byte[] classBuffer = this.classFileBuffer;
/* 3603 */     return (classBuffer[offset] & 0xFF) << 8 | classBuffer[offset + 1] & 0xFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short readShort(int offset) {
/* 3614 */     byte[] classBuffer = this.classFileBuffer;
/* 3615 */     return (short)((classBuffer[offset] & 0xFF) << 8 | classBuffer[offset + 1] & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readInt(int offset) {
/* 3626 */     byte[] classBuffer = this.classFileBuffer;
/* 3627 */     return (classBuffer[offset] & 0xFF) << 24 | (classBuffer[offset + 1] & 0xFF) << 16 | (classBuffer[offset + 2] & 0xFF) << 8 | classBuffer[offset + 3] & 0xFF;
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
/*      */   public long readLong(int offset) {
/* 3641 */     long l1 = readInt(offset);
/* 3642 */     long l0 = readInt(offset + 4) & 0xFFFFFFFFL;
/* 3643 */     return l1 << 32L | l0;
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
/*      */   public String readUTF8(int offset, char[] charBuffer) {
/* 3659 */     int constantPoolEntryIndex = readUnsignedShort(offset);
/* 3660 */     if (offset == 0 || constantPoolEntryIndex == 0) {
/* 3661 */       return null;
/*      */     }
/* 3663 */     return readUtf(constantPoolEntryIndex, charBuffer);
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
/*      */   final String readUtf(int constantPoolEntryIndex, char[] charBuffer) {
/* 3676 */     String value = this.constantUtf8Values[constantPoolEntryIndex];
/* 3677 */     if (value != null) {
/* 3678 */       return value;
/*      */     }
/* 3680 */     int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
/* 3681 */     this.constantUtf8Values[constantPoolEntryIndex] = 
/* 3682 */       readUtf(cpInfoOffset + 2, readUnsignedShort(cpInfoOffset), charBuffer); return readUtf(cpInfoOffset + 2, readUnsignedShort(cpInfoOffset), charBuffer);
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
/*      */   private String readUtf(int utfOffset, int utfLength, char[] charBuffer) {
/* 3695 */     int currentOffset = utfOffset;
/* 3696 */     int endOffset = currentOffset + utfLength;
/* 3697 */     int strLength = 0;
/* 3698 */     byte[] classBuffer = this.classFileBuffer;
/* 3699 */     while (currentOffset < endOffset) {
/* 3700 */       int currentByte = classBuffer[currentOffset++];
/* 3701 */       if ((currentByte & 0x80) == 0) {
/* 3702 */         charBuffer[strLength++] = (char)(currentByte & 0x7F); continue;
/* 3703 */       }  if ((currentByte & 0xE0) == 192) {
/* 3704 */         charBuffer[strLength++] = (char)(((currentByte & 0x1F) << 6) + (classBuffer[currentOffset++] & 0x3F));
/*      */         continue;
/*      */       } 
/* 3707 */       charBuffer[strLength++] = (char)(((currentByte & 0xF) << 12) + ((classBuffer[currentOffset++] & 0x3F) << 6) + (classBuffer[currentOffset++] & 0x3F));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3714 */     return new String(charBuffer, 0, strLength);
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
/*      */   private String readStringish(int offset, char[] charBuffer) {
/* 3733 */     return readUTF8(this.cpInfoOffsets[readUnsignedShort(offset)], charBuffer);
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
/*      */   public String readClass(int offset, char[] charBuffer) {
/* 3748 */     return readStringish(offset, charBuffer);
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
/*      */   public String readModule(int offset, char[] charBuffer) {
/* 3763 */     return readStringish(offset, charBuffer);
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
/*      */   public String readPackage(int offset, char[] charBuffer) {
/* 3778 */     return readStringish(offset, charBuffer);
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
/*      */   private ConstantDynamic readConstantDynamic(int constantPoolEntryIndex, char[] charBuffer) {
/* 3792 */     ConstantDynamic constantDynamic = this.constantDynamicValues[constantPoolEntryIndex];
/* 3793 */     if (constantDynamic != null) {
/* 3794 */       return constantDynamic;
/*      */     }
/* 3796 */     int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
/* 3797 */     int nameAndTypeCpInfoOffset = this.cpInfoOffsets[readUnsignedShort(cpInfoOffset + 2)];
/* 3798 */     String name = readUTF8(nameAndTypeCpInfoOffset, charBuffer);
/* 3799 */     String descriptor = readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
/* 3800 */     int bootstrapMethodOffset = this.bootstrapMethodOffsets[readUnsignedShort(cpInfoOffset)];
/* 3801 */     Handle handle = (Handle)readConst(readUnsignedShort(bootstrapMethodOffset), charBuffer);
/* 3802 */     Object[] bootstrapMethodArguments = new Object[readUnsignedShort(bootstrapMethodOffset + 2)];
/* 3803 */     bootstrapMethodOffset += 4;
/* 3804 */     for (int i = 0; i < bootstrapMethodArguments.length; i++) {
/* 3805 */       bootstrapMethodArguments[i] = readConst(readUnsignedShort(bootstrapMethodOffset), charBuffer);
/* 3806 */       bootstrapMethodOffset += 2;
/*      */     } 
/* 3808 */     this.constantDynamicValues[constantPoolEntryIndex] = new ConstantDynamic(name, descriptor, handle, bootstrapMethodArguments); return new ConstantDynamic(name, descriptor, handle, bootstrapMethodArguments);
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
/*      */   public Object readConst(int constantPoolEntryIndex, char[] charBuffer) {
/*      */     int referenceKind, referenceCpInfoOffset, nameAndTypeCpInfoOffset;
/*      */     String owner, name, descriptor;
/*      */     boolean isInterface;
/* 3827 */     int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
/* 3828 */     switch (this.classFileBuffer[cpInfoOffset - 1]) {
/*      */       case 3:
/* 3830 */         return Integer.valueOf(readInt(cpInfoOffset));
/*      */       case 4:
/* 3832 */         return Float.valueOf(Float.intBitsToFloat(readInt(cpInfoOffset)));
/*      */       case 5:
/* 3834 */         return Long.valueOf(readLong(cpInfoOffset));
/*      */       case 6:
/* 3836 */         return Double.valueOf(Double.longBitsToDouble(readLong(cpInfoOffset)));
/*      */       case 7:
/* 3838 */         return Type.getObjectType(readUTF8(cpInfoOffset, charBuffer));
/*      */       case 8:
/* 3840 */         return readUTF8(cpInfoOffset, charBuffer);
/*      */       case 16:
/* 3842 */         return Type.getMethodType(readUTF8(cpInfoOffset, charBuffer));
/*      */       case 15:
/* 3844 */         referenceKind = readByte(cpInfoOffset);
/* 3845 */         referenceCpInfoOffset = this.cpInfoOffsets[readUnsignedShort(cpInfoOffset + 1)];
/* 3846 */         nameAndTypeCpInfoOffset = this.cpInfoOffsets[readUnsignedShort(referenceCpInfoOffset + 2)];
/* 3847 */         owner = readClass(referenceCpInfoOffset, charBuffer);
/* 3848 */         name = readUTF8(nameAndTypeCpInfoOffset, charBuffer);
/* 3849 */         descriptor = readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
/* 3850 */         isInterface = (this.classFileBuffer[referenceCpInfoOffset - 1] == 11);
/*      */         
/* 3852 */         return new Handle(referenceKind, owner, name, descriptor, isInterface);
/*      */       case 17:
/* 3854 */         return readConstantDynamic(constantPoolEntryIndex, charBuffer);
/*      */     } 
/* 3856 */     throw new IllegalArgumentException();
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\asm\ClassReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */