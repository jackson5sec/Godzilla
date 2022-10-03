package javassist;

public interface Translator {
  void start(ClassPool paramClassPool) throws NotFoundException, CannotCompileException;
  
  void onLoad(ClassPool paramClassPool, String paramString) throws NotFoundException, CannotCompileException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\Translator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */