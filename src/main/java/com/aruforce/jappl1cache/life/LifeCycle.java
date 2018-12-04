package com.aruforce.jappl1cache.life;

/**
 * life cycle of app
 * @author Arufroce
 * @since 0.0.1
 */
public interface LifeCycle {
	/**
	 * do Jvm application start logic
	 */
	public void Start();

	/**
	 * check if is started
	 * @return
	 */
	public boolean isStart();

	/**
	 * do Jvm Application stop logic
	 */
	public void stop();
}
