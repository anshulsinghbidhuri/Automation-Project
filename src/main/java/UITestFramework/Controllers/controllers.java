package UITestFramework.Controllers;

public interface controllers {
    String namasteTalk="http://localhost:5001";


    String getPath();
    String getModulePath();
    String version();

    default String chatProject() {return namasteTalk+ "/" + getModulePath() + "/" + getPath();}
}
