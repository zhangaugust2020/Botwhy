#! /bin/bash

cd ../backendcloud/botrunningsystem/target
scp *.jar z_kob:~/kob/backendcloud

cd ../../backend/target
scp *.jar z_kob:~/kob/backendcloud

cd ../../matchingsystem/target
scp *.jar z_kob:~/kob/backendcloud


cd ../../../web

npm run build

scp -r ../web/dist z_kob:~/kob/web/
