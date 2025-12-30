package Common;
import net.serenitybdd.core.Serenity;
public class TestContext {
    public Object getSharedVariable(String key) {
        return Serenity.sessionVariableCalled(key);
    }
    public void setSharedVariable(String key, Object value) {
        Serenity.setSessionVariable(key).to(value);
    }
}