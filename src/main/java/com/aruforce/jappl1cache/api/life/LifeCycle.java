package com.aruforce.jappl1cache.api.life;

/**
 * life cycle of app
 * @author Arufroce
 * @since 0.0.1
 */
public interface LifeCycle {
	/**
	 * do start logic
	 */
	public void Start();

	/**
	 * check if is start
	 * @return
	 */
	public boolean isStart();

	/**
	 * do stop logic
	 */
	public void stop();
}
