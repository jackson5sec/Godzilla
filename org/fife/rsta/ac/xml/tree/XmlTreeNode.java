/*    */ package org.fife.rsta.ac.xml.tree;
/*    */ 
/*    */ import javax.swing.text.Position;
/*    */ import org.fife.rsta.ac.SourceTreeNode;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlTreeNode
/*    */   extends SourceTreeNode
/*    */ {
/*    */   private String mainAttr;
/*    */   private Position offset;
/*    */   private Position endOffset;
/*    */   
/*    */   public XmlTreeNode(String name) {
/* 33 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containsOffset(int offs) {
/* 38 */     return (this.offset != null && this.endOffset != null && offs >= this.offset
/* 39 */       .getOffset() && offs <= this.endOffset.getOffset());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getElement() {
/* 44 */     return (String)getUserObject();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEndOffset() {
/* 49 */     return (this.endOffset != null) ? this.endOffset.getOffset() : Integer.MAX_VALUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMainAttr() {
/* 54 */     return this.mainAttr;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getStartOffset() {
/* 59 */     return (this.offset != null) ? this.offset.getOffset() : -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEndOffset(Position pos) {
/* 64 */     this.endOffset = pos;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setMainAttribute(String attr) {
/* 69 */     this.mainAttr = attr;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setStartOffset(Position pos) {
/* 74 */     this.offset = pos;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 85 */     String text = getElement();
/* 86 */     if (this.mainAttr != null) {
/* 87 */       text = text + " " + this.mainAttr;
/*    */     }
/* 89 */     return text;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\xml\tree\XmlTreeNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */