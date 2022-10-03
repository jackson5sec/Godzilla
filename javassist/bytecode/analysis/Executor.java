/*      */ package javassist.bytecode.analysis;
/*      */ 
/*      */ import javassist.ClassPool;
/*      */ import javassist.CtClass;
/*      */ import javassist.NotFoundException;
/*      */ import javassist.bytecode.BadBytecode;
/*      */ import javassist.bytecode.CodeIterator;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.bytecode.MethodInfo;
/*      */ import javassist.bytecode.Opcode;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Executor
/*      */   implements Opcode
/*      */ {
/*      */   private final ConstPool constPool;
/*      */   private final ClassPool classPool;
/*      */   private final Type STRING_TYPE;
/*      */   private final Type CLASS_TYPE;
/*      */   private final Type THROWABLE_TYPE;
/*      */   private int lastPos;
/*      */   
/*      */   public Executor(ClassPool classPool, ConstPool constPool) {
/*   42 */     this.constPool = constPool;
/*   43 */     this.classPool = classPool;
/*      */     
/*      */     try {
/*   46 */       this.STRING_TYPE = getType("java.lang.String");
/*   47 */       this.CLASS_TYPE = getType("java.lang.Class");
/*   48 */       this.THROWABLE_TYPE = getType("java.lang.Throwable");
/*   49 */     } catch (Exception e) {
/*   50 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void execute(MethodInfo method, int pos, CodeIterator iter, Frame frame, Subroutine subroutine) throws BadBytecode {
/*      */     Type type;
/*      */     int end;
/*      */     Type type1;
/*      */     int index;
/*      */     Type array;
/*      */     int i, insert;
/*      */     Type type2;
/*      */     int j;
/*      */     Type type3, type4;
/*   68 */     this.lastPos = pos;
/*   69 */     int opcode = iter.byteAt(pos);
/*      */ 
/*      */ 
/*      */     
/*   73 */     switch (opcode) {
/*      */ 
/*      */       
/*      */       case 1:
/*   77 */         frame.push(Type.UNINIT);
/*      */         break;
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*   86 */         frame.push(Type.INTEGER);
/*      */         break;
/*      */       case 9:
/*      */       case 10:
/*   90 */         frame.push(Type.LONG);
/*   91 */         frame.push(Type.TOP);
/*      */         break;
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*   96 */         frame.push(Type.FLOAT);
/*      */         break;
/*      */       case 14:
/*      */       case 15:
/*  100 */         frame.push(Type.DOUBLE);
/*  101 */         frame.push(Type.TOP);
/*      */         break;
/*      */       case 16:
/*      */       case 17:
/*  105 */         frame.push(Type.INTEGER);
/*      */         break;
/*      */       case 18:
/*  108 */         evalLDC(iter.byteAt(pos + 1), frame);
/*      */         break;
/*      */       case 19:
/*      */       case 20:
/*  112 */         evalLDC(iter.u16bitAt(pos + 1), frame);
/*      */         break;
/*      */       case 21:
/*  115 */         evalLoad(Type.INTEGER, iter.byteAt(pos + 1), frame, subroutine);
/*      */         break;
/*      */       case 22:
/*  118 */         evalLoad(Type.LONG, iter.byteAt(pos + 1), frame, subroutine);
/*      */         break;
/*      */       case 23:
/*  121 */         evalLoad(Type.FLOAT, iter.byteAt(pos + 1), frame, subroutine);
/*      */         break;
/*      */       case 24:
/*  124 */         evalLoad(Type.DOUBLE, iter.byteAt(pos + 1), frame, subroutine);
/*      */         break;
/*      */       case 25:
/*  127 */         evalLoad(Type.OBJECT, iter.byteAt(pos + 1), frame, subroutine);
/*      */         break;
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*  133 */         evalLoad(Type.INTEGER, opcode - 26, frame, subroutine);
/*      */         break;
/*      */       case 30:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*  139 */         evalLoad(Type.LONG, opcode - 30, frame, subroutine);
/*      */         break;
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 37:
/*  145 */         evalLoad(Type.FLOAT, opcode - 34, frame, subroutine);
/*      */         break;
/*      */       case 38:
/*      */       case 39:
/*      */       case 40:
/*      */       case 41:
/*  151 */         evalLoad(Type.DOUBLE, opcode - 38, frame, subroutine);
/*      */         break;
/*      */       case 42:
/*      */       case 43:
/*      */       case 44:
/*      */       case 45:
/*  157 */         evalLoad(Type.OBJECT, opcode - 42, frame, subroutine);
/*      */         break;
/*      */       case 46:
/*  160 */         evalArrayLoad(Type.INTEGER, frame);
/*      */         break;
/*      */       case 47:
/*  163 */         evalArrayLoad(Type.LONG, frame);
/*      */         break;
/*      */       case 48:
/*  166 */         evalArrayLoad(Type.FLOAT, frame);
/*      */         break;
/*      */       case 49:
/*  169 */         evalArrayLoad(Type.DOUBLE, frame);
/*      */         break;
/*      */       case 50:
/*  172 */         evalArrayLoad(Type.OBJECT, frame);
/*      */         break;
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*  177 */         evalArrayLoad(Type.INTEGER, frame);
/*      */         break;
/*      */       case 54:
/*  180 */         evalStore(Type.INTEGER, iter.byteAt(pos + 1), frame, subroutine);
/*      */         break;
/*      */       case 55:
/*  183 */         evalStore(Type.LONG, iter.byteAt(pos + 1), frame, subroutine);
/*      */         break;
/*      */       case 56:
/*  186 */         evalStore(Type.FLOAT, iter.byteAt(pos + 1), frame, subroutine);
/*      */         break;
/*      */       case 57:
/*  189 */         evalStore(Type.DOUBLE, iter.byteAt(pos + 1), frame, subroutine);
/*      */         break;
/*      */       case 58:
/*  192 */         evalStore(Type.OBJECT, iter.byteAt(pos + 1), frame, subroutine);
/*      */         break;
/*      */       case 59:
/*      */       case 60:
/*      */       case 61:
/*      */       case 62:
/*  198 */         evalStore(Type.INTEGER, opcode - 59, frame, subroutine);
/*      */         break;
/*      */       case 63:
/*      */       case 64:
/*      */       case 65:
/*      */       case 66:
/*  204 */         evalStore(Type.LONG, opcode - 63, frame, subroutine);
/*      */         break;
/*      */       case 67:
/*      */       case 68:
/*      */       case 69:
/*      */       case 70:
/*  210 */         evalStore(Type.FLOAT, opcode - 67, frame, subroutine);
/*      */         break;
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*  216 */         evalStore(Type.DOUBLE, opcode - 71, frame, subroutine);
/*      */         break;
/*      */       case 75:
/*      */       case 76:
/*      */       case 77:
/*      */       case 78:
/*  222 */         evalStore(Type.OBJECT, opcode - 75, frame, subroutine);
/*      */         break;
/*      */       case 79:
/*  225 */         evalArrayStore(Type.INTEGER, frame);
/*      */         break;
/*      */       case 80:
/*  228 */         evalArrayStore(Type.LONG, frame);
/*      */         break;
/*      */       case 81:
/*  231 */         evalArrayStore(Type.FLOAT, frame);
/*      */         break;
/*      */       case 82:
/*  234 */         evalArrayStore(Type.DOUBLE, frame);
/*      */         break;
/*      */       case 83:
/*  237 */         evalArrayStore(Type.OBJECT, frame);
/*      */         break;
/*      */       case 84:
/*      */       case 85:
/*      */       case 86:
/*  242 */         evalArrayStore(Type.INTEGER, frame);
/*      */         break;
/*      */       case 87:
/*  245 */         if (frame.pop() == Type.TOP)
/*  246 */           throw new BadBytecode("POP can not be used with a category 2 value, pos = " + pos); 
/*      */         break;
/*      */       case 88:
/*  249 */         frame.pop();
/*  250 */         frame.pop();
/*      */         break;
/*      */       case 89:
/*  253 */         type = frame.peek();
/*  254 */         if (type == Type.TOP) {
/*  255 */           throw new BadBytecode("DUP can not be used with a category 2 value, pos = " + pos);
/*      */         }
/*  257 */         frame.push(frame.peek());
/*      */         break;
/*      */       
/*      */       case 90:
/*      */       case 91:
/*  262 */         type = frame.peek();
/*  263 */         if (type == Type.TOP)
/*  264 */           throw new BadBytecode("DUP can not be used with a category 2 value, pos = " + pos); 
/*  265 */         i = frame.getTopIndex();
/*  266 */         j = i - opcode - 90 - 1;
/*  267 */         frame.push(type);
/*      */         
/*  269 */         while (i > j) {
/*  270 */           frame.setStack(i, frame.getStack(i - 1));
/*  271 */           i--;
/*      */         } 
/*  273 */         frame.setStack(j, type);
/*      */         break;
/*      */       
/*      */       case 92:
/*  277 */         frame.push(frame.getStack(frame.getTopIndex() - 1));
/*  278 */         frame.push(frame.getStack(frame.getTopIndex() - 1));
/*      */         break;
/*      */       case 93:
/*      */       case 94:
/*  282 */         end = frame.getTopIndex();
/*  283 */         insert = end - opcode - 93 - 1;
/*  284 */         type3 = frame.getStack(frame.getTopIndex() - 1);
/*  285 */         type4 = frame.peek();
/*  286 */         frame.push(type3);
/*  287 */         frame.push(type4);
/*  288 */         while (end > insert) {
/*  289 */           frame.setStack(end, frame.getStack(end - 2));
/*  290 */           end--;
/*      */         } 
/*  292 */         frame.setStack(insert, type4);
/*  293 */         frame.setStack(insert - 1, type3);
/*      */         break;
/*      */       
/*      */       case 95:
/*  297 */         type1 = frame.pop();
/*  298 */         type2 = frame.pop();
/*  299 */         if (type1.getSize() == 2 || type2.getSize() == 2)
/*  300 */           throw new BadBytecode("Swap can not be used with category 2 values, pos = " + pos); 
/*  301 */         frame.push(type1);
/*  302 */         frame.push(type2);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 96:
/*  308 */         evalBinaryMath(Type.INTEGER, frame);
/*      */         break;
/*      */       case 97:
/*  311 */         evalBinaryMath(Type.LONG, frame);
/*      */         break;
/*      */       case 98:
/*  314 */         evalBinaryMath(Type.FLOAT, frame);
/*      */         break;
/*      */       case 99:
/*  317 */         evalBinaryMath(Type.DOUBLE, frame);
/*      */         break;
/*      */       case 100:
/*  320 */         evalBinaryMath(Type.INTEGER, frame);
/*      */         break;
/*      */       case 101:
/*  323 */         evalBinaryMath(Type.LONG, frame);
/*      */         break;
/*      */       case 102:
/*  326 */         evalBinaryMath(Type.FLOAT, frame);
/*      */         break;
/*      */       case 103:
/*  329 */         evalBinaryMath(Type.DOUBLE, frame);
/*      */         break;
/*      */       case 104:
/*  332 */         evalBinaryMath(Type.INTEGER, frame);
/*      */         break;
/*      */       case 105:
/*  335 */         evalBinaryMath(Type.LONG, frame);
/*      */         break;
/*      */       case 106:
/*  338 */         evalBinaryMath(Type.FLOAT, frame);
/*      */         break;
/*      */       case 107:
/*  341 */         evalBinaryMath(Type.DOUBLE, frame);
/*      */         break;
/*      */       case 108:
/*  344 */         evalBinaryMath(Type.INTEGER, frame);
/*      */         break;
/*      */       case 109:
/*  347 */         evalBinaryMath(Type.LONG, frame);
/*      */         break;
/*      */       case 110:
/*  350 */         evalBinaryMath(Type.FLOAT, frame);
/*      */         break;
/*      */       case 111:
/*  353 */         evalBinaryMath(Type.DOUBLE, frame);
/*      */         break;
/*      */       case 112:
/*  356 */         evalBinaryMath(Type.INTEGER, frame);
/*      */         break;
/*      */       case 113:
/*  359 */         evalBinaryMath(Type.LONG, frame);
/*      */         break;
/*      */       case 114:
/*  362 */         evalBinaryMath(Type.FLOAT, frame);
/*      */         break;
/*      */       case 115:
/*  365 */         evalBinaryMath(Type.DOUBLE, frame);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 116:
/*  370 */         verifyAssignable(Type.INTEGER, simplePeek(frame));
/*      */         break;
/*      */       case 117:
/*  373 */         verifyAssignable(Type.LONG, simplePeek(frame));
/*      */         break;
/*      */       case 118:
/*  376 */         verifyAssignable(Type.FLOAT, simplePeek(frame));
/*      */         break;
/*      */       case 119:
/*  379 */         verifyAssignable(Type.DOUBLE, simplePeek(frame));
/*      */         break;
/*      */ 
/*      */       
/*      */       case 120:
/*  384 */         evalShift(Type.INTEGER, frame);
/*      */         break;
/*      */       case 121:
/*  387 */         evalShift(Type.LONG, frame);
/*      */         break;
/*      */       case 122:
/*  390 */         evalShift(Type.INTEGER, frame);
/*      */         break;
/*      */       case 123:
/*  393 */         evalShift(Type.LONG, frame);
/*      */         break;
/*      */       case 124:
/*  396 */         evalShift(Type.INTEGER, frame);
/*      */         break;
/*      */       case 125:
/*  399 */         evalShift(Type.LONG, frame);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 126:
/*  404 */         evalBinaryMath(Type.INTEGER, frame);
/*      */         break;
/*      */       case 127:
/*  407 */         evalBinaryMath(Type.LONG, frame);
/*      */         break;
/*      */       case 128:
/*  410 */         evalBinaryMath(Type.INTEGER, frame);
/*      */         break;
/*      */       case 129:
/*  413 */         evalBinaryMath(Type.LONG, frame);
/*      */         break;
/*      */       case 130:
/*  416 */         evalBinaryMath(Type.INTEGER, frame);
/*      */         break;
/*      */       case 131:
/*  419 */         evalBinaryMath(Type.LONG, frame);
/*      */         break;
/*      */       
/*      */       case 132:
/*  423 */         index = iter.byteAt(pos + 1);
/*  424 */         verifyAssignable(Type.INTEGER, frame.getLocal(index));
/*  425 */         access(index, Type.INTEGER, subroutine);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 133:
/*  431 */         verifyAssignable(Type.INTEGER, simplePop(frame));
/*  432 */         simplePush(Type.LONG, frame);
/*      */         break;
/*      */       case 134:
/*  435 */         verifyAssignable(Type.INTEGER, simplePop(frame));
/*  436 */         simplePush(Type.FLOAT, frame);
/*      */         break;
/*      */       case 135:
/*  439 */         verifyAssignable(Type.INTEGER, simplePop(frame));
/*  440 */         simplePush(Type.DOUBLE, frame);
/*      */         break;
/*      */       case 136:
/*  443 */         verifyAssignable(Type.LONG, simplePop(frame));
/*  444 */         simplePush(Type.INTEGER, frame);
/*      */         break;
/*      */       case 137:
/*  447 */         verifyAssignable(Type.LONG, simplePop(frame));
/*  448 */         simplePush(Type.FLOAT, frame);
/*      */         break;
/*      */       case 138:
/*  451 */         verifyAssignable(Type.LONG, simplePop(frame));
/*  452 */         simplePush(Type.DOUBLE, frame);
/*      */         break;
/*      */       case 139:
/*  455 */         verifyAssignable(Type.FLOAT, simplePop(frame));
/*  456 */         simplePush(Type.INTEGER, frame);
/*      */         break;
/*      */       case 140:
/*  459 */         verifyAssignable(Type.FLOAT, simplePop(frame));
/*  460 */         simplePush(Type.LONG, frame);
/*      */         break;
/*      */       case 141:
/*  463 */         verifyAssignable(Type.FLOAT, simplePop(frame));
/*  464 */         simplePush(Type.DOUBLE, frame);
/*      */         break;
/*      */       case 142:
/*  467 */         verifyAssignable(Type.DOUBLE, simplePop(frame));
/*  468 */         simplePush(Type.INTEGER, frame);
/*      */         break;
/*      */       case 143:
/*  471 */         verifyAssignable(Type.DOUBLE, simplePop(frame));
/*  472 */         simplePush(Type.LONG, frame);
/*      */         break;
/*      */       case 144:
/*  475 */         verifyAssignable(Type.DOUBLE, simplePop(frame));
/*  476 */         simplePush(Type.FLOAT, frame);
/*      */         break;
/*      */       case 145:
/*      */       case 146:
/*      */       case 147:
/*  481 */         verifyAssignable(Type.INTEGER, frame.peek());
/*      */         break;
/*      */       case 148:
/*  484 */         verifyAssignable(Type.LONG, simplePop(frame));
/*  485 */         verifyAssignable(Type.LONG, simplePop(frame));
/*  486 */         frame.push(Type.INTEGER);
/*      */         break;
/*      */       case 149:
/*      */       case 150:
/*  490 */         verifyAssignable(Type.FLOAT, simplePop(frame));
/*  491 */         verifyAssignable(Type.FLOAT, simplePop(frame));
/*  492 */         frame.push(Type.INTEGER);
/*      */         break;
/*      */       case 151:
/*      */       case 152:
/*  496 */         verifyAssignable(Type.DOUBLE, simplePop(frame));
/*  497 */         verifyAssignable(Type.DOUBLE, simplePop(frame));
/*  498 */         frame.push(Type.INTEGER);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 153:
/*      */       case 154:
/*      */       case 155:
/*      */       case 156:
/*      */       case 157:
/*      */       case 158:
/*  508 */         verifyAssignable(Type.INTEGER, simplePop(frame));
/*      */         break;
/*      */       case 159:
/*      */       case 160:
/*      */       case 161:
/*      */       case 162:
/*      */       case 163:
/*      */       case 164:
/*  516 */         verifyAssignable(Type.INTEGER, simplePop(frame));
/*  517 */         verifyAssignable(Type.INTEGER, simplePop(frame));
/*      */         break;
/*      */       case 165:
/*      */       case 166:
/*  521 */         verifyAssignable(Type.OBJECT, simplePop(frame));
/*  522 */         verifyAssignable(Type.OBJECT, simplePop(frame));
/*      */         break;
/*      */ 
/*      */       
/*      */       case 168:
/*  527 */         frame.push(Type.RETURN_ADDRESS);
/*      */         break;
/*      */       case 169:
/*  530 */         verifyAssignable(Type.RETURN_ADDRESS, frame.getLocal(iter.byteAt(pos + 1)));
/*      */         break;
/*      */       case 170:
/*      */       case 171:
/*      */       case 172:
/*  535 */         verifyAssignable(Type.INTEGER, simplePop(frame));
/*      */         break;
/*      */       case 173:
/*  538 */         verifyAssignable(Type.LONG, simplePop(frame));
/*      */         break;
/*      */       case 174:
/*  541 */         verifyAssignable(Type.FLOAT, simplePop(frame));
/*      */         break;
/*      */       case 175:
/*  544 */         verifyAssignable(Type.DOUBLE, simplePop(frame));
/*      */         break;
/*      */       case 176:
/*      */         try {
/*  548 */           CtClass returnType = Descriptor.getReturnType(method.getDescriptor(), this.classPool);
/*  549 */           verifyAssignable(Type.get(returnType), simplePop(frame));
/*  550 */         } catch (NotFoundException e) {
/*  551 */           throw new RuntimeException(e);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 178:
/*  557 */         evalGetField(opcode, iter.u16bitAt(pos + 1), frame);
/*      */         break;
/*      */       case 179:
/*  560 */         evalPutField(opcode, iter.u16bitAt(pos + 1), frame);
/*      */         break;
/*      */       case 180:
/*  563 */         evalGetField(opcode, iter.u16bitAt(pos + 1), frame);
/*      */         break;
/*      */       case 181:
/*  566 */         evalPutField(opcode, iter.u16bitAt(pos + 1), frame);
/*      */         break;
/*      */       case 182:
/*      */       case 183:
/*      */       case 184:
/*  571 */         evalInvokeMethod(opcode, iter.u16bitAt(pos + 1), frame);
/*      */         break;
/*      */       case 185:
/*  574 */         evalInvokeIntfMethod(opcode, iter.u16bitAt(pos + 1), frame);
/*      */         break;
/*      */       case 186:
/*  577 */         evalInvokeDynamic(opcode, iter.u16bitAt(pos + 1), frame);
/*      */         break;
/*      */       case 187:
/*  580 */         frame.push(resolveClassInfo(this.constPool.getClassInfo(iter.u16bitAt(pos + 1))));
/*      */         break;
/*      */       case 188:
/*  583 */         evalNewArray(pos, iter, frame);
/*      */         break;
/*      */       case 189:
/*  586 */         evalNewObjectArray(pos, iter, frame);
/*      */         break;
/*      */       case 190:
/*  589 */         array = simplePop(frame);
/*  590 */         if (!array.isArray() && array != Type.UNINIT)
/*  591 */           throw new BadBytecode("Array length passed a non-array [pos = " + pos + "]: " + array); 
/*  592 */         frame.push(Type.INTEGER);
/*      */         break;
/*      */       
/*      */       case 191:
/*  596 */         verifyAssignable(this.THROWABLE_TYPE, simplePop(frame));
/*      */         break;
/*      */       case 192:
/*  599 */         verifyAssignable(Type.OBJECT, simplePop(frame));
/*  600 */         frame.push(typeFromDesc(this.constPool.getClassInfoByDescriptor(iter.u16bitAt(pos + 1))));
/*      */         break;
/*      */       case 193:
/*  603 */         verifyAssignable(Type.OBJECT, simplePop(frame));
/*  604 */         frame.push(Type.INTEGER);
/*      */         break;
/*      */       case 194:
/*      */       case 195:
/*  608 */         verifyAssignable(Type.OBJECT, simplePop(frame));
/*      */         break;
/*      */       case 196:
/*  611 */         evalWide(pos, iter, frame, subroutine);
/*      */         break;
/*      */       case 197:
/*  614 */         evalNewObjectArray(pos, iter, frame);
/*      */         break;
/*      */       case 198:
/*      */       case 199:
/*  618 */         verifyAssignable(Type.OBJECT, simplePop(frame));
/*      */         break;
/*      */ 
/*      */       
/*      */       case 201:
/*  623 */         frame.push(Type.RETURN_ADDRESS);
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private Type zeroExtend(Type type) {
/*  629 */     if (type == Type.SHORT || type == Type.BYTE || type == Type.CHAR || type == Type.BOOLEAN) {
/*  630 */       return Type.INTEGER;
/*      */     }
/*  632 */     return type;
/*      */   }
/*      */   
/*      */   private void evalArrayLoad(Type expectedComponent, Frame frame) throws BadBytecode {
/*  636 */     Type index = frame.pop();
/*  637 */     Type array = frame.pop();
/*      */ 
/*      */ 
/*      */     
/*  641 */     if (array == Type.UNINIT) {
/*  642 */       verifyAssignable(Type.INTEGER, index);
/*  643 */       if (expectedComponent == Type.OBJECT) {
/*  644 */         simplePush(Type.UNINIT, frame);
/*      */       } else {
/*  646 */         simplePush(expectedComponent, frame);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*  651 */     Type component = array.getComponent();
/*      */     
/*  653 */     if (component == null) {
/*  654 */       throw new BadBytecode("Not an array! [pos = " + this.lastPos + "]: " + component);
/*      */     }
/*  656 */     component = zeroExtend(component);
/*      */     
/*  658 */     verifyAssignable(expectedComponent, component);
/*  659 */     verifyAssignable(Type.INTEGER, index);
/*  660 */     simplePush(component, frame);
/*      */   }
/*      */   
/*      */   private void evalArrayStore(Type expectedComponent, Frame frame) throws BadBytecode {
/*  664 */     Type value = simplePop(frame);
/*  665 */     Type index = frame.pop();
/*  666 */     Type array = frame.pop();
/*      */     
/*  668 */     if (array == Type.UNINIT) {
/*  669 */       verifyAssignable(Type.INTEGER, index);
/*      */       
/*      */       return;
/*      */     } 
/*  673 */     Type component = array.getComponent();
/*      */     
/*  675 */     if (component == null) {
/*  676 */       throw new BadBytecode("Not an array! [pos = " + this.lastPos + "]: " + component);
/*      */     }
/*  678 */     component = zeroExtend(component);
/*      */     
/*  680 */     verifyAssignable(expectedComponent, component);
/*  681 */     verifyAssignable(Type.INTEGER, index);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  689 */     if (expectedComponent == Type.OBJECT) {
/*  690 */       verifyAssignable(expectedComponent, value);
/*      */     } else {
/*  692 */       verifyAssignable(component, value);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void evalBinaryMath(Type expected, Frame frame) throws BadBytecode {
/*  697 */     Type value2 = simplePop(frame);
/*  698 */     Type value1 = simplePop(frame);
/*      */     
/*  700 */     verifyAssignable(expected, value2);
/*  701 */     verifyAssignable(expected, value1);
/*  702 */     simplePush(value1, frame);
/*      */   }
/*      */   
/*      */   private void evalGetField(int opcode, int index, Frame frame) throws BadBytecode {
/*  706 */     String desc = this.constPool.getFieldrefType(index);
/*  707 */     Type type = zeroExtend(typeFromDesc(desc));
/*      */     
/*  709 */     if (opcode == 180) {
/*  710 */       Type objectType = resolveClassInfo(this.constPool.getFieldrefClassName(index));
/*  711 */       verifyAssignable(objectType, simplePop(frame));
/*      */     } 
/*      */     
/*  714 */     simplePush(type, frame);
/*      */   }
/*      */   
/*      */   private void evalInvokeIntfMethod(int opcode, int index, Frame frame) throws BadBytecode {
/*  718 */     String desc = this.constPool.getInterfaceMethodrefType(index);
/*  719 */     Type[] types = paramTypesFromDesc(desc);
/*  720 */     int i = types.length;
/*      */     
/*  722 */     while (i > 0) {
/*  723 */       verifyAssignable(zeroExtend(types[--i]), simplePop(frame));
/*      */     }
/*  725 */     String classInfo = this.constPool.getInterfaceMethodrefClassName(index);
/*  726 */     Type objectType = resolveClassInfo(classInfo);
/*  727 */     verifyAssignable(objectType, simplePop(frame));
/*      */     
/*  729 */     Type returnType = returnTypeFromDesc(desc);
/*  730 */     if (returnType != Type.VOID)
/*  731 */       simplePush(zeroExtend(returnType), frame); 
/*      */   }
/*      */   
/*      */   private void evalInvokeMethod(int opcode, int index, Frame frame) throws BadBytecode {
/*  735 */     String desc = this.constPool.getMethodrefType(index);
/*  736 */     Type[] types = paramTypesFromDesc(desc);
/*  737 */     int i = types.length;
/*      */     
/*  739 */     while (i > 0) {
/*  740 */       verifyAssignable(zeroExtend(types[--i]), simplePop(frame));
/*      */     }
/*  742 */     if (opcode != 184) {
/*  743 */       Type objectType = resolveClassInfo(this.constPool.getMethodrefClassName(index));
/*  744 */       verifyAssignable(objectType, simplePop(frame));
/*      */     } 
/*      */     
/*  747 */     Type returnType = returnTypeFromDesc(desc);
/*  748 */     if (returnType != Type.VOID)
/*  749 */       simplePush(zeroExtend(returnType), frame); 
/*      */   }
/*      */   
/*      */   private void evalInvokeDynamic(int opcode, int index, Frame frame) throws BadBytecode {
/*  753 */     String desc = this.constPool.getInvokeDynamicType(index);
/*  754 */     Type[] types = paramTypesFromDesc(desc);
/*  755 */     int i = types.length;
/*      */     
/*  757 */     while (i > 0) {
/*  758 */       verifyAssignable(zeroExtend(types[--i]), simplePop(frame));
/*      */     }
/*      */ 
/*      */     
/*  762 */     Type returnType = returnTypeFromDesc(desc);
/*  763 */     if (returnType != Type.VOID)
/*  764 */       simplePush(zeroExtend(returnType), frame); 
/*      */   }
/*      */   private void evalLDC(int index, Frame frame) throws BadBytecode {
/*      */     Type type;
/*  768 */     int tag = this.constPool.getTag(index);
/*      */     
/*  770 */     switch (tag) {
/*      */       case 8:
/*  772 */         type = this.STRING_TYPE;
/*      */         break;
/*      */       case 3:
/*  775 */         type = Type.INTEGER;
/*      */         break;
/*      */       case 4:
/*  778 */         type = Type.FLOAT;
/*      */         break;
/*      */       case 5:
/*  781 */         type = Type.LONG;
/*      */         break;
/*      */       case 6:
/*  784 */         type = Type.DOUBLE;
/*      */         break;
/*      */       case 7:
/*  787 */         type = this.CLASS_TYPE;
/*      */         break;
/*      */       default:
/*  790 */         throw new BadBytecode("bad LDC [pos = " + this.lastPos + "]: " + tag);
/*      */     } 
/*      */     
/*  793 */     simplePush(type, frame);
/*      */   }
/*      */   
/*      */   private void evalLoad(Type expected, int index, Frame frame, Subroutine subroutine) throws BadBytecode {
/*  797 */     Type type = frame.getLocal(index);
/*      */     
/*  799 */     verifyAssignable(expected, type);
/*      */     
/*  801 */     simplePush(type, frame);
/*  802 */     access(index, type, subroutine);
/*      */   }
/*      */   
/*      */   private void evalNewArray(int pos, CodeIterator iter, Frame frame) throws BadBytecode {
/*  806 */     verifyAssignable(Type.INTEGER, simplePop(frame));
/*  807 */     Type type = null;
/*  808 */     int typeInfo = iter.byteAt(pos + 1);
/*  809 */     switch (typeInfo) {
/*      */       case 4:
/*  811 */         type = getType("boolean[]");
/*      */         break;
/*      */       case 5:
/*  814 */         type = getType("char[]");
/*      */         break;
/*      */       case 8:
/*  817 */         type = getType("byte[]");
/*      */         break;
/*      */       case 9:
/*  820 */         type = getType("short[]");
/*      */         break;
/*      */       case 10:
/*  823 */         type = getType("int[]");
/*      */         break;
/*      */       case 11:
/*  826 */         type = getType("long[]");
/*      */         break;
/*      */       case 6:
/*  829 */         type = getType("float[]");
/*      */         break;
/*      */       case 7:
/*  832 */         type = getType("double[]");
/*      */         break;
/*      */       default:
/*  835 */         throw new BadBytecode("Invalid array type [pos = " + pos + "]: " + typeInfo);
/*      */     } 
/*      */ 
/*      */     
/*  839 */     frame.push(type);
/*      */   }
/*      */   
/*      */   private void evalNewObjectArray(int pos, CodeIterator iter, Frame frame) throws BadBytecode {
/*      */     int dimensions;
/*  844 */     Type type = resolveClassInfo(this.constPool.getClassInfo(iter.u16bitAt(pos + 1)));
/*  845 */     String name = type.getCtClass().getName();
/*  846 */     int opcode = iter.byteAt(pos);
/*      */ 
/*      */     
/*  849 */     if (opcode == 197) {
/*  850 */       dimensions = iter.byteAt(pos + 3);
/*      */     } else {
/*  852 */       name = name + "[]";
/*  853 */       dimensions = 1;
/*      */     } 
/*      */     
/*  856 */     while (dimensions-- > 0) {
/*  857 */       verifyAssignable(Type.INTEGER, simplePop(frame));
/*      */     }
/*      */     
/*  860 */     simplePush(getType(name), frame);
/*      */   }
/*      */   
/*      */   private void evalPutField(int opcode, int index, Frame frame) throws BadBytecode {
/*  864 */     String desc = this.constPool.getFieldrefType(index);
/*  865 */     Type type = zeroExtend(typeFromDesc(desc));
/*      */     
/*  867 */     verifyAssignable(type, simplePop(frame));
/*      */     
/*  869 */     if (opcode == 181) {
/*  870 */       Type objectType = resolveClassInfo(this.constPool.getFieldrefClassName(index));
/*  871 */       verifyAssignable(objectType, simplePop(frame));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void evalShift(Type expected, Frame frame) throws BadBytecode {
/*  876 */     Type value2 = simplePop(frame);
/*  877 */     Type value1 = simplePop(frame);
/*      */     
/*  879 */     verifyAssignable(Type.INTEGER, value2);
/*  880 */     verifyAssignable(expected, value1);
/*  881 */     simplePush(value1, frame);
/*      */   }
/*      */   
/*      */   private void evalStore(Type expected, int index, Frame frame, Subroutine subroutine) throws BadBytecode {
/*  885 */     Type type = simplePop(frame);
/*      */ 
/*      */     
/*  888 */     if (expected != Type.OBJECT || type != Type.RETURN_ADDRESS)
/*  889 */       verifyAssignable(expected, type); 
/*  890 */     simpleSetLocal(index, type, frame);
/*  891 */     access(index, type, subroutine);
/*      */   }
/*      */   
/*      */   private void evalWide(int pos, CodeIterator iter, Frame frame, Subroutine subroutine) throws BadBytecode {
/*  895 */     int opcode = iter.byteAt(pos + 1);
/*  896 */     int index = iter.u16bitAt(pos + 2);
/*  897 */     switch (opcode) {
/*      */       case 21:
/*  899 */         evalLoad(Type.INTEGER, index, frame, subroutine);
/*      */         return;
/*      */       case 22:
/*  902 */         evalLoad(Type.LONG, index, frame, subroutine);
/*      */         return;
/*      */       case 23:
/*  905 */         evalLoad(Type.FLOAT, index, frame, subroutine);
/*      */         return;
/*      */       case 24:
/*  908 */         evalLoad(Type.DOUBLE, index, frame, subroutine);
/*      */         return;
/*      */       case 25:
/*  911 */         evalLoad(Type.OBJECT, index, frame, subroutine);
/*      */         return;
/*      */       case 54:
/*  914 */         evalStore(Type.INTEGER, index, frame, subroutine);
/*      */         return;
/*      */       case 55:
/*  917 */         evalStore(Type.LONG, index, frame, subroutine);
/*      */         return;
/*      */       case 56:
/*  920 */         evalStore(Type.FLOAT, index, frame, subroutine);
/*      */         return;
/*      */       case 57:
/*  923 */         evalStore(Type.DOUBLE, index, frame, subroutine);
/*      */         return;
/*      */       case 58:
/*  926 */         evalStore(Type.OBJECT, index, frame, subroutine);
/*      */         return;
/*      */       case 132:
/*  929 */         verifyAssignable(Type.INTEGER, frame.getLocal(index));
/*      */         return;
/*      */       case 169:
/*  932 */         verifyAssignable(Type.RETURN_ADDRESS, frame.getLocal(index));
/*      */         return;
/*      */     } 
/*  935 */     throw new BadBytecode("Invalid WIDE operand [pos = " + pos + "]: " + opcode);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Type getType(String name) throws BadBytecode {
/*      */     try {
/*  942 */       return Type.get(this.classPool.get(name));
/*  943 */     } catch (NotFoundException e) {
/*  944 */       throw new BadBytecode("Could not find class [pos = " + this.lastPos + "]: " + name);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Type[] paramTypesFromDesc(String desc) throws BadBytecode {
/*  949 */     CtClass[] classes = null;
/*      */     try {
/*  951 */       classes = Descriptor.getParameterTypes(desc, this.classPool);
/*  952 */     } catch (NotFoundException e) {
/*  953 */       throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + e.getMessage());
/*      */     } 
/*      */     
/*  956 */     if (classes == null) {
/*  957 */       throw new BadBytecode("Could not obtain parameters for descriptor [pos = " + this.lastPos + "]: " + desc);
/*      */     }
/*  959 */     Type[] types = new Type[classes.length];
/*  960 */     for (int i = 0; i < types.length; i++) {
/*  961 */       types[i] = Type.get(classes[i]);
/*      */     }
/*  963 */     return types;
/*      */   }
/*      */   
/*      */   private Type returnTypeFromDesc(String desc) throws BadBytecode {
/*  967 */     CtClass clazz = null;
/*      */     try {
/*  969 */       clazz = Descriptor.getReturnType(desc, this.classPool);
/*  970 */     } catch (NotFoundException e) {
/*  971 */       throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + e.getMessage());
/*      */     } 
/*      */     
/*  974 */     if (clazz == null) {
/*  975 */       throw new BadBytecode("Could not obtain return type for descriptor [pos = " + this.lastPos + "]: " + desc);
/*      */     }
/*  977 */     return Type.get(clazz);
/*      */   }
/*      */   
/*      */   private Type simplePeek(Frame frame) {
/*  981 */     Type type = frame.peek();
/*  982 */     return (type == Type.TOP) ? frame.getStack(frame.getTopIndex() - 1) : type;
/*      */   }
/*      */   
/*      */   private Type simplePop(Frame frame) {
/*  986 */     Type type = frame.pop();
/*  987 */     return (type == Type.TOP) ? frame.pop() : type;
/*      */   }
/*      */   
/*      */   private void simplePush(Type type, Frame frame) {
/*  991 */     frame.push(type);
/*  992 */     if (type.getSize() == 2)
/*  993 */       frame.push(Type.TOP); 
/*      */   }
/*      */   
/*      */   private void access(int index, Type type, Subroutine subroutine) {
/*  997 */     if (subroutine == null)
/*      */       return; 
/*  999 */     subroutine.access(index);
/* 1000 */     if (type.getSize() == 2)
/* 1001 */       subroutine.access(index + 1); 
/*      */   }
/*      */   
/*      */   private void simpleSetLocal(int index, Type type, Frame frame) {
/* 1005 */     frame.setLocal(index, type);
/* 1006 */     if (type.getSize() == 2)
/* 1007 */       frame.setLocal(index + 1, Type.TOP); 
/*      */   }
/*      */   
/*      */   private Type resolveClassInfo(String info) throws BadBytecode {
/* 1011 */     CtClass clazz = null;
/*      */     try {
/* 1013 */       if (info.charAt(0) == '[') {
/* 1014 */         clazz = Descriptor.toCtClass(info, this.classPool);
/*      */       } else {
/* 1016 */         clazz = this.classPool.get(info);
/*      */       }
/*      */     
/* 1019 */     } catch (NotFoundException e) {
/* 1020 */       throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + e.getMessage());
/*      */     } 
/*      */     
/* 1023 */     if (clazz == null) {
/* 1024 */       throw new BadBytecode("Could not obtain type for descriptor [pos = " + this.lastPos + "]: " + info);
/*      */     }
/* 1026 */     return Type.get(clazz);
/*      */   }
/*      */   
/*      */   private Type typeFromDesc(String desc) throws BadBytecode {
/* 1030 */     CtClass clazz = null;
/*      */     try {
/* 1032 */       clazz = Descriptor.toCtClass(desc, this.classPool);
/* 1033 */     } catch (NotFoundException e) {
/* 1034 */       throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + e.getMessage());
/*      */     } 
/*      */     
/* 1037 */     if (clazz == null) {
/* 1038 */       throw new BadBytecode("Could not obtain type for descriptor [pos = " + this.lastPos + "]: " + desc);
/*      */     }
/* 1040 */     return Type.get(clazz);
/*      */   }
/*      */   
/*      */   private void verifyAssignable(Type expected, Type type) throws BadBytecode {
/* 1044 */     if (!expected.isAssignableFrom(type))
/* 1045 */       throw new BadBytecode("Expected type: " + expected + " Got: " + type + " [pos = " + this.lastPos + "]"); 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\Executor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */