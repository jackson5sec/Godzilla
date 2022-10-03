/*    */ package javassist.bytecode.analysis;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ public class Subroutine
/*    */ {
/* 31 */   private List<Integer> callers = new ArrayList<>();
/* 32 */   private Set<Integer> access = new HashSet<>();
/*    */   private int start;
/*    */   
/*    */   public Subroutine(int start, int caller) {
/* 36 */     this.start = start;
/* 37 */     this.callers.add(Integer.valueOf(caller));
/*    */   }
/*    */   
/*    */   public void addCaller(int caller) {
/* 41 */     this.callers.add(Integer.valueOf(caller));
/*    */   }
/*    */   
/*    */   public int start() {
/* 45 */     return this.start;
/*    */   }
/*    */   
/*    */   public void access(int index) {
/* 49 */     this.access.add(Integer.valueOf(index));
/*    */   }
/*    */   
/*    */   public boolean isAccessed(int index) {
/* 53 */     return this.access.contains(Integer.valueOf(index));
/*    */   }
/*    */   
/*    */   public Collection<Integer> accessed() {
/* 57 */     return this.access;
/*    */   }
/*    */   
/*    */   public Collection<Integer> callers() {
/* 61 */     return this.callers;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 66 */     return "start = " + this.start + " callers = " + this.callers.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\Subroutine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */