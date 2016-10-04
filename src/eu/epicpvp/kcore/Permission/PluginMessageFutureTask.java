package eu.epicpvp.kcore.Permission;

import eu.epicpvp.datenclient.client.Callback;
import dev.wolveringer.thread.ThreadFactory;
import eu.epicpvp.kcore.Util.UtilServer;

public class PluginMessageFutureTask<T> {

	private int timeout = 5000;
	private int sleep = 25;
	private T response = null;
	private RuntimeException exception;

	public void done(T response) {
		this.response = response;
	}

	protected void done(RuntimeException e) {
		this.exception = e;
	}

	public boolean haveResponse() {
		return haveResponse();
	}

	public T getSync() {
		return getSyncSave();
	}

	public T getSyncSave() throws RuntimeException {
		return getSyncSave(timeout);
	}

	public T getSync(int timeout) {
		return getSyncSave(timeout);
	}

	public T getSyncSave(int timeout) throws RuntimeException {
		if(UtilServer.isMainthread())
			throw new RuntimeException("PluginMessageFutureTask called sync!");
		long start = System.currentTimeMillis();
		while (response == null) {
			if (exception != null)
				throw exception;
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (start + timeout < System.currentTimeMillis()) {
				throw new RuntimeException("Timeout");
			}
		}
		return response;
	}

	public void getAsync(Callback<T> call) {
		getAsync(call, timeout);
	}

	public void getAsync(Callback<T> call, int timeout) {
		ThreadFactory.getFactory().createThread(new Runnable() {
			@Override
			public void run() {
				T out = null;
				try {
					out = getSyncSave(timeout);
				} catch (Exception e) {

				}
				call.call(out,null);
			}
		}).start();
	}

}
