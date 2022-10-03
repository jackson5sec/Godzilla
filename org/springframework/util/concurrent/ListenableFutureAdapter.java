/*    */ package org.springframework.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ListenableFutureAdapter<T, S>
/*    */   extends FutureAdapter<T, S>
/*    */   implements ListenableFuture<T>
/*    */ {
/*    */   protected ListenableFutureAdapter(ListenableFuture<S> adaptee) {
/* 42 */     super(adaptee);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addCallback(ListenableFutureCallback<? super T> callback) {
/* 48 */     addCallback(callback, callback);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addCallback(final SuccessCallback<? super T> successCallback, final FailureCallback failureCallback) {
/* 53 */     ListenableFuture<S> listenableAdaptee = (ListenableFuture<S>)getAdaptee();
/* 54 */     listenableAdaptee.addCallback(new ListenableFutureCallback<S>()
/*    */         {
/*    */           public void onSuccess(@Nullable S result) {
/* 57 */             T adapted = null;
/* 58 */             if (result != null) {
/*    */               try {
/* 60 */                 adapted = ListenableFutureAdapter.this.adaptInternal(result);
/*    */               }
/* 62 */               catch (ExecutionException ex) {
/* 63 */                 Throwable cause = ex.getCause();
/* 64 */                 onFailure((cause != null) ? cause : ex);
/*    */                 
/*    */                 return;
/* 67 */               } catch (Throwable ex) {
/* 68 */                 onFailure(ex);
/*    */                 return;
/*    */               } 
/*    */             }
/* 72 */             successCallback.onSuccess(adapted);
/*    */           }
/*    */           
/*    */           public void onFailure(Throwable ex) {
/* 76 */             failureCallback.onFailure(ex);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\concurrent\ListenableFutureAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */