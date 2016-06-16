# twijlio

A Twilio REST API client

## Usage

This uses the twilio rest api documented here: https://www.twilio.com/docs/api/rest

Add this to your dependencies: [[twijlio "0.1.0-SNAPSHOT"]](https://clojars.org/twijlio)

Now you can use the library:
```
(ns myapp
	(:require [twijlio :as tw]))
```

Send an SMS to (505) 555-1212 from your twilio number (888) 555-2211 with your coolest smiley face.

```clojure
(tw/send-message "+15055551212" "+18885552211" {:Body "Hey Buddy <(^_^<)"})
```

Send an MMS to (101) 555-1212 from your twilio number (999) 555-1122 with a gif of your new AI.

```clojure
(tw/send-message "+11015551212" "+19995551122" 
	{:MediaURL "https://i.imgur.com/vnvIZ.gif" :Body "Check out Tayne!"}
```

Make a call to (202) 555-1212 from your twilio number (800) 555-2121 and [say hello](https://www.twilio.com/labs/twimlets/message).

```clojure
(tw/make-call "+12025551212" "+18004442121" 
	{:Url "http://twimlets.com/message?Message%5B0%5D=Hello%2C%20World!&"})
```

## Thanks

Heavily based on [rwillig/clj-dispatch.me](https://github.com/rwillig/clj-dispatch.me). Thanks Ray!

## License

```
MIT License

Copyright (c) 2016 weekdayfabian

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
