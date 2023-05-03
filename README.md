## Version 4.0 caa 15/12/2019

## General info
ORD Counter is a minimal designed Operationally Ready Date (ORD) Countdown application for Singapore's Full-Time National Servicemen. Its main features include an ORD date calculator, ORD date countdown, Leave/OFF quota manager, and Payday countdown.
	
## Technologies
Project is created with:
* Java
* Android Studio

## Google Play Store Link
https://play.google.com/store/apps/details?id=com.lifeisaparty.ordcounter

**Updates:**
* Added Payday Counter
* Changed background colour to fit dark mode (#152642)
* Changed font/icon colour to white
* Rounded Linearlayout for a professional look
* Slight changes to Settings Activity UI (layouts)
* Added Banner Ads with test device configured (Removed (Commented) due to lack of response)
* Added percentage towards civilian life
* Added Logo
* Changed package name to 'ordcounter'
* Cleaned UI
* Changed ORD Date format on main activity
* Revamped UI (Uses Constraint layouts instead of relative layouts for more flexibility and caters to different screen sizes)
* Added percentage progress bar, changed design and updated colour
* Added option to choose payday (10th/12th)
* Added Working days
* Cleaned Code

## Bugs fixed
* Disallowed backup in Manifest.xml as it was affecting reinstallation
* Resized Logo down to below 2048x2048 due to hardware acceleration not displaying imageview correctly
