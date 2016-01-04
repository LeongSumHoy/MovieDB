package ot.sh.com.moviedb.sync;

/**
 * Created by brandon on 28/12/2015.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Define a Service that returns an IBinder for the
 * sync adapter class, allowing the sync adapter framework to call
 * onPerformSync().
 */
public class MovieSyncService extends Service {
    // Storage for an instance of the sync adapter
    private static MovieSyncAdapter mSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object mSyncAdapterLock = new Object();

    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (mSyncAdapterLock) {
            if (mSyncAdapter == null) {
                mSyncAdapter = new MovieSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     */
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return mSyncAdapter.getSyncAdapterBinder();
    }
}