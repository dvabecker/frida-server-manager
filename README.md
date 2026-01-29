# Frida Server Manager

Frida Server Manager is an Android application that simplifies managing a **Frida server** on a **rooted Android device**. It allows you to start and stop the Frida server directly from the device UI, without needing to connect to a computer or manually run commands via `adb`.

## Features

- Start and stop Frida server with a single tap
- No need for `adb shell` or manual terminal access
- Bundles Frida server binaries directly in the app assets
- Designed for rooted Android devices
- Simple and lightweight UI

## How It Works

The app ships with Frida server binaries included in its assets. On a rooted device, it:

1. Extracts the appropriate Frida server binary from assets
2. Sets executable permissions
3. Starts or stops the Frida server process using root privileges
4. Provides basic UI feedback for server state

All interaction is done locally on the device through the app’s interface.

## Requirements

- **Rooted Android device**
- Compatible Frida server binary for the device architecture
- Root management app (e.g. Magisk)

## Use Cases

- Mobile app reverse engineering
- Dynamic instrumentation and analysis
- Security research and testing
- Running Frida server without a PC connection

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/frida-server-manager.git
   ```
2. Open the project in Android Studio.
3. Build and install the app on a rooted device.

## Security Notice

This project is intended for research, development, and educational purposes only.
Ensure you have proper authorization before using Frida or this tool on any device or application.

## Limitations

- Root access is mandatory
- No automatic Frida server version updates
- Architecture support depends on included binaries

## Contributing

Contributions are welcome!
Feel free to open issues or submit pull requests for bug fixes, improvements, or additional features.

## License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.
