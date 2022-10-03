/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.stackmap.BasicBlock;
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
/*     */ public class ControlFlow
/*     */ {
/*     */   private CtClass clazz;
/*     */   private MethodInfo methodInfo;
/*     */   private Block[] basicBlocks;
/*     */   private Frame[] frames;
/*     */   
/*     */   public ControlFlow(CtMethod method) throws BadBytecode {
/*  58 */     this(method.getDeclaringClass(), method.getMethodInfo2());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ControlFlow(CtClass ctclazz, MethodInfo minfo) throws BadBytecode {
/*  65 */     this.clazz = ctclazz;
/*  66 */     this.methodInfo = minfo;
/*  67 */     this.frames = null;
/*  68 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  77 */       .basicBlocks = (Block[])(new BasicBlock.Maker() { protected BasicBlock makeBlock(int pos) { return new ControlFlow.Block(pos, ControlFlow.this.methodInfo); } protected BasicBlock[] makeArray(int size) { return (BasicBlock[])new ControlFlow.Block[size]; } }).make(minfo);
/*  78 */     if (this.basicBlocks == null)
/*  79 */       this.basicBlocks = new Block[0]; 
/*  80 */     int size = this.basicBlocks.length;
/*  81 */     int[] counters = new int[size]; int i;
/*  82 */     for (i = 0; i < size; i++) {
/*  83 */       Block b = this.basicBlocks[i];
/*  84 */       b.index = i;
/*  85 */       b.entrances = new Block[b.incomings()];
/*  86 */       counters[i] = 0;
/*     */     } 
/*     */     
/*  89 */     for (i = 0; i < size; i++) {
/*  90 */       Block b = this.basicBlocks[i];
/*  91 */       for (int k = 0; k < b.exits(); k++) {
/*  92 */         Block e = b.exit(k);
/*  93 */         counters[e.index] = counters[e.index] + 1; e.entrances[counters[e.index]] = b;
/*     */       } 
/*     */       
/*  96 */       Catcher[] catchers = b.catchers();
/*  97 */       for (int j = 0; j < catchers.length; j++) {
/*  98 */         Block catchBlock = (catchers[j]).node;
/*  99 */         counters[catchBlock.index] = counters[catchBlock.index] + 1; catchBlock.entrances[counters[catchBlock.index]] = b;
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
/*     */   public Block[] basicBlocks() {
/* 111 */     return this.basicBlocks;
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
/*     */   public Frame frameAt(int pos) throws BadBytecode {
/* 123 */     if (this.frames == null) {
/* 124 */       this.frames = (new Analyzer()).analyze(this.clazz, this.methodInfo);
/*     */     }
/* 126 */     return this.frames[pos];
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
/*     */   public Node[] dominatorTree() {
/* 150 */     int size = this.basicBlocks.length;
/* 151 */     if (size == 0) {
/* 152 */       return null;
/*     */     }
/* 154 */     Node[] nodes = new Node[size];
/* 155 */     boolean[] visited = new boolean[size];
/* 156 */     int[] distance = new int[size];
/* 157 */     for (int i = 0; i < size; i++) {
/* 158 */       nodes[i] = new Node(this.basicBlocks[i]);
/* 159 */       visited[i] = false;
/*     */     } 
/*     */     
/* 162 */     Access access = new Access(nodes) {
/*     */         BasicBlock[] exits(ControlFlow.Node n) {
/* 164 */           return n.block.getExit();
/*     */         }
/* 166 */         BasicBlock[] entrances(ControlFlow.Node n) { return (BasicBlock[])n.block.entrances; }
/*     */       };
/* 168 */     nodes[0].makeDepth1stTree(null, visited, 0, distance, access);
/*     */     while (true) {
/* 170 */       for (int j = 0; j < size; j++)
/* 171 */         visited[j] = false; 
/* 172 */       if (!nodes[0].makeDominatorTree(visited, distance, access)) {
/* 173 */         Node.setChildren(nodes);
/* 174 */         return nodes;
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
/*     */   public Node[] postDominatorTree() {
/*     */     boolean changed;
/* 198 */     int size = this.basicBlocks.length;
/* 199 */     if (size == 0) {
/* 200 */       return null;
/*     */     }
/* 202 */     Node[] nodes = new Node[size];
/* 203 */     boolean[] visited = new boolean[size];
/* 204 */     int[] distance = new int[size];
/* 205 */     for (int i = 0; i < size; i++) {
/* 206 */       nodes[i] = new Node(this.basicBlocks[i]);
/* 207 */       visited[i] = false;
/*     */     } 
/*     */     
/* 210 */     Access access = new Access(nodes) {
/*     */         BasicBlock[] exits(ControlFlow.Node n) {
/* 212 */           return (BasicBlock[])n.block.entrances;
/*     */         } BasicBlock[] entrances(ControlFlow.Node n) {
/* 214 */           return n.block.getExit();
/*     */         }
/*     */       };
/* 217 */     int counter = 0;
/* 218 */     for (int j = 0; j < size; j++) {
/* 219 */       if ((nodes[j]).block.exits() == 0)
/* 220 */         counter = nodes[j].makeDepth1stTree(null, visited, counter, distance, access); 
/*     */     } 
/*     */     do {
/*     */       int k;
/* 224 */       for (k = 0; k < size; k++) {
/* 225 */         visited[k] = false;
/*     */       }
/* 227 */       changed = false;
/* 228 */       for (k = 0; k < size; k++)
/* 229 */       { if ((nodes[k]).block.exits() == 0 && 
/* 230 */           nodes[k].makeDominatorTree(visited, distance, access))
/* 231 */           changed = true;  } 
/* 232 */     } while (changed);
/*     */     
/* 234 */     Node.setChildren(nodes);
/* 235 */     return nodes;
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
/*     */   public static class Block
/*     */     extends BasicBlock
/*     */   {
/* 252 */     public Object clientData = null;
/*     */     
/*     */     int index;
/*     */     MethodInfo method;
/*     */     Block[] entrances;
/*     */     
/*     */     Block(int pos, MethodInfo minfo) {
/* 259 */       super(pos);
/* 260 */       this.method = minfo;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void toString2(StringBuffer sbuf) {
/* 265 */       super.toString2(sbuf);
/* 266 */       sbuf.append(", incoming{");
/* 267 */       for (int i = 0; i < this.entrances.length; i++) {
/* 268 */         sbuf.append((this.entrances[i]).position).append(", ");
/*     */       }
/* 270 */       sbuf.append("}");
/*     */     }
/*     */     BasicBlock[] getExit() {
/* 273 */       return this.exit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int index() {
/* 282 */       return this.index;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int position() {
/* 288 */       return this.position;
/*     */     }
/*     */ 
/*     */     
/*     */     public int length() {
/* 293 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public int incomings() {
/* 298 */       return this.incoming;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Block incoming(int n) {
/* 304 */       return this.entrances[n];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int exits() {
/* 311 */       return (this.exit == null) ? 0 : this.exit.length;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Block exit(int n) {
/* 319 */       return (Block)this.exit[n];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ControlFlow.Catcher[] catchers() {
/* 326 */       List<ControlFlow.Catcher> catchers = new ArrayList<>();
/* 327 */       BasicBlock.Catch c = this.toCatch;
/* 328 */       while (c != null) {
/* 329 */         catchers.add(new ControlFlow.Catcher(c));
/* 330 */         c = c.next;
/*     */       } 
/*     */       
/* 333 */       return catchers.<ControlFlow.Catcher>toArray(new ControlFlow.Catcher[catchers.size()]);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class Access { ControlFlow.Node[] all;
/*     */     
/* 339 */     Access(ControlFlow.Node[] nodes) { this.all = nodes; } ControlFlow.Node node(BasicBlock b) {
/* 340 */       return this.all[((ControlFlow.Block)b).index];
/*     */     }
/*     */     
/*     */     abstract BasicBlock[] exits(ControlFlow.Node param1Node);
/*     */     
/*     */     abstract BasicBlock[] entrances(ControlFlow.Node param1Node); }
/*     */ 
/*     */   
/*     */   public static class Node {
/*     */     private ControlFlow.Block block;
/*     */     private Node parent;
/*     */     private Node[] children;
/*     */     
/*     */     Node(ControlFlow.Block b) {
/* 354 */       this.block = b;
/* 355 */       this.parent = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 363 */       StringBuffer sbuf = new StringBuffer();
/* 364 */       sbuf.append("Node[pos=").append(block().position());
/* 365 */       sbuf.append(", parent=");
/* 366 */       sbuf.append((this.parent == null) ? "*" : Integer.toString(this.parent.block().position()));
/* 367 */       sbuf.append(", children{");
/* 368 */       for (int i = 0; i < this.children.length; i++) {
/* 369 */         sbuf.append(this.children[i].block().position()).append(", ");
/*     */       }
/* 371 */       sbuf.append("}]");
/* 372 */       return sbuf.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ControlFlow.Block block() {
/* 378 */       return this.block;
/*     */     }
/*     */ 
/*     */     
/*     */     public Node parent() {
/* 383 */       return this.parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public int children() {
/* 388 */       return this.children.length;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node child(int n) {
/* 395 */       return this.children[n];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int makeDepth1stTree(Node caller, boolean[] visited, int counter, int[] distance, ControlFlow.Access access) {
/* 403 */       int index = this.block.index;
/* 404 */       if (visited[index]) {
/* 405 */         return counter;
/*     */       }
/* 407 */       visited[index] = true;
/* 408 */       this.parent = caller;
/* 409 */       BasicBlock[] exits = access.exits(this);
/* 410 */       if (exits != null) {
/* 411 */         for (int i = 0; i < exits.length; i++) {
/* 412 */           Node n = access.node(exits[i]);
/* 413 */           counter = n.makeDepth1stTree(this, visited, counter, distance, access);
/*     */         } 
/*     */       }
/* 416 */       distance[index] = counter++;
/* 417 */       return counter;
/*     */     }
/*     */     
/*     */     boolean makeDominatorTree(boolean[] visited, int[] distance, ControlFlow.Access access) {
/* 421 */       int index = this.block.index;
/* 422 */       if (visited[index]) {
/* 423 */         return false;
/*     */       }
/* 425 */       visited[index] = true;
/* 426 */       boolean changed = false;
/* 427 */       BasicBlock[] exits = access.exits(this);
/* 428 */       if (exits != null)
/* 429 */         for (int i = 0; i < exits.length; i++) {
/* 430 */           Node n = access.node(exits[i]);
/* 431 */           if (n.makeDominatorTree(visited, distance, access)) {
/* 432 */             changed = true;
/*     */           }
/*     */         }  
/* 435 */       BasicBlock[] entrances = access.entrances(this);
/* 436 */       if (entrances != null) {
/* 437 */         for (int i = 0; i < entrances.length; i++) {
/* 438 */           if (this.parent != null) {
/* 439 */             Node n = getAncestor(this.parent, access.node(entrances[i]), distance);
/* 440 */             if (n != this.parent) {
/* 441 */               this.parent = n;
/* 442 */               changed = true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/* 447 */       return changed;
/*     */     }
/*     */     
/*     */     private static Node getAncestor(Node n1, Node n2, int[] distance) {
/* 451 */       while (n1 != n2) {
/* 452 */         if (distance[n1.block.index] < distance[n2.block.index]) {
/* 453 */           n1 = n1.parent;
/*     */         } else {
/* 455 */           n2 = n2.parent;
/*     */         } 
/* 457 */         if (n1 == null || n2 == null) {
/* 458 */           return null;
/*     */         }
/*     */       } 
/* 461 */       return n1;
/*     */     }
/*     */     
/*     */     private static void setChildren(Node[] all) {
/* 465 */       int size = all.length;
/* 466 */       int[] nchildren = new int[size]; int i;
/* 467 */       for (i = 0; i < size; i++) {
/* 468 */         nchildren[i] = 0;
/*     */       }
/* 470 */       for (i = 0; i < size; i++) {
/* 471 */         Node p = (all[i]).parent;
/* 472 */         if (p != null) {
/* 473 */           nchildren[p.block.index] = nchildren[p.block.index] + 1;
/*     */         }
/*     */       } 
/* 476 */       for (i = 0; i < size; i++) {
/* 477 */         (all[i]).children = new Node[nchildren[i]];
/*     */       }
/* 479 */       for (i = 0; i < size; i++) {
/* 480 */         nchildren[i] = 0;
/*     */       }
/* 482 */       for (i = 0; i < size; i++) {
/* 483 */         Node n = all[i];
/* 484 */         Node p = n.parent;
/* 485 */         if (p != null) {
/* 486 */           nchildren[p.block.index] = nchildren[p.block.index] + 1; p.children[nchildren[p.block.index]] = n;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Catcher
/*     */   {
/*     */     private ControlFlow.Block node;
/*     */     private int typeIndex;
/*     */     
/*     */     Catcher(BasicBlock.Catch c) {
/* 499 */       this.node = (ControlFlow.Block)c.body;
/* 500 */       this.typeIndex = c.typeIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ControlFlow.Block block() {
/* 506 */       return this.node;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String type() {
/* 513 */       if (this.typeIndex == 0) {
/* 514 */         return "java.lang.Throwable";
/*     */       }
/* 516 */       return this.node.method.getConstPool().getClassInfo(this.typeIndex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\ControlFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */