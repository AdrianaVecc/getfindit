find it
=======


find it is a nifty little app for Google Glass that acts as your virtual memory. It remembers where your things are, simply by taking a picture.


# Remebering things

Say 'ok glass, remember this'. find it then asks you what is the item you want to save, opens the camera for you and saves the location data.

# Finding these things later

Simply ask 'ok glass find' followed by the name of your thing, and we'll find it for you.
/!\ You would have to call it by the exact same name. For instance, if you saved your car under "car", it would not work if you say "my car", "my red car" or "the Ferrari".

# Installing

Push the findit.apk to Glass via adb.

1. Activate the debugg mode on on Glass (Swipe all the way to the left > Settings > Device Info > Turn on debug)

2. Install the Android Developer Tools

3. Connect Glass to computer via USB

4. From the terminal, run the following command lines:

adb install -r findit.apk

* add ./ in front of 'adb' if you're using a Mac.

Happy Finding!

Adriana
adriana@getfind.it