package labrador.cse.usf.edu.projectbull;

import android.app.Application;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
public class VolleyApplicationextends Application {

    private static VolleyApplicationsInstance;
    private RequestQueuemRequestQueue;
    @Overridepublic void onCreate() {
        super.onCreate();
        mRequestQueue= Volley.newRequestQueue(this);
        sInstance= this;
    }

    public synchronized static VolleyApplication getInstance() {
        return sInstance;
    }
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}