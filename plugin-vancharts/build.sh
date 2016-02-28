#!/bin/sh

#warning:before run this, to confirm the locale branch do not have files to submit

#build vanchart jar

#update git vanCharts
cd /Users/Mitisky/Documents/vanCharts

git reset

git pull

#build vancharts
cd build

node r.js -o build.js

#move vancharts js to plugin
cd /Users/Mitisky/Documents/finereport/plugin/dev/plugins/plugin-vancharts/src/com/fr/web/core/js

rm vancharts-all.js

mv /Users/Mitisky/Documents/vanCharts/build/vancharts-all.js /Users/Mitisky/Documents/finereport/plugin/dev/plugins/plugin-vancharts/src/com/fr/web/core/js

#build plugin
cd /Users/Mitisky/Documents/finereport/plugin/dev/plugins/plugin-vancharts

ant -f build.xml jar