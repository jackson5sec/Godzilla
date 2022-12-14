/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Objects;
/*    */ import kotlin.Unit;
/*    */ import kotlin.coroutines.Continuation;
/*    */ import kotlin.coroutines.CoroutineContext;
/*    */ import kotlin.jvm.JvmClassMappingKt;
/*    */ import kotlin.reflect.KCallable;
/*    */ import kotlin.reflect.KClassifier;
/*    */ import kotlin.reflect.KFunction;
/*    */ import kotlin.reflect.full.KCallables;
/*    */ import kotlin.reflect.jvm.ReflectJvmMapping;
/*    */ import kotlinx.coroutines.BuildersKt;
/*    */ import kotlinx.coroutines.CoroutineScope;
/*    */ import kotlinx.coroutines.CoroutineStart;
/*    */ import kotlinx.coroutines.Deferred;
/*    */ import kotlinx.coroutines.Dispatchers;
/*    */ import kotlinx.coroutines.GlobalScope;
/*    */ import kotlinx.coroutines.flow.Flow;
/*    */ import kotlinx.coroutines.reactor.MonoKt;
/*    */ import kotlinx.coroutines.reactor.ReactorFlowKt;
/*    */ import org.reactivestreams.Publisher;
/*    */ import reactor.core.publisher.Flux;
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
/*    */ public abstract class CoroutinesUtils
/*    */ {
/*    */   public static <T> Mono<T> deferredToMono(Deferred<T> source) {
/* 54 */     return MonoKt.mono((CoroutineContext)Dispatchers.getUnconfined(), (scope, continuation) -> source.await(continuation));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Deferred<T> monoToDeferred(Mono<T> source) {
/* 62 */     return BuildersKt.async((CoroutineScope)GlobalScope.INSTANCE, (CoroutineContext)Dispatchers.getUnconfined(), CoroutineStart.DEFAULT, (scope, continuation) -> MonoKt.awaitSingleOrNull(source, continuation));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Publisher<?> invokeSuspendingFunction(Method method, Object target, Object... args) {
/* 72 */     KFunction<?> function = Objects.<KFunction>requireNonNull(ReflectJvmMapping.getKotlinFunction(method));
/* 73 */     KClassifier classifier = function.getReturnType().getClassifier();
/*    */ 
/*    */ 
/*    */     
/* 77 */     Mono<Object> mono = MonoKt.mono((CoroutineContext)Dispatchers.getUnconfined(), (scope, continuation) -> KCallables.callSuspend((KCallable)function, getSuspendedFunctionArgs(target, args), continuation)).filter(result -> !Objects.equals(result, Unit.INSTANCE)).onErrorMap(InvocationTargetException.class, InvocationTargetException::getTargetException);
/* 78 */     if (classifier != null && classifier.equals(JvmClassMappingKt.getKotlinClass(Flow.class))) {
/* 79 */       return (Publisher<?>)mono.flatMapMany(CoroutinesUtils::asFlux);
/*    */     }
/* 81 */     return (Publisher<?>)mono;
/*    */   }
/*    */   
/*    */   private static Object[] getSuspendedFunctionArgs(Object target, Object... args) {
/* 85 */     Object[] functionArgs = new Object[args.length];
/* 86 */     functionArgs[0] = target;
/* 87 */     System.arraycopy(args, 0, functionArgs, 1, args.length - 1);
/* 88 */     return functionArgs;
/*    */   }
/*    */   
/*    */   private static Flux<?> asFlux(Object flow) {
/* 92 */     return ReactorFlowKt.asFlux((Flow)flow);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\CoroutinesUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */