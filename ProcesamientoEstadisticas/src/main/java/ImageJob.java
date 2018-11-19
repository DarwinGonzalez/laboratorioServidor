

public class ImageJob{
   private String pathSrc;
   private String imageSrc;
   private String action;
   private String pathDst;
   private String imageDst;
   private long tsCreationMessage;
   private long tsReceptionWorker;
   private long tsFinalizationWorker;
   private String worker;
   public ImageJob(){}

   public String getAction(){
      return action;
   }
   public void setAction(String aux){
      action = aux;
   }

   public long getTsFinalizationWorker(){
      return tsFinalizationWorker;
   }
   public void setTsFinalizationWorker(long aux){
      tsFinalizationWorker = aux;
   }
   public long getTsReceptionWorker(){
      return tsReceptionWorker;
   }
   public void setTsReceptionWorker(long aux){
      tsReceptionWorker = aux;
   }
   public long getTsCreationMessage(){
      return tsCreationMessage;
   }
   public void setTsCreationMessage(long aux){
      tsCreationMessage = aux;
   }
   public String getImageDst(){
      return imageDst;
   }
   public void setImageDst(String aux){
      imageDst = aux;
   }
   public String getPathDst(){
      return pathDst;
   }
   public void setPathDst(String aux){
      pathDst = aux;
   }
   public String getImageSrc(){
      return imageSrc;
   }
   public void setImageSrc(String aux){
      imageSrc = aux;
   }
   public String getPathSrc(){
      return pathSrc;
   }
   public void setPathSrc(String aux){
      pathSrc = aux;
   }
   public String getWorker(){
      return worker;
   }
   public void setWorker(String aux){
      worker = aux;
   }

    @Override
    public String toString() {
        return "ImageJob{" + "pathSrc=" + pathSrc + ", imageSrc=" + imageSrc + ", action=" + action + ", pathDst=" + pathDst + ", imageDst=" + imageDst + ", tsCreationMessage=" + tsCreationMessage + '}';
    }
   
   
}
