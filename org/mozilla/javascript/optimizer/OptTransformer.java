/*    */ package org.mozilla.javascript.optimizer;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.mozilla.javascript.Kit;
/*    */ import org.mozilla.javascript.Node;
/*    */ import org.mozilla.javascript.NodeTransformer;
/*    */ import org.mozilla.javascript.ObjArray;
/*    */ import org.mozilla.javascript.ast.ScriptNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class OptTransformer
/*    */   extends NodeTransformer
/*    */ {
/*    */   private Map<String, OptFunctionNode> possibleDirectCalls;
/*    */   private ObjArray directCallTargets;
/*    */   
/*    */   OptTransformer(Map<String, OptFunctionNode> possibleDirectCalls, ObjArray directCallTargets) {
/* 23 */     this.possibleDirectCalls = possibleDirectCalls;
/* 24 */     this.directCallTargets = directCallTargets;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void visitNew(Node node, ScriptNode tree) {
/* 29 */     detectDirectCall(node, tree);
/* 30 */     super.visitNew(node, tree);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void visitCall(Node node, ScriptNode tree) {
/* 35 */     detectDirectCall(node, tree);
/* 36 */     super.visitCall(node, tree);
/*    */   }
/*    */ 
/*    */   
/*    */   private void detectDirectCall(Node node, ScriptNode tree) {
/* 41 */     if (tree.getType() == 109) {
/* 42 */       Node left = node.getFirstChild();
/*    */ 
/*    */       
/* 45 */       int argCount = 0;
/* 46 */       Node arg = left.getNext();
/* 47 */       while (arg != null) {
/* 48 */         arg = arg.getNext();
/* 49 */         argCount++;
/*    */       } 
/*    */       
/* 52 */       if (argCount == 0) {
/* 53 */         (OptFunctionNode.get(tree)).itsContainsCalls0 = true;
/*    */       }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 68 */       if (this.possibleDirectCalls != null) {
/* 69 */         String targetName = null;
/* 70 */         if (left.getType() == 39) {
/* 71 */           targetName = left.getString();
/* 72 */         } else if (left.getType() == 33) {
/* 73 */           targetName = left.getFirstChild().getNext().getString();
/* 74 */         } else if (left.getType() == 34) {
/* 75 */           throw Kit.codeBug();
/*    */         } 
/* 77 */         if (targetName != null) {
/*    */           
/* 79 */           OptFunctionNode ofn = this.possibleDirectCalls.get(targetName);
/* 80 */           if (ofn != null && argCount == ofn.fnode.getParamCount() && !ofn.fnode.requiresActivation())
/*    */           {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 87 */             if (argCount <= 32) {
/* 88 */               node.putProp(9, ofn);
/* 89 */               if (!ofn.isTargetOfDirectCall()) {
/* 90 */                 int index = this.directCallTargets.size();
/* 91 */                 this.directCallTargets.add(ofn);
/* 92 */                 ofn.setDirectTargetIndex(index);
/*    */               } 
/*    */             } 
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\optimizer\OptTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */