package com.skype.headless;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.SettableFuture;
import com.google.inject.Inject;
import com.google.inject.name.Named;

class SkypeInitializer {
	private final File home;
	private boolean initRequired;
	private final Xte xte;
	private final String username;
	private final String password;
	private final DBusClient client;
	@Inject
	SkypeInitializer(@Named("skype.home") String home, Xte xte, @Named("skype.username") String username, @Named("skype.password") String password, DBusClient client) {
		this.xte = xte;
		this.username = username;
		this.password = password;
		this.client = client;
		this.home = new File(home);
		initRequired = !this.home.exists();		
	}
	public void initHome() {
		if(!initRequired)
			return;
		new File(home, ".Skype/Logs").mkdirs();
	}
	public void initSkype() {
		try {
			if(!initRequired) {
				Thread.sleep(3000);
				return;
			}		
			Thread.sleep(3000);
			xte.xte("key Tab", "sleep 1", "key Tab", "str  ", "sleep 1", "str "+username, "key Tab", "str "+password, "key Tab", "str  ", "key Tab", "sleep 1", "key Tab", "str  ");			
			Thread.sleep(10000);
			final SettableFuture<String> result = SettableFuture.create();			
			new Thread() {
				public void run() {
					result.set(client.invoke("NAME headlessskype"));					
				}
			}.start();
			Thread.sleep(1000);
			xte.xte("str  ", "key Tab", "str  ");
			if(!result.get().equals("OK"))
				throw new RuntimeException("Failed to connect to Skype through DBus: NAME headlessskype -> "+result.get());
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
}

class Xte {
	private final String display;
	@Inject
	Xte(@Named("xorg.display") String display) {
		this.display = display;
	}
	public void xte(String... commands) {
		List<String> command = new ImmutableList.Builder<String>()
			.add("xte", "-x", display)
			.add(commands)
			.build();
		try {
			new ProcessBuilder(command).inheritIO().start().waitFor();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
}
