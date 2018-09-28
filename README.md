![picture](media/galileo-80px.png)

# Galileo

Galileo is a simple Android library to debug applications directly from the phone you are testing. You just have to shake your phone, and Galileo will start :smiley:

It is mostly developed in Kotlin.

Galileo allows you to generate a logs txt file with the state of your application. 

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

## Plugins

### Preferator

An adaptation from [Preferator] [6] which shows all app preferences files and allows you to update or delete them.

## Lynx

A wrapper for [Lynx] [5] which shows your adb traces.

## Chuck

An adaptation from [Chuck] [7] which shows your HTTP requests and responses. It allows you to generate a share info text from request and response, or generate a curl command from that request. 

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

* [Seismic] [1]
* [Gson] [2]
* [RxJava2] [3]
* [Okhttp] [4]
* [Lynx] [5]
* [Preferator] [6]
* [Chuck] [7]


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
    
[1]: https://github.com/square/seismic
[2]: https://github.com/google/gson
[3]: https://github.com/ReactiveX/RxJava
[4]: https://github.com/square/okhttp
[5]: https://github.com/pedrovgs/Lynx
[6]: https://github.com/Sloy/preferator
[7]: https://github.com/jgilfelt/chuck