/*     */ package org.mozilla.javascript.ast;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.mozilla.javascript.Kit;
/*     */ import org.mozilla.javascript.Node;
/*     */ import org.mozilla.javascript.Token;
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
/*     */ public abstract class AstNode
/*     */   extends Node
/*     */   implements Comparable<AstNode>
/*     */ {
/*  66 */   protected int position = -1;
/*  67 */   protected int length = 1;
/*     */   
/*     */   protected AstNode parent;
/*  70 */   private static Map<Integer, String> operatorNames = new HashMap<Integer, String>();
/*     */ 
/*     */   
/*     */   static {
/*  74 */     operatorNames.put(Integer.valueOf(52), "in");
/*  75 */     operatorNames.put(Integer.valueOf(32), "typeof");
/*  76 */     operatorNames.put(Integer.valueOf(53), "instanceof");
/*  77 */     operatorNames.put(Integer.valueOf(31), "delete");
/*  78 */     operatorNames.put(Integer.valueOf(89), ",");
/*  79 */     operatorNames.put(Integer.valueOf(103), ":");
/*  80 */     operatorNames.put(Integer.valueOf(104), "||");
/*  81 */     operatorNames.put(Integer.valueOf(105), "&&");
/*  82 */     operatorNames.put(Integer.valueOf(106), "++");
/*  83 */     operatorNames.put(Integer.valueOf(107), "--");
/*  84 */     operatorNames.put(Integer.valueOf(9), "|");
/*  85 */     operatorNames.put(Integer.valueOf(10), "^");
/*  86 */     operatorNames.put(Integer.valueOf(11), "&");
/*  87 */     operatorNames.put(Integer.valueOf(12), "==");
/*  88 */     operatorNames.put(Integer.valueOf(13), "!=");
/*  89 */     operatorNames.put(Integer.valueOf(14), "<");
/*  90 */     operatorNames.put(Integer.valueOf(16), ">");
/*  91 */     operatorNames.put(Integer.valueOf(15), "<=");
/*  92 */     operatorNames.put(Integer.valueOf(17), ">=");
/*  93 */     operatorNames.put(Integer.valueOf(18), "<<");
/*  94 */     operatorNames.put(Integer.valueOf(19), ">>");
/*  95 */     operatorNames.put(Integer.valueOf(20), ">>>");
/*  96 */     operatorNames.put(Integer.valueOf(21), "+");
/*  97 */     operatorNames.put(Integer.valueOf(22), "-");
/*  98 */     operatorNames.put(Integer.valueOf(23), "*");
/*  99 */     operatorNames.put(Integer.valueOf(24), "/");
/* 100 */     operatorNames.put(Integer.valueOf(25), "%");
/* 101 */     operatorNames.put(Integer.valueOf(26), "!");
/* 102 */     operatorNames.put(Integer.valueOf(27), "~");
/* 103 */     operatorNames.put(Integer.valueOf(28), "+");
/* 104 */     operatorNames.put(Integer.valueOf(29), "-");
/* 105 */     operatorNames.put(Integer.valueOf(46), "===");
/* 106 */     operatorNames.put(Integer.valueOf(47), "!==");
/* 107 */     operatorNames.put(Integer.valueOf(90), "=");
/* 108 */     operatorNames.put(Integer.valueOf(91), "|=");
/* 109 */     operatorNames.put(Integer.valueOf(93), "&=");
/* 110 */     operatorNames.put(Integer.valueOf(94), "<<=");
/* 111 */     operatorNames.put(Integer.valueOf(95), ">>=");
/* 112 */     operatorNames.put(Integer.valueOf(96), ">>>=");
/* 113 */     operatorNames.put(Integer.valueOf(97), "+=");
/* 114 */     operatorNames.put(Integer.valueOf(98), "-=");
/* 115 */     operatorNames.put(Integer.valueOf(99), "*=");
/* 116 */     operatorNames.put(Integer.valueOf(100), "/=");
/* 117 */     operatorNames.put(Integer.valueOf(101), "%=");
/* 118 */     operatorNames.put(Integer.valueOf(92), "^=");
/* 119 */     operatorNames.put(Integer.valueOf(126), "void");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PositionComparator
/*     */     implements Comparator<AstNode>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     
/*     */     public int compare(AstNode n1, AstNode n2) {
/* 131 */       return n1.position - n2.position;
/*     */     }
/*     */   }
/*     */   
/*     */   public AstNode() {
/* 136 */     super(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode(int pos) {
/* 144 */     this();
/* 145 */     this.position = pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode(int pos, int len) {
/* 155 */     this();
/* 156 */     this.position = pos;
/* 157 */     this.length = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPosition() {
/* 164 */     return this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosition(int position) {
/* 171 */     this.position = position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAbsolutePosition() {
/* 180 */     int pos = this.position;
/* 181 */     AstNode parent = this.parent;
/* 182 */     while (parent != null) {
/* 183 */       pos += parent.getPosition();
/* 184 */       parent = parent.getParent();
/*     */     } 
/* 186 */     return pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/* 193 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLength(int length) {
/* 200 */     this.length = length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBounds(int position, int end) {
/* 208 */     setPosition(position);
/* 209 */     setLength(end - position);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRelative(int parentPosition) {
/* 220 */     this.position -= parentPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getParent() {
/* 227 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(AstNode parent) {
/* 236 */     if (parent == this.parent) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 241 */     if (this.parent != null) {
/* 242 */       setRelative(-this.parent.getPosition());
/*     */     }
/*     */     
/* 245 */     this.parent = parent;
/* 246 */     if (parent != null) {
/* 247 */       setRelative(parent.getPosition());
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
/*     */   public void addChild(AstNode kid) {
/* 260 */     assertNotNull(kid);
/* 261 */     int end = kid.getPosition() + kid.getLength();
/* 262 */     setLength(end - getPosition());
/* 263 */     addChildToBack(kid);
/* 264 */     kid.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstRoot getAstRoot() {
/* 273 */     AstNode parent = this;
/* 274 */     while (parent != null && !(parent instanceof AstRoot)) {
/* 275 */       parent = parent.getParent();
/*     */     }
/* 277 */     return (AstRoot)parent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSource() {
/* 300 */     return toSource(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String makeIndent(int indent) {
/* 308 */     StringBuilder sb = new StringBuilder();
/* 309 */     for (int i = 0; i < indent; i++) {
/* 310 */       sb.append("  ");
/*     */     }
/* 312 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String shortName() {
/* 320 */     String classname = getClass().getName();
/* 321 */     int last = classname.lastIndexOf(".");
/* 322 */     return classname.substring(last + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String operatorToString(int op) {
/* 331 */     String result = operatorNames.get(Integer.valueOf(op));
/* 332 */     if (result == null)
/* 333 */       throw new IllegalArgumentException("Invalid operator: " + op); 
/* 334 */     return result;
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
/*     */   public boolean hasSideEffects() {
/* 360 */     switch (getType()) {
/*     */       case -1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 30:
/*     */       case 31:
/*     */       case 35:
/*     */       case 37:
/*     */       case 38:
/*     */       case 50:
/*     */       case 51:
/*     */       case 56:
/*     */       case 57:
/*     */       case 64:
/*     */       case 68:
/*     */       case 69:
/*     */       case 70:
/*     */       case 72:
/*     */       case 81:
/*     */       case 82:
/*     */       case 90:
/*     */       case 91:
/*     */       case 92:
/*     */       case 93:
/*     */       case 94:
/*     */       case 95:
/*     */       case 96:
/*     */       case 97:
/*     */       case 98:
/*     */       case 99:
/*     */       case 100:
/*     */       case 101:
/*     */       case 106:
/*     */       case 107:
/*     */       case 109:
/*     */       case 110:
/*     */       case 111:
/*     */       case 112:
/*     */       case 113:
/*     */       case 114:
/*     */       case 117:
/*     */       case 118:
/*     */       case 119:
/*     */       case 120:
/*     */       case 121:
/*     */       case 122:
/*     */       case 123:
/*     */       case 124:
/*     */       case 125:
/*     */       case 129:
/*     */       case 130:
/*     */       case 131:
/*     */       case 132:
/*     */       case 134:
/*     */       case 135:
/*     */       case 139:
/*     */       case 140:
/*     */       case 141:
/*     */       case 142:
/*     */       case 153:
/*     */       case 154:
/*     */       case 158:
/*     */       case 159:
/* 428 */         return true;
/*     */     } 
/*     */     
/* 431 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertNotNull(Object arg) {
/* 441 */     if (arg == null) {
/* 442 */       throw new IllegalArgumentException("arg cannot be null");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T extends AstNode> void printList(List<T> items, StringBuilder sb) {
/* 452 */     int max = items.size();
/* 453 */     int count = 0;
/* 454 */     for (AstNode item : items) {
/* 455 */       sb.append(item.toSource(0));
/* 456 */       if (count++ < max - 1) {
/* 457 */         sb.append(", "); continue;
/* 458 */       }  if (item instanceof EmptyExpression) {
/* 459 */         sb.append(",");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RuntimeException codeBug() throws RuntimeException {
/* 470 */     throw Kit.codeBug();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FunctionNode getEnclosingFunction() {
/* 491 */     AstNode parent = getParent();
/* 492 */     while (parent != null && !(parent instanceof FunctionNode)) {
/* 493 */       parent = parent.getParent();
/*     */     }
/* 495 */     return (FunctionNode)parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scope getEnclosingScope() {
/* 506 */     AstNode parent = getParent();
/* 507 */     while (parent != null && !(parent instanceof Scope)) {
/* 508 */       parent = parent.getParent();
/*     */     }
/* 510 */     return (Scope)parent;
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
/*     */   public int compareTo(AstNode other) {
/* 525 */     if (equals(other)) return 0; 
/* 526 */     int abs1 = getAbsolutePosition();
/* 527 */     int abs2 = other.getAbsolutePosition();
/* 528 */     if (abs1 < abs2) return -1; 
/* 529 */     if (abs2 < abs1) return 1; 
/* 530 */     int len1 = getLength();
/* 531 */     int len2 = other.getLength();
/* 532 */     if (len1 < len2) return -1; 
/* 533 */     if (len2 < len1) return 1; 
/* 534 */     return hashCode() - other.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int depth() {
/* 543 */     return (this.parent == null) ? 0 : (1 + this.parent.depth());
/*     */   }
/*     */   
/*     */   protected static class DebugPrintVisitor implements NodeVisitor { private StringBuilder buffer;
/*     */     private static final int DEBUG_INDENT = 2;
/*     */     
/*     */     public DebugPrintVisitor(StringBuilder buf) {
/* 550 */       this.buffer = buf;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 554 */       return this.buffer.toString();
/*     */     }
/*     */     private String makeIndent(int depth) {
/* 557 */       StringBuilder sb = new StringBuilder(2 * depth);
/* 558 */       for (int i = 0; i < 2 * depth; i++) {
/* 559 */         sb.append(" ");
/*     */       }
/* 561 */       return sb.toString();
/*     */     }
/*     */     public boolean visit(AstNode node) {
/* 564 */       int tt = node.getType();
/* 565 */       String name = Token.typeToName(tt);
/* 566 */       this.buffer.append(node.getAbsolutePosition()).append("\t");
/* 567 */       this.buffer.append(makeIndent(node.depth()));
/* 568 */       this.buffer.append(name).append(" ");
/* 569 */       this.buffer.append(node.getPosition()).append(" ");
/* 570 */       this.buffer.append(node.getLength());
/* 571 */       if (tt == 39) {
/* 572 */         this.buffer.append(" ").append(((Name)node).getIdentifier());
/*     */       }
/* 574 */       this.buffer.append("\n");
/* 575 */       return true;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineno() {
/* 586 */     if (this.lineno != -1)
/* 587 */       return this.lineno; 
/* 588 */     if (this.parent != null)
/* 589 */       return this.parent.getLineno(); 
/* 590 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String debugPrint() {
/* 600 */     DebugPrintVisitor dpv = new DebugPrintVisitor(new StringBuilder(1000));
/* 601 */     visit(dpv);
/* 602 */     return dpv.toString();
/*     */   }
/*     */   
/*     */   public abstract String toSource(int paramInt);
/*     */   
/*     */   public abstract void visit(NodeVisitor paramNodeVisitor);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\AstNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */