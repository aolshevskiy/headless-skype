package com.skype.headless;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
class DBusClient {
	private final Skype skype;

	@Inject
	DBusClient(Skype skype) {
		this.skype = skype;
	}
	
	String invoke(String command) {
		return skype.Invoke(command);
	}
}

@DBusInterfaceName("com.Skype.API")
interface Skype extends DBusInterface {
	String Invoke(String command);
}