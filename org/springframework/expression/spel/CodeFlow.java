/*      */ package org.springframework.expression.spel;
/*      */ 
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Deque;
/*      */ import java.util.List;
/*      */ import org.springframework.asm.ClassWriter;
/*      */ import org.springframework.asm.MethodVisitor;
/*      */ import org.springframework.asm.Opcodes;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CodeFlow
/*      */   implements Opcodes
/*      */ {
/*      */   private final String className;
/*      */   private final ClassWriter classWriter;
/*      */   private final Deque<List<String>> compilationScopes;
/*      */   @Nullable
/*      */   private List<FieldAdder> fieldAdders;
/*      */   @Nullable
/*      */   private List<ClinitAdder> clinitAdders;
/*   84 */   private int nextFieldId = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   90 */   private int nextFreeVariableId = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CodeFlow(String className, ClassWriter classWriter) {
/*   99 */     this.className = className;
/*  100 */     this.classWriter = classWriter;
/*  101 */     this.compilationScopes = new ArrayDeque<>();
/*  102 */     this.compilationScopes.add(new ArrayList<>());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadTarget(MethodVisitor mv) {
/*  112 */     mv.visitVarInsn(25, 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadEvaluationContext(MethodVisitor mv) {
/*  122 */     mv.visitVarInsn(25, 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void pushDescriptor(@Nullable String descriptor) {
/*  130 */     if (descriptor != null) {
/*  131 */       ((List<String>)this.compilationScopes.element()).add(descriptor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterCompilationScope() {
/*  141 */     this.compilationScopes.push(new ArrayList<>());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void exitCompilationScope() {
/*  150 */     this.compilationScopes.pop();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String lastDescriptor() {
/*  158 */     return (String)CollectionUtils.lastElement(this.compilationScopes.peek());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unboxBooleanIfNecessary(MethodVisitor mv) {
/*  167 */     if ("Ljava/lang/Boolean".equals(lastDescriptor())) {
/*  168 */       mv.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void finish() {
/*  178 */     if (this.fieldAdders != null) {
/*  179 */       for (FieldAdder fieldAdder : this.fieldAdders) {
/*  180 */         fieldAdder.generateField(this.classWriter, this);
/*      */       }
/*      */     }
/*  183 */     if (this.clinitAdders != null) {
/*  184 */       MethodVisitor mv = this.classWriter.visitMethod(9, "<clinit>", "()V", null, null);
/*  185 */       mv.visitCode();
/*  186 */       this.nextFreeVariableId = 0;
/*  187 */       for (ClinitAdder clinitAdder : this.clinitAdders) {
/*  188 */         clinitAdder.generateCode(mv, this);
/*      */       }
/*  190 */       mv.visitInsn(177);
/*  191 */       mv.visitMaxs(0, 0);
/*  192 */       mv.visitEnd();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerNewField(FieldAdder fieldAdder) {
/*  202 */     if (this.fieldAdders == null) {
/*  203 */       this.fieldAdders = new ArrayList<>();
/*      */     }
/*  205 */     this.fieldAdders.add(fieldAdder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerNewClinit(ClinitAdder clinitAdder) {
/*  214 */     if (this.clinitAdders == null) {
/*  215 */       this.clinitAdders = new ArrayList<>();
/*      */     }
/*  217 */     this.clinitAdders.add(clinitAdder);
/*      */   }
/*      */   
/*      */   public int nextFieldId() {
/*  221 */     return this.nextFieldId++;
/*      */   }
/*      */   
/*      */   public int nextFreeVariableId() {
/*  225 */     return this.nextFreeVariableId++;
/*      */   }
/*      */   
/*      */   public String getClassName() {
/*  229 */     return this.className;
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
/*      */   public static void insertUnboxInsns(MethodVisitor mv, char ch, @Nullable String stackDescriptor) {
/*  241 */     if (stackDescriptor == null) {
/*      */       return;
/*      */     }
/*  244 */     switch (ch) {
/*      */       case 'Z':
/*  246 */         if (!stackDescriptor.equals("Ljava/lang/Boolean")) {
/*  247 */           mv.visitTypeInsn(192, "java/lang/Boolean");
/*      */         }
/*  249 */         mv.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
/*      */         return;
/*      */       case 'B':
/*  252 */         if (!stackDescriptor.equals("Ljava/lang/Byte")) {
/*  253 */           mv.visitTypeInsn(192, "java/lang/Byte");
/*      */         }
/*  255 */         mv.visitMethodInsn(182, "java/lang/Byte", "byteValue", "()B", false);
/*      */         return;
/*      */       case 'C':
/*  258 */         if (!stackDescriptor.equals("Ljava/lang/Character")) {
/*  259 */           mv.visitTypeInsn(192, "java/lang/Character");
/*      */         }
/*  261 */         mv.visitMethodInsn(182, "java/lang/Character", "charValue", "()C", false);
/*      */         return;
/*      */       case 'D':
/*  264 */         if (!stackDescriptor.equals("Ljava/lang/Double")) {
/*  265 */           mv.visitTypeInsn(192, "java/lang/Double");
/*      */         }
/*  267 */         mv.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D", false);
/*      */         return;
/*      */       case 'F':
/*  270 */         if (!stackDescriptor.equals("Ljava/lang/Float")) {
/*  271 */           mv.visitTypeInsn(192, "java/lang/Float");
/*      */         }
/*  273 */         mv.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F", false);
/*      */         return;
/*      */       case 'I':
/*  276 */         if (!stackDescriptor.equals("Ljava/lang/Integer")) {
/*  277 */           mv.visitTypeInsn(192, "java/lang/Integer");
/*      */         }
/*  279 */         mv.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I", false);
/*      */         return;
/*      */       case 'J':
/*  282 */         if (!stackDescriptor.equals("Ljava/lang/Long")) {
/*  283 */           mv.visitTypeInsn(192, "java/lang/Long");
/*      */         }
/*  285 */         mv.visitMethodInsn(182, "java/lang/Long", "longValue", "()J", false);
/*      */         return;
/*      */       case 'S':
/*  288 */         if (!stackDescriptor.equals("Ljava/lang/Short")) {
/*  289 */           mv.visitTypeInsn(192, "java/lang/Short");
/*      */         }
/*  291 */         mv.visitMethodInsn(182, "java/lang/Short", "shortValue", "()S", false);
/*      */         return;
/*      */     } 
/*  294 */     throw new IllegalArgumentException("Unboxing should not be attempted for descriptor '" + ch + "'");
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
/*      */   public static void insertUnboxNumberInsns(MethodVisitor mv, char targetDescriptor, @Nullable String stackDescriptor) {
/*  307 */     if (stackDescriptor == null) {
/*      */       return;
/*      */     }
/*      */     
/*  311 */     switch (targetDescriptor) {
/*      */       case 'D':
/*  313 */         if (stackDescriptor.equals("Ljava/lang/Object")) {
/*  314 */           mv.visitTypeInsn(192, "java/lang/Number");
/*      */         }
/*  316 */         mv.visitMethodInsn(182, "java/lang/Number", "doubleValue", "()D", false);
/*      */         return;
/*      */       case 'F':
/*  319 */         if (stackDescriptor.equals("Ljava/lang/Object")) {
/*  320 */           mv.visitTypeInsn(192, "java/lang/Number");
/*      */         }
/*  322 */         mv.visitMethodInsn(182, "java/lang/Number", "floatValue", "()F", false);
/*      */         return;
/*      */       case 'J':
/*  325 */         if (stackDescriptor.equals("Ljava/lang/Object")) {
/*  326 */           mv.visitTypeInsn(192, "java/lang/Number");
/*      */         }
/*  328 */         mv.visitMethodInsn(182, "java/lang/Number", "longValue", "()J", false);
/*      */         return;
/*      */       case 'I':
/*  331 */         if (stackDescriptor.equals("Ljava/lang/Object")) {
/*  332 */           mv.visitTypeInsn(192, "java/lang/Number");
/*      */         }
/*  334 */         mv.visitMethodInsn(182, "java/lang/Number", "intValue", "()I", false);
/*      */         return;
/*      */     } 
/*      */     
/*  338 */     throw new IllegalArgumentException("Unboxing should not be attempted for descriptor '" + targetDescriptor + "'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void insertAnyNecessaryTypeConversionBytecodes(MethodVisitor mv, char targetDescriptor, String stackDescriptor) {
/*  349 */     if (isPrimitive(stackDescriptor)) {
/*  350 */       char stackTop = stackDescriptor.charAt(0);
/*  351 */       if (stackTop == 'I' || stackTop == 'B' || stackTop == 'S' || stackTop == 'C') {
/*  352 */         if (targetDescriptor == 'D') {
/*  353 */           mv.visitInsn(135);
/*      */         }
/*  355 */         else if (targetDescriptor == 'F') {
/*  356 */           mv.visitInsn(134);
/*      */         }
/*  358 */         else if (targetDescriptor == 'J') {
/*  359 */           mv.visitInsn(133);
/*      */         }
/*  361 */         else if (targetDescriptor != 'I') {
/*      */ 
/*      */ 
/*      */           
/*  365 */           throw new IllegalStateException("Cannot get from " + stackTop + " to " + targetDescriptor);
/*      */         }
/*      */       
/*  368 */       } else if (stackTop == 'J') {
/*  369 */         if (targetDescriptor == 'D') {
/*  370 */           mv.visitInsn(138);
/*      */         }
/*  372 */         else if (targetDescriptor == 'F') {
/*  373 */           mv.visitInsn(137);
/*      */         }
/*  375 */         else if (targetDescriptor != 'J') {
/*      */ 
/*      */           
/*  378 */           if (targetDescriptor == 'I') {
/*  379 */             mv.visitInsn(136);
/*      */           } else {
/*      */             
/*  382 */             throw new IllegalStateException("Cannot get from " + stackTop + " to " + targetDescriptor);
/*      */           } 
/*      */         } 
/*  385 */       } else if (stackTop == 'F') {
/*  386 */         if (targetDescriptor == 'D') {
/*  387 */           mv.visitInsn(141);
/*      */         }
/*  389 */         else if (targetDescriptor != 'F') {
/*      */ 
/*      */           
/*  392 */           if (targetDescriptor == 'J') {
/*  393 */             mv.visitInsn(140);
/*      */           }
/*  395 */           else if (targetDescriptor == 'I') {
/*  396 */             mv.visitInsn(139);
/*      */           } else {
/*      */             
/*  399 */             throw new IllegalStateException("Cannot get from " + stackTop + " to " + targetDescriptor);
/*      */           } 
/*      */         } 
/*  402 */       } else if (stackTop == 'D' && 
/*  403 */         targetDescriptor != 'D') {
/*      */ 
/*      */         
/*  406 */         if (targetDescriptor == 'F') {
/*  407 */           mv.visitInsn(144);
/*      */         }
/*  409 */         else if (targetDescriptor == 'J') {
/*  410 */           mv.visitInsn(143);
/*      */         }
/*  412 */         else if (targetDescriptor == 'I') {
/*  413 */           mv.visitInsn(142);
/*      */         } else {
/*      */           
/*  416 */           throw new IllegalStateException("Cannot get from " + stackDescriptor + " to " + targetDescriptor);
/*      */         } 
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
/*      */   public static String createSignatureDescriptor(Method method) {
/*  433 */     Class<?>[] params = method.getParameterTypes();
/*  434 */     StringBuilder sb = new StringBuilder();
/*  435 */     sb.append('(');
/*  436 */     for (Class<?> param : params) {
/*  437 */       sb.append(toJvmDescriptor(param));
/*      */     }
/*  439 */     sb.append(')');
/*  440 */     sb.append(toJvmDescriptor(method.getReturnType()));
/*  441 */     return sb.toString();
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
/*      */   public static String createSignatureDescriptor(Constructor<?> ctor) {
/*  454 */     Class<?>[] params = ctor.getParameterTypes();
/*  455 */     StringBuilder sb = new StringBuilder();
/*  456 */     sb.append('(');
/*  457 */     for (Class<?> param : params) {
/*  458 */       sb.append(toJvmDescriptor(param));
/*      */     }
/*  460 */     sb.append(")V");
/*  461 */     return sb.toString();
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
/*      */   public static String toJvmDescriptor(Class<?> clazz) {
/*  473 */     StringBuilder sb = new StringBuilder();
/*  474 */     if (clazz.isArray()) {
/*  475 */       while (clazz.isArray()) {
/*  476 */         sb.append('[');
/*  477 */         clazz = clazz.getComponentType();
/*      */       } 
/*      */     }
/*  480 */     if (clazz.isPrimitive()) {
/*  481 */       if (clazz == boolean.class) {
/*  482 */         sb.append('Z');
/*      */       }
/*  484 */       else if (clazz == byte.class) {
/*  485 */         sb.append('B');
/*      */       }
/*  487 */       else if (clazz == char.class) {
/*  488 */         sb.append('C');
/*      */       }
/*  490 */       else if (clazz == double.class) {
/*  491 */         sb.append('D');
/*      */       }
/*  493 */       else if (clazz == float.class) {
/*  494 */         sb.append('F');
/*      */       }
/*  496 */       else if (clazz == int.class) {
/*  497 */         sb.append('I');
/*      */       }
/*  499 */       else if (clazz == long.class) {
/*  500 */         sb.append('J');
/*      */       }
/*  502 */       else if (clazz == short.class) {
/*  503 */         sb.append('S');
/*      */       }
/*  505 */       else if (clazz == void.class) {
/*  506 */         sb.append('V');
/*      */       } 
/*      */     } else {
/*      */       
/*  510 */       sb.append('L');
/*  511 */       sb.append(clazz.getName().replace('.', '/'));
/*  512 */       sb.append(';');
/*      */     } 
/*  514 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toDescriptorFromObject(@Nullable Object value) {
/*  524 */     if (value == null) {
/*  525 */       return "Ljava/lang/Object";
/*      */     }
/*      */     
/*  528 */     return toDescriptor(value.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isBooleanCompatible(@Nullable String descriptor) {
/*  538 */     return (descriptor != null && (descriptor.equals("Z") || descriptor.equals("Ljava/lang/Boolean")));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitive(@Nullable String descriptor) {
/*  547 */     return (descriptor != null && descriptor.length() == 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveArray(@Nullable String descriptor) {
/*  556 */     if (descriptor == null) {
/*  557 */       return false;
/*      */     }
/*  559 */     boolean primitive = true;
/*  560 */     for (int i = 0, max = descriptor.length(); i < max; ) {
/*  561 */       char ch = descriptor.charAt(i);
/*  562 */       if (ch == '[') {
/*      */         i++; continue;
/*      */       } 
/*  565 */       primitive = (ch != 'L');
/*      */     } 
/*      */     
/*  568 */     return primitive;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean areBoxingCompatible(String desc1, String desc2) {
/*  577 */     if (desc1.equals(desc2)) {
/*  578 */       return true;
/*      */     }
/*  580 */     if (desc1.length() == 1) {
/*  581 */       if (desc1.equals("Z")) {
/*  582 */         return desc2.equals("Ljava/lang/Boolean");
/*      */       }
/*  584 */       if (desc1.equals("D")) {
/*  585 */         return desc2.equals("Ljava/lang/Double");
/*      */       }
/*  587 */       if (desc1.equals("F")) {
/*  588 */         return desc2.equals("Ljava/lang/Float");
/*      */       }
/*  590 */       if (desc1.equals("I")) {
/*  591 */         return desc2.equals("Ljava/lang/Integer");
/*      */       }
/*  593 */       if (desc1.equals("J")) {
/*  594 */         return desc2.equals("Ljava/lang/Long");
/*      */       }
/*      */     }
/*  597 */     else if (desc2.length() == 1) {
/*  598 */       if (desc2.equals("Z")) {
/*  599 */         return desc1.equals("Ljava/lang/Boolean");
/*      */       }
/*  601 */       if (desc2.equals("D")) {
/*  602 */         return desc1.equals("Ljava/lang/Double");
/*      */       }
/*  604 */       if (desc2.equals("F")) {
/*  605 */         return desc1.equals("Ljava/lang/Float");
/*      */       }
/*  607 */       if (desc2.equals("I")) {
/*  608 */         return desc1.equals("Ljava/lang/Integer");
/*      */       }
/*  610 */       if (desc2.equals("J")) {
/*  611 */         return desc1.equals("Ljava/lang/Long");
/*      */       }
/*      */     } 
/*  614 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveOrUnboxableSupportedNumberOrBoolean(@Nullable String descriptor) {
/*  625 */     if (descriptor == null) {
/*  626 */       return false;
/*      */     }
/*  628 */     if (isPrimitiveOrUnboxableSupportedNumber(descriptor)) {
/*  629 */       return true;
/*      */     }
/*  631 */     return ("Z".equals(descriptor) || descriptor.equals("Ljava/lang/Boolean"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveOrUnboxableSupportedNumber(@Nullable String descriptor) {
/*  642 */     if (descriptor == null) {
/*  643 */       return false;
/*      */     }
/*  645 */     if (descriptor.length() == 1) {
/*  646 */       return "DFIJ".contains(descriptor);
/*      */     }
/*  648 */     if (descriptor.startsWith("Ljava/lang/")) {
/*  649 */       String name = descriptor.substring("Ljava/lang/".length());
/*  650 */       if (name.equals("Double") || name.equals("Float") || name.equals("Integer") || name.equals("Long")) {
/*  651 */         return true;
/*      */       }
/*      */     } 
/*  654 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isIntegerForNumericOp(Number number) {
/*  664 */     return (number instanceof Integer || number instanceof Short || number instanceof Byte);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char toPrimitiveTargetDesc(String descriptor) {
/*  673 */     if (descriptor.length() == 1) {
/*  674 */       return descriptor.charAt(0);
/*      */     }
/*  676 */     if (descriptor.equals("Ljava/lang/Boolean")) {
/*  677 */       return 'Z';
/*      */     }
/*  679 */     if (descriptor.equals("Ljava/lang/Byte")) {
/*  680 */       return 'B';
/*      */     }
/*  682 */     if (descriptor.equals("Ljava/lang/Character")) {
/*  683 */       return 'C';
/*      */     }
/*  685 */     if (descriptor.equals("Ljava/lang/Double")) {
/*  686 */       return 'D';
/*      */     }
/*  688 */     if (descriptor.equals("Ljava/lang/Float")) {
/*  689 */       return 'F';
/*      */     }
/*  691 */     if (descriptor.equals("Ljava/lang/Integer")) {
/*  692 */       return 'I';
/*      */     }
/*  694 */     if (descriptor.equals("Ljava/lang/Long")) {
/*  695 */       return 'J';
/*      */     }
/*  697 */     if (descriptor.equals("Ljava/lang/Short")) {
/*  698 */       return 'S';
/*      */     }
/*      */     
/*  701 */     throw new IllegalStateException("No primitive for '" + descriptor + "'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void insertCheckCast(MethodVisitor mv, @Nullable String descriptor) {
/*  711 */     if (descriptor != null && descriptor.length() != 1) {
/*  712 */       if (descriptor.charAt(0) == '[') {
/*  713 */         if (isPrimitiveArray(descriptor)) {
/*  714 */           mv.visitTypeInsn(192, descriptor);
/*      */         } else {
/*      */           
/*  717 */           mv.visitTypeInsn(192, descriptor + ";");
/*      */         }
/*      */       
/*      */       }
/*  721 */       else if (!descriptor.equals("Ljava/lang/Object")) {
/*      */         
/*  723 */         mv.visitTypeInsn(192, descriptor.substring(1));
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
/*      */   public static void insertBoxIfNecessary(MethodVisitor mv, @Nullable String descriptor) {
/*  736 */     if (descriptor != null && descriptor.length() == 1) {
/*  737 */       insertBoxIfNecessary(mv, descriptor.charAt(0));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void insertBoxIfNecessary(MethodVisitor mv, char ch) {
/*  748 */     switch (ch) {
/*      */       case 'Z':
/*  750 */         mv.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
/*      */       
/*      */       case 'B':
/*  753 */         mv.visitMethodInsn(184, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
/*      */       
/*      */       case 'C':
/*  756 */         mv.visitMethodInsn(184, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
/*      */       
/*      */       case 'D':
/*  759 */         mv.visitMethodInsn(184, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
/*      */       
/*      */       case 'F':
/*  762 */         mv.visitMethodInsn(184, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
/*      */       
/*      */       case 'I':
/*  765 */         mv.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
/*      */       
/*      */       case 'J':
/*  768 */         mv.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
/*      */       
/*      */       case 'S':
/*  771 */         mv.visitMethodInsn(184, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
/*      */       
/*      */       case 'L':
/*      */       case 'V':
/*      */       case '[':
/*      */         return;
/*      */     } 
/*      */     
/*  779 */     throw new IllegalArgumentException("Boxing should not be attempted for descriptor '" + ch + "'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toDescriptor(Class<?> type) {
/*  790 */     String name = type.getName();
/*  791 */     if (type.isPrimitive()) {
/*  792 */       switch (name.length()) {
/*      */         case 3:
/*  794 */           return "I";
/*      */         case 4:
/*  796 */           if (name.equals("byte")) {
/*  797 */             return "B";
/*      */           }
/*  799 */           if (name.equals("char")) {
/*  800 */             return "C";
/*      */           }
/*  802 */           if (name.equals("long")) {
/*  803 */             return "J";
/*      */           }
/*  805 */           if (name.equals("void")) {
/*  806 */             return "V";
/*      */           }
/*      */           break;
/*      */         case 5:
/*  810 */           if (name.equals("float")) {
/*  811 */             return "F";
/*      */           }
/*  813 */           if (name.equals("short")) {
/*  814 */             return "S";
/*      */           }
/*      */           break;
/*      */         case 6:
/*  818 */           if (name.equals("double")) {
/*  819 */             return "D";
/*      */           }
/*      */           break;
/*      */         case 7:
/*  823 */           if (name.equals("boolean")) {
/*  824 */             return "Z";
/*      */           }
/*      */           break;
/*      */       } 
/*      */     
/*      */     } else {
/*  830 */       if (name.charAt(0) != '[') {
/*  831 */         return "L" + type.getName().replace('.', '/');
/*      */       }
/*      */       
/*  834 */       if (name.endsWith(";")) {
/*  835 */         return name.substring(0, name.length() - 1).replace('.', '/');
/*      */       }
/*      */       
/*  838 */       return name;
/*      */     } 
/*      */ 
/*      */     
/*  842 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] toParamDescriptors(Method method) {
/*  852 */     return toDescriptors(method.getParameterTypes());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] toParamDescriptors(Constructor<?> ctor) {
/*  862 */     return toDescriptors(ctor.getParameterTypes());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] toDescriptors(Class<?>[] types) {
/*  871 */     int typesCount = types.length;
/*  872 */     String[] descriptors = new String[typesCount];
/*  873 */     for (int p = 0; p < typesCount; p++) {
/*  874 */       descriptors[p] = toDescriptor(types[p]);
/*      */     }
/*  876 */     return descriptors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void insertOptimalLoad(MethodVisitor mv, int value) {
/*  885 */     if (value < 6) {
/*  886 */       mv.visitInsn(3 + value);
/*      */     }
/*  888 */     else if (value < 127) {
/*  889 */       mv.visitIntInsn(16, value);
/*      */     }
/*  891 */     else if (value < 32767) {
/*  892 */       mv.visitIntInsn(17, value);
/*      */     } else {
/*      */       
/*  895 */       mv.visitLdcInsn(Integer.valueOf(value));
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
/*      */   public static void insertArrayStore(MethodVisitor mv, String arrayElementType) {
/*  907 */     if (arrayElementType.length() == 1) {
/*  908 */       switch (arrayElementType.charAt(0)) {
/*      */         case 'I':
/*  910 */           mv.visitInsn(79);
/*      */           return;
/*      */         case 'J':
/*  913 */           mv.visitInsn(80);
/*      */           return;
/*      */         case 'F':
/*  916 */           mv.visitInsn(81);
/*      */           return;
/*      */         case 'D':
/*  919 */           mv.visitInsn(82);
/*      */           return;
/*      */         case 'B':
/*  922 */           mv.visitInsn(84);
/*      */           return;
/*      */         case 'C':
/*  925 */           mv.visitInsn(85);
/*      */           return;
/*      */         case 'S':
/*  928 */           mv.visitInsn(86);
/*      */           return;
/*      */         case 'Z':
/*  931 */           mv.visitInsn(84);
/*      */           return;
/*      */       } 
/*  934 */       throw new IllegalArgumentException("Unexpected arraytype " + arrayElementType
/*  935 */           .charAt(0));
/*      */     } 
/*      */ 
/*      */     
/*  939 */     mv.visitInsn(83);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int arrayCodeFor(String arraytype) {
/*  949 */     switch (arraytype.charAt(0)) { case 'I':
/*  950 */         return 10;
/*  951 */       case 'J': return 11;
/*  952 */       case 'F': return 6;
/*  953 */       case 'D': return 7;
/*  954 */       case 'B': return 8;
/*  955 */       case 'C': return 5;
/*  956 */       case 'S': return 9;
/*  957 */       case 'Z': return 4; }
/*      */     
/*  959 */     throw new IllegalArgumentException("Unexpected arraytype " + arraytype.charAt(0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isReferenceTypeArray(String arraytype) {
/*  967 */     int length = arraytype.length();
/*  968 */     for (int i = 0; i < length; ) {
/*  969 */       char ch = arraytype.charAt(i);
/*  970 */       if (ch == '[') {
/*      */         i++; continue;
/*      */       } 
/*  973 */       return (ch == 'L');
/*      */     } 
/*  975 */     return false;
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
/*      */   public static void insertNewArrayCode(MethodVisitor mv, int size, String arraytype) {
/*  987 */     insertOptimalLoad(mv, size);
/*  988 */     if (arraytype.length() == 1) {
/*  989 */       mv.visitIntInsn(188, arrayCodeFor(arraytype));
/*      */     
/*      */     }
/*  992 */     else if (arraytype.charAt(0) == '[') {
/*      */ 
/*      */       
/*  995 */       if (isReferenceTypeArray(arraytype)) {
/*  996 */         mv.visitTypeInsn(189, arraytype + ";");
/*      */       } else {
/*      */         
/*  999 */         mv.visitTypeInsn(189, arraytype);
/*      */       } 
/*      */     } else {
/*      */       
/* 1003 */       mv.visitTypeInsn(189, arraytype.substring(1));
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
/*      */   public static void insertNumericUnboxOrPrimitiveTypeCoercion(MethodVisitor mv, @Nullable String stackDescriptor, char targetDescriptor) {
/* 1020 */     if (!isPrimitive(stackDescriptor)) {
/* 1021 */       insertUnboxNumberInsns(mv, targetDescriptor, stackDescriptor);
/*      */     } else {
/*      */       
/* 1024 */       insertAnyNecessaryTypeConversionBytecodes(mv, targetDescriptor, stackDescriptor);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static String toBoxedDescriptor(String primitiveDescriptor) {
/* 1029 */     switch (primitiveDescriptor.charAt(0)) { case 'I':
/* 1030 */         return "Ljava/lang/Integer";
/* 1031 */       case 'J': return "Ljava/lang/Long";
/* 1032 */       case 'F': return "Ljava/lang/Float";
/* 1033 */       case 'D': return "Ljava/lang/Double";
/* 1034 */       case 'B': return "Ljava/lang/Byte";
/* 1035 */       case 'C': return "Ljava/lang/Character";
/* 1036 */       case 'S': return "Ljava/lang/Short";
/* 1037 */       case 'Z': return "Ljava/lang/Boolean"; }
/*      */     
/* 1039 */     throw new IllegalArgumentException("Unexpected non primitive descriptor " + primitiveDescriptor);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface ClinitAdder {
/*      */     void generateCode(MethodVisitor param1MethodVisitor, CodeFlow param1CodeFlow);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface FieldAdder {
/*      */     void generateField(ClassWriter param1ClassWriter, CodeFlow param1CodeFlow);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\CodeFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */