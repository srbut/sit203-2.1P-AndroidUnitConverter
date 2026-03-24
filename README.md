# UnitConverter

A simple and intuitive Android application for converting between various units of measurement, including Currency, Fuel, and Temperature.

## Features

- **Multiple Categories**: Convert units in three main categories:
  - **Currency**: USD, AUD, EUR, JPY, and GBP (using fixed conversion rates).
  - **Fuel**: Efficiency (mpg, km/L), Volume (Gallon, Liter), and Distance (Nautical Mile, Kilometer).
  - **Temperature**: Celsius, Fahrenheit, and Kelvin.
- **Easy-to-use Interface**:
  - Category selection via dropdown.
  - Quick unit swapping with a dedicated swap button.
  - Real-time input validation (e.g., prevents negative currency values and temperature below absolute zero).
- **Formatted Output**: Results are automatically formatted based on the selected unit, including currency symbols and temperature notations.
- **Modern UI**: Supports Edge-to-Edge layout for a contemporary Android experience.

## Screenshots

<p align="center">
  <img src="screenshots/home.png" width="250"/>
  <img src="screenshots/converter.png" width="250"/>
</p>

## Getting Started

### Prerequisites

- Android Studio Flamingo or newer.
- Android SDK 34 or higher.
- A physical Android device or emulator running Android 5.0 (API level 21) or higher.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/srbut/sit203-2.1P-AndroidUnitConverter.git
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on your device or emulator.

## How to Use

1. Select a conversion category (Currency, Fuel, or Temperature) from the top spinner.
2. Choose the "From" and "To" units using the respective dropdowns.
3. Enter the value you wish to convert in the input field.
4. Tap the **Convert** button to see the result.
5. Use the **Swap** button (arrows icon) to quickly interchange the selected units.

## Technologies Used

- **Language**: Java
- **Framework**: Android SDK
- **UI Components**: XML Layouts, Material Design
- **Architecture**: Activity-based (MainActivity)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
