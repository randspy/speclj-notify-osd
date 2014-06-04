# speclj-notify-osd

[![Build Status](https://travis-ci.org/randspy/speclj-notify-osd.svg?branch=master)](http://travis-ci.org/randspy/speclj-notify-osd)

speclj-notify-osd is a plugin for [speclj](http://speclj.com/) that shows success and failure messages with [notify-osd](https://wiki.ubuntu.com/NotifyOSD).
It is based on [speclj-growl](https://github.com/pgr0ss/speclj-growl).

## Installation

If you use [leiningen](https://github.com/technomancy/leiningen), add the following to your project.clj under the :dev profile::

    :dependencies [[speclj-notify-osd "0.0.1"]]

Speclj 2.7.1 or newer is required.

## Usage

Add `-f notify-osd` to lein spec to show output in notify-osd. For example, this will start autotest with both terminal and notify-osd output:

    lein spec -a -f notify-osd

This is short for:

    lein spec -r vigilant -f documentation -f notify-osd

## License

Copyright (C) 2014 Przemyslaw Koziel

Distributed under the The MIT License.
