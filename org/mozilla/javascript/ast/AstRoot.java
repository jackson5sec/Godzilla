/*     */ package org.mozilla.javascript.ast;
/*     */ 
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import org.mozilla.javascript.Node;
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
/*     */ public class AstRoot
/*     */   extends ScriptNode
/*     */ {
/*     */   private SortedSet<Comment> comments;
/*     */   private boolean inStrictMode;
/*     */   
/*     */   public AstRoot() {}
/*     */   
/*     */   public AstRoot(int pos) {
/*  38 */     super(pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<Comment> getComments() {
/*  46 */     return this.comments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComments(SortedSet<Comment> comments) {
/*  55 */     if (comments == null) {
/*  56 */       this.comments = null;
/*     */     } else {
/*  58 */       if (this.comments != null)
/*  59 */         this.comments.clear(); 
/*  60 */       for (Comment c : comments) {
/*  61 */         addComment(c);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComment(Comment comment) {
/*  71 */     assertNotNull(comment);
/*  72 */     if (this.comments == null) {
/*  73 */       this.comments = new TreeSet<Comment>(new AstNode.PositionComparator());
/*     */     }
/*  75 */     this.comments.add(comment);
/*  76 */     comment.setParent(this);
/*     */   }
/*     */   
/*     */   public void setInStrictMode(boolean inStrictMode) {
/*  80 */     this.inStrictMode = inStrictMode;
/*     */   }
/*     */   
/*     */   public boolean isInStrictMode() {
/*  84 */     return this.inStrictMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitComments(NodeVisitor visitor) {
/*  95 */     if (this.comments != null) {
/*  96 */       for (Comment c : this.comments) {
/*  97 */         visitor.visit(c);
/*     */       }
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
/*     */   public void visitAll(NodeVisitor visitor) {
/* 110 */     visit(visitor);
/* 111 */     visitComments(visitor);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 116 */     StringBuilder sb = new StringBuilder();
/* 117 */     for (Node node : this) {
/* 118 */       sb.append(((AstNode)node).toSource(depth));
/*     */     }
/* 120 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String debugPrint() {
/* 128 */     AstNode.DebugPrintVisitor dpv = new AstNode.DebugPrintVisitor(new StringBuilder(1000));
/* 129 */     visitAll(dpv);
/* 130 */     return dpv.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkParentLinks() {
/* 139 */     visit(new NodeVisitor() {
/*     */           public boolean visit(AstNode node) {
/* 141 */             int type = node.getType();
/* 142 */             if (type == 136)
/* 143 */               return true; 
/* 144 */             if (node.getParent() == null) {
/* 145 */               throw new IllegalStateException("No parent for node: " + node + "\n" + node.toSource(0));
/*     */             }
/*     */             
/* 148 */             return true;
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\AstRoot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */