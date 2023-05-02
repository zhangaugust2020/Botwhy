#! /bin/bash

cd ../web

npm run build

scp -r ../web/dist ss:~/kob/web/
