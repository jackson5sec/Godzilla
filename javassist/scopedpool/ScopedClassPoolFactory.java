package javassist.scopedpool;

import javassist.ClassPool;

public interface ScopedClassPoolFactory {
  ScopedClassPool create(ClassLoader paramClassLoader, ClassPool paramClassPool, ScopedClassPoolRepository paramScopedClassPoolRepository);
  
  ScopedClassPool create(ClassPool paramClassPool, ScopedClassPoolRepository paramScopedClassPoolRepository);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\scopedpool\ScopedClassPoolFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */