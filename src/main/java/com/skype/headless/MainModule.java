package com.skype.headless;

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class MainModule extends AbstractModule {
	private final String home;
	private final String display;
	private final String username;
	private final String password;

	private MainModule(String display, String home, String username, String password) {
		this.display = display;
		this.home = home;
		this.username = username;
		this.password = password;		
	}
	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named("xorg.display")).toInstance(display);
		bind(String.class).annotatedWith(Names.named("skype.home")).toInstance(home);
		bind(String.class).annotatedWith(Names.named("skype.username")).toInstance(username);
		bind(String.class).annotatedWith(Names.named("skype.password")).toInstance(password);
	}
	
	@Provides @Singleton
	DBusConnection dbusConnection() throws DBusException {
		return DBusConnection.getConnection(DBusConnection.SESSION);
	}
	
	@Provides
	Skype skype(DBusConnection conn) throws DBusException {
		return conn.getRemoteObject("com.Skype.API", "/com/Skype", Skype.class);
	}

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new MainModule(args[0], args[1], args[2], args[3]));
		SkypeService service = injector.getInstance(SkypeService.class);
		WebService webService = injector.getInstance(WebService.class);
		try {
			service.startAndWait();
			webService.startAndWait();			
		} finally {			
			service.stopAndWait();
			webService.stopAndWait();
			System.exit(0);
		}
	}
}
