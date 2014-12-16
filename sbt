#!/bin/sh
java -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar ./sbt-launch.jar "$@"