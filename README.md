# MessOrganizer

Organizes the mess.

## Troubleshooting

### Windows

If got the following output:

	java.util.prefs.WindowsPreferences <init>
	WARNING: Could not open/create prefs root node Software\JavaSoft\Prefs 
	at root 0x80000002. Windows RegCreateKeyEx(...) returned error code 5.

__Solution:__

1. Go into Start Menu and type `regedit` into the search field.
1. Navigate to path `HKEY_LOCAL_MACHINE\Software\JavaSoft` (Windows 10 seems to now have this here: `HKEY_LOCAL_MACHINE\Software\WOW6432Node\JavaSoft`)
1. Right click on the `JavaSoft` folder and click on `New` -> `Key`
1. Name the new Key `Prefs` and everything should work.


Alternatively, save and execute a `*.reg` file with the following content:

	Windows Registry Editor Version 5.00
	[HKEY_LOCAL_MACHINE\Software\JavaSoft\Prefs]


*Source:*

[stackoverflow.com](https://stackoverflow.com/questions/16428098/groovy-shell-warning-could-not-open-create-prefs-root-node)


## License

The code is released under the [MIT License](LICENSE "LICENSE").
