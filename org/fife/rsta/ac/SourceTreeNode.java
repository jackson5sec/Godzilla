/*     */ package org.fife.rsta.ac;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.MutableTreeNode;
/*     */ import javax.swing.tree.TreeNode;
/*     */ import org.fife.ui.autocomplete.Util;
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
/*     */ public class SourceTreeNode
/*     */   extends DefaultMutableTreeNode
/*     */   implements Comparable<SourceTreeNode>
/*     */ {
/*     */   private boolean sortable;
/*     */   private boolean sorted;
/*     */   private Pattern pattern;
/*     */   private List<TreeNode> visibleChildren;
/*     */   private int sortPriority;
/*     */   
/*     */   public SourceTreeNode(Object userObject) {
/*  46 */     this(userObject, false);
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
/*     */   public SourceTreeNode(Object userObject, boolean sorted) {
/*  58 */     super(userObject);
/*  59 */     this.visibleChildren = new ArrayList<>();
/*  60 */     setSortable(true);
/*  61 */     setSorted(sorted);
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
/*     */   public void add(MutableTreeNode child) {
/*  74 */     if (child != null && child.getParent() == this) {
/*  75 */       insert(child, super.getChildCount() - 1);
/*     */     } else {
/*     */       
/*  78 */       insert(child, super.getChildCount());
/*     */     } 
/*  80 */     if (this.sortable && this.sorted) {
/*  81 */       refreshVisibleChildren();
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
/*     */   public Enumeration<TreeNode> children() {
/*  93 */     return Collections.enumeration(this.visibleChildren);
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
/*     */   public Object clone() {
/* 106 */     SourceTreeNode node = (SourceTreeNode)super.clone();
/*     */     
/* 108 */     node.visibleChildren = new ArrayList<>();
/* 109 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourceTreeNode cloneWithChildren() {
/* 120 */     SourceTreeNode clone = (SourceTreeNode)clone();
/* 121 */     for (int i = 0; i < super.getChildCount(); i++) {
/* 122 */       clone.add(((SourceTreeNode)super.getChildAt(i)).cloneWithChildren());
/*     */     }
/* 124 */     return clone;
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
/*     */   public int compareTo(SourceTreeNode stn2) {
/* 140 */     int res = -1;
/* 141 */     if (stn2 != null) {
/* 142 */       res = getSortPriority() - stn2.getSortPriority();
/* 143 */       if (res == 0 && ((SourceTreeNode)getParent()).isSorted()) {
/* 144 */         res = toString().compareToIgnoreCase(stn2.toString());
/*     */       }
/*     */     } 
/* 147 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void filter(Pattern pattern) {
/* 158 */     this.pattern = pattern;
/* 159 */     refreshVisibleChildren();
/* 160 */     for (int i = 0; i < super.getChildCount(); i++) {
/* 161 */       Object child = this.children.get(i);
/* 162 */       if (child instanceof SourceTreeNode) {
/* 163 */         ((SourceTreeNode)child).filter(pattern);
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
/*     */   public TreeNode getChildAfter(TreeNode child) {
/* 178 */     if (child == null) {
/* 179 */       throw new IllegalArgumentException("child cannot be null");
/*     */     }
/* 181 */     int index = getIndex(child);
/* 182 */     if (index == -1) {
/* 183 */       throw new IllegalArgumentException("child node not contained");
/*     */     }
/* 185 */     return (index < getChildCount() - 1) ? getChildAt(index + 1) : null;
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
/*     */   public TreeNode getChildAt(int index) {
/* 198 */     return this.visibleChildren.get(index);
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
/*     */   public TreeNode getChildBefore(TreeNode child) {
/* 211 */     if (child == null) {
/* 212 */       throw new IllegalArgumentException("child cannot be null");
/*     */     }
/* 214 */     int index = getIndex(child);
/* 215 */     if (index == -1) {
/* 216 */       throw new IllegalArgumentException("child node not contained");
/*     */     }
/* 218 */     return (index > 0) ? getChildAt(index - 1) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getChildCount() {
/* 229 */     return this.visibleChildren.size();
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
/*     */   public int getIndex(TreeNode child) {
/* 243 */     if (child == null) {
/* 244 */       throw new IllegalArgumentException("child cannot be null");
/*     */     }
/* 246 */     for (int i = 0; i < this.visibleChildren.size(); i++) {
/* 247 */       TreeNode node = this.visibleChildren.get(i);
/* 248 */       if (node.equals(child)) {
/* 249 */         return i;
/*     */       }
/*     */     } 
/* 252 */     return -1;
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
/*     */   public int getSortPriority() {
/* 264 */     return this.sortPriority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSortable() {
/* 275 */     return this.sortable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSorted() {
/* 285 */     return this.sorted;
/*     */   }
/*     */ 
/*     */   
/*     */   public void refresh() {
/* 290 */     refreshVisibleChildren();
/* 291 */     for (int i = 0; i < getChildCount(); i++) {
/* 292 */       TreeNode child = getChildAt(i);
/* 293 */       if (child instanceof SourceTreeNode) {
/* 294 */         ((SourceTreeNode)child).refresh();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void refreshVisibleChildren() {
/* 305 */     this.visibleChildren.clear();
/* 306 */     if (this.children != null) {
/* 307 */       this.visibleChildren.addAll(this.children);
/* 308 */       if (this.sortable && this.sorted) {
/* 309 */         this.visibleChildren.sort(null);
/*     */       }
/* 311 */       if (this.pattern != null) {
/* 312 */         for (Iterator<TreeNode> i = this.visibleChildren.iterator(); i.hasNext(); ) {
/* 313 */           TreeNode node = i.next();
/* 314 */           if (node.isLeaf()) {
/* 315 */             String text = node.toString();
/* 316 */             text = Util.stripHtml(text);
/* 317 */             if (!this.pattern.matcher(text).find())
/*     */             {
/* 319 */               i.remove();
/*     */             }
/*     */           } 
/*     */         } 
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
/*     */   public void setSortable(boolean sortable) {
/* 337 */     this.sortable = sortable;
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
/*     */   public void setSorted(boolean sorted) {
/* 349 */     if (sorted != this.sorted) {
/*     */ 
/*     */       
/* 352 */       this.sorted = sorted;
/*     */       
/* 354 */       if (this.sortable) {
/* 355 */         refreshVisibleChildren();
/*     */       }
/*     */       
/* 358 */       for (int i = 0; i < super.getChildCount(); i++) {
/* 359 */         Object child = this.children.get(i);
/* 360 */         if (child instanceof SourceTreeNode) {
/* 361 */           ((SourceTreeNode)child).setSorted(sorted);
/*     */         }
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
/*     */   public void setSortPriority(int priority) {
/* 376 */     this.sortPriority = priority;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\SourceTreeNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */