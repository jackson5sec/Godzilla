/*    */ package com.kitfox.svg.animation.parser;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleNode
/*    */   implements Node
/*    */ {
/*    */   protected Node parent;
/*    */   protected Node[] children;
/*    */   protected int id;
/*    */   protected Object value;
/*    */   protected AnimTimeParser parser;
/*    */   
/*    */   public SimpleNode(int i) {
/* 15 */     this.id = i;
/*    */   }
/*    */   
/*    */   public SimpleNode(AnimTimeParser p, int i) {
/* 19 */     this(i);
/* 20 */     this.parser = p;
/*    */   }
/*    */ 
/*    */   
/*    */   public void jjtOpen() {}
/*    */   
/*    */   public void jjtClose() {}
/*    */   
/*    */   public void jjtSetParent(Node n) {
/* 29 */     this.parent = n; } public Node jjtGetParent() {
/* 30 */     return this.parent;
/*    */   }
/*    */   public void jjtAddChild(Node n, int i) {
/* 33 */     if (this.children == null) {
/* 34 */       this.children = new Node[i + 1];
/* 35 */     } else if (i >= this.children.length) {
/* 36 */       Node[] c = new Node[i + 1];
/* 37 */       System.arraycopy(this.children, 0, c, 0, this.children.length);
/* 38 */       this.children = c;
/*    */     } 
/* 40 */     this.children[i] = n;
/*    */   }
/*    */   
/*    */   public Node jjtGetChild(int i) {
/* 44 */     return this.children[i];
/*    */   }
/*    */   
/*    */   public int jjtGetNumChildren() {
/* 48 */     return (this.children == null) ? 0 : this.children.length;
/*    */   }
/*    */   
/* 51 */   public void jjtSetValue(Object aValue) { this.value = aValue; } public Object jjtGetValue() {
/* 52 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return AnimTimeParserTreeConstants.jjtNodeName[this.id];
/*    */   }
/*    */   
/*    */   public String toString(String prefix) {
/* 65 */     return prefix + toString();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void dumpString(String s) {
/* 70 */     System.out.println(s);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void dump(String prefix) {
/* 76 */     dumpString(toString(prefix));
/* 77 */     if (this.children != null)
/* 78 */       for (int i = 0; i < this.children.length; i++) {
/* 79 */         SimpleNode n = (SimpleNode)this.children[i];
/* 80 */         if (n != null) {
/* 81 */           n.dump(prefix + " ");
/*    */         }
/*    */       }  
/*    */   }
/*    */   
/*    */   public int getId() {
/* 87 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\parser\SimpleNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */