# Morgantown Bus & PRT Tracker 
###### Official Web API repository

#### [Play Store](https://play.google.com/store/apps/details?id=com.slheavner.wvubus) | [App Store](https://itunes.apple.com/us/app/morgantown-bus-prt-tracker/id993385664?ls=1&mt=8) | [Website](http://morgantownbustracker.org) ![Alt text](https://raw.github.com/slheavner/mbt-android/master/app/src/main/res/drawable-xxxhdpi/ic_launcher.png "mbt-android logo")
-----
### mbt-api

##### Setup
The project requires a number of configuration strings to be set.

###### /conf/application.conf
All of these settings will be overridden by an environment variable if it is present. 
Windows: 
```
play.crypto.secret="changeme" #application secret, doesn't need to be set unless in production
play.crypto.secret=${?APPLICATION_SECRET}

google.maps.key="webapikey" #Google Maps Browser key
google.maps.key=${?GOOGLEMAPS_KEY}

email.username="username@gmail.com"  #Gmail account for mailer
email.username=${?EMAIL_USERNAME}

email.password="password"  #Password for gmail account
email.password=${?EMAIL_PASSWORD}
```

If you would like to contribute before I get the new api project on github, message me and I will give you a url to use for the api. 

-----
##### About

This project is the Android app for the Morgantown Bus & PRT Tracker project. It was made out of a need to track the buses around Morgantown, WV, as a West Virginia University student. It was also made because *programming is fun*.

-----
##### License
```
Copyright 2015 Samuel Heavner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

