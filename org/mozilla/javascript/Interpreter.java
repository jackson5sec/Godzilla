/*      */ package org.mozilla.javascript;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import org.mozilla.javascript.ast.ScriptNode;
/*      */ import org.mozilla.javascript.debug.DebugFrame;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Interpreter
/*      */   extends Icode
/*      */   implements Evaluator
/*      */ {
/*      */   InterpreterData itsData;
/*      */   static final int EXCEPTION_TRY_START_SLOT = 0;
/*      */   static final int EXCEPTION_TRY_END_SLOT = 1;
/*      */   static final int EXCEPTION_HANDLER_SLOT = 2;
/*      */   static final int EXCEPTION_TYPE_SLOT = 3;
/*      */   static final int EXCEPTION_LOCAL_SLOT = 4;
/*      */   static final int EXCEPTION_SCOPE_SLOT = 5;
/*      */   static final int EXCEPTION_SLOT_SIZE = 6;
/*      */   
/*      */   private static class CallFrame
/*      */     implements Cloneable, Serializable
/*      */   {
/*      */     static final long serialVersionUID = -2843792508994958978L;
/*      */     CallFrame parentFrame;
/*      */     int frameIndex;
/*      */     boolean frozen;
/*      */     InterpretedFunction fnOrScript;
/*      */     InterpreterData idata;
/*      */     Object[] stack;
/*      */     int[] stackAttributes;
/*      */     double[] sDbl;
/*      */     CallFrame varSource;
/*      */     int localShift;
/*      */     int emptyStackTop;
/*      */     DebugFrame debuggerFrame;
/*      */     boolean useActivation;
/*      */     boolean isContinuationsTopFrame;
/*      */     Scriptable thisObj;
/*      */     Object result;
/*      */     double resultDbl;
/*      */     int pc;
/*      */     int pcPrevBranch;
/*      */     int pcSourceLineStart;
/*      */     Scriptable scope;
/*      */     int savedStackTop;
/*      */     int savedCallOp;
/*      */     Object throwable;
/*      */     
/*      */     private CallFrame() {}
/*      */     
/*      */     CallFrame cloneFrozen() {
/*      */       CallFrame copy;
/*   86 */       if (!this.frozen) Kit.codeBug();
/*      */ 
/*      */       
/*      */       try {
/*   90 */         copy = (CallFrame)clone();
/*   91 */       } catch (CloneNotSupportedException ex) {
/*   92 */         throw new IllegalStateException();
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*   98 */       copy.stack = (Object[])this.stack.clone();
/*   99 */       copy.stackAttributes = (int[])this.stackAttributes.clone();
/*  100 */       copy.sDbl = (double[])this.sDbl.clone();
/*      */       
/*  102 */       copy.frozen = false;
/*  103 */       return copy;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class ContinuationJump
/*      */     implements Serializable
/*      */   {
/*      */     static final long serialVersionUID = 7687739156004308247L;
/*      */     Interpreter.CallFrame capturedFrame;
/*      */     Interpreter.CallFrame branchFrame;
/*      */     Object result;
/*      */     double resultDbl;
/*      */     
/*      */     ContinuationJump(NativeContinuation c, Interpreter.CallFrame current) {
/*  118 */       this.capturedFrame = (Interpreter.CallFrame)c.getImplementation();
/*  119 */       if (this.capturedFrame == null || current == null) {
/*      */ 
/*      */ 
/*      */         
/*  123 */         this.branchFrame = null;
/*      */       }
/*      */       else {
/*      */         
/*  127 */         Interpreter.CallFrame chain1 = this.capturedFrame;
/*  128 */         Interpreter.CallFrame chain2 = current;
/*      */ 
/*      */ 
/*      */         
/*  132 */         int diff = chain1.frameIndex - chain2.frameIndex;
/*  133 */         if (diff != 0) {
/*  134 */           if (diff < 0) {
/*      */ 
/*      */             
/*  137 */             chain1 = current;
/*  138 */             chain2 = this.capturedFrame;
/*  139 */             diff = -diff;
/*      */           } 
/*      */           while (true) {
/*  142 */             chain1 = chain1.parentFrame;
/*  143 */             if (--diff == 0) {
/*  144 */               if (chain1.frameIndex != chain2.frameIndex) Kit.codeBug(); 
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*  149 */         while (chain1 != chain2 && chain1 != null) {
/*  150 */           chain1 = chain1.parentFrame;
/*  151 */           chain2 = chain2.parentFrame;
/*      */         } 
/*      */         
/*  154 */         this.branchFrame = chain1;
/*  155 */         if (this.branchFrame != null && !this.branchFrame.frozen)
/*  156 */           Kit.codeBug(); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static CallFrame captureFrameForGenerator(CallFrame frame) {
/*  162 */     frame.frozen = true;
/*  163 */     CallFrame result = frame.cloneFrozen();
/*  164 */     frame.frozen = false;
/*      */ 
/*      */     
/*  167 */     result.parentFrame = null;
/*  168 */     result.frameIndex = 0;
/*      */     
/*  170 */     return result;
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
/*      */   public Object compile(CompilerEnvirons compilerEnv, ScriptNode tree, String encodedSource, boolean returnFunction) {
/*  193 */     CodeGenerator cgen = new CodeGenerator();
/*  194 */     this.itsData = cgen.compile(compilerEnv, tree, encodedSource, returnFunction);
/*  195 */     return this.itsData;
/*      */   }
/*      */ 
/*      */   
/*      */   public Script createScriptObject(Object bytecode, Object staticSecurityDomain) {
/*  200 */     if (bytecode != this.itsData)
/*      */     {
/*  202 */       Kit.codeBug();
/*      */     }
/*  204 */     return InterpretedFunction.createScript(this.itsData, staticSecurityDomain);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEvalScriptFlag(Script script) {
/*  209 */     ((InterpretedFunction)script).idata.evalScriptFlag = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Function createFunctionObject(Context cx, Scriptable scope, Object bytecode, Object staticSecurityDomain) {
/*  216 */     if (bytecode != this.itsData)
/*      */     {
/*  218 */       Kit.codeBug();
/*      */     }
/*  220 */     return InterpretedFunction.createFunction(cx, scope, this.itsData, staticSecurityDomain);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getShort(byte[] iCode, int pc) {
/*  225 */     return iCode[pc] << 8 | iCode[pc + 1] & 0xFF;
/*      */   }
/*      */   
/*      */   private static int getIndex(byte[] iCode, int pc) {
/*  229 */     return (iCode[pc] & 0xFF) << 8 | iCode[pc + 1] & 0xFF;
/*      */   }
/*      */   
/*      */   private static int getInt(byte[] iCode, int pc) {
/*  233 */     return iCode[pc] << 24 | (iCode[pc + 1] & 0xFF) << 16 | (iCode[pc + 2] & 0xFF) << 8 | iCode[pc + 3] & 0xFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getExceptionHandler(CallFrame frame, boolean onlyFinally) {
/*  240 */     int[] exceptionTable = frame.idata.itsExceptionTable;
/*  241 */     if (exceptionTable == null)
/*      */     {
/*  243 */       return -1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  249 */     int pc = frame.pc - 1;
/*      */ 
/*      */     
/*  252 */     int best = -1, bestStart = 0, bestEnd = 0;
/*  253 */     for (int i = 0; i != exceptionTable.length; i += 6) {
/*  254 */       int start = exceptionTable[i + 0];
/*  255 */       int end = exceptionTable[i + 1];
/*  256 */       if (start > pc || pc >= end) {
/*      */         continue;
/*      */       }
/*  259 */       if (onlyFinally && exceptionTable[i + 3] != 1) {
/*      */         continue;
/*      */       }
/*  262 */       if (best >= 0) {
/*      */ 
/*      */ 
/*      */         
/*  266 */         if (bestEnd < end) {
/*      */           continue;
/*      */         }
/*      */         
/*  270 */         if (bestStart > start) Kit.codeBug(); 
/*  271 */         if (bestEnd == end) Kit.codeBug(); 
/*      */       } 
/*  273 */       best = i;
/*  274 */       bestStart = start;
/*  275 */       bestEnd = end; continue;
/*      */     } 
/*  277 */     return best;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void dumpICode(InterpreterData idata) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int bytecodeSpan(int bytecode) {
/*  493 */     switch (bytecode) {
/*      */       
/*      */       case -63:
/*      */       case -62:
/*      */       case 50:
/*      */       case 72:
/*  499 */         return 3;
/*      */ 
/*      */       
/*      */       case -54:
/*      */       case -23:
/*      */       case -6:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*  508 */         return 3;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case -21:
/*  514 */         return 5;
/*      */ 
/*      */       
/*      */       case 57:
/*  518 */         return 2;
/*      */ 
/*      */       
/*      */       case -11:
/*      */       case -10:
/*      */       case -9:
/*      */       case -8:
/*      */       case -7:
/*  526 */         return 2;
/*      */ 
/*      */       
/*      */       case -27:
/*  530 */         return 3;
/*      */ 
/*      */       
/*      */       case -28:
/*  534 */         return 5;
/*      */ 
/*      */       
/*      */       case -38:
/*  538 */         return 2;
/*      */ 
/*      */       
/*      */       case -39:
/*  542 */         return 3;
/*      */ 
/*      */       
/*      */       case -40:
/*  546 */         return 5;
/*      */ 
/*      */       
/*      */       case -45:
/*  550 */         return 2;
/*      */ 
/*      */       
/*      */       case -46:
/*  554 */         return 3;
/*      */ 
/*      */       
/*      */       case -47:
/*  558 */         return 5;
/*      */ 
/*      */       
/*      */       case -61:
/*      */       case -49:
/*      */       case -48:
/*  564 */         return 2;
/*      */ 
/*      */       
/*      */       case -26:
/*  568 */         return 3;
/*      */     } 
/*  570 */     if (!validBytecode(bytecode)) throw Kit.codeBug(); 
/*  571 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   static int[] getLineNumbers(InterpreterData data) {
/*  576 */     UintMap presentLines = new UintMap();
/*      */     
/*  578 */     byte[] iCode = data.itsICode;
/*  579 */     int iCodeLength = iCode.length; int pc;
/*  580 */     for (pc = 0; pc != iCodeLength; ) {
/*  581 */       int bytecode = iCode[pc];
/*  582 */       int span = bytecodeSpan(bytecode);
/*  583 */       if (bytecode == -26) {
/*  584 */         if (span != 3) Kit.codeBug(); 
/*  585 */         int line = getIndex(iCode, pc + 1);
/*  586 */         presentLines.put(line, 0);
/*      */       } 
/*  588 */       pc += span;
/*      */     } 
/*      */     
/*  591 */     return presentLines.getKeys();
/*      */   }
/*      */   
/*      */   public void captureStackInfo(RhinoException ex) {
/*      */     CallFrame[] array;
/*  596 */     Context cx = Context.getCurrentContext();
/*  597 */     if (cx == null || cx.lastInterpreterFrame == null) {
/*      */       
/*  599 */       ex.interpreterStackInfo = null;
/*  600 */       ex.interpreterLineData = null;
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  605 */     if (cx.previousInterpreterInvocations == null || cx.previousInterpreterInvocations.size() == 0) {
/*      */ 
/*      */       
/*  608 */       array = new CallFrame[1];
/*      */     } else {
/*  610 */       int previousCount = cx.previousInterpreterInvocations.size();
/*  611 */       if (cx.previousInterpreterInvocations.peek() == cx.lastInterpreterFrame)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  618 */         previousCount--;
/*      */       }
/*  620 */       array = new CallFrame[previousCount + 1];
/*  621 */       cx.previousInterpreterInvocations.toArray((Object[])array);
/*      */     } 
/*  623 */     array[array.length - 1] = (CallFrame)cx.lastInterpreterFrame;
/*      */     
/*  625 */     int interpreterFrameCount = 0;
/*  626 */     for (int i = 0; i != array.length; i++) {
/*  627 */       interpreterFrameCount += 1 + (array[i]).frameIndex;
/*      */     }
/*      */     
/*  630 */     int[] linePC = new int[interpreterFrameCount];
/*      */ 
/*      */     
/*  633 */     int linePCIndex = interpreterFrameCount;
/*  634 */     for (int j = array.length; j != 0; ) {
/*  635 */       j--;
/*  636 */       CallFrame frame = array[j];
/*  637 */       while (frame != null) {
/*  638 */         linePCIndex--;
/*  639 */         linePC[linePCIndex] = frame.pcSourceLineStart;
/*  640 */         frame = frame.parentFrame;
/*      */       } 
/*      */     } 
/*  643 */     if (linePCIndex != 0) Kit.codeBug();
/*      */     
/*  645 */     ex.interpreterStackInfo = array;
/*  646 */     ex.interpreterLineData = linePC;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getSourcePositionFromStack(Context cx, int[] linep) {
/*  651 */     CallFrame frame = (CallFrame)cx.lastInterpreterFrame;
/*  652 */     InterpreterData idata = frame.idata;
/*  653 */     if (frame.pcSourceLineStart >= 0) {
/*  654 */       linep[0] = getIndex(idata.itsICode, frame.pcSourceLineStart);
/*      */     } else {
/*  656 */       linep[0] = 0;
/*      */     } 
/*  658 */     return idata.itsSourceFile;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPatchedStack(RhinoException ex, String nativeStackTrace) {
/*  664 */     String tag = "org.mozilla.javascript.Interpreter.interpretLoop";
/*  665 */     StringBuilder sb = new StringBuilder(nativeStackTrace.length() + 1000);
/*  666 */     String lineSeparator = SecurityUtilities.getSystemProperty("line.separator");
/*      */     
/*  668 */     CallFrame[] array = (CallFrame[])ex.interpreterStackInfo;
/*  669 */     int[] linePC = ex.interpreterLineData;
/*  670 */     int arrayIndex = array.length;
/*  671 */     int linePCIndex = linePC.length;
/*  672 */     int offset = 0;
/*  673 */     while (arrayIndex != 0) {
/*  674 */       arrayIndex--;
/*  675 */       int pos = nativeStackTrace.indexOf(tag, offset);
/*  676 */       if (pos < 0) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  681 */       pos += tag.length();
/*      */       
/*  683 */       for (; pos != nativeStackTrace.length(); pos++) {
/*  684 */         char c = nativeStackTrace.charAt(pos);
/*  685 */         if (c == '\n' || c == '\r') {
/*      */           break;
/*      */         }
/*      */       } 
/*  689 */       sb.append(nativeStackTrace.substring(offset, pos));
/*  690 */       offset = pos;
/*      */       
/*  692 */       CallFrame frame = array[arrayIndex];
/*  693 */       while (frame != null) {
/*  694 */         if (linePCIndex == 0) Kit.codeBug(); 
/*  695 */         linePCIndex--;
/*  696 */         InterpreterData idata = frame.idata;
/*  697 */         sb.append(lineSeparator);
/*  698 */         sb.append("\tat script");
/*  699 */         if (idata.itsName != null && idata.itsName.length() != 0) {
/*  700 */           sb.append('.');
/*  701 */           sb.append(idata.itsName);
/*      */         } 
/*  703 */         sb.append('(');
/*  704 */         sb.append(idata.itsSourceFile);
/*  705 */         int pc = linePC[linePCIndex];
/*  706 */         if (pc >= 0) {
/*      */           
/*  708 */           sb.append(':');
/*  709 */           sb.append(getIndex(idata.itsICode, pc));
/*      */         } 
/*  711 */         sb.append(')');
/*  712 */         frame = frame.parentFrame;
/*      */       } 
/*      */     } 
/*  715 */     sb.append(nativeStackTrace.substring(offset));
/*      */     
/*  717 */     return sb.toString();
/*      */   }
/*      */   
/*      */   public List<String> getScriptStack(RhinoException ex) {
/*  721 */     ScriptStackElement[][] stack = getScriptStackElements(ex);
/*  722 */     List<String> list = new ArrayList<String>(stack.length);
/*  723 */     String lineSeparator = SecurityUtilities.getSystemProperty("line.separator");
/*      */     
/*  725 */     for (ScriptStackElement[] group : stack) {
/*  726 */       StringBuilder sb = new StringBuilder();
/*  727 */       for (ScriptStackElement elem : group) {
/*  728 */         elem.renderJavaStyle(sb);
/*  729 */         sb.append(lineSeparator);
/*      */       } 
/*  731 */       list.add(sb.toString());
/*      */     } 
/*  733 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   public ScriptStackElement[][] getScriptStackElements(RhinoException ex) {
/*  738 */     if (ex.interpreterStackInfo == null) {
/*  739 */       return (ScriptStackElement[][])null;
/*      */     }
/*      */     
/*  742 */     List<ScriptStackElement[]> list = (List)new ArrayList<ScriptStackElement>();
/*      */     
/*  744 */     CallFrame[] array = (CallFrame[])ex.interpreterStackInfo;
/*  745 */     int[] linePC = ex.interpreterLineData;
/*  746 */     int arrayIndex = array.length;
/*  747 */     int linePCIndex = linePC.length;
/*  748 */     while (arrayIndex != 0) {
/*  749 */       arrayIndex--;
/*  750 */       CallFrame frame = array[arrayIndex];
/*  751 */       List<ScriptStackElement> group = new ArrayList<ScriptStackElement>();
/*  752 */       while (frame != null) {
/*  753 */         if (linePCIndex == 0) Kit.codeBug(); 
/*  754 */         linePCIndex--;
/*  755 */         InterpreterData idata = frame.idata;
/*  756 */         String fileName = idata.itsSourceFile;
/*  757 */         String functionName = null;
/*  758 */         int lineNumber = -1;
/*  759 */         int pc = linePC[linePCIndex];
/*  760 */         if (pc >= 0) {
/*  761 */           lineNumber = getIndex(idata.itsICode, pc);
/*      */         }
/*  763 */         if (idata.itsName != null && idata.itsName.length() != 0) {
/*  764 */           functionName = idata.itsName;
/*      */         }
/*  766 */         frame = frame.parentFrame;
/*  767 */         group.add(new ScriptStackElement(fileName, functionName, lineNumber));
/*      */       } 
/*  769 */       list.add(group.toArray(new ScriptStackElement[group.size()]));
/*      */     } 
/*  771 */     return list.<ScriptStackElement[]>toArray(new ScriptStackElement[list.size()][]);
/*      */   }
/*      */ 
/*      */   
/*      */   static String getEncodedSource(InterpreterData idata) {
/*  776 */     if (idata.encodedSource == null) {
/*  777 */       return null;
/*      */     }
/*  779 */     return idata.encodedSource.substring(idata.encodedSourceStart, idata.encodedSourceEnd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void initFunction(Context cx, Scriptable scope, InterpretedFunction parent, int index) {
/*  787 */     InterpretedFunction fn = InterpretedFunction.createFunction(cx, scope, parent, index);
/*  788 */     ScriptRuntime.initFunction(cx, scope, fn, fn.idata.itsFunctionType, parent.idata.evalScriptFlag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object interpret(InterpretedFunction ifun, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*  796 */     if (!ScriptRuntime.hasTopCall(cx)) Kit.codeBug();
/*      */     
/*  798 */     if (cx.interpreterSecurityDomain != ifun.securityDomain) {
/*  799 */       Object savedDomain = cx.interpreterSecurityDomain;
/*  800 */       cx.interpreterSecurityDomain = ifun.securityDomain;
/*      */       try {
/*  802 */         return ifun.securityController.callWithDomain(ifun.securityDomain, cx, ifun, scope, thisObj, args);
/*      */       } finally {
/*      */         
/*  805 */         cx.interpreterSecurityDomain = savedDomain;
/*      */       } 
/*      */     } 
/*      */     
/*  809 */     CallFrame frame = new CallFrame();
/*  810 */     initFrame(cx, scope, thisObj, args, (double[])null, 0, args.length, ifun, (CallFrame)null, frame);
/*      */     
/*  812 */     frame.isContinuationsTopFrame = cx.isContinuationsTopCall;
/*  813 */     cx.isContinuationsTopCall = false;
/*      */     
/*  815 */     return interpretLoop(cx, frame, (Object)null);
/*      */   }
/*      */   static class GeneratorState { int operation; Object value; RuntimeException returnedException;
/*      */     
/*      */     GeneratorState(int operation, Object value) {
/*  820 */       this.operation = operation;
/*  821 */       this.value = value;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object resumeGenerator(Context cx, Scriptable scope, int operation, Object savedState, Object value) {
/*  834 */     CallFrame frame = (CallFrame)savedState;
/*  835 */     GeneratorState generatorState = new GeneratorState(operation, value);
/*  836 */     if (operation == 2)
/*      */       try {
/*  838 */         return interpretLoop(cx, frame, generatorState);
/*  839 */       } catch (RuntimeException e) {
/*      */         
/*  841 */         if (e != value) {
/*  842 */           throw e;
/*      */         }
/*  844 */         return Undefined.instance;
/*      */       }  
/*  846 */     Object result = interpretLoop(cx, frame, generatorState);
/*  847 */     if (generatorState.returnedException != null)
/*  848 */       throw generatorState.returnedException; 
/*  849 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object restartContinuation(NativeContinuation c, Context cx, Scriptable scope, Object[] args) {
/*      */     Object arg;
/*  855 */     if (!ScriptRuntime.hasTopCall(cx)) {
/*  856 */       return ScriptRuntime.doTopCall(c, cx, scope, null, args);
/*      */     }
/*      */ 
/*      */     
/*  860 */     if (args.length == 0) {
/*  861 */       arg = Undefined.instance;
/*      */     } else {
/*  863 */       arg = args[0];
/*      */     } 
/*      */     
/*  866 */     CallFrame capturedFrame = (CallFrame)c.getImplementation();
/*  867 */     if (capturedFrame == null)
/*      */     {
/*  869 */       return arg;
/*      */     }
/*      */     
/*  872 */     ContinuationJump cjump = new ContinuationJump(c, null);
/*      */     
/*  874 */     cjump.result = arg;
/*  875 */     return interpretLoop(cx, (CallFrame)null, cjump);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object interpretLoop(Context cx, CallFrame frame, Object throwable) {
/*  885 */     Object DBL_MRK = UniqueTag.DOUBLE_MARK;
/*  886 */     Object undefined = Undefined.instance;
/*      */     
/*  888 */     boolean instructionCounting = (cx.instructionThreshold != 0);
/*      */ 
/*      */     
/*  891 */     int INVOCATION_COST = 100;
/*      */     
/*  893 */     int EXCEPTION_COST = 100;
/*      */     
/*  895 */     String stringReg = null;
/*  896 */     int indexReg = -1;
/*      */     
/*  898 */     if (cx.lastInterpreterFrame != null) {
/*      */ 
/*      */       
/*  901 */       if (cx.previousInterpreterInvocations == null) {
/*  902 */         cx.previousInterpreterInvocations = new ObjArray();
/*      */       }
/*  904 */       cx.previousInterpreterInvocations.push(cx.lastInterpreterFrame);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  914 */     GeneratorState generatorState = null;
/*  915 */     if (throwable != null) {
/*  916 */       if (throwable instanceof GeneratorState) {
/*  917 */         generatorState = (GeneratorState)throwable;
/*      */ 
/*      */         
/*  920 */         enterFrame(cx, frame, ScriptRuntime.emptyArgs, true);
/*  921 */         throwable = null;
/*  922 */       } else if (!(throwable instanceof ContinuationJump)) {
/*      */         
/*  924 */         Kit.codeBug();
/*      */       } 
/*      */     }
/*      */     
/*  928 */     Object interpreterResult = null;
/*  929 */     double interpreterResultDbl = 0.0D;
/*      */     
/*      */     label595: while (true) {
/*      */       boolean bool;
/*      */       try {
/*  934 */         if (throwable != null)
/*      */         
/*      */         { 
/*      */           
/*  938 */           frame = processThrowable(cx, throwable, frame, indexReg, instructionCounting);
/*      */           
/*  940 */           throwable = frame.throwable;
/*  941 */           frame.throwable = null; }
/*      */         
/*  943 */         else if (generatorState == null && frame.frozen) { Kit.codeBug(); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  948 */         Object[] stack = frame.stack;
/*  949 */         double[] sDbl = frame.sDbl;
/*  950 */         Object[] vars = frame.varSource.stack;
/*  951 */         double[] varDbls = frame.varSource.sDbl;
/*  952 */         int[] varAttributes = frame.varSource.stackAttributes;
/*  953 */         byte[] iCode = frame.idata.itsICode;
/*  954 */         String[] strings = frame.idata.itsStringTable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  960 */         int stackTop = frame.savedStackTop;
/*      */ 
/*      */         
/*  963 */         cx.lastInterpreterFrame = frame; while (true) {
/*      */           Object object9; int sourceLine; Object object8; boolean bool1; int k; boolean valBln; int j, offset; Object object7, o; int rIntValue; double lDbl, rDbl; Object object6, object5, rhs, object4; Ref ref1; Object object3; Ref ref; Object object2, value; Callable fun; Object object1; boolean afterFirstScope; Object lhs, val, obj, name, re; int m; double d; Scriptable scriptable1; Object object13; Ref ref2; Object id; Scriptable funThisObj; Function function; Throwable caughtException; int enumType; Object object12, data[], object11; boolean bool2; Object object10; int n;
/*      */           Scriptable calleeScope;
/*      */           Object[] outArgs;
/*      */           Scriptable lastCatchScope;
/*      */           int i, getterSetters[];
/*      */           Object x, object14;
/*  970 */           int op = iCode[frame.pc++];
/*      */ 
/*      */ 
/*      */           
/*  974 */           switch (op)
/*      */           { case -62:
/*  976 */               if (!frame.frozen) {
/*      */ 
/*      */                 
/*  979 */                 frame.pc--;
/*  980 */                 CallFrame generatorFrame = captureFrameForGenerator(frame);
/*  981 */                 generatorFrame.frozen = true;
/*  982 */                 NativeGenerator generator = new NativeGenerator(frame.scope, generatorFrame.fnOrScript, generatorFrame);
/*      */                 
/*  984 */                 frame.result = generator;
/*      */                 continue label595;
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case 72:
/*  992 */               if (!frame.frozen) {
/*  993 */                 return freezeGenerator(cx, frame, stackTop, generatorState);
/*      */               }
/*  995 */               object9 = thawGenerator(frame, stackTop, generatorState, op);
/*  996 */               if (object9 != Scriptable.NOT_FOUND) {
/*  997 */                 throwable = object9;
/*      */                 break;
/*      */               } 
/*      */               continue;
/*      */ 
/*      */ 
/*      */             
/*      */             case -63:
/* 1005 */               frame.frozen = true;
/* 1006 */               sourceLine = getIndex(iCode, frame.pc);
/* 1007 */               generatorState.returnedException = new JavaScriptException(NativeIterator.getStopIterationObject(frame.scope), frame.idata.itsSourceFile, sourceLine);
/*      */               continue label595;
/*      */ 
/*      */ 
/*      */             
/*      */             case 50:
/* 1013 */               object8 = stack[stackTop];
/* 1014 */               if (object8 == DBL_MRK) object8 = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 1015 */               stackTop--;
/*      */               
/* 1017 */               m = getIndex(iCode, frame.pc);
/* 1018 */               throwable = new JavaScriptException(object8, frame.idata.itsSourceFile, m);
/*      */               break;
/*      */ 
/*      */ 
/*      */             
/*      */             case 51:
/* 1024 */               indexReg += frame.localShift;
/* 1025 */               throwable = stack[indexReg];
/*      */               break;
/*      */             
/*      */             case 14:
/*      */             case 15:
/*      */             case 16:
/*      */             case 17:
/* 1032 */               stackTop = doCompare(frame, op, stack, sDbl, stackTop);
/*      */               continue;
/*      */             
/*      */             case 52:
/*      */             case 53:
/* 1037 */               stackTop = doInOrInstanceof(cx, op, stack, sDbl, stackTop);
/*      */               continue;
/*      */             
/*      */             case 12:
/*      */             case 13:
/* 1042 */               stackTop--;
/* 1043 */               bool1 = doEquals(stack, sDbl, stackTop);
/* 1044 */               k = bool1 ^ ((op == 13) ? 1 : 0);
/* 1045 */               stack[stackTop] = ScriptRuntime.wrapBoolean(k);
/*      */               continue;
/*      */             
/*      */             case 46:
/*      */             case 47:
/* 1050 */               stackTop--;
/* 1051 */               valBln = doShallowEquals(stack, sDbl, stackTop);
/* 1052 */               j = valBln ^ ((op == 47) ? 1 : 0);
/* 1053 */               stack[stackTop] = ScriptRuntime.wrapBoolean(j);
/*      */               continue;
/*      */             
/*      */             case 7:
/* 1057 */               if (stack_boolean(frame, stackTop--)) {
/* 1058 */                 frame.pc += 2;
/*      */                 continue;
/*      */               } 
/*      */             
/*      */             case 6:
/* 1063 */               if (!stack_boolean(frame, stackTop--)) {
/* 1064 */                 frame.pc += 2;
/*      */                 continue;
/*      */               } 
/*      */             
/*      */             case -6:
/* 1069 */               if (!stack_boolean(frame, stackTop--)) {
/* 1070 */                 frame.pc += 2;
/*      */                 continue;
/*      */               } 
/* 1073 */               stack[stackTop--] = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case 5:
/* 1899 */               if (instructionCounting) {
/* 1900 */                 addInstructionCount(cx, frame, 2);
/*      */               }
/* 1902 */               offset = getShort(iCode, frame.pc);
/* 1903 */               if (offset != 0) {
/*      */                 
/* 1905 */                 frame.pc += offset - 1;
/*      */               } else {
/* 1907 */                 frame.pc = frame.idata.longJumps.getExistingInt(frame.pc);
/*      */               } 
/*      */               
/* 1910 */               if (instructionCounting)
/* 1911 */                 frame.pcPrevBranch = frame.pc;  continue;case -23: stackTop++; stack[stackTop] = DBL_MRK; sDbl[stackTop] = (frame.pc + 2);case -24: if (stackTop == frame.emptyStackTop + 1) { indexReg += frame.localShift; stack[indexReg] = stack[stackTop]; sDbl[indexReg] = sDbl[stackTop]; stackTop--; continue; }  if (stackTop != frame.emptyStackTop) Kit.codeBug();  continue;case -25: if (instructionCounting) addInstructionCount(cx, frame, 0);  indexReg += frame.localShift; object7 = stack[indexReg]; if (object7 != DBL_MRK) { throwable = object7; break; }  frame.pc = (int)sDbl[indexReg]; if (instructionCounting) frame.pcPrevBranch = frame.pc;  continue;case -4: stack[stackTop] = null; stackTop--; continue;case -5: frame.result = stack[stackTop]; frame.resultDbl = sDbl[stackTop]; stack[stackTop] = null; stackTop--; continue;case -1: stack[stackTop + 1] = stack[stackTop]; sDbl[stackTop + 1] = sDbl[stackTop]; stackTop++; continue;case -2: stack[stackTop + 1] = stack[stackTop - 1]; sDbl[stackTop + 1] = sDbl[stackTop - 1]; stack[stackTop + 2] = stack[stackTop]; sDbl[stackTop + 2] = sDbl[stackTop]; stackTop += 2; continue;case -3: o = stack[stackTop]; stack[stackTop] = stack[stackTop - 1]; stack[stackTop - 1] = o; d = sDbl[stackTop]; sDbl[stackTop] = sDbl[stackTop - 1]; sDbl[stackTop - 1] = d; continue;case 4: frame.result = stack[stackTop]; frame.resultDbl = sDbl[stackTop]; stackTop--; continue label595;case 64: break;case -22: frame.result = undefined; continue label595;case 27: rIntValue = stack_int32(frame, stackTop); stack[stackTop] = DBL_MRK; sDbl[stackTop] = (rIntValue ^ 0xFFFFFFFF); continue;case 9: case 10: case 11: case 18: case 19: stackTop = doBitOp(frame, op, stack, sDbl, stackTop); continue;case 20: lDbl = stack_double(frame, stackTop - 1); n = stack_int32(frame, stackTop) & 0x1F; stack[--stackTop] = DBL_MRK; sDbl[stackTop] = (ScriptRuntime.toUint32(lDbl) >>> n); continue;case 28: case 29: rDbl = stack_double(frame, stackTop); stack[stackTop] = DBL_MRK; if (op == 29) rDbl = -rDbl;  sDbl[stackTop] = rDbl; continue;case 21: stackTop--; doAdd(stack, sDbl, stackTop, cx); continue;case 22: case 23: case 24: case 25: stackTop = doArithmetic(frame, op, stack, sDbl, stackTop); continue;case 26: stack[stackTop] = ScriptRuntime.wrapBoolean(!stack_boolean(frame, stackTop)); continue;case 49: stack[++stackTop] = ScriptRuntime.bind(cx, frame.scope, stringReg); continue;case 8: case 73: object6 = stack[stackTop]; if (object6 == DBL_MRK) object6 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stackTop--; scriptable1 = (Scriptable)stack[stackTop]; stack[stackTop] = (op == 8) ? ScriptRuntime.setName(scriptable1, object6, cx, frame.scope, stringReg) : ScriptRuntime.strictSetName(scriptable1, object6, cx, frame.scope, stringReg); continue;case -59: object6 = stack[stackTop]; if (object6 == DBL_MRK) object6 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stackTop--; scriptable1 = (Scriptable)stack[stackTop]; stack[stackTop] = ScriptRuntime.setConst(scriptable1, object6, cx, stringReg); continue;case 0: case 31: stackTop = doDelName(cx, frame, op, stack, sDbl, stackTop); continue;case 34: object5 = stack[stackTop]; if (object5 == DBL_MRK) object5 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stack[stackTop] = ScriptRuntime.getObjectPropNoWarn(object5, stringReg, cx, frame.scope); continue;case 33: object5 = stack[stackTop]; if (object5 == DBL_MRK) object5 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stack[stackTop] = ScriptRuntime.getObjectProp(object5, stringReg, cx, frame.scope); continue;case 35: rhs = stack[stackTop]; if (rhs == DBL_MRK) rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stackTop--; object13 = stack[stackTop]; if (object13 == DBL_MRK) object13 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stack[stackTop] = ScriptRuntime.setObjectProp(object13, stringReg, rhs, cx, frame.scope); continue;case -9: object4 = stack[stackTop]; if (object4 == DBL_MRK) object4 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stack[stackTop] = ScriptRuntime.propIncrDecr(object4, stringReg, cx, frame.scope, iCode[frame.pc]); frame.pc++; continue;case 36: stackTop = doGetElem(cx, frame, stack, sDbl, stackTop); continue;case 37: stackTop = doSetElem(cx, frame, stack, sDbl, stackTop); continue;case -10: stackTop = doElemIncDec(cx, frame, iCode, stack, sDbl, stackTop); continue;case 67: ref1 = (Ref)stack[stackTop]; stack[stackTop] = ScriptRuntime.refGet(ref1, cx); continue;case 68: object3 = stack[stackTop]; if (object3 == DBL_MRK) object3 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stackTop--; ref2 = (Ref)stack[stackTop]; stack[stackTop] = ScriptRuntime.refSet(ref2, object3, cx, frame.scope); continue;case 69: ref = (Ref)stack[stackTop]; stack[stackTop] = ScriptRuntime.refDel(ref, cx); continue;case -11: ref = (Ref)stack[stackTop]; stack[stackTop] = ScriptRuntime.refIncrDecr(ref, cx, frame.scope, iCode[frame.pc]); frame.pc++; continue;case 54: stackTop++; indexReg += frame.localShift; stack[stackTop] = stack[indexReg]; sDbl[stackTop] = sDbl[indexReg]; continue;case -56: indexReg += frame.localShift; stack[indexReg] = null; continue;case -15: stackTop++; stack[stackTop] = ScriptRuntime.getNameFunctionAndThis(stringReg, cx, frame.scope); stackTop++; stack[stackTop] = ScriptRuntime.lastStoredScriptable(cx); continue;case -16: object2 = stack[stackTop]; if (object2 == DBL_MRK) object2 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stack[stackTop] = ScriptRuntime.getPropFunctionAndThis(object2, stringReg, cx, frame.scope); stackTop++; stack[stackTop] = ScriptRuntime.lastStoredScriptable(cx); continue;case -17: object2 = stack[stackTop - 1]; if (object2 == DBL_MRK) object2 = ScriptRuntime.wrapNumber(sDbl[stackTop - 1]);  id = stack[stackTop]; if (id == DBL_MRK) id = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stack[stackTop - 1] = ScriptRuntime.getElemFunctionAndThis(object2, id, cx, frame.scope); stack[stackTop] = ScriptRuntime.lastStoredScriptable(cx); continue;case -18: value = stack[stackTop]; if (value == DBL_MRK) value = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stack[stackTop] = ScriptRuntime.getValueFunctionAndThis(value, cx); stackTop++; stack[stackTop] = ScriptRuntime.lastStoredScriptable(cx); continue;case -21: if (instructionCounting) cx.instructionCount += 100;  stackTop = doCallSpecial(cx, frame, stack, sDbl, stackTop, iCode, indexReg); continue;case -55: case 38: case 70: if (instructionCounting) cx.instructionCount += 100;  stackTop -= 1 + indexReg; fun = (Callable)stack[stackTop]; funThisObj = (Scriptable)stack[stackTop + 1]; if (op == 70) { Object[] arrayOfObject = getArgsArray(stack, sDbl, stackTop + 2, indexReg); stack[stackTop] = ScriptRuntime.callRef(fun, funThisObj, arrayOfObject, cx); continue; }  calleeScope = frame.scope; if (frame.useActivation) calleeScope = ScriptableObject.getTopLevelScope(frame.scope);  if (fun instanceof InterpretedFunction) { InterpretedFunction ifun = (InterpretedFunction)fun; if (frame.fnOrScript.securityDomain == ifun.securityDomain) { CallFrame callParentFrame = frame; CallFrame calleeFrame = new CallFrame(); if (op == -55) { callParentFrame = frame.parentFrame; exitFrame(cx, frame, (Object)null); }  initFrame(cx, calleeScope, funThisObj, stack, sDbl, stackTop + 2, indexReg, ifun, callParentFrame, calleeFrame); if (op != -55) { frame.savedStackTop = stackTop; frame.savedCallOp = op; }  frame = calleeFrame; continue label595; }  }  if (fun instanceof NativeContinuation) { ContinuationJump continuationJump = new ContinuationJump((NativeContinuation)fun, frame); if (indexReg == 0) { continuationJump.result = undefined; } else { continuationJump.result = stack[stackTop + 2]; continuationJump.resultDbl = sDbl[stackTop + 2]; }  throwable = continuationJump; break; }  if (fun instanceof IdFunctionObject) { IdFunctionObject ifun = (IdFunctionObject)fun; if (NativeContinuation.isContinuationConstructor(ifun)) { frame.stack[stackTop] = captureContinuation(cx, frame.parentFrame, false); continue; }  if (BaseFunction.isApplyOrCall(ifun)) { Callable applyCallable = ScriptRuntime.getCallable(funThisObj); if (applyCallable instanceof InterpretedFunction) { InterpretedFunction iApplyCallable = (InterpretedFunction)applyCallable; if (frame.fnOrScript.securityDomain == iApplyCallable.securityDomain) { frame = initFrameForApplyOrCall(cx, frame, indexReg, stack, sDbl, stackTop, op, calleeScope, ifun, iApplyCallable); continue label595; }  }  }  }  if (fun instanceof ScriptRuntime.NoSuchMethodShim) { ScriptRuntime.NoSuchMethodShim noSuchMethodShim = (ScriptRuntime.NoSuchMethodShim)fun; Callable noSuchMethodMethod = noSuchMethodShim.noSuchMethodMethod; if (noSuchMethodMethod instanceof InterpretedFunction) { InterpretedFunction ifun = (InterpretedFunction)noSuchMethodMethod; if (frame.fnOrScript.securityDomain == ifun.securityDomain) { frame = initFrameForNoSuchMethod(cx, frame, indexReg, stack, sDbl, stackTop, op, funThisObj, calleeScope, noSuchMethodShim, ifun); continue label595; }  }  }  cx.lastInterpreterFrame = frame; frame.savedCallOp = op; frame.savedStackTop = stackTop; stack[stackTop] = fun.call(cx, calleeScope, funThisObj, getArgsArray(stack, sDbl, stackTop + 2, indexReg)); continue;case 30: if (instructionCounting) cx.instructionCount += 100;  stackTop -= indexReg; object1 = stack[stackTop]; if (object1 instanceof InterpretedFunction) { InterpretedFunction f = (InterpretedFunction)object1; if (frame.fnOrScript.securityDomain == f.securityDomain) { Scriptable newInstance = f.createObject(cx, frame.scope); CallFrame calleeFrame = new CallFrame(); initFrame(cx, frame.scope, newInstance, stack, sDbl, stackTop + 1, indexReg, f, frame, calleeFrame); stack[stackTop] = newInstance; frame.savedStackTop = stackTop; frame.savedCallOp = op; frame = calleeFrame; continue label595; }  }  if (!(object1 instanceof Function)) { if (object1 == DBL_MRK) object1 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  throw ScriptRuntime.notFunctionError(object1); }  function = (Function)object1; if (function instanceof IdFunctionObject) { IdFunctionObject ifun = (IdFunctionObject)function; if (NativeContinuation.isContinuationConstructor(ifun)) { frame.stack[stackTop] = captureContinuation(cx, frame.parentFrame, false); continue; }  }  outArgs = getArgsArray(stack, sDbl, stackTop + 1, indexReg); stack[stackTop] = function.construct(cx, frame.scope, outArgs); continue;case 32: object1 = stack[stackTop]; if (object1 == DBL_MRK) object1 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stack[stackTop] = ScriptRuntime.typeof(object1); continue;case -14: stack[++stackTop] = ScriptRuntime.typeofName(frame.scope, stringReg); continue;case 41: stack[++stackTop] = stringReg; continue;case -27: stackTop++; stack[stackTop] = DBL_MRK; sDbl[stackTop] = getShort(iCode, frame.pc); frame.pc += 2; continue;case -28: stackTop++; stack[stackTop] = DBL_MRK; sDbl[stackTop] = getInt(iCode, frame.pc); frame.pc += 4; continue;case 40: stackTop++; stack[stackTop] = DBL_MRK; sDbl[stackTop] = frame.idata.itsDoubleTable[indexReg]; continue;case 39: stack[++stackTop] = ScriptRuntime.name(cx, frame.scope, stringReg); continue;case -8: stack[++stackTop] = ScriptRuntime.nameIncrDecr(frame.scope, stringReg, cx, iCode[frame.pc]); frame.pc++; continue;case -61: indexReg = iCode[frame.pc++];case 156: stackTop = doSetConstVar(frame, stack, sDbl, stackTop, vars, varDbls, varAttributes, indexReg); continue;case -49: indexReg = iCode[frame.pc++];case 56: stackTop = doSetVar(frame, stack, sDbl, stackTop, vars, varDbls, varAttributes, indexReg); continue;case -48: indexReg = iCode[frame.pc++];case 55: stackTop = doGetVar(frame, stack, sDbl, stackTop, vars, varDbls, indexReg); continue;case -7: stackTop = doVarIncDec(cx, frame, stack, sDbl, stackTop, vars, varDbls, varAttributes, indexReg); continue;case -51: stackTop++; stack[stackTop] = DBL_MRK; sDbl[stackTop] = 0.0D; continue;case -52: stackTop++; stack[stackTop] = DBL_MRK; sDbl[stackTop] = 1.0D; continue;case 42: stack[++stackTop] = null; continue;case 43: stack[++stackTop] = frame.thisObj; continue;case 63: stack[++stackTop] = frame.fnOrScript; continue;case 44: stack[++stackTop] = Boolean.FALSE; continue;case 45: stack[++stackTop] = Boolean.TRUE; continue;case -50: stack[++stackTop] = undefined; continue;case 2: object1 = stack[stackTop]; if (object1 == DBL_MRK) object1 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stackTop--; frame.scope = ScriptRuntime.enterWith(object1, cx, frame.scope); continue;case 3: frame.scope = ScriptRuntime.leaveWith(frame.scope); continue;case 57: stackTop--; indexReg += frame.localShift; afterFirstScope = (frame.idata.itsICode[frame.pc] != 0); caughtException = (Throwable)stack[stackTop + 1]; if (!afterFirstScope) { lastCatchScope = null; } else { lastCatchScope = (Scriptable)stack[indexReg]; }  stack[indexReg] = ScriptRuntime.newCatchScope(caughtException, lastCatchScope, stringReg, cx, frame.scope); frame.pc++; continue;case 58: case 59: case 60: lhs = stack[stackTop]; if (lhs == DBL_MRK) lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stackTop--; indexReg += frame.localShift; enumType = (op == 58) ? 0 : ((op == 59) ? 1 : 2); stack[indexReg] = ScriptRuntime.enumInit(lhs, cx, frame.scope, enumType); continue;case 61: case 62: indexReg += frame.localShift; val = stack[indexReg]; stackTop++; stack[stackTop] = (op == 61) ? ScriptRuntime.enumNext(val) : ScriptRuntime.enumId(val, cx); continue;case 71: obj = stack[stackTop]; if (obj == DBL_MRK) obj = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stack[stackTop] = ScriptRuntime.specialRef(obj, stringReg, cx, frame.scope); continue;case 77: stackTop = doRefMember(cx, stack, sDbl, stackTop, indexReg); continue;case 78: stackTop = doRefNsMember(cx, stack, sDbl, stackTop, indexReg); continue;case 79: name = stack[stackTop]; if (name == DBL_MRK) name = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stack[stackTop] = ScriptRuntime.nameRef(name, cx, frame.scope, indexReg); continue;case 80: stackTop = doRefNsName(cx, frame, stack, sDbl, stackTop, indexReg); continue;case -12: indexReg += frame.localShift; frame.scope = (Scriptable)stack[indexReg]; continue;case -13: indexReg += frame.localShift; stack[indexReg] = frame.scope; continue;case -19: stack[++stackTop] = InterpretedFunction.createFunction(cx, frame.scope, frame.fnOrScript, indexReg); continue;case -20: initFunction(cx, frame.scope, frame.fnOrScript, indexReg); continue;case 48: re = frame.idata.itsRegExpLiterals[indexReg]; stack[++stackTop] = ScriptRuntime.wrapRegExp(cx, frame.scope, re); continue;case -29: stackTop++; stack[stackTop] = new int[indexReg]; stackTop++; stack[stackTop] = new Object[indexReg]; sDbl[stackTop] = 0.0D; continue;case -30: object12 = stack[stackTop]; if (object12 == DBL_MRK) object12 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stackTop--; i = (int)sDbl[stackTop]; ((Object[])stack[stackTop])[i] = object12; sDbl[stackTop] = (i + 1); continue;case -57: object12 = stack[stackTop]; stackTop--; i = (int)sDbl[stackTop]; ((Object[])stack[stackTop])[i] = object12; ((int[])stack[stackTop - 1])[i] = -1; sDbl[stackTop] = (i + 1); continue;case -58: object12 = stack[stackTop]; stackTop--; i = (int)sDbl[stackTop]; ((Object[])stack[stackTop])[i] = object12; ((int[])stack[stackTop - 1])[i] = 1; sDbl[stackTop] = (i + 1); continue;case -31: case 65: case 66: data = (Object[])stack[stackTop]; stackTop--; getterSetters = (int[])stack[stackTop]; if (op == 66) { Object[] ids = (Object[])frame.idata.literalIds[indexReg]; object14 = ScriptRuntime.newObjectLiteral(ids, data, getterSetters, cx, frame.scope); } else { int[] skipIndexces = null; if (op == -31)
/*      */                   skipIndexces = (int[])frame.idata.literalIds[indexReg];  object14 = ScriptRuntime.newArrayLiteral(data, skipIndexces, cx, frame.scope); }  stack[stackTop] = object14; continue;case -53: object11 = stack[stackTop]; if (object11 == DBL_MRK)
/*      */                 object11 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stackTop--; frame.scope = ScriptRuntime.enterDotQuery(object11, frame.scope); continue;case -54: bool2 = stack_boolean(frame, stackTop); x = ScriptRuntime.updateDotQuery(bool2, frame.scope); if (x != null) { stack[stackTop] = x; frame.scope = ScriptRuntime.leaveDotQuery(frame.scope); frame.pc += 2; continue; }  stackTop--;case 74: object10 = stack[stackTop]; if (object10 == DBL_MRK)
/*      */                 object10 = ScriptRuntime.wrapNumber(sDbl[stackTop]);  stack[stackTop] = ScriptRuntime.setDefaultNamespace(object10, cx); continue;case 75: object10 = stack[stackTop]; if (object10 != DBL_MRK)
/*      */                 stack[stackTop] = ScriptRuntime.escapeAttributeValue(object10, cx);  continue;case 76: object10 = stack[stackTop]; if (object10 != DBL_MRK)
/*      */                 stack[stackTop] = ScriptRuntime.escapeTextValue(object10, cx);  continue;case -64: if (frame.debuggerFrame != null)
/* 1917 */                 frame.debuggerFrame.onDebuggerStatement(cx);  continue;case -26: frame.pcSourceLineStart = frame.pc; if (frame.debuggerFrame != null) { int line = getIndex(iCode, frame.pc); frame.debuggerFrame.onLineChange(cx, line); }  frame.pc += 2; continue;case -32: indexReg = 0; continue;case -33: indexReg = 1; continue;case -34: indexReg = 2; continue;case -35: indexReg = 3; continue;case -36: indexReg = 4; continue;case -37: indexReg = 5; continue;case -38: indexReg = 0xFF & iCode[frame.pc]; frame.pc++; continue;case -39: indexReg = getIndex(iCode, frame.pc); frame.pc += 2; continue;case -40: indexReg = getInt(iCode, frame.pc); frame.pc += 4; continue;case -41: stringReg = strings[0]; continue;case -42: stringReg = strings[1]; continue;case -43: stringReg = strings[2]; continue;case -44: stringReg = strings[3]; continue;case -45: stringReg = strings[0xFF & iCode[frame.pc]]; frame.pc++; continue;case -46: stringReg = strings[getIndex(iCode, frame.pc)]; frame.pc += 2; continue;case -47: stringReg = strings[getInt(iCode, frame.pc)]; frame.pc += 4; continue;default: dumpICode(frame.idata); throw new RuntimeException("Unknown icode : " + op + " @ pc : " + (frame.pc - 1)); }  exitFrame(cx, frame, (Object)null);
/* 1918 */           interpreterResult = frame.result;
/* 1919 */           interpreterResultDbl = frame.resultDbl;
/* 1920 */           if (frame.parentFrame != null) {
/* 1921 */             frame = frame.parentFrame;
/* 1922 */             if (frame.frozen) {
/* 1923 */               frame = frame.cloneFrozen();
/*      */             }
/* 1925 */             setCallResult(frame, interpreterResult, interpreterResultDbl);
/*      */             
/* 1927 */             interpreterResult = null;
/*      */             
/*      */             continue;
/*      */           } 
/*      */           break;
/*      */         } 
/* 1933 */       } catch (Throwable ex) {
/* 1934 */         if (throwable != null) {
/*      */           
/* 1936 */           ex.printStackTrace(System.err);
/* 1937 */           throw new IllegalStateException();
/*      */         } 
/* 1939 */         throwable = ex;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1945 */       if (throwable == null) Kit.codeBug();
/*      */ 
/*      */       
/* 1948 */       int EX_CATCH_STATE = 2;
/* 1949 */       int EX_FINALLY_STATE = 1;
/* 1950 */       int EX_NO_JS_STATE = 0;
/*      */ 
/*      */       
/* 1953 */       ContinuationJump cjump = null;
/*      */       
/* 1955 */       if (generatorState != null && generatorState.operation == 2 && throwable == generatorState.value) {
/*      */ 
/*      */ 
/*      */         
/* 1959 */         bool = true;
/* 1960 */       } else if (throwable instanceof JavaScriptException) {
/* 1961 */         bool = true;
/* 1962 */       } else if (throwable instanceof EcmaError) {
/*      */         
/* 1964 */         bool = true;
/* 1965 */       } else if (throwable instanceof EvaluatorException) {
/* 1966 */         bool = true;
/* 1967 */       } else if (throwable instanceof ContinuationPending) {
/* 1968 */         bool = false;
/* 1969 */       } else if (throwable instanceof RuntimeException) {
/* 1970 */         bool = cx.hasFeature(13) ? true : true;
/*      */       
/*      */       }
/* 1973 */       else if (throwable instanceof Error) {
/* 1974 */         bool = cx.hasFeature(13) ? true : false;
/*      */       
/*      */       }
/* 1977 */       else if (throwable instanceof ContinuationJump) {
/*      */         
/* 1979 */         bool = true;
/* 1980 */         cjump = (ContinuationJump)throwable;
/*      */       } else {
/* 1982 */         bool = cx.hasFeature(13) ? true : true;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1987 */       if (instructionCounting) {
/*      */         try {
/* 1989 */           addInstructionCount(cx, frame, 100);
/* 1990 */         } catch (RuntimeException ex) {
/* 1991 */           throwable = ex;
/* 1992 */           bool = true;
/* 1993 */         } catch (Error ex) {
/*      */ 
/*      */           
/* 1996 */           throwable = ex;
/* 1997 */           cjump = null;
/* 1998 */           bool = false;
/*      */         } 
/*      */       }
/* 2001 */       if (frame.debuggerFrame != null && throwable instanceof RuntimeException) {
/*      */ 
/*      */ 
/*      */         
/* 2005 */         RuntimeException rex = (RuntimeException)throwable;
/*      */         try {
/* 2007 */           frame.debuggerFrame.onExceptionThrown(cx, rex);
/* 2008 */         } catch (Throwable ex) {
/*      */ 
/*      */           
/* 2011 */           throwable = ex;
/* 2012 */           cjump = null;
/* 2013 */           bool = false;
/*      */         } 
/*      */       } 
/*      */       
/*      */       while (true) {
/* 2018 */         if (bool) {
/* 2019 */           boolean onlyFinally = (bool != 2);
/* 2020 */           indexReg = getExceptionHandler(frame, onlyFinally);
/* 2021 */           if (indexReg >= 0) {
/*      */             continue label595;
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2031 */         exitFrame(cx, frame, throwable);
/*      */         
/* 2033 */         frame = frame.parentFrame;
/* 2034 */         if (frame == null)
/* 2035 */           break;  if (cjump != null && cjump.branchFrame == frame)
/*      */         {
/*      */           
/* 2038 */           indexReg = -1;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2044 */       if (cjump != null) {
/* 2045 */         if (cjump.branchFrame != null)
/*      */         {
/* 2047 */           Kit.codeBug();
/*      */         }
/* 2049 */         if (cjump.capturedFrame != null) {
/*      */           
/* 2051 */           indexReg = -1;
/*      */           
/*      */           continue;
/*      */         } 
/* 2055 */         interpreterResult = cjump.result;
/* 2056 */         interpreterResultDbl = cjump.resultDbl;
/* 2057 */         throwable = null;
/*      */       } 
/*      */ 
/*      */       
/*      */       break;
/*      */     } 
/*      */ 
/*      */     
/* 2065 */     if (cx.previousInterpreterInvocations != null && cx.previousInterpreterInvocations.size() != 0) {
/*      */ 
/*      */       
/* 2068 */       cx.lastInterpreterFrame = cx.previousInterpreterInvocations.pop();
/*      */     }
/*      */     else {
/*      */       
/* 2072 */       cx.lastInterpreterFrame = null;
/*      */       
/* 2074 */       cx.previousInterpreterInvocations = null;
/*      */     } 
/*      */     
/* 2077 */     if (throwable != null) {
/* 2078 */       if (throwable instanceof RuntimeException) {
/* 2079 */         throw (RuntimeException)throwable;
/*      */       }
/*      */       
/* 2082 */       throw (Error)throwable;
/*      */     } 
/*      */ 
/*      */     
/* 2086 */     return (interpreterResult != DBL_MRK) ? interpreterResult : ScriptRuntime.wrapNumber(interpreterResultDbl);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int doInOrInstanceof(Context cx, int op, Object[] stack, double[] sDbl, int stackTop) {
/*      */     boolean valBln;
/* 2093 */     Object rhs = stack[stackTop];
/* 2094 */     if (rhs == UniqueTag.DOUBLE_MARK) rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2095 */     stackTop--;
/* 2096 */     Object lhs = stack[stackTop];
/* 2097 */     if (lhs == UniqueTag.DOUBLE_MARK) lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
/*      */     
/* 2099 */     if (op == 52) {
/* 2100 */       valBln = ScriptRuntime.in(lhs, rhs, cx);
/*      */     } else {
/* 2102 */       valBln = ScriptRuntime.instanceOf(lhs, rhs, cx);
/*      */     } 
/* 2104 */     stack[stackTop] = ScriptRuntime.wrapBoolean(valBln);
/* 2105 */     return stackTop;
/*      */   }
/*      */   private static int doCompare(CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop) {
/*      */     boolean bool;
/*      */     double rDbl, lDbl;
/* 2110 */     stackTop--;
/* 2111 */     Object rhs = stack[stackTop + 1];
/* 2112 */     Object lhs = stack[stackTop];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2119 */     if (rhs == UniqueTag.DOUBLE_MARK)
/* 2120 */     { rDbl = sDbl[stackTop + 1];
/* 2121 */       lDbl = stack_double(frame, stackTop); }
/* 2122 */     else if (lhs == UniqueTag.DOUBLE_MARK)
/* 2123 */     { rDbl = ScriptRuntime.toNumber(rhs);
/* 2124 */       lDbl = sDbl[stackTop]; }
/*      */     else
/*      */     { boolean valBln;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2145 */       switch (op)
/*      */       { case 17:
/* 2147 */           valBln = ScriptRuntime.cmp_LE(rhs, lhs);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2162 */           stack[stackTop] = ScriptRuntime.wrapBoolean(valBln);
/* 2163 */           return stackTop;case 15: valBln = ScriptRuntime.cmp_LE(lhs, rhs); stack[stackTop] = ScriptRuntime.wrapBoolean(valBln); return stackTop;case 16: valBln = ScriptRuntime.cmp_LT(rhs, lhs); stack[stackTop] = ScriptRuntime.wrapBoolean(valBln); return stackTop;case 14: valBln = ScriptRuntime.cmp_LT(lhs, rhs); stack[stackTop] = ScriptRuntime.wrapBoolean(valBln); return stackTop; }  throw Kit.codeBug(); }  switch (op) { case 17: bool = (lDbl >= rDbl) ? true : false; stack[stackTop] = ScriptRuntime.wrapBoolean(bool); return stackTop;case 15: bool = (lDbl <= rDbl) ? true : false; stack[stackTop] = ScriptRuntime.wrapBoolean(bool); return stackTop;case 16: bool = (lDbl > rDbl) ? true : false; stack[stackTop] = ScriptRuntime.wrapBoolean(bool); return stackTop;case 14: bool = (lDbl < rDbl) ? true : false; stack[stackTop] = ScriptRuntime.wrapBoolean(bool); return stackTop; }
/*      */     
/*      */     throw Kit.codeBug();
/*      */   }
/*      */   private static int doBitOp(CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop) {
/* 2168 */     int lIntValue = stack_int32(frame, stackTop - 1);
/* 2169 */     int rIntValue = stack_int32(frame, stackTop);
/* 2170 */     stack[--stackTop] = UniqueTag.DOUBLE_MARK;
/* 2171 */     switch (op) {
/*      */       case 11:
/* 2173 */         lIntValue &= rIntValue;
/*      */         break;
/*      */       case 9:
/* 2176 */         lIntValue |= rIntValue;
/*      */         break;
/*      */       case 10:
/* 2179 */         lIntValue ^= rIntValue;
/*      */         break;
/*      */       case 18:
/* 2182 */         lIntValue <<= rIntValue;
/*      */         break;
/*      */       case 19:
/* 2185 */         lIntValue >>= rIntValue;
/*      */         break;
/*      */     } 
/* 2188 */     sDbl[stackTop] = lIntValue;
/* 2189 */     return stackTop;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int doDelName(Context cx, CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop) {
/* 2194 */     Object rhs = stack[stackTop];
/* 2195 */     if (rhs == UniqueTag.DOUBLE_MARK) rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2196 */     stackTop--;
/* 2197 */     Object lhs = stack[stackTop];
/* 2198 */     if (lhs == UniqueTag.DOUBLE_MARK) lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2199 */     stack[stackTop] = ScriptRuntime.delete(lhs, rhs, cx, frame.scope, (op == 0));
/*      */     
/* 2201 */     return stackTop;
/*      */   }
/*      */   
/*      */   private static int doGetElem(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop) {
/*      */     Object value;
/* 2206 */     stackTop--;
/* 2207 */     Object lhs = stack[stackTop];
/* 2208 */     if (lhs == UniqueTag.DOUBLE_MARK) {
/* 2209 */       lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
/*      */     }
/*      */     
/* 2212 */     Object id = stack[stackTop + 1];
/* 2213 */     if (id != UniqueTag.DOUBLE_MARK) {
/* 2214 */       value = ScriptRuntime.getObjectElem(lhs, id, cx, frame.scope);
/*      */     } else {
/* 2216 */       double d = sDbl[stackTop + 1];
/* 2217 */       value = ScriptRuntime.getObjectIndex(lhs, d, cx, frame.scope);
/*      */     } 
/* 2219 */     stack[stackTop] = value;
/* 2220 */     return stackTop;
/*      */   }
/*      */   
/*      */   private static int doSetElem(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop) {
/*      */     Object value;
/* 2225 */     stackTop -= 2;
/* 2226 */     Object rhs = stack[stackTop + 2];
/* 2227 */     if (rhs == UniqueTag.DOUBLE_MARK) {
/* 2228 */       rhs = ScriptRuntime.wrapNumber(sDbl[stackTop + 2]);
/*      */     }
/* 2230 */     Object lhs = stack[stackTop];
/* 2231 */     if (lhs == UniqueTag.DOUBLE_MARK) {
/* 2232 */       lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
/*      */     }
/*      */     
/* 2235 */     Object id = stack[stackTop + 1];
/* 2236 */     if (id != UniqueTag.DOUBLE_MARK) {
/* 2237 */       value = ScriptRuntime.setObjectElem(lhs, id, rhs, cx, frame.scope);
/*      */     } else {
/* 2239 */       double d = sDbl[stackTop + 1];
/* 2240 */       value = ScriptRuntime.setObjectIndex(lhs, d, rhs, cx, frame.scope);
/*      */     } 
/* 2242 */     stack[stackTop] = value;
/* 2243 */     return stackTop;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int doElemIncDec(Context cx, CallFrame frame, byte[] iCode, Object[] stack, double[] sDbl, int stackTop) {
/* 2248 */     Object rhs = stack[stackTop];
/* 2249 */     if (rhs == UniqueTag.DOUBLE_MARK) rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2250 */     stackTop--;
/* 2251 */     Object lhs = stack[stackTop];
/* 2252 */     if (lhs == UniqueTag.DOUBLE_MARK) lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2253 */     stack[stackTop] = ScriptRuntime.elemIncrDecr(lhs, rhs, cx, frame.scope, iCode[frame.pc]);
/*      */     
/* 2255 */     frame.pc++;
/* 2256 */     return stackTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int doCallSpecial(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop, byte[] iCode, int indexReg) {
/* 2263 */     int callType = iCode[frame.pc] & 0xFF;
/* 2264 */     boolean isNew = (iCode[frame.pc + 1] != 0);
/* 2265 */     int sourceLine = getIndex(iCode, frame.pc + 2);
/*      */ 
/*      */     
/* 2268 */     if (isNew) {
/*      */       
/* 2270 */       stackTop -= indexReg;
/*      */       
/* 2272 */       Object function = stack[stackTop];
/* 2273 */       if (function == UniqueTag.DOUBLE_MARK)
/* 2274 */         function = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2275 */       Object[] outArgs = getArgsArray(stack, sDbl, stackTop + 1, indexReg);
/*      */       
/* 2277 */       stack[stackTop] = ScriptRuntime.newSpecial(cx, function, outArgs, frame.scope, callType);
/*      */     }
/*      */     else {
/*      */       
/* 2281 */       stackTop -= 1 + indexReg;
/*      */ 
/*      */ 
/*      */       
/* 2285 */       Scriptable functionThis = (Scriptable)stack[stackTop + 1];
/* 2286 */       Callable function = (Callable)stack[stackTop];
/* 2287 */       Object[] outArgs = getArgsArray(stack, sDbl, stackTop + 2, indexReg);
/*      */       
/* 2289 */       stack[stackTop] = ScriptRuntime.callSpecial(cx, function, functionThis, outArgs, frame.scope, frame.thisObj, callType, frame.idata.itsSourceFile, sourceLine);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2294 */     frame.pc += 4;
/* 2295 */     return stackTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int doSetConstVar(CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg) {
/* 2302 */     if (!frame.useActivation) {
/* 2303 */       if ((varAttributes[indexReg] & 0x1) == 0) {
/* 2304 */         throw Context.reportRuntimeError1("msg.var.redecl", frame.idata.argNames[indexReg]);
/*      */       }
/*      */       
/* 2307 */       if ((varAttributes[indexReg] & 0x8) != 0) {
/*      */ 
/*      */         
/* 2310 */         vars[indexReg] = stack[stackTop];
/* 2311 */         varAttributes[indexReg] = varAttributes[indexReg] & 0xFFFFFFF7;
/* 2312 */         varDbls[indexReg] = sDbl[stackTop];
/*      */       } 
/*      */     } else {
/* 2315 */       Object val = stack[stackTop];
/* 2316 */       if (val == UniqueTag.DOUBLE_MARK) val = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2317 */       String stringReg = frame.idata.argNames[indexReg];
/* 2318 */       if (frame.scope instanceof ConstProperties) {
/* 2319 */         ConstProperties cp = (ConstProperties)frame.scope;
/* 2320 */         cp.putConst(stringReg, frame.scope, val);
/*      */       } else {
/* 2322 */         throw Kit.codeBug();
/*      */       } 
/* 2324 */     }  return stackTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int doSetVar(CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg) {
/* 2331 */     if (!frame.useActivation) {
/* 2332 */       if ((varAttributes[indexReg] & 0x1) == 0) {
/* 2333 */         vars[indexReg] = stack[stackTop];
/* 2334 */         varDbls[indexReg] = sDbl[stackTop];
/*      */       } 
/*      */     } else {
/* 2337 */       Object val = stack[stackTop];
/* 2338 */       if (val == UniqueTag.DOUBLE_MARK) val = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2339 */       String stringReg = frame.idata.argNames[indexReg];
/* 2340 */       frame.scope.put(stringReg, frame.scope, val);
/*      */     } 
/* 2342 */     return stackTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int doGetVar(CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int indexReg) {
/* 2349 */     stackTop++;
/* 2350 */     if (!frame.useActivation) {
/* 2351 */       stack[stackTop] = vars[indexReg];
/* 2352 */       sDbl[stackTop] = varDbls[indexReg];
/*      */     } else {
/* 2354 */       String stringReg = frame.idata.argNames[indexReg];
/* 2355 */       stack[stackTop] = frame.scope.get(stringReg, frame.scope);
/*      */     } 
/* 2357 */     return stackTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int doVarIncDec(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg) {
/* 2366 */     stackTop++;
/* 2367 */     int incrDecrMask = frame.idata.itsICode[frame.pc];
/* 2368 */     if (!frame.useActivation) {
/* 2369 */       double d; Object varValue = vars[indexReg];
/*      */       
/* 2371 */       if (varValue == UniqueTag.DOUBLE_MARK) {
/* 2372 */         d = varDbls[indexReg];
/*      */       } else {
/* 2374 */         d = ScriptRuntime.toNumber(varValue);
/*      */       } 
/* 2376 */       double d2 = ((incrDecrMask & 0x1) == 0) ? (d + 1.0D) : (d - 1.0D);
/*      */       
/* 2378 */       boolean post = ((incrDecrMask & 0x2) != 0);
/* 2379 */       if ((varAttributes[indexReg] & 0x1) == 0) {
/* 2380 */         if (varValue != UniqueTag.DOUBLE_MARK) {
/* 2381 */           vars[indexReg] = UniqueTag.DOUBLE_MARK;
/*      */         }
/* 2383 */         varDbls[indexReg] = d2;
/* 2384 */         stack[stackTop] = UniqueTag.DOUBLE_MARK;
/* 2385 */         sDbl[stackTop] = post ? d : d2;
/*      */       }
/* 2387 */       else if (post && varValue != UniqueTag.DOUBLE_MARK) {
/* 2388 */         stack[stackTop] = varValue;
/*      */       } else {
/* 2390 */         stack[stackTop] = UniqueTag.DOUBLE_MARK;
/* 2391 */         sDbl[stackTop] = post ? d : d2;
/*      */       } 
/*      */     } else {
/*      */       
/* 2395 */       String varName = frame.idata.argNames[indexReg];
/* 2396 */       stack[stackTop] = ScriptRuntime.nameIncrDecr(frame.scope, varName, cx, incrDecrMask);
/*      */     } 
/*      */     
/* 2399 */     frame.pc++;
/* 2400 */     return stackTop;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int doRefMember(Context cx, Object[] stack, double[] sDbl, int stackTop, int flags) {
/* 2405 */     Object elem = stack[stackTop];
/* 2406 */     if (elem == UniqueTag.DOUBLE_MARK) elem = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2407 */     stackTop--;
/* 2408 */     Object obj = stack[stackTop];
/* 2409 */     if (obj == UniqueTag.DOUBLE_MARK) obj = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2410 */     stack[stackTop] = ScriptRuntime.memberRef(obj, elem, cx, flags);
/* 2411 */     return stackTop;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int doRefNsMember(Context cx, Object[] stack, double[] sDbl, int stackTop, int flags) {
/* 2416 */     Object elem = stack[stackTop];
/* 2417 */     if (elem == UniqueTag.DOUBLE_MARK) elem = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2418 */     stackTop--;
/* 2419 */     Object ns = stack[stackTop];
/* 2420 */     if (ns == UniqueTag.DOUBLE_MARK) ns = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2421 */     stackTop--;
/* 2422 */     Object obj = stack[stackTop];
/* 2423 */     if (obj == UniqueTag.DOUBLE_MARK) obj = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2424 */     stack[stackTop] = ScriptRuntime.memberRef(obj, ns, elem, cx, flags);
/* 2425 */     return stackTop;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int doRefNsName(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop, int flags) {
/* 2431 */     Object name = stack[stackTop];
/* 2432 */     if (name == UniqueTag.DOUBLE_MARK) name = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2433 */     stackTop--;
/* 2434 */     Object ns = stack[stackTop];
/* 2435 */     if (ns == UniqueTag.DOUBLE_MARK) ns = ScriptRuntime.wrapNumber(sDbl[stackTop]); 
/* 2436 */     stack[stackTop] = ScriptRuntime.nameRef(ns, name, cx, frame.scope, flags);
/* 2437 */     return stackTop;
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
/*      */   private static CallFrame initFrameForNoSuchMethod(Context cx, CallFrame frame, int indexReg, Object[] stack, double[] sDbl, int stackTop, int op, Scriptable funThisObj, Scriptable calleeScope, ScriptRuntime.NoSuchMethodShim noSuchMethodShim, InterpretedFunction ifun) {
/* 2449 */     Object[] argsArray = null;
/*      */ 
/*      */     
/* 2452 */     int shift = stackTop + 2;
/* 2453 */     Object[] elements = new Object[indexReg];
/* 2454 */     for (int i = 0; i < indexReg; i++, shift++) {
/* 2455 */       Object val = stack[shift];
/* 2456 */       if (val == UniqueTag.DOUBLE_MARK) {
/* 2457 */         val = ScriptRuntime.wrapNumber(sDbl[shift]);
/*      */       }
/* 2459 */       elements[i] = val;
/*      */     } 
/* 2461 */     argsArray = new Object[2];
/* 2462 */     argsArray[0] = noSuchMethodShim.methodName;
/* 2463 */     argsArray[1] = cx.newArray(calleeScope, elements);
/*      */ 
/*      */     
/* 2466 */     CallFrame callParentFrame = frame;
/* 2467 */     CallFrame calleeFrame = new CallFrame();
/* 2468 */     if (op == -55) {
/* 2469 */       callParentFrame = frame.parentFrame;
/* 2470 */       exitFrame(cx, frame, (Object)null);
/*      */     } 
/*      */ 
/*      */     
/* 2474 */     initFrame(cx, calleeScope, funThisObj, argsArray, (double[])null, 0, 2, ifun, callParentFrame, calleeFrame);
/*      */     
/* 2476 */     if (op != -55) {
/* 2477 */       frame.savedStackTop = stackTop;
/* 2478 */       frame.savedCallOp = op;
/*      */     } 
/* 2480 */     return calleeFrame;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean doEquals(Object[] stack, double[] sDbl, int stackTop) {
/* 2485 */     Object rhs = stack[stackTop + 1];
/* 2486 */     Object lhs = stack[stackTop];
/* 2487 */     if (rhs == UniqueTag.DOUBLE_MARK) {
/* 2488 */       if (lhs == UniqueTag.DOUBLE_MARK) {
/* 2489 */         return (sDbl[stackTop] == sDbl[stackTop + 1]);
/*      */       }
/* 2491 */       return ScriptRuntime.eqNumber(sDbl[stackTop + 1], lhs);
/*      */     } 
/*      */     
/* 2494 */     if (lhs == UniqueTag.DOUBLE_MARK) {
/* 2495 */       return ScriptRuntime.eqNumber(sDbl[stackTop], rhs);
/*      */     }
/* 2497 */     return ScriptRuntime.eq(lhs, rhs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean doShallowEquals(Object[] stack, double[] sDbl, int stackTop) {
/*      */     double rdbl, ldbl;
/* 2505 */     Object rhs = stack[stackTop + 1];
/* 2506 */     Object lhs = stack[stackTop];
/* 2507 */     Object DBL_MRK = UniqueTag.DOUBLE_MARK;
/*      */     
/* 2509 */     if (rhs == DBL_MRK) {
/* 2510 */       rdbl = sDbl[stackTop + 1];
/* 2511 */       if (lhs == DBL_MRK) {
/* 2512 */         ldbl = sDbl[stackTop];
/* 2513 */       } else if (lhs instanceof Number) {
/* 2514 */         ldbl = ((Number)lhs).doubleValue();
/*      */       } else {
/* 2516 */         return false;
/*      */       } 
/* 2518 */     } else if (lhs == DBL_MRK) {
/* 2519 */       ldbl = sDbl[stackTop];
/* 2520 */       if (rhs instanceof Number) {
/* 2521 */         rdbl = ((Number)rhs).doubleValue();
/*      */       } else {
/* 2523 */         return false;
/*      */       } 
/*      */     } else {
/* 2526 */       return ScriptRuntime.shallowEq(lhs, rhs);
/*      */     } 
/* 2528 */     return (ldbl == rdbl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static CallFrame processThrowable(Context cx, Object throwable, CallFrame frame, int indexReg, boolean instructionCounting) {
/* 2538 */     if (indexReg >= 0) {
/*      */ 
/*      */ 
/*      */       
/* 2542 */       if (frame.frozen)
/*      */       {
/* 2544 */         frame = frame.cloneFrozen();
/*      */       }
/*      */       
/* 2547 */       int[] table = frame.idata.itsExceptionTable;
/*      */       
/* 2549 */       frame.pc = table[indexReg + 2];
/* 2550 */       if (instructionCounting) {
/* 2551 */         frame.pcPrevBranch = frame.pc;
/*      */       }
/*      */       
/* 2554 */       frame.savedStackTop = frame.emptyStackTop;
/* 2555 */       int scopeLocal = frame.localShift + table[indexReg + 5];
/*      */ 
/*      */       
/* 2558 */       int exLocal = frame.localShift + table[indexReg + 4];
/*      */ 
/*      */       
/* 2561 */       frame.scope = (Scriptable)frame.stack[scopeLocal];
/* 2562 */       frame.stack[exLocal] = throwable;
/*      */       
/* 2564 */       throwable = null;
/*      */     } else {
/*      */       
/* 2567 */       ContinuationJump cjump = (ContinuationJump)throwable;
/*      */ 
/*      */       
/* 2570 */       throwable = null;
/*      */       
/* 2572 */       if (cjump.branchFrame != frame) Kit.codeBug();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2577 */       if (cjump.capturedFrame == null) Kit.codeBug();
/*      */ 
/*      */ 
/*      */       
/* 2581 */       int rewindCount = cjump.capturedFrame.frameIndex + 1;
/* 2582 */       if (cjump.branchFrame != null) {
/* 2583 */         rewindCount -= cjump.branchFrame.frameIndex;
/*      */       }
/*      */       
/* 2586 */       int enterCount = 0;
/* 2587 */       CallFrame[] enterFrames = null;
/*      */       
/* 2589 */       CallFrame x = cjump.capturedFrame;
/* 2590 */       for (int i = 0; i != rewindCount; i++) {
/* 2591 */         if (!x.frozen) Kit.codeBug(); 
/* 2592 */         if (isFrameEnterExitRequired(x)) {
/* 2593 */           if (enterFrames == null)
/*      */           {
/*      */ 
/*      */             
/* 2597 */             enterFrames = new CallFrame[rewindCount - i];
/*      */           }
/*      */           
/* 2600 */           enterFrames[enterCount] = x;
/* 2601 */           enterCount++;
/*      */         } 
/* 2603 */         x = x.parentFrame;
/*      */       } 
/*      */       
/* 2606 */       while (enterCount != 0) {
/*      */ 
/*      */ 
/*      */         
/* 2610 */         enterCount--;
/* 2611 */         x = enterFrames[enterCount];
/* 2612 */         enterFrame(cx, x, ScriptRuntime.emptyArgs, true);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2619 */       frame = cjump.capturedFrame.cloneFrozen();
/* 2620 */       setCallResult(frame, cjump.result, cjump.resultDbl);
/*      */     } 
/*      */     
/* 2623 */     frame.throwable = throwable;
/* 2624 */     return frame;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object freezeGenerator(Context cx, CallFrame frame, int stackTop, GeneratorState generatorState) {
/* 2631 */     if (generatorState.operation == 2)
/*      */     {
/* 2633 */       throw ScriptRuntime.typeError0("msg.yield.closing");
/*      */     }
/*      */     
/* 2636 */     frame.frozen = true;
/* 2637 */     frame.result = frame.stack[stackTop];
/* 2638 */     frame.resultDbl = frame.sDbl[stackTop];
/* 2639 */     frame.savedStackTop = stackTop;
/* 2640 */     frame.pc--;
/* 2641 */     ScriptRuntime.exitActivationFunction(cx);
/* 2642 */     return (frame.result != UniqueTag.DOUBLE_MARK) ? frame.result : ScriptRuntime.wrapNumber(frame.resultDbl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object thawGenerator(CallFrame frame, int stackTop, GeneratorState generatorState, int op) {
/* 2651 */     frame.frozen = false;
/* 2652 */     int sourceLine = getIndex(frame.idata.itsICode, frame.pc);
/* 2653 */     frame.pc += 2;
/* 2654 */     if (generatorState.operation == 1)
/*      */     {
/*      */       
/* 2657 */       return new JavaScriptException(generatorState.value, frame.idata.itsSourceFile, sourceLine);
/*      */     }
/*      */ 
/*      */     
/* 2661 */     if (generatorState.operation == 2) {
/* 2662 */       return generatorState.value;
/*      */     }
/* 2664 */     if (generatorState.operation != 0)
/* 2665 */       throw Kit.codeBug(); 
/* 2666 */     if (op == 72)
/* 2667 */       frame.stack[stackTop] = generatorState.value; 
/* 2668 */     return Scriptable.NOT_FOUND;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static CallFrame initFrameForApplyOrCall(Context cx, CallFrame frame, int indexReg, Object[] stack, double[] sDbl, int stackTop, int op, Scriptable calleeScope, IdFunctionObject ifun, InterpretedFunction iApplyCallable) {
/*      */     Scriptable applyThis;
/* 2677 */     if (indexReg != 0) {
/* 2678 */       Object obj = stack[stackTop + 2];
/* 2679 */       if (obj == UniqueTag.DOUBLE_MARK)
/* 2680 */         obj = ScriptRuntime.wrapNumber(sDbl[stackTop + 2]); 
/* 2681 */       applyThis = ScriptRuntime.toObjectOrNull(cx, obj, frame.scope);
/*      */     } else {
/*      */       
/* 2684 */       applyThis = null;
/*      */     } 
/* 2686 */     if (applyThis == null)
/*      */     {
/* 2688 */       applyThis = ScriptRuntime.getTopCallScope(cx);
/*      */     }
/* 2690 */     if (op == -55) {
/* 2691 */       exitFrame(cx, frame, (Object)null);
/* 2692 */       frame = frame.parentFrame;
/*      */     } else {
/*      */       
/* 2695 */       frame.savedStackTop = stackTop;
/* 2696 */       frame.savedCallOp = op;
/*      */     } 
/* 2698 */     CallFrame calleeFrame = new CallFrame();
/* 2699 */     if (BaseFunction.isApply(ifun)) {
/* 2700 */       Object[] callArgs = (indexReg < 2) ? ScriptRuntime.emptyArgs : ScriptRuntime.getApplyArguments(cx, stack[stackTop + 3]);
/*      */       
/* 2702 */       initFrame(cx, calleeScope, applyThis, callArgs, (double[])null, 0, callArgs.length, iApplyCallable, frame, calleeFrame);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 2707 */       for (int i = 1; i < indexReg; i++) {
/* 2708 */         stack[stackTop + 1 + i] = stack[stackTop + 2 + i];
/* 2709 */         sDbl[stackTop + 1 + i] = sDbl[stackTop + 2 + i];
/*      */       } 
/* 2711 */       int argCount = (indexReg < 2) ? 0 : (indexReg - 1);
/* 2712 */       initFrame(cx, calleeScope, applyThis, stack, sDbl, stackTop + 2, argCount, iApplyCallable, frame, calleeFrame);
/*      */     } 
/*      */ 
/*      */     
/* 2716 */     frame = calleeFrame;
/* 2717 */     return frame;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void initFrame(Context cx, Scriptable callerScope, Scriptable thisObj, Object[] args, double[] argsDbl, int argShift, int argCount, InterpretedFunction fnOrScript, CallFrame parentFrame, CallFrame frame) {
/*      */     Scriptable scope;
/*      */     Object[] stack;
/*      */     int[] stackAttributes;
/*      */     double[] sDbl;
/*      */     boolean stackReuse;
/* 2727 */     InterpreterData idata = fnOrScript.idata;
/*      */     
/* 2729 */     boolean useActivation = idata.itsNeedsActivation;
/* 2730 */     DebugFrame debuggerFrame = null;
/* 2731 */     if (cx.debugger != null) {
/* 2732 */       debuggerFrame = cx.debugger.getFrame(cx, idata);
/* 2733 */       if (debuggerFrame != null) {
/* 2734 */         useActivation = true;
/*      */       }
/*      */     } 
/*      */     
/* 2738 */     if (useActivation) {
/*      */ 
/*      */       
/* 2741 */       if (argsDbl != null) {
/* 2742 */         args = getArgsArray(args, argsDbl, argShift, argCount);
/*      */       }
/* 2744 */       argShift = 0;
/* 2745 */       argsDbl = null;
/*      */     } 
/*      */ 
/*      */     
/* 2749 */     if (idata.itsFunctionType != 0) {
/* 2750 */       scope = fnOrScript.getParentScope();
/*      */       
/* 2752 */       if (useActivation) {
/* 2753 */         scope = ScriptRuntime.createFunctionActivation(fnOrScript, scope, args);
/*      */       }
/*      */     } else {
/*      */       
/* 2757 */       scope = callerScope;
/* 2758 */       ScriptRuntime.initScript(fnOrScript, thisObj, cx, scope, fnOrScript.idata.evalScriptFlag);
/*      */     } 
/*      */ 
/*      */     
/* 2762 */     if (idata.itsNestedFunctions != null) {
/* 2763 */       if (idata.itsFunctionType != 0 && !idata.itsNeedsActivation)
/* 2764 */         Kit.codeBug(); 
/* 2765 */       for (int k = 0; k < idata.itsNestedFunctions.length; k++) {
/* 2766 */         InterpreterData fdata = idata.itsNestedFunctions[k];
/* 2767 */         if (fdata.itsFunctionType == 1) {
/* 2768 */           initFunction(cx, scope, fnOrScript, k);
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2775 */     int emptyStackTop = idata.itsMaxVars + idata.itsMaxLocals - 1;
/* 2776 */     int maxFrameArray = idata.itsMaxFrameArray;
/* 2777 */     if (maxFrameArray != emptyStackTop + idata.itsMaxStack + 1) {
/* 2778 */       Kit.codeBug();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2784 */     if (frame.stack != null && maxFrameArray <= frame.stack.length) {
/*      */       
/* 2786 */       stackReuse = true;
/* 2787 */       stack = frame.stack;
/* 2788 */       stackAttributes = frame.stackAttributes;
/* 2789 */       sDbl = frame.sDbl;
/*      */     } else {
/* 2791 */       stackReuse = false;
/* 2792 */       stack = new Object[maxFrameArray];
/* 2793 */       stackAttributes = new int[maxFrameArray];
/* 2794 */       sDbl = new double[maxFrameArray];
/*      */     } 
/*      */     
/* 2797 */     int varCount = idata.getParamAndVarCount();
/* 2798 */     for (int i = 0; i < varCount; i++) {
/* 2799 */       if (idata.getParamOrVarConst(i))
/* 2800 */         stackAttributes[i] = 13; 
/*      */     } 
/* 2802 */     int definedArgs = idata.argCount;
/* 2803 */     if (definedArgs > argCount) definedArgs = argCount;
/*      */ 
/*      */ 
/*      */     
/* 2807 */     frame.parentFrame = parentFrame;
/* 2808 */     frame.frameIndex = (parentFrame == null) ? 0 : (parentFrame.frameIndex + 1);
/*      */     
/* 2810 */     if (frame.frameIndex > cx.getMaximumInterpreterStackDepth())
/*      */     {
/* 2812 */       throw Context.reportRuntimeError("Exceeded maximum stack depth");
/*      */     }
/* 2814 */     frame.frozen = false;
/*      */     
/* 2816 */     frame.fnOrScript = fnOrScript;
/* 2817 */     frame.idata = idata;
/*      */     
/* 2819 */     frame.stack = stack;
/* 2820 */     frame.stackAttributes = stackAttributes;
/* 2821 */     frame.sDbl = sDbl;
/* 2822 */     frame.varSource = frame;
/* 2823 */     frame.localShift = idata.itsMaxVars;
/* 2824 */     frame.emptyStackTop = emptyStackTop;
/*      */     
/* 2826 */     frame.debuggerFrame = debuggerFrame;
/* 2827 */     frame.useActivation = useActivation;
/*      */     
/* 2829 */     frame.thisObj = thisObj;
/*      */ 
/*      */ 
/*      */     
/* 2833 */     frame.result = Undefined.instance;
/* 2834 */     frame.pc = 0;
/* 2835 */     frame.pcPrevBranch = 0;
/* 2836 */     frame.pcSourceLineStart = idata.firstLinePC;
/* 2837 */     frame.scope = scope;
/*      */     
/* 2839 */     frame.savedStackTop = emptyStackTop;
/* 2840 */     frame.savedCallOp = 0;
/*      */     
/* 2842 */     System.arraycopy(args, argShift, stack, 0, definedArgs);
/* 2843 */     if (argsDbl != null)
/* 2844 */       System.arraycopy(argsDbl, argShift, sDbl, 0, definedArgs); 
/*      */     int j;
/* 2846 */     for (j = definedArgs; j != idata.itsMaxVars; j++) {
/* 2847 */       stack[j] = Undefined.instance;
/*      */     }
/* 2849 */     if (stackReuse)
/*      */     {
/*      */       
/* 2852 */       for (j = emptyStackTop + 1; j != stack.length; j++) {
/* 2853 */         stack[j] = null;
/*      */       }
/*      */     }
/*      */     
/* 2857 */     enterFrame(cx, frame, args, false);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isFrameEnterExitRequired(CallFrame frame) {
/* 2862 */     return (frame.debuggerFrame != null || frame.idata.itsNeedsActivation);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void enterFrame(Context cx, CallFrame frame, Object[] args, boolean continuationRestart) {
/* 2868 */     boolean usesActivation = frame.idata.itsNeedsActivation;
/* 2869 */     boolean isDebugged = (frame.debuggerFrame != null);
/* 2870 */     if (usesActivation || isDebugged) {
/* 2871 */       Scriptable scope = frame.scope;
/* 2872 */       if (scope == null) {
/* 2873 */         Kit.codeBug();
/* 2874 */       } else if (continuationRestart) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2884 */         while (scope instanceof NativeWith) {
/* 2885 */           scope = scope.getParentScope();
/* 2886 */           if (scope == null || (frame.parentFrame != null && frame.parentFrame.scope == scope)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2892 */             Kit.codeBug();
/*      */ 
/*      */ 
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2902 */       if (isDebugged) {
/* 2903 */         frame.debuggerFrame.onEnter(cx, scope, frame.thisObj, args);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 2908 */       if (usesActivation) {
/* 2909 */         ScriptRuntime.enterActivationFunction(cx, scope);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void exitFrame(Context cx, CallFrame frame, Object throwable) {
/* 2917 */     if (frame.idata.itsNeedsActivation) {
/* 2918 */       ScriptRuntime.exitActivationFunction(cx);
/*      */     }
/*      */     
/* 2921 */     if (frame.debuggerFrame != null) {
/*      */       try {
/* 2923 */         if (throwable instanceof Throwable) {
/* 2924 */           frame.debuggerFrame.onExit(cx, true, throwable);
/*      */         } else {
/*      */           Object result;
/* 2927 */           ContinuationJump cjump = (ContinuationJump)throwable;
/* 2928 */           if (cjump == null) {
/* 2929 */             result = frame.result;
/*      */           } else {
/* 2931 */             result = cjump.result;
/*      */           } 
/* 2933 */           if (result == UniqueTag.DOUBLE_MARK) {
/*      */             double resultDbl;
/* 2935 */             if (cjump == null) {
/* 2936 */               resultDbl = frame.resultDbl;
/*      */             } else {
/* 2938 */               resultDbl = cjump.resultDbl;
/*      */             } 
/* 2940 */             result = ScriptRuntime.wrapNumber(resultDbl);
/*      */           } 
/* 2942 */           frame.debuggerFrame.onExit(cx, false, result);
/*      */         } 
/* 2944 */       } catch (Throwable ex) {
/* 2945 */         System.err.println("RHINO USAGE WARNING: onExit terminated with exception");
/*      */         
/* 2947 */         ex.printStackTrace(System.err);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void setCallResult(CallFrame frame, Object callResult, double callResultDbl) {
/* 2956 */     if (frame.savedCallOp == 38) {
/* 2957 */       frame.stack[frame.savedStackTop] = callResult;
/* 2958 */       frame.sDbl[frame.savedStackTop] = callResultDbl;
/* 2959 */     } else if (frame.savedCallOp == 30) {
/*      */ 
/*      */ 
/*      */       
/* 2963 */       if (callResult instanceof Scriptable) {
/* 2964 */         frame.stack[frame.savedStackTop] = callResult;
/*      */       }
/*      */     } else {
/* 2967 */       Kit.codeBug();
/*      */     } 
/* 2969 */     frame.savedCallOp = 0;
/*      */   }
/*      */   
/*      */   public static NativeContinuation captureContinuation(Context cx) {
/* 2973 */     if (cx.lastInterpreterFrame == null || !(cx.lastInterpreterFrame instanceof CallFrame))
/*      */     {
/*      */       
/* 2976 */       throw new IllegalStateException("Interpreter frames not found");
/*      */     }
/* 2978 */     return captureContinuation(cx, (CallFrame)cx.lastInterpreterFrame, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static NativeContinuation captureContinuation(Context cx, CallFrame frame, boolean requireContinuationsTopFrame) {
/* 2984 */     NativeContinuation c = new NativeContinuation();
/* 2985 */     ScriptRuntime.setObjectProtoAndParent(c, ScriptRuntime.getTopCallScope(cx));
/*      */ 
/*      */ 
/*      */     
/* 2989 */     CallFrame x = frame;
/* 2990 */     CallFrame outermost = frame;
/* 2991 */     while (x != null && !x.frozen) {
/* 2992 */       x.frozen = true;
/*      */       
/* 2994 */       for (int i = x.savedStackTop + 1; i != x.stack.length; i++) {
/*      */         
/* 2996 */         x.stack[i] = null;
/* 2997 */         x.stackAttributes[i] = 0;
/*      */       } 
/* 2999 */       if (x.savedCallOp == 38)
/*      */       
/* 3001 */       { x.stack[x.savedStackTop] = null; }
/*      */       
/* 3003 */       else if (x.savedCallOp != 30) { Kit.codeBug(); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3008 */       outermost = x;
/* 3009 */       x = x.parentFrame;
/*      */     } 
/*      */     
/* 3012 */     if (requireContinuationsTopFrame) {
/* 3013 */       while (outermost.parentFrame != null) {
/* 3014 */         outermost = outermost.parentFrame;
/*      */       }
/* 3016 */       if (!outermost.isContinuationsTopFrame) {
/* 3017 */         throw new IllegalStateException("Cannot capture continuation from JavaScript code not called directly by executeScriptWithContinuations or callFunctionWithContinuations");
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3024 */     c.initImplementation(frame);
/* 3025 */     return c;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int stack_int32(CallFrame frame, int i) {
/* 3030 */     Object x = frame.stack[i];
/* 3031 */     if (x == UniqueTag.DOUBLE_MARK) {
/* 3032 */       return ScriptRuntime.toInt32(frame.sDbl[i]);
/*      */     }
/* 3034 */     return ScriptRuntime.toInt32(x);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static double stack_double(CallFrame frame, int i) {
/* 3040 */     Object x = frame.stack[i];
/* 3041 */     if (x != UniqueTag.DOUBLE_MARK) {
/* 3042 */       return ScriptRuntime.toNumber(x);
/*      */     }
/* 3044 */     return frame.sDbl[i];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean stack_boolean(CallFrame frame, int i) {
/* 3050 */     Object x = frame.stack[i];
/* 3051 */     if (x == Boolean.TRUE)
/* 3052 */       return true; 
/* 3053 */     if (x == Boolean.FALSE)
/* 3054 */       return false; 
/* 3055 */     if (x == UniqueTag.DOUBLE_MARK) {
/* 3056 */       double d = frame.sDbl[i];
/* 3057 */       return (d == d && d != 0.0D);
/* 3058 */     }  if (x == null || x == Undefined.instance)
/* 3059 */       return false; 
/* 3060 */     if (x instanceof Number) {
/* 3061 */       double d = ((Number)x).doubleValue();
/* 3062 */       return (d == d && d != 0.0D);
/* 3063 */     }  if (x instanceof Boolean) {
/* 3064 */       return ((Boolean)x).booleanValue();
/*      */     }
/* 3066 */     return ScriptRuntime.toBoolean(x);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void doAdd(Object[] stack, double[] sDbl, int stackTop, Context cx) {
/*      */     double d;
/*      */     boolean leftRightOrder;
/* 3073 */     Object rhs = stack[stackTop + 1];
/* 3074 */     Object lhs = stack[stackTop];
/*      */ 
/*      */     
/* 3077 */     if (rhs == UniqueTag.DOUBLE_MARK) {
/* 3078 */       d = sDbl[stackTop + 1];
/* 3079 */       if (lhs == UniqueTag.DOUBLE_MARK) {
/* 3080 */         sDbl[stackTop] = sDbl[stackTop] + d;
/*      */         return;
/*      */       } 
/* 3083 */       leftRightOrder = true;
/*      */     }
/* 3085 */     else if (lhs == UniqueTag.DOUBLE_MARK) {
/* 3086 */       d = sDbl[stackTop];
/* 3087 */       lhs = rhs;
/* 3088 */       leftRightOrder = false;
/*      */     } else {
/*      */       
/* 3091 */       if (lhs instanceof Scriptable || rhs instanceof Scriptable) {
/* 3092 */         stack[stackTop] = ScriptRuntime.add(lhs, rhs, cx);
/* 3093 */       } else if (lhs instanceof CharSequence || rhs instanceof CharSequence) {
/* 3094 */         CharSequence lstr = ScriptRuntime.toCharSequence(lhs);
/* 3095 */         CharSequence rstr = ScriptRuntime.toCharSequence(rhs);
/* 3096 */         stack[stackTop] = new ConsString(lstr, rstr);
/*      */       } else {
/* 3098 */         double lDbl = (lhs instanceof Number) ? ((Number)lhs).doubleValue() : ScriptRuntime.toNumber(lhs);
/*      */         
/* 3100 */         double rDbl = (rhs instanceof Number) ? ((Number)rhs).doubleValue() : ScriptRuntime.toNumber(rhs);
/*      */         
/* 3102 */         stack[stackTop] = UniqueTag.DOUBLE_MARK;
/* 3103 */         sDbl[stackTop] = lDbl + rDbl;
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 3109 */     if (lhs instanceof Scriptable) {
/* 3110 */       rhs = ScriptRuntime.wrapNumber(d);
/* 3111 */       if (!leftRightOrder) {
/* 3112 */         Object tmp = lhs;
/* 3113 */         lhs = rhs;
/* 3114 */         rhs = tmp;
/*      */       } 
/* 3116 */       stack[stackTop] = ScriptRuntime.add(lhs, rhs, cx);
/* 3117 */     } else if (lhs instanceof CharSequence) {
/* 3118 */       CharSequence lstr = (CharSequence)lhs;
/* 3119 */       CharSequence rstr = ScriptRuntime.toCharSequence(Double.valueOf(d));
/* 3120 */       if (leftRightOrder) {
/* 3121 */         stack[stackTop] = new ConsString(lstr, rstr);
/*      */       } else {
/* 3123 */         stack[stackTop] = new ConsString(rstr, lstr);
/*      */       } 
/*      */     } else {
/* 3126 */       double lDbl = (lhs instanceof Number) ? ((Number)lhs).doubleValue() : ScriptRuntime.toNumber(lhs);
/*      */       
/* 3128 */       stack[stackTop] = UniqueTag.DOUBLE_MARK;
/* 3129 */       sDbl[stackTop] = lDbl + d;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static int doArithmetic(CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop) {
/* 3135 */     double rDbl = stack_double(frame, stackTop);
/* 3136 */     stackTop--;
/* 3137 */     double lDbl = stack_double(frame, stackTop);
/* 3138 */     stack[stackTop] = UniqueTag.DOUBLE_MARK;
/* 3139 */     switch (op) {
/*      */       case 22:
/* 3141 */         lDbl -= rDbl;
/*      */         break;
/*      */       case 23:
/* 3144 */         lDbl *= rDbl;
/*      */         break;
/*      */       case 24:
/* 3147 */         lDbl /= rDbl;
/*      */         break;
/*      */       case 25:
/* 3150 */         lDbl %= rDbl;
/*      */         break;
/*      */     } 
/* 3153 */     sDbl[stackTop] = lDbl;
/* 3154 */     return stackTop;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object[] getArgsArray(Object[] stack, double[] sDbl, int shift, int count) {
/* 3160 */     if (count == 0) {
/* 3161 */       return ScriptRuntime.emptyArgs;
/*      */     }
/* 3163 */     Object[] args = new Object[count];
/* 3164 */     for (int i = 0; i != count; i++, shift++) {
/* 3165 */       Object val = stack[shift];
/* 3166 */       if (val == UniqueTag.DOUBLE_MARK) {
/* 3167 */         val = ScriptRuntime.wrapNumber(sDbl[shift]);
/*      */       }
/* 3169 */       args[i] = val;
/*      */     } 
/* 3171 */     return args;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addInstructionCount(Context cx, CallFrame frame, int extra) {
/* 3177 */     cx.instructionCount += frame.pc - frame.pcPrevBranch + extra;
/* 3178 */     if (cx.instructionCount > cx.instructionThreshold) {
/* 3179 */       cx.observeInstructionCount(cx.instructionCount);
/* 3180 */       cx.instructionCount = 0;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Interpreter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */