# Morgantown Bus & PRT Tracker 
###### Official Web API repository

#### [Play Store](https://play.google.com/store/apps/details?id=com.slheavner.wvubus) | [App Store](https://itunes.apple.com/us/app/morgantown-bus-prt-tracker/id993385664?ls=1&mt=8) | [Website](http://morgantownbustracker.org) ![Alt text](https://raw.github.com/slheavner/mbt-android/master/app/src/main/res/drawable-xxxhdpi/ic_launcher.png "mbt-android logo")
-----
### mbt-api

##### Setup
The project requires a number of configuration strings to be set.

###### /conf/application.conf
Simply rename **'example.private.conf'** to **'private.conf'**, and set the values. Your values will be used, and private.conf is in the .gitignore  
[MongoDB installation guide](https://docs.mongodb.org/v3.0/installation/#installation-guides)  
```
mongo.url="mongodb://localhost/dev"
google.maps.key="browserkey" #not very important
play.crypto.secret="supersecret123"
email {
  username = "youremail@gmail.com"
  password = "yourpassword"
}
```
###### /twitter4j.properties
Simply rename **example.twitter4j.properties** to **twitter4j.properties** and add your Twitter application keys.  
[Manage Twitter apps here](https://apps.twitter.com/)
```
debug=true
oauth.consumerKey=key123
oauth.consumerSecret=key123
oauth.accessToken=key123
oauth.accessTokenSecret=key123
```

-----
##### About

This project is the Web API for the Morgantown Bus & PRT Tracker project. It was made out of a need to track the buses around Morgantown, WV, as a West Virginia University student. It was also made because *programming is fun*.

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
