/*     */ package com.kitfox.svg.animation.parser;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JJTAnimTimeParserState
/*     */   implements Serializable
/*     */ {
/*  15 */   private List<Node> nodes = new ArrayList<Node>();
/*  16 */   private List<Integer> marks = new ArrayList<Integer>();
/*  17 */   private int sp = 0;
/*  18 */   private int mk = 0;
/*     */ 
/*     */   
/*     */   private boolean node_created;
/*     */ 
/*     */   
/*     */   public boolean nodeCreated() {
/*  25 */     return this.node_created;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  31 */     this.nodes.clear();
/*  32 */     this.marks.clear();
/*  33 */     this.sp = 0;
/*  34 */     this.mk = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node rootNode() {
/*  40 */     return this.nodes.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushNode(Node n) {
/*  45 */     this.nodes.add(n);
/*  46 */     this.sp++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node popNode() {
/*  52 */     this.sp--;
/*  53 */     if (this.sp < this.mk) {
/*  54 */       this.mk = ((Integer)this.marks.remove(this.marks.size() - 1)).intValue();
/*     */     }
/*  56 */     return this.nodes.remove(this.nodes.size() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Node peekNode() {
/*  61 */     return this.nodes.get(this.nodes.size() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int nodeArity() {
/*  67 */     return this.sp - this.mk;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearNodeScope(Node n) {
/*  72 */     while (this.sp > this.mk) {
/*  73 */       popNode();
/*     */     }
/*  75 */     this.mk = ((Integer)this.marks.remove(this.marks.size() - 1)).intValue();
/*     */   }
/*     */   
/*     */   public void openNodeScope(Node n) {
/*  79 */     this.marks.add(Integer.valueOf(this.mk));
/*  80 */     this.mk = this.sp;
/*  81 */     n.jjtOpen();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeNodeScope(Node n, int numIn) {
/*  89 */     this.mk = ((Integer)this.marks.remove(this.marks.size() - 1)).intValue();
/*  90 */     int num = numIn;
/*  91 */     while (num-- > 0) {
/*  92 */       Node c = popNode();
/*  93 */       c.jjtSetParent(n);
/*  94 */       n.jjtAddChild(c, num);
/*     */     } 
/*  96 */     n.jjtClose();
/*  97 */     pushNode(n);
/*  98 */     this.node_created = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeNodeScope(Node n, boolean condition) {
/* 108 */     if (condition) {
/* 109 */       int a = nodeArity();
/* 110 */       this.mk = ((Integer)this.marks.remove(this.marks.size() - 1)).intValue();
/* 111 */       while (a-- > 0) {
/* 112 */         Node c = popNode();
/* 113 */         c.jjtSetParent(n);
/* 114 */         n.jjtAddChild(c, a);
/*     */       } 
/* 116 */       n.jjtClose();
/* 117 */       pushNode(n);
/* 118 */       this.node_created = true;
/*     */     } else {
/* 120 */       this.mk = ((Integer)this.marks.remove(this.marks.size() - 1)).intValue();
/* 121 */       this.node_created = false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\parser\JJTAnimTimeParserState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */