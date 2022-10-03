/*    */ package org.springframework.util.concurrent;
/*    */ 
/*    */ import reactor.core.publisher.Mono;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MonoToListenableFutureAdapter<T>
/*    */   extends CompletableToListenableFutureAdapter<T>
/*    */ {
/*    */   public MonoToListenableFutureAdapter(Mono<T> mono) {
/* 34 */     super(mono.toFuture());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\concurrent\MonoToListenableFutureAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */