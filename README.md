![picture](media/galileo-80px.png)

# Galileo

Android library that helps you to debug applications directly from your android device. Just shake your device to get Galileo to work for you!

By default, Galileo shows three features: SharedPreferences edition, logcat analysis and network requests tracking. Besides that, it allows you to dump the state of your application to a txt file.

![gif](media/galileo.gif)

## Usage

It is as easy as adding this line to your onCreate Application method. In kotlin:

```kotlin
Galileo(this)
```

Or Java:

```java
new Galileo(this);
```

If you want to track your network requests you must add GalileoInterceptor to your OkHttpClient.

```kotlin
OkHttpClient.Builder()
            .addInterceptor(Galileo.interceptor)
            .build()
```

Galileo supports OkHttp versions 2.X.X and 3.X.X. In order to use it with OkHttp version 2.X.X just add this:
```kotlin
OkHttpClient.Builder()
            .addInterceptor(Galileo.interceptorOld)
            .build()
```

## Plugins

### Preferator

An adaptation from [Preferator] which shows all app preferences files and allows you to edit or delete them.

Be careful with changing third-parties preferences. Some well known third-party libraries won't be shown by default in order to avoid undesired behaviours. You can find them [here] 

Feel free to open a PR to add yours 😄

Thanks to [Sloy] for Preferator lib!!

### Lynx

A wrapper for [Lynx] which shows your adb traces. Lynx is a well-known library in Android world that allows you to filter by TraceLevel and/or by a custom text.

Thanks to [pedrovgs] for Lynx lib!!

### Chuck

An adaptation from [Chuck] which shows your HTTP requests and responses. With chuck you can track your network requests, see your backend responses and generate a share info text from request and response, or generate a curl command from that request.

Thanks to [jgilfelt] for Chuck lib!!

## Snapshot file
Galileo allows you to generate a snapshot file with your application state. This txt file could be sent by email to you or your teammates.

This file is something like this:

```text
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%   PREFERATOR   %%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

===================================================
===================================================

SAMPLEACTIVITY 


some_int --> 42
some_boolean --> true
some_string --> a string value
some_long --> 1538218852181
some_set --> [a, b, c]
some_float --> 3.14

===================================================
===================================================


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%   LYNX   %%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Trace{level=DEBUG, message='09-29 12:59:45.939 OkHttp  (21743): <-- 401 UNAUTHORIZED https://httpbin.org/basic-auth/me/pass (112ms)'}
Trace{level=DEBUG, message='09-29 12:59:45.939 OkHttp  (21743): Connection: keep-alive'}
Trace{level=DEBUG, message='09-29 12:59:45.939 OkHttp  (21743): Server: gunicorn/19.9.0'}
Trace{level=DEBUG, message='09-29 12:59:45.939 OkHttp  (21743): Date: Sat, 29 Sep 2018 10:59:44 GMT'}


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%   CHUCK   %%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

===================================================
===================================================

URL: https://httpbin.org/post
Method: POST
Protocol: http/1.1
Status: Complete
Response: 200 OK
SSL: Yes

Request time: Sat Sep 29 13:00:52 GMT+02:00 2018
Response time: Sat Sep 29 13:00:52 GMT+02:00 2018
Duration: 575 ms

Request size: 18 B
Response size: 421 B
Total size: 439 B

---------- REQUEST ----------

{
  "thing": "posted"
}

---------- RESPONSE ----------

Connection: keep-alive
Server: gunicorn/19.9.0
Date: Sat, 29 Sep 2018 11:00:51 GMT
Content-Type: application/json
Content-Length: 421
Access-Control-Allow-Origin: *
Access-Control-Allow-Credentials: true
Via: 1.1 vegur

{
  "args": {},
  "data": "{\"thing\":\"posted\"}",
  "files": {},
  "form": {},
  "headers": {
    "Accept-Encoding": "gzip",
    "Connection": "close",
    "Content-Length": "18",
    "Content-Type": "application/json; charset\u003dUTF-8",
    "Host": "httpbin.org",
    "User-Agent": "okhttp/3.11.0"
  },
  "json": {
    "thing": "posted"
  },
  "origin": "81.37.166.176",
  "url": "https://httpbin.org/post"
}

===================================================
===================================================
```

A complete example created with galileo-sample is tracked in galileo-sample/snapshot.txt.

## Add it to your project

By now we are using Jitpack to deploy Galileo artifacts. Add these lines to your build.gradle file:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
  }
```

```groovy
debugImplementation "com.github.josedlpozo.Galileo:galileo-android:0.0.4"
releaseImplementation "com.github.josedlpozo.Galileo:galileo-no-op:0.0.4"
```

## Libraries used in this project

* [Seismic]
* [Gson]
* [RxJava2]
* [Okhttp]
* [Lynx]
* [Preferator]
* [Chuck]


# License

    Copyright (C) 2018 josedlpozo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[Seismic]: https://github.com/square/seismic
[Gson]: https://github.com/google/gson
[RxJava2]: https://github.com/ReactiveX/RxJava
[Okhttp]: https://github.com/square/okhttp
[Lynx]: https://github.com/pedrovgs/Lynx
[Preferator]: https://github.com/Sloy/preferator
[Chuck]: https://github.com/jgilfelt/chuck
[Sloy]: https://github.com/Sloy
[here]: https://github.com/josedlpozo/Galileo/blob/master/galileo/src/main/java/com/josedlpozo/galileo/preferator/SdkFilter.kt
[pedrovgs]: https://github.com/pedrovgs
[jgilfelt]: https://github.com/jgilfelt