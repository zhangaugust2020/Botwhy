#! /bin/bash

cd ../backendcloud/botrunningsystem/target
scp *.jar ss:~/kob/backendcloud

cd ../../backend/target
scp *.jar ss:~/kob/backendcloud

cd ../../matchingsystem/target
scp *.jar ss:~/kob/backendcloud


cd ../../../web

npm run build

scp -r ../web/dist ss:~/kob/web/
