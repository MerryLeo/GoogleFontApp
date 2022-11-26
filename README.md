# Google Font Application
<img align="right" src="assets/GoogleFontAppPreview.gif" width="25%" alt="Application Preview">

An Android native application for viewing different fonts available from the 
[Google Font Developer API](https://developers.google.com/fonts/docs/developer_api). </br>
Each font is displayed in a ViewHolder with its name, its family, and a sample which is downloaded asynchronously using a Font Provider. </br> 

This application features a login screen, register screen, reset password screen, and a font screen (main screen).
But, username and password are only saved on the phone using Shared Preferences, which is considered **unsafe**.

This project was created during one of my courses on Android mobile development for an assignment.

## Requirements
- Java SDK
- Android Studio
- An API Key for the Google Font Developer API, which you can generate via [Google Cloud Console](https://console.cloud.google.com)
- An Android Emulator (or an Android Phone with a minimum SDK of 28)

## Setup
1. Clone the repos by running `git clone https://github.com/MerryLeo/GoogleFontApp` in the directory of your choosing
2. Open the project in Android Studio
3. Add `apiKey="enter your api key here"` in **local.properties** file
4. Build the application and run it

Note if you are using a physical mobile phone, you must replace the 
font provider certificate used in `FontAdapter.kt` for `com_google.android_gms_fonts_certs_prod`
