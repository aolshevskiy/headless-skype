package com.skype.headless;

import java.util.Map;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

class SkypeService extends AbstractExecutionThreadService {
	@Override
	protected void run() throws Exception {
		System.out.println("Waiting for skype process to exit...");
		skypeProcess.waitFor();
		System.out.println("Shutting down service...");
	}

	private final String display;
	private Process skypeProcess;
	private final String home;
	private Process displayProcess;
	private final SkypeInitializer initializer;

	@Inject
	SkypeService(@Named("xorg.display") String display, @Named("skype.home") String home, SkypeInitializer initializer) {
		this.display = display;
		this.home = home;
		this.initializer = initializer;		
	}
	@Override
	protected void shutDown() throws Exception {
		skypeProcess.destroy();
		displayProcess.destroy();
	}

	@Override
	protected void startUp() throws Exception {
		//decreased resolution seems to solve troubles with window activation
		displayProcess = new ProcessBuilder("Xvfb", display, "-screen", "0", "800x600x8").inheritIO().start();
		//first init step
		initializer.initHome();
		ProcessBuilder builder = new ProcessBuilder("skype").inheritIO();
		Map<String, String> env = builder.environment();
		env.put("DISPLAY", display);
		env.put("HOME", home);
		skypeProcess = builder.start();		
		initializer.initSkype();
	}		
}
