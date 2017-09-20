package yealink;

import com.yealink.lib.common.AbstractYealinkApplication;

/**
 * MainApplication
 * 
 * @author liurs
 *
 */
public class MainApplication extends AbstractYealinkApplication {
    public static String code = "";
    
    @Override
    public void onCreate() {
        super.onCreate();
        NotifyService.start(this);
    }

    

}
