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

```text
MIT License

Copyright (c) 2026 Your Name

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
