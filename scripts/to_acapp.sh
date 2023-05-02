#! /bin/bash


# find ../dist/js/ -type f -path "*.js" | xargs sed -i 's:被替换的内容:新的内容:g'
#find ../acapp//dist/js/ -type f -path "*.js" | xargs sed -i 's:(function(){var e={:const myfunc = (function(myappid, AcWingOS){var e={:g'
#find ../acapp/dist/js/ -type f -path "*.js" | xargs sed -i 's:.mount("#app"):.mount(myappid):g'
#find ../acapp/dist/js/ -type f -path "*.js" | xargs sed -i 's:()})();:()});:g'
#echo "
#export class Game {
#    constructor(id, AcWingOS) {
#        myfunc('#' + id, AcWingOS);
#    }
#}
#" >> ../acapp/dist/js/*.js

scp ../acapp/dist/js/*.js kob:~/kob/acapp/app.js

scp ../acapp/dist/css/*.css kob:~/kob/acapp/app.css
